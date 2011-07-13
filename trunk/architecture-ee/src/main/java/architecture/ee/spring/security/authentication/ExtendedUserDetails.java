package architecture.ee.spring.security.authentication;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import architecture.ee.security.AuthToken;

public class ExtendedUserDetails extends User implements AuthToken {
	
    private final long userId;
    private final architecture.ee.user.User extUser;
    
	public ExtendedUserDetails(architecture.ee.user.User extUser, List<GrantedAuthority> authorities) {		
		super(extUser.getUsername(), extUser.getPasswordHash(), true, true, true, true, authorities);		
		this.extUser = extUser;
		this.userId = this.extUser.getUserId();
	}

	public long getUserId() {
		return userId;
	}

	public boolean isAnonymous() {
		return userId == -1L;
	}
	
    public architecture.ee.user.User getUser()
    {
        return extUser;
    }

    public boolean isAccountNonExpired()
    {
        return extUser.isEnabled();
    }

    public boolean isAccountNonLocked()
    {
        return extUser.isEnabled();
    }

    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    public boolean isEnabled()
    {
        return extUser.isEnabled();
    }
    
    public long getCreationDate()
    {
        return extUser.getCreationDate() != null ? extUser.getCreationDate().getTime() : -1L;
    }
}
