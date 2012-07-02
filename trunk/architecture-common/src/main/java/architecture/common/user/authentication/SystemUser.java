package architecture.common.user.authentication;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import architecture.common.user.User;


public class SystemUser implements AuthToken, User {
	
	private static final long serialVersionUID = -2371386795832701297L;
	
	public static final String SYSTEM_USERNAME = "SYSTEM";
	
	public Object getPrimaryKey() {		
		return getUserId();
	}

	public int getObjectType() {
		return 1;
	}
	
	public String getUsername() {
		return SYSTEM_USERNAME;
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

	public String getPasswordHash() throws UnAuthorizedException {
		return null;
	}

	public String getPassword() throws UnAuthorizedException {
		return null;
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
		return new HashMap<String, String>();
	}

	public Date getLastLoggedIn() {
		return null;
	}

	public Date getLastProfileUpdate() {
		return null;
	}

	public boolean isEnabled() {
		return false;
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
		return null;
	}

	public int compareTo(User o) {
		return 0;
	}

	public long getUserId() {
		return 0;
	}

	public boolean isAnonymous() {
		return false;
	}

	@Override
	public Object clone() {
		return null;
	}

	public boolean isNew() {
		return false;
	}


	public void setNew(boolean n) {		
	}


	public Serializable getPrimaryKeyObject() {
		return getUserId();
	}


	public void setPrimaryKeyObject(Serializable primaryKeyObj) {		
	}

	public void setCreationDate(Date creationDate) {		
	}

	public void setModifiedDate(Date modifiedDate) {
		
	}

	public int getCachedSize() {
		return 0;
	}

}
