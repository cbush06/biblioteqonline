//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.backing
 * File: Screen_Backing.java
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
 * This class provides super class with general methods needed by all backing beans. These methods are to be inherited by all backing beans for the convenience
 * of the programmer.
 * 
 * #######################
 * #      Revision       #
 * ####################### 
 * Apr 5, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.backing;

import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.entities.User;
import org.biblioteq.web.common.Constants;

/**
 * This class provides super class with general methods needed by all backing beans. These methods are to be inherited by all backing beans for the convenience
 * of the programmer.
 * 
 * @author Clint Bush
 * 
 */
public class Screen_Backing
{
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(Screen_Backing.class);
	
	/**
	 * Get a copy of the Constants bean. This will be injected via the setConstants(Constants constants) method.
	 */
	@ManagedProperty("#{Constants}")
	private Constants constants;
	
	/**
	 * The current user.
	 */
	protected User currentUser = null;
	
	public Screen_Backing()
	{
		// Get the current user from the Session Map
		this.currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		        .get(Constants.SESSION_LOGGED_IN_USER);
	}
	
	/**
	 * Returns the user who is currently logged in.
	 * 
	 * @return (User) Current user.
	 */
	public User getCurrentUser()
	{
		return this.currentUser;
	}
	
	/**
	 * Returns the root path for the web site.
	 * 
	 * @return (String) Root path.
	 */
	public String getRootPath()
	{
		return FacesContext.getCurrentInstance().getExternalContext().getRequestServerName() + ":"
		        + FacesContext.getCurrentInstance().getExternalContext().getRequestServerPort()
		        + FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
	}
	
	/**
	 * Used to inject the Constants bean into this class.
	 * 
	 * @param constants
	 *            (Constants) The constants to set.
	 */
	public void setConstants(Constants constants)
	{
		this.constants = constants;
	}
}
