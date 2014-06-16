//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.backing
 * File: Browse_Backing.java
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
 * Backs the "Browse" page by providing all functionality required by the page.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jul 28, 2012, Clinton Bush, 1.1.1,
 *    New file.
 * Aug 08, 2012, Clinton Bush, 1.1.2,
 *    Implemented Serializable, added an initializer, and set it to ViewScoped.
 * Aug 09, 2012, Clinton Bush, 1.1.2,
 *    Renamed to Browse_Backing to indicate this file will be used by Restricted and Unrestricted sections of the site (like the Search_Backing bean).
 *    Added controls to restrict it if Searching is disabled for unregistered users.
 *    
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.backing;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.interfaces.IndexBusinessLocal;
import org.biblioteq.ejb.interfaces.SettingBusinessLocal;
import org.biblioteq.ejb.model.BrowseItem;
import org.biblioteq.web.backing.Search_Backing.SimpleOrAdvanced;
import org.biblioteq.web.common.Constants;
import org.biblioteq.web.model.AdvancedSearch_Model;
import org.biblioteq.web.model.AdvancedSearch_Model.QueryType;
import org.biblioteq.web.model.AdvancedSearch_Model.Requirement;
import org.biblioteq.web.model.ValidationMessage_Model;

/**
 * Backs the "Browse" page by providing all functionality required by the page.
 * 
 * @author Clint Bush
 * 
 */
@ManagedBean(name = "Browse_Backing")
@ViewScoped
public class Browse_Backing implements Serializable
{
	/**
	 * Use this as a data model for the page numbers at the bottom of the page.
	 * 
	 * @author Clint Bush
	 * 
	 */
	public class PageNumber
	{
		public String display = "";
		public int value = 0;
		
		public PageNumber(String display, int value)
		{
			this.display = display;
			this.value = value;
		}
		
		public String getDisplay()
		{
			return this.display;
		}
		
		public int getValue()
		{
			return this.value;
		}
	}
	
	/**
	 * GUID for Serializable.
	 */
	private static final long serialVersionUID = -6643883824580454047L;
	
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(Browse_Backing.class);
	
	/**
	 * Prepare a validation model for displaying Error Messages.
	 */
	private ValidationMessage_Model errorMessages = new ValidationMessage_Model();
	
	/**
	 * Get a reference to the IndexBusiness EJB.
	 */
	@EJB(name = "IndexBusiness")
	IndexBusinessLocal indexEjb;
	
	/**
	 * Get a reference to the SettingBusiness EJB.
	 */
	@EJB(name = "SettingBusiness")
	SettingBusinessLocal settingEjb;
	
	/**
	 * Store the type of listing we are currently showing the user (Subject, Type, Creator, Location).
	 */
	private String typeFilter = "Subject";
	
	/**
	 * Members used in browsing.
	 */
	private ArrayList<BrowseItem> resultsList = new ArrayList<BrowseItem>();
	private long totalHits = 0;
	private int resultsPerPage = 0;
	private int resultsPage = 1;
	
	/**
	 * Store the selected item.
	 */
	private BrowseItem selectedItem = null;
	
	/**
	 * Get a copy of the Page_Backing bean. This will be injected via the setPageBackingBean(Page_Backing pageBackingBean) method.
	 */
	@ManagedProperty("#{Page_Backing}")
	private Page_Backing pageBackingBean;
	
	/**
	 * Get a copy of the Search_Backing bean. This will be injected via the setPageBackingBean(Page_Backing pageBackingBean) method.
	 */
	@ManagedProperty("#{Search_Backing}")
	private Search_Backing searchBackingBean;
	
	/**
	 * Conducts a search on the selected item.
	 * 
	 * @param e
	 *            (ActionEvent) The ActionEvent generated by the user's action that triggered this method.
	 */
	public String conductSearchForItem()
	{
		AdvancedSearch_Model query = new AdvancedSearch_Model();
		
		if (this.typeFilter.equals("Subject"))
		{
			query.addSearchTerm(Requirement.MUST, QueryType.CONTAIN, Constants.SEARCH_DOCUMENT_FIELD_CATEGORY, this.selectedItem.getTerm());
		}
		if (this.typeFilter.equals("Type"))
		{
			query.addSearchTerm(Requirement.MUST, QueryType.TERM_QUERY, Constants.SEARCH_DOCUMENT_FIELD_TYPE, this.selectedItem.getTerm());
		}
		if (this.typeFilter.equals("Creator"))
		{
			query.addSearchTerm(Requirement.MUST, QueryType.CONTAIN, Constants.SEARCH_DOCUMENT_FIELD_CREATOR, this.selectedItem.getTerm());
		}
		if (this.typeFilter.equals("Location"))
		{
			query.addSearchTerm(Requirement.MUST, QueryType.TERM_QUERY, Constants.SEARCH_DOCUMENT_FIELD_LOCATION,
			        this.selectedItem.getTerm());
			query.addSearchTerm(Requirement.MUST, QueryType.TERM_QUERY, Constants.SEARCH_DOCUMENT_FIELD_TYPE, this.selectedItem.getType());
		}
		
		// Set the AdvancedSearch_Model on the Search Backing Bean
		this.searchBackingBean.setAdvancedQueryModel(query);
		this.searchBackingBean.setSimpleOrAdvanced(SimpleOrAdvanced.ADVANCED);
		this.searchBackingBean.setSearchType("all");
		this.searchBackingBean.autoSearch();
		
		// Transfer the user to the Search Results page
		this.pageBackingBean.setRenderPage(Constants.PAGE_RESTRICTED_SEARCHRESULTS);
		
		return "update";
	}
	
	/**
	 * Conducts a search on the selected item for the unrestricted section of the web site.
	 * 
	 * @return (String) JSF navigation outcome that directs the user to the unrestricted search results page.
	 */
	public String conductUnRestrictedSearchForItem()
	{
		AdvancedSearch_Model query = new AdvancedSearch_Model();
		
		if (this.typeFilter.equals("Subject"))
		{
			query.addSearchTerm(Requirement.MUST, QueryType.CONTAIN, Constants.SEARCH_DOCUMENT_FIELD_CATEGORY,
			        "\"" + this.selectedItem.getTerm() + "\"");
		}
		if (this.typeFilter.equals("Type"))
		{
			query.addSearchTerm(Requirement.MUST, QueryType.TERM_QUERY, Constants.SEARCH_DOCUMENT_FIELD_TYPE, this.selectedItem.getTerm());
		}
		if (this.typeFilter.equals("Creator"))
		{
			query.addSearchTerm(Requirement.MUST, QueryType.CONTAIN, Constants.SEARCH_DOCUMENT_FIELD_CREATOR,
			        "\"" + this.selectedItem.getTerm() + "\"");
		}
		if (this.typeFilter.equals("Location"))
		{
			query.addSearchTerm(Requirement.MUST, QueryType.TERM_QUERY, Constants.SEARCH_DOCUMENT_FIELD_LOCATION,
			        this.selectedItem.getTerm());
			query.addSearchTerm(Requirement.MUST, QueryType.TERM_QUERY, Constants.SEARCH_DOCUMENT_FIELD_TYPE, this.selectedItem.getType());
		}
		
		// Set the AdvancedSearch_Model on the Search Backing Bean
		this.searchBackingBean.setAdvancedQueryModel(query);
		this.searchBackingBean.setSimpleOrAdvanced(SimpleOrAdvanced.ADVANCED);
		this.searchBackingBean.setSearchType("all");
		this.searchBackingBean.autoSearch();
		
		return "search";
	}
	
	/**
	 * Loads the next set of results into the resultsList.
	 * 
	 * @param e
	 */
	public void getNextResultSet(ActionEvent e)
	{
		// Ensure that an Indexing is not taking place
		if (this.settingEjb.getBooleanSettingByName(Constants.SETTING_SEARCH_INDEXING_COMMENCED))
		{
			this.errorMessages
			        .addMessage("We're sorry. The Search Engine is re-indexing the database right now. Please try again in 1 - 2 minutes. Thank you!");
			this.errorMessages.renderMessages();
			return;
		}
		
		if ((this.pageBackingBean.getCurrentUser() != null && this.pageBackingBean.getCurrentUser().isActive())
		        || this.settingEjb.getBooleanSettingByName(Constants.SETTING_SEARCH_ALLOW_NON_USERS))
		{
			// Clear the current set of results
			this.setResultsList(this.indexEjb.getBrowseResults(((this.resultsPage - 1) * this.resultsPerPage), this.resultsPerPage));
		}
	}
	
	/**
	 * Returns the page numbers as an ArrayList of PageNumber objects.
	 * 
	 * @return (ArrayList<PageNumber>) Page numbers.
	 */
	public ArrayList<PageNumber> getPageNumbers()
	{
		ArrayList<PageNumber> returnVal = new ArrayList<PageNumber>();
		
		if (((this.pageBackingBean.getCurrentUser() != null && this.pageBackingBean.getCurrentUser().isActive()) || this.settingEjb
		        .getBooleanSettingByName(Constants.SETTING_SEARCH_ALLOW_NON_USERS))
		        && !(this.settingEjb.getBooleanSettingByName(Constants.SETTING_SEARCH_INDEXING_COMMENCED)))
		{
			// The page to stop at
			long endingPage = this.totalHits / this.resultsPerPage;
			if ((this.totalHits % this.resultsPerPage) > 0)
			{
				endingPage++;
			}
			
			// If we are past the number 6, we need to begin scrolling the result list with the current page
			if (this.resultsPage > 6)
			{
				if (endingPage > this.resultsPage + 4)
				{
					endingPage = this.resultsPage + 4;
				}
				
				for (int i = this.resultsPage - 5; i <= endingPage; i++)
				{
					returnVal.add(new PageNumber(String.valueOf(i), i));
				}
			}
			// If we are at or below page 6, we just show page numbers 1 - 10
			else
			{
				if (endingPage > 10)
				{
					endingPage = 10;
				}
				
				for (int i = 1; i <= endingPage; i++)
				{
					returnVal.add(new PageNumber(String.valueOf(i), i));
				}
			}
		}
		
		return returnVal;
	}
	
	/**
	 * Returns the list of results as BrowseItem objects in an ArrayList.
	 * 
	 * @return (ArrayList) The resultsList.
	 */
	public ArrayList<BrowseItem> getResultsList()
	{
		return this.resultsList;
	}
	
	/**
	 * Returns the current page of results the user is viewing.
	 * 
	 * @return (int) The resultsPage.
	 */
	public int getResultsPage()
	{
		return this.resultsPage;
	}
	
	/**
	 * Returns the number of results to be listed per page.
	 * 
	 * @return (int) The resultsPerPage.
	 */
	public int getResultsPerPage()
	{
		return this.resultsPerPage;
	}
	
	/**
	 * Returns the item the user selected from the results list.
	 * 
	 * @return (BrowseItem) The selectedItem.
	 */
	public BrowseItem getSelectedItem()
	{
		return this.selectedItem;
	}
	
	/**
	 * Returns the total hits returned for the type of browsing the user is conducting.
	 * 
	 * @return (long) The totalHits.
	 */
	public long getTotalHits()
	{
		return this.totalHits;
	}
	
	/**
	 * Returns the current value of the type filter.
	 * 
	 * @return (String) The typeFilter.
	 */
	public String getTypeFilter()
	{
		return this.typeFilter;
	}
	
	/**
	 * Perform preliminary steps to initializing the bean.
	 */
	@PostConstruct
	public void init()
	{
		if (((this.pageBackingBean.getCurrentUser() != null && this.pageBackingBean.getCurrentUser().isActive()) || this.settingEjb
		        .getBooleanSettingByName(Constants.SETTING_SEARCH_ALLOW_NON_USERS))
		        && !(this.settingEjb.getBooleanSettingByName(Constants.SETTING_SEARCH_INDEXING_COMMENCED)))
		{
			this.resultsPerPage = this.settingEjb.getIntegerSettingByName(Constants.SETTING_SEARCH_BROWSE_PER_PAGE);
			this.totalHits = this.indexEjb.getTotalSubjects();
			this.setResultsList(this.indexEjb.getBrowseResults(((this.resultsPage - 1) * this.resultsPerPage), this.resultsPerPage));
		}
	}
	
	/**
	 * Returns a boolean indicating whether the Search box should be shown on an unrestricted page.
	 * 
	 * @return (boolean) True if it should be shown, false, otherwise.
	 */
	public boolean isBrowseShown()
	{
		return ((this.pageBackingBean.getCurrentUser() != null) || (this.settingEjb
		        .getBooleanSettingByName(Constants.SETTING_SEARCH_ALLOW_NON_USERS)))
		        && !(this.settingEjb.getBooleanSettingByName(Constants.SETTING_SEARCH_INDEXING_COMMENCED));
	}
	
	/**
	 * Used to inject a reference to the Page_Backing bean into this class.
	 * 
	 * @param pageBackingBean
	 *            the pageBackingBean to set
	 */
	public void setPageBackingBean(Page_Backing pageBackingBean)
	{
		this.pageBackingBean = pageBackingBean;
	}
	
	/**
	 * Sets the list of results.
	 * 
	 * @param resultsList
	 *            (ArrayList<BrowseItem>) The resultsList to set.
	 */
	public void setResultsList(ArrayList<BrowseItem> resultsList)
	{
		this.resultsList = resultsList;
	}
	
	/**
	 * Sets the current page of results the user is viewing.
	 * 
	 * @param resultsPage
	 *            (int) The resultsPage to set.
	 */
	public void setResultsPage(int resultsPage)
	{
		this.resultsPage = resultsPage;
	}
	
	/**
	 * Sets the number of results to show on each page.
	 * 
	 * @param resultsPerPage
	 *            (int) The resultsPerPage to set.
	 */
	public void setResultsPerPage(int resultsPerPage)
	{
		this.resultsPerPage = resultsPerPage;
	}
	
	/**
	 * Allows for the injection of the Search_Backing bean into this class.
	 * 
	 * @param searchBackingBean
	 *            the searchBackingBean to set
	 */
	public void setSearchBackingBean(Search_Backing searchBackingBean)
	{
		this.searchBackingBean = searchBackingBean;
	}
	
	/**
	 * Sets the item selected by the user when he/she clicked an item in the results list.
	 * 
	 * @param selectedItem
	 *            (BrowseItem) The selectedItem to set.
	 */
	public void setSelectedItem(BrowseItem selectedItem)
	{
		this.selectedItem = selectedItem;
	}
	
	/**
	 * Sets the total hits returned for the current type of browsing.
	 * 
	 * @param totalHits
	 *            (long) The totalHits to set.
	 */
	public void setTotalHits(long totalHits)
	{
		this.totalHits = totalHits;
	}
	
	/**
	 * Sets the value of the type filter.
	 * 
	 * @param typeFilter
	 *            (String) The typeFilter to set.
	 */
	public void setTypeFilter(String typeFilter)
	{
		if ((this.pageBackingBean.getCurrentUser() != null && this.pageBackingBean.getCurrentUser().isActive())
		        || this.settingEjb.getBooleanSettingByName(Constants.SETTING_SEARCH_ALLOW_NON_USERS))
		{
			// Ensure that an Indexing is not taking place
			if (this.settingEjb.getBooleanSettingByName(Constants.SETTING_SEARCH_INDEXING_COMMENCED))
			{
				return;
			}
			
			this.typeFilter = typeFilter;
			this.resultsPage = 1;
			
			// If we're browsing by subject
			if (this.typeFilter.equals("Subject"))
			{
				this.indexEjb.setBrowseQueryBySubject();
				this.totalHits = this.indexEjb.getTotalSubjects();
			}
			
			// If we're browsing by type
			if (this.typeFilter.equals("Type"))
			{
				this.indexEjb.setBrowseQueryByType();
				this.totalHits = this.indexEjb.getTotalTypes();
			}
			
			// If we're browsing by Creator
			if (this.typeFilter.equals("Creator"))
			{
				this.indexEjb.setBrowseQueryByCreator();
				this.totalHits = this.indexEjb.getTotalCreators();
			}
			
			// If we're browsing by Location
			if (this.typeFilter.equals("Location"))
			{
				this.indexEjb.setBrowseQueryByLocation();
				this.totalHits = this.indexEjb.getTotalNonEmptyLocations();
			}
		}
	}
}
