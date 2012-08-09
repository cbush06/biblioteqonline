//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.model
 * File: SearchTerm.java
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
 * Provides a model for SearchTerms that comprise the AdvancedSearch_Model used by the Search_Backing bean.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Aug 06, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.model;

import org.biblioteq.web.model.AdvancedSearch_Model.QueryType;
import org.biblioteq.web.model.AdvancedSearch_Model.Requirement;

/**
 * Provides a model for SearchTerms that comprise the AdvancedSearch_Model used by the Search_Backing bean.
 * 
 * @author Clint Bush
 * 
 */
public class SearchTerm
{
	private Requirement requirement;
	private QueryType queryType;
	private String searchField = "";
	
	private String query = "";
	
	public SearchTerm(Requirement requirement, QueryType queryType, String searchField, String query)
	{
		this.requirement = requirement;
		this.queryType = queryType;
		this.searchField = searchField;
		this.query = query;
	}
	
	/**
	 * @return the query
	 */
	public String getQuery()
	{
		return this.query;
	}
	
	/**
	 * @return the queryType
	 */
	public QueryType getQueryType()
	{
		return this.queryType;
	}
	
	/**
	 * @return the requirement
	 */
	public Requirement getRequirement()
	{
		return this.requirement;
	}
	
	/**
	 * @return the searchField
	 */
	public String getSearchField()
	{
		return this.searchField;
	}
	
	/**
	 * @param query
	 *            the query to set
	 */
	public void setQuery(String query)
	{
		this.query = query;
	}
	
	/**
	 * @param queryType
	 *            the queryType to set
	 */
	public void setQueryType(QueryType queryType)
	{
		this.queryType = queryType;
	}
	
	/**
	 * @param requirement
	 *            the requirement to set
	 */
	public void setRequirement(Requirement requirement)
	{
		this.requirement = requirement;
	}
	
	/**
	 * @param searchField
	 *            the searchField to set
	 */
	public void setSearchField(String searchField)
	{
		this.searchField = searchField;
	}
	
}
