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
 *
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jun 7, 2012, Clinton Bush, 1.0.0,
 *    New file.
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
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.interfaces.IndexBusinessLocal;
import org.biblioteq.ejb.interfaces.IndexBusinessRemote;
import org.biblioteq.ejb.interfaces.Item;

/**
 * @author Clint Bush
 * 
 */
@Stateful(name = "IndexBusiness")
@StatefulTimeout(unit = TimeUnit.DAYS, value = 1)
public class IndexBusiness implements IndexBusinessLocal, IndexBusinessRemote, Serializable
{
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 6418031905331227760L;
	
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(IndexBusiness.class);
	
	// Get the entity manager
	@PersistenceContext(unitName = "BiblioteQPersistenceUnit")
	private EntityManager em;
	
	// Declare variables we'll be using
	private long recordsTotal = 0;
	private long recordsReturned = 0;
	private long recordsReturnedFromBatch = 0;
	private int batchSize = 100;
	Query resultsQuery;
	
	// List of entities to be indexed. These must be EXACT, as they will be used to query the database.
	private String[] itemsToIndex = new String[] { "Book", "CD", "DVD", "Journal", "Magazine", "VideoGame" };
	
	// List of totals for each Entity
	private long[] itemsCount = new long[this.itemsToIndex.length];
	
	// Index of the current Entity being indexed
	int currentEntity = 0;
	
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
		}
		
		// Have we finished the current batch?
		if (this.recordsReturnedFromBatch < this.itemsCount[this.currentEntity])
		{
			// Get the results
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
	 * @see org.biblioteq.ejb.interfaces.IndexBusinessLocal#getTotalRecords()
	 */
	@Override
	public long getTotalRecords()
	{
		return this.recordsTotal;
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
		
		IndexBusiness.log.info("Total items [" + this.recordsTotal + "].");
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
}
