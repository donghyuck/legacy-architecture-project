/*
 * Copyright 2012 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
   
package architecture.ee.web.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CookieUtils {

	private static final Log log = LogFactory.getLog(CookieUtils.class);
	
    public CookieUtils()
    {
    }

    public static Cookie getCookie(HttpServletRequest request, String name)
    {
        if(null == request || null == name)
            return null;
        Cookie cookies[];
        cookies = request.getCookies();
        if(cookies == null || name == null || name.length() == 0)
            return null;
        try
        {
            Cookie cookie = null;
            for(int i = 0; i < cookies.length; i++)
            {
                if(cookies[i] == null || cookies[i].getName() == null || !cookies[i].getName().equals(name))
                    continue;
                cookie = cookies[i];
                if(request.getServerName() != null && request.getServerName().equals(cookie.getDomain()))
                    break;
            }

            return cookie;
        }
        catch(NullPointerException e)
        {
            log.debug("NPE retrieving cookies from request, returning null", e);
        }
        return null;
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, Cookie cookie)
    {
        if(cookie != null)
        {
            String path = request.getContextPath() != null ? request.getContextPath() : "/";
            if("".equals(path))
                path = "/";
            cookie.setPath(path);
            cookie.setValue("");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value)
    {
        setCookie(request, response, name, value, 0x278d00);
    }

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, int maxAge)
    {
        if(value == null)
            value = "";
        String path = request.getContextPath() != null ? request.getContextPath() : "/";
        if("".equals(path))
            path = "/";
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

}
