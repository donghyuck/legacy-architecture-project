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

/**
 * @author  donghyuck
 */
public class UserModelImpl extends BaseModelObject <User> implements UserModel {

	private Log log = LogFactory.getLog(getClass());

	/**
	 * @uml.property  name="userId"
	 */
	private long userId;
	/**
	 * @uml.property  name="username"
	 */
	private String username;
	/**
	 * @uml.property  name="passwordHash"
	 */
	private String passwordHash;
	/**
	 * @uml.property  name="password"
	 */
	private String password;
	/**
	 * @uml.property  name="name"
	 */
	private String name;
	/**
	 * @uml.property  name="firstName"
	 */
	private String firstName;
	/**
	 * @uml.property  name="lastName"
	 */
	private String lastName;
	/**
	 * @uml.property  name="nameVisible"
	 */
	private boolean nameVisible;
	/**
	 * @uml.property  name="email"
	 */
	private String email;
	/**
	 * @uml.property  name="emailVisible"
	 */
	private boolean emailVisible;
	/**
	 * @uml.property  name="properties"
	 */
	private Map<String, String> properties;
	/**
	 * @uml.property  name="enabled"
	 */
	private boolean enabled;
	/**
	 * @uml.property  name="lastLoggedIn"
	 */
	private Date lastLoggedIn;
	/**
	 * @uml.property  name="lastProfileUpdate"
	 */
	private Date lastProfileUpdate;
	/**
	 * @uml.property  name="external"
	 */
	private boolean external;
	/**
	 * @uml.property  name="federated"
	 */
	private boolean federated;
	/**
	 * @uml.property  name="setNameVisibleSupported"
	 */
	private boolean setNameVisibleSupported;
	/**
	 * @uml.property  name="setPasswordSupported"
	 */
	private boolean setPasswordSupported;
	/**
	 * @uml.property  name="getPasswordHashSupported"
	 */
	private boolean getPasswordHashSupported;
	/**
	 * @uml.property  name="setEmailVisibleSupported"
	 */
	private boolean setEmailVisibleSupported;
	/**
	 * @uml.property  name="setNameSupported"
	 */
	private boolean setNameSupported;
	/**
	 * @uml.property  name="setUsernameSupported"
	 */
	private boolean setUsernameSupported;
	/**
	 * @uml.property  name="setPasswordHashSupported"
	 */
	private boolean setPasswordHashSupported;
	private boolean setPropertyEditSupported;
	private boolean setEmailSuppoted;
	/**
	 * @uml.property  name="status"
	 * @uml.associationEnd  
	 */
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
        
        setCreationDate(user.getCreationDate());
        setModifiedDate(user.getModifiedDate());
        
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
    
	/**
	 * @return
	 * @throws UnauthorizedException
	 * @uml.property  name="password"
	 */
	public String getPassword() throws UnauthorizedException {
		return password;
	}

	/**
	 * @param password
	 * @uml.property  name="password"
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isReadOnly() {
		return false;
	}

	/**
	 * @return
	 * @uml.property  name="email"
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 * @uml.property  name="email"
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return
	 * @uml.property  name="emailVisible"
	 */
	public boolean isEmailVisible() {
		return emailVisible;
	}

	/**
	 * @param emailVisible
	 * @uml.property  name="emailVisible"
	 */
	public void setEmailVisible(boolean emailVisible) {
		this.emailVisible = emailVisible;
	}

	/**
	 * @return
	 * @uml.property  name="enabled"
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 * @uml.property  name="enabled"
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return
	 * @uml.property  name="userId"
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param ID
	 * @uml.property  name="userId"
	 */
	public void setUserId(long ID) {
		this.userId = ID;
	}

	/**
	 * @return
	 * @uml.property  name="lastLoggedIn"
	 */
	public Date getLastLoggedIn() {
		return lastLoggedIn;
	}

	/**
	 * @param lastLoggedIn
	 * @uml.property  name="lastLoggedIn"
	 */
	public void setLastLoggedIn(Date lastLoggedIn) {
		this.lastLoggedIn = lastLoggedIn;
	}

	/**
	 * @return
	 * @uml.property  name="lastProfileUpdate"
	 */
	public Date getLastProfileUpdate() {
		return lastProfileUpdate ;
	}

	/**
	 * @param lastProfileUpdate
	 * @uml.property  name="lastProfileUpdate"
	 */
	public void setLastProfileUpdate(Date lastProfileUpdate) {
		this.lastProfileUpdate = lastProfileUpdate;
	}

	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName() {
		if (lastName != null && firstName != null) {
			StringBuilder builder = new StringBuilder(firstName);
			builder.append(" ").append(lastName);
			return builder.toString();
		} else {
			return name;
		}
	}

	/**
	 * @param name
	 * @uml.property  name="name"
	 */
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

	/**
	 * @return
	 * @uml.property  name="firstName"
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 * @uml.property  name="firstName"
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return
	 * @uml.property  name="lastName"
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 * @uml.property  name="lastName"
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return
	 * @uml.property  name="nameVisible"
	 */
	public boolean isNameVisible() {
		return nameVisible;
	}

	/**
	 * @param nameVisible
	 * @uml.property  name="nameVisible"
	 */
	public void setNameVisible(boolean nameVisible) {
		this.nameVisible = nameVisible;
	}

	/**
	 * @return
	 * @uml.property  name="passwordHash"
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * @param passwordHash
	 * @uml.property  name="passwordHash"
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	/**
	 * @return
	 * @uml.property  name="properties"
	 */
	public Map<String, String> getProperties() {
		if (properties == null)
			properties = new HashMap<String, String>();
		return properties;
	}

	/**
	 * @param properties
	 * @uml.property  name="properties"
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	/**
	 * @return
	 * @uml.property  name="username"
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 * @uml.property  name="username"
	 */
	public void setUsername(String username) {
		this.username = formatUsername(username);
	}

	/**
	 * @return
	 * @uml.property  name="federated"
	 */
	public boolean isFederated() {
		return federated;
	}

	/**
	 * @param federated
	 * @uml.property  name="federated"
	 */
	public void setFederated(boolean federated) {
		this.federated = federated;
	}

	/**
	 * @return
	 * @uml.property  name="external"
	 */
	public boolean isExternal() {
		return external;
	}

	/**
	 * @param external
	 * @uml.property  name="external"
	 */
	public void setExternal(boolean external) {
		this.external = external;
	}

	public boolean isAuthorized(long perm) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param status
	 * @uml.property  name="status"
	 */
	public void setStatus(User.Status status) {
		this.status = status == null ? User.Status.none : status;
	}

	/**
	 * @return
	 * @uml.property  name="status"
	 */
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

	/**
	 * @return
	 * @uml.property  name="setPasswordSupported"
	 */
	public boolean isSetPasswordSupported() {
		return setPasswordSupported;
	}

	/**
	 * @return
	 * @uml.property  name="getPasswordHashSupported"
	 */
	public boolean isGetPasswordHashSupported() {
		return getPasswordHashSupported;
	}

	/**
	 * @return
	 * @uml.property  name="setPasswordHashSupported"
	 */
	public boolean isSetPasswordHashSupported() {
		return setPasswordHashSupported;
	}

	/**
	 * @return
	 * @uml.property  name="setNameSupported"
	 */
	public boolean isSetNameSupported() {
		return setNameSupported;
	}

	/**
	 * @return
	 * @uml.property  name="setUsernameSupported"
	 */
	public boolean isSetUsernameSupported() {
		return setUsernameSupported;
	}

	public boolean isSetEmailSupported() {
		return setEmailSuppoted;
	}

	/**
	 * @return
	 * @uml.property  name="setNameVisibleSupported"
	 */
	public boolean isSetNameVisibleSupported() {
		return setNameVisibleSupported;
	}

	/**
	 * @return
	 * @uml.property  name="setEmailVisibleSupported"
	 */
	public boolean isSetEmailVisibleSupported() {
		return setEmailVisibleSupported;
	}

	public boolean isPropertyEditSupported() {
		return setPropertyEditSupported;
	}

	/**
	 * @param setNameVisibleSupported
	 * @uml.property  name="setNameVisibleSupported"
	 */
	public void setSetNameVisibleSupported(boolean setNameVisibleSupported) {
		this.setNameVisibleSupported = setNameVisibleSupported;
	}

	/**
	 * @param setPasswordSupported
	 * @uml.property  name="setPasswordSupported"
	 */
	public void setSetPasswordSupported(boolean setPasswordSupported) {
		this.setPasswordSupported = setPasswordSupported;
	}

	/**
	 * @param getPasswordHashSupported
	 * @uml.property  name="getPasswordHashSupported"
	 */
	public void setGetPasswordHashSupported(boolean getPasswordHashSupported) {
		this.getPasswordHashSupported = getPasswordHashSupported;
	}

	/**
	 * @param setEmailVisibleSupported
	 * @uml.property  name="setEmailVisibleSupported"
	 */
	public void setSetEmailVisibleSupported(boolean setEmailVisibleSupported) {
		this.setEmailVisibleSupported = setEmailVisibleSupported;
	}

	/**
	 * @param setNameSupported
	 * @uml.property  name="setNameSupported"
	 */
	public void setSetNameSupported(boolean setNameSupported) {
		this.setNameSupported = setNameSupported;
	}

	/**
	 * @param setUsernameSupported
	 * @uml.property  name="setUsernameSupported"
	 */
	public void setSetUsernameSupported(boolean setUsernameSupported) {
		this.setUsernameSupported = setUsernameSupported;
	}

	/**
	 * @param setPasswordHashSupported
	 * @uml.property  name="setPasswordHashSupported"
	 */
	public void setSetPasswordHashSupported(boolean setPasswordHashSupported) {
		this.setPasswordHashSupported = setPasswordHashSupported;
	}

	/**
	 * @param setPropertyEditSupported
	 * @uml.property  name="setPropertyEditSupported"
	 */
	public void setSetPropertyEditSupported(boolean setPropertyEditSupported) {
		this.setPropertyEditSupported = setPropertyEditSupported;
	}

	/**
	 * @param setEmailSuppoted
	 * @uml.property  name="setEmailSuppoted"
	 */
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
