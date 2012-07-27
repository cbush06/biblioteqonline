//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb
 * File: FrontCoverBusiness.java
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
 * Provides methods for retrieving the Front Cover images of items.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * May 10, 2012, Clinton Bush, 1.0.0,
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
import javax.persistence.Query;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.biblioteq.ejb.interfaces.FrontCoverBusinessLocal;
import org.biblioteq.ejb.interfaces.FrontCoverBusinessRemote;
import org.biblioteq.ejb.interfaces.Item;

/**
 * Provides methods for retrieving the Front Cover images of items.
 * 
 * @author Clint Bush
 * 
 */
@Stateless(name = "FrontCoverBusiness")
public class FrontCoverBusiness implements FrontCoverBusinessLocal, FrontCoverBusinessRemote, Serializable
{
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = -4494905015213090540L;
	
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(FrontCoverBusiness.class);
	
	// Get the entity manager
	@PersistenceContext(unitName = "BiblioteQPersistenceUnit")
	private EntityManager em;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.FrontCoverBusinessLocal#getFrontCover(java.lang.String, java.lang.String)
	 */
	@Override
	public byte[] getFrontCover(String type, String itemId)
	{
		Class<?> itemClass = null;
		
		try
		{
			itemClass = Class.forName("org.biblioteq.ejb.entities." + type);
		}
		catch (ClassNotFoundException e)
		{
			FrontCoverBusiness.log.error("Error finding entity class type for: " + type + "\n\nException Message:" + e.getMessage());
			e.printStackTrace();
			return new byte[] {};
		}
		
		if (itemClass != null)
		{
			//@formatter:off
			Query q = this.em.createQuery(
					"SELECT o FROM " + type + " o " +
					"WHERE " +
						"o.id = :itemId",
					itemClass
			);
			//@formatter:on
			
			// Add the parameters
			q.setParameter("itemId", itemId);
			
			// Get the results of the query
			Item theItem = null;
			try
			{
				theItem = (Item) q.getSingleResult();
			}
			catch (NoResultException e)
			{
				FrontCoverBusiness.log.error("No item returned while trying to resolve " + itemId + " to an item of type " + type + ".");
				e.printStackTrace();
			}
			catch (NonUniqueResultException e)
			{
				FrontCoverBusiness.log.error("More than one item returned while trying to resolve " + itemId + " to an item of type "
				        + type + ".");
				e.printStackTrace();
			}
			
			// If no results were returned, return an empty byte[]
			if (theItem == null)
			{
				FrontCoverBusiness.log.error("Could not find an object for " + itemId + " of type " + type
				        + " to get its front cover image.");
				return new byte[] {};
			}
			else
			{
				return Base64.decodeBase64(theItem.getFrontCover());
			}
		}
		
		return null;
	}
	
}
