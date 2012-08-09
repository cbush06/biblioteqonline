//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.model
 * File: BrowseItem.java
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
 * This is a data structure used for return results of queries executed by IndexBusiness for browsing.
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
package org.biblioteq.ejb.model;

import java.io.Serializable;

/**
 * This is a data structure used for return results of queries executed by IndexBusiness for browsing.
 * 
 * @author Clint Bush
 * 
 */
public class BrowseItem implements Serializable
{
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = -357538016516221681L;
	
	private String type = "";
	
	private String term = "";
	
	private long total = 0;
	
	public BrowseItem(String term, long total)
	{
		this.term = term;
		this.total = total;
	}
	
	public BrowseItem(String term, long total, String type)
	{
		this.term = term;
		this.total = total;
		this.type = type;
	}
	
	/**
	 * Returns the term that was indexed for browsing.
	 * 
	 * @return (String) The term.
	 */
	public String getTerm()
	{
		return this.term;
	}
	
	/**
	 * Returns the total items that exist under the indexed term.
	 * 
	 * @return (long) The total.
	 */
	public long getTotal()
	{
		return this.total;
	}
	
	/**
	 * Returns the type this item applies to (DVD, Book, etc.) if applicable.
	 * 
	 * @return (String) The type.
	 */
	public String getType()
	{
		return this.type;
	}
	
	/**
	 * Sets the term that was indexed for browsing.
	 * 
	 * @param term
	 *            (String) The term to set.
	 */
	public void setTerm(String term)
	{
		this.term = term;
	}
	
	/**
	 * Sets the total items that exist under the indexed term.
	 * 
	 * @param total
	 *            (long) The total to set.
	 */
	public void setTotal(long total)
	{
		this.total = total;
	}
	
	/**
	 * Sets the type this item applies to.
	 * 
	 * @param type
	 *            (String) The type to set.
	 */
	public void setType(String type)
	{
		this.type = type;
	}
}
