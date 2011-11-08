package architecture.ee.security.authentication.impl;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import architecture.ee.security.authentication.AuthToken;

/**
 * @author  donghyuck
 */
public class ExtendedUserDetails extends User implements AuthToken {

	private static final long serialVersionUID = -6654445173418843563L;
	/**
	 * @uml.property  name="userId"
	 */
	private final long userId;
    /**
	 * @uml.property  name="extUser"
	 * @uml.associationEnd  
	 */
    private final architecture.ee.user.User extUser;
    
	public ExtendedUserDetails(architecture.ee.user.User extUser, List<GrantedAuthority> authorities) {		
		super(extUser.getUsername(), extUser.getPasswordHash(), true, true, true, true, authorities);		
		this.extUser = extUser;
		this.userId = this.extUser.getUserId();
	}
	
	

	public ExtendedUserDetails(ExtendedAuthentication authen) {
		super( authen.getUser().getUsername(), 
				authen.isAnonymous() ? (String)authen.getPrincipal() : authen.getUser().getPasswordHash(), 
			   true, 
			   true, 
			   true, 
			   true, 
			   authen.getAuthorities());	
		
		this.extUser = authen.getUser();
		this.userId = extUser.getUserId();
	}



	public long getCreationDate()
    {
        return extUser.getCreationDate() != null ? extUser.getCreationDate().getTime() : -1L;
    }

	public architecture.ee.user.User getUser()
    {
        return extUser;
    }
	
    /**
	 * @return
	 * @uml.property  name="userId"
	 */
    public long getUserId() {
		return userId;
	}

    public boolean isAccountNonExpired()
    {
        return extUser.isEnabled();
    }

    public boolean isAccountNonLocked()
    {
        return extUser.isEnabled();
    }

    public boolean isAnonymous() {
		return userId == -1L;
	}

    public boolean isCredentialsNonExpired()
    {
        return true;
    }
    
    public boolean isEnabled()
    {
        return extUser.isEnabled();
    }
}
