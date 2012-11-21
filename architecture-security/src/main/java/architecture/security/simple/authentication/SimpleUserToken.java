package architecture.security.simple.authentication;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import architecture.common.model.ModelObjectType;
import architecture.common.user.User;
import architecture.common.user.authentication.AuthToken;
import architecture.common.user.authentication.UnAuthorizedException;

public class SimpleUserToken implements AuthToken, User {

	private long userId = -1L;
	
	private String username ;
	
	private String name ;
	
	private String email;
	
	private Map<String, String> properties = new HashMap<String, String>() ;
	
	public long getUserId() {
		return userId;
	}
	
	public SimpleUserToken(String username) {
		this.userId =  -1L;
		this.username = username;
		this.name = null;
		this.email = null;
	}
	
	public SimpleUserToken(long userId, String username) {
		this.userId = userId;
		this.username = username;
		this.name = null;
		this.email = null;
	}
	
	public SimpleUserToken(long userId, String username, String name) {
		this.userId = userId;
		this.username = username;
		this.name = name;
		this.email = null;
	}

	public Serializable getPrimaryKeyObject() {
		return getUserId();
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public ModelObjectType getModelObjectType() {
		return ModelObjectType.USER ;
	}
	
	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
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
		return email;
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
		return properties ;
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
		return Status.registered ;
	}

	public int compareTo(User o) {
		return 0;
	}


	public boolean isAnonymous() {
		return false;
	}

	public void setCreationDate(Date creationDate) {		

	}

	public void setModifiedDate(Date modifiedDate) {
		
	}

	public int getCachedSize() {
		return 0;
	}
}