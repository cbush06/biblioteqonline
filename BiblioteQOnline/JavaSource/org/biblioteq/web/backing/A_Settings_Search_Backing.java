//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.backing
 * File: A_Settings_Search_Backing.java
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
 * This backing bean provides all the methods required by the Search Settings page.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jun 03, 2012, Clinton Bush, 1.0.0,
 *    New file.
 *    
 * Aug 08, 2012, Clinton Bush, 1.1.2,
 *    Added settings for browsing.
 *    
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.backing;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.interfaces.SettingBusinessLocal;
import org.biblioteq.web.common.Constants;
import org.biblioteq.web.model.ValidationMessage_Model;

/**
 * This backing bean provides all the methods required by the Search Settings page.
 * 
 * @author Clint Bush
 * 
 */
@ManagedBean(name = "A_Settings_Search_Backing")
@ViewScoped
public class A_Settings_Search_Backing implements Serializable
{
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 8040128950108317332L;
	
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(A_Settings_SummaryPage_Backing.class);
	
	/**
	 * Import the SettingBusiness EJB.
	 */
	@EJB(name = "SettingBusinessLocal")
	SettingBusinessLocal settingEjb;
	
	/**
	 * Get a copy of the Page_Backing bean. This will be injected via the setPageBackingBean(Page_Backing pageBackingBean) method.
	 */
	@ManagedProperty("#{Page_Backing}")
	private Page_Backing pageBackingBean;
	
	/**
	 * Use the ValidationMessage_Model to provide error messages for our page.
	 */
	private ValidationMessage_Model validationMessages = new ValidationMessage_Model();
	
	private String resultsPerPage = "";
	private String browseResultsPerPage = "";
	
	private String showTypeIcons = "";
	private String allowNonUserSearch = "";
	private String autoIndexing = "";
	private String autoIndexingFreq = "";
	
	/**
	 * Returns the value of "Allow Non-Users to Search".
	 * 
	 * @return (String) The allowNonUserSearch value.
	 */
	public String getAllowNonUserSearch()
	{
		return this.allowNonUserSearch;
	}
	
	/**
	 * Returns the value of "Auto-Indexing".
	 * 
	 * @return (String) The autoIndexing value.
	 */
	public String getAutoIndexing()
	{
		return this.autoIndexing;
	}
	
	/**
	 * Returns the value of "Auto-Indexing Frequency".
	 * 
	 * @return (String) The autoIndexingFreq value.
	 */
	public String getAutoIndexingFreq()
	{
		return this.autoIndexingFreq;
	}
	
	/**
	 * Returns the results per page setting for browsing.
	 * 
	 * @return (String) The browseResultsPerPage.
	 */
	public String getBrowseResultsPerPage()
	{
		return this.browseResultsPerPage;
	}
	
	/**
	 * Returns the value of "Results Per Page".
	 * 
	 * @return (String) The resultsPerPage value.
	 */
	public String getResultsPerPage()
	{
		return this.resultsPerPage;
	}
	
	/**
	 * Returns the value of "Show Type Icons".
	 * 
	 * @return (String) The showTypeIcons value.
	 */
	public String getShowTypeIcons()
	{
		return this.showTypeIcons;
	}
	
	/**
	 * This method pre-populates the settings on this page from the database.
	 */
	@PostConstruct
	public void init()
	{
		this.resultsPerPage = this.settingEjb.getSettingByName(Constants.SETTING_SEARCH_RESULTS_PER_PAGE).getValue();
		this.browseResultsPerPage = this.settingEjb.getSettingByName(Constants.SETTING_SEARCH_BROWSE_PER_PAGE).getValue();
		this.showTypeIcons = this.settingEjb.getSettingByName(Constants.SETTING_SEARCH_SHOW_TYPE_ICONS).getValue();
		this.allowNonUserSearch = this.settingEjb.getSettingByName(Constants.SETTING_SEARCH_ALLOW_NON_USERS).getValue();
		this.autoIndexing = this.settingEjb.getSettingByName(Constants.SETTING_SEARCH_AUTO_INDEXING).getValue();
		this.autoIndexingFreq = this.settingEjb.getSettingByName(Constants.SETTING_SEARCH_AUTO_INDEXING_FREQ).getValue();
	}
	
	/**
	 * Saves the Search settings.
	 * 
	 * @param e
	 *            (ActionEvent) The ActionEvent generated by the UI element that called this method (i.e. the Save button).
	 */
	public void save(ActionEvent e)
	{
		// If everthing validates, Save
		if (this.validateFields())
		{
			// Persist the settings
			this.settingEjb.saveSetting(Constants.SETTING_SEARCH_RESULTS_PER_PAGE, this.resultsPerPage);
			this.settingEjb.saveSetting(Constants.SETTING_SEARCH_BROWSE_PER_PAGE, this.browseResultsPerPage);
			this.settingEjb.saveSetting(Constants.SETTING_SEARCH_SHOW_TYPE_ICONS, this.showTypeIcons);
			this.settingEjb.saveSetting(Constants.SETTING_SEARCH_ALLOW_NON_USERS, this.allowNonUserSearch);
			this.settingEjb.saveSetting(Constants.SETTING_SEARCH_AUTO_INDEXING, this.autoIndexing);
			this.settingEjb.saveSetting(Constants.SETTING_SEARCH_AUTO_INDEXING_FREQ, this.autoIndexingFreq);
			
			// Display the Successful Save dialog
			this.pageBackingBean.showInfoMessage("Success!", "Your changes have been successfully saved!\n");
		}
		else
		{
			// Validation failed, show the messages
			this.validationMessages.renderMessages();
		}
	}
	
	/**
	 * Sets the value of "Allow Non-Users to Search".
	 * 
	 * @param allowNonUserSearch
	 *            (String) New value.
	 */
	public void setAllowNonUserSearch(String allowNonUserSearch)
	{
		this.allowNonUserSearch = allowNonUserSearch;
	}
	
	/**
	 * Sets the value of "Auto-Indexing".
	 * 
	 * @param autoIndexing
	 *            (String) New value.
	 */
	public void setAutoIndexing(String autoIndexing)
	{
		this.autoIndexing = autoIndexing;
	}
	
	/**
	 * Sets the value of "Auto-Indexing Frequency".
	 * 
	 * @param autoIndexingFreq
	 *            (String) New value.
	 */
	public void setAutoIndexingFreq(String autoIndexingFreq)
	{
		this.autoIndexingFreq = autoIndexingFreq;
	}
	
	/**
	 * Sets the results per page setting for browsing.
	 * 
	 * @param browseResultsPerPage
	 *            (String) The browseResultsPerPage to set.
	 */
	public void setBrowseResultsPerPage(String browseResultsPerPage)
	{
		this.browseResultsPerPage = browseResultsPerPage;
	}
	
	/**
	 * Used to inject the Page_Backing.java bean into this class.
	 * 
	 * @param pageBackingBean
	 *            (Page_Backing) The pageBackingBean to set.
	 */
	public void setPageBackingBean(Page_Backing pageBackingBean)
	{
		this.pageBackingBean = pageBackingBean;
	}
	
	/**
	 * Sets the value of "Results Per Page".
	 * 
	 * @param resultsPerPage
	 *            (String) New value.
	 */
	public void setResultsPerPage(String resultsPerPage)
	{
		this.resultsPerPage = resultsPerPage;
	}
	
	/**
	 * Sets the value of "Show Type Icons".
	 * 
	 * @param showTypeIcons
	 *            (String) New value.A_S
	 */
	public void setShowTypeIcons(String showTypeIcons)
	{
		this.showTypeIcons = showTypeIcons;
	}
	
	/**
	 * Validates all fields on the page. Returns true if validation is successful; false, otherwise.
	 * 
	 * @return (boolean) True if validation succeeded; false, otherwise.
	 */
	public boolean validateFields()
	{
		// Declare the variables we'll be using
		boolean isValidated = true;
		
		// Validate the Results Per Page
		if (Integer.parseInt(this.resultsPerPage) < 5)
		{
			this.validationMessages.addMessage("The results per page can be no less than 5.");
			isValidated = false;
		}
		
		// Validate the Browse Results Per Page
		if (Integer.parseInt(this.browseResultsPerPage) < 5)
		{
			this.validationMessages.addMessage("The browse results per page can be no less than 5.");
			isValidated = false;
		}
		
		// Return the results
		return isValidated;
	}
}
