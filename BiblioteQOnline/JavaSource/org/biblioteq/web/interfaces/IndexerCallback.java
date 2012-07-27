//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.interfaces
 * File: IndexerCallback.java
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
 * This interface allows the Indexer class to notify its users of its progress and when it completes indexing.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jun 8, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.interfaces;

/**
 * This interface allows the Indexer class to notify its users of its progress and when it completes indexing.
 * 
 * @author Clint Bush
 * 
 */
public interface IndexerCallback
{
	/**
	 * Provides notification of the completion of indexing to an object using the Indexer class.
	 */
	public void indexingComplete();
	
	/**
	 * Provides updates of the progress of indexing.
	 */
	public void updateProgress(int recordsIndexed);
}
