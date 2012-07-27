//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.validators
 * File: FloatValidator.java
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
 * This class validates that a value contains only floats and throws a FacesException if it does not.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Jun 3, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.web.converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.log4j.Logger;

/**
 * This class validates that a value contains only floats and throws a FacesException if it does not.
 * 
 * @author Clint Bush
 * 
 */
public class FloatConverter implements Converter
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(FloatConverter.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value)
	{
		String toValidate = value;
		Float returnVal = 0.0f;
		
		try
		{
			returnVal = Float.parseFloat(toValidate);
		}
		catch (NumberFormatException e)
		{
			FloatConverter.log.error("A value passed as a float could not be parsed as a float.");
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
			        "A non-floating-point value was provided for the field [" + component.getClientId() + "].",
			        "A non-floating-point value was provided for the field [" + component.getClientId() + "]."));
		}
		
		return returnVal;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value)
	{
		return String.valueOf(value);
	}
}
