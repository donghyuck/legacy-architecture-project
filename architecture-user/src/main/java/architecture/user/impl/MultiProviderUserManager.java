package architecture.user.impl;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.common.user.User;
import architecture.common.user.UserTemplate;
import architecture.common.user.authentication.AnonymousUser;
import architecture.ee.component.admin.AdminHelper;
import architecture.user.EmailAlreadyExistsException;
import architecture.user.UserAlreadyExistsException;
import architecture.user.UserManager;
import architecture.user.UserNotFoundException;
import architecture.user.dao.UserDao;
import architecture.user.security.authentication.InvalidProviderUserException;
import architecture.user.spi.UserProvider;
import architecture.user.spring.security.authentication.ExtendedUserDetailsAdapter;

/**
 * @author  donghyuck
 */
public class MultiProviderUserManager implements UserManager, EventSource {

	private Log log = LogFactory.getLog(getClass());
	
	private static final SecureRandom entropy = new SecureRandom();
	
	
	protected boolean allowApplicationUserCreation;
	protected boolean emailAddressCaseSensitive;
	protected SaltSource saltSource;
	protected PasswordEncoder passwordEncoder;
	protected List<UserProvider> providers;
	
	/**
	 */
	private UserDao userDao;
	/**
	 */
	private EventPublisher eventPublisher;
	/**
	 */
	protected int applicationUserCount;
	/**
	 */
	protected int authenticatedUserCount;
	/**
	 */
	protected int totalUserCount;

	private Cache userCache;
	private Cache userIdCache;
	private Cache userProviderCache;

	
	public MultiProviderUserManager() {
		this.allowApplicationUserCreation = true;
		this.emailAddressCaseSensitive = false;
		this.providers = new ArrayList<UserProvider>();
		this.applicationUserCount = -1;
		this.authenticatedUserCount = -1;
		this.totalUserCount = -1;

	}

	public void initialize(){		
		this.userCache = AdminHelper.getCache("userCache");
		this.userIdCache = AdminHelper.getCache("userIDCache");
		this.userProviderCache = AdminHelper.getCache("userProviderCache");
	}
	
	/**
	 * @param allowApplicationUserCreation
	 */
	public void setAllowApplicationUserCreation(
			boolean allowApplicationUserCreation) {
		this.allowApplicationUserCreation = allowApplicationUserCreation;
	}

	/**
	 * @param emailAddressCaseSensitive
	 */
	public void setEmailAddressCaseSensitive(boolean emailAddressCaseSensitive) {
		this.emailAddressCaseSensitive = emailAddressCaseSensitive;
	}

	/**
	 * @param saltSource
	 */
	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	/**
	 * @param passwordEncoder
	 */
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public void addUserProvider(UserProvider provider) {
        synchronized(providers)
        {
            providers.add(provider);
        }
	}

	/**
	 * @param providers
	 */
	public void setProviders(List<UserProvider> providers) {
        if(null == providers)
            throw new NullPointerException("User provider list cannot be null.");
        this.providers = providers;
	}

	/**
	 * @param userDao
	 */
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * @param userCache
	 */
	public void setUserCache(Cache userCache) {
		this.userCache = userCache;
	}

	/**
	 * @param userIdCache
	 */
	public void setUserIdCache(Cache userIdCache) {
		this.userIdCache = userIdCache;
	}

	/**
	 * @param providerCache
	 */
	public void setUserProviderCache(Cache providerCache) {
		this.userProviderCache = providerCache;
	}

	/**
	 * @param eventPublisher
	 */
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public User createUser(User newUser) throws UserAlreadyExistsException,
			UnsupportedOperationException, EmailAlreadyExistsException {
		
		
		User user = getUser(newUser);
		if(null != user)
            if(!user.getUsername().equals(newUser.getUsername()))
            {
                if(caseEmailAddress(user).equals(caseEmailAddress(newUser)))
                {
                    String message = (new StringBuilder()).append("Email already exists: ").append(user.getUsername()).append(" / ").append(caseEmailAddress(user)).toString();
                    log.info(message);
                    throw new EmailAlreadyExistsException(message);
                }
            } else
            {
                String message = (new StringBuilder()).append("User already exists: ").append(user.getUsername()).append(" / ").append(caseEmailAddress(user)).toString();
                log.info(message);
                throw new UserAlreadyExistsException(message);
            }
		
		UserTemplate userTemplate = new UserTemplate(newUser);
		wireTemplateDates(userTemplate);
		userTemplate.setPassword(newUser.getPassword());
		userTemplate.setPasswordHash(getPasswordHash(userTemplate));
		user = createApplicationUser(userTemplate);
		
		for(UserProvider up : providers ){
			
			if(up.supportsUpdate())
			{
				log.info((new StringBuilder()).append("Creating user with provider '").append(up.getName()).append("'.").toString());
				try
                {
                    long systemId = user.getUserId();
                    User result = up.create(user);
                    if(null == result || result.getUserId() != systemId)
                        log.warn((new StringBuilder()).append("Provider '").append(up.getName()).append("' did not honor application user id.").toString());
                }
                catch(Exception exc)
                {
                    log.warn("Provider user creation failed.", exc);
                    throw new UnsupportedOperationException((new StringBuilder()).append("User provider '").append(up.getName()).append("' ").append(" failed to create user.").toString());
                }
			}
		}
		
		// fire event ;
		//eventPublisher.publish(event);
		
		return user;
	}

	public User createApplicationUser(User newUser) throws UserAlreadyExistsException {
		User user = getUser(newUser);
		if(null != user)
        {
            String message = (new StringBuilder()).append("User already exists: ").append(user.toString()).toString();
            log.info(message);
            throw new UserAlreadyExistsException(message);
        }
		UserTemplate userTemplate = new UserTemplate(newUser);
		
		// 외부 계정인 경우 처리 방법...
		if(userTemplate.isFederated() || userTemplate.isExternal()){
	        byte buffer[] = new byte[64];
	        entropy.nextBytes(buffer);
	        userTemplate.setPassword(new String(buffer));
		}
		
		wireTemplateDates(userTemplate);
	    userTemplate.setPasswordHash(getPasswordHash(userTemplate));
	    userTemplate.setEmail(caseEmailAddress(newUser));
	    
	    user = userDao.create(userTemplate);
	    
        userTemplate = new UserTemplate(user);
        
        // translate into fadderate
        translateFederated(userTemplate);
        
        userTemplate.setPassword(null);
        updateCaches(userTemplate);
        
        if(!user.isExternal()){
        	// @Todo 개인화 데이터 생성...
        	
        }        
		return userTemplate;
	}

	public User getUser(User template) {
		return getUser(template, false);
	}

	public User getUser(User template, boolean caseInsensitive) {
		
		User user = null;
		// 익명 사용자 이다.
		if(template.getUserId() == -1L || template.getUsername() != null && template.getUsername().equals("ANONYMOUS")){
			return new AnonymousUser();	
		}
				 
		if(template.getUserId() > 0L){
			
			user = getUserFromCacheById(template.getUserId());
		    			
		    if( user == null)
				try {
					user = userDao.getUserById(template.getUserId());
				} catch (Exception ex) {
					log.error("DAO exception loading user by ID.", ex);
				}				
		}
		
		// 이름으로 검색
		log.debug(userIdCache.getName());
		long id = -2L;
		
		if(user != null || template.getUsername() != null )
		{
			
			if( userIdCache.get(template.getUsername()) != null ){
				id = (Long)userIdCache.get(template.getUsername()).getValue();				
			}
			
			if( id > 0L ){
				user = getUserFromCacheById(id);		    
				if( user == null)
					try {
						user = userDao.getUserById(template.getUserId());
					} catch (Exception ex) {
						log.error("DAO exception loading user by ID.", ex);
					}
			}
			
			if(null == user){				
				if(caseInsensitive){
		            try
		            {
		                user = userDao.getUserByUsernameNoCase(template.getUsername());
		            }
		            catch(Exception ex)
		            {
		                log.debug("DAO exception loading user by username (case insensitive).", ex);
		            }	
				}else{
			        try
			        {
			            user = userDao.getUserByUsername(template.getUsername());
			        }
			        catch(Exception ex)
			        {
			            log.debug("DAO exception loading user by username.", ex);
			        }
				}
			}
			
		}

		 if(null == user && null != template.getEmail()){
			 try
	            {
	                user = userDao.getUserByEmail(template.getEmail());
	            }
	            // Misplaced declaration of an exception variable
	            catch(Exception ex)
	            {
	                log.debug("DAO exception loading user by email.", ex);
	            }
		 } 
		 
		if (null == user) {
			user = sourceUserFromProvider(template);
			if (null != user) {
				log.debug((new StringBuilder()).append("Sourced user '").append(user.toString()).append("' from provider.").toString());
				try {
					user = createApplicationUser(user);
				} catch (UserAlreadyExistsException e) {}
			}
		}
		if (null != user) {
			user = translateFederated(new UserTemplate(user));
			updateCaches(user);
		}
		
		return user;
	}

	public User getUser(String userName) throws UserNotFoundException {
		if(null == userName)
        {
            return null;
        }		
		User user = getUser(((User) (new UserTemplate(userName))));
		if (null == user) {
			String message = (new StringBuilder()).append("User '").append(userName).append("' not found.").toString();
			log.info(message);
			throw new UserNotFoundException(message);
		}
		return user;        
	}

	public User getUser(long userId) throws UserNotFoundException {
		
		User user = getUser(((User) (new UserTemplate(userId))));
        if(null == user)
        {
            String message = (new StringBuilder()).append("User not found for ID ").append(userId).append(".").toString();
            log.info(message);
            throw new UserNotFoundException(message);
        }		
		return user;
	}

	public void deleteUser(User user) throws UnsupportedOperationException, UserNotFoundException {
		log.info((new StringBuilder()).append("Deleting user ").append(user).append(".").toString());
        User loadedUser = getUser(user);
        if(null == loadedUser)
        {
            log.info((new StringBuilder()).append("Unable to load user for deletion ").append(user.toString()).append(".").toString());
        } else
        {
        	try
            {
                UserProvider up = getUserProvider(loadedUser);
                if(null != up)
                {
                    log.debug("Deleting user via provider.");
                    up.delete(loadedUser);
                    userProviderCache.remove(Long.valueOf(user.getUserId()));
                }
            }
            catch(Exception ex)
            {
                log.warn("Deletion of user via provider produced exception.", ex);
            }
            try
            {
                userDao.delete(loadedUser);
                userCache.remove(Long.valueOf(loadedUser.getUserId()));
                userIdCache.remove(loadedUser.getUsername());
                resetUserCounts();
            }
            catch(DataAccessException ex)
            {
                String message = (new StringBuilder()).append("Failed to remove application user for ").append(user).append(".").toString();
                log.error(message, ex);
                throw ex;
            }        	
        }
		
	}

	public void deleteUserAndContent(User user) throws UnsupportedOperationException, UserNotFoundException {
        log.info((new StringBuilder()).append("Deleting user ").append(user).append(" and content.").toString());
        User loadedUser = getUser(user);
		
        /*if(null == loadedUser)
            log.info((new StringBuilder()).append("Unable to load user for deletion ").append(user.toString()).append(".").toString());
        else
            dispatchEvent(new UserEvent(com.jivesoftware.base.event.UserEvent.Type.DELETED, new UserTemplate(loadedUser), null));*/
        //eventPublisher.publish(event);
	}

	/**
	 * @return
	 */
	public int getApplicationUserCount() {
        if(applicationUserCount == -1)
            applicationUserCount = userDao.getApplicationUserCount();
        return applicationUserCount;
	}

	public List<User> getApplicationUsers() {
		return userDao.getApplicationUsers();
	}

	public List<User> getApplicationUsers(int startIndex, int numResults) {
		return userDao.getApplicationUsers(startIndex, numResults);
	}

	/**
	 * @return
	 */
	public int getAuthenticatedUserCount() {
        if(authenticatedUserCount == -1)
            authenticatedUserCount = userDao.getAuthenticatedUserCount();
        return authenticatedUserCount;
	}

	/**
	 * @return
	 */
	public int getTotalUserCount() {
        if(totalUserCount == -1)
            totalUserCount = userDao.getTotalUserCount();
		return totalUserCount;
	}

	public int getRecentUserCount(Date date) {
		return userDao.getRecentUserCount(date);
	}

	public List<User> getUsers() {
		return userDao.getAllUsers();
	}

	public List<User> getUsers(int startIndex, int numResults) {		
		return userDao.getAllUsers(startIndex, numResults);
	}

	public boolean isCreationSupported() {
		return allowApplicationUserCreation;
	}

	public User updateUser(User user) throws UserNotFoundException, UserAlreadyExistsException {

		log.debug((new StringBuilder()).append("Updating user information for ").append(user).append(".").toString());
        UserTemplate userModel = new UserTemplate(getUser(user));
        
/*        if( null == userModel){
        	throw new UserNotFoundException((new StringBuilder()).append("User not found for ").append(user).toString());
        }*/
		
        
        String previousUsername = null;
        if(!userModel.getUsername().equals(user.getUsername()))
        {
            previousUsername = userModel.getUsername();
            UserTemplate toCheck = new UserTemplate();
            toCheck.setUsername(user.getUsername());
            User match = getUser(toCheck);
            if(null != match && match.getUserId() != user.getUserId())
            {
                log.info("Rejecting attempt to change username to an existing username");
                throw new UserAlreadyExistsException((new StringBuilder()).append("Username '").append(user.getUsername()).append("' already exists.").toString());
            }
        }
        
        
        Map<String, Object> eventParams = new HashMap<String, Object>();
        if(!equals(caseEmailAddress(userModel), caseEmailAddress(user)))
            eventParams.put("emailModify", caseEmailAddress(userModel));
        if(userModel.isEmailVisible() != user.isEmailVisible())
            eventParams.put("emailVisibleModify", userModel.isEmailVisible());
        if(userModel.isExternal() != user.isExternal())
            eventParams.put("externalModify", userModel.isExternal());
        if(userModel.isFederated() != user.isFederated())
            eventParams.put("federatedModify", userModel.isFederated());
        if(!equals(userModel.getLastLoggedIn(), user.getLastLoggedIn()))
            eventParams.put("lastLoggedInModify", userModel.getLastLoggedIn());
        if(!equals(userModel.getName(), user.getName()))
            eventParams.put("nameModify", userModel.getName());
        if(!equals(userModel.getFirstName(), user.getFirstName()))
            eventParams.put("nameModifyFirst", userModel.getFirstName());
        if(!equals(userModel.getLastName(), user.getLastName()))
            eventParams.put("nameModifyLast", userModel.getLastName());
        if(userModel.isNameVisible() != user.isNameVisible())
            eventParams.put("nameVisibleModify", userModel.isNameVisible());
        if(!equals(userModel.getUsername(), user.getUsername()))
            eventParams.put("usernameModify", userModel.getUsername());
        if(userModel.isEnabled() != user.isEnabled())
            eventParams.put("enabledModify", userModel.isEnabled());
        if(userModel.getStatus() != user.getStatus())
            eventParams.put("statusOfUserModified", userModel.getStatus());
        
        userModel.setEmail(caseEmailAddress(user));
        userModel.setEmailVisible(user.isEmailVisible());
        userModel.setExternal(user.isExternal());
        userModel.setFederated(user.isFederated());
        userModel.setLastLoggedIn(user.getLastLoggedIn());
        userModel.setLastProfileUpdate(user.getLastProfileUpdate());
        userModel.setModifiedDate(new Date());
        userModel.setName(user.getName());
        userModel.setNameVisible(user.isNameVisible());
        userModel.setFirstName(user.getFirstName());
        userModel.setLastName(user.getLastName());
        userModel.setProperties(user.getProperties());
        userModel.setUsername(user.getUsername());
        userModel.setEnabled(user.isEnabled());
        userModel.setStatus(user.getStatus());
        userModel.setPassword(user.getPassword());
        wireTemplateDates(userModel);
        String newHash = getPasswordHash(userModel);
        if(null != newHash && !newHash.equals(user.getPasswordHash()))
        {
            eventParams.put("passwordModified", user.getPassword());
            userModel.setPasswordHash(newHash);
        }
        if(userModel.getLastLoggedIn() == null || userModel.getLastLoggedIn().equals(new Date(0L)))
        {
            User lliUser = userDao.getUserById(userModel.getUserId());
            if(lliUser.getLastLoggedIn() != null && lliUser.getLastLoggedIn().getTime() > (new Date(0L)).getTime())
                throw new IllegalArgumentException("Can't set last logged in to zero if already greater than zero");
        }
        
        UserProvider up = getUserProvider(userModel);
        try
        {
            if(null != up)
            {
                log.info((new StringBuilder()).append("Attempting to update user with provider '").append(up.getName()).append("'.").toString());
                up.update(userModel);
            }
        }
        catch(Exception ex)
        {
            log.warn((new StringBuilder()).append("Exception attempting to update user with provider '").append(up.getName()).append("'. The local user will be updated but the system may be ").append("in an inconsistent state.").toString(), ex);
        }
        try
        {
        	userDao.update(userModel);
            userCache.put( new Element( userModel.getUserId(), userModel ));
            
            
            if(previousUsername != null)
                userIdCache.remove(previousUsername);
            userIdCache.put(new Element( userModel.getUsername(), userModel.getUserId( )));
            
            resetUserCounts();
        }
        catch(DataAccessException ex)
        {
            String message = (new StringBuilder()).append("Unable to update user ").append(user).append(".").toString();
            log.error(message, ex);
            throw ex;
        }
        //dispatchEvent(new UserEvent(com.jivesoftware.base.event.UserEvent.Type.MODIFIED, userModel, eventParams));
       //eventPublisher.publish(event);
        
		return userModel;
	}

	public void enableUser(User user) {
		try {
			modifyUserEnabled(user, true);
		} catch (UserNotFoundException e) {
			log.debug(e.getMessage(), e);
		} catch (UserAlreadyExistsException e) {
			log.debug(e.getMessage(), e);
		}
	}

	protected User sourceUserFromProvider(User user) {
		if(providers.size() <= 0)
			return null;
		
		User sourcedUser;
		for(UserProvider up :providers ){			
			sourcedUser = up.getUser(user);
			
			if(sourcedUser == null)
				continue;
			
			validateProviderUser(sourcedUser);
			if (null == sourcedUser.getPasswordHash()) {
				UserTemplate sourcedUserWithPass = new UserTemplate(sourcedUser);
				byte buffer[] = new byte[64];
				entropy.nextBytes(buffer);
				sourcedUserWithPass.setPasswordHash(new String(buffer));
				sourcedUser = sourcedUserWithPass;
			}

			User appUser = userDao.getUserByUsername(sourcedUser.getUsername());
			if (null == appUser) {
				log.debug("Creating application user for externally-sourced user.");
				appUser = userDao.create(sourcedUser);
			}
			userProviderCache.put(new Element(appUser.getUserId(), up.getName()));
			
			return appUser;
		}
		
		return null;
	}

    private User getUserFromCacheById(long id)
    {
        if(userCache.isKeyInCache(id))
            return (User)userCache.get(id).getValue();
        else
            return null;
    }
    
    private void resetUserCounts()
    {
        applicationUserCount = -1;
        authenticatedUserCount = -1;
        totalUserCount = -1;
    }
    
    private boolean equals(String a, String b)
    {
        if(a == null && b == null)
            return true;
        return a != null && a.equals(b);
    }

    private boolean equals(Date a, Date b)
    {
        if(a == null && b == null)
            return true;
        return a != null && a.equals(b);
    }
    
	
	protected void validateProviderUser(User user)
    {
        if(null == user.getName())
            throw new InvalidProviderUserException("Name is invalid [null].");
        if(null == user.getEmail())
            throw new InvalidProviderUserException("Email is invalid [null]");
        else
            return;
    }
    
    
    protected void modifyUserEnabled(User user, boolean enabled) throws UserNotFoundException, UserAlreadyExistsException
    {
    	UserTemplate userModel = new UserTemplate(getUser(user));
    	
    	//Map eventParams = new HashMap();
    	//eventParams.put("enabledModify", userModel.isEnabled());
    	
    	userModel.setEnabled(enabled);
    	wireTemplateDates(userModel);
    	
    	UserProvider up = getUserProvider(userModel);
    	
    	try
        {
            if(null != up)
            {
                log.info((new StringBuilder()).append("Attempting to update user with provider '").append(up.getName()).append("'.").toString());
                up.update(userModel);
            }
        }
        catch(Exception ex)
        {
            if(up != null)
                log.warn((new StringBuilder()).append("Exception attempting to update user with provider '").append(up.getName()).append("'. The local user will be updated but the system may be ").append("in an inconsistent state.").toString(), ex);
        }
        
    	
    	try
        {
    		userDao.update(userModel);
            userCache.put(new Element(userModel.getUserId(), userModel));
            userIdCache.put(new Element(userModel.getUsername(), userModel.getUserId()));
            resetUserCounts();
        }
        catch(DataAccessException ex)
        {
            String message = (new StringBuilder()).append("Unable to update user ").append(user).append(".").toString();
            log.error(message, ex);
            throw ex;
        }
    	// fire event ;;
        //eventPublisher.publish(event);
    }
    
    protected UserProvider getUserProvider(User user){
    	 if(providers.size() == 0)
             return null;
    	 
        
    	 String providerName = (String) userProviderCache.get( user.getUserId() ).getValue();
         
    	 for(UserProvider up : providers){
        	 if( up.getName().equals(providerName) )
        		 return up;
         }
         
    	 for(UserProvider up : providers){
    		 User sourcedUser = up.getUser(user);
    		 if(null != sourcedUser){
                 userProviderCache.put( new Element( user.getUserId(), up.getName() ));
                 updateCaches(sourcedUser);
                 return up;
    		 }
    	 }
         
         return null;
    }
    
    
	private void updateCaches(User user)
    {
        long uid = user.getUserId();
        if(uid < 0L)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("Invalid user Id ").append(uid).append(".").toString());
        } else
        {
            this.userCache.put(new Element(uid, user));
            this.userIdCache.put(new Element(user.getUsername(), uid ));
            return;
        }
    }
	
	private String getPasswordHash(User user)
    {
        String passwd;
        passwd = user.getPassword();
        if(null == passwd)
            return null;
        if(passwd.equals(""))
            return null;
        try
        {
            return passwordEncoder.encodePassword(passwd, saltSource.getSalt(new ExtendedUserDetailsAdapter(user)));
        }
        catch(Exception ex)
        {
            log.warn("Unable to generate new user password hash.", ex);
        }
        return null;
    }
	
    private void wireTemplateDates(UserTemplate ut)
    {
        if(null == ut)
            return;
        if( null == ut.getCreationDate() )
            ut.setCreationDate(new Date());
        if(null == ut.getModifiedDate())
            ut.setModifiedDate(new Date());
    }
    

    private String caseEmailAddress(User user)
    {
        return emailAddressCaseSensitive || user.getEmail() == null ? user.getEmail() : user.getEmail().toLowerCase();
    }
    
    protected UserTemplate translateFederated(UserTemplate usert)
    {
        if(null == usert)
            return null;
        if(usert.isFederated())
        {
            usert.setGetPasswordHashSupported(false);
            usert.setSetEmailSuppoted(false);
            usert.setSetNameSupported(false);
            usert.setSetPasswordHashSupported(false);
            usert.setSetPasswordSupported(false);
            usert.setSetUsernameSupported(false);
        }
        return usert;
    }
    
	
}