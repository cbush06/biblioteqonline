//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: Admin.java
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
 * #      Revision       #
 * ####################### 
 * Mar 4, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.ejb.entities;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

/**
 * Entity representing entries of the "admin" table.
 * 
 * @author Clint Bush
 * 
 */
@Entity(name = "Admin")
@Table(name = "admin")
public class Admin implements Serializable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(Admin.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 4841159116570719887L;
	
	private String username = "";
	private String roles = "";
	
	/**
	 * Get the original stored value of the administrator's roles (separated by spaces if anything other than administrator -- circulation, librarian, and/or membership).
	 * 
	 * @return (String) The roles.
	 */
	@Column(name = "roles")
	public String getRoles()
	{
		return this.roles;
	}
	
	/**
	 * Get the administrator's user name.
	 * 
	 * @return (String) The username.
	 */
	@Id
	@Column(name = "username", length = 128)
	public String getUsername()
	{
		return this.username;
	}
	
	/**
	 * Convenience method that determines if the administrator keyword exists in the roles sequence.
	 * 
	 * @return (boolean) True if the user is an administrator; otherwise, false.
	 */
	@Transient
	public boolean isAdministrator()
	{
		Pattern p = Pattern.compile("administrator");
		Matcher m = p.matcher(this.roles);
		
		return m.find();
	}
	
	/**
	 * Convenience method that determines if the circulation keyword exists in the roles sequence.
	 * 
	 * @return (boolean) True if the user has circulation privileges; otherwise, false.
	 */
	@Transient
	public boolean isCirculation()
	{
		Pattern p = Pattern.compile("circulation");
		Matcher m = p.matcher(this.roles);
		
		return m.find();
	}
	
	/**
	 * Convenience method that determines if the librarian keyword exists in the roles sequence.
	 * 
	 * @return (boolean) True if the user has librarian privileges; otherwise, false.
	 */
	@Transient
	public boolean isLibrarian()
	{
		Pattern p = Pattern.compile("librarian");
		Matcher m = p.matcher(this.roles);
		
		return m.find();
	}
	
	/**
	 * Convenience method that determines if the membership keyword exists in the roles sequence.
	 * 
	 * @return (boolean) True if the user has membership privileges; otherwise, false.
	 */
	@Transient
	public boolean isMembership()
	{
		Pattern p = Pattern.compile("membership");
		Matcher m = p.matcher(this.roles);
		
		return m.find();
	}
	
	/**
	 * Sets the roles of the Administrator. Should be private and never used.
	 * 
	 * @param roles
	 *            (String) Administrator roles.
	 */
	private void setRoles(String roles)
	{
		this.roles = roles;
	}
	
	/**
	 * Sets the Administrator's username. Should be private and never used.
	 * 
	 * @param username
	 *            (String) Admin username.
	 */
	private void setUsername(String username)
	{
		this.username = username;
	}
}
