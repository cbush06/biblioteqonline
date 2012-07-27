//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.interfaces
 * File: AdminBusinessLocal.java
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
 * This interface provides a definition of the local methods for the AdminBusiness EJB.
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
package org.biblioteq.ejb.interfaces;

import javax.ejb.Local;

import org.biblioteq.ejb.entities.Admin;

/**
 * Local interface for the AdminBusiness EJB.
 * 
 * @author Clint Bush
 * 
 */
@Local
public interface AdminBusinessLocal
{
	/**
	 * Attempts to find an Admin with the provided username. If such an Admin is found, it returns that object; otherwise, null is returned.
	 * 
	 * @param userName
	 *            (String) The username of the Admin to be found.
	 * @return (Admin) The Admin object if one exists with the provided username; otherwise, null.
	 */
	public abstract Admin getMatchingAdmin(String userName);
}
