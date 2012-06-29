package architecture.security.user.spring.authentication;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;

import architecture.common.user.User;
import architecture.common.user.authentication.AnonymousUser;
import architecture.common.user.authentication.AuthToken;

/**
 * @author  donghyuck
 */
public class ExtendedUserAuthehtication implements ExtendedAuthentication {
	
	/**
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
