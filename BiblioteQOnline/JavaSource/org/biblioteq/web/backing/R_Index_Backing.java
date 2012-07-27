//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.backing
 * File: R_Index_Backing.java
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
 * This bean provides the backing methods and data fields for the Index Page of the Restricted section.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Apr 7, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.backing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.interfaces.LoanBusinessLocal;
import org.biblioteq.ejb.interfaces.RequestBusinessLocal;
import org.biblioteq.ejb.interfaces.SettingBusinessLocal;
import org.biblioteq.ejb.model.Loan;
import org.biblioteq.ejb.model.Request;
import org.biblioteq.web.common.Constants;
import org.richfaces.component.UIExtendedDataTable;

/**
 * This bean provides the backing methods and data fields for the Index Page of the Restricted section.
 * 
 * @author Clint Bush
 */
@ManagedBean(name = "R_Index_Backing")
@ViewScoped
public class R_Index_Backing extends Screen_Backing
{
	/**
	 * Get the LoanBusiness EJB.
	 */
	@EJB(name = "LoanBusiness")
	private LoanBusinessLocal loanEjb;
	
	/**
	 * Get the RequestBusiness EJB.
	 */
	@EJB(name = "RequestBusiness")
	private RequestBusinessLocal requestEjb;
	
	/**
	 * Get the SettingBusiness EJB.
	 */
	@EJB(name = "SettingBusiness")
	private SettingBusinessLocal settingEjb;
	
	/**
	 * Get a copy of the Page_Backing bean. This will be injected via the setPageBackingBean(Page_Backing pageBackingBean) method.
	 */
	@ManagedProperty("#{Page_Backing}")
	private Page_Backing pageBackingBean;
	
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(R_Index_Backing.class);
	
	/**
	 * Name of active tab of the Summary table.
	 */
	private String activeTabName = "borrowed";
	
	/**
	 * List of loaned items that backs the loaned items table on this page.
	 */
	private List<Loan> loanedItems = null;
	
	/**
	 * List of requested items.
	 */
	private List<Request> requestedItems = null;
	
	/**
	 * Stores the item currently selected by the user.
	 */
	private List<Loan> selectedLoans = new ArrayList<Loan>();
	
	/**
	 * Stores the item currently selected by the user in the requested items table.
	 */
	private List<Request> selectedRequests = new ArrayList<Request>();
	
	/**
	 * Stores the row key items generated when a user makes a selection. This allows us to populate the selectedItems list with the items currently selected.
	 */
	private Collection<Object> borrowedSelection = new ArrayList<Object>();
	
	/**
	 * Stores the row key items generated when a user makes a selection. This allows us to populate the selectedItems list with the items currently selected.
	 */
	private Collection<Object> requestedSelection = new ArrayList<Object>();
	
	/**
	 * Default constructor.
	 */
	public R_Index_Backing()
	{
		
	}
	
	/**
	 * Returns the name of the active tab for the Summary table.
	 * 
	 * @return (String) The activeTabName.
	 */
	public String getActiveTabName()
	{
		return this.activeTabName;
	}
	
	/**
	 * Returns a collection of items that are selected in the "Borrowed Items" table.
	 * 
	 * @return the selection
	 */
	public Collection<Object> getBorrowedSelection()
	{
		return this.borrowedSelection;
	}
	
	/**
	 * Returns a list of the items the user is currently borrowing.
	 * 
	 * @return (List<Loan>) List of items loaned to the user.
	 */
	public List<Loan> getLoanedItems()
	{
		// Ensure loanedItems is populated
		if (this.loanedItems == null)
		{
			// Retrieve any items currently loaned to the user
			this.loanedItems = this.loanEjb.getMemberLoans(this.currentUser.getMember());
		}
		
		return this.loanedItems;
	}
	
	/**
	 * Returns the number of items the user has checked out.
	 * 
	 * @return (int) Number of items out.
	 */
	public int getLoanedItemsCount()
	{
		return this.getLoanedItems().size();
	}
	
	/**
	 * Returns the user's overdue fees as a formatted string.
	 * 
	 * @return (String) The user's overdue fees as a formatted string.
	 */
	public String getOverdueFees()
	{
		String formatPattern = "%.2f";
		
		return String.format(formatPattern, this.getCurrentUser().getMember().getOverdueFees());
	}
	
	/**
	 * Returns the number of Overdue Items the user has checked out.
	 * 
	 * @return (int) Number of overdue items out.
	 */
	public int getOverdueItemsCount()
	{
		// Cycle through the items and count the overdue ones
		int overdueCount = 0;
		for (Loan nextItem : this.getLoanedItems())
		{
			if (nextItem.getDaysRemaining() < 1)
			{
				overdueCount++;
			}
		}
		
		return overdueCount;
	}
	
	/**
	 * Returns a list of the items the user has requested.
	 * 
	 * @return (List<Request>) List of items requested by the user.
	 */
	public List<Request> getRequestedItems()
	{
		// Ensure loanedItems is populated
		if (this.requestedItems == null)
		{
			// Retrieve any items currently loaned to the user
			this.requestedItems = this.requestEjb.getMemberRequests(this.currentUser.getMember());
		}
		
		return this.requestedItems;
	}
	
	/**
	 * Returns the number of items the user has requested.
	 * 
	 * @return (int) Number of items requested.
	 */
	public int getRequestedItemsCount()
	{
		return this.getRequestedItems().size();
	}
	
	/**
	 * Returns a collection of items that are selected in the "Requested Items" table.
	 * 
	 * @return the requestedSelection
	 */
	public Collection<Object> getRequestedSelection()
	{
		return this.requestedSelection;
	}
	
	/**
	 * Returns a boolean value indicating if the "Fees Owed" box is displayed.
	 * 
	 * @return (boolean) True if the "Fees Owed" box is set to be shown; otherwise, false.
	 */
	public boolean isFeesOwedShown()
	{
		return this.settingEjb.getBooleanSettingByName(Constants.SETTING_SHOW_FEES_OWED);
	}
	
	/**
	 * Returns a boolean value indicating if the "Items Out" box is displayed.
	 * 
	 * @return (boolean) True if the "Items Out" box is set to be shown; otherwise, false.
	 */
	public boolean isItemsOutShown()
	{
		return this.settingEjb.getBooleanSettingByName(Constants.SETTING_SHOW_ITEMS_OUT);
	}
	
	/**
	 * Returns a boolean value indicating if the "Items Overdue" box is displayed.
	 * 
	 * @return (boolean) True if the "Items Overdue" box is set to be shown; otherwise, false.
	 */
	public boolean isItemsOverdueShown()
	{
		return this.settingEjb.getBooleanSettingByName(Constants.SETTING_SHOW_ITEMS_OVERDUE);
	}
	
	/**
	 * Returns a boolean value indicating if the "Items Requested" box is displayed.
	 * 
	 * @return (boolean) True if the "Items Requested" box is set to be shown; otherwise, false.
	 */
	public boolean isItemsRequestedShown()
	{
		return this.settingEjb.getBooleanSettingByName(Constants.SETTING_SHOW_ITEMS_REQUESTED);
	}
	
	/**
	 * This is the event listener for row selections from the borrowed items table. It gets the selected row keys and then adds their corresponding items to the selectedItems List.
	 * 
	 * @param event
	 */
	public void selectionListener(AjaxBehaviorEvent event)
	{
		UIExtendedDataTable dataTable = (UIExtendedDataTable) event.getComponent();
		Object originalKey = dataTable.getRowKey();
		
		//@formatter:off
		if (this.activeTabName.equals("borrowed"))
		{
			this.selectedLoans.clear();
			for (Object selectionKey : this.borrowedSelection)
			{
				dataTable.setRowKey(selectionKey);
				
				if (dataTable.isRowAvailable())
				{
					this.selectedLoans.add((Loan) dataTable.getRowData());
					// R_Index_Backing.log.info(((Loan) dataTable.getRowData()).getLoanedItem().getTitle());
				}
			}
			dataTable.setRowKey(originalKey);
		}
		else if (this.activeTabName.equals("requested"))
		{
			this.selectedRequests.clear();
			for (Object selectionKey : this.requestedSelection)
			{
				dataTable.setRowKey(selectionKey);
				
				if (dataTable.isRowAvailable())
				{
					this.selectedRequests.add((Request) dataTable.getRowData());
					// R_Index_Backing.log.info(((Loan) dataTable.getRowData()).getLoanedItem().getTitle());
				}
			}
			dataTable.setRowKey(originalKey);
		}
		//@formatter:on
	}
	
	/**
	 * Sets the name of the active tab of the Summary table.
	 * 
	 * @param activeTabName
	 *            (String) The activeTabName to set.
	 */
	public void setActiveTabName(String activeTabName)
	{
		this.activeTabName = activeTabName;
	}
	
	/**
	 * @param selection
	 *            the selection to set
	 */
	public void setBorrowedSelection(Collection<Object> borrowedSelection)
	{
		this.borrowedSelection = borrowedSelection;
	}
	
	/**
	 * Allow the injection of the Page_Backing bean.
	 * 
	 * @param pageBackingBean
	 *            (Page_Backing) The injected instance of the Page_Backing bean.
	 */
	public void setPageBackingBean(Page_Backing pageBackingBean)
	{
		this.pageBackingBean = pageBackingBean;
	}
	
	/**
	 * @param requestedSelection
	 *            the requestedSelection to set
	 */
	public void setRequestedSelection(Collection<Object> requestedSelection)
	{
		this.requestedSelection = requestedSelection;
	}
	
	/**
	 * Handles the click event for the "View Info." button.
	 * 
	 * @param event
	 *            (AjaxBehaviorEvent) The event object generated by a double-click on the loaned items table.
	 */
	public void viewInfoListener(AjaxBehaviorEvent event)
	{
		//@formatter:off
		if(this.activeTabName.equals("borrowed"))
		{
			if (this.selectedLoans.size() > 0)
			{
				this.pageBackingBean.setInfoItem(this.selectedLoans.get(0).getLoanedItem());
			}
		} 
		else if(this.activeTabName.equals("requested"))
		{
			if (this.selectedRequests.size() > 0)
			{
				this.pageBackingBean.setInfoItem(this.selectedRequests.get(0).getRequestedItem());
			}
		}
		//@formatter:on
	}
}
