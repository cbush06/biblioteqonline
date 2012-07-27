//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: VideoGame.java
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
 * This entity represents a Video Game, as used by BiblioteQ. It is mapped to the "videogame" table.
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
 * This entity represents a Video Game, as used by BiblioteQ. It is mapped to the "videogame" table.
 * 
 * @author Clint Bush
 * 
 */
@Entity(name = "VideoGame")
@Table(name = "videogame")
public class VideoGame implements Item, Serializable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(VideoGame.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 3736933862459719337L;
	
	private String id;
	private long itemId;
	private String title;
	private String developers;
	private String genres;
	private String releaseDate;
	private String publisher;
	private String publishPlace;
	private float price;
	private String description;
	private String language;
	private String monetaryUnit;
	private int quantity;
	private String location;
	private String rating;
	private String platform;
	private String playerMode;
	private byte[] frontCover;
	private byte[] backCover;
	private String type;
	private List<VideoGameCopy> videoGameCopies;
	
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
		return "Description";
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
	 * Returns a byte array containing the back cover image.
	 * 
	 * @return (byte[]) The back cover.
	 */
	@Column(name = "back_cover")
	public byte[] getBackCover()
	{
		return this.backCover;
	}
	
	/**
	 * A kludge to allow the getType() method to return a type that is compatible with Java reflection, but still maintain a coherent Entity. Read the comment
	 * in the getType() method for more details. This should always return "Video Game".
	 * 
	 * @return (String) The type of this item.
	 */
	@Column(name = "type", length = 16)
	public String getBiblioteQType()
	{
		return this.type;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.biblioteq.ejb.interfaces.Item#getCategories()
	 */
	@Override
	@Transient
	public String getCategories()
	{
		return this.genres;
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
		return "Genres";
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
		return this.getDevelopers().replace("\n", "<br/>");
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
		return "Developers";
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
	 * Returns the descrition of the game.
	 * 
	 * @return (String) The description.
	 */
	@Column(name = "description")
	public String getDescription()
	{
		return this.description;
	}
	
	/**
	 * Returns the developers of the game.
	 * 
	 * @return (String) The developers.
	 */
	@Column(name = "developer")
	public String getDevelopers()
	{
		return this.developers;
	}
	
	/**
	 * Returns the developers formatted for HTML output by replacing newline characters with HTML linebreak tags.
	 * 
	 * @return (String) Developers formatted for HTML output.
	 */
	@Transient
	public String getDevelopersForHtml()
	{
		return this.getDevelopers().replace("\n", "<br/>");
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
	
	/**
	 * Returns the genres this game belongs to.
	 * 
	 * @return (String) The genres.
	 */
	@Column(name = "genre")
	public String getGenres()
	{
		return this.genres;
	}
	
	/**
	 * Returns the genres for this game formatted for HTML output by replacing newline character with HTML linebreak tags.
	 * 
	 * @return (String) Genres formatted for HTML output.
	 */
	@Transient
	public String getGenresForHtml()
	{
		return this.getGenres().replace("\n", "<br/>");
	}
	
	/**
	 * Returns a unique ID for this video game. This is usually the UPC code for the game.
	 * 
	 * @return (String) The ID for this video game. Usually the UPC code.
	 */
	@Override
	@Id
	@Column(name = "id", length = 32)
	public String getId()
	{
		return this.id;
	}
	
	/**
	 * A unique, generated, serial ID for this item.
	 * 
	 * @return (long) Uniqe, generated, serial ID for this item.
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
	
	/**
	 * Returns the game console/platform this game works with.
	 * 
	 * @return (String) The platform.
	 */
	@Column(name = "vgplatform", length = 64)
	public String getPlatform()
	{
		return this.platform;
	}
	
	/**
	 * Returns the player mode this game supports (single/multi).
	 * 
	 * @return (String) The player mode.
	 */
	@Column(name = "vgmode", length = 16)
	public String getPlayerMode()
	{
		return this.playerMode;
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
	 * Returns the publisher of the game.
	 * 
	 * @return (String) The publisher.
	 */
	@Column(name = "publisher")
	public String getPublisher()
	{
		return this.publisher;
	}
	
	/**
	 * Returns the publishers formatted for HTML output by replacing newline characters with HTML linebreak tags.
	 * 
	 * @return (String) Publishers formatted for HTML output.
	 */
	@Transient
	public String getPublisherForHtml()
	{
		return this.getPublisher().replace("\n", "<br/>");
	}
	
	/**
	 * Returns the place the video game was published.
	 * 
	 * @return (String) The publish place.
	 */
	@Column(name = "place")
	public String getPublishPlace()
	{
		return this.publishPlace;
	}
	
	/**
	 * Returns the places the video game was published formatted for HTML output by replacing the newline characters with HTML linebreak tags.
	 * 
	 * @return (String) Publish Place formatted for HTML output.
	 */
	@Transient
	public String getPublishPlaceForHtml()
	{
		return this.getPublishPlace().replace("\n", "<br/>");
	}
	
	/**
	 * Returns the quantity of this video game the library has.
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
	 * Returns the rating of the video game.
	 * 
	 * @return (String) The rating.
	 */
	@Column(name = "vgrating", length = 64)
	public String getRating()
	{
		return this.rating;
	}
	
	/**
	 * Returns the date the video game was released. This is in MM/DD/YYYY format.
	 * 
	 * @return (String) The release date.
	 */
	@Column(name = "rdate", length = 32)
	public String getReleaseDate()
	{
		return this.releaseDate;
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
	@Transient
	public String getType()
	{
		// This should always be "Video Game" in the database, but we must override that value to remove the space.
		// BiblioteQ Online uses reflection to find the right entity type based on the item type returned by objects
		// that implement the "Item" interface. Because class names cannot contain spaces and reflection cannot accept
		// spaces, we must return a type that does not contain spaces; hence, we return "VideoGame" instead of "Video Game".
		return "VideoGame";
	}
	
	/**
	 * Returns a list of the copies of this Video Game in the library.
	 * 
	 * @return (List<VideoGameCopy>) The video game copies.
	 */
	@OneToMany(mappedBy = "thisVideoGame", fetch = FetchType.EAGER)
	public List<VideoGameCopy> getVideoGameCopies()
	{
		return this.videoGameCopies;
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
	 * Sets the type of the video game. This should always be "Video Game".
	 * 
	 * @param type
	 *            (String) The type to set.
	 */
	public void setBiblioteQType(String type)
	{
		this.type = type;
	}
	
	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            (String) The description to set.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * Sets the developers of the game.
	 * 
	 * @param developers
	 *            (String) The developers to set.
	 */
	public void setDevelopers(String developers)
	{
		this.developers = developers;
	}
	
	/**
	 * Sets a byte array containg the front cover image.
	 * 
	 * @param frontCover
	 *            (byte[]) The front cover to set.
	 */
	public void setFrontCover(byte[] frontCover)
	{
		this.frontCover = frontCover;
	}
	
	/**
	 * Sets the genres this game belongs to. They should be separated by newline characters.
	 * 
	 * @param genres
	 *            (String) The genres to set.
	 */
	public void setGenres(String genres)
	{
		this.genres = genres;
	}
	
	/**
	 * Sets the ID of this video game. This should be the UPC code of the game.
	 * 
	 * @param id
	 *            (String) The ID to set.
	 */
	public void setId(String id)
	{
		this.id = id;
	}
	
	/**
	 * Sets the unique, generated, serial ID of this item. This should be unique to all BiblioteQ items.
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
	 * Sets the location of this game in the library.
	 * 
	 * @param location
	 *            (String) The location to set.
	 */
	public void setLocation(String location)
	{
		this.location = location;
	}
	
	/**
	 * Sets the monetary unit of the price.
	 * 
	 * @param monetaryUnit
	 *            (String) The monetary unit to set.
	 */
	public void setMonetaryUnit(String monetaryUnit)
	{
		this.monetaryUnit = monetaryUnit;
	}
	
	/**
	 * Sets the platform/console this game is made for.
	 * 
	 * @param platform
	 *            (String) The platform to set.
	 */
	public void setPlatform(String platform)
	{
		this.platform = platform;
	}
	
	/**
	 * Sets the player mode of the game.
	 * 
	 * @param playerMode
	 *            (String) The player mode to set.
	 */
	public void setPlayerMode(String playerMode)
	{
		this.playerMode = playerMode;
	}
	
	/**
	 * Sets the price of the game.
	 * 
	 * @param price
	 *            (float) The price to set.
	 */
	public void setPrice(float price)
	{
		this.price = price;
	}
	
	/**
	 * Sets the publisher of the game. If there are more than one, they should be separated by newline characters.
	 * 
	 * @param publisher
	 *            (String) The publisher to set.
	 */
	public void setPublisher(String publisher)
	{
		this.publisher = publisher;
	}
	
	/**
	 * Sets the place this game was published.
	 * 
	 * @param publishPlace
	 *            (String) The publish place to set.
	 */
	public void setPublishPlace(String publishPlace)
	{
		this.publishPlace = publishPlace;
	}
	
	/**
	 * Sets the quantity of this game the library has.
	 * 
	 * @param quantity
	 *            (int) The quantity to set.
	 */
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	
	/**
	 * Sets the rating of the video game.
	 * 
	 * @param rating
	 *            (String) The rating to set.
	 */
	public void setRating(String rating)
	{
		this.rating = rating;
	}
	
	/**
	 * Sets the release date of the game. This should be in MM/DD/YYYY format.
	 * 
	 * @param releaseDate
	 *            (String) The release date to set.
	 */
	public void setReleaseDate(String releaseDate)
	{
		this.releaseDate = releaseDate;
	}
	
	/**
	 * Sets the title of the video game.
	 * 
	 * @param title
	 *            (String) The title to set.
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	/**
	 * Sets the copies of this video game.
	 * 
	 * @param videoGameCopies
	 *            (List<VideoGameCopy>) The video game copies to set.
	 */
	public void setVideoGameCopies(List<VideoGameCopy> videoGameCopies)
	{
		this.videoGameCopies = videoGameCopies;
	}
}
