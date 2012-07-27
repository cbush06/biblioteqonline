//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: DvdCopy.java
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
 * This entity represents a copy of a DVD, as recorded in the "dvd_copy_info" table.
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
 * This entity represents a copy of a DVD, as recorded in the "dvd_copy_info" table.
 * 
 * @author Clint Bush
 * 
 */
@Entity(name = "DvdCopy")
@Table(name = "dvd_copy_info")
public class DvdCopy implements Serializable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(DvdCopy.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 5709475717158656851L;
	
	private DVD thisDvd;
	private String dvdCopyId;
	private int copyNumber;
	
	/**
	 * Returns the copy number of this copy of the DVD.
	 * 
	 * @return (int) The copy number.
	 */
	@Column(name = "copy_number")
	public int getCopyNumber()
	{
		return this.copyNumber;
	}
	
	/**
	 * Returns the copy ID of this DVD. This is the UPC number concatenated with this copy's copy number, separated by a dash (e.g. 2332342365-1).
	 * 
	 * @return (String) The dvd copy ID.
	 */
	@Id
	@Column(name = "copyid", length = 64)
	public String getDvdCopyId()
	{
		return this.dvdCopyId;
	}
	
	/**
	 * Returns the DVD this is a copy of.
	 * 
	 * @return (DVD) This DVD.
	 */
	@ManyToOne
	@JoinColumn(name = "item_oid", unique = false, nullable = false, referencedColumnName = "myoid")
	public DVD getThisDvd()
	{
		return this.thisDvd;
	}
	
	/**
	 * Sets the copy number of this copy of the DVD.
	 * 
	 * @param copyNumber
	 *            (int) The copy number to set.
	 */
	public void setCopyNumber(int copyNumber)
	{
		this.copyNumber = copyNumber;
	}
	
	/**
	 * Sets the copy ID for this DVD copy (this is the UPC number concatenated with the copy number and separated by a dash).
	 * 
	 * @param dvdCopyId
	 *            (String) The DVD copy ID to set.
	 */
	public void setDvdCopyId(String dvdCopyId)
	{
		this.dvdCopyId = dvdCopyId;
	}
	
	/**
	 * Sets the DVD this is a copy of.
	 * 
	 * @param thisDvd
	 *            (DVD) The DVD to set.
	 */
	public void setThisDvd(DVD thisDvd)
	{
		this.thisDvd = thisDvd;
	}
	
}
