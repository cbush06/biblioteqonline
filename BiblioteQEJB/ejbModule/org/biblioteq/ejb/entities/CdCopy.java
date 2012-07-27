//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: CdCopy.java
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
 * This entity represents a copy of a CD, as recorded in the "cd_copy_info" table.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * May 31, 2012, Clinton Bush, 1.0.0,
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
 * This entity represents a copy of a CD, as recorded in the "cd_copy_info" table.
 * 
 * @author Clint Bush
 * 
 */
@Entity(name = "CdCopy")
@Table(name = "cd_copy_info")
public class CdCopy implements Serializable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(CdCopy.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 242476031743808967L;
	
	private CD thisCd;
	private String cdCopyId;
	private int cdCopyNumber;
	
	/**
	 * Returns the ID of this CD copy. This is the UPC number concatenated with a dash and then the copy number of this copy.
	 * 
	 * @return (String) The copy ID for this CD copy.
	 */
	@Id
	@Column(name = "copyid", length = 64)
	public String getCdCopyId()
	{
		return this.cdCopyId;
	}
	
	/**
	 * Returns the copy number of this CD copy. This is an auto-incremented number that counts each copy of a particular CD.
	 * 
	 * @return (int) The CD copy number of this copy.
	 */
	@Column(name = "copy_number")
	public int getCdCopyNumber()
	{
		return this.cdCopyNumber;
	}
	
	/**
	 * Returns the CD of which this is a copy.
	 * 
	 * @return (CD) The CD of which this is a copy.
	 */
	@ManyToOne
	@JoinColumn(name = "item_oid", unique = false, nullable = false, referencedColumnName = "myoid")
	public CD getThisCd()
	{
		return this.thisCd;
	}
	
	/**
	 * Sets the CD copy ID of this CD. This should be the UPC number of the CD concatenated with this copy's copy number, separated by a dash.
	 * 
	 * @param cdCopyId
	 *            (String) The CD copy ID.
	 */
	public void setCdCopyId(String cdCopyId)
	{
		this.cdCopyId = cdCopyId;
	}
	
	/**
	 * Sets the copy number of this CD copy. This should be incremented so that it counts each additional copy of a particular CD.
	 * 
	 * @param cdCopyNumber
	 *            (int) The CD copy number of this copy.
	 */
	public void setCdCopyNumber(int cdCopyNumber)
	{
		this.cdCopyNumber = cdCopyNumber;
	}
	
	/**
	 * Sets the CD of which this is a copy.
	 * 
	 * @param thisCd
	 *            (CD) The CD of which this is a copy.
	 */
	public void setThisCd(CD thisCd)
	{
		this.thisCd = thisCd;
	}
	
}
