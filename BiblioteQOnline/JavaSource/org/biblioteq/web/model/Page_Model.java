//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQ Online
 * Package: org.biblioteq.web.model
 * File: Page_Model.java
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
 * 03 Mar 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on

package org.biblioteq.web.model;

public class Page_Model
{
	public static enum PageType {
		Restricted, Admin
	}
	
	/**
	 * The type of page.
	 */
	private PageType type = PageType.Restricted;
	
	/**
	 * The title of the page.
	 */
	private String title = "";
	
	/**
	 * The XHTML file that provides the content of the page.
	 */
	private String content = "";
	
	/**
	 * The XHTML file that provides the navigation of the page.
	 */
	private String navigation = "";
	
	/**
	 * The XHTML file that provides the header of the page.
	 */
	private String header = "";
	
	/**
	 * Constructor for a Page_Model object that is used to construct a requested page.
	 * 
	 * @param title
	 *            (String) The title of the page.
	 * @param content
	 *            (String) XHTML file that provides the content of this page.
	 * @param navigation
	 *            (String) XHTML file that provides the navigation section of this page.
	 * @param header
	 *            (String) XHTML file that provides the header section of this page.
	 */
	public Page_Model(PageType type, String title, String content, String navigation, String header)
	{
		this.type = type;
		this.title = title;
		this.content = content;
		this.navigation = navigation;
		this.header = header;
	}
	
	/**
	 * Get the name of the XHTML file that will provide the content of the page.
	 * 
	 * @return (String) File name of XHTML file providing the page's content.
	 */
	public String getContent()
	{
		return this.content;
	}
	
	/**
	 * Get the name of the XHTML file that will provide the header section of the page.
	 * 
	 * @return (String) File name of XHTML file providing the header section of the page.
	 */
	public String getHeader()
	{
		return this.header;
	}
	
	/**
	 * Get the name of the XHTML file that will provide the navigation section of the page.
	 * 
	 * @return (String) File name of XHTML file providing the navigation section of the page.
	 */
	public String getNavigation()
	{
		return this.navigation;
	}
	
	/**
	 * Get the title of the page.
	 * 
	 * @return (String) Page title.
	 */
	public String getTitle()
	{
		return this.title;
	}
	
	/**
	 * Get the type of the page. This determines where its resources will be obtained from.
	 * 
	 * @return (PageType) Page type.
	 */
	public PageType getType()
	{
		return this.type;
	}
	
}
