//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb
 * File: ItemBusiness.java
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
 * Jun 9, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.ejb;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.interfaces.Item;
import org.biblioteq.ejb.interfaces.ItemBusinessLocal;
import org.biblioteq.ejb.interfaces.ItemBusinessRemote;

/**
 * @author Clint Bush
 * 
 */
@Stateless(name = "ItemBusiness")
public class ItemBusiness implements ItemBusinessLocal, ItemBusinessRemote, Serializable
{
	/**
	 * GUID for impelemting Serializable.
	 */
	private static final long serialVersionUID = 7179665550554949868L;
	
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(ItemBusiness.class);
	
	// Get the entity manager
	@PersistenceContext(unitName = "BiblioteQPersistenceUnit")
	private EntityManager em;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.ItemBusinessLocal#getItemById(java.lang.String, java.lang.String)
	 */
	@Override
	public Item getItemById(String id, String type)
	{
		// Prepare the return value
		Item returnVal = null;
		
		// Create the query
		TypedQuery<Item> q = this.em.createQuery("SELECT e FROM " + type + " e WHERE e.id = :id", Item.class);
		
		// Set the parameters
		q.setParameter("id", id);
		
		// Get the result
		try
		{
			returnVal = q.getSingleResult();
		}
		catch (NoResultException e)
		{
			ItemBusiness.log.error("No results found for entity of type [" + type + "] and ID [" + id + "].");
		}
		catch (NonUniqueResultException e)
		{
			ItemBusiness.log.error("Multiple results found for entity of type [" + type + "] and ID [" + id + "].");
		}
		
		return returnVal;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.ItemBusinessLocal#getNumberCheckedOutOrRequested(org.biblioteq.ejb.interfaces.Item)
	 */
	@Override
	public Long getNumberCheckedOutOrRequested(Item item)
	{
		// Declare the variables we'll be using
		Long borrowedCount = 0L;
		Long requestedCount = 0L;
		TypedQuery<Long> q = null;
		
		/*
		 * Step 1 - Get the number of this item's copies that are borrowed/reserved/checked out.
		 */
		//@formatter:off
	    q = this.em.createQuery(
	    		"SELECT COUNT(*) " +
	    		"FROM BorrowedItem b " +
	    			"WHERE " +
	    				"b.itemId = :itemId AND " +
	    				"b.type = :type",
				Long.class
		);
	    //@formatter:on
		
		// Set the parameters
		q.setParameter("itemId", item.getItemId());
		q.setParameter("type", item.getType());
		
		// Get the results
		try
		{
			borrowedCount = q.getSingleResult();
		}
		catch (NoResultException e)
		{
			ItemBusiness.log.error("No Result returned while trying to find the count of copies of Item [" + item.getTitle()
			        + "] of Type [" + item.getType() + "] currently borrowed.");
			e.printStackTrace();
		}
		
		/*
		 * Step 2 - Get the number of this item's copies that are requested.
		 */
		//@formatter:off
		q = this.em.createQuery(
				"SELECT COUNT(*) " +
				"FROM RequestedItem r " +
					"WHERE " +
						"r.itemId = :itemId AND " +
						"r.type = :type",
				Long.class
		);
		//@formatter:on
		
		// Set the parameters
		q.setParameter("itemId", item.getItemId());
		q.setParameter("type", item.getType());
		
		// Get the results
		try
		{
			requestedCount = q.getSingleResult();
		}
		catch (NoResultException e)
		{
			ItemBusiness.log.error("No Result returned while trying to find the count of copies of Item [" + item.getTitle()
			        + "] of Type [" + item.getType() + "] currently requested.");
			e.printStackTrace();
		}
		
		/*
		 * Step 3 - Sum the two counts and return the value.
		 */
		return (borrowedCount.longValue() + requestedCount.longValue());
	}
	
}
