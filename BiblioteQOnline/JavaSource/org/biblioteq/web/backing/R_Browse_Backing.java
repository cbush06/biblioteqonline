//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.backing
 * File: R_Browse_Backing.java
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
 * Backs the "Browse" page by providing all functionality required by the page.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jul 28, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.backing;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;
import org.biblioteq.web.model.ValidationMessage_Model;

/**
 * Backs the "Browse" page by providing all functionality required by the page.
 * 
 * @author Clint Bush
 * 
 */
@ManagedBean(name = "R_Browse_Backing")
@SessionScoped
public class R_Browse_Backing
{
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(R_Browse_Backing.class);
	
	/**
	 * Prepare a validation model for displaying Error Messages.
	 */
	private ValidationMessage_Model errorMessages = new ValidationMessage_Model();
	
	/**
	 * Store the type of listing we are currently showing the user (Subject, Type, Creator, Location).
	 */
	private String typeFilter = "Subject";
	
	/**
	 * Get a copy of the Page_Backing bean. This will be injected via the setPageBackingBean(Page_Backing pageBackingBean) method.
	 */
	@ManagedProperty("#{Page_Backing}")
	private Page_Backing pageBackingBean;
	
	/**
	 * Returns the current value of the type filter.
	 * 
	 * @return (String) The typeFilter.
	 */
	public String getTypeFilter()
	{
		return this.typeFilter;
	}
	
	/**
	 * Used to inject a reference to the Page_Backing bean into this class.
	 * 
	 * @param pageBackingBean
	 *            the pageBackingBean to set
	 */
	public void setPageBackingBean(Page_Backing pageBackingBean)
	{
		this.pageBackingBean = pageBackingBean;
	}
	
	/**
	 * Sets the value of the type filter.
	 * 
	 * @param typeFilter
	 *            (String) The typeFilter to set.
	 */
	public void setTypeFilter(String typeFilter)
	{
		this.typeFilter = typeFilter;
	}
}
