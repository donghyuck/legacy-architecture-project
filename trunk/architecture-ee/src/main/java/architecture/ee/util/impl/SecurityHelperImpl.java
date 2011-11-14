package architecture.ee.util.impl;

import org.springframework.security.core.Authentication;

import architecture.ee.security.authentication.AuthToken;
import architecture.ee.user.User;
import architecture.ee.util.SecurityHelper.Implementation;

public class SecurityHelperImpl implements Implementation {

	public boolean isAnonymous(Authentication authen) {
		return false;
	}

	public boolean isApplicaitonUser() {
		return false;
	}

	public AuthToken getAuthToke() {
		return null;
	}

	public User getUser() {
		return null;
	}

}
