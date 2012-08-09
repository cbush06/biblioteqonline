//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.backing
 * File: A_Navigation_Backing.java
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
 * Provides methods that enable navigation within the Administration section.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * May 09, 2012, Clinton Bush, 1.0.0,
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

import org.apache.log4j.Logger;
import org.biblioteq.web.common.Constants;

/**
 * Provides methods that enable navigation within the Administration section.
 * 
 * @author Clint Bush
 */
@ManagedBean(name = "A_Navigation_Backing")
@SessionScoped
public class A_Navigation_Backing implements Serializable
{
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = -5377919441875292844L;
	
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(A_Navigation_Backing.class);
	
	/**
	 * Get a copy of the Page_Backing bean. This will be injected via the setPageBacking(Page_Backing pageBacking) method.
	 */
	@ManagedProperty("#{Page_Backing}")
	private Page_Backing pageBacking;
	
	/**
	 * Navigates the admin to the Admin Home page.
	 */
	public void gotoAdminHome()
	{
		this.pageBacking.setRenderPage(Constants.PAGE_ADMIN_INDEX);
	}
	
	/**
	 * Navigates the admin to the Manage Users page.
	 */
	public void gotoManageUsers()
	{
		this.pageBacking.setRenderPage(Constants.PAGE_ADMIN_MANAGEUSERS);
	}
	
	/**
	 * Navigates the admin to the Settings page.
	 */
	public void gotoSettings()
	{
		this.pageBacking.setRenderPage(Constants.PAGE_ADMIN_SETTINGS);
	}
	
	/**
	 * Allow the injection of the Page_Backing bean.
	 * 
	 * @param pageBacking
	 *            (Page_Backing) The injected instance of the Page_Backing bean.
	 */
	public void setPageBacking(Page_Backing pageBacking)
	{
		this.pageBacking = pageBacking;
	}
}
