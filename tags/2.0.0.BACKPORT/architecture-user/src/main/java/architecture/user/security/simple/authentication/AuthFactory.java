package architecture.user.security.simple.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import architecture.common.user.authentication.AnonymousUser;
import architecture.common.user.authentication.AuthToken;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.common.util.ImplFactory;

public class AuthFactory {
	
	public static final String AUTHORIZATION_SESSION_KEY = "security.authentication.token" ;
	
	public static final AuthToken AnonymousUserToekn = new AnonymousUser();
	
	public static interface Implementation {
		
		AuthProvider getAuthProvider() ;
		
		boolean isPlainSupported();
		
		boolean isDigestSupported();
		
		AuthToken getAuthToken(String username, String password) throws UnAuthorizedException ;
		
	}
	
	private static Implementation impl = null;
    
    static 
    {
        impl = (Implementation)ImplFactory.loadImplFromKey(AuthFactory.Implementation.class);
    }
    
	public static void logoutUser(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse){
		httpservletrequest.getSession().removeAttribute(AUTHORIZATION_SESSION_KEY);
	}	
	
    public static AuthToken getAuthToken(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse) {
        HttpSession httpsession = httpservletrequest.getSession();
        AuthToken token = (AuthToken)httpsession.getAttribute(AUTHORIZATION_SESSION_KEY);
        if(token != null)
            return token;
        else
        	return AnonymousUserToekn;
    }
    
    public static AuthToken authenticate (String username, String password) throws UnAuthorizedException {		
    	impl.getAuthProvider().authenticate(username, password);		
		return new SimpleUserToken(-1L, username, null);
	}
    
	public static boolean isPlainSupported() {
		return impl.isPlainSupported();
	}

	public static boolean isDigestSupported() {
		return impl.isDigestSupported();
	}
	
	public static AuthToken loginUserAndLoadProperties(String username, String password, boolean newSession, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse ){	    
		
		 AuthToken token = impl.getAuthProvider().authenticateAndGetAuthToken(username, password);		    
		HttpSession httpsession = httpservletrequest.getSession(newSession);
	    httpsession.setAttribute(AUTHORIZATION_SESSION_KEY, token);		    
	    httpsession.setAttribute("IP", httpservletrequest.getRemoteHost() );	
	    return token ;
	}
		
	public static AuthToken loginUser( String username, String password, boolean newSession, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
	        throws UnAuthorizedException
   {		    
		    AuthToken token = authenticate (username, password);		    
		    HttpSession httpsession = httpservletrequest.getSession(newSession);
		    httpsession.setAttribute(AUTHORIZATION_SESSION_KEY, token);		    
		    httpsession.setAttribute("IP", httpservletrequest.getRemoteHost() );		    
	        return token ;
	 }
	
}