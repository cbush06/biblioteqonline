//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.interfaces
 * File: UserBusinessLocal.java
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
 * This interface provides a definition of the local methods for the UserBusiness EJB.
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
package org.biblioteq.ejb.interfaces;

import java.util.List;

import javax.ejb.Local;

import org.biblioteq.ejb.entities.Admin;
import org.biblioteq.ejb.entities.Member;
import org.biblioteq.ejb.entities.User;

/**
 * Local interface for the UserBusiness EJB.
 * 
 * @author Clint Bush
 * 
 */
@Local
public interface UserBusinessLocal
{
	/**
	 * Create a new User.
	 * 
	 * @param userName
	 *            (String) The username for the new user.
	 * @param password
	 *            (String) The password for the new user. This should be MD5 encrypted with the length prepended to the password like so: "3:cat" (the quotes should not be
	 *            included).
	 * @param member
	 *            (String) The library Member to which this User account will belong.
	 * @param admin
	 *            (Admin) If this account belongs to an administrator, this is the Admin object representing the adminsitrator.
	 * @param isActive
	 *            (boolean) Set the account to be active or inactive (enabled or disabled).
	 * @param createdBy
	 *            (User) If this is created by the "Register" page, set this to null. If this was created by an administrator or other user, set this to their account.
	 * @return (User) If the creation was successful, the new User object; otherwise, null.
	 */
	public abstract User createNewUser(String userName, String password, Member member, Admin admin, boolean isActive, User createdBy);
	
	/**
	 * Returns all users.
	 * 
	 * @return (List<User>) List of all users.
	 */
	public abstract List<User> getAllUsers();
	
	/**
	 * Returns the total number of online users.
	 * 
	 * @return (long) Total count of online users.
	 */
	public abstract long getTotalUsers();
	
	/**
	 * Finds a user with a matching username.
	 * 
	 * @param userName
	 *            (String) The user's username. This will be treated as case-insensitive.
	 * @return (User) The user whose username matched that provided.
	 */
	public abstract User getUser(String userName);
	
	/**
	 * Finds a user with a matching username and password.
	 * 
	 * @param userName
	 *            (String) The user's username. This will be treated as case-insensitive.
	 * @param password
	 *            (String) The user's password. This should be MD5 encrypted.
	 * @return (User) The user whose username and password match those provided.
	 */
	public abstract User getUser(String userName, String password);
	
	/**
	 * Find the online user that belongs to the specified library member.
	 * 
	 * @param member
	 *            (Member) The library member whose online user account is to be returned.
	 * @return (User) The online user account belonging to the specified member.
	 */
	public abstract User getUserForMember(Member member);
	
	/**
	 * Refreshes all fields of this entity from the database.
	 * 
	 * @param user
	 *            (User) The user entity to be refreshed.
	 * @return (User) The refreshed User entity.
	 */
	public abstract User refreshUser(User user);
	
	/**
	 * Save a user back to the database.
	 * 
	 * @param user
	 *            (User) The user object to save.
	 * @return (User) The merged user.
	 */
	public abstract User saveUser(User user);
}
