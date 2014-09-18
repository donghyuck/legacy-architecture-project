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
   

package architecture.ee.web.security.authentication;

/**
 * Pluggable authentication service. Users of system that wish to change the AuthProvider
 * implementation used to authenticate users can set the <code>AuthProvider.className</code>
 * system property. For example, if you have configured architecture server to use LDAP for user information,
 * you'd want to send a custom implementation of AuthFactory to make LDAP auth queries.
 * After changing the <code>AuthProvider.className</code> system property, you must restart your
 * application server.
 * 
 * @author donghyuck, son
 *
 */
public abstract class AuthFactory
{

    public AuthFactory()
    {
    }

/*    public static AuthToken getAuthToken(String s, String s1)
        throws UnAuthorizedException
    {
        loadAuthFactory();
        return factory.createAuthToken(s, s1);
    }

    public static AuthToken getAuthToken(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws UnAuthorizedException
    {
        loadAuthFactory();
        return factory.createAuthToken(httpservletrequest, httpservletresponse);
    }

    public static AuthToken getAnonymousAuthToken()
    {
        loadAuthFactory();
        return factory.createAnonymousAuthToken();
    }

    public static String encryptAuthInfo(String s, String s1)
    {
        if(s == null || s1 == null)
            throw new NullPointerException("Username or password was null.");
        try
        {
            return cipher.encryptString(s + '\002' + s1);
        }
        catch(Exception exception)
        {
            log.error(exception);
        }
        return null;
    }

    public static String[] decryptAuthInfo(String s)
        throws BadPaddingException, IllegalBlockSizeException
    {
        if(s == null || s.length() <= 0)
            return null;
        s = cipher.decryptString(s);
        if(s == null)
        {
            return null;
        } else
        {
            int i = s.indexOf('\002');
            String s1 = i < 0 ? "" : s.substring(0, i);
            String s2 = i < 0 ? "" : s.substring(i + 1);
            return (new String[] {
                s1, s2
            });
        }
    }

    protected abstract AuthToken createAuthToken(String s, String s1)
        throws UnAuthorizedException;

    protected AuthToken createAuthToken(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws UnAuthorizedException
    {
        HttpSession httpsession = httpservletrequest.getSession();
        AuthToken authtoken = (AuthToken)httpsession.getAttribute("security.authToken");
        if(authtoken != null)
            return authtoken;
        else
            throw new UnAuthorizedException();
    }

    protected abstract AuthToken createAnonymousAuthToken();

    public static AuthToken loginUser(String s, String s1, boolean flag, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws UnAuthorizedException
    {
        loadAuthFactory();
        return factory.createSessionUserInfo(s, s1, flag, httpservletrequest, httpservletresponse);
    }

    protected AuthToken createSessionUserInfo(String s, String s1, boolean flag, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws UnAuthorizedException
    {
        HttpSession httpsession = httpservletrequest.getSession();
        AuthToken authtoken = getAuthToken(s, s1);
        httpsession.setAttribute("security.authToken", authtoken);
        return authtoken;
    }

    public static void logoutUser(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
    {
        loadAuthFactory();
        factory.clearSessionUserInfo(httpservletrequest, httpservletresponse);
    }

    protected void clearSessionUserInfo(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
    {
        httpservletrequest.getSession().removeAttribute("security.authToken");
    }

    private static void loadAuthFactory()
    {
        if(factory == null)
            synchronized(className)
            {
                if(factory == null)
                {
                    log.debug("Loading auth factory...");
                    
                    ApplicationProperties setupProperties = AdminHelper.getRepository().getSetupApplicationProperties();
                    
                    
                    
                    
                    String s1 = WebApplicationHelperFactory.getWebApplicationHelper().getXMLProperty("security.AuthFactory.className");
                    if(s1 != null)
                    {
                        log.debug("...using custom auth factory classname of: " + s1);
                        className = s1;
                    }
                    try
                    {
                        log.debug("...loading auth factory instance");
                        Class class1 = ClassUtils.forName(className);
                        factory = (AuthFactory)class1.newInstance();
                        log.debug("...auth factory '" + className + "' loaded successfully.");
                    }
                    catch(Exception exception)
                    {
                        log.debug("...loading auth factory '" + className + "' failed, see error log.");
                        log.error("Exception loading class: " + exception, exception);
                    }
                }
            }
    }


    private static final Log log;
    public static final String SESSION_AUTHORIZATION = "security.authToken";
    public static final String COOKIE_AUTOLOGIN = "security.authToken.autologin";
    private static String className = "architecture.ee.security.authentication.DefaultAuthFactory";
    private static AuthFactory factory = null;
    protected static EncryptionUtil cipher = null;

    static 
    {
        log = LogFactory.getLog(architecture.ee.web.security.authentication.AuthFactory.class);
        String s = StringUtils.randomString(15);
        try
        {
            cipher = new EncryptionUtil(s);
        }
        catch(Exception exception)
        {
            log.error("Unable to create cipher, keyString = " + s, exception);
        }
    }*/
}
