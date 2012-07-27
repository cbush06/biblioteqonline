//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb
 * File: LoanBusiness.java
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
 * The LoanBusiness EJB handles all database transactions directly related to the library loan process.
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
package org.biblioteq.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.entities.BorrowedItem;
import org.biblioteq.ejb.entities.Member;
import org.biblioteq.ejb.entities.MemberBorrowRecord;
import org.biblioteq.ejb.interfaces.Item;
import org.biblioteq.ejb.interfaces.LoanBusinessLocal;
import org.biblioteq.ejb.interfaces.LoanBusinessRemote;
import org.biblioteq.ejb.model.Loan;

/**
 * The LoanBusiness EJB handles all database transactions directly related to the library loan process.
 * 
 * @author Clint Bush
 * 
 */
@Stateless(name = "LoanBusiness")
public class LoanBusiness implements LoanBusinessLocal, LoanBusinessRemote, Serializable
{
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 2642006208140037640L;
	
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(LoanBusiness.class);
	
	// Get the entity manager
	@PersistenceContext(unitName = "BiblioteQPersistenceUnit")
	private EntityManager em;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.LoanBusinessLocal#getConsecutiveLoanCount(org.biblioteq.ejb.model.Loan)
	 */
	@Override
	public int getConsecutiveLoanCount(Loan loan)
	{
		MemberBorrowRecord currentRecord = null;
		int returnVal = 0;
		
		/*
		 * Step 1 is to get the MemberBorrowRecord matching this Loan.
		 */
		//@formatter:off
		TypedQuery<MemberBorrowRecord> q = this.em.createQuery(
				"SELECT m " +
				"FROM MemberBorrowRecord m " +
				"WHERE " +
					"m.copyId = :copyId AND " +
					"m.dateReserved = :dateReserved AND " +
					"m.dateReturned = :dateReturned AND " +
					"m.dateDue = :dateDue AND " +
					"m.member = :member",
				MemberBorrowRecord.class
		);
		//@formatter:on
		
		// Add the parameters
		q.setParameter("copyId", loan.getLoan().getCopyId());
		q.setParameter("dateReserved", loan.getLoan().getReservedDate());
		q.setParameter("dateReturned", "N/A");
		q.setParameter("dateDue", loan.getLoan().getDueDate());
		q.setParameter("member", loan.getLoan().getBorrower());
		
		// Get the record if it exists
		try
		{
			currentRecord = q.getSingleResult();
		}
		catch (NoResultException e)
		{
			// No history exists for this user and item
			LoanBusiness.log.error("No result found for the specified loan when trying to find consecutive loan count.");
			return 0;
		}
		catch (NonUniqueResultException e)
		{
			// More than one record match the query
			LoanBusiness.log.error("Multiple results found for the specified loan when trying to find consecutive loan count.");
			return -1;
		}
		
		/*
		 * Step 2 is to loop through any previous reservations for this user and item. These must all be consecutive reservations indicating the user
		 * renewed his/her lease on them.
		 */
		if (currentRecord != null)
		{
			// Add one for the current reservation
			returnVal++;
			
			// Re-write the query to search for a record of the user returning the book on the same day it was checked out for the current reservation.
			// This would indicate it was both returned and checked out on the same day, or "renewed."
			//@formatter:off
			q = this.em.createQuery(
					"SELECT m " +
					"FROM MemberBorrowRecord m " +
					"WHERE " +
						"m.copyId = :copyId AND " +
						"m.dateReturned = :dateCurrentReserved AND " +
						"m.member = :member",
					MemberBorrowRecord.class
			);
			//@formatter:on
			
			// Loop, running this query for each consecutive renewal into the past. Count each time a match is found.
			do
			{
				q.setParameter("copyId", currentRecord.getCopyId());
				q.setParameter("dateCurrentReserved", currentRecord.getDateReserved());
				q.setParameter("member", currentRecord.getMember());
				
				// Get the record if it exists
				try
				{
					currentRecord = q.getSingleResult();
				}
				catch (NoResultException e)
				{
					// No more records are found
					currentRecord = null;
				}
				catch (NonUniqueResultException e)
				{
					// More than one record match the query
					LoanBusiness.log.error("Multiple results found when trying to find consecutive loans prior to the specified loan.");
					return -1;
				}
				
				returnVal++;
			} while (currentRecord != null);
		}
		else
		{
			return 0;
		}
		
		return returnVal;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.LoanBusinessLocal#getMemberLoans(org.biblioteq.ejb.entities.Member)
	 */
	@Override
	public List<Loan> getMemberLoans(Member member)
	{
		// Prepare the return value
		List<Loan> returnVal = new ArrayList<Loan>();
		
		/*
		 * Step 1 is to get the BorrowedItem records that represent all loans made to a member.
		 */
		//@formatter:off
		TypedQuery<BorrowedItem> q = this.em.createQuery(
				"SELECT b " +
				"FROM BorrowedItem b " +
				"WHERE " +
					"b.borrower = :member",
				BorrowedItem.class
		);
		//@formatter:on
		
		// Add the parameters
		q.setParameter("member", member);
		
		// Get the results of our query
		List<BorrowedItem> resultList = q.getResultList();
		
		// If no results were returned (the user has no borrowed items), return an empty list
		if (resultList.size() < 1)
		{
			return new ArrayList<Loan>();
		}
		
		/*
		 * Step 2 is to try to resolve the type of borrowed item to an entity type; then, we will get the actual entity object for that item.
		 */
		for (BorrowedItem nextLoan : resultList)
		{
			Class<?> nextItemClass = null;
			
			// Try to resolve the type of the item to an entity class type
			try
			{
				nextItemClass = Class.forName("org.biblioteq.ejb.entities." + nextLoan.getType());
			}
			catch (ClassNotFoundException e)
			{
				LoanBusiness.log.error("Error finding entity class type for: " + nextLoan.getType() + "\n\nException Message: "
				        + e.getMessage());
				e.printStackTrace();
				continue;
			}
			
			// If the item's Entity class type could not be found, continue on
			if (nextItemClass == null)
			{
				LoanBusiness.log.error("Entity Class could not be found for: " + nextLoan.getType());
				continue;
			}
			
			// Let's try to retrieve the actual entity for this item
			//@formatter:off
			Query q2 = this.em.createQuery(
					"SELECT o " +
					"FROM " + nextLoan.getType() + " o " +
					"WHERE " +
						"o.itemId = :itemId",
					nextItemClass
			);
			//@formatter:on
			
			// Add the parameters
			q2.setParameter("itemId", nextLoan.getItemId());
			
			// Execute the query
			Item nextItem = null;
			try
			{
				nextItem = (Item) q2.getSingleResult();
			}
			catch (NoResultException e)
			{
				LoanBusiness.log.error("No item returned while trying to resolve the item of a BorrowedItem loan to its entity object.");
				e.printStackTrace();
			}
			catch (NonUniqueResultException e)
			{
				LoanBusiness.log
				        .error("More than one item returned while trying to resolve the item of a BorrowedItem loan to its entity object.");
				e.printStackTrace();
			}
			
			// If the borrowed item's Entity object could not be found, continue on
			if (nextItem == null)
			{
				LoanBusiness.log.error("Entity object could not be found for [" + nextLoan.getItemId() + "] of type [" + nextLoan.getType()
				        + "].");
				continue;
			}
			
			// Now that we have the actual item's Entity object, let's add a new Loan object to the return list
			returnVal.add(new Loan(nextItem, nextLoan));
		}
		
		/*
		 * Step 3 is to return the list of Loan objects. It now contains one Loan object for each loan to the Member. Each of these objects contains the Entity object of the
		 * borrowed item and the BorrowedItem object that represents the loan to the Member.
		 */
		return returnVal;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.LoanBusinessLocal#getTotalItemsBorrowed()
	 */
	@Override
	public long getTotalItemsBorrowed()
	{
		// Declare return value
		Long returnVal = 0L;
		
		// Create the query
		// @formatter:off
		TypedQuery<Long> q = this.em.createQuery(
				"SELECT COUNT(*) FROM BorrowedItem b",
				Long.class
		);
		// @formatter:on
		
		// Get the result
		try
		{
			returnVal = q.getSingleResult();
		}
		catch (NoResultException e)
		{
			LoanBusiness.log.error("No result returned while trying to count total Items Borrowed.");
			e.printStackTrace();
		}
		
		// Return the result
		return returnVal.longValue();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.LoanBusinessLocal#getTotalItemsOverdue()
	 */
	@Override
	public long getTotalItemsOverdue()
	{
		// Declare variables we'll be using
		long returnVal = 0L;
		List<BorrowedItem> borrowedItems = new ArrayList<BorrowedItem>();
		
		// Create the query
		//@formatter:off
    	TypedQuery<BorrowedItem> q = this.em.createQuery(
    			"SELECT b FROM BorrowedItem b",
    			BorrowedItem.class
		);
    	//@formatter:on
		
		// Get the results
		borrowedItems = q.getResultList();
		
		// Count the overdue items
		for (BorrowedItem nextItem : borrowedItems)
		{
			// Get the difference in milliseconds
			long msDifference = nextItem.getDueDateAsDate().getTime() - (new Date()).getTime();
			
			// If it's negative it's overdue
			if (msDifference < 0)
			{
				returnVal++;
			}
		}
		
		// Return the value
		return returnVal;
	}
	
}
