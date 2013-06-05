package architecture.user.security.simple.authentication;

import architecture.common.user.authentication.AuthToken;
import architecture.common.user.authentication.UnAuthorizedException;

public interface AuthProvider {

	boolean isPlainSupported();
	
	boolean isDigestSupported();

	void authenticate(String username, String password) throws UnAuthorizedException ;
	
	AuthToken authenticateAndGetAuthToken(String username, String password) throws UnAuthorizedException;
		
}
