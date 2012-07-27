//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.security
 * File: Login_Security.java
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
 * This file should provide convenience methods for logging users in and out of the application, verifying their login status, checking their credentials, and any other
 * security related tasks.
 * 
 * #######################
 * #      Revision       #
 * ####################### 
 * Mar 3, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.security;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.entities.User;
import org.biblioteq.ejb.interfaces.UserBusinessLocal;
import org.biblioteq.web.common.Constants;

@ManagedBean(name = "Login_Security")
@SessionScoped
public class Login_Security
{
	/**
	 * Get the logger.
	 */
	private final Logger log = Logger.getLogger(Login_Security.class);
	
	/**
	 * Get a copy of our UserBusiness EJB for working with user accounts, logging in and out, etc.
	 */
	@EJB(name = "UserBusiness")
	private UserBusinessLocal userEjb;
	
	/**
	 * Default constructor
	 */
	public Login_Security()
	{
		
	}
	
	/**
	 * Logs a user into the system. This sets appropriate variables in the Session Map and returns true if the login is successful.
	 * 
	 * @param userName
	 *            (String) The user's username.
	 * @param password
	 *            (String) The user's MD5 encrypted password.
	 * @return (boolean) True if the login was successful.
	 */
	public boolean login(String userName, String password)
	{
		// Attempt to match the username and password to an account
		User loginUser = this.userEjb.getUser(userName, password);
		
		// If it matches, add a reference to that user to the SessionMap and return true
		if (loginUser != null && loginUser.isActive())
		{
			// Add the user to the session map
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(Constants.SESSION_LOGGED_IN_USER, loginUser);
			return true;
		}
		
		// If no user was found, return false
		return false;
	}
	
	/**
	 * Logs a user out of the system. This removes the User entry from the Session Map and invalidates the session. This method should not be used, as it is better to direct the
	 * user to the /logout/ directory so the LogOutServlet can handle logging them out.
	 * 
	 * @return (boolean) True if the logout was successful; otherwise, false.
	 */
	public boolean logout()
	{
		// Remove the User entry from the SessionMap
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(Constants.SESSION_LOGGED_IN_USER);
		
		// Attempt to get the current session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		
		// If an HttpSession is returned, invalidate it
		if (session != null)
		{
			try
			{
				session.invalidate();
			}
			catch (IllegalStateException e)
			{
				this.log.error("An IllegalStateException was thrown while trying to invalidate the HttpSession to log out.");
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		return false;
	}
}
