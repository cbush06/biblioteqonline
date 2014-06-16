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
 * Jun 07, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * Aug 03, 2012, Clinton Bush, 1.1.2,
 *    Added methods to support indexing creators and subjects, and for supporting browsing.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.ejb.interfaces;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import org.biblioteq.ejb.model.BrowseItem;

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
	 * If the term is already in the index, this increments the total count. If it is not in the index, it is added with a total count of 1.
	 * 
	 * @param indexTerm
	 *            (String) The term to index.
	 * @param count
	 *            (int) The count of times this term exists.
	 */
	public abstract void addOrUpdateCreatorIndex(String indexTerm, int count);
	
	/**
	 * If the term is already in the index, this increments the total count. If it is not in the index, it is added with a total count of 1.
	 * 
	 * @param indexTerm
	 *            (String) The term to index.
	 * @param count
	 *            (int) The count of times this term exists.
	 */
	public abstract void addOrUpdateSubjectIndex(String indexTerm, int count);
	
	/**
	 * Deletes all entries from the creator_index table.
	 */
	public abstract void clearCreatorIndex();
	
	/**
	 * Deletes all entries from the subject_index table.
	 */
	public abstract void clearSubjectIndex();
	
	/**
	 * Returns an ArrayList of BrowseItems containing the results of a query specified by the type of browsing the user is conducting.
	 * 
	 * @param startIndex
	 *            (int) The index to begin retrieving results from.
	 * @param length
	 *            (int) The number of results to get.
	 * @return (ArrayList<BrowseItem>) Query results.
	 */
	public abstract ArrayList<BrowseItem> getBrowseResults(int startIndex, int length);
	
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
	 * Returns the total count of creators indexed.
	 * 
	 * @return (long) Count of creators.
	 */
	public abstract long getTotalCreators();
	
	/**
	 * Returns the total count of locations indexed.
	 * 
	 * @return (long) Count of locations.
	 */
	public abstract long getTotalLocations();
	
	/**
	 * Returns the total count of locations that have items assigned to them.
	 * 
	 * @return (long) Count of locations that actually have items assigned to them.
	 */
	public abstract long getTotalNonEmptyLocations();
	
	/**
	 * Returns the total number of records to be indexed.
	 * 
	 * @return (long) The count of all records to be indexed.
	 */
	public abstract long getTotalRecords();
	
	/**
	 * Returns the total count of subjects indexed.
	 * 
	 * @return (long) Count of subjects.
	 */
	public abstract long getTotalSubjects();
	
	/**
	 * Returns the total count of item types.
	 * 
	 * @return (int) Count of item types.
	 */
	public abstract int getTotalTypes();
	
	/**
	 * Resets/Removes the IndexBusiness Stateful EJB.
	 */
	public abstract void reset();
	
	/**
	 * Creates a new query to retrieve and paginate results shown while a user is browsing by Creator.
	 */
	public abstract void setBrowseQueryByCreator();
	
	/**
	 * Creates a new query to retrieve and paginate results shown while a user is browsing by Location.
	 */
	public abstract void setBrowseQueryByLocation();
	
	/**
	 * Creates a new query to retrieve and paginate results shown while a user is browsing by Subject.
	 */
	public abstract void setBrowseQueryBySubject();
	
	/**
	 * Creates a new query to retrieve and paginate results shown while a user is browsing by Type.
	 */
	public abstract void setBrowseQueryByType();
}
