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
 * Aug 07, 2012, Clinton Bush, 1.1.2,
 *    Added setting for the System Title and Header.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.backing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

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
	private boolean showImageUpload = false;
	private String systemTitle = "";
	private boolean systemEnabled = true;
	private boolean systemHeaderUseDefault = true;
	private String systemDisabledMessage = "";
	private boolean allowOnlineRequests = true;
	private String agreementTermsMessage = "";
	private String agreementCheckboxMessage = "";
	private String requestConfirmationMessage = "";
	
	/**
	 * Error handler.
	 */
	private ValidationMessage_Model errorMessages = new ValidationMessage_Model();
	
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
	 * Returns the title shown in the title bar for the entire web site.
	 * 
	 * @return (String) The systemTitle.
	 */
	public String getSystemTitle()
	{
		return this.systemTitle;
	}
	
	/**
	 * This method pre-populates the settings on this page from the database.
	 */
	@PostConstruct
	public void init()
	{
		this.setSystemTitle(this.settingEjb.getStringSettingByName(Constants.SETTING_SYSTEM_TITLE));
		this.setSystemEnabled(this.settingEjb.getBooleanSettingByName(Constants.SETTING_SYSTEM_ENABLED));
		this.setSystemDisabledMessage(this.settingEjb.getStringSettingByName(Constants.SETTING_SYSTEM_DISABLED_MESSAGE));
		this.setAllowOnlineRequests(this.settingEjb.getBooleanSettingByName(Constants.SETTING_ALLOW_ONLINE_REQUEST));
		this.setAgreementTermsMessage(this.settingEjb.getStringSettingByName(Constants.SETTING_REQUEST_AGREEMENT_TERMS));
		this.setAgreementCheckboxMessage(this.settingEjb.getStringSettingByName(Constants.SETTING_REQUEST_AGREEMENT_CHECKBOX_MESSAGE));
		this.setRequestConfirmationMessage(this.settingEjb.getStringSettingByName(Constants.SETTING_REQUEST_CONFIRMATION_MESSAGE));
		this.setSystemHeaderUseDefault(!(this.settingEjb.getBooleanSettingByName(Constants.SETTING_SYSTEM_CUSTOM_HEADER)));
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
	 * If true, the image upload box will be shown.
	 * 
	 * @return (boolean) The showImageUpload.
	 */
	public boolean isShowImageUpload()
	{
		return this.showImageUpload;
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
	 * Returns true if the user has selected to use the default header.
	 * 
	 * @return (boolean) The systemHeaderUseDefault.
	 */
	public boolean isSystemHeaderUseDefault()
	{
		return this.systemHeaderUseDefault;
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
		this.settingEjb.saveSetting(Constants.SETTING_SYSTEM_TITLE, this.systemTitle);
		this.settingEjb.saveSetting(Constants.SETTING_SYSTEM_ENABLED, String.valueOf(this.systemEnabled));
		this.settingEjb.saveSetting(Constants.SETTING_SYSTEM_DISABLED_MESSAGE, this.systemDisabledMessage);
		this.settingEjb.saveSetting(Constants.SETTING_ALLOW_ONLINE_REQUEST, String.valueOf(this.allowOnlineRequests));
		this.settingEjb.saveSetting(Constants.SETTING_REQUEST_AGREEMENT_TERMS, this.agreementTermsMessage);
		this.settingEjb.saveSetting(Constants.SETTING_REQUEST_AGREEMENT_CHECKBOX_MESSAGE, this.agreementCheckboxMessage);
		this.settingEjb.saveSetting(Constants.SETTING_REQUEST_CONFIRMATION_MESSAGE, this.requestConfirmationMessage);
		this.settingEjb.saveSetting(Constants.SETTING_SYSTEM_CUSTOM_HEADER, String.valueOf(!(this.systemHeaderUseDefault)));
		
		// Update the system title in the Page_Backing Bean
		this.pageBackingBean.setSystemTitle(this.systemTitle);
		
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
	 * Sets the value determining if the image upload box is shown.
	 * 
	 * @param showImageUpload
	 *            (boolean) The showImageUpload to set.
	 */
	public void setShowImageUpload(boolean showImageUpload)
	{
		this.showImageUpload = showImageUpload;
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
	
	/**
	 * Sets if the user has selected to use the default header.
	 * 
	 * @param systemHeaderUseDefault
	 *            (boolean) The systemHeaderUseDefault to set.
	 */
	public void setSystemHeaderUseDefault(boolean systemHeaderUseDefault)
	{
		this.systemHeaderUseDefault = systemHeaderUseDefault;
	}
	
	/**
	 * Sets the title shown in the title bar for the entire web site.
	 * 
	 * @param systemTitle
	 *            (String) The systemTitle to set.
	 */
	public void setSystemTitle(String systemTitle)
	{
		this.systemTitle = systemTitle;
	}
	
	/**
	 * Receives a custom Header Image file.
	 * 
	 * @param e
	 *            (FileUploadEvent) The event generated by the upload action.
	 */
	public void uploadListener(FileUploadEvent e)
	{
		UploadedFile item = e.getUploadedFile();
		File saveDirectory = new File(File.separator + Constants.PATH_CUSTOM_IMAGES + File.separator);
		File saveFile = null;
		FileOutputStream os = null;
		String fileExtension = "";
		InputStream is = null;
		byte buffer[] = new byte[512];
		
		// Ensure our directory exists
		if (!(saveDirectory.exists()))
		{
			saveDirectory.mkdir();
		}
		
		// Attempt to save the image
		if (item.getContentType().equals("image/jpg") || item.getContentType().equals("image/jpeg")
		        || item.getContentType().equals("image/gif") || item.getContentType().equals("image/png"))
		{
			//@formatter:off
			if (item.getContentType().equals("image/jpg") || item.getContentType().equals("image/jpeg"))
			{
				fileExtension = "jpg";
				saveFile = new File(File.separator + Constants.PATH_CUSTOM_IMAGES + File.separator + "header." + fileExtension);
			}
			else if (item.getContentType().equals("image/gif"))
			{
				fileExtension = "gif";
				saveFile = new File(File.separator + Constants.PATH_CUSTOM_IMAGES + File.separator + "header." + fileExtension);
			}
			else
			{
				fileExtension = "png";
				saveFile = new File(File.separator + Constants.PATH_CUSTOM_IMAGES + File.separator + "header." + fileExtension);
			}
			//@formatter:on
			
			try
			{
				// If the file doesn't exist, create it
				if (!(saveFile.exists()))
				{
					A_Settings_System_Backing.log.info(saveFile.getCanonicalPath());
					saveFile.createNewFile();
				}
				
				// Open our I/O Streams
				os = new FileOutputStream(saveFile);
				is = item.getInputStream();
				
				// Save the file to disk
				while (is.read(buffer, 0, buffer.length) > 0)
				{
					os.write(buffer);
				}
				
				// Close the streams
				os.close();
				is.close();
			}
			catch (IOException e1)
			{
				A_Settings_System_Backing.log.error("An error occurred while copying the file to disk.");
				e1.printStackTrace();
			}
			
			// Update the header type setting
			this.settingEjb.saveSetting(Constants.SETTING_SYSTEM_HEADER_TYPE, fileExtension);
		}
		else
		{
			this.errorMessages.addMessage("Only JPEG, GIF, or PNG files are allowed as the custom header.");
			this.errorMessages.renderMessages();
		}
	}
}
