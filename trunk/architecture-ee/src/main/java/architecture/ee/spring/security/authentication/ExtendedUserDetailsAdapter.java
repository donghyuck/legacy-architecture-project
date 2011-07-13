package architecture.ee.spring.security.authentication;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import architecture.ee.user.User;

public class ExtendedUserDetailsAdapter implements UserDetails {
	
	private static final long serialVersionUID = -38812967069360635L;

	private final User user;
	private final List<GrantedAuthority> authorities;

	public ExtendedUserDetailsAdapter(User user) {
		this.user = user;
		this.authorities = Collections.emptyList();
	}
	
	public ExtendedUserDetailsAdapter(User user, List<GrantedAuthority> authorities) {
		this.user = user;
		this.authorities = authorities;
	}

	public Collection<GrantedAuthority> getAuthorities() {
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

	public long getUserId(){
		return user.getUserId();
	}
	
	public Date getCreationDate(){
		return user.getCreationDate();
	}
	
	public User getUser(){
		return user;
	}
}
