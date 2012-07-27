//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: Setting.java
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
 * This represents a system setting controlled by an administrator.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * May 26, 2012, Clinton Bush, 1.0.0,
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
 * This represents a system setting controlled by an administrator.
 * 
 * @author Clint Bush
 * 
 */
@Entity(name = "Setting")
@Table(name = "online_setting")
public class Setting implements Serializable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(Setting.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = -3412185309415458166L;
	
	private long serialId;
	private String name;
	private String value;
	
	/**
	 * Returns the name of the property.
	 * 
	 * @return (String) The name.
	 */
	@Column(name = "name")
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Returns the serial, generated ID for this record.
	 * 
	 * @return (long) The serialId.
	 */
	@Id
	@Column(name = "id")
	public long getSerialId()
	{
		return this.serialId;
	}
	
	/**
	 * Returns the value of the property.
	 * 
	 * @return (String) The value.
	 */
	@Column(name = "value")
	public String getValue()
	{
		return this.value;
	}
	
	/**
	 * Sets the name of the property.
	 * 
	 * @param name
	 *            (String) The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Sets the serial generated ID of the record. Should never be used.
	 * 
	 * @param serialId
	 *            (long) The Serial ID to set.
	 */
	public void setSerialId(long serialId)
	{
		this.serialId = serialId;
	}
	
	/**
	 * Sets the value of the property.
	 * 
	 * @param value
	 *            (String) The value to set.
	 */
	public void setValue(String value)
	{
		this.value = value;
	}
}
