//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.interfaces
 * File: Resource.java
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
 * This provides a common interface for dealing with the various types of resources a library may contain. Some common methods are required of the actual resource type classes. 
 * This makes it easier to build screens that display lists of various kinds of resources (e.g. a user's borrowed items screen, search result screens, etc.).
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * May 6, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.ejb.interfaces;

/**
 * @author Clint Bush
 * 
 */
public interface Item
{
	/**
	 * Returns the abstract/description.
	 * 
	 * @return (String) Abstract.
	 */
	public abstract String getAbstract();
	
	/**
	 * Returns the abstract formatted for output in HTML.
	 * 
	 * @return (String) Abstract.
	 */
	public abstract String getAbstractForHtml();
	
	/**
	 * Returns the appropriate label for an abstract/description of a particular type of item (e.g. Description, Abstract, Summary, etc.).
	 * 
	 * @return (String) Abstract label.
	 */
	public abstract String getAbstractLabel();
	
	/**
	 * Returns the abstract trimmed to 250 characters. If it is longer than 250 characters, an ellipsis is added.
	 * 
	 * @return (String) Abstract.
	 */
	public abstract String getAbstractTrimmed();
	
	/**
	 * Returns the categories this item belongs to.
	 * 
	 * @return (String) Categories.
	 */
	public abstract String getCategories();
	
	/**
	 * Returns the categories formatted for output in HTML.
	 * 
	 * @return (String) Categories.
	 */
	public abstract String getCategoriesForHtml();
	
	/**
	 * Returns the appropriate label for the categories/genres of a particular type of item (e.g. Categories, Genre, Subject, etc.).
	 * 
	 * @return (String) Categoreis label.
	 */
	public abstract String getCategoriesLabel();
	
	/**
	 * Returns the company that produced this item (e.g. this is the publisher for books, studio for movies and music, etc.).
	 * 
	 * @return (String) Company that produced this item.
	 */
	public abstract String getCompany();
	
	/**
	 * Returns the appropriate label for the producing company of a particular type of item (e.g. Studio, Publisher, Record Label, etc.).
	 * 
	 * @return (String) Company label.
	 */
	public abstract String getCompanyLabel();
	
	/**
	 * Returns the creator of this item (e.g. this is the author for books, producer for movies, artist for music, etc.).
	 * 
	 * @return (String) Creator.
	 */
	public abstract String getCreator();
	
	/**
	 * Returns the appropriate label for the creator of a particular type of item (e.g. Author, Artist, Journalist, etc.).
	 * 
	 * @return (String) Creator label.
	 */
	public abstract String getCreatorLabel();
	
	/**
	 * Returns the date the item was created.
	 * 
	 * @return (String) The date the item was created.
	 */
	public abstract String getDateCreated();
	
	/**
	 * Returns the appropriate label for the date created of a particular type of item (e.g. Publish Date, Release Date, etc.).
	 * 
	 * @return (String) Date Created label.
	 */
	public abstract String getDateCreatedLabel();
	
	/**
	 * Returns the front cover image of this item as a byte array.
	 * 
	 * @return (byte[]) The Front Cover image.
	 */
	public abstract byte[] getFrontCover();
	
	/**
	 * Returns the item's ID. This ID will vary in format among the different types of items (e.g. for books this is the ISBN-10 number).
	 * 
	 * @return (String) Item's ID.
	 */
	public abstract String getId();
	
	/**
	 * This is the auto-incremented serial ID of the item.
	 * 
	 * @return (long) Serial Item ID.
	 */
	public abstract long getItemId();
	
	/**
	 * Returns the language this item is in.
	 * 
	 * @return (String) Language.
	 */
	public abstract String getLanguage();
	
	/**
	 * Returns the location of the libary this item is in.
	 * 
	 * @return (String) Location in the library.
	 */
	public abstract String getLocation();
	
	/**
	 * Returns the monetary units the price of this item is in.
	 * 
	 * @return (String) Monetary Unit of the item price.
	 */
	public abstract String getMonetaryUnit();
	
	/**
	 * The cost to replace this item.
	 * 
	 * @return (float) The price.
	 */
	public abstract float getPrice();
	
	/**
	 * The number of copies of this item owned by the library.
	 * 
	 * @return (int) The quantity.
	 */
	public abstract int getQuantity();
	
	/**
	 * Returns the title of the item.
	 * 
	 * @return (String) Title.
	 */
	public abstract String getTitle();
	
	/**
	 * Returns the appropriate label for the title of a particular type of item (e.g. Title, Article, Album, etc.).
	 * 
	 * @return (String) Title label.
	 */
	public abstract String getTitleLabel();
	
	/**
	 * Returns the type of item this is (e.g. Book, DVD, etc.).
	 * 
	 * @return (String) Type of item.
	 */
	public abstract String getType();
}
