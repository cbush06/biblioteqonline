//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.backing
 * File: Search_Backing.java
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
 * Provides the methods and members backing the search features of BiblioteQOnline.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jun 9, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.backing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.biblioteq.ejb.interfaces.Item;
import org.biblioteq.ejb.interfaces.ItemBusinessLocal;
import org.biblioteq.ejb.interfaces.SettingBusinessLocal;
import org.biblioteq.web.common.Constants;
import org.biblioteq.web.model.ValidationMessage_Model;

/**
 * Provides the methods and members backing the search features of BiblioteQOnline.
 * 
 * @author Clint Bush
 * 
 */
@ManagedBean(name = "Search_Backing")
@SessionScoped
public class Search_Backing
{
	/**
	 * Use this as a data model for the page numbers at the bottom of the page.
	 * 
	 * @author Clint Bush
	 * 
	 */
	public class PageNumber
	{
		public String display = "";
		public int value = 0;
		
		public PageNumber(String display, int value)
		{
			this.display = display;
			this.value = value;
		}
		
		public String getDisplay()
		{
			return this.display;
		}
		
		public int getValue()
		{
			return this.value;
		}
	}
	
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(Search_Backing.class);
	
	/**
	 * Prepare a validation model for displaying Error Messages.
	 */
	private ValidationMessage_Model errorMessages = new ValidationMessage_Model();
	
	/**
	 * Get a copy of the Page_Backing bean. This will be injected via the setPageBackingBean(Page_Backing pageBackingBean) method.
	 */
	@ManagedProperty("#{Page_Backing}")
	private Page_Backing pageBackingBean;
	
	/**
	 * Get a reference to the ItemBusiness EJB.
	 */
	@EJB(name = "ItemBusiness")
	ItemBusinessLocal itemEjb;
	
	/**
	 * Get a reference to the SettingBusiness EJB.
	 */
	@EJB(name = "SettingBusiness")
	SettingBusinessLocal settingEjb;
	
	/**
	 * The list of items returned from the most recent search ordered by relevance.
	 */
	ArrayList<Item> searchResults = new ArrayList<Item>();
	
	/**
	 * Search type menu selection.
	 */
	private String searchType = "";
	
	/**
	 * Query entered in the Simple Search textbox.
	 */
	private String simpleSearchQuery = "";
	
	/**
	 * If set, this will filter the results based on item type.
	 */
	private String typeFilter = "All";
	
	/**
	 * List backing the search type menu.
	 */
	private List<SelectItem> searchTypeOptions = new ArrayList<SelectItem>();
	
	/**
	 * Members used in searching.
	 */
	private QueryParser queryParser = null;
	private Query query = null;
	private File indexDirectoryFile;
	private Directory indexDirectory = null;
	private IndexReader indexReader = null;
	private IndexSearcher indexSearcher = null;
	private int totalHits = 0;
	private int resultsPerPage = 0;
	
	private String[] fieldsToSearch;
	
	/**
	 * Use this to improve Pagination of results.
	 */
	private ScoreDoc searchAfter = null;
	
	/**
	 * Current page of results selected to be shown.
	 */
	private int resultsPage = 1;
	
	/**
	 * Indicates if the type icon should be shown with the search results.
	 */
	private boolean showIcon = true;
	
	/**
	 * The page just viewed.
	 */
	private int previousResultsPage = 1;
	
	/**
	 * The item the user selected.
	 */
	private Item selectedItem;
	
	public Search_Backing()
	{
		// Populate the search type options
		this.searchTypeOptions.add(new SelectItem("all", "All"));
		this.searchTypeOptions.add(new SelectItem(Constants.SEARCH_DOCUMENT_FIELD_TITLE, "Title"));
		this.searchTypeOptions.add(new SelectItem(Constants.SEARCH_DOCUMENT_FIELD_CREATOR, "Creator"));
		this.searchTypeOptions.add(new SelectItem(Constants.SEARCH_DOCUMENT_FIELD_CATEGORY, "Category"));
		this.searchTypeOptions.add(new SelectItem(Constants.SEARCH_DOCUMENT_FIELD_ID, "ID #"));
		this.searchTypeOptions.add(new SelectItem(Constants.SEARCH_DOCUMENT_FIELD_PUBLISHER, "Publisher"));
		
		// Create the fields to search array
		//@formatter:off
		this.fieldsToSearch = new String[]
		{
			Constants.SEARCH_DOCUMENT_FIELD_TITLE,
			Constants.SEARCH_DOCUMENT_FIELD_CREATOR,
			Constants.SEARCH_DOCUMENT_FIELD_CATEGORY,
			Constants.SEARCH_DOCUMENT_FIELD_PUBLISHER,
			Constants.SEARCH_DOCUMENT_FIELD_CREATOR
		};
		//@formatter:on
	}
	
	/**
	 * Executes an advanced search, resets the search fields, and redirects the user to the search results page.
	 * 
	 * @return (String) The navigation rule for JSF to follow.
	 */
	public String advancedSearch()
	{
		// Update the results per page setting
		this.resultsPerPage = this.settingEjb.getIntegerSettingByName(Constants.SETTING_SEARCH_RESULTS_PER_PAGE);
		
		return "";
	}
	
	/**
	 * Continues a Simple Search to the selected page.
	 */
	public void continueSearch()
	{
		// Declare the variables we'll be using
		int i = 0;
		
		// Ensure that an Indexing is not taking place
		if (this.settingEjb.getBooleanSettingByName(Constants.SETTING_SEARCH_INDEXING_COMMENCED))
		{
			this.errorMessages
			        .addMessage("We're sorry. The Search Engine is re-indexing the database right now. Please try your search again in 1 - 2 minutes. Thank you!");
			this.errorMessages.renderMessages();
			return;
		}
		
		// Update the results per page setting
		this.resultsPerPage = this.settingEjb.getIntegerSettingByName(Constants.SETTING_SEARCH_RESULTS_PER_PAGE);
		
		// Get a handle for the indexing directory
		this.indexDirectoryFile = new File(File.separator + Constants.PATH_DIR_SEARCH_INDEX + File.separator);
		
		// If the directory doesn't exist (the index hasn't been built), stop here
		if (!(this.indexDirectoryFile.exists()))
		{
			Search_Backing.log.error("Index Directory doesn't exist.");
			return;
		}
		
		// Prepare the Index Reader and Searcher; then, perform the search.
		try
		{
			// Prepare the Reader and Searcher
			this.indexDirectory = FSDirectory.open(this.indexDirectoryFile);
			this.indexReader = IndexReader.open(this.indexDirectory);
			this.indexSearcher = new IndexSearcher(this.indexReader);
			
			// Execute the search
			TopDocs topDocs = null;
			ScoreDoc[] results = null;
			
			if (this.resultsPage > this.previousResultsPage)
			{
				topDocs = this.indexSearcher.searchAfter(this.searchAfter, this.query, this.resultsPerPage
				        * (this.resultsPage - this.previousResultsPage));
				this.totalHits = topDocs.totalHits;
				results = topDocs.scoreDocs;
				
				// Set the point in the array where we start reading from
				if (this.totalHits / (this.resultsPage * this.resultsPerPage) > 0)
				{
					i = results.length - this.resultsPerPage;
				}
				else
				{
					i = results.length - (this.totalHits % ((this.resultsPage - 1) * this.resultsPerPage));
				}
			}
			else
			{
				topDocs = this.indexSearcher.search(this.query, this.resultsPerPage * this.resultsPage);
				this.totalHits = topDocs.totalHits;
				results = topDocs.scoreDocs;
				
				// Set the point in the array where we start reading from
				i = (this.resultsPage - 1) * this.resultsPerPage;
			}
			
			// Clear the current search result list that backs the page
			this.searchResults.clear();
			
			for (; (i < (this.resultsPerPage * this.resultsPage)) && (i < results.length); i++)
			{
				this.searchResults.add(this.itemEjb.getItemById(
				        (this.indexSearcher.doc(results[i].doc)).get(Constants.SEARCH_DOCUMENT_FIELD_ID),
				        (this.indexSearcher.doc(results[i].doc)).get(Constants.SEARCH_DOCUMENT_FIELD_TYPE)));
			}
			
			// Save the last result for faster paging
			if (results.length > 0)
			{
				this.searchAfter = results[results.length - 1];
			}
			
			// Update the Previous Results Page to the Current One
			this.previousResultsPage = this.resultsPage;
		}
		catch (CorruptIndexException e)
		{
			Search_Backing.log.error("CorruptIndexException thrown while attempting to perform a Simple Search.");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			Search_Backing.log.error("IOException thrown while attempting to perform a Simple Search.");
			e.printStackTrace();
		}
		catch (Exception e)
		{
			Search_Backing.log.error("Exception thrown while attempting to perform a Simple Search.");
			e.printStackTrace();
			this.errorMessages.addMessage("An error occurred while trying to render your results.");
			return;
		}
		finally
		{
			if (this.indexSearcher != null)
			{
				try
				{
					this.indexSearcher.close();
				}
				catch (IOException e)
				{
					Search_Backing.log.error("Error closing IndexSearcher.");
					e.printStackTrace();
				}
			}
			if (this.indexReader != null)
			{
				try
				{
					this.indexReader.close();
				}
				catch (IOException e)
				{
					Search_Backing.log.error("Error closing IndexReader.");
					e.printStackTrace();
				}
			}
			if (this.indexDirectory != null)
			{
				try
				{
					this.indexDirectory.close();
				}
				catch (IOException e)
				{
					Search_Backing.log.error("Error closing Directory.");
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Returns the next page.
	 * 
	 * @return (int) The next results page.
	 */
	public int getNextPageNumber()
	{
		return this.resultsPage + 1;
	}
	
	/**
	 * Returns the page numbers as an ArrayList of PageNumber objects.
	 * 
	 * @return (ArrayList<PageNumber>) Page numbers.
	 */
	public ArrayList<PageNumber> getPageNumbers()
	{
		ArrayList<PageNumber> returnVal = new ArrayList<PageNumber>();
		
		// Ensure there are some
		
		// The page to stop at
		int endingPage = this.totalHits / this.resultsPerPage;
		if ((this.totalHits % this.resultsPerPage) > 0)
		{
			endingPage++;
		}
		
		// If we are past the number 6, we need to begin scrolling the result list with the current page
		if (this.resultsPage > 6)
		{
			if (endingPage > this.resultsPage + 4)
			{
				endingPage = this.resultsPage + 4;
			}
			
			for (int i = this.resultsPage - 5; i <= endingPage; i++)
			{
				returnVal.add(new PageNumber(String.valueOf(i), i));
			}
		}
		// If we are at or below page 6, we just show page numbers 1 - 10
		else
		{
			if (endingPage > 10)
			{
				endingPage = 10;
			}
			
			for (int i = 1; i <= endingPage; i++)
			{
				returnVal.add(new PageNumber(String.valueOf(i), i));
			}
		}
		
		return returnVal;
	}
	
	/**
	 * Returns the previous page.
	 * 
	 * @return (int) The previous results page.
	 */
	public int getPreviousPageNumber()
	{
		return this.resultsPage - 1;
	}
	
	/**
	 * Returns the current results page number.
	 * 
	 * @return (int) The resultsPage.
	 */
	public int getResultsPage()
	{
		return this.resultsPage;
	}
	
	/**
	 * Returns the list of items returned from the most recent search ordered by relevance.
	 * 
	 * @return (ArrayList<Item>) The searchResults.
	 */
	public ArrayList<Item> getSearchResults()
	{
		return this.searchResults;
	}
	
	/**
	 * Returns the current Search Type selection.
	 * 
	 * @return (String) The search type.
	 */
	public String getSearchType()
	{
		return this.searchType;
	}
	
	/**
	 * Returns the search type options.
	 * 
	 * @return (List<String>) The search type options.
	 */
	public List<SelectItem> getSearchTypeOptions()
	{
		return this.searchTypeOptions;
	}
	
	/**
	 * Returns the item selected by the user.
	 * 
	 * @return the selectedItem
	 */
	public Item getSelectedItem()
	{
		return this.selectedItem;
	}
	
	/**
	 * Returns the value entered in the Simple Search textbox.
	 * 
	 * @return (String) The simpleSearchQuery.
	 */
	public String getSimpleSearchQuery()
	{
		return this.simpleSearchQuery;
	}
	
	/**
	 * Returns the total hits from the most recent search.
	 * 
	 * @return the totalHits
	 */
	public int getTotalHits()
	{
		return this.totalHits;
	}
	
	/**
	 * Returns the type results are filtered for.
	 * 
	 * @return the typeFilter
	 */
	public String getTypeFilter()
	{
		return this.typeFilter;
	}
	
	/**
	 * Navigates the user to the Search Results page of the Restricted Section.
	 * 
	 * @param (ActionEvent) The ActionEvent object generated by the UI element that called this method.
	 */
	public String gotoRestrictedSearchResults()
	{
		// Ensure that an Indexing is not taking place
		if (this.settingEjb.getBooleanSettingByName(Constants.SETTING_SEARCH_INDEXING_COMMENCED))
		{
			this.errorMessages
			        .addMessage("We're sorry. The Search Engine is re-indexing the database right now. Please try your search again in 1 - 2 minutes. Thank you!");
			this.errorMessages.renderMessages();
			return "";
		}
		
		this.typeFilter = "All";
		this.simpleSearch();
		this.showIcon = this.settingEjb.getBooleanSettingByName(Constants.SETTING_SEARCH_SHOW_TYPE_ICONS);
		this.pageBackingBean.setRenderPage(Constants.PAGE_RESTRICTED_SEARCHRESULTS);
		this.errorMessages.renderMessages();
		return "update";
	}
	
	/**
	 * Uses the JSF navigation rules to take the user to the unrestricted search results page.
	 * 
	 * @return (String) The JSF navigation rule for the unrestricted search results page.
	 */
	public String gotoUnRestrictedSearchResults()
	{
		// Ensure that an Indexing is not taking place
		if (this.settingEjb.getBooleanSettingByName(Constants.SETTING_SEARCH_INDEXING_COMMENCED))
		{
			this.errorMessages
			        .addMessage("We're sorry. The Search Engine is re-indexing the database right now. Please try your search again in 1 - 2 minutes. Thank you!");
			this.errorMessages.renderMessages();
			return "";
		}
		
		this.typeFilter = "All";
		this.simpleSearch();
		this.showIcon = this.settingEjb.getBooleanSettingByName(Constants.SETTING_SEARCH_SHOW_TYPE_ICONS);
		this.errorMessages.renderMessages();
		return "search";
	}
	
	public String gotoUnRestrictedSearchResultsReturn()
	{
		// Ensure that an Indexing is not taking place
		if (this.settingEjb.getBooleanSettingByName(Constants.SETTING_SEARCH_INDEXING_COMMENCED))
		{
			this.errorMessages
			        .addMessage("We're sorry. The Search Engine is re-indexing the database right now. Please try again in 1 - 2 minutes. Thank you!");
			this.errorMessages.renderMessages();
			return "";
		}
		
		this.errorMessages.renderMessages();
		return "search";
	}
	
	/**
	 * Navigate the user to the Selected Item Description.
	 */
	public String gotoUnRestrictedSelectedItem()
	{
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(Constants.SESSION_SELECTED_ITEM, this.selectedItem);
		return "itemdetails";
	}
	
	/**
	 * Initialization logic.
	 */
	@PostConstruct
	public void init()
	{
		// Update the results per page setting
		this.resultsPerPage = this.settingEjb.getIntegerSettingByName(Constants.SETTING_SEARCH_RESULTS_PER_PAGE);
	}
	
	/**
	 * Indicates whether or not the type icon for search results should be shown.
	 * 
	 * @return (boolean) True if the type icon should be shown; false, if not.
	 */
	public boolean isIconShown()
	{
		return this.showIcon;
	}
	
	/**
	 * Returns true if the Next Link should be shown.
	 * 
	 * @return (boolean) True if Next Link should be shown.
	 */
	public boolean isNextShown()
	{
		return ((this.resultsPage * this.resultsPerPage) < this.totalHits);
	}
	
	/**
	 * Returns true if the Previous Page link should be displayed.
	 * 
	 * @return (boolean) True if Previous Page link should be shown.
	 */
	public boolean isPreviousShown()
	{
		return (this.resultsPage > 1);
	}
	
	/**
	 * Returns a boolean indicating whether the Search box should be shown on an unrestricted page.
	 * 
	 * @return (boolean) True if it should be shown, false, otherwise.
	 */
	public boolean isSearchShown()
	{
		return ((this.pageBackingBean.getCurrentUser() != null) || (this.settingEjb
		        .getBooleanSettingByName(Constants.SETTING_SEARCH_ALLOW_NON_USERS)));
	}
	
	/**
	 * Allows the Page_Backing bean to be injected into this class.
	 * 
	 * @param pageBackingBean
	 *            the pageBackingBean to set
	 */
	public void setPageBackingBean(Page_Backing pageBackingBean)
	{
		this.pageBackingBean = pageBackingBean;
	}
	
	/**
	 * Sets the page number of the results page to show.
	 * 
	 * @param resultsPage
	 *            (int) The resultsPage to set.
	 */
	public void setResultsPage(int resultsPage)
	{
		this.resultsPage = resultsPage;
		this.continueSearch();
	}
	
	/**
	 * Sets the search type.
	 * 
	 * @param searchType
	 *            (String) The search type.
	 */
	public void setSearchType(String searchType)
	{
		this.searchType = searchType;
	}
	
	/**
	 * Sets the item selected by the user.
	 * 
	 * @param selectedItem
	 *            the selectedItem to set
	 */
	public void setSelectedItem(Item selectedItem)
	{
		this.selectedItem = selectedItem;
	}
	
	/**
	 * Sets the value of the Simple Search textbox.
	 * 
	 * @param simpleSearchQuery
	 *            (String) The simpleSearchQuery to set.
	 */
	public void setSimpleSearchQuery(String simpleSearchQuery)
	{
		this.simpleSearchQuery = simpleSearchQuery;
	}
	
	/**
	 * Sets the type results are filtered to. Should be an item type or "All".
	 * 
	 * @param typeFilter
	 *            the typeFilter to set
	 */
	public void setTypeFilter(String typeFilter)
	{
		this.typeFilter = typeFilter;
		this.resultsPage = 1;
		this.simpleSearch();
		this.errorMessages.renderMessages();
	}
	
	/**
	 * Executes a simple search, resets the search type drop-down list, and redirects the user to the search results page.
	 */
	public void simpleSearch()
	{
		// Ensure that an Indexing is not taking place
		if (this.settingEjb.getBooleanSettingByName(Constants.SETTING_SEARCH_INDEXING_COMMENCED))
		{
			this.errorMessages
			        .addMessage("We're sorry. The Search Engine is re-indexing the database right now. Please try your search again in 1 - 2 minutes. Thank you!");
			this.errorMessages.renderMessages();
			return;
		}
		
		// Update the results per page setting
		this.resultsPerPage = this.settingEjb.getIntegerSettingByName(Constants.SETTING_SEARCH_RESULTS_PER_PAGE);
		
		// Parse the Query
		try
		{
			// If the user is searching all fields
			if (this.searchType.equals("all"))
			{
				// If the user is searching all fields with no type filter
				if (this.typeFilter.equals("All"))
				{
					this.queryParser = new MultiFieldQueryParser(Version.LUCENE_36, this.fieldsToSearch,
					        new StopAnalyzer(Version.LUCENE_36));
					this.queryParser.setAllowLeadingWildcard(true);
					
					this.query = this.queryParser.parse(this.simpleSearchQuery);
				}
				// If a type filter has been applied, we must restrict the type of items returned
				else
				{
					this.queryParser = new MultiFieldQueryParser(Version.LUCENE_36, this.fieldsToSearch,
					        new StopAnalyzer(Version.LUCENE_36));
					this.queryParser.setAllowLeadingWildcard(true);
					
					this.query = new BooleanQuery();
					((BooleanQuery) this.query).add(this.queryParser.parse(this.simpleSearchQuery), BooleanClause.Occur.MUST);
					((BooleanQuery) this.query).add(new BooleanClause(new TermQuery(new Term(Constants.SEARCH_DOCUMENT_FIELD_TYPE,
					        this.typeFilter)), BooleanClause.Occur.MUST));
				}
			}
			// If the user is searching a specific field
			else
			{
				// If the field being searched is not the ID
				if (!(this.searchType.equals(Constants.SEARCH_DOCUMENT_FIELD_ID)))
				{
					// If the user has no type filter applied
					if (this.typeFilter.equals("All"))
					{
						this.queryParser = new QueryParser(Version.LUCENE_36, this.searchType, new StopAnalyzer(Version.LUCENE_36));
						this.queryParser.setAllowLeadingWildcard(true);
						
						this.query = this.queryParser.parse(this.simpleSearchQuery);
					}
					// If a type filter is applied, we must restricted the items returned to that type
					else
					{
						this.queryParser = new QueryParser(Version.LUCENE_36, this.searchType, new StopAnalyzer(Version.LUCENE_36));
						this.queryParser.setAllowLeadingWildcard(true);
						
						this.query = new BooleanQuery();
						((BooleanQuery) this.query).add(this.queryParser.parse(this.simpleSearchQuery), BooleanClause.Occur.MUST);
						((BooleanQuery) this.query).add(new TermQuery(new Term(Constants.SEARCH_DOCUMENT_FIELD_TYPE, this.typeFilter)),
						        BooleanClause.Occur.MUST);
					}
				}
				// If an ID is being searched, we must use a term query to require an exact match
				else
				{
					// If the user has no type filter applied
					if (this.typeFilter.equals("All"))
					{
						this.query = new TermQuery(new Term(Constants.SEARCH_DOCUMENT_FIELD_ID, this.simpleSearchQuery));
					}
					// If a type filter is applied, we must restricted the items returned to that type
					else
					{
						this.query = new BooleanQuery();
						((BooleanQuery) this.query).add(new BooleanClause(new TermQuery(new Term(Constants.SEARCH_DOCUMENT_FIELD_ID,
						        this.simpleSearchQuery)), BooleanClause.Occur.MUST));
						((BooleanQuery) this.query).add(new BooleanClause(new TermQuery(new Term(Constants.SEARCH_DOCUMENT_FIELD_TYPE,
						        this.typeFilter)), BooleanClause.Occur.MUST));
					}
				}
			}
		}
		catch (Exception e)
		{
			Search_Backing.log.error("Exception occurred while trying to parse Simple Search query.");
			this.errorMessages
			        .addMessage("An error occurred as a result of your Search Query. Please ensure it is not blank and does not begin with a Wildcard Character (e.g. '*', '?', etc.).");
			e.printStackTrace();
			return;
		}
		
		// Get a handle for the indexing directory
		this.indexDirectoryFile = new File(File.separator + Constants.PATH_DIR_SEARCH_INDEX + File.separator);
		
		// If the directory doesn't exist (the index hasn't been built), stop here
		if (!(this.indexDirectoryFile.exists()))
		{
			Search_Backing.log.error("Index Directory doesn't exist.");
			return;
		}
		
		// Prepare the Index Reader and Searcher; then, perform the search.
		try
		{
			// Prepare the Reader and Searcher
			this.indexDirectory = FSDirectory.open(this.indexDirectoryFile);
			this.indexReader = IndexReader.open(this.indexDirectory);
			this.indexSearcher = new IndexSearcher(this.indexReader);
			
			// Execute the search -- Get the 10 x RESULTS PER PAGE results.
			TopDocs topDocs = this.indexSearcher.search(this.query, this.resultsPerPage);
			this.totalHits = topDocs.totalHits;
			ScoreDoc[] results = topDocs.scoreDocs;
			
			// Translate the score docs into Items to be added to the results list
			this.searchResults.clear();
			for (ScoreDoc doc : results)
			{
				this.searchResults.add(this.itemEjb.getItemById((this.indexSearcher.doc(doc.doc)).get(Constants.SEARCH_DOCUMENT_FIELD_ID),
				        (this.indexSearcher.doc(doc.doc)).get(Constants.SEARCH_DOCUMENT_FIELD_TYPE)));
			}
			
			// Save the last result to enable faster paging
			if (results.length > 0)
			{
				this.searchAfter = results[results.length - 1];
			}
		}
		catch (CorruptIndexException e)
		{
			Search_Backing.log.error("CorruptIndexException thrown while attempting to perform a Simple Search.");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			Search_Backing.log.error("IOException thrown while attempting to perform a Simple Search.");
			e.printStackTrace();
		}
		finally
		{
			if (this.indexSearcher != null)
			{
				try
				{
					this.indexSearcher.close();
				}
				catch (IOException e)
				{
					Search_Backing.log.error("Error closing IndexSearcher.");
					e.printStackTrace();
				}
			}
			if (this.indexReader != null)
			{
				try
				{
					this.indexReader.close();
				}
				catch (IOException e)
				{
					Search_Backing.log.error("Error closing IndexReader.");
					e.printStackTrace();
				}
			}
			if (this.indexDirectory != null)
			{
				try
				{
					this.indexDirectory.close();
				}
				catch (IOException e)
				{
					Search_Backing.log.error("Error closing Directory.");
					e.printStackTrace();
				}
			}
		}
	}
}
