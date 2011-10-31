package architecture.ee.security.authentication.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

public class ExtendedSecurityContext implements SecurityContext {

    private Log log = LogFactory.getLog(getClass());
    private Authentication auth;
	
    public Authentication getAuthentication() {
        return auth;
    }
    
    public int hashCode() {
        if (this.auth == null) {
            return -1;
        } else {
            return this.auth.hashCode();
        }
    }
    
    public void setAuthentication(Authentication authentication) {
        this.auth = authentication;
    }


    public void setAuthentication(Authentication auth, boolean bypass)
    {
        if(!bypass && (auth instanceof SystemAuthentication))
        {
            log.warn("Ignoring unchecked attempt to establish system authentication.", new RuntimeException());
            return;
        } else
        {
            this.auth = auth;
            return;
        }
    }
    
    public String toString()
    {
        StringBuilder buffer = new StringBuilder(32);
        if(auth == null)
        {
            buffer.append("[null]");
        } else
        {
            if(auth instanceof ExtendedAuthentication)
                buffer.append("[ExtendedAuthentication]:");
            else
            if(auth instanceof ExtendedAnonymousAuthenticationToken)
                buffer.append("[Anonymous]:");
            else
            if(auth instanceof SystemAuthentication)
                buffer.append("[SystemAuthentication]:");
            else
                buffer.append("[Authentication]:");
            buffer.append(auth.toString());
        }
        return buffer.toString();
    }
}
