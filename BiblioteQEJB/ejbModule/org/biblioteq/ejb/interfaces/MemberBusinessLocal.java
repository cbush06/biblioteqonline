//@formatter:off
/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: org.biblioteq.ejb.interfaces
 * File: MemberBusinessLocal.java
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
 * This interface provides a definition of the local methods for the MemberBusiness EJB.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * Apr 7, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * 
 ********************************************************************************************************************************************************************************** 
 */
//@formatter:on
package org.biblioteq.ejb.interfaces;

import javax.ejb.Local;

import org.biblioteq.ejb.entities.Member;

/**
 * Local interface for the MemberBusiness EJB.
 * 
 * @author Clint Bush
 * 
 */
@Local
public interface MemberBusinessLocal
{
	/**
	 * Attempts to match the information provided to a library member record. If such a record is found, the Member entity is returned. If anything other than exactly 1 result is
	 * found, null is returned. All parameters are case-insensitive. Any empty fields will be matched against two single quotes (''), as this is how BiblioteQ stores empty string
	 * values.
	 * 
	 * @param firstName
	 *            (String) REQUIRED: The first name of the member.
	 * @param lastName
	 *            (String) REQUIRED: The last name of the member.
	 * @param dateOfBirth
	 *            (String) REQUIRED: The member's date of birth in the following format: MM/DD/YYYY. Do not exclude leading zeros on the month or day.
	 * @param phoneNumber
	 *            (String) The member's phone number in the following format: ###-###-####. An empty string will be matched against "--" which is what BiblioteQ stores if no phone
	 *            number is provided.
	 * @param zipCode
	 *            (String) REQUIRED: The member's ZIP code.
	 * @param eMail
	 *            (String) The member's e-mail address.
	 * @return (Member) If a matching record is found, the Member entity of the member. If no match or more than 1 match is found, null.
	 */
	public abstract Member getMatchingMember(String firstName, String lastName, String dateOfBirth, String phoneNumber, String zipCode,
	        String eMail);
	
	/**
	 * Returns the total number of Members in the library.
	 * 
	 * @return (long) Total count of members.
	 */
	public abstract long getTotalMembers();
	
	/**
	 * Save the member back to the database.
	 * 
	 * @param member
	 *            (Member) The Member to save.
	 * @return (Member) The merged Member.
	 */
	public abstract Member saveMember(Member member);
}
