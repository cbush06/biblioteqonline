//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: OnlineSetting.java
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * @author Clint Bush
 * 
 */
@Entity(name = "OnlineSetting")
@Table(name = "online_setting")
public class OnlineSetting implements Serializable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(OnlineSetting.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 2198190844917982866L;
	
	private int id = -1;
	private String name = "";
	private String value = "";
	
	/**
	 * Gets the serial id.
	 * 
	 * @return (int) The id.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SettingIdGen")
	@SequenceGenerator(initialValue = 1, name = "SettingIdGen", sequenceName = "online_setting_id_seq")
	@Column(name = "id")
	public int getId()
	{
		return this.id;
	}
	
	/**
	 * Get the name of the setting.
	 * 
	 * @return (String) Name of setting.
	 */
	@Column(name = "name", length = 128)
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Get the value of the setting.
	 * 
	 * @return (String) Value of setting.
	 */
	@Column(name = "value")
	public String getValue()
	{
		return this.value;
	}
	
	/**
	 * Sets the serial id.
	 * 
	 * @param id
	 *            (int) The id to set.
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * Set the setting name.
	 * 
	 * @param name
	 *            (String) Name of setting.
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Set the setting value.
	 * 
	 * @param value
	 *            (String) Value of setting.
	 */
	public void setValue(String value)
	{
		this.value = value;
	}
}
