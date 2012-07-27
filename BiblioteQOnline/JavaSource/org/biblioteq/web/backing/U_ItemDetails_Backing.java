//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.backing
 * File: R_ItemDetails_Backing.java
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
 * Jun 27, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.backing;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.entities.Book;
import org.biblioteq.ejb.entities.CD;
import org.biblioteq.ejb.entities.DVD;
import org.biblioteq.ejb.entities.Journal;
import org.biblioteq.ejb.entities.Magazine;
import org.biblioteq.ejb.entities.VideoGame;
import org.biblioteq.ejb.interfaces.Item;
import org.biblioteq.web.common.Constants;
import org.biblioteq.web.model.ValidationMessage_Model;

/**
 * 
 * @author Clint Bush
 * 
 */
@ManagedBean(name = "U_ItemDetails_Backing")
@RequestScoped
public class U_ItemDetails_Backing extends Screen_Backing
{
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(U_ItemDetails_Backing.class);
	
	/**
	 * Get a copy of the Page_Backing bean. This will be injected via the setPageBacking(Page_Backing pageBacking) method.
	 */
	@ManagedProperty("#{Page_Backing}")
	private Page_Backing pageBacking;
	
	/**
	 * Get a copy of the error model.
	 */
	private ValidationMessage_Model errorMessages = new ValidationMessage_Model();
	
	/**
	 * The item we're displaying information for.
	 */
	private Item selectedItem = null;
	
	/**
	 * Get the Selected Item.
	 * 
	 * @return the selectedItem
	 */
	public Item getSelectedItem()
	{
		return this.selectedItem;
	}
	
	/**
	 * Get the Selected Item as a book.
	 * 
	 * @return (Book) The selected item cast as a book object.
	 */
	public Book getSelectedItemBook()
	{
		if (this.selectedItem instanceof Book)
		{
			return (Book) this.selectedItem;
		}
		
		U_ItemDetails_Backing.log.error("Attempt to cast selected item to Book when it is not a Book.");
		
		return null;
	}
	
	/**
	 * Get the Selected Item as a CD.
	 * 
	 * @return (Book) The selected item cast as a CD object.
	 */
	public CD getSelectedItemCd()
	{
		if (this.selectedItem instanceof CD)
		{
			return (CD) this.selectedItem;
		}
		
		U_ItemDetails_Backing.log.error("Attempt to cast selected item to CD when it is not a CD.");
		
		return null;
	}
	
	/**
	 * Get the Selected Item as a DVD.
	 * 
	 * @return (DVD) The selected item cast as a DVD object.
	 */
	public DVD getSelectedItemDvd()
	{
		if (this.selectedItem instanceof DVD)
		{
			return (DVD) this.selectedItem;
		}
		
		U_ItemDetails_Backing.log.error("Attempt to cast selected item to DVD when it is not a DVD.");
		
		return null;
	}
	
	/**
	 * Get the Selected Item as a Journal.
	 * 
	 * @return (Journal) The selected item cast as a Journal object.
	 */
	public Journal getSelectedItemJournal()
	{
		if (this.selectedItem instanceof Journal)
		{
			return (Journal) this.selectedItem;
		}
		
		U_ItemDetails_Backing.log.error("Attempt to cast selected item to Journal when it is not a Journal.");
		
		return null;
	}
	
	/**
	 * Get the Selected Item as a Magazine.
	 * 
	 * @return (Magazine) The selected item cast as a Magazine object.
	 */
	public Magazine getSelectedItemMagazine()
	{
		if (this.selectedItem instanceof Magazine)
		{
			return (Magazine) this.selectedItem;
		}
		
		U_ItemDetails_Backing.log.error("Attempt to cast selected item to Magazine when it is not a Magazine.");
		
		return null;
	}
	
	/**
	 * Get the Selected Item as a VideoGame.
	 * 
	 * @return (VideoGame) The selected item cast as a VideoGame object.
	 */
	public VideoGame getSelectedItemVideoGame()
	{
		if (this.selectedItem instanceof VideoGame)
		{
			return (VideoGame) this.selectedItem;
		}
		
		U_ItemDetails_Backing.log.error("Attempt to cast selected item to VideoGame when it is not a VideoGame.");
		
		return null;
	}
	
	/**
	 * Returns the path to the type specific details page to be included in the Item Details page.
	 * 
	 * @return (String) Path to specific details page.
	 */
	public String getSpecificDetailsPage()
	{
		if (this.selectedItem != null)
		{
			return "ItemDetails_" + this.selectedItem.getType() + ".xhtml";
		}
		return "";
	}
	
	/**
	 * Return back to the Search Results page.
	 * 
	 * @param e
	 *            (ActionEvent) The ActionEvent object generated by the element that called this method.
	 */
	public void gotoLastPage(ActionEvent e)
	{
		this.pageBacking.setRenderPage((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		        .get(Constants.SESSION_ITEM_DESCRIPTION_BACK));
	}
	
	/**
	 * Perform initialization activities
	 */
	@PostConstruct
	private void init()
	{
		// Get the currently selected Item
		this.selectedItem = (Item) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		        .get(Constants.SESSION_SELECTED_ITEM);
		
		// Verify that an item was actually specified
		if (this.getSelectedItem() == null)
		{
			this.errorMessages.addMessage("No library item was specified for this page.");
			U_ItemDetails_Backing.log.error("No library item was specified for this page.");
		}
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
