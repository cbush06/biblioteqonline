//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: Magazine.java
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
 * This entity represents a Magazine, as used by BiblioteQ. It is mapped to the "magazine" table.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jun 2, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.ejb.entities;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
 * This entity represents a Magazine, as used by BiblioteQ. It is mapped to the "magazine" table.
 * 
 * @author Clint Bush
 * 
 */
@Entity(name = "Magazine")
@Table(name = "magazine")
public class Magazine implements Item, Serializable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(Magazine.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 387837416199168140L;
	
	private String id;
	private long itemId;
	private String title;
	private String publishDate;
	private String publisher;
	private String publishPlace;
	private String categories;
	private float price;
	private String description;
	private String language;
	private String monetaryUnit;
	private int quantity;
	private String location;
	private int issueVolume;
	private int issueNumber;
	private String lcControlNumber;
	private String callNumber;
	private String deweyDecimalNumber;
	private byte[] frontCover;
	private byte[] backCover;
	private String marcTags;
	private String type;
	private List<MagazineCopy> magazineCopies;
	
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
	 * Returns the back cover image.
	 * 
	 * @return (byte[]) The back cover.
	 */
	@Column(name = "back_cover")
	public byte[] getBackCover()
	{
		return this.backCover;
	}
	
	/**
	 * Returns the call number.
	 * 
	 * @return (String) The call number.
	 */
	@Column(name = "callnumber", length = 64)
	public String getCallNumber()
	{
		return this.callNumber;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getCategories()
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
		return "Categories";
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
		// Let's hijack this method and use it to display the issue volume and number
		// since a journal doesn't have a single "Creator"
		return "Vol. " + this.getIssueVolume() + ", Number " + this.getIssueNumber();
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
		// Let's hijack this method and use it to display the issue volume and number
		// since a journal doesn't have a single "Creator"
		return "Issue";
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
	 * Returns a description of this magazine.
	 * 
	 * @return (String) The description.
	 */
	@Column(name = "description")
	public String getDescription()
	{
		return this.description;
	}
	
	/**
	 * Returns the dewey decimal number of this magazine.
	 * 
	 * @return (String) The dewey decimal number.
	 */
	@Column(name = "deweynumber", length = 64)
	public String getDeweyDecimalNumber()
	{
		return this.deweyDecimalNumber;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getFrontCover()
	 */
	@Override
	@Column(name = "front_cover")
	public byte[] getFrontCover()
	{
		return this.frontCover;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getId()
	 */
	@Override
	@Id
	@Column(name = "id", length = 32)
	public String getId()
	{
		return this.id;
	}
	
	/**
	 * Returns the issue number.
	 * 
	 * @return (int) The issue number.
	 */
	@Column(name = "issueno")
	public int getIssueNumber()
	{
		return this.issueNumber;
	}
	
	/**
	 * Returns the issue volume.
	 * 
	 * @return (int) The issue volume.
	 */
	@Column(name = "issuevolume")
	public int getIssueVolume()
	{
		return this.issueVolume;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getItemId()
	 */
	@Override
	@Column(name = "myoid")
	public long getItemId()
	{
		return this.itemId;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getLanguage()
	 */
	@Override
	@Column(name = "language", length = 64)
	public String getLanguage()
	{
		return this.language;
	}
	
	/**
	 * Returns the Library of Congress Control Number.
	 * 
	 * @return (String) The LC Control Number.
	 */
	@Column(name = "lccontrolnumber", length = 64)
	public String getLcControlNumber()
	{
		return this.lcControlNumber;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getLocation()
	 */
	@Override
	@Column(name = "location")
	public String getLocation()
	{
		return this.location;
	}
	
	/**
	 * Returns a list of the Magazine Copies for this magazine.
	 * 
	 * @return (List<MagazineCopy>) The magazine copies.
	 */
	@OneToMany(mappedBy = "thisMagazine", fetch = FetchType.EAGER)
	public List<MagazineCopy> getMagazineCopies()
	{
		return this.magazineCopies;
	}
	
	/**
	 * Returns the MARC description tags.
	 * 
	 * @return (String) The MARC Tags.
	 */
	@Column(name = "marc_tags")
	public String getMarcTags()
	{
		return this.marcTags;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getMonetaryUnit()
	 */
	@Override
	@Column(name = "monetary_units", length = 64)
	public String getMonetaryUnit()
	{
		return this.monetaryUnit;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getPrice()
	 */
	@Override
	@Column(name = "price")
	public float getPrice()
	{
		return this.price;
	}
	
	/**
	 * Returns the date this magazine was published.
	 * 
	 * @return (String) The publish date.
	 */
	@Column(name = "pdate", length = 32)
	public String getPublishDate()
	{
		return this.publishDate;
	}
	
	/**
	 * Returns the date this magazine was published as a Date object.
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
			Magazine.log.error(e.getMessage() + "\n" + e.getStackTrace());
			return null;
		}
	}
	
	/**
	 * Returns the publisher of the magazine.
	 * 
	 * @return (String) The publisher.
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
	 * Returns the place the magazine was published.
	 * 
	 * @return (String) The publish place.
	 */
	@Column(name = "place")
	public String getPublishPlace()
	{
		return this.publishPlace;
	}
	
	/**
	 * Returns the places the Magazine was published formatted for HTML output. The newline characters have been replaced by HTML linebreak tags.
	 * 
	 * @return (String) The publish places formatted for HTML.
	 */
	@Transient
	public String getPublishPlaceForHtml()
	{
		return this.getPublishPlace().replace("\n", "<br/>");
	}
	
	/**
	 * Returns the quantity of this magazine the library owns.
	 * 
	 * @return (int) The quantity.
	 */
	@Override
	@Column(name = "quantity")
	public int getQuantity()
	{
		return this.quantity;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getTitle()
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
	 * Sets a byte array containing the back cover image.
	 * 
	 * @param backCover
	 *            (byte[]) The back cover to set.
	 */
	public void setBackCover(byte[] backCover)
	{
		this.backCover = backCover;
	}
	
	/**
	 * Sets the call number.
	 * 
	 * @param callNumber
	 *            (String) The call number to set.
	 */
	public void setCallNumber(String callNumber)
	{
		this.callNumber = callNumber;
	}
	
	/**
	 * Sets the categories. Each one should be separated by a newline character.
	 * 
	 * @param categories
	 *            (String) The categories to set.
	 */
	public void setCategories(String categories)
	{
		this.categories = categories;
	}
	
	/**
	 * Sets the description/abstract.
	 * 
	 * @param description
	 *            (String) The description to set.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * Sets the dewey decimal classification number.
	 * 
	 * @param deweyDecimalNumber
	 *            (String) The dewey decimal number to set.
	 */
	public void setDeweyDecimalNumber(String deweyDecimalNumber)
	{
		this.deweyDecimalNumber = deweyDecimalNumber;
	}
	
	/**
	 * Sets a byte array containing the front cover image.
	 * 
	 * @param frontCover
	 *            (byte[]) The front cover to set.
	 */
	public void setFrontCover(byte[] frontCover)
	{
		this.frontCover = frontCover;
	}
	
	/**
	 * Sets the ID for this magazine. This should be the ISSN number for it.
	 * 
	 * @param id
	 *            (String) The id to set.
	 */
	public void setId(String id)
	{
		this.id = id;
	}
	
	/**
	 * Sets the issue number.
	 * 
	 * @param issueNumber
	 *            (int) The issue number to set.
	 */
	public void setIssueNumber(int issueNumber)
	{
		this.issueNumber = issueNumber;
	}
	
	/**
	 * Sets the volume number.
	 * 
	 * @param issueVolume
	 *            (int) The issue volume to set.
	 */
	public void setIssueVolume(int issueVolume)
	{
		this.issueVolume = issueVolume;
	}
	
	/**
	 * Sets the unique item ID. This is a generated serial ID unique to this item in BiblioteQ.
	 * 
	 * @param itemId
	 *            (long) The item ID to set.
	 */
	public void setItemId(long itemId)
	{
		this.itemId = itemId;
	}
	
	/**
	 * Sets the language.
	 * 
	 * @param language
	 *            (String) The language to set.
	 */
	public void setLanguage(String language)
	{
		this.language = language;
	}
	
	/**
	 * Sets the Library of Congress Control Number for this magazine.
	 * 
	 * @param lcControlNumber
	 *            (String) The LC Control Number to set.
	 */
	public void setLcControlNumber(String lcControlNumber)
	{
		this.lcControlNumber = lcControlNumber;
	}
	
	/**
	 * Sets the location of the magazine in the library.
	 * 
	 * @param location
	 *            (String) The location to set.
	 */
	public void setLocation(String location)
	{
		this.location = location;
	}
	
	/**
	 * Sets a list of the Magazine copies for this magazine.
	 * 
	 * @param magazineCopies
	 *            (List<MagazineCopy>) The magazine copies to set.
	 */
	public void setMagazineCopies(List<MagazineCopy> magazineCopies)
	{
		this.magazineCopies = magazineCopies;
	}
	
	/**
	 * Sets the MARC tags describing this magazine.
	 * 
	 * @param marcTags
	 *            (String) The MARC Tags to set.
	 */
	public void setMarcTags(String marcTags)
	{
		this.marcTags = marcTags;
	}
	
	/**
	 * Sets the monetary unit of the price for this magazine.
	 * 
	 * @param monetaryUnit
	 *            (String) The monetary unit to set.
	 */
	public void setMonetaryUnit(String monetaryUnit)
	{
		this.monetaryUnit = monetaryUnit;
	}
	
	/**
	 * Sets the price of this magazine.
	 * 
	 * @param price
	 *            (float) The price to set.
	 */
	public void setPrice(float price)
	{
		this.price = price;
	}
	
	/**
	 * Sets the date it was published.
	 * 
	 * @param publishDate
	 *            (String) The publish date to set.
	 */
	public void setPublishDate(String publishDate)
	{
		this.publishDate = publishDate;
	}
	
	/**
	 * Sets the publisher.
	 * 
	 * @param publisher
	 *            (String) The publisher to set.
	 */
	public void setPublisher(String publisher)
	{
		this.publisher = publisher;
	}
	
	/**
	 * Sets the place it was published.
	 * 
	 * @param publishPlace
	 *            (String) The publish place to set.
	 */
	public void setPublishPlace(String publishPlace)
	{
		this.publishPlace = publishPlace;
	}
	
	/**
	 * Sets the quantity of this magazine the library has.
	 * 
	 * @param quantity
	 *            (int) The quantity to set.
	 */
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	
	/**
	 * Sets the title of the magazine.
	 * 
	 * @param title
	 *            (String) The title to set.
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	/**
	 * Sets the item type. This should always be "Magazine".
	 * 
	 * @param type
	 *            (String) The type to set.
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	
}