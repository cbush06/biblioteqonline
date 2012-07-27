//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.interfaces
 * File: ItemBusinessLocal.java
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
 * The local interface for the ItemBusiness EJB.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jun 9, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.ejb.interfaces;

import javax.ejb.Local;

/**
 * The local interface for the ItemBusiness EJB.
 * 
 * @author Clint Bush
 * 
 */
@Local
public interface ItemBusinessLocal
{
	/**
	 * Using the provided ID (type is determined by item type -- i.e. ISBN-10 for books, UPC barcode for CDs, etc.) and the item type (the name of the item type's entity), this
	 * method will find that particular item and return it.
	 * 
	 * @param id
	 *            (String) The ID of the item that, as determined by its type.
	 * @param type
	 *            (String) The item type (i.e. its entity name).
	 * @return (Item) The Item/Entity matching the provided criteria.
	 */
	public abstract Item getItemById(String id, String type);
	
	/**
	 * Returns the number of this item's copies that are either borrowed or requested.
	 * 
	 * @param item
	 *            (Item) The Item to query about.
	 * @return (Long) The number of copies borrowed or requested.
	 */
	public abstract Long getNumberCheckedOutOrRequested(Item item);
}
