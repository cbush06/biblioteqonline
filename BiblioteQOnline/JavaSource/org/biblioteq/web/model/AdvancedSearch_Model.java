//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.model
 * File: AdvancedSearch_Model.java
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
 * This class provides a simple data model that can be passed to the Search_Backing bean's Advanced Search method with all the necessary information to execute such a search.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Aug 03, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.model;

import java.util.ArrayList;

/**
 * This class provides a simple data model that can be passed to the Search_Backing bean's Advanced Search method with all the necessary information to execute such a search.
 * 
 * @author Clint Bush
 * 
 */
public class AdvancedSearch_Model
{
	
	/**
	 * The type of Search Term this is.
	 * 
	 * @author Clint Bush
	 * 
	 */
	public enum QueryType {
		BEGIN_WITH, END_WITH, CONTAIN, EQUAL, TERM_QUERY
	}
	
	/**
	 * The requirement level placed on this Search Term.
	 * 
	 * @author Clint Bush
	 * 
	 */
	public enum Requirement {
		MUST, MUST_NOT, SHOULD
	}
	
	/**
	 * List of search terms that comprise this Search. These will be used to build the final search that is to be performed by the Advanced Search feature of the Search_Backing
	 * bean.
	 */
	private ArrayList<SearchTerm> searchTerms = new ArrayList<SearchTerm>();
	
	/**
	 * Add another SearchTerm to this model.
	 * 
	 * @param requirementLevel
	 *            (Requirement) This represents the emphasis that will be placed on the Term to be added. If this is Requirement.MUST, the search will only return a result if that
	 *            result matches this term. Requirement.SHOULD rates matching results higher than non-matching.
	 * @param queryType
	 *            (QueryType) This indicates the kind of comparison that will be performed on possible matches when searching for this term.
	 * @param searchField
	 *            (String) The field to be searched. This should be one of the "SEARCH_DOCUMENT_FIELD_..." options in the biblioteq.web.common.Constants class.
	 * @param query
	 *            (String) The actual term to be queried for.
	 */
	public void addSearchTerm(Requirement requirementLevel, QueryType queryType, String searchField, String query)
	{
		this.searchTerms.add(new SearchTerm(requirementLevel, queryType, searchField, query));
	}
	
	/**
	 * Returns a reference to the ArrayList of search terms in this model.
	 * 
	 * @return (ArrayList<SearchTerms>) List of search terms.
	 */
	public ArrayList<SearchTerm> getSearchTerms()
	{
		return this.searchTerms;
	}
}
