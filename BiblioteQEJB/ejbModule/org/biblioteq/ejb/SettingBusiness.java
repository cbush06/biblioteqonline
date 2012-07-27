//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb
 * File: SettingBusiness.java
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
 * The SettingBusiness EJB handles all transactions regarding system settings controlled by administrators.
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
package org.biblioteq.ejb;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.entities.Setting;
import org.biblioteq.ejb.interfaces.SettingBusinessLocal;
import org.biblioteq.ejb.interfaces.SettingBusinessRemote;

/**
 * The SettingBusiness EJB handles all transactions regarding system settings controlled by administrators.
 * 
 * @author Clint Bush
 * 
 */
@Stateless(name = "SettingBusiness")
public class SettingBusiness implements SettingBusinessLocal, SettingBusinessRemote, Serializable
{
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 1217896438761278138L;
	
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(Setting.class);
	
	// Get the entity manager
	@PersistenceContext(unitName = "BiblioteQPersistenceUnit")
	private EntityManager em;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.SettingBusinessLocal#getBooleanSettingByName(java.lang.String)
	 */
	@Override
	public boolean getBooleanSettingByName(String settingName)
	{
		// Attempt to get the Setting
		Setting theSetting = this.getSettingByName(settingName);
		
		// If the setting could not be found, log the error and return false
		if (theSetting == null)
		{
			SettingBusiness.log.error("Null Setting returned while trying to get a boolean setting by name: " + settingName);
			return false;
		}
		
		// If the setting was found and is true
		if (theSetting.getValue().toLowerCase().equals("true"))
		{
			return true;
		}
		
		// If the setting was false or could not be converted to a boolean value, return false
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.SettingBusinessLocal#getIntegerSettingByName(java.lang.String)
	 */
	@Override
	public int getIntegerSettingByName(String settingName)
	{
		// Declare the variables we'll be using
		int returnVal = -1;
		
		// Attempt to get the Setting
		Setting theSetting = this.getSettingByName(settingName);
		
		// If the setting could not be found, log the error and return -1
		if (theSetting == null)
		{
			SettingBusiness.log.error("Null Setting returned while trying to get an integer setting by name: " + settingName);
			return -1;
		}
		
		// If the setting was found, try to parse it as an integer and return its value
		try
		{
			returnVal = Integer.parseInt(theSetting.getValue());
		}
		catch (NumberFormatException e)
		{
			// The setting value could not be parsed to an Integer, so log the error and return -1
			SettingBusiness.log.error("Exception thrown while trying to parse a Setting value as an integer. Setting name: " + settingName);
			e.printStackTrace();
			return -1;
		}
		
		// Assuming everything went as planned, return the value of the setting as an int
		return returnVal;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.SettingBusinessLocal#getSettingByName(java.lang.String)
	 */
	@Override
	public Setting getSettingByName(String settingName)
	{
		Setting returnVal = null;
		
		//@formatter:off
		TypedQuery<Setting> q = this.em.createQuery(
				"SELECT s " +
				"FROM Setting s " +
				"WHERE " +
					"s.name = :name",
				Setting.class
		);
		//@formatter:on
		
		// Set the parameters
		q.setParameter("name", settingName);
		
		// Try to get the one result
		try
		{
			returnVal = q.getSingleResult();
		}
		catch (NoResultException e)
		{
			// No setting found
			SettingBusiness.log.error("No setting found by name: " + settingName);
			return null;
		}
		catch (NonUniqueResultException e)
		{
			// Multiple record found
			SettingBusiness.log.error("Multiple records found by name: " + settingName);
			return null;
		}
		
		return returnVal;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.SettingBusinessLocal#getStringSettingByName(java.lang.String)
	 */
	@Override
	public String getStringSettingByName(String settingName)
	{
		// Declare the variables we'll be using
		String returnVal = "";
		
		// Attempt to get the Setting
		Setting theSetting = this.getSettingByName(settingName);
		
		// If the setting could not be found, log the error and return -1
		if (theSetting == null)
		{
			SettingBusiness.log.error("Null Setting returned while trying to get an integer setting by name: " + settingName);
			return "";
		}
		
		// If the setting was found, try to parse it as an integer and return its value
		returnVal = theSetting.getValue();
		
		// Assuming everything went as planned, return the value of the setting as an int
		return returnVal;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.SettingBusinessLocal#saveSetting(org.biblioteq.ejb.entities.Setting)
	 */
	@Override
	public Setting saveSetting(Setting setting)
	{
		return this.saveSetting(setting.getName(), setting.getValue());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.SettingBusinessLocal#saveSetting(java.lang.String, java.lang.String)
	 */
	@Override
	public Setting saveSetting(String settingName, String settingValue)
	{
		Setting returnVal = null;
		
		//@formatter:off
	    TypedQuery<Setting> q = this.em.createQuery(
	    		"SELECT s " +
	    		"FROM Setting s " +
	    		"WHERE " +
	    			"s.name = :settingName",
	    		Setting.class
		);
	    //@formatter:on
		
		// Set the parameters
		q.setParameter("settingName", settingName);
		
		// Try to get the one result
		try
		{
			returnVal = q.getSingleResult();
		}
		catch (NoResultException e)
		{
			returnVal = null;
		}
		catch (NonUniqueResultException e)
		{
			// Multiple record found
			SettingBusiness.log.error("Multiple records found by name: " + settingName);
			return null;
		}
		
		// If it isn't null, update the value of the existing. If it is, create a new one.
		if (returnVal != null)
		{
			returnVal.setValue(settingValue);
		}
		else
		{
			returnVal = new Setting();
			returnVal.setName(settingName);
			returnVal.setValue(settingValue);
		}
		
		// Persist this new/updated setting and return it
		return this.em.merge(returnVal);
	}
}
