//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: MemberBorrowRecord.java
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
 * This represents a history record of an item reservation made by a member.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * May 5, 2012, Clinton Bush, 1.0.0,
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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

/**
 * This represents a history record of an item reservation made by a member.
 * 
 * @author Clint Bush
 * 
 */
@Entity(name = "MemberBorrowRecord")
@Table(name = "member_history")
public class MemberBorrowRecord implements Serializable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(MemberBorrowRecord.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = -6202860886175420866L;
	
	private Member member;
	private long itemSerialId;
	private String copyId;
	private String dateReserved;
	private String dateDue;
	private String dateReturned;
	private long id;
	private Admin reservedBy;
	private String itemType;
	private String itemId;
	
	/**
	 * Returns the ID for the copy of the item being borrowed (e.g. for books this is the ISBN-10 number plus the copy number: "059035342X-1").
	 * 
	 * @return (String) The copy ID.
	 */
	@Column(name = "copyid")
	public String getCopyId()
	{
		return this.copyId;
	}
	
	/**
	 * Returns the date the item was due back.
	 * 
	 * @return (String) The date due.
	 */
	@Column(name = "duedate")
	public String getDateDue()
	{
		return this.dateDue;
	}
	
	/**
	 * Returns the date the item was due back as a Date object.
	 * 
	 * @return (Date) The date due.
	 */
	@Transient
	public Date getDateDueAsDate()
	{
		try
		{
			return (new SimpleDateFormat("MM/dd/yyyy")).parse(this.getDateDue());
		}
		catch (ParseException e)
		{
			MemberBorrowRecord.log.error(e.getMessage() + "\n" + e.getStackTrace());
			return null;
		}
	}
	
	/**
	 * Returns the date the Member reserved the item.
	 * 
	 * @return (String) The date reserved.
	 */
	@Column(name = "reserved_date")
	public String getDateReserved()
	{
		return this.dateReserved;
	}
	
	/**
	 * Returns the date the Member reserved the item as a Date object.
	 * 
	 * @return (Date) The date reserved.
	 */
	@Transient
	public Date getDateReservedAsDate()
	{
		try
		{
			return (new SimpleDateFormat("MM/dd/yyyy")).parse(this.getDateReserved());
		}
		catch (ParseException e)
		{
			MemberBorrowRecord.log.error(e.getMessage() + "\n" + e.getStackTrace());
			return null;
		}
	}
	
	/**
	 * Returns the date the item was returned.
	 * 
	 * @return (String) The date returned.
	 */
	@Column(name = "returned_date")
	public String getDateReturned()
	{
		return this.dateReturned;
	}
	
	/**
	 * Returns the date the item was returned as a Date object.
	 * 
	 * @return (Date) The date returned.
	 */
	@Transient
	public Date getDateReturnedAsDate()
	{
		try
		{
			return (new SimpleDateFormat("MM/dd/yyyy")).parse(this.getDateReturned());
		}
		catch (ParseException e)
		{
			MemberBorrowRecord.log.error(e.getMessage() + "\n" + e.getStackTrace());
			return null;
		}
	}
	
	/**
	 * Returns this record's ID. This is an auto-incremented serial number for the borrow records.
	 * 
	 * @return (long) The ID.
	 */
	@Id
	@Column(name = "myoid")
	public long getId()
	{
		return this.id;
	}
	
	/**
	 * Returns the item's ID. The form of this ID varies among the different item types (e.g. for books it is the ISBN-10 number).
	 * 
	 * @return (String) The item ID.
	 */
	@Column(name = "item_id")
	public String getItemId()
	{
		return this.itemId;
	}
	
	/**
	 * Returns the Serial ID for this item. This is an auto-incremented serial ID number that differentiates this item among other items of the same type.
	 * 
	 * @return (long) The item's serial ID.
	 */
	@Column(name = "item_oid")
	public long getItemSerialId()
	{
		return this.itemSerialId;
	}
	
	/**
	 * Returns the item type of the borrowed item (e.g. Book, DVD, etc.).
	 * 
	 * @return (String) The item type.
	 */
	@Column(name = "type")
	public String getItemType()
	{
		return this.itemType;
	}
	
	/**
	 * Returns the Member who is borrowing this item.
	 * 
	 * @return (Member) The member.
	 */
	@ManyToOne
	@JoinColumn(name = "memberid", unique = false, nullable = false, referencedColumnName = "memberid")
	public Member getMember()
	{
		return this.member;
	}
	
	/**
	 * Returns the Admin who reserved the item for the Member borrower.
	 * 
	 * @return (Admin) The Admin that reserved this item for the Member borrower.
	 */
	@ManyToOne
	@JoinColumn(name = "reserved_by", unique = false, nullable = false, referencedColumnName = "username")
	public Admin getReservedBy()
	{
		return this.reservedBy;
	}
	
	/**
	 * Sets the copy ID of the reserved item.
	 * 
	 * @param copyId
	 *            the copyId to set
	 */
	public void setCopyId(String copyId)
	{
		this.copyId = copyId;
	}
	
	/**
	 * Sets the due date of the item when it was borrowed. Must be in MM/DD/YYYY format.
	 * 
	 * @param dateDue
	 *            (String) The due date to set.
	 */
	public void setDateDue(String dateDue)
	{
		this.dateDue = dateDue;
	}
	
	/**
	 * Sets the due date of the item when it was borrowed.
	 * 
	 * @param dateDue
	 *            (Date) The due date to set.
	 */
	public void setDateDueAsDate(Date dateDue)
	{
		// Prepare the format patterns
		String monthDayFormat = "%02d";
		String yearFormat = "%d";
		
		// Use the Calendar class to get the month, day, and year values from the Date object
		Calendar dateDueCal = Calendar.getInstance();
		dateDueCal.setTime(dateDue);
		
		// Use the String Formatter to prepare the String value of the date
		this.dateDue = String.format(monthDayFormat, dateDueCal.get(Calendar.MONTH)) + "/"
		        + String.format(monthDayFormat, dateDueCal.get(Calendar.DAY_OF_MONTH)) + "/"
		        + String.format(yearFormat, dateDueCal.get(Calendar.YEAR));
	}
	
	/**
	 * Sets the date the item was reserved. Must be in MM/DD/YYYY format.
	 * 
	 * @param dateReserved
	 *            (String) The date the item reserved.
	 */
	public void setDateReserved(String dateReserved)
	{
		this.dateReserved = dateReserved;
	}
	
	/**
	 * Sets the date the item was reserved.
	 * 
	 * @param dateReserved
	 *            (Date) The date the item was reserved.
	 */
	public void setDateReservedAsDate(Date dateReserved)
	{
		// Prepare the format patterns
		String monthDayFormat = "%02d";
		String yearFormat = "%d";
		
		// Use the Calendar class to get the month, day, and year values from the Date object
		Calendar dateReservedCal = Calendar.getInstance();
		dateReservedCal.setTime(dateReserved);
		
		// Use the String Formatter to prepare the String value of the date
		this.dateReserved = String.format(monthDayFormat, dateReservedCal.get(Calendar.MONTH)) + "/"
		        + String.format(monthDayFormat, dateReservedCal.get(Calendar.DAY_OF_MONTH)) + "/"
		        + String.format(yearFormat, dateReservedCal.get(Calendar.YEAR));
	}
	
	/**
	 * Sets the date the item was returned. Must be in MM/DD/YYYY format.
	 * 
	 * @param dateReturned
	 *            (String) The date the item was returned.
	 */
	public void setDateReturned(String dateReturned)
	{
		this.dateReturned = dateReturned;
	}
	
	/**
	 * Sets the date the item was returned.
	 * 
	 * @param dateReturned
	 *            (Date) The date the item was returned.
	 */
	public void setDateReturnedAsDate(Date dateReturned)
	{
		// Prepare the format patterns
		String monthDayFormat = "%02d";
		String yearFormat = "%d";
		
		// Use the Calendar class to get the month, day, and year values from the Date object
		Calendar dateReturnedCal = Calendar.getInstance();
		dateReturnedCal.setTime(dateReturned);
		
		// Use the String Formatter to prepare the String value of the date
		this.dateReturned = String.format(monthDayFormat, dateReturnedCal.get(Calendar.MONTH)) + "/"
		        + String.format(monthDayFormat, dateReturnedCal.get(Calendar.DAY_OF_MONTH)) + "/"
		        + String.format(yearFormat, dateReturnedCal.get(Calendar.YEAR));
	}
	
	/**
	 * Sets the serial ID of this borrow record.
	 * 
	 * @param id
	 *            (long) The serial ID.
	 */
	public void setId(long id)
	{
		this.id = id;
	}
	
	/**
	 * Sets the Item ID of the item borrowed. This is an ID used for items whose form varies between the different item types (e.g. for books it is the ISBN-10 number).
	 * 
	 * @param itemId
	 *            (String) The item iD.
	 */
	public void setItemId(String itemId)
	{
		this.itemId = itemId;
	}
	
	/**
	 * Sets the serial ID of the item borrowed. This is an auto-generated serial ID that represents this item among the other items of the same type.
	 * 
	 * @param itemSerialId
	 *            (long) The item serial ID.
	 */
	public void setItemSerialId(long itemSerialId)
	{
		this.itemSerialId = itemSerialId;
	}
	
	/**
	 * Sets the type of the item that was borrowed (e.g. Book, DVD, etc.).
	 * 
	 * @param itemType
	 *            (String) The item type.
	 */
	public void setItemType(String itemType)
	{
		this.itemType = itemType;
	}
	
	/**
	 * Sets the Member who borrowed the item.
	 * 
	 * @param member
	 *            (Member) The member who borrowed the item.
	 */
	public void setMember(Member member)
	{
		this.member = member;
	}
	
	/**
	 * Sets the Admin who reserved the item for the borrower.
	 * 
	 * @param reservedBy
	 *            (Admin) The Admin who reserved this item.
	 */
	public void setReservedBy(Admin reservedBy)
	{
		this.reservedBy = reservedBy;
	}
	
}
