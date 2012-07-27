//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: Cd.java
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
 * This entity represents a CD, as used by BiblioteQ. It is mapped to the "cd" table.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * May 29, 2012, Clinton Bush, 1.0.0,
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
 * This entity represents a CD, as used by BiblioteQ. It is mapped to the "cd" table.
 * 
 * @author Clint Bush
 * 
 */
@Entity(name = "CD")
@Table(name = "cd")
public class CD implements Item, Serializable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(CD.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 8768714610259533452L;
	
	private String id;
	private long itemId;
	private String title;
	private String artist;
	private String recordingLabel;
	private String recordingDate;
	private String categories;
	private float price;
	private String description;
	private String language;
	private String monetaryUnit;
	private int quantity;
	private String location;
	private String runTime;
	private String format;
	private int diskCount;
	private String audio;
	private String recording;
	private byte[] frontCover;
	private byte[] backCover;
	private String type;
	private List<CdCopy> cdCopies;
	
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
		return this.getDescription().replaceAll("\n", "<br/>");
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
	 * Returns the artist of this CD.
	 * 
	 * @return (String) The artist.
	 */
	@Column(name = "artist")
	public String getArtist()
	{
		return this.artist;
	}
	
	/**
	 * Returns the artist of this CD formatted for HTML output by replacing newline characters with HTML linebreak tags.
	 * 
	 * @return (String) The artist.
	 */
	@Transient
	public String getArtistForHtml()
	{
		return this.artist.replace("\n", "<br/>");
	}
	
	/**
	 * Returns the type of audio this CD was recorded in (Stereo or Mono).
	 * 
	 * @return (String) The audio.
	 */
	@Column(name = "cdaudio", length = 32)
	public String getAudio()
	{
		return this.audio;
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
	 * Returns the categories of the CD. These are keyed in by the librarian and usually separated by new line characters.
	 * 
	 * @return (String) The categories.
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
	
	/**
	 * Returns a list of the CD copies for this CD.
	 * 
	 * @return (List<CdCopy>) The CD copies.
	 */
	@OneToMany(mappedBy = "thisCd", fetch = FetchType.EAGER)
	public List<CdCopy> getCdCopies()
	{
		return this.cdCopies;
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
		return this.getRecordingLabel();
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
		return "Recording Label";
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
		return this.getArtist();
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
		return "Artist";
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
		return this.getRecordingDate();
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
		return "Recording Date";
	}
	
	/**
	 * Returns the description of the CD.
	 * 
	 * @return (String) The description.
	 */
	@Column(name = "description")
	public String getDescription()
	{
		return this.description;
	}
	
	/**
	 * Returns the number of disks that come in this CD package.
	 * 
	 * @return (int) The disk count.
	 */
	@Column(name = "cddiskcount")
	public int getDiskCount()
	{
		return this.diskCount;
	}
	
	/**
	 * Returns the format of this CD.
	 * 
	 * @return (String) The format.
	 */
	@Column(name = "cdformat", length = 128)
	public String getFormat()
	{
		return this.format;
	}
	
	/**
	 * Returns the byte array containing the front cover of the CD.
	 * 
	 * @return (byte[]) The front cover of the CD.
	 */
	@Override
	@Column(name = "front_cover")
	public byte[] getFrontCover()
	{
		return this.frontCover;
	}
	
	/**
	 * Returns the unique id of the CD. This is also the barcode for CDs.
	 * 
	 * @return (String) The unique id of the CD.
	 */
	@Override
	@Id
	@Column(name = "id", length = 32)
	public String getId()
	{
		return this.id;
	}
	
	/**
	 * Returns the Item ID of the CD. This ID is used to track the various items (books, dvds, etc.) and keep them differentiated. This is also the ID that links CDs with their
	 * individual copies and the tracks they are composed of.
	 * 
	 * @return (long) The Item ID of the CD.
	 */
	@Override
	@Column(name = "myoid")
	public long getItemId()
	{
		return this.itemId;
	}
	
	/**
	 * Returns the language the CD is recorded in.
	 * 
	 * @return (String) The language of the CD.
	 */
	@Override
	@Column(name = "language", length = 64)
	public String getLanguage()
	{
		return this.language;
	}
	
	/**
	 * Returns the library location of the CD.
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
	 * Returns the monetary units the price of the CD is in.
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
	 * Returns the price of the CD.
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
	 * Returns the number of copies of this CD on hand.
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
	 * Returns the type of recording on the CD (Live or Studio).
	 * 
	 * @return (String) The recording type.
	 */
	@Column(name = "cdrecording", length = 32)
	public String getRecording()
	{
		return this.recording;
	}
	
	/**
	 * Returns the date the CD was recorded/released.
	 * 
	 * @return (String) The recording/release date.
	 */
	@Column(name = "rdate", length = 32)
	public String getRecordingDate()
	{
		return this.recordingDate;
	}
	
	/**
	 * Returns the recording/release date as a Date object.
	 * 
	 * @return (Date) The recording date.
	 */
	@Transient
	public Date getRecordingDateAsDate()
	{
		try
		{
			return (new SimpleDateFormat("MM/dd/yyyy")).parse(this.getRecordingDate());
		}
		catch (ParseException e)
		{
			CD.log.error(e.getMessage() + "\n" + e.getStackTrace());
			return null;
		}
	}
	
	/**
	 * Returns the Recording Label the CD was sold under.
	 * 
	 * @return (String) The recording label.
	 */
	@Column(name = "recording_label")
	public String getRecordingLabel()
	{
		return this.recordingLabel;
	}
	
	/**
	 * Returns the Recording Label the CD was sold under formatted for HTML output by replacing newline characters with HTML linebreak tags.
	 * 
	 * @return (String) The recording label.
	 */
	@Transient
	public String getRecordingLabelForHtml()
	{
		return this.recordingLabel.replace("\n", "<br/>");
	}
	
	/**
	 * Returns the total runtime of the CD.
	 * 
	 * @return (String) The runtime.
	 */
	@Column(name = "cdruntime", length = 32)
	public String getRunTime()
	{
		return this.runTime;
	}
	
	/**
	 * Returns the CD title.
	 * 
	 * @return (String) The CD title.
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
	 * Sets the CD artist.
	 * 
	 * @param artist
	 *            (String) The artist.
	 */
	public void setArtist(String artist)
	{
		this.artist = artist;
	}
	
	/**
	 * Sets the type of audio for the CD (Stereo or Mono).
	 * 
	 * @param audio
	 *            (String) The audio.
	 */
	public void setAudio(String audio)
	{
		this.audio = audio;
	}
	
	/**
	 * Sets the back cover of the CD.
	 * 
	 * @param backCover
	 *            (byte[]) The back cover.
	 */
	public void setBackCover(byte[] backCover)
	{
		this.backCover = backCover;
	}
	
	/**
	 * Sets the categories of the CD. Should be separated by newline character.
	 * 
	 * @param categories
	 *            (String) The categories.
	 */
	public void setCategories(String categories)
	{
		this.categories = categories;
	}
	
	/**
	 * Sets the list of copies of this CD.
	 * 
	 * @param cdCopies
	 *            (List<CdCopy>) The CD copies to set.
	 */
	public void setCdCopies(List<CdCopy> cdCopies)
	{
		this.cdCopies = cdCopies;
	}
	
	/**
	 * Sets the description of the CD.
	 * 
	 * @param description
	 *            (String) The description.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * Sets the disk count for the CD case.
	 * 
	 * @param diskCount
	 *            (int) The disk count.
	 */
	public void setDiskCount(int diskCount)
	{
		this.diskCount = diskCount;
	}
	
	/**
	 * Sets the format of the CD.
	 * 
	 * @param format
	 *            (String) The format.
	 */
	public void setFormat(String format)
	{
		this.format = format;
	}
	
	/**
	 * Sets the front cover of the CD.
	 * 
	 * @param frontCover
	 *            (byte[]) The front cover.
	 */
	public void setFrontCover(byte[] frontCover)
	{
		this.frontCover = frontCover;
	}
	
	/**
	 * Sets the ID of the CD. This should be the barcode number.
	 * 
	 * @param id
	 *            (String) The ID.
	 */
	public void setId(String id)
	{
		this.id = id;
	}
	
	/**
	 * Sets the item ID of the CD. This should be the serial, generated ID number that is unique to this item.
	 * 
	 * @param itemId
	 *            (long) The item ID.
	 */
	public void setItemId(long itemId)
	{
		this.itemId = itemId;
	}
	
	/**
	 * Sets the language the CD was recorded in.
	 * 
	 * @param language
	 *            (String) The language.
	 */
	public void setLanguage(String language)
	{
		this.language = language;
	}
	
	/**
	 * Sets the location the CD is stored in in the library.
	 * 
	 * @param location
	 *            (String) The location.
	 */
	public void setLocation(String location)
	{
		this.location = location;
	}
	
	/**
	 * Sets the monetary unit for the CD price.
	 * 
	 * @param monetaryUnit
	 *            (String) The monetary unit.
	 */
	public void setMonetaryUnit(String monetaryUnit)
	{
		this.monetaryUnit = monetaryUnit;
	}
	
	/**
	 * Sets the price of the CD.
	 * 
	 * @param price
	 *            (float) The price.
	 */
	public void setPrice(float price)
	{
		this.price = price;
	}
	
	/**
	 * Sets the quantity of this CD on hand.
	 * 
	 * @param quantity
	 *            (int) The quantity.
	 */
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	
	/**
	 * Sets the type of recording this CD is (Studio or Live).
	 * 
	 * @param recording
	 *            (String) The recording type.
	 */
	public void setRecording(String recording)
	{
		this.recording = recording;
	}
	
	/**
	 * Sets the date this CD was recorded/released.
	 * 
	 * @param recordingDate
	 *            (String) The recording date.
	 */
	public void setRecordingDate(String recordingDate)
	{
		this.recordingDate = recordingDate;
	}
	
	/**
	 * Sets the label this CD was sold under.
	 * 
	 * @param recordingLabel
	 *            (String) The recording label.
	 */
	public void setRecordingLabel(String recordingLabel)
	{
		this.recordingLabel = recordingLabel;
	}
	
	/**
	 * Sets the total runtime of this CD.
	 * 
	 * @param runTime
	 *            (String) The runtime.
	 */
	public void setRunTime(String runTime)
	{
		this.runTime = runTime;
	}
	
	/**
	 * Sets the title of the CD.
	 * 
	 * @param title
	 *            (String) The title.
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	/**
	 * Sets the type of item this is (CD).
	 * 
	 * @param type
	 *            (String) The type.
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	
}
