//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.backing
 * File: R_ItemRequestConfirmation_Backing.java
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
 * This page backs the Item Request Confirmation page.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jun 30, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * Aug 08, 2012, Clinton Bush, 1.1.2,
 *    Implemented Serializable.
 *    
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.backing;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.interfaces.Item;
import org.biblioteq.ejb.interfaces.SettingBusinessLocal;
import org.biblioteq.web.common.Constants;

/**
 * This page backs the Item Request Confirmation page.
 * 
 * @author Clint Bush
 * 
 */
@ManagedBean(name = "R_ItemRequestConfirmation_Backing")
@ViewScoped
public class R_ItemRequestConfirmation_Backing implements Serializable
{
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 4878900129434064926L;
	
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(R_ItemRequestConfirmation_Backing.class);
	
	/**
	 * Get a copy of the Page_Backing bean. This will be injected via the setPageBacking(Page_Backing pageBacking) method.
	 */
	@ManagedProperty("#{Page_Backing}")
	private Page_Backing pageBacking;
	
	/**
	 * Get a reference to the Settings EJB.
	 */
	@EJB(name = "SettingBusiness")
	private SettingBusinessLocal settingEjb;
	
	public R_ItemRequestConfirmation_Backing()
	{
		
	}
	
	/**
	 * Returns the confirmation message prepared for HTML output.
	 * 
	 * @return (String) The confirmation message.
	 */
	public String getConfirmationForHtml()
	{
		return this.settingEjb.getStringSettingByName(Constants.SETTING_REQUEST_CONFIRMATION_MESSAGE).replace("\n", "<br/>");
	}
	
	/**
	 * Returns the current Selected Item stored in the Session Map.
	 * 
	 * @return (Item) The selected item.
	 */
	public Item getSelectedItem()
	{
		Item returnVal = (Item) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Constants.SESSION_SELECTED_ITEM);
		
		if (returnVal == null)
		{
			R_ItemRequestConfirmation_Backing.log.error("Attempting to request an Item with a NULL selected item.");
		}
		
		return returnVal;
	}
	
	/**
	 * Return back to the Search Results page.
	 * 
	 * @return (String) JSF Navigation Action.
	 */
	public String gotoLastPage()
	{
		this.pageBacking.setRenderPage((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		        .get(Constants.SESSION_ITEM_REQUEST_BACK));
		
		return "update";
	}
	
	/**
	 * Returns the user to the home/summary page.
	 * 
	 * @return (String) JSF Navigation Action.
	 */
	public String gotoSummaryPage()
	{
		this.pageBacking.setRenderPage(Constants.PAGE_RESTRICTED_INDEX);
		return "update";
	}
	
	/**
	 * Allows the injection of the Page_Backing bean.
	 * 
	 * @param pageBacking
	 *            the pageBacking to set
	 */
	public void setPageBacking(Page_Backing pageBacking)
	{
		this.pageBacking = pageBacking;
	}
}
