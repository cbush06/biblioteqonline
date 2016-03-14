# Introduction #

It is a little confusing understanding how library members of the Desktop Application are registered for an online account, but the below explanation will help clear things up.


# Details #

**Regular members** (non-admin) can register by using the "Register" page that is linked to from the login page of BiblioteQ Online. They identify themselves as a valid library member by providing several key pieces of personal information about themselves.

These include:
  * First Name
  * Last Name
  * Date of Birth
  * Phone Number
  * ZIP Code
  * E-mail address

If all of these pieces of information match an active member of the library, the user will be prompted to enter a username and password of his or her choice. Assuming the username is not already taken, they will receive an online account after submitting their username and password.

**Administrators** can register by using the same page, but there are a few more steps. The Administrator must have a standard library member account so that their personal information is in the BiblioteQ database, as the Administrator editor of BiblioteQ does not accept information besides username, password, and privileges. Once the Administrator creates a standard account for him/her self, they may fill out the Registration form.

When the Administrator gets to step 2 where they are prompted for a username and password, they _MUST_ enter the same username that they use to log into BiblioteQ Desktop as an Administrator. BiblioteQ Online checks a username when a person registers for the first time. If it finds and Administrator with that username, it assumes the person registering is an Administrator and grants that person such privileges.

# Security Considerations #
Considering the information above, it is clear that when you create an administrator account for a person on the Desktop Application, you should immediately register that user online, as well. This prevents any future non-administrators from inadvertently using an administrator's username and receiving their privileges.

I would also recommend that you register the xbook\_admin account online (whether you'll use it or not) to prevent it from being used for dishonorable purposes.

# Reasons for this Design #
The reason things had to be done this way is to maintain a seamless interface between BiblioteQ Online and BiblioteQ Desktop.