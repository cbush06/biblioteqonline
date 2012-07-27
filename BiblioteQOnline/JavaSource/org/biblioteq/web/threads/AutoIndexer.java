//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.threads
 * File: AutoIndexer.java
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
 *
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jul 1, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.threads;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.interfaces.IndexBusinessLocal;
import org.biblioteq.ejb.interfaces.SettingBusinessLocal;
import org.biblioteq.web.common.Constants;
import org.biblioteq.web.interfaces.IndexerCallback;

/**
 * @author Clint Bush
 * 
 */
public class AutoIndexer implements Runnable, IndexerCallback
{
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(AutoIndexer.class);
	
	/**
	 * Get a reference to the Index EJB.
	 */
	private IndexBusinessLocal indexEjb;
	
	/**
	 * Get a reference to the Setting EJB.
	 */
	private SettingBusinessLocal settingEjb;
	
	/**
	 * Hold a flag that indicates if an Index is complete.
	 */
	private boolean indexingComplete = true;
	
	/**
	 * Create our Indexer Thread.
	 */
	private Indexer indexer = new Indexer(this, this.indexEjb, File.separator);
	
	/**
	 * A flag that indicates if the Thread should still be alive.
	 */
	private boolean threadAlive = true;
	
	/**
	 * Constructor for AutoIndexer. Requires the IndexBusiness and SettingBusiness EJBs be provided (presumably by the ServletContextListener).
	 * 
	 * @param indexEjb
	 *            (IndexBusinessLocal) Index EJB.
	 * @param settingEjb
	 *            (SettingBusinessLocal) Setting EJB.
	 */
	public AutoIndexer(IndexBusinessLocal indexEjb, SettingBusinessLocal settingEjb)
	{
		this.indexEjb = indexEjb;
		this.settingEjb = settingEjb;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.web.interfaces.IndexerCallback#indexingComplete()
	 */
	@Override
	public void indexingComplete()
	{
		// Set the indexing complete flag
		this.indexingComplete = true;
		
		// Unset the Indexing Commenced Setting
		this.settingEjb.saveSetting(Constants.SETTING_SEARCH_INDEXING_COMMENCED, "false");
		
		// Update the Last Indexed Setting
		this.settingEjb.saveSetting(Constants.SETTING_SEARCH_LAST_INDEXING, String.valueOf((new Date()).getTime()));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		boolean autoIndexing = false;
		String frequency = "";
		long autoFreqInMilli = 0L;
		long differenceInMilli = 0L;
		
		// Run forever
		while (true)
		{
			// Get the on/off setting for Auto-Indexing
			autoIndexing = this.settingEjb.getBooleanSettingByName(Constants.SETTING_SEARCH_AUTO_INDEXING);
			
			if (autoIndexing)
			{
				// Get the Frequency setting
				frequency = this.settingEjb.getStringSettingByName(Constants.SETTING_SEARCH_AUTO_INDEXING_FREQ);
				
				// Find out the millisecond difference between now and the last time an Index was done
				differenceInMilli = (new Date()).getTime()
				        - Long.parseLong(this.settingEjb.getStringSettingByName(Constants.SETTING_SEARCH_LAST_INDEXING));
				
				// Get milliseconds for # hours (i.e. 43200000 = 1000 * 60 * 60 * 12hrs) where 12 hrs is bi-daily
				autoFreqInMilli = 3600000L;
				
				//@formatter:off
				if(frequency.equals("daily"))
				{
					autoFreqInMilli *= 24; // Every 1 day (1 * 24 = 24)
				}
				else if(frequency.equals("bi-daily"))
				{
					autoFreqInMilli *= 12; // Every .5 days (.5 * 24 = 12) 
				}
				else if(frequency.equals("weekly"))
				{
					autoFreqInMilli *= 168; // Every 7 days (7 * 24 = 168)
				}
				else if(frequency.equals("bi-weekly"))
				{
					autoFreqInMilli *= 84; // Every 3.5 days (3.5 * 24 = 84)
				}
				else if(frequency.equals("monthly"))
				{
					autoFreqInMilli *= 744; // Every 31 days (31 * 24 = 744)
				}
				else
				{
					autoFreqInMilli *= 372; // Every 15.5 days (15.5 * 24 = 372)
				}
				//@formatter:on
				
				// Compare the time since the last indexing to the autoFreqInMilli. If it's greater than or equal to the difference,
				// We need to run an Indexing
				if (differenceInMilli >= autoFreqInMilli
				        && !(this.settingEjb.getBooleanSettingByName(Constants.SETTING_SEARCH_INDEXING_COMMENCED)))
				{
					// Set our Indexing Complete flag to false
					this.indexingComplete = false;
					
					// Set the Indexing Commenced Setting
					this.settingEjb.saveSetting(Constants.SETTING_SEARCH_INDEXING_COMMENCED, "true");
					
					// Start the Indexing
					(new Thread(this.indexer)).start();
					
					// Wait until it's done
					while (!(this.indexingComplete))
					{
						try
						{
							Thread.sleep(10000);
						}
						catch (InterruptedException e)
						{
							AutoIndexer.log.error("AutoIndexer received an Interruption while waiting for Indexing to complete.");
							
							// Check if this interrupt is to notify the thread to die
							if (!(this.threadAlive))
							{
								return;
							}
						}
					}
				}
				
				// Check if the thread should still be alive
				if (!(this.threadAlive))
				{
					return;
				}
				
				// Wait 2 minutes before we check again
				try
				{
					Thread.sleep(120000);
				}
				catch (InterruptedException e)
				{
					AutoIndexer.log.error("AutoIndexer received an Interrupt while waiting before checking on Indexing again.");
					
					// Check if this interrupt is to notify the thread to die
					if (!(this.threadAlive))
					{
						return;
					}
				}
			}
		}
	}
	
	/**
	 * This sets the "threadAlive" property. If it is set to false, the thread will exit its running loop and die.
	 * 
	 * @param threadAlive
	 *            (boolean) If set to false, this will kill the thread.
	 */
	public void setThreadAlive(boolean threadAlive)
	{
		this.threadAlive = threadAlive;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.web.interfaces.IndexerCallback#updateProgress(int)
	 */
	@Override
	public void updateProgress(int recordsIndexed)
	{
		// TODO Auto-generated method stub
	}
	
}
