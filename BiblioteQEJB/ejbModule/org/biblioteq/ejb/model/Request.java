//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.model
 * File: Request.java
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
 * Jun 30, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.ejb.model;

import java.io.Serializable;
import java.util.Calendar;

import org.biblioteq.ejb.entities.RequestedItem;
import org.biblioteq.ejb.interfaces.Item;

/**
 * @author Clint Bush
 * 
 */
public class Request implements Serializable
{
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 9171743585698155649L;
	
	private Item requestedItem;
	private RequestedItem request;
	
	/**
	 * Constructor for created a new Request object.
	 * 
	 * @param requestedItem
	 *            (Item) The actual entity object for the borrowed item (e.g. Book, DVD, Journal, etc.).
	 * @param request
	 *            (RequestedItem) The RequestedItem entity that represents the requesting of the item.
	 */
	public Request(Item requestedItem, RequestedItem request)
	{
		this.requestedItem = requestedItem;
		this.request = request;
	}
	
	/**
	 * Returns the age of the request in days.
	 * 
	 * @return (int) Number of days since the request was made.
	 */
	public int getAgeInDays()
	{
		// Get a Calendar object for now and the date requested
		Calendar now = Calendar.getInstance();
		Calendar requestDate = Calendar.getInstance();
		requestDate.setTime(this.getRequest().getRequestDateAsDate());
		
		// Calculate the difference in milliseconds
		long msDifference = now.getTimeInMillis() - requestDate.getTimeInMillis();
		
		// Divide by 86,400,000 = (1000ms / 1s) * (60s / 1min) * (60min / 1hr) * (24hr / 1day)
		return (int) (msDifference / 86400000L);
	}
	
	/**
	 * Returns the RequestedItem entity that represents the actual request made by a library member.
	 * 
	 * @return (RequestedItem) The request.
	 */
	public RequestedItem getRequest()
	{
		return this.request;
	}
	
	/**
	 * Returns the requested Item.
	 * 
	 * @return (Item) The requestedItem.
	 */
	public Item getRequestedItem()
	{
		return this.requestedItem;
	}
	
}
