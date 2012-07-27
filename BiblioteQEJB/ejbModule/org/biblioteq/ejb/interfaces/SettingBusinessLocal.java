//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.interfaces
 * File: SettingBusinessLocal.java
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
 * Local interface for the SettingBusiness EJB.
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
package org.biblioteq.ejb.interfaces;

import javax.ejb.Local;

import org.biblioteq.ejb.entities.Setting;

/**
 * Local interface for the SettingBusiness EJB.
 * 
 * @author Clint Bush
 * 
 */
@Local
public interface SettingBusinessLocal
{
	/**
	 * Retrieves and interprets a boolean Setting. Returns its boolean value (true/false).
	 * 
	 * @param settingName
	 *            (String) Name of the Setting to be interpreted.
	 * @return (boolean) The value of the Setting as a Java boolean instead of its actual String value. If the setting is not found or cannot be interpreted in a boolean manner,
	 *         this returns false.
	 */
	public abstract boolean getBooleanSettingByName(String settingName);
	
	/**
	 * Retrieves and interprets an integer Setting. Returns its integer value. Returns -1 if it cannot be interpreted as an integer.
	 * 
	 * @param settingName
	 *            (String) Name of the Setting to be interpreted.
	 * @return (int) The value of the Setting as an integer. If the setting is not found or cannot be interpreted as an integer, returns -1.
	 */
	public abstract int getIntegerSettingByName(String settingName);
	
	/**
	 * Returns the system setting matching the provided name.
	 * 
	 * @param settingName
	 *            (String) Name of the Setting whose record is to be returned.
	 * @return (Setting) The Setting matching the provided name, or null if no setting is found.
	 */
	public abstract Setting getSettingByName(String settingName);
	
	/**
	 * Retrieves a Setting and returns its value as a String object.
	 * 
	 * @param settingName
	 *            (String) Name of the Setting to be interpreted.
	 * @return (String) The value of the Setting as a String object.
	 */
	public abstract String getStringSettingByName(String settingName);
	
	/**
	 * Saves/Creates a setting with the name specified and assigns it the value specified.
	 * 
	 * @param setting
	 *            (Setting) The new Setting object to create or to replace the existing Setting of the same name with.
	 * @return (Setting) The updated Setting object.
	 */
	public abstract Setting saveSetting(Setting setting);
	
	/**
	 * Saves/Creates a setting with the name specified and assigns it the value specified.
	 * 
	 * @param settingName
	 *            (String) Name of the setting to create or update.
	 * @param settingValue
	 *            (String) Value to assign the setting.
	 * @return (Setting) The updated Setting object.
	 */
	public abstract Setting saveSetting(String settingName, String settingValue);
}
