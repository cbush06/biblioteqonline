//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQOnline
 * Package: org.biblioteq.web.validators
 * File: IntegerValidator.java
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
 * This class validates that a value contains only integers and throws a FacesException if it does not.
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
package org.biblioteq.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;

/**
 * This class validates that a value contains only integers and throws a FacesException if it does not.
 * 
 * @author Clint Bush
 * 
 */
public class IntegerValidator implements Validator
{
	/**
	 * Logger for this class.
	 */
	private static Logger log = Logger.getLogger(IntegerValidator.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
	{
		String toValidate = (String) value;
		
		try
		{
			Integer.parseInt(toValidate);
		}
		catch (NumberFormatException e)
		{
			IntegerValidator.log.error("A value passed as an integer could not be parsed as an integer.");
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "A non-integer value was provided for the field ["
			        + component.getClientId() + "].", "A non-integer value was provided for the field [" + component.getClientId() + "]."));
		}
	}
}
