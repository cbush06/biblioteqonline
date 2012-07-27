//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: RequestedItem.java
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
 *
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
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

/**
 * @author Clint Bush
 * 
 */
@Entity(name = "RequestedItem")
@Table(name = "item_request")
public class RequestedItem implements Serializable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(RequestedItem.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = -3901845323903264635L;
	
	private long itemId;
	private Member requestor;
	private String requestDate;
	private long id;
	private String type;
	
	/**
	 * Returns this request's ID.
	 * 
	 * @return (long) This request's ID.
	 */
	@Id
	@SequenceGenerator(initialValue = 1, name = "item_request_myoid_seq", sequenceName = "item_request_myoid_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_request_myoid_seq")
	@Column(name = "myoid", unique = true)
	public long getId()
	{
		return this.id;
	}
	
	/**
	 * Returns the ID of the item requested. This is an auto-generated serial ID.
	 * 
	 * @return (long) The ID of the item requested.
	 */
	@Column(name = "item_oid")
	public long getItemId()
	{
		return this.itemId;
	}
	
	/**
	 * Returns the date the request was made.
	 * 
	 * @return (String) The date the request was made.
	 */
	@Column(name = "requestdate")
	public String getRequestDate()
	{
		return this.requestDate;
	}
	
	/**
	 * Returns the date the request was made as a Date object.
	 * 
	 * @return (Date) The date the request was made.
	 */
	@Transient
	public Date getRequestDateAsDate()
	{
		try
		{
			return (new SimpleDateFormat("MM/dd/yyyy")).parse(this.getRequestDate());
		}
		catch (ParseException e)
		{
			RequestedItem.log.error(e.getMessage() + "\n" + e.getStackTrace());
			return null;
		}
	}
	
	/**
	 * Returns the Member who requested the item.
	 * 
	 * @return (Member) The member requesting the item.
	 */
	@ManyToOne
	@JoinColumn(name = "memberid", unique = false, nullable = false, referencedColumnName = "memberid")
	public Member getRequestor()
	{
		return this.requestor;
	}
	
	/**
	 * Returns the type of item being requested (e.g. Book, DVD, etc.).
	 * 
	 * @return (String) The type of item being requested.
	 */
	@Column(name = "type")
	public String getType()
	{
		return this.type;
	}
	
	/**
	 * Sets this request's ID. This method is private and should never be used.
	 * 
	 * @param id
	 *            (long) The ID of this request.
	 */
	private void setId(long id)
	{
		this.id = id;
	}
	
	/**
	 * Sets the ID of the Item being requested.
	 * 
	 * @param itemId
	 *            (long) The ID of the item being requested.
	 */
	public void setItemId(long itemId)
	{
		this.itemId = itemId;
	}
	
	/**
	 * Sets the date this request was made on. This must be in MM/DD/YYYY format.
	 * 
	 * @param requestDate
	 *            (String) The date this request was made on.
	 */
	public void setRequestDate(String requestDate)
	{
		this.requestDate = requestDate;
	}
	
	/**
	 * Sets the date the request was made on.
	 * 
	 * @param requestDate
	 *            (Date) The date this request was made on.
	 */
	public void setRequestDateAsDate(Date requestDate)
	{
		// Prepare the format patterns
		String monthDayFormat = "%02d";
		String yearFormat = "%d";
		
		// Use the Calendar class to get the month, day, and year values from the Date object
		Calendar requestDateCal = new GregorianCalendar();
		requestDateCal.setTime(requestDate);
		
		// Use the String Formatter to prepare the String value of the date
		this.requestDate = String.format(monthDayFormat, requestDateCal.get(Calendar.MONTH) + 1) + "/"
		        + String.format(monthDayFormat, requestDateCal.get(Calendar.DAY_OF_MONTH)) + "/"
		        + String.format(yearFormat, requestDateCal.get(Calendar.YEAR));
	}
	
	/**
	 * Sets the Member this request was made by.
	 * 
	 * @param requestor
	 *            (Member) The member making the request.
	 */
	public void setRequestor(Member requestor)
	{
		this.requestor = requestor;
	}
	
	/**
	 * Sets the type of the item being requested (e.g. Book, DVD, etc.).
	 * 
	 * @param type
	 *            (String) The type of the item being requested.
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	
}
