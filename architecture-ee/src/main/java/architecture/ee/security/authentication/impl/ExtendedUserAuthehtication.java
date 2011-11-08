package architecture.ee.security.authentication.impl;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;

import architecture.ee.security.authentication.AnonymousUser;
import architecture.ee.security.authentication.AuthToken;
import architecture.ee.user.User;

/**
 * @author  donghyuck
 */
public class ExtendedUserAuthehtication implements ExtendedAuthentication {
	
	/**
	 * @uml.property  name="user"
	 * @uml.associationEnd  
	 */
	private final User user;
	
	public ExtendedUserAuthehtication(User u) {
        if(u == null)
        	user = new AnonymousUser();
        else
        	user = u;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		return Collections.EMPTY_LIST;
	}

	public AuthToken getAuthToken() {
		return new AuthToken() {

            public long getUserId()
            {
                return user.getUserId();
            }

            public String getUsername() {
				return user.getUsername();
			}

			public boolean isAnonymous()
            {
                return user.isAnonymous();
            }
			
        };
	}

	public Object getCredentials() {
		return null;
	}

	public Object getDetails() {
		return user;
	}

	public String getName() {
		
		return user.getName();
	}

	public Object getPrincipal() {
		return user;
	}

	/**
	 * @return
	 * @uml.property  name="user"
	 */
	public User getUser() {
		return user;
	}

	public long getUserId() {
		return 0;
	}

    public boolean isAnonymous()
    {
        return user.isAnonymous();
    }

    public boolean isAuthenticated() {
		return true;
	}

	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		
	}

	public String toString()
    {
        if(null == user)
            return "[NULL]";
        else
            return (new StringBuilder()).append("[").append(user.getUserId()).append(":").append(user.getUsername()).append("] ").append(user.getName()).toString();
    }


}
