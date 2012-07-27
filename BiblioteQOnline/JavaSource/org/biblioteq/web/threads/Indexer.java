//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.threads
 * File: Indexer.java
 * 
 * #######################
 * #   GNU DISCLAIMER    #
 * ####################### 
 * BiblioteQ Online is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version. BiblioteQ Online is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see http://www.gnu.org/licenses/.
 * 
 * #######################
 * #       Purpose       #
 * #######################
 * The Indexer is a multi-threaded object that clears and recreates the Search Index. It reports its progress back to the object that created it by means of a reference to an
 * Integer object.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jun 8, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.threads;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.biblioteq.ejb.interfaces.IndexBusinessLocal;
import org.biblioteq.ejb.interfaces.Item;
import org.biblioteq.web.common.Constants;
import org.biblioteq.web.interfaces.IndexerCallback;

/**
 * The Indexer is a multi-threaded object that clears and recreates the Search Index. It reports its progress back to the object that created it by means of a reference to an
 * Integer object.
 * 
 * @author Clint Bush
 * 
 */
public class Indexer implements Runnable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(Indexer.class);
	
	/**
	 * IndexBusiness EJB.
	 */
	private IndexBusinessLocal indexEjb;
	
	// We'll store a reference to the backing bean's copy of this variable
	private Integer recordsIndexed = null;
	
	// Lucene Index Writer
	IndexWriterConfig indexWriterConfig = null;
	File indexDirectoryFile = null;
	Directory indexDirectory = null;
	IndexWriter indexWriter = null;
	
	// IndexerCallback object (usually the object using this class)
	IndexerCallback indexerCallback;
	
	/**
	 * Create a new copy of Indexer.
	 * 
	 * @param indexerCallback
	 *            (IndexerCallback) Reference to the class that created this object and implements IndexerCallback interface.
	 * @param recordsIndexed
	 *            (Integer) Reference to the actual recordsIndexed variable.
	 * @param indexEjb
	 *            (IndexBusinessLocal) The EJB that will be used for querying the database for batches of records to index.
	 * @param deplDirectory
	 *            (String) The filepath to the deployment directory of this JSF application. This will be used to create the directory to store the index.
	 */
	public Indexer(IndexerCallback indexerCallback, IndexBusinessLocal indexEjb, String deplDirectory)
	{
		// Initialize Variables
		this.indexerCallback = indexerCallback;
		
		// Initialize Lucene configuration
		this.indexDirectoryFile = new File(deplDirectory + Constants.PATH_DIR_SEARCH_INDEX + File.separator);
		Indexer.log.info("Index Directory: " + deplDirectory + Constants.PATH_DIR_SEARCH_INDEX + File.separator);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		// Declare the variables we'll be using
		int batchCount = 0;
		this.recordsIndexed = 0;
		
		// Initialize the Index EJB
		try
		{
			this.indexEjb = (IndexBusinessLocal) new InitialContext()
			        .lookup("java:global/BiblioteQEAR/BiblioteQEJB/IndexBusiness!org.biblioteq.ejb.interfaces.IndexBusinessLocal");
		}
		catch (NamingException e)
		{
			Indexer.log.error("Error getting instance of IndexBusinessLocal");
			e.printStackTrace();
			return;
		}
		
		// Allow the page time to update
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			Indexer.log.error("Interrupt occurred while sleeping.");
			e.printStackTrace();
		}
		
		// Prepare the Index Directory and Index Writer
		try
		{
			// Create the directory if it does not already exist
			if (!(this.indexDirectoryFile.exists()))
			{
				this.indexDirectoryFile.mkdir();
			}
			
			// Open it for indexing
			this.indexDirectory = FSDirectory.open(this.indexDirectoryFile);
			
			// Create the IndexWriter that will write the index to the directory
			this.indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36, new StopAnalyzer(Version.LUCENE_36));
			this.indexWriter = new IndexWriter(this.indexDirectory, this.indexWriterConfig);
			
			// Clean the index
			this.indexWriter.deleteAll();
			this.indexWriter.commit();
		}
		catch (CorruptIndexException e)
		{
			Indexer.log.error("Corrupt Index while trying to prepare the Index Directory and Index Writer");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			Indexer.log.error("IOException thrown while trying to prepare the Index Directory and Index Writer.");
			e.printStackTrace();
		}
		
		// Cycle through the batches to perform indexing
		List<Item> nextBatch = null;
		while ((nextBatch = this.indexEjb.getNextBatch()) != null)
		{
			batchCount++;
			
			// Index each item in the batch
			for (Item nextItem : nextBatch)
			{
				// Create a new Index document
				Document newDocument = new Document();
				
				newDocument.add(new Field(Constants.SEARCH_DOCUMENT_FIELD_TYPE, nextItem.getType(), Store.YES, Index.NOT_ANALYZED));
				newDocument.add(new Field(Constants.SEARCH_DOCUMENT_FIELD_ID, nextItem.getId(), Store.YES, Index.NOT_ANALYZED));
				newDocument.add(new Field(Constants.SEARCH_DOCUMENT_FIELD_OID, String.valueOf(nextItem.getItemId()), Store.YES, Index.NO));
				newDocument.add(new Field(Constants.SEARCH_DOCUMENT_FIELD_TITLE, nextItem.getTitle(), Store.NO, Index.ANALYZED));
				newDocument.add(new Field(Constants.SEARCH_DOCUMENT_FIELD_CREATOR, nextItem.getCreator(), Store.NO, Index.ANALYZED));
				
				// Give Category field a slightly lower score so Title holds more weight
				Field categoryField = new Field(Constants.SEARCH_DOCUMENT_FIELD_CATEGORY, nextItem.getCategories(), Store.NO,
				        Index.ANALYZED);
				categoryField.setBoost(0.9f);
				newDocument.add(categoryField);
				
				newDocument.add(new Field(Constants.SEARCH_DOCUMENT_FIELD_PUBLISHER, nextItem.getCompany(), Store.NO, Index.ANALYZED));
				
				// Give the Description field a slightly lower score so Title holds more weight
				Field descriptionField = new Field(Constants.SEARCH_DOCUMENT_FIELD_DESCRIPTION, nextItem.getAbstract(), Store.NO,
				        Index.ANALYZED);
				descriptionField.setBoost(0.9f);
				newDocument.add(descriptionField);
				
				// Add newDocument to the index
				try
				{
					this.indexWriter.addDocument(newDocument);
				}
				catch (CorruptIndexException e)
				{
					Indexer.log.error("Corrupt Index while trying to add new document to Search Index.");
					e.printStackTrace();
				}
				catch (IOException e)
				{
					Indexer.log.error("IOExceptin while tryign to add new document to Search Index.");
					e.printStackTrace();
				}
				
				// Increment the records indexed count
				this.recordsIndexed++;
				this.indexerCallback.updateProgress(this.recordsIndexed);
				
				// All the page time to update
				try
				{
					Thread.sleep(10);
				}
				catch (InterruptedException e)
				{
					Indexer.log.error("Interruption occurred while indexing.");
					e.printStackTrace();
				}
			}
			
			// Pause to allow other processes time
			try
			{
				Thread.sleep(10);
			}
			catch (InterruptedException e)
			{
				Indexer.log.error("Interruption occurred while indexing.");
				e.printStackTrace();
			}
		}
		
		// Now that we're done, close the index writer
		try
		{
			Indexer.log.info("Indexing complete...");
			this.indexWriter.commit();
			this.indexWriter.close(true);
			this.indexDirectory.close();
		}
		catch (CorruptIndexException e)
		{
			Indexer.log.error("Corrupt Index while trying to commit and close the IndexWriter.");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			Indexer.log.error("IOException while trying to commit and close the IndexWriter.");
			e.printStackTrace();
		}
		
		// Notify the object using this class that indexing is complete
		this.indexerCallback.indexingComplete();
	}
}
