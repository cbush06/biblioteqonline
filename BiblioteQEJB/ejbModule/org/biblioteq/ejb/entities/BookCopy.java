//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: BookCopy.java
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
 *
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * May 5, 2012, Clinton Bush, 1.0.0,
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * @author Clint Bush
 * 
 */
@Entity(name = "BookCopy")
@Table(name = "book_copy_info")
public class BookCopy implements Serializable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(BookCopy.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 7410808057628187581L;
	
	private Book book;
	private long serialId;
	private String copyId;
	private int copyNumber;
	
	/**
	 * Returns the Book this is a copy of.
	 * 
	 * @return (Book) The book this is a copy of.
	 */
	@ManyToOne
	@JoinColumn(name = "item_oid", nullable = false, referencedColumnName = "myoid")
	public Book getBook()
	{
		return this.book;
	}
	
	/**
	 * Returns the ID of this copy. This is a combination of the book's ISBN-10 number and the copy number (e.g. 059035342X-1, 059035342X-2, etc.)
	 * 
	 * @return (String) The copy ID.
	 */
	@Id
	@Column(name = "copyid", length = 64)
	public String getCopyId()
	{
		return this.copyId;
	}
	
	/**
	 * Returns the number of this copy for its particular book.
	 * 
	 * @return (int) The copy number.
	 */
	@Column(name = "copy_number")
	public int getCopyNumber()
	{
		return this.copyNumber;
	}
	
	/**
	 * Returns the auto-incremented serial ID for this record.
	 * 
	 * @return (long) The serial ID.
	 */
	@Column(name = "myoid")
	public long getSerialId()
	{
		return this.serialId;
	}
	
	/**
	 * Sets the Book of which this is a copy of. This method is private and should never be used.
	 * 
	 * @param book
	 *            (Book) The book.
	 */
	private void setBook(Book book)
	{
		this.book = book;
	}
	
	/**
	 * Sets the copy ID for this copy. This method is private and should never be used.
	 * 
	 * @param copyId
	 *            (String) The copy ID.
	 */
	private void setCopyId(String copyId)
	{
		this.copyId = copyId;
	}
	
	/**
	 * Sets the copy number of this copy. This method is private and should never be used.
	 * 
	 * @param copyNumber
	 *            (int) The copy number.
	 */
	private void setCopyNumber(int copyNumber)
	{
		this.copyNumber = copyNumber;
	}
	
	/**
	 * Sets the auto-incremented serial ID for this copy's record. This method is private and should never be used.
	 * 
	 * @param serialId
	 *            (long) The serial ID.
	 */
	private void setSerialId(long serialId)
	{
		this.serialId = serialId;
	}
	
}
