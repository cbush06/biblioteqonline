//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb
 * File: MemberBusiness.java
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
 * This EJB provides all methods directly related to Member entities. These entities represent library members.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Apr 7, 2012, Clinton Bush, 1.0.0,
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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.entities.Member;
import org.biblioteq.ejb.interfaces.MemberBusinessLocal;
import org.biblioteq.ejb.interfaces.MemberBusinessRemote;

/**
 * This EJB provides methods that access the database for CRUD operations on the Member table and Member entities.
 * 
 * @author Clint Bush
 * 
 */
@Stateless(name = "MemberBusiness")
public class MemberBusiness implements MemberBusinessLocal, MemberBusinessRemote, Serializable
{
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 2221859262701653548L;
	
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
	 * @see org.biblioteq.ejb.interfaces.MemberBusinessLocal#getMatchingMember(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Member getMatchingMember(String firstName, String lastName, String dateOfBirth, String phoneNumber, String zipCode, String eMail)
	{
		// Check the phone number. If it's empty, add 2 dashes (--) to match BiblioteQ's way of storing an empty phone number.
		if (phoneNumber.length() < 1)
		{
			phoneNumber = "--";
		}
		
		// Check the eMail. If it's empty, add 2 single quotes ('') to match BiblioteQ's way of storing an empty string.
		if (eMail.length() < 1)
		{
			eMail = "''";
		}
		
		//@formatter:off
		TypedQuery<Member> q = this.em.createQuery(
				"SELECT m " +
				"FROM Member m " +
				"WHERE " +
					"lower(m.firstName) = lower(:firstName) AND " +
					"lower(m.lastName) = lower(:lastName) AND " +
					"m.dob = :dateOfBirth AND " +
					"m.telephone = :phoneNumber AND " +
					"m.zip = :zipCode AND " +
					"lower(m.email) = lower(:eMail)",
				Member.class
		);
		//@formatter:on
		
		// Add the parameters
		q.setParameter("firstName", firstName);
		q.setParameter("lastName", lastName);
		q.setParameter("dateOfBirth", dateOfBirth);
		q.setParameter("phoneNumber", phoneNumber);
		q.setParameter("zipCode", zipCode);
		q.setParameter("eMail", eMail);
		
		// Get the results of our query
		List<Member> resultList = q.getResultList();
		
		// If we received anything other than exactly 1 result, return NULL
		if (resultList.size() != 1)
		{
			return null;
		}
		
		// If everything was successful, return our 1 and only result.
		return resultList.get(0);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.MemberBusinessLocal#getTotalMembers()
	 */
	@Override
	public long getTotalMembers()
	{
		// The Return Value
		Long returnVal = 0L;
		
		// Build the Query
		// @formatter:off
		TypedQuery<Long> q = this.em.createQuery(
				"SELECT COUNT(*) FROM Member m",
				Long.class
		);
		// @formatter:on
		
		// Get the Return Value
		try
		{
			returnVal = q.getSingleResult();
		}
		catch (NoResultException e)
		{
			MemberBusiness.log.error("No result returned while trying to count Members.");
		}
		
		return returnVal.longValue();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.MemberBusinessLocal#saveMember(org.biblioteq.ejb.entities.Member)
	 */
	@Override
	public Member saveMember(Member member)
	{
		// If we successfully refreshed the member object
		if (member != null)
		{
			try
			{
				member = this.em.merge(member);
			}
			catch (Exception e)
			{
				MemberBusiness.log.error("Exception thrown while trying to merge Member to database.");
				e.printStackTrace();
			}
		}
		else
		{
			MemberBusiness.log.error("Member is null while trying to save.");
		}
		
		// If we failed, return null
		return member;
	}
}
