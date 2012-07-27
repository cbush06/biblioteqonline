//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: JournalCopy.java
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
 * This entity represents a Journal Copy, as used by BiblioteQ. It is mapped to the "journal_copy_info" table.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jun 1, 2012, Clinton Bush, 1.0.0,
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
 * This entity represents a Journal Copy, as used by BiblioteQ. It is mapped to the "journal_copy_info" table.
 * 
 * @author Clint Bush
 * 
 */
@Entity(name = "JournalCopy")
@Table(name = "journal_copy_info")
public class JournalCopy implements Serializable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(JournalCopy.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = -5508400387695541293L;
	
	private Journal thisJournal;
	private String journalCopyId;
	private int journalCopyNumber;
	
	/**
	 * Returns the ID of this Journal copy. This is the ISSN number concatenated with a dash and then the copy number of this copy.
	 * 
	 * @return (String) The copy ID of this Journal copy.
	 */
	@Id
	@Column(name = "copyid", length = 64)
	public String getJournalCopyId()
	{
		return this.journalCopyId;
	}
	
	/**
	 * Returns the copy number of this Journal copy. This is an auto-incremented number that counts each copy of a particular Journal.
	 * 
	 * @return (int) The journal copy number.
	 */
	@Column(name = "copy_number")
	public int getJournalCopyNumber()
	{
		return this.journalCopyNumber;
	}
	
	/**
	 * Returns the Journal of which this is a copy.
	 * 
	 * @return (Journal) The Journal of which this is a copy.
	 */
	@ManyToOne
	@JoinColumn(name = "item_oid", unique = false, nullable = false, referencedColumnName = "myoid")
	public Journal getThisJournal()
	{
		return this.thisJournal;
	}
	
	/**
	 * Sets the Journal copy ID. This should be the ISSN number of the Journal concatenated with a dash and this copy's copy number.
	 * 
	 * @param journalCopyId
	 *            (String) The journal copy ID to set.
	 */
	public void setJournalCopyId(String journalCopyId)
	{
		this.journalCopyId = journalCopyId;
	}
	
	/**
	 * Sets the copy number of this Journal copy. This should be incremented so that it counts each additional copy of a particular Journal.
	 * 
	 * @param journalCopyNumber
	 *            (int) The journal copy number to set.
	 */
	public void setJournalCopyNumber(int journalCopyNumber)
	{
		this.journalCopyNumber = journalCopyNumber;
	}
	
	/**
	 * Sets the Journal of which this is a copy.
	 * 
	 * @param thisJournal
	 *            (Journal) The Journal of which this is a copy.
	 */
	public void setThisJournal(Journal thisJournal)
	{
		this.thisJournal = thisJournal;
	}
	
}
