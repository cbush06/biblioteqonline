//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb
 * File: UserBusiness.java
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
 * This EJB provides all the methods directly related to User entities. These entities represent online users tied to Members and/or Admins.
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.entities.Admin;
import org.biblioteq.ejb.entities.Member;
import org.biblioteq.ejb.entities.User;
import org.biblioteq.ejb.interfaces.UserBusinessLocal;
import org.biblioteq.ejb.interfaces.UserBusinessRemote;

/**
 * @author Clint Bush
 * 
 */
@Stateless(name = "UserBusiness")
public class UserBusiness implements UserBusinessLocal, UserBusinessRemote, Serializable
{
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = -599668820199162709L;
	
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(UserBusiness.class);
	
	// Get the entity manager
	@PersistenceContext(unitName = "BiblioteQPersistenceUnit")
	private EntityManager em;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.UserBusinessLocal#createNewUser(java.lang.String, java.lang.String, org.biblioteq.ejb.entities.Member, java.lang.String, boolean,
	 * org.biblioteq.ejb.entities.User)
	 */
	@Override
	public User createNewUser(String userName, String password, Member member, Admin admin, boolean isActive, User createdBy)
	{
		// Create a new User object
		User newUser = new User();
		newUser.setUserName(userName);
		newUser.setPassword(password);
		newUser.setMember(member);
		newUser.setAdmin(admin);
		newUser.setActive(isActive);
		newUser.setCreatedBy(createdBy);
		newUser.setUpdatedBy(createdBy);
		newUser.setDateCreated((Calendar.getInstance()).getTime());
		newUser.setDateUpdated((Calendar.getInstance()).getTime());
		
		// Merge the new User object into the Persistence Context
		newUser = this.em.merge(newUser);
		
		// Return the new User object
		return newUser;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.UserBusinessLocal#getAllUsers()
	 */
	@Override
	public List<User> getAllUsers()
	{
		// Declare the return value
		List<User> returnVal = new ArrayList<User>();
		
		// Create the query
		TypedQuery<User> q = this.em.createQuery("SELECT u FROM User u", User.class);
		
		// Get the results
		returnVal = q.getResultList();
		
		// Return
		return returnVal;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.UserBusinessLocal#getTotalUsers()
	 */
	@Override
	public long getTotalUsers()
	{
		// Declare the return value
		Long returnVal = 0L;
		
		// Create the Query
		// @formatter:off
		TypedQuery<Long> q = this.em.createQuery(
				"SELECT COUNT(*) FROM User u",
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
			UserBusiness.log.error("No result returned while trying to count users.");
			e.printStackTrace();
		}
		
		return returnVal.longValue();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.UserBusinessLocal#getUser(java.lang.String)
	 */
	@Override
	public User getUser(String userName)
	{
		//@formatter:off
		TypedQuery<User> q = this.em.createQuery(
				"SELECT u " +
			    "FROM User u " +
			    "WHERE " +
			    	"lower(u.userName) = lower(:userName)",
		    	User.class
    	);
		//@formatter:on
		
		// Add the parameters
		q.setParameter("userName", userName);
		
		// Get the results of the query
		List<User> resultList = q.getResultList();
		
		// If we did not receive exactly 1 record, return null
		if (resultList.size() != 1)
		{
			return null;
		}
		
		// Return the matching user
		return resultList.get(0);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.UserBusinessLocal#getUser(java.lang.String, java.lang.String)
	 */
	@Override
	public User getUser(String userName, String password)
	{
		//@formatter:off
    	TypedQuery<User> q = this.em.createQuery(
    			"SELECT u " +
    			"FROM User u " +
				"WHERE " +
					"lower(u.userName) = lower(:userName) AND " +
					"u.password = :password",
				User.class
		);
    	//@formatter:on
		
		// Add the parameters
		q.setParameter("userName", userName);
		q.setParameter("password", password);
		
		// Get the results of the query
		List<User> resultList = q.getResultList();
		
		// If we did not receive exactly 1 record, return null
		if (resultList.size() != 1)
		{
			return null;
		}
		
		// Return the matching user
		return resultList.get(0);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.UserBusinessLocal#getUserForMember(org.biblioteq.ejb.entities.Member)
	 */
	@Override
	public User getUserForMember(Member member)
	{
		// Refresh the member account so we have one that is managed
		member = this.em.find(Member.class, member.getMemberId());
		
		// If we successfully refreshed the member object
		if (member != null)
		{
			//@formatter:off
			TypedQuery<User> q = this.em.createQuery(
					"SELECT u " +
					"FROM User u " +
					"WHERE " +
						"u.member = :member",
					User.class
			);
			//@formatter:on
			
			// Add the parameters
			q.setParameter("member", member);
			
			// Get the results of the query
			List<User> resultList = q.getResultList();
			
			// If we received anything, return the first element
			if (resultList.size() > 0)
			{
				return resultList.get(0);
			}
		}
		
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.UserBusinessLocal#refreshUser(org.biblioteq.ejb.entities.User)
	 */
	@Override
	public User refreshUser(User user)
	{
		User toRefresh = this.em.find(User.class, user.getId());
		this.em.refresh(toRefresh);
		return toRefresh;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.UserBusinessLocal#saveUser(org.biblioteq.ejb.entities.User)
	 */
	@Override
	public User saveUser(User user)
	{
		// If we successfully refreshed the User object
		if (user != null)
		{
			try
			{
				user = this.em.merge(user);
			}
			catch (Exception e)
			{
				UserBusiness.log.error("Exception thrown while trying to merge User to database.");
				e.printStackTrace();
			}
		}
		else
		{
			UserBusiness.log.error("User is null while trying to save.");
		}
		
		// If we faild, return null
		return user;
	}
}
