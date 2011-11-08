package architecture.ee.security.authentication.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import architecture.ee.user.User;

/**
 * @author  donghyuck
 */
public class ExtendedUserDetailsAdapter implements UserDetails {
	
	private static final long serialVersionUID = -38812967069360635L;

	/**
	 * @uml.property  name="user"
	 * @uml.associationEnd  
	 */
	private final User user;
	/**
	 * @uml.property  name="authorities"
	 */
	private final List<GrantedAuthority> authorities;

	public ExtendedUserDetailsAdapter(User user) {
		this.user = user;
		this.authorities = Collections.emptyList();
	}
	
	public ExtendedUserDetailsAdapter(User user, List<GrantedAuthority> authorities) {
		this.user = user;
		this.authorities = authorities;
	}

	/**
	 * @return
	 * @uml.property  name="authorities"
	 */
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Date getCreationDate(){
		return user.getCreationDate();
	}

	public String getPassword() {
		return user.getPassword();
	}

	/**
	 * @return
	 * @uml.property  name="user"
	 */
	public User getUser(){
		return user;
	}

	public long getUserId(){
		return user.getUserId();
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
