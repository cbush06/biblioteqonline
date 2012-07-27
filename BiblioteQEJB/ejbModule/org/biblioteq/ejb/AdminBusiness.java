//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb
 * File: AdminBusiness.java
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
 * Provides methods for working with Admin entities in the database.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * May 8, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.ejb;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.entities.Admin;
import org.biblioteq.ejb.interfaces.AdminBusinessLocal;
import org.biblioteq.ejb.interfaces.AdminBusinessRemote;

/**
 * Provides methods for working with Admin entities in the database.
 * 
 * @author Clint Bush
 * 
 */
@Stateless(name = "AdminBusiness")
public class AdminBusiness implements AdminBusinessLocal, AdminBusinessRemote, Serializable
{
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = -4443965197489890407L;
	
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(MemberBusiness.class);
	
	// Get the entity manager
	@PersistenceContext(unitName = "BiblioteQPersistenceUnit")
	private EntityManager em;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.AdminBusinessLocal#getMatchingAdmin(java.lang.String)
	 */
	@Override
	public Admin getMatchingAdmin(String userName)
	{
		/*
		 * Attempt to locate an Admin with the matching username.
		 */
		//@formatter:off
		TypedQuery<Admin> q = this.em.createQuery(
				"SELECT a FROM Admin a " +
				"WHERE " +
					"a.username = :username"
				, Admin.class);
		//@formatter:on
		
		// Add the parameters
		q.setParameter("username", userName);
		
		// Get the results of our query
		List<Admin> results = q.getResultList();
		
		// If no results were returned, return null
		if (results.size() < 1)
		{
			return null;
		}
		
		// If there were results, there could only be one
		return results.get(0);
	}
	
}
