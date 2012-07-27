//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.backing
 * File: A_Settings_Search_Indexer.java
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
 * This is an Application Scoped bean that provides a Singleton-style access point for creating the Search Index.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jun 7, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.backing;

import java.io.File;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.interfaces.IndexBusinessLocal;
import org.biblioteq.ejb.interfaces.SettingBusinessLocal;
import org.biblioteq.web.common.Constants;
import org.biblioteq.web.interfaces.IndexerCallback;
import org.biblioteq.web.threads.Indexer;

/**
 * This is an Application Scoped bean that provides a Singleton-style access point for creating the Search Index.
 * 
 * @author Clint Bush
 * 
 */
@ManagedBean(name = "A_Settings_Search_Indexer", eager = true)
@ApplicationScoped
public class A_Settings_Search_Indexer implements IndexerCallback
{
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(A_Settings_Search_Indexer.class);
	
	/**
	 * Get a Reference to the Settings EJB.
	 */
	@EJB(name = "SettingBusiness")
	private SettingBusinessLocal settingEjb;
	
	/*
	 * These will back the progress bar.
	 */
	private int recordsIndexed = 0;
	private long recordsTotal = 10;
	private boolean indexingCommenced = false;
	
	/**
	 * IndexBusiness EJB.
	 */
	private IndexBusinessLocal indexEjb;
	
	/**
	 * The Indexer is a multi-threaded object that clears and recreates the search index.
	 */
	private Indexer indexer = new Indexer(this, this.indexEjb, File.separator);
	
	/**
	 * The Action Listener method that handles clicking the Index button.
	 * 
	 * @param e
	 *            (ActionEvent) Object generated when the UI element using this method (the Index button) is activated.
	 */
	public String commenceIndexing()
	{
		this.init();
		
		if (!(this.indexingCommenced) && !(this.settingEjb.getBooleanSettingByName(Constants.SETTING_SEARCH_INDEXING_COMMENCED)))
		{
			// Set the total records to be indexed
			this.recordsTotal = (int) this.indexEjb.getTotalRecords();
			
			// Update the Indexing Commenced Setting
			this.settingEjb.saveSetting(Constants.SETTING_SEARCH_INDEXING_COMMENCED, "true");
			
			// Start the indexing process
			(new Thread(this.indexer)).start();
			
			// Set indexingCommenced to true
			this.indexingCommenced = true;
		}
		
		return "";
	}
	
	/**
	 * Returns a String representation of the percentage of records indexed.
	 * 
	 * @return (String) Percentage of records indexed.
	 */
	public String getProgress()
	{
		if (this.recordsTotal < 1)
		{
			return "0";
		}
		return String.valueOf((int) ((float) this.recordsIndexed / (float) this.recordsTotal * 100));
	}
	
	/**
	 * Returns the number of records currently Indexed.
	 * 
	 * @return (int) The recordsIndexed.
	 */
	public Integer getRecordsIndexed()
	{
		return this.recordsIndexed;
	}
	
	/**
	 * Returns the total number of records to be indexed.
	 * 
	 * @return (Long) The recordsTotal.
	 */
	public Long getRecordsTotal()
	{
		return this.recordsTotal;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.web.interfaces.IndexerCallback#indexingComplete()
	 */
	@Override
	public void indexingComplete()
	{
		// Reset Local Values
		this.recordsIndexed = 0;
		this.setIndexingCommenced(false);
		
		// Update Settings
		this.settingEjb.saveSetting(Constants.SETTING_SEARCH_LAST_INDEXING, String.valueOf((new Date()).getTime()));
		this.settingEjb.saveSetting(Constants.SETTING_SEARCH_INDEXING_COMMENCED, "false");
		
		// Reset the Indexing Session Bean
		this.indexEjb.reset();
	}
	
	@PostConstruct
	public void init()
	{
		// Initialize the Index EJB
		try
		{
			this.indexEjb = (IndexBusinessLocal) new InitialContext()
			        .lookup("java:global/BiblioteQEAR/BiblioteQEJB/IndexBusiness!org.biblioteq.ejb.interfaces.IndexBusinessLocal");
		}
		catch (NamingException e)
		{
			A_Settings_Search_Indexer.log.error("Error getting instance of IndexBusinessLocal");
			e.printStackTrace();
		}
		
		// Set the total records to be indexed
		this.recordsTotal = this.indexEjb.getTotalRecords();
	}
	
	/**
	 * Returns true if Indexing is in progress.
	 * 
	 * @return (boolean) The indexingCommenced.
	 */
	public boolean isIndexingCommenced()
	{
		return this.indexingCommenced;
	}
	
	/**
	 * Sets the IndexingCommenced variable
	 * 
	 * @param indexingCommenced
	 *            the indexingCommenced to set
	 */
	public void setIndexingCommenced(boolean indexingCommenced)
	{
		this.indexingCommenced = indexingCommenced;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.web.interfaces.IndexerCallback#updateProgress(int)
	 */
	@Override
	public void updateProgress(int recordsIndexed)
	{
		this.recordsIndexed = recordsIndexed;
	}
}
