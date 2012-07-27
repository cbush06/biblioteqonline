//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.interfaces
 * File: RequestBusinessLocal.java
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
 * Local interface for the RequestBusiness EJB.
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
package org.biblioteq.ejb.interfaces;

import java.util.List;

import javax.ejb.Local;

import org.biblioteq.ejb.entities.Member;
import org.biblioteq.ejb.model.Request;

/**
 * Local interface for the RequestBusiness EJB.
 * 
 * @author Clint Bush
 * 
 */
@Local
public interface RequestBusinessLocal
{
	/**
	 * Creates a new RequestedItem entity for the Member and Item (book, dvd, etc.) specified. The new entity is persisted to the database and a Request object is returned. The
	 * Request object is a wrapper for the RequestedItem entity and the Item (book, dvd, etc.).
	 * 
	 * @param member
	 *            (Member) The library member making the request.
	 * @param item
	 *            (Item) The Item a request is being created for.
	 * @return (Request) A new Request object that encapsulates the Item being requested and the RequestedItem entity that represents the actual request.
	 */
	public abstract Request createNewRequest(Member member, Item item);
	
	/**
	 * Retrieves all requests made by the specified library member and returns them as a list of Request objects.
	 * 
	 * @param member
	 *            (Member) The member whose requests will be returned.
	 * @return (List<Request>) List of Request objects that provide a common model for housing the Item (book, dvd, etc.) and RequestedItem that represents that actual request made
	 *         by the member.
	 */
	public abstract List<Request> getMemberRequests(Member member);
	
	/**
	 * Returns the total number of Requests.
	 * 
	 * @return (long) Total count of Requests.
	 */
	public abstract long getTotalRequests();
	
	/**
	 * Removes the RequestItem object of the provided Request from persistence.
	 * 
	 * @param request
	 *            (Request) The Request that contains the RequestedItem to be removed.
	 */
	public abstract void removeRequest(Request request);
}
