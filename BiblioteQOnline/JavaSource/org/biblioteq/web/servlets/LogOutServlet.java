//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.servlets
 * File: LogOutServlet.java
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
 *
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * May 9, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * @author Clint Bush
 * 
 */
public class LogOutServlet extends HttpServlet
{
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(LogOutServlet.class);
	
	/**
	 * GUID for impelementing Serializable.
	 */
	private static final long serialVersionUID = 2669594999030904187L;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		// Kill the session
		request.getSession().invalidate();
		
		try
		{
			// Send to login page
			response.sendRedirect(request.getContextPath() + "/index.xhtml");
		}
		catch (IOException e)
		{
			LogOutServlet.log.error("Error redirecting logged out user to index.xhtml!");
			e.printStackTrace();
		}
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		// Kill the session
		request.getSession().invalidate();
		
		try
		{
			// Send to login page
			response.sendRedirect(request.getContextPath() + "/index.xhtml");
		}
		catch (IOException e)
		{
			LogOutServlet.log.error("Error redirecting logged out user to index.xhtml!");
			e.printStackTrace();
		}
	}
}
