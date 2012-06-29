package architecture.security.user.spring.authentication;

import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

public class ExtendedLogoutFilter extends LogoutFilter {

	public ExtendedLogoutFilter(String logoutSuccessUrl) {
		super(logoutSuccessUrl, new SecurityContextLogoutHandler());
	}
	
	public ExtendedLogoutFilter(String logoutSuccessUrl, LogoutHandler... handlers) {
		super(logoutSuccessUrl, handlers);
	}

}
