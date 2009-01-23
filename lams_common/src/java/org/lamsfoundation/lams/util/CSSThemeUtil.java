/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.themes.dto.CSSThemeBriefDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class CSSThemeUtil {
	
	// private static Logger log = Logger.getLogger(CSSThemeUtil.class);
	public static String DEFAULT_HTML_THEME = "defaultHTML";
	
	public static String DEFAULT_MAIN_HTML_THEME = "defaultMainHTML";
	
	/**
	 * Will return a list of stylesheets for the current user.
	 * If the user does not have a specific stylesheet, then 
	 * the default stylesheet will be included in this list.
	 * The default stylesheet will always be included in this list.
	 * @return
	 */
	public static List<String> getAllUserThemes(String defaultTheme)
	{
		List<String> themeList = new ArrayList<String>();
		
		// Always have default as that defines everything. Other themes
		// define changes.
   		

		themeList.add(defaultTheme);   	


   		boolean userThemeFound = false;
	   	HttpSession ss = SessionManager.getSession();
	   	if ( ss != null ) {
	   		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	   		if ( user != null ) {
	   			CSSThemeBriefDTO theme = user.getHtmlTheme();
	   			
	   			if (theme != null ) {
	   				userThemeFound = true;
	   				String themeName = theme.getName();
	   				if ( themeName != null && ! isLAMSDefaultTheme(themeName) ) {
	   					themeList.add(theme.getName());
	   				}
	   			}
	   		}
	   		
	   	}
	 
	   	// if we haven't got a user theme, we are probably on the login page
	   	// so we'd better include the default server theme (if it isn't the LAMS default theme
	   	if ( !userThemeFound ) {
	   		String serverDefaultTheme = Configuration.get(ConfigurationKeys.DEFAULT_HTML_THEME);
	   		if ( serverDefaultTheme != null && ! serverDefaultTheme.equals(DEFAULT_HTML_THEME) ) {
	   					themeList.add(serverDefaultTheme);
	   		}
	   	}
	   	
	   	return themeList;
	}
	
	public static CSSThemeBriefDTO getUserTheme()
	{
		CSSThemeBriefDTO theme = null;
		
	   	HttpSession ss = SessionManager.getSession();
	   	if ( ss != null ) {
	   		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	   		if ( user != null ) {
	   			theme = user.getHtmlTheme();
	   		} 
	   	}
	   	
	   	return theme;
	   	
	}
	
	// Is this theme the LAMS basic theme, which must always be included on a web page?
	// This is NOT the server default theme - which may be a custom theme.
	public static boolean isLAMSDefaultTheme(String themeName) {
		return themeName.equals(DEFAULT_HTML_THEME);
	}
}
