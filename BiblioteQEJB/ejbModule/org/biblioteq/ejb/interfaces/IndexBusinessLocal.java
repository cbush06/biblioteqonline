//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.interfaces
 * File: IndexBusinessLocal.java
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
 * Local interface for IndexBusiness EJB.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jun 7, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.ejb.interfaces;

import java.util.List;

import javax.ejb.Local;

/**
 * Local interface for IndexBusiness EJB.
 * 
 * @author Clint Bush
 * 
 */
@Local
public interface IndexBusinessLocal
{
	/**
	 * Returns the next batch of records to be indexed.
	 * 
	 * @return (List<Item>) List of next batch of Items to be indexed.
	 */
	public abstract List<Item> getNextBatch();
	
	/**
	 * Returns the number of records returned so far.
	 * 
	 * @return (long) The count of records returned.
	 */
	public abstract long getReturnedRecords();
	
	/**
	 * Returns the total number of records to be indexed.
	 * 
	 * @return (long) The count of all records to be indexed.
	 */
	public abstract long getTotalRecords();
	
	/**
	 * Resets/Removes the IndexBusiness Stateful EJB.
	 */
	public abstract void reset();
}
