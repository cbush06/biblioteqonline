//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.backing
 * File: R_MyAccount_Backing.java
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
 * This provides the backing class for all "MyAccount" pages.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jul 4, 2012, Clinton Bush, 1.0.0,
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
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.entities.User;
import org.biblioteq.ejb.interfaces.MemberBusinessLocal;
import org.biblioteq.ejb.interfaces.UserBusinessLocal;
import org.biblioteq.web.common.Constants;
import org.biblioteq.web.model.ValidationMessage_Model;

/**
 * This provides the backing class for all "MyAccount" pages.
 * 
 * @author Clint Bush
 * 
 */
@ManagedBean(name = "R_MyAccount_Backing")
@ViewScoped
public class R_MyAccount_Backing implements Serializable
{
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 4662307298860616085L;
	
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(R_MyAccount_Backing.class);
	
	/**
	 * Get the User object.
	 */
	private User currentUser = null;
	
	/**
	 * Indicates if the password should be validated for change.
	 */
	private boolean isChangePassword = false;
	
	private String password1 = "";
	
	private String password2 = "";
	
	/**
	 * Get the utility for handling error messages.
	 */
	ValidationMessage_Model errorMessages = new ValidationMessage_Model();
	
	/**
	 * Get a copy of the Page_Backing bean. This will be injected via the setPageBacking(Page_Backing pageBacking) method.
	 */
	@ManagedProperty("#{Page_Backing}")
	private Page_Backing pageBacking;
	
	/**
	 * Get a copy of the User EJB.
	 */
	@EJB(name = "UserBusiness")
	private UserBusinessLocal userEjb;
	
	/**
	 * Get a copy of the Member EJB.
	 */
	@EJB(name = "MemberBusiness")
	private MemberBusinessLocal memberEjb;
	
	/**
	 * Default constructor.
	 */
	public R_MyAccount_Backing()
	{
		
	}
	
	/**
	 * Returns the current user that is being edited.
	 * 
	 * @return the currentUser
	 */
	public User getCurrentUser()
	{
		return this.currentUser;
	}
	
	/**
	 * Returns the first password in the 2 passwords required for changing.
	 * 
	 * @return the password1
	 */
	public String getPassword1()
	{
		return this.password1;
	}
	
	/**
	 * Returns the verification password in the 2 passwords required for changing.
	 * 
	 * @return the password2
	 */
	public String getPassword2()
	{
		return this.password2;
	}
	
	/**
	 * Event listener for navigating to the "My Info" page in the My Account section.
	 * 
	 * @param e
	 *            (ActionEvent) Object generated by the action of the component calling this method.
	 */
	public void gotoMyAccountInfo(ActionEvent e)
	{
		this.pageBacking.setRenderPage(Constants.PAGE_RESTRICTED_MYACCOUNT);
	}
	
	/**
	 * Handle initialization tasks.
	 */
	@PostConstruct
	public void init()
	{
		this.currentUser = this.userEjb.refreshUser(this.pageBacking.getCurrentUser());
		this.pageBacking.setCurrentUser(this.currentUser);
	}
	
	/**
	 * Returns a boolean indicating if the password is to be changed or not.
	 * 
	 * @return the isChangePassword
	 */
	public boolean isChangePassword()
	{
		return this.isChangePassword;
	}
	
	/**
	 * Saves the changes to the currentlySelectedUser and then sets it to null to close it.
	 * 
	 * @param e
	 *            (AjaxBehaviorEvent) The ActionEvent object generated by the component that called this method.
	 */
	public void saveUserListener(AjaxBehaviorEvent e)
	{
		if (FacesContext.getCurrentInstance().getMessageList().size() < 1)
		{
			// Validate the fields
			this.validate(this.currentUser);
			
			// Check if our ValidationMessage_Model has any error messages
			if (!(this.errorMessages.hasMessages()))
			{
				// Save the changes
				this.currentUser.setMember(this.memberEjb.saveMember(this.currentUser.getMember()));
				this.userEjb.saveUser(this.currentUser);
				
				// Re-initialize to get a clean copy of the user
				this.init();
				
				// Reset flags
				this.isChangePassword = false;
				
				// Show a confirmation message
				this.pageBacking.showInfoMessage("Success!", "Your changes have been saved!");
			}
			else
			{
				// Render Error Messages and Refresh our User List to Remove temporary changes
				this.errorMessages.renderMessages();
				this.init();
			}
		}
	}
	
	/**
	 * Sets the property that indicates if the passwords should be changed.
	 * 
	 * @param isChangePassword
	 *            the isChangePassword to set
	 */
	public void setChangePassword(boolean isChangePassword)
	{
		this.isChangePassword = isChangePassword;
	}
	
	/**
	 * Sets the current user that is being edited.
	 * 
	 * @param currentUser
	 *            the currentUser to set
	 */
	public void setCurrentUser(User currentUser)
	{
		this.currentUser = currentUser;
	}
	
	/**
	 * Allows the injection of the Page_Backing bean.
	 * 
	 * @param pageBacking
	 *            the pageBacking to set
	 */
	public void setPageBacking(Page_Backing pageBacking)
	{
		this.pageBacking = pageBacking;
	}
	
	/**
	 * Sets the first password in the 2 password verification system for changing the password.
	 * 
	 * @param password1
	 *            the password1 to set
	 */
	public void setPassword1(String password1)
	{
		this.password1 = password1;
	}
	
	/**
	 * Sets the verification password.
	 * 
	 * @param password2
	 *            the password2 to set
	 */
	public void setPassword2(String password2)
	{
		this.password2 = password2;
	}
	
	/**
	 * Validate the fields prior to saving.
	 * 
	 * @return (boolean)
	 */
	public void validate(User userToValidate)
	{
		/*
		 * USERNAME
		 */
		User userWithUsername = this.userEjb.getUser(userToValidate.getUserName());
		if (userWithUsername != null && userWithUsername.getId() != userToValidate.getId())
		{
			this.errorMessages.addMessage("The User Name has been changed to a non-unique username and cannot be saved.");
		}
		
		if (userToValidate.getUserName().length() < 1)
		{
			this.errorMessages.addMessage("The User Name cannot be empty.");
		}
		
		/*
		 * PASSWORD
		 */
		if (this.isChangePassword)
		{
			if (this.password1.length() < 1)
			{
				this.errorMessages.addMessage("Your new password must not be blank.");
			}
			else
				if (!(this.password1.equals(this.password2)))
				{
					this.errorMessages.addMessage("The new password and verification password do not match.");
				}
				else
				{
					userToValidate.setPassword(this.password1);
				}
		}
		
		/*
		 * FIRST AND LAST NAME
		 */
		if (userToValidate.getMember().getFirstName().length() < 1 || userToValidate.getMember().getLastName().length() < 1)
		{
			this.errorMessages.addMessage("First and Last Name Fields cannot be blank.");
		}
		
		/*
		 * STREET ADDRESS
		 */
		if (userToValidate.getMember().getStreetAddress().length() < 1)
		{
			this.errorMessages.addMessage("The Street Address Field cannot be blank.");
		}
		
		/*
		 * CITY
		 */
		if (userToValidate.getMember().getCity().length() < 1)
		{
			this.errorMessages.addMessage("The City Field cannot be blank.");
		}
		
		/*
		 * ZIP
		 */
		if (userToValidate.getMember().getZip().length() < 1)
		{
			this.errorMessages.addMessage("The ZIP Field cannot be blank.");
		}
		
		/*
		 * PHONE
		 */
		if (userToValidate.getMember().getTelephone().length() < 1)
		{
			this.errorMessages.addMessage("The Phone Field cannot be blank.");
		}
		
		/*
		 * E-MAIL
		 */
		if (userToValidate.getMember().getEmail().length() < 1)
		{
			this.errorMessages.addMessage("The E-Mail Field cannot be blank.");
		}
	}
}