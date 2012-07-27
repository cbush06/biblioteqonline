//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: CdSong.java
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
 * This entity represents a song on a CD, as defined in the "cd_songs" table.
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

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * This entity represents a song on a CD, as defined in the "cd_songs" table.
 * 
 * @author Clint Bush
 * 
 */
@Entity(name = "CdSong")
@Table(name = "cd_songs")
public class CdSong implements Serializable
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(CdSong.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 8163893758126465510L;
	
	@EmbeddedId
	private CdSongPK cdSongPk;
	
	@MapsId("cdSerialId")
	@ManyToOne
	@JoinColumn(name = "item_oid", unique = false, nullable = false, referencedColumnName = "myoid")
	private CD thisCd;
	
	private String songTitle;
	private String runTime;
	
	/**
	 * Returns the runtime of the song. Should be in HH:MM:SS form.
	 * 
	 * @return (String) The runtime.
	 */
	@Column(name = "runtime", length = 32)
	public String getRunTime()
	{
		return this.runTime;
	}
	
	/**
	 * Returns the title of this song.
	 * 
	 * @return (String) The song title.
	 */
	@Column(name = "songtitle", length = 256)
	public String getSongTitle()
	{
		return this.songTitle;
	}
	
	/**
	 * Sets the runtime of this song. Should be in HH:MM:SS form.
	 * 
	 * @param runTime
	 *            (String) The runtime to set.
	 */
	public void setRunTime(String runTime)
	{
		this.runTime = runTime;
	}
	
	/**
	 * Sets the title of this song.
	 * 
	 * @param songTitle
	 *            (String) The song title to set.
	 */
	public void setSongTitle(String songTitle)
	{
		this.songTitle = songTitle;
	}
}
