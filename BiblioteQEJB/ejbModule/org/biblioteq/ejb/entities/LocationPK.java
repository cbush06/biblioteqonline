//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: LocationPK.java
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
 * Provides the data structure for representing the composite primary key for the "locations" table. Used in the "Location" entity.
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

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Provides the data structure for representing the composite primary key for the "locations" table. Used in the "Location" entity.
 * 
 * @author Clint Bush
 * 
 */
@Embeddable
public class LocationPK implements Serializable
{
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = -194723005367207944L;
	
	private String location = "";
	private String type = "";
	
	/**
	 * Default constructor.
	 */
	public LocationPK()
	{
		
	}
	
	/**
	 * Convenience constructor for this class.
	 * 
	 * @param location
	 *            (String) The location in the library.
	 * @param type
	 *            (String) The type of item stored in this location.
	 */
	public LocationPK(String location, String type)
	{
		this.location = location;
		this.type = type;
	}
	
	/**
	 * Returns the location.
	 * 
	 * @return (String) The location.
	 */
	@Column(name = "location")
	public String getLocation()
	{
		return this.location;
	}
	
	/**
	 * Returns the type of item stored in this location.
	 * 
	 * @return (String) The type.
	 */
	@Column(name = "type")
	public String getType()
	{
		return this.type;
	}
	
	/**
	 * Sets the location.
	 * 
	 * @param location
	 *            (String) The location to set.
	 */
	public void setLocation(String location)
	{
		this.location = location;
	}
	
	/**
	 * Sets the item type.
	 * 
	 * @param type
	 *            (String) The type to set.
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	
}
