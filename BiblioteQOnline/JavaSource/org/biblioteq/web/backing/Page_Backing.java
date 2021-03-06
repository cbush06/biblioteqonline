//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.backing
 * File: Page_Backing.java
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
 * This backing bean backs all pages in a restricted or admin area with methods that provide the different parts to each section of the page. This enables using AJAX within
 * the secured areas of the web site.
 * 
 * #######################
 * #      Revision       #
 * ####################### 
 * Mar 3, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * Aug 08, 2012, Clinton Bush, 1.1.2,
 *    Implemented the Serializable interface.
 *    
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on

package org.biblioteq.web.backing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.entities.User;
import org.biblioteq.ejb.interfaces.Item;
import org.biblioteq.ejb.interfaces.SettingBusinessLocal;
import org.biblioteq.web.common.Constants;
import org.biblioteq.web.model.Page_Model;
import org.biblioteq.web.model.Page_Model.PageType;
import org.biblioteq.web.model.ValidationMessage_Model;
import org.biblioteq.web.security.Login_Security;

/**
 * Provides methods and members that control the navigation between and generation of pages.
 */
@ManagedBean(name = "Page_Backing")
@SessionScoped
public class Page_Backing extends Screen_Backing implements Serializable
{
	/**
	 * GUID for implementing Serializable
	 */
	private static final long serialVersionUID = -8874926569573461089L;
	
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(Page_Backing.class);
	
	/**
	 * Get a copy of the Login_Security bean. This will be injected via the setLoginBean(Login_Security loginBean) method.
	 */
	@ManagedProperty("#{Login_Security}")
	private Login_Security loginBean;
	
	/**
	 * Get a copy of the Setting EJB.
	 */
	@EJB(name = "SettingBusiness")
	private SettingBusinessLocal settingEjb;
	
	/**
	 * Wrapper of FacesMessage framework for handling error messages.
	 */
	private ValidationMessage_Model errorMsg = new ValidationMessage_Model();
	
	/**
	 * Stores the current page to be rendered. This value will be used as a key to retrieve a Page_Model object from the Page Map stored in the Application Map.
	 */
	private String renderPage = "index.xhtml";
	
	/**
	 * Stores a reference to the Page Map stored in the Application Map.
	 */
	private Map<String, Page_Model> pageMap = new HashMap<String, Page_Model>();
	
	/**
	 * Stores a reference to the currently logged in user for use in layout sections of each page (e.g. navigation panel, header, etc.)
	 */
	private User currentUser = null;
	
	/**
	 * The current book to show data on.
	 */
	private Item infoItem = null;
	
	/**
	 * These fields back the info popup.
	 */
	private String infoTitle = "";
	private String infoMessage = "";
	
	/**
	 * The title of the entire system. This is only retrieved when the session is created to save trips to the database.
	 */
	private String systemTitle = "";
	
	/**
	 * The constructor will obtain the PageMap from the Application Map. The PageMap is constructed by a Context Listener when the application is first loaded; thus, it must be
	 * stored in the Application Map in order for JSF to obtain it.
	 */
	@SuppressWarnings("unchecked")
	public Page_Backing()
	{
		try
		{
			// Attempt to obtain the PageMap from the Application Map
			this.pageMap = (HashMap<String, Page_Model>) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap()
			        .get(Constants.APPLICATION_PAGE_MAP);
		}
		catch (Exception e)
		{
			Page_Backing.log.error("Exception thrown while obtaining the PageMap!");
		}
		
		// Ensure a NULL was not returned
		if (this.pageMap == null)
		{
			Page_Backing.log.error("The PageMap obtained from the Application Map is null!");
		}
	}
	
	/**
	 * Clears the info message and title of the info message popup that was shown.
	 * 
	 * @param e
	 *            (ActionEvent) The ActionEvent generated by the UI element that called this method (i.e. the Info Message popup's close button).
	 */
	public void clearInfoMessage(ActionEvent e)
	{
		this.infoTitle = "";
		this.infoMessage = "";
	}
	
	/**
	 * Clears the book that info is currently being shown for. This should hide the book information dialog.
	 * 
	 * @param e
	 *            (ActionEvent) The ActionEvent generated by the commandButton.
	 */
	public void clearItemListener(AjaxBehaviorEvent e)
	{
		this.infoItem = null;
	}
	
	/**
	 * @return the currentUser
	 */
	@Override
	public User getCurrentUser()
	{
		// If this is the first time we're trying to get the current user, get it from the Session Map
		if (this.currentUser == null)
		{
			// Attempt to obtain the Current Logged In User from the Session Map
			this.currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
			        .get(Constants.SESSION_LOGGED_IN_USER);
		}
		
		return this.currentUser;
	}
	
	public String getHeaderImageFilePath()
	{
		if (this.settingEjb.getBooleanSettingByName(Constants.SETTING_SYSTEM_CUSTOM_HEADER))
		{
			return "customimgs/header." + this.settingEjb.getStringSettingByName(Constants.SETTING_SYSTEM_HEADER_TYPE);
		}
		else
		{
			return "imgs/header_logo.jpg";
		}
	}
	
	/**
	 * Returns the book that info will be shown for.
	 * 
	 * @return (Book) The Book.
	 */
	public Item getInfoItem()
	{
		return this.infoItem;
	}
	
	/**
	 * Returns the message of the info message popup.
	 * 
	 * @return (String) The infoMessage.
	 */
	public String getInfoMessage()
	{
		return this.infoMessage;
	}
	
	/**
	 * Returns the title of the info message popup.
	 * 
	 * @return (String) The infoTitle.
	 */
	public String getInfoTitle()
	{
		return this.infoTitle;
	}
	
	/**
	 * Returns the name of the XHTML file to be rendered. This is a file located in the WebContent/restricted or WebContent/admin directory.
	 * 
	 * @return (String) The file name of the current render page.
	 */
	public String getRenderPage()
	{
		return this.renderPage;
	}
	
	/**
	 * Returns the file name of the XHTML file that contains the content of the current render page. If this render page cannot be located in the PageMap, returns an empty string.
	 * This file should be located in the WebContent/restricted/content or WebContent/admin/content directory.
	 * 
	 * @return (String) Returns the file name of the file that contains the content of the current render page.
	 */
	public String getRenderPageContent()
	{
		if (this.pageMap.containsKey(this.renderPage))
		{
			if (this.pageMap.get(this.renderPage).getType() == PageType.Restricted)
			{
				return Constants.PATH_DIR_RESTRICTED_CONTENT + this.pageMap.get(this.renderPage).getContent();
			}
			if (this.pageMap.get(this.renderPage).getType() == PageType.Admin)
			{
				return Constants.PATH_DIR_ADMIN_CONTENT + this.pageMap.get(this.renderPage).getContent();
			}
		}
		return "";
	}
	
	/**
	 * Returns the file name of the XHTML file that contains the header section of the current render page. If this render page cannot be located in the PageMap, returns an empty
	 * string.
	 * 
	 * This file should be located in the WebContent/restricted/header or WebContent/admin/header directory.
	 * 
	 * @return (String) Returns the file name of the file that contains the header of the current render page.
	 */
	public String getRenderPageHeader()
	{
		if (this.pageMap.containsKey(this.renderPage))
		{
			if (this.pageMap.get(this.renderPage).getType() == PageType.Restricted)
			{
				return Constants.PATH_DIR_RESTRICTED_HEADERS + this.pageMap.get(this.renderPage).getHeader();
			}
			if (this.pageMap.get(this.renderPage).getType() == PageType.Admin)
			{
				return Constants.PATH_DIR_ADMIN_HEADERS + this.pageMap.get(this.renderPage).getHeader();
			}
			
		}
		return "";
	}
	
	/**
	 * Returns the file name of the XHTML file that contains the navigation section of the current render page. If this render page cannot be located in the PageMap, returns an
	 * empty string.
	 * 
	 * This file should be located in the WebContent/restricted/navigation or WebContent/admin/navigation directory.
	 * 
	 * @return (String)
	 */
	public String getRenderPageNavigation()
	{
		if (this.pageMap.containsKey(this.renderPage))
		{
			if (this.pageMap.get(this.renderPage).getType() == PageType.Restricted)
			{
				return Constants.PATH_DIR_RESTRICTED_NAVIGATION + this.pageMap.get(this.renderPage).getNavigation();
			}
			if (this.pageMap.get(this.renderPage).getType() == PageType.Admin)
			{
				return Constants.PATH_DIR_ADMIN_NAVIGATION + this.pageMap.get(this.renderPage).getNavigation();
			}
		}
		return "";
	}
	
	/**
	 * Returns the title of the current render page. If this render page cannot be located in the PageMap, returns an empty string.
	 * 
	 * @return (String) The Title of the render page.
	 */
	public String getRenderPageTitle()
	{
		String pageTitle = this.getSystemTitle();
		
		if (this.pageMap.containsKey(this.renderPage))
		{
			if (pageTitle.length() > 0)
			{
				pageTitle += " - ";
			}
			pageTitle += this.pageMap.get(this.renderPage).getTitle();
		}
		return pageTitle;
	}
	
	/**
	 * Returns the System Title used in the title bar.
	 * 
	 * @return (String) The systemTitle.
	 */
	public String getSystemTitle()
	{
		return this.systemTitle;
	}
	
	/**
	 * Navigate the user to the Administrative Index Page.
	 * 
	 * @return (String) The navigation rule for navigating to the Administrative Section's index page.
	 */
	public String gotoAdminHome()
	{
		if (this.isAdmin())
		{
			this.setRenderPage(Constants.PAGE_ADMIN_INDEX);
			return "admin";
		}
		Page_Backing.log.error("Invalid attempt to access Administrative section!");
		return "logout";
	}
	
	/**
	 * Navigate the user to the Item Details page.
	 * 
	 * @param e
	 *            (ActionEvent) The ActionEvent object generated by the component that called this method.
	 */
	public void gotoItemDetails()
	{
		// Put the selected Item in the Session Map for the Item Details page
		this.setSelectedItem(this.infoItem);
		
		// Set the page the Item Details page's "<< Back" link will return the user to
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		        .put(Constants.SESSION_ITEM_DESCRIPTION_BACK, this.renderPage);
		
		// Clear the info item so the details dialog will hide
		this.infoItem = null;
		
		// Navigate to the item details page
		this.setRenderPage(Constants.PAGE_RESTRICTED_ITEMDETAILS);
	}
	
	/**
	 * Navigates the user to the My Account page.
	 * 
	 * @return (String) Navigates the user to the "My Account" page.
	 */
	public String gotoMyAccount()
	{
		this.setRenderPage(Constants.PAGE_RESTRICTED_MYACCOUNT);
		return "update";
	}
	
	/**
	 * Navigates the user to the Restricted Section Browse page.
	 * 
	 * @return (String) Navigates the user to the "Browse" page and updates the browser.
	 */
	public String gotoRestrictedBrowse()
	{
		// Ensure that an Indexing is not taking place
		if (this.settingEjb.getBooleanSettingByName(Constants.SETTING_SEARCH_INDEXING_COMMENCED))
		{
			this.errorMsg
			        .addMessage("We're sorry. The Search Engine is re-indexing the database right now. Please try again in 1 - 2 minutes. Thank you!");
			this.errorMsg.renderMessages();
			return "";
		}
		
		this.setRenderPage(Constants.PAGE_RESTRICTED_BROWSE);
		return "update";
	}
	
	/**
	 * Navigates the user to the Restricted Section Index page.
	 * 
	 * @return (String) The navigation rule to go to the Restricted Index page.
	 */
	public String gotoRestrictedHome()
	{
		if (this.getCurrentUser() != null && this.getCurrentUser().isActive())
		{
			this.setRenderPage(Constants.PAGE_RESTRICTED_INDEX);
			return "restrictedindex";
		}
		Page_Backing.log.error("Invalid attempt to access Restricted section!");
		return "logout";
	}
	
	@PostConstruct
	public void init()
	{
		this.setSystemTitle(this.settingEjb.getStringSettingByName(Constants.SETTING_SYSTEM_TITLE));
	}
	
	/**
	 * Convenience method for determining if the user has administrative privileges of some sort.
	 * 
	 * @return (boolean) True if the user is a form of an administrator; otherwise, false.
	 */
	public boolean isAdmin()
	{
		return (this.getCurrentUser().getAdmin() != null);
	}
	
	/**
	 * Returns True if error messages exist.
	 * 
	 * @return (boolean) True if error messages exist.
	 */
	public boolean isErrorMessages()
	{
		return FacesContext.getCurrentInstance().getMessageList().size() > 0;
	}
	
	/**
	 * Returns true if an info message is waiting to be displayed.
	 * 
	 * @return (boolean) True if an info message exists.
	 */
	public boolean isInfoMessageShown()
	{
		return (this.infoTitle.length() > 0 && this.infoMessage.length() > 0);
	}
	
	/**
	 * Convenience method used to display the book info panel.
	 * 
	 * @return (boolean) True if the book info panel should be shown.
	 */
	public boolean isItemInfoShown()
	{
		return (this.infoItem != null);
	}
	
	/**
	 * System-wide method for logging users out. This uses the Login_Security bean's logout method to invalidate the session and log users out.
	 * 
	 * @return (String) Navigation rule to direct JSF's navigation framework.
	 */
	public String logout()
	{
		// Send them to the /logout/ directory so the LogOutServlet can handle logging them out
		return "logout";
	}
	
	/**
	 * Sets the current user.
	 * 
	 * @param currentUser
	 *            the currentUser to set
	 */
	public void setCurrentUser(User currentUser)
	{
		this.currentUser = currentUser;
	}
	
	/**
	 * Sets the book that info will be shown for.
	 * 
	 * @param infoBook
	 *            (Book) The Book to set.
	 */
	public void setInfoItem(Item infoItem)
	{
		this.infoItem = infoItem;
	}
	
	/**
	 * Allow the injection of the Login_Security bean.
	 * 
	 * @param loginBean
	 *            (Login_Security) The injected instance of the Login_Security bean.
	 */
	public void setLoginBean(Login_Security loginBean)
	{
		this.loginBean = loginBean;
	}
	
	/**
	 * Set the page to be rendered next.
	 * 
	 * This file should be located in the WebContent/restricted or WebContent/admin folder.
	 * 
	 * @param renderPage
	 *            (String) File name of the next page to be rendered.
	 */
	public void setRenderPage(String renderPage)
	{
		this.renderPage = renderPage;
	}
	
	/**
	 * Sets the item currently selected for viewing on the Item Details page. This method actually stores the item in the Session Map.
	 * 
	 * @param selectedItem
	 *            (Item) The selected item.
	 */
	public void setSelectedItem(Item selectedItem)
	{
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(Constants.SESSION_SELECTED_ITEM, selectedItem);
		if (selectedItem == null)
		{
			Page_Backing.log.error("Selected Item is null");
		}
	}
	
	/**
	 * Sets the System Title used in the title bar.
	 * 
	 * @param systemTitle
	 *            the systemTitle to set
	 */
	public void setSystemTitle(String systemTitle)
	{
		this.systemTitle = systemTitle;
	}
	
	/**
	 * Sets the title and message of an info message to be shown.
	 * 
	 * @param title
	 *            (String) Title of the info message popup.
	 * @param message
	 *            (String) Message of the info message popup.
	 */
	public void showInfoMessage(String title, String message)
	{
		this.infoTitle = title;
		this.infoMessage = message;
	}
}
