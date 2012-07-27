//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: BorrowedItem.java
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
 * This file represents the item_borrower class. It links that class to the User object for the actual borrower, to the ItemCopy object for the item, and to the actual object
 * for the item checked out.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * May 4, 2012, Clinton Bush, 1.0.0,
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
 * This file represents the item_borrower class. It links that class to the User object for the actual borrower, to the ItemCopy object for the item, and to the actual object
 * for the item checked out.
 * 
 * @author Clint Bush
 * 
 */
@Entity(name = "BorrowedItem")
@Table(name = "item_borrower")
public class BorrowedItem implements Serializable
{
	// TODO: Convert the copy ID to a copy object.
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(BorrowedItem.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = -6393078702408984991L;
	
	private long itemId = 0;
	private Member borrower;
	private String reservedDate = "";
	private String dueDate = "";
	private long id = 0;
	private String copyId = "";
	private int copyNumber = 0;
	private Admin reservedBy;
	private String type = "";
	
	/**
	 * Returns the Member object representing the borrower.
	 * 
	 * @return (Member) The person who is borrowing this item.
	 */
	@ManyToOne
	@JoinColumn(name = "memberid", unique = false, nullable = false, referencedColumnName = "memberid")
	public Member getBorrower()
	{
		return this.borrower;
	}
	
	/**
	 * Returns the ID of the copy borrowed by the user.
	 * 
	 * @return (String) ID of the copy borrowed.
	 */
	@Column(name = "copyid", length = 64)
	public String getCopyId()
	{
		return this.copyId;
	}
	
	/**
	 * Returns the number of the copy borrowed.
	 * 
	 * @return (int) Number of the copy borrowed.
	 */
	@Column(name = "copy_number")
	public int getCopyNumber()
	{
		return this.copyNumber;
	}
	
	/**
	 * Returns the date the item is due back in the MM/DD/YYYY format.
	 * 
	 * @return (String) The due date.
	 */
	@Column(name = "duedate", length = 32)
	public String getDueDate()
	{
		return this.dueDate;
	}
	
	/**
	 * Returns the due date as a Date object rather than a String.
	 * 
	 * @return (Date) The Date object of the due date or null if parsing of the String date failed.
	 */
	@Transient
	public Date getDueDateAsDate()
	{
		try
		{
			return (new SimpleDateFormat("MM/dd/yyyy")).parse(this.getDueDate());
		}
		catch (ParseException e)
		{
			BorrowedItem.log.error(e.getMessage() + "\n" + e.getStackTrace());
			return null;
		}
	}
	
	/**
	 * Returns the ID of this borrow record.
	 * 
	 * @return (long) ID of this borrow record.
	 */
	@Id
	@Column(name = "myoid")
	public long getId()
	{
		return this.id;
	}
	
	/**
	 * Returns the ID of the item.
	 * 
	 * @return (long) ID of the item.
	 */
	@Column(name = "item_oid")
	public long getItemId()
	{
		return this.itemId;
	}
	
	/**
	 * Returns the Admin object representing the library worker who reserved this item for the borrower.
	 * 
	 * @return (Admin) The Admin who reserved this item.
	 */
	@ManyToOne
	@JoinColumn(name = "reserved_by", unique = false, nullable = false, referencedColumnName = "username")
	public Admin getReservedBy()
	{
		return this.reservedBy;
	}
	
	/**
	 * Returns the date this item was reserved in the MM/DD/YYYY format.
	 * 
	 * @return (String) The date this item was reserved.
	 */
	@Column(name = "reserved_date", length = 32)
	public String getReservedDate()
	{
		return this.reservedDate;
	}
	
	/**
	 * Returns the reserved date as a Date object rather than a String.
	 * 
	 * @return (Date) The Date object of the reserved date or null if parsing of the String failed.
	 */
	@Transient
	public Date getReservedDateAsDate()
	{
		try
		{
			return (new SimpleDateFormat("MM/dd/yyyy")).parse(this.getReservedDate());
		}
		catch (ParseException e)
		{
			BorrowedItem.log.error(e.getMessage() + "\n" + e.getStackTrace());
			return null;
		}
	}
	
	/**
	 * Returns the item type (e.g. Book).
	 * 
	 * @return (String) The item type.
	 */
	@Column(name = "type", length = 16)
	public String getType()
	{
		// Must remove spaces as a fix to allow Java reflection to convert the item type into a valid class name.
		// The VideoGame class is an example of where this is necessary because BiblioteQ stores "Video Game" as the type
		// instead of the requisite "VideoGame". We accomodate this by removing the spaces.
		return this.type.replace(" ", "");
	}
	
	/**
	 * Sets the Member who is borrowing this item.
	 * 
	 * @param borrower
	 *            (Member) The Member borrowing this item.
	 */
	public void setBorrower(Member borrower)
	{
		this.borrower = borrower;
	}
	
	/**
	 * Sets the copy ID of the borrowed item.
	 * 
	 * @param copyId
	 *            (String) The copyId to set.
	 */
	public void setCopyId(String copyId)
	{
		this.copyId = copyId;
	}
	
	/**
	 * Sets the copy number of the borrowed item.
	 * 
	 * @param copyNumber
	 *            (int) The copyNumber to set.
	 */
	public void setCopyNumber(int copyNumber)
	{
		this.copyNumber = copyNumber;
	}
	
	/**
	 * Sets the date the borrowed item is due to be returned. Must be in the MM/DD/YYYY format.
	 * 
	 * @param dueDate
	 *            (String) The dueDate to set.
	 */
	public void setDueDate(String dueDate)
	{
		this.dueDate = dueDate;
	}
	
	/**
	 * Sets the item's due date.
	 * 
	 * @param dueDate
	 *            (Date) The Date object of the due date.
	 */
	public void setDueDateAsDate(Date dueDate)
	{
		// Prepare the format patterns
		String monthDayFormat = "%02d";
		String yearFormat = "%d";
		
		// Use the Calendar class to get the month, day, and year values from the Date object
		Calendar dueDateCal = Calendar.getInstance();
		dueDateCal.setTime(dueDate);
		
		// Use the String Formatter to prepare the String value of the date
		this.dueDate = String.format(monthDayFormat, dueDateCal.get(Calendar.MONTH)) + "/"
		        + String.format(monthDayFormat, dueDateCal.get(Calendar.DAY_OF_MONTH)) + "/"
		        + String.format(yearFormat, dueDateCal.get(Calendar.YEAR));
	}
	
	/**
	 * Sets this record's ID.
	 * 
	 * @param id
	 *            (long) The id to set.
	 */
	private void setId(long id)
	{
		this.id = id;
	}
	
	/**
	 * Sets the ID of the borrowed item.
	 * 
	 * @param itemId
	 *            (long) The itemId to set.
	 */
	public void setItemId(long itemId)
	{
		this.itemId = itemId;
	}
	
	/**
	 * Sets the Admin who reserved the item for the borrower.
	 * 
	 * @param reservedBy
	 *            (Admin) The reservedBy to set.
	 */
	public void setReservedBy(Admin reservedBy)
	{
		this.reservedBy = reservedBy;
	}
	
	/**
	 * Sets the date the item was reserved on. Must be in MM/DD/YYYY format.
	 * 
	 * @param reservedDate
	 *            (String) The reservedDate to set.
	 */
	public void setReservedDate(String reservedDate)
	{
		this.reservedDate = reservedDate;
	}
	
	/**
	 * Sets the date the item was reserved on.
	 * 
	 * @param reservedDate
	 *            (Date) The Date object of the reservation date.
	 */
	public void setReservedDateAsDate(Date reservedDate)
	{
		// Prepare the format patterns
		String monthDayFormat = "%02d";
		String yearFormat = "%d";
		
		// Use the Calendar class to get the month, day, and year values from the Date object
		Calendar reservedDateCal = Calendar.getInstance();
		reservedDateCal.setTime(reservedDate);
		
		// Use the String Formatter to prepare the String value of the date
		this.reservedDate = String.format(monthDayFormat, reservedDateCal.get(Calendar.MONTH)) + "/"
		        + String.format(monthDayFormat, reservedDateCal.get(Calendar.DAY_OF_MONTH)) + "/"
		        + String.format(yearFormat, reservedDateCal.get(Calendar.YEAR));
	}
	
	/**
	 * Sets the type of item being reserved (e.g. Book).
	 * 
	 * @param type
	 *            (String) The type to set.
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	
}