//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: MagazineCopy.java
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
 * This entity represents a Magazine Copy, as used by BiblioteQ. It is mapped to the "magazine_copy_info" table. 
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jun 2, 2012, Clinton Bush, 1.0.0,
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
 * This entity represents a Magazine Copy, as used by BiblioteQ. It is mapped to the "magazine_copy_info" table.
 * 
 * @author Clint Bush
 * 
 */
@Entity(name = "MagazineCopy")
@Table(name = "magazine_copy_info")
public class MagazineCopy implements Serializable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(MagazineCopy.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = -2734270644100073332L;
	
	private Magazine thisMagazine;
	private String magazineCopyId;
	private int magazineCopyNumber;
	
	/**
	 * Returns the ID of this Magazine copy. This is the ISSN number concatenated with a dash and then the copy number.
	 * 
	 * @return (String) The magazine copy ID.
	 */
	@Id
	@Column(name = "copyid", length = 64)
	public String getMagazineCopyId()
	{
		return this.magazineCopyId;
	}
	
	/**
	 * Returns the copy number of this magazine copy.
	 * 
	 * @return (int) The magazine copy number.
	 */
	@Column(name = "copy_number")
	public int getMagazineCopyNumber()
	{
		return this.magazineCopyNumber;
	}
	
	/**
	 * Returns the Magazine of which this is a copy.
	 * 
	 * @return (Magazine) The magazine.
	 */
	@ManyToOne
	@JoinColumn(name = "item_oid", unique = false, nullable = false, referencedColumnName = "myoid")
	public Magazine getThisMagazine()
	{
		return this.thisMagazine;
	}
	
	/**
	 * Sets the Magazine copy ID. This should be the ISSN number of the Magazine concatenated with a dash and then the copy number.
	 * 
	 * @param magazineCopyId
	 *            (String) The magazine copy ID to set.
	 */
	public void setMagazineCopyId(String magazineCopyId)
	{
		this.magazineCopyId = magazineCopyId;
	}
	
	/**
	 * Sets the copy number of this Magazine copy. This should be incremented so that it counts each additional copy of a particular Magazine.
	 * 
	 * @param magazineCopyNumber
	 *            (int) The magazine copy number to set.
	 */
	public void setMagazineCopyNumber(int magazineCopyNumber)
	{
		this.magazineCopyNumber = magazineCopyNumber;
	}
	
	/**
	 * Sets the Magazine of which this is a copy.
	 * 
	 * @param thisMagazine
	 *            (Magazine) The this magazine to set.
	 */
	public void setThisMagazine(Magazine thisMagazine)
	{
		this.thisMagazine = thisMagazine;
	}
	
}
