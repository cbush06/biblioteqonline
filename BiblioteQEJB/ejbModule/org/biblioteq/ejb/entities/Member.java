//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: Member.java
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
 * This entity represents a library member, as used by BiblioteQ. It is mapped to the "member" table. 
 * 
 * #######################
 * #      Revision       #
 * ####################### 
 * Mar 4, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.ejb.entities;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

/**
 * @author Clint Bush
 * 
 */
@Entity(name = "Member")
@Table(name = "member")
public class Member implements Serializable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(Member.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 913403553943254090L;
	
	private String memberId = "";
	private String memberSince = "";
	private String dob = "";
	private String sex = "";
	private String firstName = "";
	private String middleInitial = "";
	private String lastName = "";
	private String telephone = "";
	private String streetAddress = "";
	private String city = "";
	private String state = "";
	private String zip = "";
	private String email = "";
	private String expirationDate = "";
	private float overdueFees = 0.00f;
	private String comments = "";
	private String generalRegistrationNumber = "";
	private User user = null;
	
	private String memberClass = "";
	
	/**
	 * Returns the city of residence of the member.
	 * 
	 * @return (String) City the member resides in.
	 */
	@Column(name = "city", length = 256)
	public String getCity()
	{
		return this.city;
	}
	
	/**
	 * Returns comments about the member.
	 * 
	 * @return (String) The comments.
	 */
	@Column(name = "comments")
	public String getComments()
	{
		return this.comments;
	}
	
	/**
	 * Returns the date of birth of the member in String format.
	 * 
	 * @return (String) Date of birth of the member.
	 */
	@Column(name = "dob", length = 32)
	public String getDob()
	{
		return this.dob;
	}
	
	/**
	 * Returns the date of birth of the member as a Date object.
	 * 
	 * @return (Date) Date of birth of the member.
	 */
	@Transient
	public Date getDobAsDate()
	{
		try
		{
			return (new SimpleDateFormat("MM/dd/yyyy")).parse(this.getDob());
		}
		catch (ParseException e)
		{
			Member.log.error(e.getMessage() + "\n" + e.getStackTrace());
			return null;
		}
	}
	
	/**
	 * Returns the member's e-mail address.
	 * 
	 * @return (String) E-mail address of the member.
	 */
	@Column(name = "email", length = 128)
	public String getEmail()
	{
		return this.email;
	}
	
	/**
	 * Returns the expiration date of the member as a Date object.
	 * 
	 * @return (Date) Expiration date of the member.
	 */
	@Transient
	public Date getExpirationAsDate()
	{
		try
		{
			return (new SimpleDateFormat("MM/dd/yyyy")).parse(this.getExpirationDate());
		}
		catch (ParseException e)
		{
			Member.log.error(e.getMessage() + "\n" + e.getStackTrace());
			return null;
		}
	}
	
	/**
	 * Returns the expiration date of this member's account in String format.
	 * 
	 * @return (String) Expiration date of the member's account.
	 */
	@Column(name = "expiration_date", length = 32)
	public String getExpirationDate()
	{
		return this.expirationDate;
	}
	
	/**
	 * Returns the first name of the member.
	 * 
	 * @return (String) First name of the member.
	 */
	@Column(name = "first_name", length = 128)
	public String getFirstName()
	{
		return this.firstName;
	}
	
	/**
	 * Returns the General Registration Number of the member.
	 * 
	 * @return (String) The generalRegistrationNumber.
	 */
	@Column(name = "general_registration_number")
	public String getGeneralRegistrationNumber()
	{
		return this.generalRegistrationNumber;
	}
	
	/**
	 * Returns the member's last name.
	 * 
	 * @return (String) Member's last name.
	 */
	@Column(name = "last_name", length = 128)
	public String getLastName()
	{
		return this.lastName;
	}
	
	/**
	 * Returns the Member Class of the member.
	 * 
	 * @return The memberClass.
	 */
	@Column(name = "memberclass")
	public String getMemberClass()
	{
		return this.memberClass;
	}
	
	/**
	 * Returns the Member ID generated by BiblioteQ.
	 * 
	 * @return (String) The Member ID.
	 */
	@Id
	@Column(name = "memberid", length = 16)
	public String getMemberId()
	{
		return this.memberId;
	}
	
	/**
	 * Returns the date the member became a member in String format.
	 * 
	 * @return (String) The date the member became a member.
	 */
	@Column(name = "membersince", length = 32)
	public String getMemberSince()
	{
		return this.memberSince;
	}
	
	/**
	 * Returns the date the member became a member as a Date object.
	 * 
	 * @return (Date) The date the member became a member.
	 */
	@Transient
	public Date getMemberSinceAsDate()
	{
		try
		{
			return (new SimpleDateFormat("MM/dd/yyyy")).parse(this.getMemberSince());
		}
		catch (ParseException e)
		{
			Member.log.error(e.getMessage() + "\n" + e.getStackTrace());
			return null;
		}
	}
	
	/**
	 * Returns the middle initial of the member.
	 * 
	 * @return (String) The member's middle initial.
	 */
	@Column(name = "middle_init", length = 1)
	public String getMiddleInitial()
	{
		return this.middleInitial;
	}
	
	/**
	 * Returns the overdue fees owed by the member as a float.
	 * 
	 * @return (float) The overdue fees owed by the user.
	 */
	@Column(name = "overdue_fees")
	public float getOverdueFees()
	{
		return this.overdueFees;
	}
	
	/**
	 * Returns the sex of the member.
	 * 
	 * @return (String) Sex of the member.
	 */
	@Column(name = "sex", length = 8)
	public String getSex()
	{
		return this.sex;
	}
	
	/**
	 * Returns the state of residence of the member.
	 * 
	 * @return (String) State the member resides in.
	 */
	@Column(name = "state_abbr", length = 16)
	public String getState()
	{
		return this.state;
	}
	
	/**
	 * Returns the street address of the member.
	 * 
	 * @return (String) Member's street address.
	 */
	@Column(name = "street", length = 256)
	public String getStreetAddress()
	{
		return this.streetAddress;
	}
	
	/**
	 * Returns the telephone number of the member in String format.
	 * 
	 * @return (String) Member's telephone number.
	 */
	@Column(name = "telephone_num", length = 32)
	public String getTelephone()
	{
		return this.telephone;
	}
	
	/**
	 * Returns the User object associated with this Member (if one exists).
	 * 
	 * @return the user
	 */
	@OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
	public User getUser()
	{
		return this.user;
	}
	
	/**
	 * Returns the member's ZIP code in String format.
	 * 
	 * @return (String) ZIP code of the member.
	 */
	@Column(name = "zip", length = 16)
	public String getZip()
	{
		return this.zip;
	}
	
	/**
	 * Sets the member's city.
	 * 
	 * @param city
	 *            (String)
	 */
	public void setCity(String city)
	{
		this.city = city;
	}
	
	/**
	 * Returns comments about the member.
	 * 
	 * @param comments
	 *            (String) The comments to set.
	 */
	public void setComments(String comments)
	{
		this.comments = comments;
	}
	
	/**
	 * Sets the member's Date of Birth.
	 * 
	 * @param dob
	 *            (String)
	 */
	public void setDob(String dob)
	{
		this.dob = dob;
	}
	
	/**
	 * Sets the member's Date of Birth.
	 * 
	 * @param dob
	 *            (Date)
	 */
	public void setDobAsDate(Date dob)
	{
		// Prepare the format patterns
		String monthDayFormat = "%02d";
		String yearFormat = "%d";
		
		// Use the Calendar class to get the month, day, and year values from the Date object
		Calendar dobCal = Calendar.getInstance();
		dobCal.setTime(dob);
		
		// Use the String Formatter to prepare the String value of the date
		this.dob = String.format(monthDayFormat, dobCal.get(Calendar.MONTH) + 1) + "/"
		        + String.format(monthDayFormat, dobCal.get(Calendar.DAY_OF_MONTH)) + "/"
		        + String.format(yearFormat, dobCal.get(Calendar.YEAR));
	}
	
	/**
	 * Sets the member's e-mail address.
	 * 
	 * @param eMail
	 *            (String)
	 */
	public void setEmail(String eMail)
	{
		this.email = eMail;
	}
	
	/**
	 * Sets the member's Expiration Date.
	 * 
	 * @param expiration
	 *            (Date)
	 */
	public void setExpirationAsDate(Date expiration)
	{
		// Prepare the format patterns
		String monthDayFormat = "%02d";
		String yearFormat = "%d";
		
		// Use the Calendar class to get the month, day, and year values from the Date object
		Calendar expCal = Calendar.getInstance();
		expCal.setTime(expiration);
		
		// Use the String Formatter to prepare the String value of the date
		this.expirationDate = String.format(monthDayFormat, expCal.get(Calendar.MONTH) + 1) + "/"
		        + String.format(monthDayFormat, expCal.get(Calendar.DAY_OF_MONTH)) + "/"
		        + String.format(yearFormat, expCal.get(Calendar.YEAR));
	}
	
	/**
	 * Sets the member account's expiration date.
	 * 
	 * @param expirationDate
	 *            (String)
	 */
	public void setExpirationDate(String expirationDate)
	{
		this.expirationDate = expirationDate;
	}
	
	/**
	 * Sets the member's first name.
	 * 
	 * @param firstName
	 *            (String)
	 */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	/**
	 * Sets the General Registration Number of the member.
	 * 
	 * @param generalRegistrationNumber
	 *            (String) The generalRegistrationNumber to set.
	 */
	public void setGeneralRegistrationNumber(String generalRegistrationNumber)
	{
		this.generalRegistrationNumber = generalRegistrationNumber;
	}
	
	/**
	 * Sets the member's last name.
	 * 
	 * @param lastName
	 *            (String)
	 */
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
	/**
	 * Sets the Member Class of the member.
	 * 
	 * @param memberClass
	 *            (String) The memberClass to set.
	 */
	public void setMemberClass(String memberClass)
	{
		this.memberClass = memberClass;
	}
	
	/**
	 * Sets the member's member ID. Should be private and never used.
	 * 
	 * @param memberId
	 *            (String)
	 */
	private void setMemberId(String memberId)
	{
		this.memberId = memberId;
	}
	
	/**
	 * Sets the member account's creation date.
	 * 
	 * @param memberSince
	 *            (String)
	 */
	public void setMemberSince(String memberSince)
	{
		this.memberSince = memberSince;
	}
	
	/**
	 * Sets the member account's creation date.
	 * 
	 * @param memberSince
	 *            (Date)
	 */
	public void setMemberSinceAsDate(Date memberSince)
	{
		// Prepare the format patterns
		String monthDayFormat = "%02d";
		String yearFormat = "%d";
		
		// Use the Calendar class to get the month, day, and year values from the Date object
		Calendar memberSinceCal = Calendar.getInstance();
		memberSinceCal.setTime(memberSince);
		
		// Use the String Formatter to prepare the String value of the date
		this.memberSince = String.format(monthDayFormat, memberSinceCal.get(Calendar.MONTH) + 1) + "/"
		        + String.format(monthDayFormat, memberSinceCal.get(Calendar.DAY_OF_MONTH)) + "/"
		        + String.format(yearFormat, memberSinceCal.get(Calendar.YEAR));
	}
	
	/**
	 * Sets the member's middle initial.
	 * 
	 * @param middleInitial
	 *            (String)
	 */
	public void setMiddleInitial(String middleInitial)
	{
		this.middleInitial = middleInitial;
	}
	
	/**
	 * Sets the member's overdue fees.
	 * 
	 * @param overdueFees
	 *            (float)
	 */
	public void setOverdueFees(float overdueFees)
	{
		this.overdueFees = overdueFees;
	}
	
	/**
	 * Sets the member's sex (Male/Female).
	 * 
	 * @param sex
	 *            (String)
	 */
	public void setSex(String sex)
	{
		if (!(sex.equals("Male")) && !(sex.equals("Female")))
		{
			Member.log.error("Invalid sex for Member! Must be \"Female\" or \"Male\". Defaulting to \"Female\".");
			this.sex = "Female";
			return;
		}
		this.sex = sex;
	}
	
	/**
	 * Sets the member's state of residence.
	 * 
	 * @param state
	 *            (String)
	 */
	public void setState(String state)
	{
		this.state = state;
	}
	
	/**
	 * Sets the member's street address.
	 * 
	 * @param streetAddress
	 *            (String)
	 */
	public void setStreetAddress(String streetAddress)
	{
		this.streetAddress = streetAddress;
	}
	
	/**
	 * Sets the member's telephone number.
	 * 
	 * @param telephone
	 *            (String)
	 */
	public void setTelephone(String telephone)
	{
		this.telephone = telephone;
	}
	
	/**
	 * Sets the user object associated with this Member.
	 * 
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user)
	{
		this.user = user;
	}
	
	/**
	 * Sets the member's ZIP code.
	 * 
	 * @param zip
	 *            (String)
	 */
	public void setZip(String zip)
	{
		this.zip = zip;
	}
	
}
