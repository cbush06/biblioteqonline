//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: SubjectIndexItem.java
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
 * Provides a structure for representing an indexed Subject Item. This structure stores the subject entry and the number of its occurrences in the database.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Aug 02, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.ejb.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * Provides a structure for representing an indexed Subject Item. This structure stores the subject entry and the number of its occurrences in the database.
 * 
 * @author Clint Bush
 * 
 */
@Entity(name = "SubjectIndexItem")
@Table(name = "subject_index")
public class SubjectIndexItem implements Serializable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(SubjectIndexItem.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = -5968700950674693200L;
	
	private String term = "";
	private long total = 0;
	
	/**
	 * Default constructor.
	 */
	public SubjectIndexItem()
	{
		
	}
	
	/**
	 * Constructor creating a new SubjectIndexItem with preset values.
	 * 
	 * @param term
	 *            (String) The new term.
	 * @param total
	 *            (long) The total count of occurrences of this term (likely 1).
	 */
	public SubjectIndexItem(String term, long total)
	{
		this.term = term;
		this.total = total;
	}
	
	/**
	 * The indexed term (a subject).
	 * 
	 * @return the term
	 */
	@Id
	@Column(name = "term")
	public String getTerm()
	{
		return this.term;
	}
	
	/**
	 * The total occurrences of the term (the number of items that have this term).
	 * 
	 * @return the total
	 */
	@Column(name = "total")
	public long getTotal()
	{
		return this.total;
	}
	
	/**
	 * Sets the term.
	 * 
	 * @param term
	 *            (String) The term to set.
	 */
	public void setTerm(String term)
	{
		this.term = term;
	}
	
	/**
	 * Sets the total occurrences of the term.
	 * 
	 * @param total
	 *            (long) The total to set.
	 */
	public void setTotal(long total)
	{
		this.total = total;
	}
}
