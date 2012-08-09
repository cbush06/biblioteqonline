//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: Location.java
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
 * Represents a location in the library.
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
package org.biblioteq.ejb.entities;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * Represents a location in the library.
 * 
 * @author Clint Bush
 * 
 */
@Entity(name = "Location")
@Table(name = "locations")
public class Location implements Serializable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(Location.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = -1380395345696037719L;
	
	@EmbeddedId
	private LocationPK locationPk;
	
	/**
	 * Returns a reference to the Composite Primary Key.
	 * 
	 * @return the locationPk
	 */
	public LocationPK getLocationPk()
	{
		return this.locationPk;
	}
	
	/**
	 * Sets the Composite Primary Key.
	 * 
	 * @param locationPk
	 *            the locationPk to set
	 */
	public void setLocationPk(LocationPK locationPk)
	{
		this.locationPk = locationPk;
	}
	
}
