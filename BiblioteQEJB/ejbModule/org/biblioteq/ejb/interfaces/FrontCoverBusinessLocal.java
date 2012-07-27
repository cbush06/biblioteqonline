//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.interfaces
 * File: FrontCoverBusinessLocal.java
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
 * Local interface for the FrontCoverBusiness EJB.
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
package org.biblioteq.ejb.interfaces;

import javax.ejb.Local;

/**
 * Local interface for the FrontCoverBusiness EJB.
 * 
 * @author Clint Bush
 * 
 */
@Local
public interface FrontCoverBusinessLocal
{
	/**
	 * Attempts to locate the front cover of an item and return its byte array.
	 * 
	 * @param type
	 *            (String) The type of item (e.g. Book, DVD, Journal, etc.) as defined by BiblioteQ.
	 * @param itemId
	 *            (String) The ID of the item.
	 * @return (byte[]) A byte array containing the bytes of the image.
	 */
	public abstract byte[] getFrontCover(String type, String itemId);
}
