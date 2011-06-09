package architecture.ee.user;

public interface User {

	enum Status {
		
		none(0),
		approved(1),
		rejected(2),
		validated(3),
		registered(4);
		
		int id ;
		
		private Status(int id) {
			this.id = id;
		}		
	}
	
    public static final String EMAIL_VISIBILITY_DEFAULT = "user.default.displayEmailAddress";
    public static final String EMAIL_VISIBILITY_ALLOW_TOGGLE = "user.allowEmailVisibilityToggle";
    public static final String NAME_VISIBILITY_ALLOW_TOGGLE = "user.allowNameVisibilityToggle";
    
    public abstract long getID();

    public abstract String getUsername();

    public abstract String getName();

    public abstract String getFirstName();

    public abstract String getLastName();

    public abstract boolean isNameVisible();

    public abstract String getPasswordHash() throws UnauthorizedException;

    public abstract String getPassword() throws UnauthorizedException;

    public abstract String getEmail();

    public abstract boolean isEmailVisible();

    public abstract Date getCreationDate();

    public abstract Date getModifiedDate();

    public abstract Map getProperties();

    public abstract Permissions getPermissions(AuthToken authtoken);

    public abstract boolean isAuthorized(long l);

    public abstract Date getLastLoggedIn();

    public abstract Date getLastProfileUpdate();

    public abstract boolean isEnabled();

    public abstract boolean isFederated();

    public abstract boolean isExternal();

    public abstract boolean isSetPasswordSupported();

    public abstract boolean isGetPasswordHashSupported();

    public abstract boolean isSetPasswordHashSupported();

    public abstract boolean isSetNameSupported();

    public abstract boolean isSetUsernameSupported();

    public abstract boolean isSetEmailSupported();

    public abstract boolean isSetNameVisibleSupported();

    public abstract boolean isSetEmailVisibleSupported();

    public abstract boolean isPropertyEditSupported();

    public abstract Status getStatus();

    public abstract boolean isAnonymous();
    
}
