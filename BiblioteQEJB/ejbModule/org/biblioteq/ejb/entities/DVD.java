//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: DVD.java
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
 * This entity represents a DVD, as used by Biblioteq. It is mapped to the "dvd" table.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * May 31, 2012, Clinton Bush, 1.0.0,
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
 * This entity represents a DVD, as used by Biblioteq. It is mapped to the "dvd" table.
 * 
 * @author Clint Bush
 * 
 */
@Entity(name = "DVD")
@Table(name = "dvd")
public class DVD implements Serializable, Item
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(DVD.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 400424119449972587L;
	
	private String id;
	private long itemId;
	private String title;
	private String studios;
	private String releaseDate;
	private String categories;
	private float price;
	private String description;
	private String language;
	private String monetaryUnit;
	private int quantity;
	private String location;
	private String actors;
	private String formats;
	private String runtime;
	private String rating;
	private String region;
	private int diskCount;
	private String directors;
	private String aspectRatio;
	private byte[] frontCover;
	private byte[] backCover;
	private String type;
	private List<DvdCopy> dvdCopies;
	
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
	 * Returns the actors in this DVD. These are separated by newline characters.
	 * 
	 * @return (String) The actors.
	 */
	@Column(name = "dvdactor")
	public String getActors()
	{
		return this.actors;
	}
	
	/**
	 * Returns the actors in this DVD with newline characters replaced by line break tags <br/>
	 * .
	 * 
	 * @return (String) DVD actors with newline characters replaced by HTML linebreak tags.
	 */
	@Transient
	public String getActorsForHtml()
	{
		return this.actors.replace("\n", "<br/>");
	}
	
	/**
	 * Returns the aspect ratio of this DVD.
	 * 
	 * @return (String) The aspect ratio.
	 */
	@Column(name = "dvdaspectratio", length = 64)
	public String getAspectRatio()
	{
		return this.aspectRatio;
	}
	
	/**
	 * Returns the back cover of this DVD.
	 * 
	 * @return (byte[]) The back cover.
	 */
	@Column(name = "back_cover")
	public byte[] getBackCover()
	{
		return this.backCover;
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
		return this.studios;
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
		return "Studio";
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
		return this.directors.replace("\\n", "</br>");
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
		return "Directors";
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
		return this.getReleaseDate();
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
		return "Release Date";
	}
	
	/**
	 * Returns the description/abstract of this DVD.
	 * 
	 * @return (String) The description.
	 */
	@Column(name = "description")
	public String getDescription()
	{
		return this.description;
	}
	
	/**
	 * Returns the directors of this DVD. These are separated by newline characters.
	 * 
	 * @return (String) The directors.
	 */
	@Column(name = "dvddirector")
	public String getDirectors()
	{
		return this.directors;
	}
	
	/**
	 * Returns the directors of this DVD formatted for HTML output by replacing newline characters with the HTML linebreak tag.
	 * 
	 * @return (String) The directors formatted for HTML output.
	 */
	@Transient
	public String getDirectorsForHtml()
	{
		return this.directors.replace("\n", "<br/>");
	}
	
	/**
	 * Returns the number of disks included in this DVD.
	 * 
	 * @return (int) The disk count.
	 */
	@Column(name = "dvddiskcount")
	public int getDiskCount()
	{
		return this.diskCount;
	}
	
	/**
	 * Returns a list of the copies of this DVD.
	 * 
	 * @return (List<DvdCopy>) List of the copies of this DVD.
	 */
	@OneToMany(mappedBy = "thisDvd", fetch = FetchType.EAGER)
	public List<DvdCopy> getDvdCopies()
	{
		return this.dvdCopies;
	}
	
	/**
	 * Returns the formats that describe this DVD.
	 * 
	 * @return (String) The formats.
	 */
	@Column(name = "dvdformat")
	public String getFormats()
	{
		return this.formats;
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
	 * Returns the quantity of this DVD contained in the library.
	 * 
	 * @return (int) The quantity.
	 */
	@Override
	@Column(name = "quantity")
	public int getQuantity()
	{
		return this.quantity;
	}
	
	/**
	 * Returns the rating of this DVD.
	 * 
	 * @return (String) The rating.
	 */
	@Column(name = "dvdrating", length = 64)
	public String getRating()
	{
		return this.rating;
	}
	
	/**
	 * Returns the region this DVD is restricted to.
	 * 
	 * @return (String) The region.
	 */
	@Column(name = "dvdregion", length = 64)
	public String getRegion()
	{
		return this.region;
	}
	
	/**
	 * Returns the release date of this DVD.
	 * 
	 * @return (String) The release date.
	 */
	@Column(name = "rdate", length = 32)
	public String getReleaseDate()
	{
		return this.releaseDate;
	}
	
	/**
	 * Returns the release date of the DVD as a Date object.
	 * 
	 * @return (Date) The release date.
	 */
	@Transient
	public Date getReleaseDateAsDate()
	{
		try
		{
			return (new SimpleDateFormat("MM/dd/yyyy")).parse(this.getReleaseDate());
		}
		catch (ParseException e)
		{
			DVD.log.error(e.getMessage() + "\n" + e.getStackTrace());
			return null;
		}
	}
	
	/**
	 * Returns the runtime of this DVD.
	 * 
	 * @return (String) The runtime.
	 */
	@Column(name = "dvdruntime", length = 32)
	public String getRuntime()
	{
		return this.runtime;
	}
	
	/**
	 * Returns the studios this DVD was released by.
	 * 
	 * @return (String) The studios.
	 */
	@Column(name = "studio")
	public String getStudios()
	{
		return this.studios;
	}
	
	/**
	 * Returns the Studios formatted for HTML by replacing newline characters with the HTML linebreak tag, <br/>
	 * .
	 * 
	 * @return (String) The Studios formatted for HTML.
	 */
	@Transient
	public String getStudiosForHtml()
	{
		return this.studios.replace("\n", "<br/>");
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
	 * Sets the actors of this DVD. These should be separated by newline characters.
	 * 
	 * @param actors
	 *            (String) The actors to set.
	 */
	public void setActors(String actors)
	{
		this.actors = actors;
	}
	
	/**
	 * Sets the aspect ratio of this DVD.
	 * 
	 * @param aspectRatio
	 *            (String) The aspect ratio to set.
	 */
	public void setAspectRatio(String aspectRatio)
	{
		this.aspectRatio = aspectRatio;
	}
	
	/**
	 * Sets the back cover of this DVD.
	 * 
	 * @param backCover
	 *            (byte[]) The back cover to set.
	 */
	public void setBackCover(byte[] backCover)
	{
		this.backCover = backCover;
	}
	
	/**
	 * Sets the categories of this DVD. They should be separated by newline characters with each line ending in a period.
	 * 
	 * @param categories
	 *            (String) The categories to set.
	 */
	public void setCategories(String categories)
	{
		this.categories = categories;
	}
	
	/**
	 * Sets the description of this DVD.
	 * 
	 * @param description
	 *            (String) The description to set.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * Sets the directors of this DVD. These should be separated by newline characters.
	 * 
	 * @param directors
	 *            (String) The directors to set.
	 */
	public void setDirectors(String directors)
	{
		this.directors = directors;
	}
	
	/**
	 * Sets the disk count of this DVD.
	 * 
	 * @param diskCount
	 *            (int) The disk count to set.
	 */
	public void setDiskCount(int diskCount)
	{
		this.diskCount = diskCount;
	}
	
	/**
	 * Sets the list of copies of this DVD.
	 * 
	 * @param dvdCopies
	 *            (List<DvdCopy>) The list of copies of this DVD to set.
	 */
	public void setDvdCopies(List<DvdCopy> dvdCopies)
	{
		this.dvdCopies = dvdCopies;
	}
	
	/**
	 * Sets the formats that describe this DVD.
	 * 
	 * @param formats
	 *            (String) The formats to set.
	 */
	public void setFormats(String formats)
	{
		this.formats = formats;
	}
	
	/**
	 * Sets the front cover of this DVD.
	 * 
	 * @param frontCover
	 *            (byte[]) The front cover to set.
	 */
	public void setFrontCover(byte[] frontCover)
	{
		this.frontCover = frontCover;
	}
	
	/**
	 * Sets the ID of this DVD. This should be the UPC code.
	 * 
	 * @param id
	 *            (String) The id to set.
	 */
	public void setId(String id)
	{
		this.id = id;
	}
	
	/**
	 * Sets the Item ID of this DVD. This should be a generated serial ID unique to this item.
	 * 
	 * @param itemId
	 *            (long) The itemId to set.
	 */
	public void setItemId(long itemId)
	{
		this.itemId = itemId;
	}
	
	/**
	 * Sets the language of this DVD.
	 * 
	 * @param language
	 *            (String) The language to set.
	 */
	public void setLanguage(String language)
	{
		this.language = language;
	}
	
	/**
	 * Sets the location this DVD is located at in the library.
	 * 
	 * @param location
	 *            (String) The location to set.
	 */
	public void setLocation(String location)
	{
		this.location = location;
	}
	
	/**
	 * Sets the monetary unit the price of this DVD is measured in.
	 * 
	 * @param monetaryUnit
	 *            (String) The monetary unit to set.
	 */
	public void setMonetaryUnit(String monetaryUnit)
	{
		this.monetaryUnit = monetaryUnit;
	}
	
	/**
	 * Sets the price of this DVD.
	 * 
	 * @param price
	 *            (float) The price to set.
	 */
	public void setPrice(float price)
	{
		this.price = price;
	}
	
	/**
	 * Sets the quantity of this DVD the library contains.
	 * 
	 * @param quantity
	 *            (int) The quantity to set.
	 */
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	
	/**
	 * Sets the rating of this DVD.
	 * 
	 * @param rating
	 *            (String) The rating to set.
	 */
	public void setRating(String rating)
	{
		this.rating = rating;
	}
	
	/**
	 * Sets the region this DVD is restricted to.
	 * 
	 * @param region
	 *            (String) The region to set.
	 */
	public void setRegion(String region)
	{
		this.region = region;
	}
	
	/**
	 * Sets the release date of this DVD. Should be in MM/DD/YYYY format.
	 * 
	 * @param releaseDate
	 *            (String) The release date to set.1
	 */
	public void setReleaseDate(String releaseDate)
	{
		this.releaseDate = releaseDate;
	}
	
	/**
	 * Sets the runtime of this DVD. Should be in HH:SS:MM format.
	 * 
	 * @param runtime
	 *            (String) The runtime to set.
	 */
	public void setRuntime(String runtime)
	{
		this.runtime = runtime;
	}
	
	/**
	 * Sets the studios this DVD was released by. These should be separated by newline characters.
	 * 
	 * @param studios
	 *            (String) The studios to set.
	 */
	public void setStudios(String studios)
	{
		this.studios = studios;
	}
	
	/**
	 * Sets the title of this DVD.
	 * 
	 * @param title
	 *            (String) The title to set.
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	/**
	 * Sets the type of item this is. This should always be "DVD".
	 * 
	 * @param type
	 *            (String) The type to set.
	 */
	public void setType(String type)
	{
		this.type = type;
	}
}
