//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.common
 * File: Constants.java
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
 * #      Revision       #
 * ####################### 
 * Apr 3, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.common;

/**
 * @author Clint Bush
 * 
 */
public class Constants
{
	/*
	 * System-Wide Constants
	 */
	public static String HOST_NAME = "localhost";
	public static int PORT = 8080;
	
	/*
	 * File Paths
	 */
	public static String PATH_DIR_RESTRICTED = "/restricted/";
	public static String PATH_DIR_RESTRICTED_CONTENT = "/restricted/content/";
	public static String PATH_DIR_RESTRICTED_HEADERS = "/restricted/headers/";
	public static String PATH_DIR_RESTRICTED_NAVIGATION = "/restricted/navigation/";
	public static String PATH_DIR_ADMIN = "/admin/";
	public static String PATH_DIR_ADMIN_CONTENT = "/admin/content/";
	public static String PATH_DIR_ADMIN_HEADERS = "/admin/headers/";
	public static String PATH_DIR_ADMIN_NAVIGATION = "/admin/navigation/";
	
	/*
	 * Page Names
	 */
	// Restricted
	public static String PAGE_RESTRICTED_INDEX = "Restricted_Index";
	public static String PAGE_RESTRICTED_SEARCHRESULTS = "Restricted_SearchResults";
	public static String PAGE_RESTRICTED_ITEMDETAILS = "Restricted_ItemDetails";
	public static String PAGE_RESTRICTED_ITEMREQUESTAGREEMENT = "Restricted_ItemRequestAgreement";
	public static String PAGE_RESTRICTED_ITEMREQUESTCONFIRMATION = "Restricted_ItemRequestConfirmation";
	public static String PAGE_RESTRICTED_MYACCOUNT = "Restricted_MyAccount";
	
	// Admin
	public static String PAGE_ADMIN_INDEX = "Admin_Index";
	
	public static String PAGE_ADMIN_SETTINGS = "Admin_Settings";
	public static String PAGE_ADMIN_SETTINGS_SEARCH = "Admin_Settings_Search";
	public static String PAGE_ADMIN_SETTINGS_SUMMARYPAGE = "Admin_Settings_SummaryPage";
	public static String PAGE_ADMIN_MANAGEUSERS = "Admin_ManageUsers";
	
	/*
	 * Application Map Entry Keys
	 */
	public static String APPLICATION_PAGE_MAP = "PageMap";
	
	/*
	 * Session Map Entry Keys
	 */
	public static String SESSION_LOGGED_IN_USER = "UserLogin";
	public static String SESSION_SELECTED_ITEM = "SelectedItem";
	public static String SESSION_ITEM_DESCRIPTION_BACK = "ItemDescriptionBack";
	public static String SESSION_ITEM_REQUEST_BACK = "ItemRequestBack";
	
	/*
	 * Admin setting keys.
	 */
	public static String SETTING_ALLOW_ONLINE_REQUEST = "allow_online_request";
	public static String SETTING_SHOW_FEES_OWED = "show_fees_owed";
	public static String SETTING_SHOW_ITEMS_OUT = "show_items_out";
	public static String SETTING_SHOW_ITEMS_OVERDUE = "show_items_overdue";
	public static String SETTING_SHOW_ITEMS_REQUESTED = "show_items_requested";
	public static String SETTING_SEARCH_RESULTS_PER_PAGE = "search_results_per_page";
	public static String SETTING_SEARCH_SHOW_TYPE_ICONS = "search_show_type_icons";
	public static String SETTING_SEARCH_ALLOW_NON_USERS = "search_allow_non_users";
	public static String SETTING_SEARCH_AUTO_INDEXING = "search_auto_indexing";
	public static String SETTING_SEARCH_AUTO_INDEXING_FREQ = "search_auto_indexing_frequency";
	public static String SETTING_SEARCH_LAST_INDEXING = "search_last_indexing";
	public static String SETTING_SEARCH_INDEXING_COMMENCED = "search_indexing_commenced";
	public static String SETTING_SYSTEM_ENABLED = "system_enabled";
	public static String SETTING_SYSTEM_DISABLED_MESSAGE = "system_disabled_message";
	public static String SETTING_REQUEST_AGREEMENT_TERMS = "request_agreement_terms";
	public static String SETTING_REQUEST_AGREEMENT_CHECKBOX_MESSAGE = "request_agreement_checkbox_message";
	public static String SETTING_REQUEST_CONFIRMATION_MESSAGE = "request_confirmation_message";
	
	/*
	 * Search Index Parameters.
	 */
	public static String MANAGED_BEAN_SEARCH_INDEXER = "A_Settings_Search_Indexer";
	public static String PATH_DIR_SEARCH_INDEX = "searchindex";
	public static String SEARCH_DOCUMENT_FIELD_TYPE = "type";
	public static String SEARCH_DOCUMENT_FIELD_ID = "id";
	public static String SEARCH_DOCUMENT_FIELD_OID = "oid";
	public static String SEARCH_DOCUMENT_FIELD_TITLE = "title";
	public static String SEARCH_DOCUMENT_FIELD_CREATOR = "creator";
	public static String SEARCH_DOCUMENT_FIELD_CATEGORY = "category";
	public static String SEARCH_DOCUMENT_FIELD_PUBLISHER = "publisher";
	public static String SEARCH_DOCUMENT_FIELD_DESCRIPTION = "description";
}
