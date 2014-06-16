/*********************************************************************************************************************************************************************************
 * #######################
 * #   FILE DESCRIPTOR   #
 * #######################
 * Application: BiblioteQEJB
 * Package: sql
 * File: Create_Scripts.sql
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
 * Prepares the BiblioteQ database for installation of BiblioteQOnline.
 *
 * #######################
 * #      Revision       #
 * ####################### 
 * May 06, 2012, Clinton Bush, 1.0.0,
 *    New file.
 * May 26, 2012, Clinton Bush, 1.0.0,
 *    Added the CREATE script for online_setting.
 * Aug 02, 2012, Clinton Bush, 1.1.2,
 * 	  Added CREATE scripts for tables used for browsing items. Added several settings.
 *
 ********************************************************************************************************************************************************************************** 
 */
CREATE TABLE online_user (
	id				SERIAL NOT NULL,
	username		character varying(128),
	password		text,
	memberid		character varying(16),
	adminUserName	character varying(128),
	active			boolean,
	dateCreated		timestamp,
	dateUpdated		timestamp,
	createdby_id	integer,
	updatedby_id	integer,
	PRIMARY KEY 	(id),
	FOREIGN KEY 	(memberid) REFERENCES member (memberid),
	FOREIGN KEY 	(adminUserName) REFERENCES admin (username)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE online_setting
(
  id 				SERIAL NOT NULL,
  name 				character varying(128),
  value 			text
)
WITH (
  OIDS=FALSE
);

INSERT INTO online_setting (name, value) VALUES('allow_online_request', 'true');
INSERT INTO online_setting (name, value) VALUES('show_fees_owed', 'true');
INSERT INTO online_setting (name, value) VALUES('show_items_out', 'true');
INSERT INTO online_setting (name, value) VALUES('show_items_overdue', 'true');
INSERT INTO online_setting (name, value) VALUES('show_items_requested', 'true');
INSERT INTO online_setting (name, value) VALUES('search_results_per_page', '10');
INSERT INTO online_setting (name, value) VALUES('search_show_type_icons', 'true');
INSERT INTO online_setting (name, value) VALUES('search_allow_non_users', 'false');
INSERT INTO online_setting (name, value) VALUES('search_auto_indexing', 'false');
INSERT INTO online_setting (name, value) VALUES('search_auto_indexing_frequency', 'daily');
INSERT INTO online_setting (name, value) VALUES('search_last_indexing', '0');
INSERT INTO online_setting (name, value) VALUES('search_indexing_commenced', 'false');
INSERT INTO online_setting (name, value) VALUES('system_enabled', 'true');
INSERT INTO online_setting (name, value) VALUES('system_disabled_message', 'We are sorry! The system is currently unavailable.');
INSERT INTO online_setting (name, value) VALUES('request_agreement_terms', 'See library staff for terms.');
INSERT INTO online_setting (name, value) VALUES('request_agreement_checkbox_message', 'I agree to the terms above.');
INSERT INTO online_setting (name, value) VALUES('request_confirmation_message', 'Your Request has been Successfully Placed!');

/*
 * UPGRADE - 1.1.2
 */
CREATE TABLE creator_index (
	term			text,
	total			bigint,
	PRIMARY KEY		(term)
)
WITH (
	OIDS=FALSE
);

CREATE TABLE subject_index (
	term			text,
	total			bigint,
	PRIMARY KEY		(term)
)
WITH (
	OIDS=FALSE
);

INSERT INTO online_setting (name, value) VALUES('search_browse_per_page', '10');
INSERT INTO online_setting (name, value) VALUES('system_title', 'BiblioteQ Online');
INSERT INTO online_setting (name, value) VALUES('system_custom_header', 'false');
INSERT INTO online_setting (name, value) VALUES('system_header_type', 'jpg');
CREATE INDEX creator_term_index ON creator_index (term);
CREATE INDEX subject_term_index ON subject_index (term);