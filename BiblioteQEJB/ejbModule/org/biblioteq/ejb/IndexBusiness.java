//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb
 * File: IndexBusiness.java
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
 * This EJB provides access to various methods used when indexing the database.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jun 07, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * Aug 02, 2012, Clinton Bush, 1.1.2,
 * 	  Added methods to be used for indexing creators and subjects. Also added methods to
 * 	  retrieve a count of items of a particular type and a count of items in a particular
 *    location. 
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.ejb.PostActivate;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.entities.CreatorIndexItem;
import org.biblioteq.ejb.entities.Location;
import org.biblioteq.ejb.entities.SubjectIndexItem;
import org.biblioteq.ejb.interfaces.IndexBusinessLocal;
import org.biblioteq.ejb.interfaces.IndexBusinessRemote;
import org.biblioteq.ejb.interfaces.Item;
import org.biblioteq.ejb.model.BrowseItem;

/**
 * @author Clint Bush
 * 
 */
@Stateful(name = "IndexBusiness")
@StatefulTimeout(unit = TimeUnit.DAYS, value = 1)
public class IndexBusiness implements IndexBusinessLocal, IndexBusinessRemote, Serializable
{
	// Enum used to indicate type of browsing we're doing
	private enum BrowseType {
		CREATOR, SUBJECT, LOCATION, TYPE
	}
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 6418031905331227760L;
	
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(IndexBusiness.class);
	
	// Get the entity manager
	@PersistenceContext(unitName = "BiblioteQPersistenceUnit", type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	
	// Declare variables we'll be using
	private BrowseType browseType;
	private long recordsTotal = 0;
	private long recordsReturned = 0;
	private int recordsReturnedFromBatch = 0;
	private int batchSize = 100;
	private Query resultsQuery;
	private Query browseQuery;
	private TypedQuery<CreatorIndexItem> addOrUpdateCreatorQuery;
	private TypedQuery<SubjectIndexItem> addOrUpdateSubjectQuery;
	
	// List of entities to be indexed. These must be EXACT, as they will be used to query the database.
	private String[] itemsToIndex = new String[] { "Book", "CD", "DVD", "Journal", "Magazine", "VideoGame" };
	
	// List of totals for each Entity
	private long[] itemsCount = new long[this.itemsToIndex.length];
	
	// Index of the current Entity being indexed
	int currentEntity = 0;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.IndexBusinessLocal#addOrUpdateCreatorIndex(java.lang.String)
	 */
	@Override
	public void addOrUpdateCreatorIndex(String indexTerm, int count)
	{
		// Declare the variables we'll be using
		CreatorIndexItem item = null;
		
		// Add the parameter
		this.addOrUpdateCreatorQuery.setParameter("term", indexTerm.toLowerCase());
		
		// Get the result
		try
		{
			// We got a result!
			item = this.addOrUpdateCreatorQuery.getSingleResult();
			
			// Since we have a result, just increment its count
			item.setTotal(count);
			
			// And update it
			this.em.merge(item);
		}
		catch (NonUniqueResultException e)
		{
			IndexBusiness.log.error("More than one entry was found for the Creator Index Term [" + indexTerm + "].");
			e.printStackTrace();
		}
		catch (NoResultException e)
		{
			// No result was found, so create an entry for this term
			item = new CreatorIndexItem(indexTerm.toLowerCase(), count);
			
			// Add the new item to the database
			this.em.merge(item);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.IndexBusinessLocal#addOrUpdateSubjectIndex(java.lang.String)
	 */
	@Override
	public void addOrUpdateSubjectIndex(String indexTerm, int count)
	{
		// Declare the variables we'll be using
		SubjectIndexItem item = null;
		
		// Add the parameter
		this.addOrUpdateSubjectQuery.setParameter("term", indexTerm.toLowerCase());
		
		// Get the result
		try
		{
			// We got a result!
			item = this.addOrUpdateSubjectQuery.getSingleResult();
			
			// Since we have a result, just increment its count
			item.setTotal(count);
			
			// And update it
			this.em.merge(item);
		}
		catch (NonUniqueResultException e)
		{
			IndexBusiness.log.error("More than one entry was found for the Subject Index Term [" + indexTerm + "].");
			e.printStackTrace();
		}
		catch (NoResultException e)
		{
			// No result was found, so create an entry for this term
			item = new SubjectIndexItem(indexTerm.toLowerCase(), count);
			
			// Add the new item to the database
			this.em.merge(item);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.IndexBusinessLocal#clearCreatorIndex()
	 */
	@Override
	public void clearCreatorIndex()
	{
		Query q = this.em.createQuery("DELETE FROM CreatorIndexItem");
		q.executeUpdate();
		this.em.flush();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.IndexBusinessLocal#clearSubjectIndex()
	 */
	@Override
	public void clearSubjectIndex()
	{
		Query q = this.em.createQuery("DELETE FROM SubjectIndexItem");
		q.executeUpdate();
		this.em.flush();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.IndexBusinessLocal#getBrowseResults(long, long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<BrowseItem> getBrowseResults(int startIndex, int length)
	{
		ArrayList<BrowseItem> returnVal = new ArrayList<BrowseItem>();
		Query locationCountQuery = null;
		Long locationCountResult = new Long(0);
		
		// If we're browsing the subjects
		if (this.browseType == BrowseType.SUBJECT)
		{
			this.browseQuery.setFirstResult(startIndex);
			this.browseQuery.setMaxResults(length);
			
			for (SubjectIndexItem nextRecord : (List<SubjectIndexItem>) this.browseQuery.getResultList())
			{
				returnVal.add(new BrowseItem(nextRecord.getTerm(), nextRecord.getTotal()));
			}
		}
		
		// If we're browsing by creators
		if (this.browseType == BrowseType.CREATOR)
		{
			this.browseQuery.setFirstResult(startIndex);
			this.browseQuery.setMaxResults(length);
			
			for (CreatorIndexItem nextRecord : (List<CreatorIndexItem>) this.browseQuery.getResultList())
			{
				returnVal.add(new BrowseItem(nextRecord.getTerm(), nextRecord.getTotal()));
			}
		}
		
		// If we're browsing by locations
		if (this.browseType == BrowseType.LOCATION)
		{
			this.browseQuery.setFirstResult(startIndex);
			this.browseQuery.setMaxResults(length);
			
			/*
			 * We have 2 steps here:
			 * 1. We must get the locations. Our query is already prepared to do that.
			 * 2. We must get the number of items in each location. This is dependent upon the type of items a location stores.
			 * We will need to create another query for this.
			 */
			
			for (Location nextRecord : (List<Location>) this.browseQuery.getResultList())
			{
				//@formatter:off
				locationCountQuery = this.em.createQuery(
						"SELECT COUNT(*) " +
						"FROM " + nextRecord.getLocationPk().getType() + " t " +
						"WHERE t.location = '" + nextRecord.getLocationPk().getLocation() + "'"
				);
				//@formatter:on
				
				// Get the result
				locationCountResult = (Long) locationCountQuery.getSingleResult();
				
				// Only return a location if we have any items for that location
				if (locationCountResult > 0)
				{
					// Add a BrowseItem to the list
					returnVal.add(new BrowseItem(nextRecord.getLocationPk().getLocation(), locationCountResult, nextRecord.getLocationPk()
					        .getType()));
				}
			}
		}
		
		// If we're browsing by type
		if (this.browseType == BrowseType.TYPE)
		{
			/*
			 * We have 2 steps here:
			 * 1. We must get the types. This is already done because we maintain a list of them in a String array in this file.
			 * 2. We must get the number of items for each type. This is also already done because this EJB retrieves that information upon activation.
			 */
			for (int i = startIndex; i < this.itemsToIndex.length && i < length; i++)
			{
				// Only return an item type if we have any of that item
				if (this.itemsCount[i] > 0)
				{
					returnVal.add(new BrowseItem(this.itemsToIndex[i], this.itemsCount[i]));
				}
			}
		}
		
		// Return our result list
		return returnVal;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.IndexBusinessLocal#getNextBatch()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Item> getNextBatch()
	{
		// To return
		List<Item> resultSet = new ArrayList<Item>();
		
		// The Query must be initialized if this is the first time this method is called...
		if (this.resultsQuery == null)
		{
			this.resultsQuery = this.em.createQuery("SELECT e FROM " + this.itemsToIndex[this.currentEntity] + " e", Item.class);
			this.resultsQuery.setMaxResults(this.batchSize);
		}
		
		// Have we finished the current batch?
		if (this.recordsReturnedFromBatch < this.itemsCount[this.currentEntity])
		{
			// Get the results
			resultsQuery.setFirstResult(this.recordsReturnedFromBatch);
			resultSet.addAll(this.resultsQuery.getResultList());
			
			// Update the counting variables
			this.recordsReturned += resultSet.size();
			this.recordsReturnedFromBatch += resultSet.size();
			
			// Return the results
			return resultSet;
		}
		// The current batch is finished, so increment the entity, reset the batch item count, and rock on
		else
		{
			// If that was the last entity, return null because we're done
			if (this.currentEntity == (this.itemsToIndex.length - 1))
			{
				// Reset the counting variables
				this.currentEntity = 0;
				this.recordsReturned = 0;
				this.recordsReturnedFromBatch = 0;
				
				// Reset the Query for the first entity
				this.resultsQuery = this.em.createQuery("SELECT e FROM " + this.itemsToIndex[this.currentEntity] + " e", Item.class);
				this.resultsQuery.setMaxResults(this.batchSize);
				
				// Return null indicating we've reached the end
				return null;
			}
			// There's still more entities
			else
			{
				// Increment the entity
				this.currentEntity++;
				
				// Reset the batch item counter
				this.recordsReturnedFromBatch = 0;
				
				// Create a query for the next entity
				this.resultsQuery = this.em.createQuery("SELECT e FROM " + this.itemsToIndex[this.currentEntity] + " e", Item.class);
				this.resultsQuery.setMaxResults(this.batchSize);
				
				// Recurse the get the next entity's first batch of results
				return this.getNextBatch();
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.IndexBusinessLocal#getReturnedRecords()
	 */
	@Override
	public long getReturnedRecords()
	{
		return this.recordsReturned;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.IndexBusinessLocal#getTotalCreators()
	 */
	@Override
	public long getTotalCreators()
	{
		//@formatter:off
		TypedQuery<Long> q = this.em.createQuery(
				"SELECT COUNT(*) FROM CreatorIndexItem",
				Long.class
		);
		//@formatter:on
		
		return q.getSingleResult().longValue();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.IndexBusinessLocal#getTotalLocations()
	 */
	@Override
	public long getTotalLocations()
	{
		//@formatter:off
		TypedQuery<Long> q = this.em.createQuery(
				"SELECT COUNT(*) FROM Location",
				Long.class
		);
		//@formatter:on
		
		return q.getSingleResult().longValue();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.IndexBusinessLocal#getTotalNonEmptyLocations()
	 */
	@Override
	public long getTotalNonEmptyLocations()
	{
		long returnVal = 0;
		
		/*
		 * There are 2 Steps:
		 * 1. Get a list of the locations
		 * 2. Execute a query on each item type for a count of items
		 * assigned that location. Count the locations that have items.
		 */
		//@formatter:off
		TypedQuery<Location> q = this.em.createQuery(
				"SELECT l " +
				"FROM Location l",
				Location.class
		);
		//@formatter:on
		
		TypedQuery<Long> countQuery;
		for (Location nextLoc : q.getResultList())
		{
			//@formatter:off
			countQuery = this.em.createQuery(
					"SELECT COUNT(*)" +
					"FROM " + nextLoc.getLocationPk().getType() + " t " +
					"WHERE t.location = '" + nextLoc.getLocationPk().getLocation() + "'",
					Long.class
			);
			//@formatter:on
			
			if (countQuery.getSingleResult().longValue() > 0)
			{
				returnVal++;
			}
		}
		
		return returnVal;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.IndexBusinessLocal#getTotalRecords()
	 */
	@Override
	public long getTotalRecords()
	{
		return this.recordsTotal;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.IndexBusinessLocal#getTotalSubjects()
	 */
	@Override
	public long getTotalSubjects()
	{
		//@formatter:off
		TypedQuery<Long> q = this.em.createQuery(
				"SELECT COUNT(*) FROM SubjectIndexItem",
				Long.class
		);
		//@formatter:on
		
		return q.getSingleResult().longValue();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.IndexBusinessLocal#getTotalTypes()
	 */
	@Override
	public int getTotalTypes()
	{
		int returnVal = 0;
		for (int i = 0; i < this.itemsToIndex.length; i++)
		{
			if (this.itemsCount[i] > 0)
			{
				returnVal++;
			}
		}
		return returnVal;
	}
	
	/**
	 * Initialize the variables.
	 */
	@PostConstruct
	@PostActivate
	public void postActivation()
	{
		// Declare the variables we'll be using
		String nextEntity = null;
		long currentResult = 0;
		
		/*
		 * Get the total records to be indexed.
		 */
		// Get count from each entity
		TypedQuery<Long> q;
		
		for (int i = 0; i < this.itemsToIndex.length; i++)
		{
			nextEntity = this.itemsToIndex[i];
			
			q = this.em.createQuery("SELECT COUNT(*) FROM " + nextEntity, Long.class);
			
			try
			{
				currentResult = q.getSingleResult();
				this.recordsTotal += currentResult;
				this.itemsCount[i] = currentResult;
			}
			catch (NoResultException e)
			{
				IndexBusiness.log.error("No records returned when counting " + nextEntity + " records.");
				e.printStackTrace();
			}
		}
		
		/*
		 * Prepare the browsing query.
		 */
		this.setBrowseQueryBySubject();
		
		/*
		 * Create queries
		 */
		//@formatter:off
		this.addOrUpdateCreatorQuery = this.em.createQuery(
			"SELECT c " +
			"FROM CreatorIndexItem c " +
			"WHERE " +
				"c.term = :term",
			CreatorIndexItem.class
		);
		//@formatter:on
		
		//@formatter:off
		this.addOrUpdateSubjectQuery = this.em.createQuery(
			"SELECT s " +
			"FROM SubjectIndexItem s " +
			"WHERE " +
				"s.term = :term",
			SubjectIndexItem.class
		);
		//@formatter:on
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.IndexBusinessLocal#reset()
	 */
	@Override
	@Remove
	public void reset()
	{
		IndexBusiness.log.info("IndexBusiness EJB removed.");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.IndexBusinessLocal#setBrowseQueryByCreator()
	 */
	@Override
	public void setBrowseQueryByCreator()
	{
		// Set the enum
		this.browseType = BrowseType.CREATOR;
		
		// Build the query
		//@formatter:off
		this.browseQuery = this.em.createQuery(
				"SELECT c " +
				"FROM CreatorIndexItem c " +
				"ORDER BY " +
					"c.term"
		);
		//@formatter:on
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.IndexBusinessLocal#setBrowseQueryByLocation()
	 */
	@Override
	public void setBrowseQueryByLocation()
	{
		// Set the enum
		this.browseType = BrowseType.LOCATION;
		
		// Build the query
		//@formatter:off
		this.browseQuery = this.em.createQuery(
				"SELECT l " +
				"FROM Location l " +
				"ORDER BY " +
					"l.locationPk.location"
		);
		//@formatter:on
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.IndexBusinessLocal#setBrowseQueryBySubject()
	 */
	@Override
	public void setBrowseQueryBySubject()
	{
		// Set the enum
		this.browseType = BrowseType.SUBJECT;
		
		// Build the query
		//@formatter:off
		this.browseQuery = this.em.createQuery(
				"SELECT s " +
				"FROM SubjectIndexItem s " +
				"ORDER BY " +
					"s.term"
		);
		//@formatter:on
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.IndexBusinessLocal#setBrowseQueryByType()
	 */
	@Override
	public void setBrowseQueryByType()
	{
		// Set the enum
		this.browseType = BrowseType.TYPE;
	}
}
