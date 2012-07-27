//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: VideoGameCopy.java
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
 * This entity represents a copy of a Video Game, as recorded in the "videogame_copy_info" table.
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * This entity represents a copy of a Video Game, as recorded in the "videogame_copy_info" table.
 * 
 * @author Clint Bush
 * 
 */
@Entity(name = "VideoGameCopy")
@Table(name = "videogame_copy_info")
public class VideoGameCopy implements Serializable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(VideoGameCopy.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 5192550381741413819L;
	
	private VideoGame thisVideoGame;
	private String videoGameCopyId;
	private int copyNumber;
	
	/**
	 * Returns the copy number of this copy of the Video Game.
	 * 
	 * @return (int) The copy number.
	 */
	@Column(name = "copy_number")
	public int getCopyNumber()
	{
		return this.copyNumber;
	}
	
	/**
	 * Returns the Video Game of which this is a copy.
	 * 
	 * @return (VideoGame) The Video Game of which this is a copy.
	 */
	@ManyToOne
	@JoinColumn(name = "item_oid", unique = false, nullable = false, referencedColumnName = "myoid")
	public VideoGame getThisVideoGame()
	{
		return this.thisVideoGame;
	}
	
	/**
	 * Returns the copy ID of this Video Game. This is the UPC number concatenated with this copy's copy number, separated by a dash (e.g. 2332342365-1).
	 * 
	 * @return (String) The video game copy ID.
	 */
	@Id
	@Column(name = "copyid")
	public String getVideoGameCopyId()
	{
		return this.videoGameCopyId;
	}
	
	/**
	 * Sets the copy number of this copy of the Video Game.
	 * 
	 * @param copyNumber
	 *            (String) The copy number to set.
	 */
	public void setCopyNumber(int copyNumber)
	{
		this.copyNumber = copyNumber;
	}
	
	/**
	 * Sets the Video Game this is a copy of.
	 * 
	 * @param thisVideoGame
	 *            (VideoGame) The Video Game this is a copy of.
	 */
	public void setThisVideoGame(VideoGame thisVideoGame)
	{
		this.thisVideoGame = thisVideoGame;
	}
	
	/**
	 * Sets the copy ID of this Video Game copy (this is the UPC number concatenated with the copy number and separated by a dash).
	 * 
	 * @param videoGameCopyId
	 *            (String) The Video Game Copy ID to set.
	 */
	public void setVideoGameCopyId(String videoGameCopyId)
	{
		this.videoGameCopyId = videoGameCopyId;
	}
	
}
