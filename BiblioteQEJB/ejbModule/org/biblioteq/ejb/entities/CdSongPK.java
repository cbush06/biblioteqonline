//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.entities
 * File: CdSongPK.java
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
 * This class provides the data structure that represents the composite primary key of a CD song since there is no PK defined for that table.
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
import javax.persistence.Embeddable;

/**
 * This class provides the data structure that represents the composite primary key of a CD song since there is no PK defined for that table.
 * 
 * @author Clint Bush
 * 
 */
@Embeddable
public class CdSongPK implements Serializable
{
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = 8075633989179522325L;
	
	private long cdSerialId;
	private int albumNumber;
	private int songNumber;
	
	/**
	 * Default constructor for this class.
	 */
	public CdSongPK()
	{
	}
	
	/**
	 * Convenience constructor for this class.
	 * 
	 * @param cdSerialId
	 *            (long) The serial ID that is the primary key of the CD this song is on.
	 * @param albumNumber
	 *            (int) The album number of the CD this song is on.
	 * @param songNumber
	 *            (int) The song number of this song.
	 */
	public CdSongPK(long cdSerialId, int albumNumber, int songNumber)
	{
		this.cdSerialId = cdSerialId;
		this.albumNumber = albumNumber;
		this.songNumber = songNumber;
	}
	
	/**
	 * Returns the album number of this song.
	 * 
	 * @return (int) The album number.
	 */
	@Column(name = "albumnum")
	public int getAlbumNumber()
	{
		return this.albumNumber;
	}
	
	/**
	 * Returns the CD serial ID.
	 * 
	 * @return (long) The cd serial ID.
	 */
	@Column(name = "item_oid")
	public long getCdSerialId()
	{
		return this.cdSerialId;
	}
	
	/**
	 * Returns this song's number.
	 * 
	 * @return (int) The song number.
	 */
	@Column(name = "songnum")
	public int getSongNumber()
	{
		return this.songNumber;
	}
	
	/**
	 * Sets the album number of this song.
	 * 
	 * @param albumNumber
	 *            (int) The album number to set.
	 */
	public void setAlbumNumber(int albumNumber)
	{
		this.albumNumber = albumNumber;
	}
	
	/**
	 * Sets the CD serial ID that indicates which CD this song is on.
	 * 
	 * @param cdSerialId
	 *            (long) The cd serial ID to set.
	 */
	public void setCdSerialId(long cdSerialId)
	{
		this.cdSerialId = cdSerialId;
	}
	
	/**
	 * Sets the song number of this song.
	 * 
	 * @param songNumber
	 *            (int) The song number to set.
	 */
	public void setSongNumber(int songNumber)
	{
		this.songNumber = songNumber;
	}
}
