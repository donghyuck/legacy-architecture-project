package architecture.security.simple.authentication.impl;

import architecture.common.user.authentication.AuthToken;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.util.ApplicationHelper;
import architecture.security.simple.authentication.AuthProvider;

public class AuthFactoryImpl implements architecture.security.simple.authentication.AuthFactory.Implementation {
	
	public AuthProvider getAuthProvider() {
		return ApplicationHelper.getComponent(AuthProvider.class);
	}

	public boolean isPlainSupported() {
		return getAuthProvider().isPlainSupported();
	}

	public boolean isDigestSupported() {
		return getAuthProvider().isDigestSupported();
	}

	public AuthToken getAuthToken(String username, String password) throws UnAuthorizedException {
		return getAuthProvider().authenticateAndGetAuthToken(username, password);
	}

}
