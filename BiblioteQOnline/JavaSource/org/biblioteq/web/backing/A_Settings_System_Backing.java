//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.backing
 * File: A_Settings_System_Backing.java
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
 * This backing bean provides all the methods required by the System Settings page.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jun 30, 2012, Clinton Bush, 1.0.0,
 *    New file.
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

/**
 * This backing bean provides all the methods required by the System Settings page.
 * 
 * @author Clint Bush
 * 
 */
@ManagedBean(name = "A_Settings_System_Backing")
@ViewScoped
public class A_Settings_System_Backing implements Serializable
{
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = -6593100834261672945L;
	
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(A_Settings_System_Backing.class);
	
	/**
	 * Variables backing the form fields.
	 */
	private boolean systemEnabled = true;
	private String systemDisabledMessage = "";
	private boolean allowOnlineRequests = true;
	private String agreementTermsMessage = "";
	private String agreementCheckboxMessage = "";
	private String requestConfirmationMessage = "";
	
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
	 * Returns the Agreement Checkbox Message on the Item Request Agreement page.
	 * 
	 * @return (String) The agreementCheckboxMessage.
	 */
	public String getAgreementCheckboxMessage()
	{
		return this.agreementCheckboxMessage;
	}
	
	/**
	 * Returns the Agreement Terms Message for the Item Request Agreement page.
	 * 
	 * @return (String) The agreementTermsMessage.
	 */
	public String getAgreementTermsMessage()
	{
		return this.agreementTermsMessage;
	}
	
	/**
	 * Returns the Confirmation Message displayed after a user places an online request.
	 * 
	 * @return (String) The requestConfirmationMessage.
	 */
	public String getRequestConfirmationMessage()
	{
		return this.requestConfirmationMessage;
	}
	
	/**
	 * Returns the System Disabled Message.
	 * 
	 * @return (String) The systemDisabledMessage.
	 */
	public String getSystemDisabledMessage()
	{
		return this.systemDisabledMessage;
	}
	
	/**
	 * This method pre-populates the settings on this page from the database.
	 */
	@PostConstruct
	public void init()
	{
		this.setSystemEnabled(this.settingEjb.getBooleanSettingByName(Constants.SETTING_SYSTEM_ENABLED));
		this.setSystemDisabledMessage(this.settingEjb.getStringSettingByName(Constants.SETTING_SYSTEM_DISABLED_MESSAGE));
		this.setAllowOnlineRequests(this.settingEjb.getBooleanSettingByName(Constants.SETTING_ALLOW_ONLINE_REQUEST));
		this.setAgreementTermsMessage(this.settingEjb.getStringSettingByName(Constants.SETTING_REQUEST_AGREEMENT_TERMS));
		this.setAgreementCheckboxMessage(this.settingEjb.getStringSettingByName(Constants.SETTING_REQUEST_AGREEMENT_CHECKBOX_MESSAGE));
		this.setRequestConfirmationMessage(this.settingEjb.getStringSettingByName(Constants.SETTING_REQUEST_CONFIRMATION_MESSAGE));
	}
	
	/**
	 * Returns the Allow Online Requests setting.
	 * 
	 * @return (boolean) The allowOnlineRequests setting.
	 */
	public boolean isAllowOnlineRequests()
	{
		return this.allowOnlineRequests;
	}
	
	/**
	 * Returns the value of the System Enabled Setting.
	 * 
	 * @return (boolean) The cbSystemEnabled.
	 */
	public boolean isSystemEnabled()
	{
		return this.systemEnabled;
	}
	
	/**
	 * Saves the Summary Page settings.
	 * 
	 * @param e
	 *            (ActionEvent) The ActionEvent generated by the UI element that called this method (i.e. the Save button).
	 */
	public void save(ActionEvent e)
	{
		// Persist the settings
		this.settingEjb.saveSetting(Constants.SETTING_SYSTEM_ENABLED, String.valueOf(this.systemEnabled));
		this.settingEjb.saveSetting(Constants.SETTING_SYSTEM_DISABLED_MESSAGE, this.systemDisabledMessage);
		this.settingEjb.saveSetting(Constants.SETTING_ALLOW_ONLINE_REQUEST, String.valueOf(this.allowOnlineRequests));
		this.settingEjb.saveSetting(Constants.SETTING_REQUEST_AGREEMENT_TERMS, this.agreementTermsMessage);
		this.settingEjb.saveSetting(Constants.SETTING_REQUEST_AGREEMENT_CHECKBOX_MESSAGE, this.agreementCheckboxMessage);
		this.settingEjb.saveSetting(Constants.SETTING_REQUEST_CONFIRMATION_MESSAGE, this.requestConfirmationMessage);
		
		// Display the Successful Save dialog
		this.pageBackingBean.showInfoMessage("Success!", "Your changes have been successfully saved!");
	}
	
	/**
	 * Sets the Agreement Checkbox Message on the Item Request Agreement page.
	 * 
	 * @param agreementCheckboxMessage
	 *            (String) The agreementCheckboxMessage to set.
	 */
	public void setAgreementCheckboxMessage(String agreementCheckboxMessage)
	{
		this.agreementCheckboxMessage = agreementCheckboxMessage;
	}
	
	/**
	 * Sets the Agreement Terms Message for the Item Request Agreement page.
	 * 
	 * @param agreementTermsMessage
	 *            (String) The agreementTermsMessage to set.
	 */
	public void setAgreementTermsMessage(String agreementTermsMessage)
	{
		this.agreementTermsMessage = agreementTermsMessage;
	}
	
	/**
	 * Sets the Allow Online Requests setting.
	 * 
	 * @param allowOnlineRequests
	 *            (boolean) The allowOnlineRequests to set.
	 */
	public void setAllowOnlineRequests(boolean allowOnlineRequests)
	{
		this.allowOnlineRequests = allowOnlineRequests;
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
	 * Sets the Confirmation Message displayed after a user places an online request.
	 * 
	 * @param requestConfirmationMessage
	 *            (String) The requestConfirmationMessage to set.
	 */
	public void setRequestConfirmationMessage(String requestConfirmationMessage)
	{
		this.requestConfirmationMessage = requestConfirmationMessage;
	}
	
	/**
	 * Sets the System Disabled Message.
	 * 
	 * @param systemDisabledMessage
	 *            (String) The systemDisabledMessage to set.
	 */
	public void setSystemDisabledMessage(String systemDisabledMessage)
	{
		this.systemDisabledMessage = systemDisabledMessage;
	}
	
	/**
	 * Sets the System Enabled setting.
	 * 
	 * @param cbSystemEnabled
	 *            (boolean) The cbSystemEnabled to set.
	 */
	public void setSystemEnabled(boolean systemEnabled)
	{
		this.systemEnabled = systemEnabled;
	}
}
