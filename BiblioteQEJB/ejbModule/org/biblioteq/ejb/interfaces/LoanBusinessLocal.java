//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.interfaces
 * File: LoanBusinessLocal.java
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
 * This is the Local interface for the LoanBusiness EJB.
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
package org.biblioteq.ejb.interfaces;

import java.util.List;

import javax.ejb.Local;

import org.biblioteq.ejb.entities.Member;
import org.biblioteq.ejb.model.Loan;

/**
 * Local interface for the LoanBusiness EJB.
 * 
 * @author Clint Bush
 * 
 */
@Local
public interface LoanBusinessLocal
{
	/**
	 * Returns the number of consecutive reservations a member has for a particular item.
	 * 
	 * @param item
	 *            (Loan) The item whose reservation history is being reviewed.
	 */
	public abstract int getConsecutiveLoanCount(Loan loan);
	
	/**
	 * Retrieves all loans made to the specified library member and returns them as a list of Loan objects.
	 * 
	 * @param member
	 *            (Member) The member whose loans will be returned.
	 * @return (List<Loan>) List of Loan objects that provide a common model for housing the Item (book, dvd, etc.) and BorrowedItem that represents the actual loan made to a
	 *         member.
	 */
	public abstract List<Loan> getMemberLoans(Member member);
	
	/**
	 * Returns the total number of Items Borrowed.
	 * 
	 * @return (long) Total count of borrowed items.
	 */
	public abstract long getTotalItemsBorrowed();
	
	/**
	 * Returns the total number of Items Borrowed that are overdue.
	 * 
	 * @return (long) Total count of overdue items.
	 */
	public abstract long getTotalItemsOverdue();
}
