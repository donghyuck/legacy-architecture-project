package architecture.ee.security.authentication;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import architecture.ee.model.internal.BaseModelImpl;
import architecture.ee.security.AuthToken;
import architecture.ee.security.UnauthorizedException;
import architecture.ee.user.User;

public class AnonymousUser extends BaseModelImpl<User> implements AuthToken, User, Serializable {

	private static final long serialVersionUID = 2198341141233384287L;

	public static final long ANONYMOUS_ID = -1L;
	
	public static final String ANONYMOUS_USERNAME = "ANONYMOUS";
	
	private String username ;
	
	public AnonymousUser() {
		username = ANONYMOUS_USERNAME ;
	}

	public AnonymousUser(String username, String password) {
	}
	
	public String getUsername() {
		return username;
	}

	public String getName() {
		return null;
	}

	public String getFirstName() {
		return null;
	}

	public String getLastName() {
		return null;
	}

	public boolean isNameVisible() {
		return false;
	}

	public String getPasswordHash() throws UnauthorizedException {
		return "";
	}

	public String getPassword() throws UnauthorizedException {
		return "";
	}

	public String getEmail() {
		return null;
	}

	public boolean isEmailVisible() {
		return false;
	}

	public Date getCreationDate() {
		return null;
	}

	public Date getModifiedDate() {
		return null;
	}

	public Map<String, String> getProperties() {
		return new HashMap<String, String>(0);
	}

	public Date getLastLoggedIn() {
		return null;
	}

	public Date getLastProfileUpdate() {
		return null;
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isFederated() {
		return false;
	}

	public boolean isExternal() {
		return false;
	}

	public boolean isSetPasswordSupported() {
		return false;
	}

	public boolean isGetPasswordHashSupported() {
		return false;
	}

	public boolean isSetPasswordHashSupported() {
		return false;
	}

	public boolean isSetNameSupported() {
		return false;
	}

	public boolean isSetUsernameSupported() {
		return false;
	}

	public boolean isSetEmailSupported() {
		return false;
	}

	public boolean isSetNameVisibleSupported() {
		return false;
	}

	public boolean isSetEmailVisibleSupported() {
		return false;
	}

	public boolean isPropertyEditSupported() {
		return false;
	}

	public Status getStatus() {
		return Status.none;
	}

	public long getPrimaryKey() {
		return getUserId();
	}

	public String toXmlString() {
		return null;
	}

	public int compareTo(User o) {
		return 0;
	}
	
	

	public long getUserId() {
		return ANONYMOUS_ID;
	}

	public boolean isAnonymous() {
		return true;
	}

	@Override
	public Object clone() {
		return new AnonymousUser();
	}

	@Override
	public void setPrimaryKey(long pk) {

	}

}
