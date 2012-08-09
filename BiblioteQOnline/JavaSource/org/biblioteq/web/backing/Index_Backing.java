//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQ Online
 * Package: org.biblioteq.web.backing
 * File: Index_Backing.java
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
 * This backing bean backs the home page.
 * 
 * #######################
 * #      Revision       #
 * ####################### 
 * 03 Mar 2012, Clinton Bush, 1.0.0,
 *    New file.
 *    
 * 08 Aug 2012, Clinton Bush, 1.1.2,
 *    Implemented the Serializable interface.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on

package org.biblioteq.web.backing;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.entities.User;
import org.biblioteq.ejb.interfaces.SettingBusinessLocal;
import org.biblioteq.web.common.Constants;
import org.biblioteq.web.model.ValidationMessage_Model;
import org.biblioteq.web.security.Login_Security;

@ManagedBean(name = "Index_Backing")
@ViewScoped
public class Index_Backing extends Screen_Backing implements Serializable
{
	/**
	 * GUID for implementing Serializable
	 */
	private static final long serialVersionUID = -2222049632808163955L;
	
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(Index_Backing.class);
	
	/**
	 * Get a copy of the Login_Security bean. This will be injected via the setLoginBean(Login_Security loginBean) method.
	 */
	@ManagedProperty("#{Login_Security}")
	private Login_Security loginBean;
	
	/**
	 * Get a copy of the Page_Backing bean. This will be injected via the setPageBackingBean(Page_Backing pageBackingBean) method.
	 */
	@ManagedProperty("#{Page_Backing}")
	private Page_Backing pageBackingBean;
	
	@EJB(name = "SettingBusiness")
	private SettingBusinessLocal settingEjb;
	
	/**
	 * Wrapper of FacesMessage framework for handling error messages.
	 */
	private ValidationMessage_Model errorMsg = new ValidationMessage_Model();
	
	public String txtUserName = "";
	
	public String txtPassword = "";
	
	/**
	 * Returns the message shown if the system is disabled.
	 * 
	 * @return (String) System disabled message.
	 */
	public String getSystemDisabledMessage()
	{
		return this.settingEjb.getStringSettingByName(Constants.SETTING_SYSTEM_DISABLED_MESSAGE);
	}
	
	public String getTxtPassword()
	{
		return this.txtPassword;
	}
	
	public String getTxtUserName()
	{
		return this.txtUserName;
	}
	
	/**
	 * Returns true if the system is enabled; false, otherwise.
	 * 
	 * @return (boolean) System enabled.
	 */
	public boolean isSystemEnabled()
	{
		return this.settingEjb.getBooleanSettingByName(Constants.SETTING_SYSTEM_ENABLED);
	}
	
	/**
	 * Performs the log in for the user.
	 * 
	 * @return (String) The action for JSF navigation rules.
	 */
	public String login()
	{
		// Don't bother verifying the login unless the user enters their username and password
		if (this.txtUserName.equals("") || this.txtPassword.equals(""))
		{
			this.errorMsg.addMessage("Username and Password are required fields!");
			this.errorMsg.renderMessages();
			return "";
		}
		
		// Attempt to verify the user's credentials
		if (this.loginBean.login(this.txtUserName, this.txtPassword))
		{
			// If the system is disabled, only Administrators can log in
			if (!(this.isSystemEnabled()))
			{
				// If the user isn't an Administrator, remove them from the Session Map and show an error message
				if (((User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Constants.SESSION_LOGGED_IN_USER))
				        .getAdmin() == null)
				{
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(Constants.SESSION_LOGGED_IN_USER);
					this.errorMsg.addMessage("The system is currently disabled. Only Administrators are allowed to log in.");
					this.errorMsg.renderMessages();
					return "";
				}
			}
			
			// Go ahead and set the first page to be rendered in the Restricted area
			((Page_Backing) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Page_Backing"))
			        .setRenderPage(Constants.PAGE_RESTRICTED_INDEX);
			
			// Redirect the user to the Restricted area
			return "login";
		}
		
		// If the login failed, throw an error and return
		this.errorMsg.addMessage("The username and/or password provided could not be verified.");
		this.errorMsg.renderMessages();
		return "";
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
	 * Allow the injection of the Page_Backing bean.
	 * 
	 * @param pageBackingBean
	 *            (Page_Backing) The injected instance of the Page_Backing bean.
	 */
	public void setPageBackingBean(Page_Backing pageBackingBean)
	{
		this.pageBackingBean = pageBackingBean;
	}
	
	public void setTxtPassword(String txtPassword)
	{
		this.txtPassword = txtPassword;
	}
	
	public void setTxtUserName(String txtUserName)
	{
		this.txtUserName = txtUserName;
	}
}
