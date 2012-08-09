//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.backing
 * File: A_Settings_Backing.java
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
 * Provides methods that back all settings functions.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jun 02, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * Aug 08, 2012, Clinton Bush, 1.1.2,
 *    Implemented the Serializable interface.
 *    
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.backing;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.biblioteq.web.common.Constants;

/**
 * Provides methods that back all settings functions.
 * 
 * @author Clint Bush
 * 
 */
@ManagedBean(name = "A_Settings_Backing")
@SessionScoped
public class A_Settings_Backing implements Serializable
{
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 2814870873193802703L;
	
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(A_Settings_Backing.class);
	
	/**
	 * Get a copy of the Page_Backing bean. This will be injected via the setPageBacking(Page_Backing pageBacking) method.
	 */
	@ManagedProperty("#{Page_Backing}")
	private Page_Backing pageBacking;
	
	/**
	 * Navigates the user to the Search Settings page.
	 */
	public void gotoSearchSettings(ActionEvent e)
	{
		this.pageBacking.setRenderPage(Constants.PAGE_ADMIN_SETTINGS_SEARCH);
	}
	
	/**
	 * Navigates the user to the Summary Page Settings page.
	 */
	public void gotoSummaryPageSettings(ActionEvent e)
	{
		this.pageBacking.setRenderPage(Constants.PAGE_ADMIN_SETTINGS_SUMMARYPAGE);
	}
	
	/**
	 * Navigates the user to the System Settings page.
	 */
	public void gotoSystemSettings(ActionEvent e)
	{
		this.pageBacking.setRenderPage(Constants.PAGE_ADMIN_SETTINGS);
	}
	
	/**
	 * @param pageBacking
	 *            the pageBacking to set
	 */
	public void setPageBacking(Page_Backing pageBacking)
	{
		this.pageBacking = pageBacking;
	}
}
