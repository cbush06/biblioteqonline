//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.backing
 * File: A_Index_Backing.java
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
 * This bean provides the backing methods and data fields for the Index Page of the Admin section.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * May 9, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.backing;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.interfaces.IndexBusinessLocal;
import org.biblioteq.ejb.interfaces.LoanBusinessLocal;
import org.biblioteq.ejb.interfaces.MemberBusinessLocal;
import org.biblioteq.ejb.interfaces.RequestBusinessLocal;
import org.biblioteq.ejb.interfaces.SettingBusinessLocal;
import org.biblioteq.ejb.interfaces.UserBusinessLocal;
import org.biblioteq.web.common.Constants;

/**
 * This bean provides the backing methods and data fields for the Index Page of the Admin section.
 * 
 * @author Clint Bush
 */
@ManagedBean(name = "A_Index_Backing")
@ViewScoped
public class A_Index_Backing implements Serializable
{
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(A_Index_Backing.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 6941057373107151394L;
	
	/**
	 * Get a copy of the Page_Backing bean. This will be injected via the setPageBacking(Page_Backing pageBacking) method.
	 */
	@ManagedProperty("#{Page_Backing}")
	private Page_Backing pageBacking;
	
	/**
	 * Get a reference to the Loan EJB.
	 */
	@EJB(name = "LoanBusiness")
	private LoanBusinessLocal loanEjb;
	
	/**
	 * Get a reference to the Member EJB.
	 */
	@EJB(name = "MemberBusiness")
	private MemberBusinessLocal memberEjb;
	
	/**
	 * Get a reference to the Request EJB.
	 */
	@EJB(name = "RequestBusiness")
	private RequestBusinessLocal requestEjb;
	
	/**
	 * Get a reference to the Setting EJB.
	 */
	@EJB(name = "SettingBusiness")
	private SettingBusinessLocal settingEjb;
	
	/**
	 * Get a reference to the User EJB.
	 */
	@EJB(name = "UserBusiness")
	private UserBusinessLocal userEjb;
	
	/**
	 * IndexBusiness EJB.
	 */
	private IndexBusinessLocal indexEjb;
	
	/**
	 * Store the total indexed records.
	 */
	private long recordsTotal = 0L;
	
	/**
	 * Default constructor.
	 */
	public A_Index_Backing()
	{
		
	}
	
	/**
	 * Returns the number of whole days since the last indexing was performed.
	 * 
	 * @return (String) Number of whole days since the last indexing was performed.
	 */
	public String getDaysSinceLastIndexing()
	{
		// Declare the variables we'll be using
		long lastIndexingMilli = Long.valueOf(this.settingEjb.getStringSettingByName(Constants.SETTING_SEARCH_LAST_INDEXING));
		
		// If an index has never been performed, return "N/A"
		if (lastIndexingMilli <= 0)
		{
			return "N/A";
		}
		
		// Return the whole number of days
		lastIndexingMilli = ((new Date()).getTime() - lastIndexingMilli);
		return String.valueOf((int) (lastIndexingMilli / 1000L / 60L / 60L / 24L));
	}
	
	/**
	 * Returns the date the Search Engine last indexed the database.
	 * 
	 * @return (String) The date the database was last indexed.
	 */
	public String getLastIndexing()
	{
		// Declare the variables we'll be using
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy 'at' hh:mm aaa");
		
		// Set the time/date of the calendar
		cal.setTimeInMillis(Long.valueOf(this.settingEjb.getStringSettingByName(Constants.SETTING_SEARCH_LAST_INDEXING)));
		
		// Return the formatted output
		return sdf.format(cal.getTime());
	}
	
	/**
	 * Returns the date the Search Engine will next automatically index the database.
	 * 
	 * @return (String) The date the database will next be indexed.
	 */
	public String getNextIndexing()
	{
		// Declare the variables we'll be using
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy 'at' hh:mm aaa");
		String frequency = this.settingEjb.getStringSettingByName(Constants.SETTING_SEARCH_AUTO_INDEXING_FREQ);
		
		// Get milliseconds for # hours (i.e. 43200000 = 1000 * 60 * 60 * 12hrs)
		long autoFreqInMilli = 3600000L;
		
		//@formatter:off
		if(frequency.equals("daily"))
		{
			autoFreqInMilli *= 24; // Every 1 day (1 * 24 = 24)
		}
		else if(frequency.equals("bi-daily"))
		{
			autoFreqInMilli *= 12; // Every .5 days (.5 * 24 = 12) 
		}
		else if(frequency.equals("weekly"))
		{
			autoFreqInMilli *= 168; // Every 7 days (7 * 24 = 168)
		}
		else if(frequency.equals("bi-weekly"))
		{
			autoFreqInMilli *= 84; // Every 3.5 days (3.5 * 24 = 84)
		}
		else if(frequency.equals("monthly"))
		{
			autoFreqInMilli *= 744; // Every 31 days (31 * 24 = 744)
		}
		else
		{
			autoFreqInMilli *= 372; // Every 15.5 days (15.5 * 24 = 372)
		}
		//@formatter:on
		
		// If auto indexing is turned off
		if (!(this.settingEjb.getBooleanSettingByName(Constants.SETTING_SEARCH_AUTO_INDEXING)))
		{
			return "N/A";
		}
		
		// Set the time/date of the calendar
		cal.setTimeInMillis(Long.valueOf(this.settingEjb.getStringSettingByName(Constants.SETTING_SEARCH_LAST_INDEXING)) + autoFreqInMilli);
		
		// Return the formatted output
		return sdf.format(cal.getTime());
	}
	
	/**
	 * Returns the total number of items borrowed.
	 * 
	 * @return (String) Total count of borrowed items.
	 */
	public String getTotalItemsBorrowed()
	{
		return String.valueOf(this.loanEjb.getTotalItemsBorrowed());
	}
	
	/**
	 * Returns the Total Items Indexed.
	 * 
	 * @return (String) Total Items Indexed.
	 */
	public String getTotalItemsIndexed()
	{
		return String.valueOf(this.recordsTotal);
	}
	
	/**
	 * Returns the total number of overdue items.
	 * 
	 * @return (String) Total count of overdue items.
	 */
	public String getTotalItemsOverdue()
	{
		return String.valueOf(this.loanEjb.getTotalItemsOverdue());
	}
	
	/**
	 * Returns the total number of library members.
	 * 
	 * @return (String) Total library members.
	 */
	public String getTotalMembers()
	{
		return String.valueOf(this.memberEjb.getTotalMembers());
	}
	
	/**
	 * Returns the total number of requests.
	 * 
	 * @return (String) Total requests currently in the system.
	 */
	public String getTotalRequests()
	{
		return String.valueOf(this.requestEjb.getTotalRequests());
	}
	
	/**
	 * Returns the total number of online users.
	 * 
	 * @return (String) Total library members who are also online users.
	 */
	public String getTotalUsers()
	{
		return String.valueOf(this.userEjb.getTotalUsers());
	}
	
	/**
	 * Navigates the user to the Manage Users page.
	 */
	public void gotoAdminHome(ActionEvent e)
	{
		this.pageBacking.setRenderPage(Constants.PAGE_ADMIN_INDEX);
	}
	
	/**
	 * Handle initialization work.
	 */
	@PostConstruct
	public void init()
	{
		// Initialize the Index EJB
		try
		{
			this.indexEjb = (IndexBusinessLocal) new InitialContext()
			        .lookup("java:global/BiblioteQEAR/BiblioteQEJB/IndexBusiness!org.biblioteq.ejb.interfaces.IndexBusinessLocal");
		}
		catch (NamingException e)
		{
			A_Index_Backing.log.error("Error getting instance of IndexBusinessLocal");
			e.printStackTrace();
		}
		
		// Set the total records to be indexed
		this.recordsTotal = this.indexEjb.getTotalRecords();
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
