package architecture.ee.model.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.util.StringUtils;
import architecture.ee.model.ModelConstants;
import architecture.ee.model.UserModel;
import architecture.ee.security.authentication.UnauthorizedException;
import architecture.ee.user.User;
import architecture.ee.util.ApplicationHelper;

public class UserModelImpl extends AbstractModelObject <User> implements UserModel {

	private Log log = LogFactory.getLog(getClass());

	private long userId;
	private String username;
	private String passwordHash;
	private String password;
	private String name;
	private String firstName;
	private String lastName;
	private boolean nameVisible;
	private String email;
	private boolean emailVisible;
	private Date creationDate;
	private Date modifiedDate;
	private Map<String, String> properties;
	private boolean enabled;
	private Date lastLoggedIn;
	private Date lastProfileUpdate;
	private boolean external;
	private boolean federated;
	private boolean setNameVisibleSupported;
	private boolean setPasswordSupported;
	private boolean getPasswordHashSupported;
	private boolean setEmailVisibleSupported;
	private boolean setNameSupported;
	private boolean setUsernameSupported;
	private boolean setPasswordHashSupported;
	private boolean setPropertyEditSupported;
	private boolean setEmailSuppoted;
	private User.Status status;
		
	/**
	 * ID 값이 -2 인 경우 아직 생성되지 않은 계정을 의미!
	 * -1 인 경우는 익명 사용자를 의미.
	 */
    public UserModelImpl()
    {
        userId = -2L;
        username = null;
        password = null;
        name = null;
        firstName = null;
        lastName = null;
        nameVisible = true;
        email = null;
        emailVisible = false;
        creationDate = null;
        modifiedDate = null;
        properties = null;
        enabled = true;
        lastLoggedIn = null;
        lastProfileUpdate = null;
        external = false;
        federated = false;
        setNameVisibleSupported = true;
        setPasswordSupported = true;
        getPasswordHashSupported = true;
        setEmailVisibleSupported = true;
        setNameSupported = true;
        setUsernameSupported = true;
        setPasswordHashSupported = true;
        setPropertyEditSupported = true;
        setEmailSuppoted = true;
        status = null;
    }
    
    public UserModelImpl(String userName)
    {
        userId = -2L;
        username = null;
        password = null;
        name = null;
        firstName = null;
        lastName = null;
        nameVisible = true;
        email = null;
        emailVisible = false;
        creationDate = null;
        modifiedDate = null;
        properties = null;
        enabled = true;
        lastLoggedIn = null;
        lastProfileUpdate = null;
        external = false;
        federated = false;
        setNameVisibleSupported = true;
        setPasswordSupported = true;
        getPasswordHashSupported = true;
        setEmailVisibleSupported = true;
        setNameSupported = true;
        setUsernameSupported = true;
        setPasswordHashSupported = true;
        setPropertyEditSupported = true;
        setEmailSuppoted = true;
        status = null;
        username = formatUsername(userName);
    }
    

    public UserModelImpl(String userName, String password, String email, String name)
    {
        userId = -2L;
        username = null;
        this.password = null;
        this.name = null;
        firstName = null;
        lastName = null;
        nameVisible = true;
        this.email = null;
        emailVisible = false;
        creationDate = null;
        modifiedDate = null;
        properties = null;
        enabled = true;
        lastLoggedIn = null;
        lastProfileUpdate = null;
        external = false;
        federated = false;
        setNameVisibleSupported = true;
        setPasswordSupported = true;
        getPasswordHashSupported = true;
        setEmailVisibleSupported = true;
        setNameSupported = true;
        setUsernameSupported = true;
        setPasswordHashSupported = true;
        setPropertyEditSupported = true;
        setEmailSuppoted = true;
        status = null;
        username = formatUsername(userName);
        this.password = password;
        this.email = email;
        this.name = name;
    }
    
    public UserModelImpl(String userName, String password, String email, String name, boolean emailVisible, boolean nameVisible, Map<String, String> props)
    {
        userId = -2L;
        username = null;
        this.password = null;
        this.name = null;
        firstName = null;
        lastName = null;
        this.nameVisible = true;
        this.email = null;
        this.emailVisible = false;
        creationDate = null;
        modifiedDate = null;
        properties = null;
        enabled = true;
        lastLoggedIn = null;
        lastProfileUpdate = null;
        external = false;
        federated = false;
        setNameVisibleSupported = true;
        setPasswordSupported = true;
        getPasswordHashSupported = true;
        setEmailVisibleSupported = true;
        setNameSupported = true;
        setUsernameSupported = true;
        setPasswordHashSupported = true;
        setPropertyEditSupported = true;
        setEmailSuppoted = true;
        status = null;
        username = formatUsername(userName);
        this.password = password;
        this.email = email;
        this.name = name;
        this.nameVisible = nameVisible;
        this.emailVisible = emailVisible;
        properties = props;        
    }

    public UserModelImpl(String userName, String password, String email, String firstName, String lastName, boolean emailVisible, boolean nameVisible, Map<String, String> props)
    {
        userId = -2L;
        username = null;
        this.password = null;
        name = null;
        this.firstName = null;
        this.lastName = null;
        this.nameVisible = true;
        this.email = null;
        this.emailVisible = false;
        creationDate = null;
        modifiedDate = null;
        properties = null;
        enabled = true;
        lastLoggedIn = null;
        lastProfileUpdate = null;
        external = false;
        federated = false;
        setNameVisibleSupported = true;
        setPasswordSupported = true;
        getPasswordHashSupported = true;
        setEmailVisibleSupported = true;
        setNameSupported = true;
        setUsernameSupported = true;
        setPasswordHashSupported = true;
        setPropertyEditSupported = true;
        setEmailSuppoted = true;
        status = null;
        username = formatUsername(userName);
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nameVisible = nameVisible;
        this.emailVisible = emailVisible;
        properties = props;
    }
    

    public UserModelImpl(String userName, String password, String email)
    {
        this(userName, password, email, null);
    }

    public UserModelImpl(long userId)
    {
        this.userId = -2L;
        username = null;
        password = null;
        name = null;
        firstName = null;
        lastName = null;
        nameVisible = true;
        email = null;
        emailVisible = false;
        creationDate = null;
        modifiedDate = null;
        properties = null;
        enabled = true;
        lastLoggedIn = null;
        lastProfileUpdate = null;
        external = false;
        federated = false;
        setNameVisibleSupported = true;
        setPasswordSupported = true;
        getPasswordHashSupported = true;
        setEmailVisibleSupported = true;
        setNameSupported = true;
        setUsernameSupported = true;
        setPasswordHashSupported = true;
        setPropertyEditSupported = true;
        setEmailSuppoted = true;
        status = null;
        this.userId = userId;
    }

    public UserModelImpl(User user)
    {
        userId = -2L;
        username = null;
        password = null;
        name = null;
        firstName = null;
        lastName = null;
        nameVisible = true;
        email = null;
        emailVisible = false;
        creationDate = null;
        modifiedDate = null;
        properties = null;
        enabled = true;
        lastLoggedIn = null;
        lastProfileUpdate = null;
        external = false;
        federated = false;
        setNameVisibleSupported = true;
        setPasswordSupported = true;
        getPasswordHashSupported = true;
        setEmailVisibleSupported = true;
        setNameSupported = true;
        setUsernameSupported = true;
        setPasswordHashSupported = true;
        setPropertyEditSupported = true;
        setEmailSuppoted = true;
        status = null;
        if(null == user)
            return;
        userId = user.getUserId();
        username = formatUsername(user.getUsername());
        if(user.getFirstName() != null && user.getLastName() != null)
        {
            firstName = user.getFirstName();
            lastName = user.getLastName();
        } else
        {
            name = user.getName();
        }
        email = user.getEmail();
        nameVisible = user.isNameVisible();
        emailVisible = user.isEmailVisible();
        enabled = user.isEnabled();
        creationDate = user.getCreationDate();
        modifiedDate = user.getModifiedDate();
        lastLoggedIn = user.getLastLoggedIn();
        lastProfileUpdate = user.getLastProfileUpdate();
        external = user.isExternal();
        federated = user.isFederated();
        status = user.getStatus();
        setEmailSuppoted = user.isSetEmailSupported();
        setEmailVisibleSupported = user.isSetEmailVisibleSupported();
        setNameSupported = user.isSetNameSupported();
        setNameVisibleSupported = user.isSetNameVisibleSupported();
        getPasswordHashSupported = user.isGetPasswordHashSupported();
        setPasswordHashSupported = user.isSetPasswordHashSupported();
        setUsernameSupported = user.isSetUsernameSupported();
        setPropertyEditSupported = user.isPropertyEditSupported();
        setPasswordSupported = user.isSetPasswordSupported();
        if(user.getProperties() != null)
            properties = new HashMap<String, String>(user.getProperties());
        try
        {
            passwordHash = user.getPasswordHash();
            password = user.getPassword();
        }
        catch(UnauthorizedException e)
        {
            log.debug((new StringBuilder()).append("Couldn't copy password or password hash to newly constructed template for ").append(user).toString());
        }
    }
    
	public String getPassword() throws UnauthorizedException {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isReadOnly() {
		return false;
	}

	public Date getCreationDate() {
		return creationDate ;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEmailVisible() {
		return emailVisible;
	}

	public void setEmailVisible(boolean emailVisible) {
		this.emailVisible = emailVisible;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long ID) {
		this.userId = ID;
	}

	public Date getLastLoggedIn() {
		return lastLoggedIn;
	}

	public void setLastLoggedIn(Date lastLoggedIn) {
		this.lastLoggedIn = lastLoggedIn;
	}

	public Date getLastProfileUpdate() {
		return lastProfileUpdate ;
	}

	public void setLastProfileUpdate(Date lastProfileUpdate) {
		this.lastProfileUpdate = lastProfileUpdate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modifiedDate = modificationDate;
	}

	public String getName() {
		if (lastName != null && firstName != null) {
			StringBuilder builder = new StringBuilder(firstName);
			builder.append(" ").append(lastName);
			return builder.toString();
		} else {
			return name;
		}
	}

	public void setName(String name) {
		if (lastName != null && firstName != null && name != null) {
			name = name.trim();
			int index = name.indexOf(" ");
			if (index > -1) {
				firstName = name.substring(0, index);
				lastName = name.substring(index + 1, name.length());
				lastName = lastName.trim();
				this.name = null;
			} else {
				firstName = null;
				lastName = null;
				this.name = name;
			}
		} else {
			this.name = name;
		}
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isNameVisible() {
		return nameVisible;
	}

	public void setNameVisible(boolean nameVisible) {
		this.nameVisible = nameVisible;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public Map<String, String> getProperties() {
		if (properties == null)
			properties = new HashMap<String, String>();
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = formatUsername(username);
	}

	public boolean isFederated() {
		return federated;
	}

	public void setFederated(boolean federated) {
		this.federated = federated;
	}

	public boolean isExternal() {
		return external;
	}

	public void setExternal(boolean external) {
		this.external = external;
	}

	public boolean isAuthorized(long perm) {
		throw new UnsupportedOperationException();
	}

	public void setStatus(User.Status status) {
		this.status = status == null ? User.Status.none : status;
	}

	public User.Status getStatus() {
		return status;
	}

	public String toString() {
		return String
				.format("User: %s [%s] [%s] ",
						new Object[] { Long.valueOf(getUserId()), getUsername(),
								getEmail() });
	}

	public int hashCode() {
		return (int) userId;
	}

	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object != null && (object instanceof User)) {
			User user = (User) object;
			return userId == user.getUserId();
		} else {
			return false;
		}
	}

	public boolean isSetPasswordSupported() {
		return setPasswordSupported;
	}

	public boolean isGetPasswordHashSupported() {
		return getPasswordHashSupported;
	}

	public boolean isSetPasswordHashSupported() {
		return setPasswordHashSupported;
	}

	public boolean isSetNameSupported() {
		return setNameSupported;
	}

	public boolean isSetUsernameSupported() {
		return setUsernameSupported;
	}

	public boolean isSetEmailSupported() {
		return setEmailSuppoted;
	}

	public boolean isSetNameVisibleSupported() {
		return setNameVisibleSupported;
	}

	public boolean isSetEmailVisibleSupported() {
		return setEmailVisibleSupported;
	}

	public boolean isPropertyEditSupported() {
		return setPropertyEditSupported;
	}

	public void setSetNameVisibleSupported(boolean setNameVisibleSupported) {
		this.setNameVisibleSupported = setNameVisibleSupported;
	}

	public void setSetPasswordSupported(boolean setPasswordSupported) {
		this.setPasswordSupported = setPasswordSupported;
	}

	public void setGetPasswordHashSupported(boolean getPasswordHashSupported) {
		this.getPasswordHashSupported = getPasswordHashSupported;
	}

	public void setSetEmailVisibleSupported(boolean setEmailVisibleSupported) {
		this.setEmailVisibleSupported = setEmailVisibleSupported;
	}

	public void setSetNameSupported(boolean setNameSupported) {
		this.setNameSupported = setNameSupported;
	}

	public void setSetUsernameSupported(boolean setUsernameSupported) {
		this.setUsernameSupported = setUsernameSupported;
	}

	public void setSetPasswordHashSupported(boolean setPasswordHashSupported) {
		this.setPasswordHashSupported = setPasswordHashSupported;
	}

	public void setSetPropertyEditSupported(boolean setPropertyEditSupported) {
		this.setPropertyEditSupported = setPropertyEditSupported;
	}

	public void setSetEmailSuppoted(boolean setEmailSuppoted) {
		this.setEmailSuppoted = setEmailSuppoted;
	}

	public boolean isAnonymous() {
		return userId == -1L;
	}

	public int compareTo(User user) {
		long pk = user.getUserId();

		if (getUserId() < pk) {
			return -1;
		} else if (getUserId() > pk) {
			return 1;
		} else {
			return 0;
		}
	}


	public Serializable getPrimaryKeyObject() {
		return getUserId();
	}

	public void setPrimaryKeyObject(Serializable primaryKeyObj) {
		setUserId(((Long)primaryKeyObj).longValue());		
	}

	public int getObjectType() {
		return ModelConstants.USER;
	}


	@Override
	public Object clone() {
		return null;
	}

    private static final char USERNAME_DISALLOWED_CHARS[] = {
        '/', ';', '#', ',', ':'
    };
	
    public static String formatUsername(String username)
    {
        if(username == null)
            return null;
                        
        if(ApplicationHelper.getApplicationBooleanProperty("username.allowWhiteSpace", false))
        {
            String formattedUsername = "";
            Pattern p = Pattern.compile("\\s+");
            Matcher m = p.matcher(username.trim());
            if(m.find())
            {
                formattedUsername = m.replaceAll(" ");
                username = formattedUsername;
            }
        } else
        {
            username = StringUtils.stripToEmpty(username);
        }
        return username;
    }
    
    public static boolean isUsernameValid(String username)
    {
        return username != null && StringUtils.indexOfAny(username, USERNAME_DISALLOWED_CHARS) == -1 && username.length() <= 100;
    }


}
