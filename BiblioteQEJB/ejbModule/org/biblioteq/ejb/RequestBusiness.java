//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb
 * File: RequestBusiness.java
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
 * The RequestBusiness EJB handles all database transactions directly related to the library request process.
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
import org.biblioteq.ejb.entities.Member;
import org.biblioteq.ejb.entities.RequestedItem;
import org.biblioteq.ejb.interfaces.Item;
import org.biblioteq.ejb.interfaces.RequestBusinessLocal;
import org.biblioteq.ejb.interfaces.RequestBusinessRemote;
import org.biblioteq.ejb.model.Request;

/**
 * The RequestBusiness EJB handles all database transactions directly related to the library request process.
 * 
 * @author Clint Bush
 * 
 */
@Stateless(name = "RequestBusiness")
public class RequestBusiness implements RequestBusinessLocal, RequestBusinessRemote, Serializable
{
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = -8212047759626718070L;
	
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(RequestBusiness.class);
	
	// Get the entity manager
	@PersistenceContext(unitName = "BiblioteQPersistenceUnit")
	private EntityManager em;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.RequestBusinessLocal#createNewRequest(org.biblioteq.ejb.entities.Member, org.biblioteq.ejb.interfaces.Item)
	 */
	@Override
	public Request createNewRequest(Member member, Item item)
	{
		// Declare the variables we'll be using
		Request returnVal = null;
		Date today = new Date();
		
		/*
		 * Step 1 - Create the new RequestedItem object.
		 */
		RequestedItem requestedItem = new RequestedItem();
		requestedItem.setItemId(item.getItemId());
		requestedItem.setRequestDateAsDate(today);
		requestedItem.setRequestor(member);
		requestedItem.setType(item.getType());
		
		/*
		 * Step 2 - Persist the new RequestedItem object.
		 */
		requestedItem = this.em.merge(requestedItem);
		
		/*
		 * Step 3 - Create the Request object to return.
		 */
		returnVal = new Request(item, requestedItem);
		
		return returnVal;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.RequestBusinessLocal#getMemberRequests(org.biblioteq.ejb.entities.Member)
	 */
	@Override
	public List<Request> getMemberRequests(Member member)
	{
		// Prepare the return value
		ArrayList<Request> returnVal = new ArrayList<Request>();
		
		/*
		 * Step 1 is to get the RequestedItem records that represent all requests made by the member.
		 */
		//@formatter:off
		TypedQuery<RequestedItem> q = this.em.createQuery(
				"SELECT r " +
				"FROM RequestedItem r " +
				"WHERE " +
					"r.requestor = :requestor",
				RequestedItem.class
		);
		//@formatter:on
		
		// Add the parameters
		q.setParameter("requestor", member);
		
		// Get the results of the query
		List<RequestedItem> resultList = q.getResultList();
		
		// If no results were returned (the user has no requested items), return an empty list
		if (resultList.size() < 1)
		{
			return new ArrayList<Request>();
		}
		
		/*
		 * Step 2 is to try to resolve the type of requested item to an entity type; then, we will get the actual entity object for that item
		 */
		for (RequestedItem nextRequest : resultList)
		{
			Class<?> nextItemClass = null;
			
			// Try to resolve the type of the item to an entity class type
			try
			{
				nextItemClass = Class.forName("org.biblioteq.ejb.entities." + nextRequest.getType());
			}
			catch (ClassNotFoundException e)
			{
				RequestBusiness.log.error("Error finding entity class type for: " + nextRequest.getType() + "\n\nException Message: "
				        + e.getMessage());
				e.printStackTrace();
				continue;
			}
			
			// If the item's Entity class type could not be found, continue on
			if (nextItemClass == null)
			{
				RequestBusiness.log.error("Entity Class could not be found for: " + nextRequest.getType());
				continue;
			}
			
			// Let's try to retrieve the actual entity for this item
			//@formatter:off
			Query q2 = this.em.createQuery(
					"SELECT o " +
					"FROM " + nextRequest.getType() + " o " +
					"WHERE " +
						"o.itemId = :itemId",
					nextItemClass
			);
			//@formatter:on
			
			// Add the parameters
			q2.setParameter("itemId", nextRequest.getItemId());
			
			// Execute the query
			Item nextItem = null;
			try
			{
				nextItem = (Item) q2.getSingleResult();
			}
			catch (NoResultException e)
			{
				RequestBusiness.log.error("No item returned while trying to resolve the item of a RequestedItem to its entity object.");
				e.printStackTrace();
			}
			catch (NonUniqueResultException e)
			{
				RequestBusiness.log
				        .error("More than one item returned while trying to resolve the item of a RequestedItem to its entity object.");
			}
			
			// If the RequestedItem's Entity object could not be found, continue on
			if (nextItem == null)
			{
				RequestBusiness.log.error("Entity object could not be found for [" + nextRequest.getItemId() + "] of type ["
				        + nextRequest.getType() + "].");
				continue;
			}
			
			// Now that we have the actual item's Entity object, let's add a new Request object to the return list
			returnVal.add(new Request(nextItem, nextRequest));
		}
		
		/*
		 * Step 3 is to return the list of Request objects. It now contains one Request object for each request made by the Member. Each of these objects contains the Entity object
		 * of the requested item and the RequestedItem object that represents the request made by the Member.
		 */
		return returnVal;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.RequestBusinessLocal#getTotalRequests()
	 */
	@Override
	public long getTotalRequests()
	{
		// Declare the return value
		Long returnVal = 0L;
		
		// Create the query
		TypedQuery<Long> q = this.em.createQuery("SELECT COUNT(*) FROM RequestedItem r", Long.class);
		
		// Get the result
		try
		{
			returnVal = q.getSingleResult();
		}
		catch (NoResultException e)
		{
			RequestBusiness.log.error("No result returned while trying to count total Requests.");
			e.printStackTrace();
		}
		
		// Return the result
		return returnVal.longValue();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.RequestBusinessLocal#removeRequest(org.biblioteq.ejb.model.Request)
	 */
	@Override
	public void removeRequest(Request request)
	{
		try
		{
			RequestedItem requestedItem = this.em.find(RequestedItem.class, request.getRequest().getId());
			this.em.remove(requestedItem);
		}
		catch (Exception e)
		{
			RequestBusiness.log.error("Exception occured while trying to remove a RequestedItem from persistence.");
			e.printStackTrace();
		}
	}
}
