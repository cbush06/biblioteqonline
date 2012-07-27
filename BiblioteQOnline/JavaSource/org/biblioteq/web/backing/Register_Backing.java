//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.backing
 * File: Register_Backing.java
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
 * This request scoped bean backs the registration page for a user.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Apr 5, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.backing;

import java.util.ArrayList;
import java.util.Calendar;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.entities.Admin;
import org.biblioteq.ejb.entities.Member;
import org.biblioteq.ejb.interfaces.AdminBusinessLocal;
import org.biblioteq.ejb.interfaces.MemberBusinessLocal;
import org.biblioteq.ejb.interfaces.UserBusinessLocal;
import org.biblioteq.web.model.ValidationMessage_Model;

/**
 * This class backs the Registration page that allows current member's of the library to register for a new online account using their personal information.
 * 
 * @author Clint Bush
 * 
 */
@ManagedBean(name = "Register_Backing")
@ViewScoped
public class Register_Backing extends Screen_Backing
{
	/**
	 * Get the logger.
	 */
	private final Logger log = Logger.getLogger(Register_Backing.class);
	
	/**
	 * Get a copy of our MemberBusiness EJB for verifying the information.
	 */
	@EJB(name = "MemberBusiness")
	private MemberBusinessLocal memberEjb;
	
	/**
	 * Get a copy of our UserBusiness EJB for checking to see if the member already has a user account.
	 */
	@EJB(name = "UserBusiness")
	private UserBusinessLocal userEjb;
	
	/**
	 * Get a copy of our AdminBusiness EJB for checking to see if the user has an Admin account in BiblioteQ.
	 */
	@EJB(name = "AdminBusiness")
	private AdminBusinessLocal adminEjb;
	
	/**
	 * Wrapper of FacesMessage framework for handling error messages.
	 */
	private ValidationMessage_Model errorMsg = new ValidationMessage_Model();
	
	/**
	 * The Member whose information matches that provided by the user.
	 */
	private Member matchingMember = null;
	
	/*
	 * Data members backing our fields
	 */
	private String txtFirstName = "";
	private String txtMiddleInitial = "";
	private String txtLastName = "";
	private String txtUserName = "";
	private String txtPassword = "";
	private String txtVerifyPassword = "";
	private String dobMonth = "01";
	private String dobDay = "01";
	private String dobYear = String.valueOf((Calendar.getInstance()).get(Calendar.YEAR));
	private String email = "";
	private String phone1 = "";
	private String phone2 = "";
	private String phone3 = "";
	private String zipCode = "";
	private int registrationStep = 1;
	
	/**
	 * Default constructor.
	 */
	public Register_Backing()
	{
		
	}
	
	/**
	 * Generate a list of 31 days to back the date of birth day SelectMenu.
	 * 
	 * @return (ArrayList<SelectItem>) List of select items for all 31 days.
	 */
	public ArrayList<SelectItem> getDays()
	{
		ArrayList<SelectItem> dayList = new ArrayList<SelectItem>();
		
		for (int i = 0; i < 31; i++)
		{
			if (i < 9)
			{
				dayList.add(new SelectItem("0" + String.valueOf(i + 1), "0" + String.valueOf(i + 1)));
			}
			else
			{
				dayList.add(new SelectItem(String.valueOf(i + 1), String.valueOf(i + 1)));
			}
		}
		
		return dayList;
	}
	
	/**
	 * Converts the integer dobDay to a String and returns it to provide the current value of the date of birth day SelectMenu.
	 * 
	 * @return (String) The current date of birth day value.
	 */
	public String getDobDay()
	{
		return String.valueOf(this.dobDay);
	}
	
	/**
	 * Converts the integer dobMonth to a String and returns it to provide the current value of the date of birth month SelectMenu.
	 * 
	 * @return (String) The current date of birth month value.
	 */
	public String getDobMonth()
	{
		return String.valueOf(this.dobMonth);
	}
	
	/**
	 * Converts the integer dobYear to a String and returns it to provide a current value of the date of birth year SelectMenu.
	 * 
	 * @return (String) The current date of birth year value.
	 */
	public String getDobYear()
	{
		return String.valueOf(this.dobYear);
	}
	
	/**
	 * Returns the current value of the user's email address.
	 * 
	 * @return (String) The user's email address.
	 */
	public String getEmail()
	{
		return this.email;
	}
	
	/**
	 * Generate a list of 12 months to back the date of birth month SelectMenu.
	 * 
	 * @return (ArrayList<SelectItem>) List of select items for all 12 months.
	 */
	public ArrayList<SelectItem> getMonths()
	{
		ArrayList<SelectItem> monthList = new ArrayList<SelectItem>();
		
		for (int i = 0; i < 12; i++)
		{
			if (i < 9)
			{
				monthList.add(new SelectItem("0" + String.valueOf(i + 1), "0" + String.valueOf(i + 1)));
			}
			else
			{
				monthList.add(new SelectItem(String.valueOf(i + 1), String.valueOf(i + 1)));
			}
		}
		
		return monthList;
	}
	
	/**
	 * Returns the area code of the phone number.
	 * 
	 * @return (String) Phone area code.
	 */
	public String getPhone1()
	{
		return this.phone1;
	}
	
	/**
	 * Returns the prefix of the phone number.
	 * 
	 * @return (String) Phone prefix.
	 */
	public String getPhone2()
	{
		return this.phone2;
	}
	
	/**
	 * Returns the line number of the phone.
	 * 
	 * @return (String) Phone line number.
	 */
	public String getPhone3()
	{
		return this.phone3;
	}
	
	/**
	 * Returns the step of registration the user is currently on.
	 * 
	 * @return (int) Registration step.
	 */
	public int getRegistrationStep()
	{
		return this.registrationStep;
	}
	
	/**
	 * Returns the current value of the user's first name.
	 * 
	 * @return (String) User's first name.
	 */
	public String getTxtFirstName()
	{
		return this.txtFirstName;
	}
	
	/**
	 * Returns the current value of the user's last name.
	 * 
	 * @return (String) User's last name.
	 */
	public String getTxtLastName()
	{
		return this.txtLastName;
	}
	
	/**
	 * Returns the current value of the user's middle initial.
	 * 
	 * @return (String) The user's middle initial.
	 */
	public String getTxtMiddleInitial()
	{
		return this.txtMiddleInitial;
	}
	
	/**
	 * Returns the password.
	 * 
	 * @return (String) Password.
	 */
	public String getTxtPassword()
	{
		return this.txtPassword;
	}
	
	/**
	 * Returns the user name.
	 * 
	 * @return (String) User name.
	 */
	public String getTxtUserName()
	{
		return this.txtUserName;
	}
	
	/**
	 * Returns the verification password.
	 * 
	 * @return (String) Verification password.
	 */
	public String getTxtVerifyPassword()
	{
		return this.txtVerifyPassword;
	}
	
	/**
	 * Generate a list of 101 years to back the date of birth year SelectMenu. This starts at the current year goes back 100 years.
	 * 
	 * @return (ArrayList<SelectItem>) List of select items for 101 years starting with the current year and going backwards.
	 */
	public ArrayList<SelectItem> getYears()
	{
		int currentYear = (Calendar.getInstance()).get(Calendar.YEAR);
		
		ArrayList<SelectItem> yearList = new ArrayList<SelectItem>();
		
		for (int i = 0; i <= 100; i++)
		{
			yearList.add(new SelectItem(String.valueOf(currentYear - i), String.valueOf(currentYear - i)));
		}
		
		return yearList;
	}
	
	/**
	 * Returns the user's ZIP code.
	 * 
	 * @return (String) User's ZIP code.
	 */
	public String getZipCode()
	{
		return this.zipCode;
	}
	
	/**
	 * Registers the user if all fields are correctly filled out.
	 * 
	 * @return (String) The action for the JSF nav rules.
	 */
	public String register()
	{
		if (!(this.validateFields()))
		{
			this.errorMsg.renderMessages();
			return "";
		}
		return "";
	}
	
	/**
	 * Sets the current value of the date of birth day by converting the String parameter to an integer and setting dobDay.
	 * 
	 * @param dobDay
	 *            (String) The date of birth day value as a String.
	 */
	public void setDobDay(String dobDay)
	{
		this.dobDay = dobDay;
	}
	
	/**
	 * Sets the current value of the date of birth month by converting the String parameter to an integer and setting dobMonth.
	 * 
	 * @param dobMonth
	 *            (String) The date of birth month value as a String.
	 */
	public void setDobMonth(String dobMonth)
	{
		this.dobMonth = dobMonth;
	}
	
	/**
	 * Sets the current value of the date of birth year by converting the String parameter to an integer and setting dobYear.
	 * 
	 * @param dobYear
	 *            (String) The date of birth year value as a String.
	 */
	public void setDobYear(String dobYear)
	{
		this.dobYear = dobYear;
	}
	
	/**
	 * Sets the current value of the user's email address.
	 * 
	 * @param email
	 *            (String) The user's email address.
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	/**
	 * Sets the phone area code.
	 * 
	 * @param phone1
	 *            (String) Phone area code.
	 */
	public void setPhone1(String phone1)
	{
		this.phone1 = phone1;
	}
	
	/**
	 * Sets the phone prefix.
	 * 
	 * @param phone2
	 *            (String) Phone prefix.
	 */
	public void setPhone2(String phone2)
	{
		this.phone2 = phone2;
	}
	
	/**
	 * Sets the phone line number.
	 * 
	 * @param phone3
	 *            (String) Phone line number.
	 */
	public void setPhone3(String phone3)
	{
		this.phone3 = phone3;
	}
	
	/**
	 * Sets the value of the user's first name.
	 * 
	 * @param txtFirstName
	 *            (String) The user's first name.
	 */
	public void setTxtFirstName(String txtFirstName)
	{
		this.txtFirstName = txtFirstName;
	}
	
	/**
	 * Sets the value of the user's last name.
	 * 
	 * @param txtLastName
	 *            (String) The user's last name.
	 */
	public void setTxtLastName(String txtLastName)
	{
		this.txtLastName = txtLastName;
	}
	
	/**
	 * Sets the value of the user's middle initial.
	 * 
	 * @param txtMiddleInitial
	 *            (String) The user's middle initial.
	 */
	public void setTxtMiddleInitial(String txtMiddleInitial)
	{
		this.txtMiddleInitial = txtMiddleInitial;
	}
	
	/**
	 * Sets the password.
	 * 
	 * @param txtPassword
	 *            (String) The password.
	 */
	public void setTxtPassword(String txtPassword)
	{
		this.txtPassword = txtPassword;
	}
	
	/**
	 * Sets the value of the user name.
	 * 
	 * @param txtUserName
	 *            (String) The user name.
	 */
	public void setTxtUserName(String txtUserName)
	{
		this.txtUserName = txtUserName;
	}
	
	/**
	 * Sets the value of the verification password.
	 * 
	 * @param txtVerifyPassword
	 *            (String) The verification password.
	 */
	public void setTxtVerifyPassword(String txtVerifyPassword)
	{
		this.txtVerifyPassword = txtVerifyPassword;
	}
	
	/**
	 * Sets the user's ZIP code.
	 * 
	 * @param zipCode
	 *            (String) The user's ZIP code.
	 */
	public void setZipCode(String zipCode)
	{
		this.zipCode = zipCode;
	}
	
	/**
	 * Perform final validation steps.
	 * 
	 * @return (boolean) True if validation passed; otherwise, false.
	 */
	public boolean validateFields()
	{
		boolean returnVal = true;
		
		// Validation for Step 1
		if (this.registrationStep == 1)
		{
			if (this.phone1.length() != 3 || this.phone2.length() != 3 || this.phone3.length() != 4)
			{
				this.errorMsg.addMessage("A valid phone number is required.");
				returnVal = false;
			}
			
			// Verify the information if everything is validated
			if (returnVal)
			{
				// Try to find a library member with matching information
				Member result = this.memberEjb.getMatchingMember(this.txtFirstName, this.txtLastName, this.dobMonth + "/" + this.dobDay
				        + "/" + this.dobYear, this.phone1 + "-" + this.phone2 + "-" + this.phone3, this.zipCode, this.email);
				if (result != null)
				{
					// Ensure they do not already have an account
					if (this.userEjb.getUserForMember(result) != null)
					{
						this.errorMsg
						        .addMessage("You already have an online user account. If you forgot your username or password, use the links on the log in page to recover your username or reset your password.");
						returnVal = false;
					}
					else
					{
						// If a matching member is found and they do not already have an account, store their information
						this.matchingMember = result;
					}
				}
				else
				{
					this.errorMsg
					        .addMessage("The information you provided did not match a library member's record. Please check the information for errors or contact the libary to be added as a member.");
					returnVal = false;
				}
			}
			
			// Increment to the next step if the member record was found
			if (returnVal)
			{
				this.registrationStep++;
			}
			
			// Now that we're done validating, return the value before reaching the next step
			return returnVal;
		}
		
		// Validation for Step 2
		if (this.registrationStep == 2)
		{
			// Ensure the username is not already used
			if (this.userEjb.getUser(this.txtUserName) != null)
			{
				this.errorMsg.addMessage("The username you entered is already in use. Please choose another username.");
				returnVal = false;
			}
			
			// Ensure the passwords match
			if (!(this.txtPassword.equals(this.txtVerifyPassword)))
			{
				this.errorMsg.addMessage("The [Password] and [Verify Password] fields must match exactly.");
				returnVal = false;
			}
			
			// If everything checks out, increment the step, create a new account, and return
			if (returnVal)
			{
				// Increment to the next step if everything is validated
				this.registrationStep++;
				
				// If this user is also an Admin, link the two together
				Admin userAdmin = this.adminEjb.getMatchingAdmin(this.txtUserName);
				
				// Create the account
				this.userEjb.createNewUser(this.txtUserName, this.txtPassword, this.matchingMember, userAdmin, true, null);
				
				// Now that we're done, return the value
				return returnVal;
			}
		}
		
		return returnVal;
	}
}
