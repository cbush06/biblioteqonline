//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.model
 * File: ValidationMessage_Model.java
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
 * #      Revision       #
 * ####################### 
 * Mar 3, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.model;

import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class ValidationMessage_Model
{
	/**
	 * List of the error messages to display.
	 */
	private ArrayList<String> messages = new ArrayList<String>();
	
	/**
	 * Add a message to be rendered.
	 * 
	 * @param message
	 *            (String) Message to be added.
	 */
	public void addMessage(String message)
	{
		this.messages.add(message);
	}
	
	/**
	 * Get a reference to the ArrayList containing all of the messaes.
	 * 
	 * @return (ArrayList<String>) ArrayList of error messages.
	 */
	public ArrayList<String> getMessages()
	{
		return this.messages;
	}
	
	/**
	 * Returns true if there are messages.
	 * 
	 * @return (boolean) True if there are messages to be displayed.
	 */
	public boolean hasMessages()
	{
		return this.messages.size() > 0;
	}
	
	/**
	 * Adds the messages into the FacesContext to be displayed.
	 */
	public void renderMessages()
	{
		for (String nextMessage : this.messages)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, nextMessage, nextMessage));
		}
		this.messages.clear();
	}
}
