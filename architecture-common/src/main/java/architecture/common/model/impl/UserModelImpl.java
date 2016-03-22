/*
 * Copyright 2012 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.common.model.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import architecture.common.cache.CacheSizes;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.bootstrap.Bootstrap;
import architecture.common.model.UserModel;
import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.json.CustomJsonDateDeserializer;
import architecture.common.model.json.CustomJsonDateSerializer;
import architecture.common.model.support.EntityModelObjectSupport;
import architecture.common.user.Company;
import architecture.common.user.User;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.common.util.StringUtils;

/**
 * @author donghyuck
 */

public class UserModelImpl extends EntityModelObjectSupport implements UserModel {

    private Log log = LogFactory.getLog(getClass());

    private long companyId;

    private Company company;

    /**
     * 사용자 객체 아이디 값 (내부적으로 사용하는 시스템 아이디)
     */
    private long userId;

    /**
     * 사용자 아이디 (일반적인 로그인 아이디를 의미)
     */
    private String username;

    /**
     * 단방향 암호화 처리된 패스워드
     */
    private String passwordHash;

    /**
     * 패스워드 평문
     */
    private String password;

    /**
     * 전체 이름
     */
    private String name;

    /**
     * 이름
     */

    private String firstName;

    /**
     * 성
     */
    private String lastName;

    /**
     * 이름 공개 여부
     */
    private boolean nameVisible;

    /**
     * 전자메일
     */
    private String email;

    /**
     * 메일 공개 여부
     */
    private boolean emailVisible;

    /**
     * 추가 프로퍼티 값
     */
    private Map<String, Object> profile;

    /**
     * 사용 여부
     */
    private boolean enabled;

    /**
     * 마지막 로그인 시간
     */
    private Date lastLoggedIn;

    /**
     * 마지막으로 프로파일을 수정한 시간
     */
    private Date lastProfileUpdate;

    /**
     * 외부 계정 여부
     */
    private boolean external;

    /**
     * 연합 여부 (예를 들어 외부 시스템과 연계를 통하여 생성된 계정 여부 )
     */
    private boolean federated;

    /**
     * 이름 공개 여부 수정 지원 여부
     */
    private boolean setNameVisibleSupported;

    /**
     * 패스워드 변경 지원 여부
     */
    private boolean setPasswordSupported;

    /**
     * 암호화된 페스워드 제공 여부
     */
    private boolean getPasswordHashSupported;

    /**
     * 메일 주소 공겨 여부 수정 지원 여부
     */
    private boolean setEmailVisibleSupported;

    /**
     * 이름 변경 지원 여부
     */
    private boolean setNameSupported;

    /**
     * 사용자 아이디 변경 지원 여부
     */
    private boolean setUsernameSupported;

    /**
     * 암호화된 패스워드 설정 지원 여부
     */
    private boolean setPasswordHashSupported;

    /**
     * 사용자 프로퍼티 값 변경 지원 여부
     */
    private boolean setPropertyEditSupported;

    /**
     * 메일 주소 변경 지원 여부
     */
    private boolean setEmailSuppoted;

    private boolean profileSuppoted = false;

    private boolean setProfileEditSuppoted = false;

    private User.Status status;

    /**
     * ID 값이 -2 인 경우 아직 생성되지 않은 계정을 의미! -1 인 경우는 익명 사용자를 의미.
     */
    public UserModelImpl() {
	companyId = -1L;
	company = null;
	userId = -2L;
	username = null;
	password = null;
	name = null;
	firstName = null;
	lastName = null;
	nameVisible = true;
	email = null;
	emailVisible = false;
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
	profileSuppoted = true;
	setProfileEditSuppoted = true;
	status = null;
    }

    public UserModelImpl(String username) {
	companyId = -1L;
	company = null;
	userId = -2L;
	this.username = null;
	password = null;
	name = null;
	firstName = null;
	lastName = null;
	nameVisible = true;
	email = null;
	emailVisible = false;
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
	profileSuppoted = true;
	setProfileEditSuppoted = true;
	this.username = formatUsername(username);
	status = null;
    }

    public UserModelImpl(String username, String password, String email, String name) {
	companyId = -1L;
	company = null;
	userId = -2L;
	this.username = null;
	this.password = null;
	this.name = null;
	firstName = null;
	lastName = null;
	nameVisible = true;
	this.email = null;
	emailVisible = false;
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
	profileSuppoted = true;
	setProfileEditSuppoted = true;
	this.username = formatUsername(username);
	this.password = password;
	this.email = email;
	this.name = name;
	status = null;
    }

    public UserModelImpl(String username, String password, String email, String name, boolean emailVisible,
	    boolean nameVisible, Map<String, String> props) {
	companyId = -1L;
	company = null;
	this.userId = -2L;
	this.username = null;
	this.password = null;
	this.name = null;
	firstName = null;
	lastName = null;
	this.nameVisible = true;
	this.email = null;
	this.emailVisible = false;
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
	profileSuppoted = true;
	setProfileEditSuppoted = true;
	this.username = formatUsername(username);
	this.password = password;
	this.email = email;
	this.name = name;
	this.nameVisible = nameVisible;
	this.emailVisible = emailVisible;
	setProperties(props);
	status = null;
    }

    public UserModelImpl(String userName, String password, String email, String firstName, String lastName,
	    boolean emailVisible, boolean nameVisible, Map<String, String> props) {
	companyId = -1L;
	company = null;
	userId = -2L;
	username = null;
	this.password = null;
	name = null;
	this.firstName = null;
	this.lastName = null;
	this.nameVisible = true;
	this.email = null;
	this.emailVisible = false;
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
	profileSuppoted = true;
	setProfileEditSuppoted = true;
	username = formatUsername(userName);
	this.password = password;
	this.email = email;
	this.firstName = firstName;
	this.lastName = lastName;
	this.nameVisible = nameVisible;
	this.emailVisible = emailVisible;
	setProperties(props);
	status = null;
    }

    public UserModelImpl(String userName, String password, String email) {
	this(userName, password, email, null);
    }

    public UserModelImpl(long userId) {
	companyId = -1L;
	company = null;
	this.userId = -2L;
	username = null;
	password = null;
	name = null;
	firstName = null;
	lastName = null;
	nameVisible = true;
	email = null;
	emailVisible = false;

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
	profileSuppoted = true;
	setProfileEditSuppoted = true;
	this.userId = userId;
	status = null;
    }

    public UserModelImpl(User user) {
	companyId = -1L;
	company = null;
	userId = -2L;
	username = null;
	password = null;
	name = null;
	firstName = null;
	lastName = null;
	nameVisible = true;
	email = null;
	emailVisible = false;

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
	profileSuppoted = true;
	setProfileEditSuppoted = true;

	if (null == user)
	    return;
	userId = user.getUserId();
	username = formatUsername(user.getUsername());
	name = user.getName();
	email = user.getEmail();
	nameVisible = user.isNameVisible();
	emailVisible = user.isEmailVisible();

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
	profileSuppoted = user.isProfileSupported();
	setProfileEditSuppoted = user.isProfileEditSupported();

	companyId = user.getCompanyId();
	company = user.getCompany();

	if (user.getProperties() != null)
	    profile = user.getProfile();

	if (user.getProperties() != null)
	    setProperties(user.getProperties());
	try {
	    passwordHash = user.getPasswordHash();
	    password = user.getPassword();
	} catch (UnAuthorizedException e) {
	    log.debug((new StringBuilder())
		    .append("Couldn't copy password or password hash to newly constructed template for ").append(user)
		    .toString());
	}
	status = null;
    }

    /**
     * @return
     * @throws UnAuthorizedException
     */
    @JsonIgnore
    public String getPassword() throws UnAuthorizedException {
	return password;
    }

    /**
     * @param password
     */
    @JsonIgnore
    public void setPassword(String password) {
	this.password = password;
    }

    public boolean isReadOnly() {
	return false;
    }

    /**
     * @return
     */
    public String getEmail() {
	return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
	this.email = email;
    }

    /**
     * @return
     */
    public boolean isEmailVisible() {
	return emailVisible;
    }

    /**
     * @param emailVisible
     */
    public void setEmailVisible(boolean emailVisible) {
	this.emailVisible = emailVisible;
    }

    /**
     * @return
     */
    public boolean isEnabled() {
	return enabled;
    }

    /**
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }

    /**
     * @return
     */
    public long getUserId() {
	return userId;
    }

    /**
     * @param ID
     */
    public void setUserId(long ID) {
	this.userId = ID;
    }

    /**
     * @return
     */
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    public Date getLastLoggedIn() {
	return lastLoggedIn;
    }

    /**
     * @param lastLoggedIn
     */
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    public void setLastLoggedIn(Date lastLoggedIn) {
	this.lastLoggedIn = lastLoggedIn;
    }

    /**
     * @return
     */
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    public Date getLastProfileUpdate() {
	return lastProfileUpdate;
    }

    /**
     * @param lastProfileUpdate
     */
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    public void setLastProfileUpdate(Date lastProfileUpdate) {
	this.lastProfileUpdate = lastProfileUpdate;
    }

    /**
     * @return
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
     */
    public String getFirstName() {
	return firstName;
    }

    /**
     * @param firstName
     */
    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    /**
     * @return
     */
    public String getLastName() {
	return lastName;
    }

    /**
     * @param lastName
     */
    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    /**
     * @return
     */
    public boolean isNameVisible() {
	return nameVisible;
    }

    /**
     * @param nameVisible
     */
    public void setNameVisible(boolean nameVisible) {
	this.nameVisible = nameVisible;
    }

    /**
     * @return
     */
    @JsonIgnore
    public String getPasswordHash() {
	return passwordHash;
    }

    /**
     * @param passwordHash
     */
    @JsonIgnore
    public void setPasswordHash(String passwordHash) {
	this.passwordHash = passwordHash;
    }

    /**
     * @return
     */
    public String getUsername() {
	return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
	this.username = formatUsername(username);
    }

    /**
     * @return
     */
    public boolean isFederated() {
	return federated;
    }

    /**
     * @param federated
     */
    public void setFederated(boolean federated) {
	this.federated = federated;
    }

    /**
     * @return
     */
    public boolean isExternal() {
	return external;
    }

    /**
     * @param external
     */
    public void setExternal(boolean external) {
	this.external = external;
    }

    public boolean isAuthorized(long perm) {
	throw new UnsupportedOperationException();
    }

    /**
     * @param status
     */
    public void setStatus(User.Status status) {
	this.status = status == null ? User.Status.none : status;
    }

    /**
     * @return
     */
    public User.Status getStatus() {
	return status;
    }

    public String toString() {
	return String.format("User: %s [%s] [%s] ",
		new Object[] { Long.valueOf(getUserId()), getUsername(), getEmail() });
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
     */
    public boolean isSetPasswordSupported() {
	return setPasswordSupported;
    }

    /**
     * @return
     */
    public boolean isGetPasswordHashSupported() {
	return getPasswordHashSupported;
    }

    /**
     * @return
     */
    public boolean isSetPasswordHashSupported() {
	return setPasswordHashSupported;
    }

    /**
     * @return
     */
    public boolean isSetNameSupported() {
	return setNameSupported;
    }

    /**
     * @return
     */
    public boolean isSetUsernameSupported() {
	return setUsernameSupported;
    }

    public boolean isSetEmailSupported() {
	return setEmailSuppoted;
    }

    /**
     * @return
     */
    public boolean isSetNameVisibleSupported() {
	return setNameVisibleSupported;
    }

    /**
     * @return
     */
    public boolean isSetEmailVisibleSupported() {
	return setEmailVisibleSupported;
    }

    public boolean isPropertyEditSupported() {
	return setPropertyEditSupported;
    }

    /**
     * @param setNameVisibleSupported
     */
    public void setSetNameVisibleSupported(boolean setNameVisibleSupported) {
	this.setNameVisibleSupported = setNameVisibleSupported;
    }

    /**
     * @param setPasswordSupported
     */
    public void setSetPasswordSupported(boolean setPasswordSupported) {
	this.setPasswordSupported = setPasswordSupported;
    }

    /**
     * @param getPasswordHashSupported
     */
    public void setGetPasswordHashSupported(boolean getPasswordHashSupported) {
	this.getPasswordHashSupported = getPasswordHashSupported;
    }

    /**
     * @param setEmailVisibleSupported
     */
    public void setSetEmailVisibleSupported(boolean setEmailVisibleSupported) {
	this.setEmailVisibleSupported = setEmailVisibleSupported;
    }

    /**
     * @param setNameSupported
     */
    public void setSetNameSupported(boolean setNameSupported) {
	this.setNameSupported = setNameSupported;
    }

    /**
     * @param setUsernameSupported
     */
    public void setSetUsernameSupported(boolean setUsernameSupported) {
	this.setUsernameSupported = setUsernameSupported;
    }

    /**
     * @param setPasswordHashSupported
     */
    public void setSetPasswordHashSupported(boolean setPasswordHashSupported) {
	this.setPasswordHashSupported = setPasswordHashSupported;
    }

    /**
     * @param setPropertyEditSupported
     */
    public void setSetPropertyEditSupported(boolean setPropertyEditSupported) {
	this.setPropertyEditSupported = setPropertyEditSupported;
    }

    /**
     * @param setEmailSuppoted
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

    @JsonIgnore
    public Serializable getPrimaryKeyObject() {
	return getUserId();
    }

    public void setPrimaryKeyObject(Serializable primaryKeyObj) {
	setUserId(((Long) primaryKeyObj).longValue());
    }

    @JsonIgnore
    public int getModelObjectType() {
	return ModelTypeFactory.getTypeIdFromCode("USER");
    }

    private static final char USERNAME_DISALLOWED_CHARS[] = { '/', ';', '#', ',', ':' };

    public static String formatUsername(String username) {
	if (username == null)
	    return null;

	boolean allowWhiteSpace = false;

	try {
	    ConfigService config = Bootstrap.getBootstrapComponent(ConfigService.class);
	    allowWhiteSpace = config.getApplicationBooleanProperty("username.allowWhiteSpace", false);
	} catch (Throwable ignore) {
	}

	if (allowWhiteSpace) {
	    String formattedUsername = "";
	    Pattern p = Pattern.compile("\\s+");
	    Matcher m = p.matcher(username.trim());
	    if (m.find()) {
		formattedUsername = m.replaceAll(" ");
		username = formattedUsername;
	    }
	} else {
	    username = StringUtils.stripToEmpty(username);
	}
	return username;
    }

    public static boolean isUsernameValid(String username) {
	return username != null && StringUtils.indexOfAny(username, USERNAME_DISALLOWED_CHARS) == -1
		&& username.length() <= 100;
    }

    @JsonIgnore
    public int getCachedSize() {
	int size = 0;
	size += CacheSizes.sizeOfLong();
	size += CacheSizes.sizeOfBoolean();
	size += CacheSizes.sizeOfBoolean();
	size += CacheSizes.sizeOfBoolean();
	size += CacheSizes.sizeOfBoolean();
	size += CacheSizes.sizeOfBoolean();
	size += CacheSizes.sizeOfBoolean();
	size += CacheSizes.sizeOfBoolean();
	size += CacheSizes.sizeOfBoolean();
	size += CacheSizes.sizeOfBoolean();
	size += CacheSizes.sizeOfBoolean();
	size += CacheSizes.sizeOfBoolean();
	size += CacheSizes.sizeOfBoolean();
	size += CacheSizes.sizeOfBoolean();
	size += CacheSizes.sizeOfBoolean();

	size += CacheSizes.sizeOfString(email);
	size += CacheSizes.sizeOfString(firstName);
	size += CacheSizes.sizeOfString(lastName);
	size += CacheSizes.sizeOfString(name);
	size += CacheSizes.sizeOfString(password);
	size += CacheSizes.sizeOfString(passwordHash);
	size += CacheSizes.sizeOfString(username);

	size += CacheSizes.sizeOfObjectMap(profile);
	size += CacheSizes.sizeOfMap(getProperties());
	size += CacheSizes.sizeOfObject();
	size += CacheSizes.sizeOfDate();
	size += CacheSizes.sizeOfDate();
	size += CacheSizes.sizeOfDate();
	size += CacheSizes.sizeOfDate();

	return size;
    }

    public boolean isProfileSupported() {
	return this.profileSuppoted;
    }

    public boolean isProfileEditSupported() {
	return this.setProfileEditSuppoted;
    }

    public Map<String, Object> getProfile() {
	if (profile == null)
	    profile = new HashMap<String, Object>();
	return profile;
    }

    public void setProfile(Map<String, Object> profile) {
	this.profile = profile;
    }

    // string, date, int, long
    public <T> T getProfileFieldValue(String fieldName, Class<T> elementType) {
	if (profile == null)
	    profile = new HashMap<String, Object>();

	Object object = profile.get(fieldName);
	if (object != null) {
	    if (object.getClass() == elementType) {
		return (T) object;
	    } else {
		String valueToUse = object.toString();
		return (T) StringUtils.defaultString(valueToUse, "");
	    }
	} else {
	    if (elementType == Number.class)
		return (T) new Long(0);
	    return null;
	}
    }

    public void setProfileFieldValue(String fieldName, Object value) {
	if (profile == null)
	    profile = new HashMap<String, Object>();
	profile.put(fieldName, value);
    }

    public String getProfileFieldValueString(String fieldName) {
	return getProfileFieldValue(fieldName, String.class);
    }

    public long getCompanyId() {
	return companyId;
    }

    public void setCompanyId(long companyId) {
	this.companyId = companyId;
    }

    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    @Override
    public boolean hasCompany() {
	if (this.companyId > 0)
	    return true;
	return false;
    }

}