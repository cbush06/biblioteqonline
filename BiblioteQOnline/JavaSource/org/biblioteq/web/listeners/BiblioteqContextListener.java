//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQ Online
 * Package: org.biblioteq.web.listeners
 * File: BiblioteqContextListener.java
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
 * #      Revision       #
 * ####################### 
 * 03 Mar 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 08 Aug 2012, Clinton Bush, 1.1.2,
 * 	  Adjusted the page titles to work with the new title setting added to the administration settings.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on

package org.biblioteq.web.listeners;

import java.util.HashMap;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.interfaces.IndexBusinessLocal;
import org.biblioteq.ejb.interfaces.SettingBusinessLocal;
import org.biblioteq.web.common.Constants;
import org.biblioteq.web.model.Page_Model;
import org.biblioteq.web.model.Page_Model.PageType;
import org.biblioteq.web.threads.AutoIndexer;

/**
 * Initializes the Page Map used for navigation and other important values required by iNspire and cleans house before shutting down.
 * 
 * @author Clint Bush
 */
public class BiblioteqContextListener implements ServletContextListener
{
	@EJB(name = "SettingBusiness")
	private SettingBusinessLocal settingEjb;
	
	@EJB(name = "IndexBusiness")
	private IndexBusinessLocal indexEjb;
	
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(BiblioteqContextListener.class);
	
	/**
	 * Create the Auto Indexer Thread
	 */
	private AutoIndexer autoIndexer = null;
	private Thread autoIndexerThread = null;
	
	@Override
	public void contextDestroyed(ServletContextEvent context)
	{
		/**
		 * Kill the Auto Indexer.
		 */
		this.autoIndexer.setThreadAlive(false);
		this.autoIndexerThread.interrupt();
	}
	
	@Override
	public void contextInitialized(ServletContextEvent context)
	{
		/**
		 * Page Map to be stored in the Application Map and used by iNspire for navigation and page construction.
		 */
		HashMap<String, Page_Model> pageMap = new HashMap<String, Page_Model>();
		
		// Let's add all the Restricted Pages and their attributes to the Page Map
		pageMap.put(Constants.PAGE_RESTRICTED_INDEX, new Page_Model(PageType.Restricted, "Home", "Restricted_Index.xhtml",
		        "Restricted_Main_Navigation.xhtml", "Restricted_Header.xhtml"));
		pageMap.put(Constants.PAGE_RESTRICTED_SEARCHRESULTS, new Page_Model(PageType.Restricted, "Search Results",
		        "Restricted_SearchResults.xhtml", "Restricted_Main_Navigation.xhtml", "Restricted_Header.xhtml"));
		pageMap.put(Constants.PAGE_RESTRICTED_ITEMDETAILS, new Page_Model(PageType.Restricted, "Item Details",
		        "Restricted_ItemDetails.xhtml", "Restricted_Main_Navigation.xhtml", "Restricted_Header.xhtml"));
		pageMap.put(Constants.PAGE_RESTRICTED_ITEMREQUESTAGREEMENT, new Page_Model(PageType.Restricted, "Request Agreement",
		        "Restricted_ItemRequestAgreement.xhtml", "Restricted_Main_Navigation.xhtml", "Restricted_Header.xhtml"));
		pageMap.put(Constants.PAGE_RESTRICTED_ITEMREQUESTCONFIRMATION, new Page_Model(PageType.Restricted, "Request Confirmation",
		        "Restricted_ItemRequestConfirmation.xhtml", "Restricted_Main_Navigation.xhtml", "Restricted_Header.xhtml"));
		pageMap.put(Constants.PAGE_RESTRICTED_MYACCOUNT, new Page_Model(PageType.Restricted, "My Account", "Restricted_MyAccount.xhtml",
		        "Restricted_Main_Navigation.xhtml", "Restricted_Header.xhtml"));
		pageMap.put(Constants.PAGE_RESTRICTED_BROWSE, new Page_Model(PageType.Restricted, "Browse", "Restricted_Browse.xhtml",
		        "Restricted_Main_Navigation.xhtml", "Restricted_Header.xhtml"));
		
		// Let's add all the Admin Pages and their attributes to the Page Map
		pageMap.put(Constants.PAGE_ADMIN_INDEX, new Page_Model(PageType.Admin, "Administration", "Admin_Index.xhtml",
		        "Admin_Main_Navigation.xhtml", "Admin_Header.xhtml"));
		pageMap.put(Constants.PAGE_ADMIN_SETTINGS, new Page_Model(PageType.Admin, "System Settings", "Admin_Settings.xhtml",
		        "Admin_Main_Navigation.xhtml", "Admin_Header.xhtml"));
		pageMap.put(Constants.PAGE_ADMIN_SETTINGS_SEARCH, new Page_Model(PageType.Admin, "Search Settings", "Admin_Settings_Search.xhtml",
		        "Admin_Main_Navigation.xhtml", "Admin_Header.xhtml"));
		pageMap.put(Constants.PAGE_ADMIN_SETTINGS_SUMMARYPAGE, new Page_Model(PageType.Admin, "Summary Page Settings",
		        "Admin_Settings_SummaryPage.xhtml", "Admin_Main_Navigation.xhtml", "Admin_Header.xhtml"));
		pageMap.put(Constants.PAGE_ADMIN_MANAGEUSERS, new Page_Model(PageType.Admin, "Manage Users", "Admin_ManageUsers.xhtml",
		        "Admin_Main_Navigation.xhtml", "Admin_Header.xhtml"));
		
		// Add the pageMap to the context
		context.getServletContext().setAttribute(Constants.APPLICATION_PAGE_MAP, pageMap);
		
		/**
		 * Start the Auto Indexer.
		 */
		this.autoIndexer = new AutoIndexer(this.indexEjb, this.settingEjb);
		this.autoIndexerThread = new Thread(this.autoIndexer);
		this.autoIndexerThread.start();
	}
}
