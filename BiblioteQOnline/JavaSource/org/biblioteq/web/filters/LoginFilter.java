package org.biblioteq.web.filters;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.biblioteq.ejb.entities.User;
import org.biblioteq.ejb.interfaces.SettingBusinessLocal;
import org.biblioteq.web.common.Constants;

/**
 * Servlet Filter implementation class LoginFilter
 */
public class LoginFilter implements Filter
{
	/**
	 * Enum denoting the type of request coming through.
	 */
	private enum RequestType {
		UNRESTRICTED, RESTRICTED, ADMIN, LOGOUT
	}
	
	/**
	 * Inject the SettingBusiness EJB.
	 */
	@EJB(name = "SettingBusiness")
	private SettingBusinessLocal settingEjb;
	
	/**
	 * Get the logger.
	 */
	private static Logger log = Logger.getLogger(LoginFilter.class);
	
	/**
	 * Default constructor.
	 */
	public LoginFilter()
	{
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub
	}
	
	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		// Default the reqType to UNRESTRICTED
		RequestType reqType = RequestType.UNRESTRICTED;
		
		// Cast as an HttpServletRequest so we can get the request URL
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		// Put the request URL into a URL object for parsing
		URL requestUrl = new URL(httpRequest.getRequestURL().toString());
		
		// Is this a RESTRICTED request type?
		Pattern p = Pattern.compile("(/restricted/)");
		Matcher m = p.matcher(requestUrl.getPath());
		
		if (m.find())
		{
			reqType = RequestType.RESTRICTED;
		}
		
		// Is this an ADMIN request type?
		p = Pattern.compile("(/admin/)");
		m = p.matcher(requestUrl.getPath());
		
		if (m.find())
		{
			reqType = RequestType.ADMIN;
		}
		
		// Handle validating the appropriate type of request...
		switch (reqType)
		{
			case UNRESTRICTED:
				this.handleUnrestricted((HttpServletRequest) request, (HttpServletResponse) response);
				break;
			case RESTRICTED:
				this.handleRestricted((HttpServletRequest) request, (HttpServletResponse) response);
				break;
			case ADMIN:
				this.handleAdmin((HttpServletRequest) request, (HttpServletResponse) response);
				break;
		}
		
		// Pass the request along the filter chain
		chain.doFilter(request, response);
	}
	
	/**
	 * Handle validation/authorization for an ADMIN request.
	 * 
	 * @param request
	 * @param response
	 */
	private void handleAdmin(HttpServletRequest request, HttpServletResponse response)
	{
		// Check for the logged in user in the SessionMap
		User userLogin = (User) (request.getSession().getAttribute(Constants.SESSION_LOGGED_IN_USER));
		
		// If the user is logged in (userLogin is not null) and the user is an Admin (getAdmin() is not null), they're good
		if (userLogin != null && userLogin.getAdmin() != null)
		{
			return;
		}
		
		// If the user is not logged in, redirect back to the login page
		try
		{
			// Send them to log in again
			response.sendRedirect(request.getContextPath() + "/logout/");
		}
		catch (IOException e)
		{
			LoginFilter.log.error("Error while redirecting invalid user!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Handle validation/authorization for an RESTRICTED request.
	 * 
	 * @param request
	 * @param response
	 */
	private void handleRestricted(HttpServletRequest request, HttpServletResponse response)
	{
		// Check for the logged in user in the SessionMap
		User userLogin = (User) (request.getSession().getAttribute(Constants.SESSION_LOGGED_IN_USER));
		
		// If this is not null, the user is successfully logged in
		if (userLogin != null)
		{
			return;
		}
		
		// If the user is not logged in, redirect back to the login page
		try
		{
			// Send them to log in again
			response.sendRedirect(request.getContextPath() + "/logout/");
		}
		catch (IOException e)
		{
			LoginFilter.log.error("Error while redirecting invalid user!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Handle validation/authorization for an UNRESTRICTED request.
	 * 
	 * @param request
	 * @param response
	 */
	private void handleUnrestricted(HttpServletRequest request, HttpServletResponse response)
	{
		// Check for the logged in user in the SessionMap
		User userLogin = (User) (request.getSession().getAttribute(Constants.SESSION_LOGGED_IN_USER));
		
		// If the user is an Admin, or the system is enabled, they're good
		if ((userLogin != null && userLogin.getAdmin() != null)
		        || (this.settingEjb.getBooleanSettingByName(Constants.SETTING_SYSTEM_ENABLED)))
		{
			return;
		}
		
		// If the user is not logged in, redirect back to the login page
		try
		{
			LoginFilter.log.info("Unrestricted denied.");
			// Send them to log in again
			response.sendRedirect(request.getContextPath() + "/logout/");
		}
		catch (IOException e)
		{
			LoginFilter.log.error("Error while redirecting invalid user!");
			e.printStackTrace();
		}
	}
	
	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException
	{
		LoginFilter.log.info("LoginFilter initialized.");
	}
}
