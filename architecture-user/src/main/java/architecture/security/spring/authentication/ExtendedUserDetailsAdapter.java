package architecture.security.spring.authentication;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import architecture.common.user.User;

public class ExtendedUserDetailsAdapter implements UserDetails  {
	
	public ExtendedUserDetailsAdapter(User user) {
		this.user = user;
	}

	private final User user;
	private final List<GrantedAuthority> authorities = Collections.EMPTY_LIST ;

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getPassword() {
		return user.getPassword();
	}

	public String getUsername() {
		return user.getUsername();
	}

	public boolean isAccountNonExpired() {
		return user.isEnabled();
	}

	public boolean isAccountNonLocked() {
		return user.isEnabled();
	}

	public boolean isCredentialsNonExpired() {
		return user.isEnabled();
	}

	public boolean isEnabled() {
		return user.isEnabled();
	}
}
