//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: Book.java
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
 * This entity represents a Book, as used by BiblioteQ. It is mapped to the "book" table.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * May 4, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.ejb.entities;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.interfaces.Item;

/**
 * This entity represents a Book, as used by BiblioteQ. It is mapped to the "book" table.
 * 
 * @author Clint Bush
 * 
 */
@Entity(name = "Book")
@Table(name = "book")
public class Book implements Item, Serializable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(Book.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 2291491876778337778L;
	
	private String id;
	private long itemId;
	private String title;
	private String edition;
	private String author;
	private String publishDate;
	private String publisher;
	private String publishPlace;
	private String categories;
	private float price;
	private String description;
	private String language;
	private String monetaryUnit;
	private int quantity;
	private String bindingType;
	private String location;
	private String isbn13;
	private String lcControlNumber;
	private String callNumber;
	private String deweyNumber;
	private byte[] frontCover;
	private byte[] backCover;
	private String marcTags;
	private String type;
	private List<BookCopy> bookCopies;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getAbstract()
	 */
	@Override
	@Transient
	public String getAbstract()
	{
		return this.getDescription();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getAbstractForHtml()
	 */
	@Override
	@Transient
	public String getAbstractForHtml()
	{
		return this.getDescription().replace("\n", "<br/>");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getAbstractLabel()
	 */
	@Override
	@Transient
	public String getAbstractLabel()
	{
		return "Abstract";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getAbstractTrimmed()
	 */
	@Override
	@Transient
	public String getAbstractTrimmed()
	{
		if (this.getDescription().length() > 250)
		{
			return this.getDescription().substring(0, 250) + "...";
		}
		return this.getDescription();
	}
	
	/**
	 * Returns the author of this book.
	 * 
	 * @return (String) The author.
	 */
	@Column(name = "author")
	public String getAuthor()
	{
		return this.author;
	}
	
	/**
	 * Returns the byte array containing the book's cover.
	 * 
	 * @return (byte[]) The back cover.
	 */
	@Column(name = "back_cover")
	public byte[] getBackCover()
	{
		return this.backCover;
	}
	
	/**
	 * Returns the type of binding for this book (Hardcover or Paperback).
	 * 
	 * @return (String) The binding type.
	 */
	@Column(name = "binding_type", length = 32)
	public String getBindingType()
	{
		return this.bindingType;
	}
	
	/**
	 * Returns a list of the copies of this book.
	 * 
	 * @return (List<BookCopy>) List of the copies of this book.
	 */
	@OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
	public List<BookCopy> getBookCopies()
	{
		return this.bookCopies;
	}
	
	/**
	 * Returns the Call Number of the book.
	 * 
	 * @return (String) The call number.
	 */
	@Column(name = "callnumber", length = 64)
	public String getCallNumber()
	{
		return this.callNumber;
	}
	
	/**
	 * Returns the categories of the book. These are keyed in by the librarian and usually separated by new line characters.
	 * 
	 * @return (String) The categories of the book.
	 */
	@Override
	@Column(name = "category")
	public String getCategories()
	{
		return this.categories;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getCategoriesForHtml()
	 */
	@Override
	@Transient
	public String getCategoriesForHtml()
	{
		return this.getCategories().replace("\n", "<br/>");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getCategoriesLabel()
	 */
	@Override
	@Transient
	public String getCategoriesLabel()
	{
		return "Subjects";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getCompany()
	 */
	@Override
	@Transient
	public String getCompany()
	{
		return this.getPublisher();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getCompanyLabel()
	 */
	@Override
	@Transient
	public String getCompanyLabel()
	{
		return "Publisher";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getCreator()
	 */
	@Override
	@Transient
	public String getCreator()
	{
		return this.getAuthor();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getCreatorLabel()
	 */
	@Override
	@Transient
	public String getCreatorLabel()
	{
		return "Author";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getDateCreated()
	 */
	@Override
	@Transient
	public String getDateCreated()
	{
		return this.getPublishDate();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getDateCreatedLabel()
	 */
	@Override
	@Transient
	public String getDateCreatedLabel()
	{
		return "Publish Date";
	}
	
	/**
	 * Returns the description of the book.
	 * 
	 * @return (String) The description.
	 */
	@Column(name = "description")
	public String getDescription()
	{
		return this.description;
	}
	
	/**
	 * Returns the dewey decimal number of the book.
	 * 
	 * @return The dewey decimal number of the book.
	 */
	@Column(name = "deweynumber", length = 64)
	public String getDeweyNumber()
	{
		return this.deweyNumber;
	}
	
	/**
	 * Returns the edition of the book.
	 * 
	 * @return (String) The edition of the book.
	 */
	@Column(name = "edition", length = 8)
	public String getEdition()
	{
		return this.edition;
	}
	
	/**
	 * Returns the byte array containing the front cover of the book.
	 * 
	 * @return (byte[]) The front cover of the book.
	 */
	@Override
	@Column(name = "front_cover")
	public byte[] getFrontCover()
	{
		return this.frontCover;
	}
	
	/**
	 * Returns the unique id of the book. This is also the ISBN-10 for books.
	 * 
	 * @return (String) The unique id of the book.
	 */
	@Override
	@Id
	@Column(name = "id", length = 32)
	public String getId()
	{
		return this.id;
	}
	
	/**
	 * Returns the ISBN-10 number for this book. This method is just a copy of the getId() method. BiblioteQ uses the ISBN-10 number for Book IDs.
	 * 
	 * @return (String) The ISBN-10 number for this book.
	 */
	@Transient
	public String getIsbn10()
	{
		return this.id;
	}
	
	/**
	 * Returns the ISBN-13 number for the book.
	 * 
	 * @return (String) The ISBN-13 number.
	 */
	@Column(name = "isbn13", length = 16)
	public String getIsbn13()
	{
		return this.isbn13;
	}
	
	/**
	 * Returns the Item ID of the book. This ID is used to track the various items (books, dvds, etc.) and keep them differentiated. This is also the ID that links books with their
	 * individual copies.
	 * 
	 * @return (long) The Item ID of the book.
	 */
	@Override
	@Column(name = "myoid")
	public long getItemId()
	{
		return this.itemId;
	}
	
	/**
	 * Returns the language the book is written in.
	 * 
	 * @return (String) The language of the book.
	 */
	@Override
	@Column(name = "language", length = 64)
	public String getLanguage()
	{
		return this.language;
	}
	
	/**
	 * Returns the Library of Congress Control number.
	 * 
	 * @return (String) The Library of Congress Control Number.
	 */
	@Column(name = "lccontrolnumber", length = 64)
	public String getLcControlNumber()
	{
		return this.lcControlNumber;
	}
	
	/**
	 * Returns the library location of the book.
	 * 
	 * @return (String) The location.
	 */
	@Override
	@Column(name = "location")
	public String getLocation()
	{
		return this.location;
	}
	
	/**
	 * Returns the MARC Tags for the book.
	 * 
	 * @return (String) The MARC Tags.
	 */
	@Column(name = "marc_tags")
	public String getMarcTags()
	{
		return this.marcTags;
	}
	
	/**
	 * Returns the monetary units the price of the book is in.
	 * 
	 * @return (String) The monetary units.
	 */
	@Override
	@Column(name = "monetary_units", length = 64)
	public String getMonetaryUnit()
	{
		return this.monetaryUnit;
	}
	
	/**
	 * Returns the price of the book.
	 * 
	 * @return (float) The price.
	 */
	@Override
	@Column(name = "price")
	public float getPrice()
	{
		return this.price;
	}
	
	/**
	 * Returns the publish date of the book in MM/DD/YYYY format.
	 * 
	 * @return (String) The publish date.
	 */
	@Column(name = "pdate", length = 32)
	public String getPublishDate()
	{
		return this.publishDate;
	}
	
	/**
	 * Returns the publish date of the book as a Date object.
	 * 
	 * @return (Date) The publish date.
	 */
	@Transient
	public Date getPublishDateAsDate()
	{
		try
		{
			return (new SimpleDateFormat("MM/dd/yyyy")).parse(this.getPublishDate());
		}
		catch (ParseException e)
		{
			Book.log.error(e.getMessage() + "\n" + e.getStackTrace());
			return null;
		}
	}
	
	/**
	 * Returns the publisher of the book.
	 * 
	 * @return (String) The publisher of the book.
	 */
	@Column(name = "publisher")
	public String getPublisher()
	{
		return this.publisher;
	}
	
	/**
	 * Returns the publisher formatted for HTML output by replacing newline characters with an HTML linebreak tag.
	 * 
	 * @return (String) Publisher for HTML.
	 */
	@Transient
	public String getPublisherForHtml()
	{
		return this.publisher.replace("\n", "<br/>");
	}
	
	/**
	 * Returns the place the book was published.
	 * 
	 * @return (String) The place the book was published.
	 */
	@Column(name = "place")
	public String getPublishPlace()
	{
		return this.publishPlace;
	}
	
	/**
	 * Returns the places the Book was published formatted for HTML output. The newline characters have been replaced by HTML linebreak tags.
	 * 
	 * @return (String) The publish places formatted for HTML.
	 */
	@Transient
	public String getPublishPlaceForHtml()
	{
		return this.getPublishPlace().replace("\n", "<br/>");
	}
	
	/**
	 * Returns the quantity of books available.
	 * 
	 * @return (int) The quantity of books.
	 */
	@Override
	@Column(name = "quantity")
	public int getQuantity()
	{
		return this.quantity;
	}
	
	/**
	 * Returns the book title.
	 * 
	 * @return (String) The book title.
	 */
	@Override
	@Column(name = "title")
	public String getTitle()
	{
		return this.title;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getTitleLabel()
	 */
	@Override
	@Transient
	public String getTitleLabel()
	{
		return "Title";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getType()
	 */
	@Override
	@Column(name = "type", length = 16)
	public String getType()
	{
		return this.type;
	}
	
	/**
	 * Sets the book author.
	 * 
	 * @param author
	 *            (String) The author.
	 */
	public void setAuthor(String author)
	{
		this.author = author;
	}
	
	/**
	 * Sets the back cover image as a byte array.
	 * 
	 * @param backCover
	 *            (byte[]) The back cover.
	 */
	public void setBackCover(byte[] backCover)
	{
		this.backCover = backCover;
	}
	
	/**
	 * Sets the binding type (Hardcover or Paperback).
	 * 
	 * @param bindingType
	 *            (String) The binding type.
	 */
	public void setBindingType(String bindingType)
	{
		this.bindingType = bindingType;
	}
	
	/**
	 * Sets the list of copies of this book.
	 * 
	 * @param bookCopies
	 *            (List<BookCopy>) List of copies of this book.
	 */
	public void setBookCopies(List<BookCopy> bookCopies)
	{
		this.bookCopies = bookCopies;
	}
	
	/**
	 * Sets the call number of the book.
	 * 
	 * @param callNumber
	 *            (String) The call number.
	 */
	public void setCallNumber(String callNumber)
	{
		this.callNumber = callNumber;
	}
	
	/**
	 * Sets the book's categories.
	 * 
	 * @param categories
	 *            (String) The categories.
	 */
	public void setCategories(String categories)
	{
		this.categories = categories;
	}
	
	/**
	 * Sets the description of the book.
	 * 
	 * @param description
	 *            (String) The description.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * Sets the dewey decimal number of the book.
	 * 
	 * @param deweyNumber
	 *            (String) The dewey decimal number.
	 */
	public void setDeweyNumber(String deweyNumber)
	{
		this.deweyNumber = deweyNumber;
	}
	
	/**
	 * Sets the edition of the book.
	 * 
	 * @param edition
	 *            (String) The edition.
	 */
	public void setEdition(String edition)
	{
		this.edition = edition;
	}
	
	/**
	 * Sets the front cover of the book as a byte array.
	 * 
	 * @param frontCover
	 *            (String) The front cover.
	 */
	public void setFrontCover(byte[] frontCover)
	{
		this.frontCover = frontCover;
	}
	
	/**
	 * Sets the id of the book. This method is private and should never be used.
	 * 
	 * @param id
	 *            (String) The id.
	 */
	private void setId(String id)
	{
		this.id = id;
	}
	
	/**
	 * Sets the ISBN-13 of the book.
	 * 
	 * @param isbn13
	 *            (String) The ISBN-13.
	 */
	public void setIsbn13(String isbn13)
	{
		this.isbn13 = isbn13;
	}
	
	/**
	 * Sets the Item ID of this book. This is a private method and should never be used.
	 * 
	 * @param itemId
	 *            (long) The Item ID of this book.
	 */
	private void setItemId(long itemId)
	{
		this.itemId = itemId;
	}
	
	/**
	 * Sets the language of the book.
	 * 
	 * @param language
	 *            (String) The language.
	 */
	public void setLanguage(String language)
	{
		this.language = language;
	}
	
	/**
	 * Sets the Library of Congress Control Number.
	 * 
	 * @param lcControlNumber
	 *            (String) The Library of Congress Control Number.
	 */
	public void setLcControlNumber(String lcControlNumber)
	{
		this.lcControlNumber = lcControlNumber;
	}
	
	/**
	 * Sets the location of the book in the Library.
	 * 
	 * @param location
	 *            (String) The location of the book in the Library.
	 */
	public void setLocation(String location)
	{
		this.location = location;
	}
	
	/**
	 * Sets the MARC Tags for this book.
	 * 
	 * @param marcTags
	 *            (String) The MARC Tags.
	 */
	public void setMarcTags(String marcTags)
	{
		this.marcTags = marcTags;
	}
	
	/**
	 * Sets the monetary unit of the book price.
	 * 
	 * @param monetaryUnit
	 *            (String) The monetary unit of the book's price.
	 */
	public void setMonetaryUnit(String monetaryUnit)
	{
		this.monetaryUnit = monetaryUnit;
	}
	
	/**
	 * Sets the price of the book.
	 * 
	 * @param price
	 *            (float) The price.
	 */
	public void setPrice(float price)
	{
		this.price = price;
	}
	
	/**
	 * Sets the date the book was published. Must be in MM/DD/YYYY format.
	 * 
	 * @param publishDate
	 *            (String) The date the book was published.
	 */
	public void setPublishDate(String publishDate)
	{
		this.publishDate = publishDate;
	}
	
	/**
	 * Sets the date the book was published on.
	 * 
	 * @param publishDate
	 *            (Date) The Date object of the publish date.
	 */
	public void setPublishDateAsDate(Date publishDate)
	{
		// Prepare the format patterns
		String monthDayFormat = "%02d";
		String yearFormat = "%d";
		
		// Use the Calendar class to get the month, day, and year values from the Date object
		Calendar publishDateCal = Calendar.getInstance();
		publishDateCal.setTime(publishDate);
		
		// Use the String Formatter to prepare the String value of the date
		this.publishDate = String.format(monthDayFormat, publishDateCal.get(Calendar.MONTH)) + "/"
		        + String.format(monthDayFormat, publishDateCal.get(Calendar.DAY_OF_MONTH)) + "/"
		        + String.format(yearFormat, publishDateCal.get(Calendar.YEAR));
	}
	
	/**
	 * Sets the book publisher.
	 * 
	 * @param publisher
	 *            (String) The publisher.
	 */
	public void setPublisher(String publisher)
	{
		this.publisher = publisher;
	}
	
	/**
	 * Sets the location the book was published.
	 * 
	 * @param publishPlace
	 *            (String) The location the book was published.
	 */
	public void setPublishPlace(String publishPlace)
	{
		this.publishPlace = publishPlace;
	}
	
	/**
	 * Sets the number of copies of this book.
	 * 
	 * @param quantity
	 *            (int) Quantity of this book.
	 */
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	
	/**
	 * Sets the title of this book.
	 * 
	 * @param title
	 *            (String) The title.
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	/**
	 * Sets the type of this item (i.e. Book).
	 * 
	 * @param type
	 *            (String) The type.
	 */
	public void setType(String type)
	{
		this.type = type;
	}
}
