//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.model
 * File: Loan.java
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
 * This provides a data structure to house information regarding a loaned item. By using the org.biblioteq.interfaces.Item interface to represent the actual loaned item, this
 * model can be used for any type of item. This is convenient when working with lists of loaned items where the item type varies. For instance, this is used with the list of
 * borrowed items a user sees when he/she first logs in. This model could also be used with search results, an administrator's interface listing overdue items, etc.
 * 
 * This model is quite simple in that it only links an "Item" with the "BorrowedItem" entity that represents the actual loan made to a library Member; however, without this model,
 * some of the views in this application would be difficult, if not impossible.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * May 6, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.ejb.model;

import java.io.Serializable;
import java.util.Calendar;

import org.biblioteq.ejb.entities.BorrowedItem;
import org.biblioteq.ejb.interfaces.Item;

/**
 * @author Clint Bush
 * 
 */
public class Loan implements Serializable
{
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = -2867530111130735413L;
	
	private Item loanedItem;
	private BorrowedItem loan;
	
	/**
	 * Constructor for creating a new loan object.
	 * 
	 * @param loanedItem
	 *            (Item) The actual Entity object for the borrowed item (e.g. Book, DVD, Journal, etc.).
	 * @param loan
	 *            (BorrowedItem) The BorrowedItem Entity that represents the loaning of the item.
	 */
	public Loan(Item loanedItem, BorrowedItem loan)
	{
		this.loanedItem = loanedItem;
		this.loan = loan;
	}
	
	/**
	 * Returns the number of days remaining for the loaned item to be returned.
	 * 
	 * @return (int) Days remaining for item to be returned.
	 */
	public int getDaysRemaining()
	{
		// Get a Calendar object for now and the date due
		Calendar now = Calendar.getInstance();
		Calendar dueDate = Calendar.getInstance();
		dueDate.setTime(this.getLoan().getDueDateAsDate());
		
		// Calculate the time difference in milliseconds
		long msDifference = dueDate.getTimeInMillis() - now.getTimeInMillis();
		
		// Divide by 86,400,000 = (1000ms / 1s) * (60s / 1min) * (60min / 1hr) * (24hr / 1day)
		return (int) (msDifference / 86400000L);
	}
	
	/**
	 * Returns the BorrowedItem entity that represents the actual loan made to a library member.
	 * 
	 * @return (BorrowedItem) The loan made to a library member.
	 */
	public BorrowedItem getLoan()
	{
		return this.loan;
	}
	
	/**
	 * Returns the loaned item.
	 * 
	 * @return (Item) Loaned item.
	 */
	public Item getLoanedItem()
	{
		return this.loanedItem;
	}
}
