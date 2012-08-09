//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.backing
 * File: R_ItemRequestAgreement_Backing.java
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
 * This page backs the Item Request Agreement page.
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
import org.biblioteq.ejb.interfaces.ItemBusinessLocal;
import org.biblioteq.ejb.interfaces.RequestBusinessLocal;
import org.biblioteq.ejb.interfaces.SettingBusinessLocal;
import org.biblioteq.ejb.model.Request;
import org.biblioteq.web.common.Constants;
import org.biblioteq.web.model.ValidationMessage_Model;

/**
 * This page backs the Item Request Agreement page.
 * 
 * @author Clint Bush
 * 
 */
@ManagedBean(name = "R_ItemRequestAgreement_Backing")
@ViewScoped
public class R_ItemRequestAgreement_Backing implements Serializable
{
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 8606238536530869775L;
	
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(R_ItemRequestAgreement_Backing.class);
	
	/**
	 * Get a copy of the Page_Backing bean. This will be injected via the setPageBacking(Page_Backing pageBacking) method.
	 */
	@ManagedProperty("#{Page_Backing}")
	private Page_Backing pageBacking;
	
	/**
	 * Get a reference to the Item EJB.
	 */
	@EJB(name = "ItemBusiness")
	private ItemBusinessLocal itemEjb;
	
	/**
	 * Get a reference to the Request EJB.
	 */
	@EJB(name = "RequestBusiness")
	private RequestBusinessLocal requestEjb;
	
	/**
	 * Get a reference to the Settings EJB.
	 */
	@EJB(name = "SettingBusiness")
	private SettingBusinessLocal settingEjb;
	
	/**
	 * Get a model to handle our error messages.
	 */
	private ValidationMessage_Model errorMessages = new ValidationMessage_Model();
	
	/**
	 * Stores the value indicating if the user clicked the Agreement checkbox.
	 */
	private boolean agreementCheckboxValue = false;
	
	public R_ItemRequestAgreement_Backing()
	{
		
	}
	
	/**
	 * Returns the agreement message prepared for HTML output.
	 * 
	 * @return (String) The agreement message.
	 */
	public String getAgreementForHtml()
	{
		return this.settingEjb.getStringSettingByName(Constants.SETTING_REQUEST_AGREEMENT_CHECKBOX_MESSAGE).replace("\n", "<br/>");
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
			R_ItemRequestAgreement_Backing.log.error("Attempting to request an Item with a NULL selected item.");
		}
		
		return returnVal;
	}
	
	/**
	 * Returns the agreement terms prepared for HTML output.
	 * 
	 * @return (String) The agreement terms.
	 */
	public String getTermsForHtml()
	{
		return this.settingEjb.getStringSettingByName(Constants.SETTING_REQUEST_AGREEMENT_TERMS).replace("\n", "<br/>");
	}
	
	/**
	 * Return back to the Search Results page.
	 * 
	 * @param (String) JSF Navigation Action.
	 */
	public String gotoLastPage()
	{
		this.pageBacking.setRenderPage((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		        .get(Constants.SESSION_ITEM_REQUEST_BACK));
		
		return "update";
	}
	
	/**
	 * Returns true if the user clicked the Agreement checkbox.
	 * 
	 * @return (boolean) The agreementCheckboxValue.
	 */
	public boolean isAgreementCheckboxValue()
	{
		return this.agreementCheckboxValue;
	}
	
	/**
	 * Sets the value indicating if the user clicked the Agreement checkbox.
	 * 
	 * @param agreementCheckboxValue
	 *            (boolean) The agreementCheckboxValue to set.
	 */
	public void setAgreementCheckboxValue(boolean agreementCheckboxValue)
	{
		this.agreementCheckboxValue = agreementCheckboxValue;
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
	
	/**
	 * Validate the page, create a new request, and send the user to the next page.
	 * 
	 * @return (String) JSF Navigation rule.
	 */
	public String submitRequest()
	{
		// Ensure the user agrees to the terms
		if (!(this.agreementCheckboxValue))
		{
			this.errorMessages.addMessage("You must accept the terms by clicking the \"Agreement\" checkbox.");
			this.errorMessages.renderMessages();
			return "";
		}
		
		// Check the available quantity before creation
		if (this.getSelectedItem().getQuantity() <= this.itemEjb.getNumberCheckedOutOrRequested(this.getSelectedItem()))
		{
			this.errorMessages
			        .addMessage("We're sorry. It appears someone beat you to requesting/reserving this item while you were reading the Agreement. There are no available copies.");
			this.errorMessages.renderMessages();
			return "";
		}
		
		// Complete the process
		Request newRequest = this.requestEjb.createNewRequest(this.pageBacking.getCurrentUser().getMember(), this.getSelectedItem());
		
		// Check the available quantity after creation
		if (this.getSelectedItem().getQuantity() < this.itemEjb.getNumberCheckedOutOrRequested(this.getSelectedItem()))
		{
			this.errorMessages
			        .addMessage("We're sorry. It appears someone beat you to requesting/reserving this item while you were reading the Agreement. There are no available copies.");
			this.requestEjb.removeRequest(newRequest);
			this.errorMessages.renderMessages();
			return "";
		}
		
		// Navigate the user to the confirmation page
		this.pageBacking.setRenderPage(Constants.PAGE_RESTRICTED_ITEMREQUESTCONFIRMATION);
		return "";
	}
}
