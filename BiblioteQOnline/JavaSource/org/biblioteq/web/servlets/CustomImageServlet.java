//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.servlets
 * File: CustomImageServlet.java
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
 * Serves images from the custom "images" folder (e.g. a custom header, icons, etc.).
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Aug 08, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJB;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.interfaces.SettingBusinessLocal;
import org.biblioteq.web.common.Constants;

/**
 * Serves images from the custom "images" folder (e.g. a custom header, icons, etc.).
 * 
 * @author Clint Bush
 * 
 */
public class CustomImageServlet extends HttpServlet
{
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(CustomImageServlet.class);
	
	/**
	 * GUID for implementing Serializable.
	 */
	private static final long serialVersionUID = -5324386285507747644L;
	
	/**
	 * Get a copy of the FrontCoverBusiness EJB.
	 */
	@EJB(name = "SettingBusiness")
	private SettingBusinessLocal settingEjb;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			URL requestUrl = null;
			try
			{
				requestUrl = new URL(request.getRequestURL().toString());
			}
			catch (MalformedURLException e)
			{
				CustomImageServlet.log.error("Error parsing request URL!!!");
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			
			Pattern p = Pattern.compile("([A-Za-z0-9]+)/([A-Za-z0-9-_.]+)\\z");
			Matcher m = p.matcher(requestUrl.getPath());
			
			if (m.find(0))
			{
				byte[] frontCover = null;
				FileInputStream input = null;
				String fileType = null;
				String fileName = m.group(2);
				File customImage = new File(File.separator + Constants.PATH_CUSTOM_IMAGES + File.separator + fileName);
				int imageLength = 0;
				CustomImageServlet.log.info(customImage.getCanonicalPath());
				if (customImage.exists())
				{
					// The image was found, so create a file name
					input = new FileInputStream(customImage);
					fileType = (new MimetypesFileTypeMap()).getContentType(customImage);
					imageLength = (int) customImage.length();
					
					//@formatter:off
					if (fileType == null)
					{
						CustomImageServlet.log.error("Could Not Determine Image Resource MIME Type!");
						response.sendError(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}
					//@formatter:on
				}
				else
				{
					CustomImageServlet.log.error("Image resource not found for " + fileName + ".");
					response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				}
				
				// Prepare the response headers
				response.reset();
				response.setBufferSize(1024); // 1KB
				response.setContentType(fileType);
				response.setHeader("Content-Length", String.valueOf(imageLength)); // Length in octets/8-bit bytes
				response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
				
				// Prepare the Buffered Streams
				BufferedInputStream bufferedInput = null;
				BufferedOutputStream bufferedOutput = null;
				
				try
				{
					bufferedInput = new BufferedInputStream(input, 1024);
					bufferedOutput = new BufferedOutputStream(response.getOutputStream(), 1024);
					
					byte[] buffer = new byte[1024];
					int length;
					
					while ((length = input.read(buffer)) > 0)
					{
						bufferedOutput.write(buffer, 0, length);
					}
				}
				catch (Exception e)
				{
					CustomImageServlet.log.error("Error delivering buffered image content.");
					e.printStackTrace();
				}
				finally
				{
					// Flush
					bufferedOutput.flush();
					
					// Close the Streams
					if (bufferedInput != null)
					{
						bufferedInput.close();
					}
					if (bufferedOutput != null)
					{
						bufferedOutput.close();
					}
				}
			}
			else
			{
				CustomImageServlet.log.error("Malformed image resource request URL: " + requestUrl.getPath());
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
			
			// TODO: Return a default cover image
		}
		catch (Exception e)
		{
			CustomImageServlet.log.error("Error serving image resource!");
			e.printStackTrace();
		}
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		// Use the same method for both request types
		this.doGet(request, response);
	}
}
