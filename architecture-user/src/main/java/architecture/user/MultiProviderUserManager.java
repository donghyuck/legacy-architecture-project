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

package architecture.user;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.common.exception.CodeableException;
import architecture.common.exception.CodeableRuntimeException;
import architecture.common.user.Company;
import architecture.common.user.EmailAlreadyExistsException;
import architecture.common.user.Group;
import architecture.common.user.User;
import architecture.common.user.UserAlreadyExistsException;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.UserTemplate;
import architecture.common.user.authentication.AnonymousUser;
import architecture.common.user.spi.UserProvider;
import architecture.common.util.L10NUtils;
import architecture.ee.component.admin.AdminHelper;
import architecture.user.dao.UserDao;
import architecture.user.security.authentication.InvalidProviderUserException;
import architecture.user.security.spring.userdetails.ExtendedUserDetailsAdaptor;

import com.google.common.collect.Sets;


/**
 * 멀티 소스를 지원하는 사용자 관리자 클래스 
 * 
 * @author  <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 *
 */
@Monitor
public class MultiProviderUserManager implements UserManager, EventSource {

	private Log log = LogFactory.getLog(getClass());
	
	private static final SecureRandom entropy = new SecureRandom();
	protected boolean allowApplicationUserCreation;
	protected boolean emailAddressCaseSensitive;
	protected SaltSource saltSource;
	protected PasswordEncoder passwordEncoder;
	protected List<UserProvider> providers;
	private UserDao userDao;
	private EventPublisher eventPublisher;
	
	protected int applicationUserCount;
	protected int authenticatedUserCount;
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
		this.userCache = AdminHelper.getCache("userCache");
		this.userIdCache = AdminHelper.getCache("userIDCache");
		this.userProviderCache = AdminHelper.getCache("userProviderCache");
	}

	public void setAllowApplicationUserCreation(
			boolean allowApplicationUserCreation) {
		this.allowApplicationUserCreation = allowApplicationUserCreation;
	}

	public void setEmailAddressCaseSensitive(boolean emailAddressCaseSensitive) {
		this.emailAddressCaseSensitive = emailAddressCaseSensitive;
	}

	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public void addUserProvider(UserProvider provider) {
        synchronized(providers)
        {
            providers.add(provider);
        }
	}

	public void setProviders(List<UserProvider> providers) {
        if(null == providers)
            throw new NullPointerException(L10NUtils.getMessage("005103"));
        this.providers = providers;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setUserCache(Cache userCache) {
		this.userCache = userCache;
	}

	public void setUserIdCache(Cache userIdCache) {
		this.userIdCache = userIdCache;
	}

	public void setUserProviderCache(Cache userProviderCache) {
		this.userProviderCache = userProviderCache;
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}


	public User createUser(User newUser, Company company) throws UserAlreadyExistsException, UnsupportedOperationException, EmailAlreadyExistsException {
		User user = getUser(newUser);
		if(null != user)
            if(!user.getUsername().equals(newUser.getUsername()))
            {
                if(caseEmailAddress(user).equals(caseEmailAddress(newUser)))
                {
                	EmailAlreadyExistsException e = CodeableException.newException(EmailAlreadyExistsException.class, 5104, user.getUsername(), caseEmailAddress(user));                   
                    throw e;
                }
            } else
            {                
                UserAlreadyExistsException e = CodeableException.newException(UserAlreadyExistsException.class, 5105, user.getUsername(), caseEmailAddress(user));
                log.info(e.getMessage());
                throw e;                
            }
		
		UserTemplate userTemplate = new UserTemplate(newUser);
		wireTemplateDates(userTemplate);
		userTemplate.setPassword(newUser.getPassword());
		userTemplate.setPasswordHash(getPasswordHash(userTemplate));
		userTemplate.setCompanyId(company.getCompanyId());
		userTemplate.setCompany(company);
		
		user = createApplicationUser(userTemplate);
		
		for(UserProvider up : providers ){
			
			if(up.supportsUpdate())
			{
				log.info( L10NUtils.format("005106", up.getName() ) );
				try
                {
                    long systemId = user.getUserId();
                    User result = up.create(user);
                    if(null == result || result.getUserId() != systemId){
                    	log.warn(L10NUtils.format("005107", up.getName()));
                    }
                }
                catch(Exception exc)
                {
                    log.warn( L10NUtils.getMessage("005108"), exc);
                    throw new UnsupportedOperationException( L10NUtils.format("005109", up.getName()) );
                }
			}
		}		
		return user;
	}    	
	
	public User createUser(User newUser) throws UserAlreadyExistsException,
			UnsupportedOperationException, EmailAlreadyExistsException {
		
		User user = getUser(newUser);
		if(null != user)
            if(!user.getUsername().equals(newUser.getUsername()))
            {
                if(caseEmailAddress(user).equals(caseEmailAddress(newUser)))
                {
                	EmailAlreadyExistsException e = CodeableException.newException(EmailAlreadyExistsException.class, 5104, user.getUsername(), caseEmailAddress(user));                   
                    throw e;
                }
            } else
            {                
                UserAlreadyExistsException e = CodeableException.newException(UserAlreadyExistsException.class, 5105, user.getUsername(), caseEmailAddress(user));
                log.info(e.getMessage());
                throw e;                
            }
		
		UserTemplate userTemplate = new UserTemplate(newUser);
		wireTemplateDates(userTemplate);
		userTemplate.setPassword(newUser.getPassword());
		userTemplate.setPasswordHash(getPasswordHash(userTemplate));
		user = createApplicationUser(userTemplate);
		
		for(UserProvider up : providers ){
			
			if(up.supportsUpdate())
			{
				log.info( L10NUtils.format("005106", up.getName() ) );
				try
                {
                    long systemId = user.getUserId();
                    User result = up.create(user);
                    if(null == result || result.getUserId() != systemId){
                    	log.warn(L10NUtils.format("005107", up.getName()));
                    }
                }
                catch(Exception exc)
                {
                    log.warn( L10NUtils.getMessage("005108"), exc);
                    throw new UnsupportedOperationException( L10NUtils.format("005109", up.getName()) );
                }
			}
		}		
		// fire event ;
		//eventPublisher.publish(event);
		
		return user;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public User createApplicationUser(User newUser) throws UserAlreadyExistsException {
		User user = getUser(newUser);
		if(null != user)
        {
            UserAlreadyExistsException e = CodeableException.newException(UserAlreadyExistsException.class, 5110, user.getUsername()) ;
            log.info(e.getMessage());
            throw e;
        }
		
		UserTemplate userTemplate = new UserTemplate(newUser);
		
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
        	// @Todo 외부에서 연계된 사용자인경우 .. 처리..
        	
        }        
		return userTemplate;
	}

	public User getUser(User template) {
		return getUser(template, false);
	}

	public User getUser(User template, boolean caseInsensitive) {
		
		User user = null;

		if(template.getUserId() == -1L || template.getUsername() != null && template.getUsername().equals("ANONYMOUS")){
			return new AnonymousUser();	
		}
				 
		if(template.getUserId() > 0L){
			
			user = getUserFromCacheById(template.getUserId());
		    			
		    if( user == null)
				try {
					user = userDao.getUserById(template.getUserId());
					
				} catch (Exception ex) {					
					log.error( L10NUtils.getMessage("005111") , ex);
				}				
		}
		
		long id = -2L;		
		if(user != null || template.getUsername() != null )
		{			
			if( userIdCache.get(template.getUsername()) != null ){
				id = (Long)userIdCache.get(template.getUsername()).getValue();
			}			
			if( id > 0L ){
				
				user = getUserFromCacheById(id);		    
				
				log.debug( 
					"user cached:"+ user + ", properties=" +  user.getProperties() 
				);
				
				if( user == null){
					try {
						user = userDao.getUserById(template.getUserId());
					} catch (Exception ex) {
						log.error(L10NUtils.getMessage("005111"), ex);
					}
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
		                log.debug(L10NUtils.getMessage("005112"), ex);
		            }	
				}else{
			        try
			        {
			            user = userDao.getUserByUsername(template.getUsername());
			        }
			        catch(Exception ex)
			        {
			            log.debug(L10NUtils.getMessage("005113"), ex);
			        }
				}
			}
			
		}

		if (null == user && null != template.getEmail()) {
			try {
				user = userDao.getUserByEmail(template.getEmail());
			}
			// Misplaced declaration of an exception variable
			catch (Exception ex) {
				log.debug(L10NUtils.getMessage("005114"), ex);
			}
		}
		 
		if (null == user) {
			user = sourceUserFromProvider(template);
			if (null != user) {
				log.debug(L10NUtils.format("005115", user.toString()));				
				try {
					user = createApplicationUser(user);
				} catch (UserAlreadyExistsException e) {
					//?
				}
			}
		}
		if (null != user) {
			user = translateFederated(new UserTemplate(user));
			updateCaches(user);
		}
		
		return user;
	}

	public User getUser(String userName) throws UserNotFoundException {
		if (null == userName) {
			return null;
		}
		User user = getUser(((User) (new UserTemplate(userName))));
		if (null == user) {
			UserNotFoundException e = CodeableException.newException(
					UserNotFoundException.class, 5116, userName);
			log.info(e.getMessage());
			throw e;
		}
		return user;
	}

	public User getUser(long userId) throws UserNotFoundException {
		User user = getUser(((User) (new UserTemplate(userId))));
		if (null == user) {
			UserNotFoundException e = CodeableException.newException( UserNotFoundException.class, 5117, userId);
			log.info(e.getMessage());
			throw e;
		}
		return user;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteUser(User user) throws UnsupportedOperationException,
			UserNotFoundException {
		log.info(L10NUtils.format("005118", user));
		User loadedUser = getUser(user);
		if (null == loadedUser) {
			log.info(L10NUtils.format("005119", user.toString()));
		} else {
			try {
				UserProvider up = getUserProvider(loadedUser);
				if (null != up) {
					log.debug(L10NUtils.getMessage("005120"));
					up.deleteUser(loadedUser);
					userProviderCache.remove(Long.valueOf(user.getUserId()));
				}
			} catch (Exception ex) {
				log.warn(L10NUtils.getMessage("005121"), ex);
			}
			try {
				userDao.delete(loadedUser);
				userCache.remove(Long.valueOf(loadedUser.getUserId()));
				userIdCache.remove(loadedUser.getUsername());
				resetUserCounts();
			} catch (DataAccessException ex) {
				String message = (new StringBuilder())
						.append("Failed to remove application user for ")
						.append(user).append(".").toString();
				log.error(message, ex);
				throw ex;
			}
		}

	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteUserAndContent(User user) throws UnsupportedOperationException, UserNotFoundException {
        log.info((new StringBuilder()).append("Deleting user ").append(user).append(" and content.").toString());
        User loadedUser = getUser(user);
		
        /*if(null == loadedUser)
            log.info((new StringBuilder()).append("Unable to load user for deletion ").append(user.toString()).append(".").toString());
        else
            dispatchEvent(new UserEvent(com.jivesoftware.base.event.UserEvent.Type.DELETED, new UserTemplate(loadedUser), null));*/
        //eventPublisher.publish(event);
	}

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

	public int getAuthenticatedUserCount() {
        if(authenticatedUserCount == -1)
            authenticatedUserCount = userDao.getAuthenticatedUserCount();
        return authenticatedUserCount;
	}

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

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public User updateUser(User user) throws UserNotFoundException, UserAlreadyExistsException {
		log.debug( L10NUtils.format("005124", user ) );
        UserTemplate userModel = new UserTemplate(getUser(user));        
        if( null == userModel){
        	throw CodeableException.newException(UserNotFoundException.class, 5116, user);
        }		        
        String previousUsername = null;
        if(!userModel.getUsername().equals(user.getUsername()))
        {
            previousUsername = userModel.getUsername();
            UserTemplate toCheck = new UserTemplate();
            toCheck.setUsername(user.getUsername());
            User match = getUser(toCheck);
            if(null != match && match.getUserId() != user.getUserId())
            {
                log.info( L10NUtils.getMessage("005125")  );
                throw CodeableException.newException(UserAlreadyExistsException.class, 5126, user.getUsername());
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
                throw new IllegalArgumentException( L10NUtils.getMessage("005127") );
        }
        
        UserProvider up = getUserProvider(userModel);
        try
        {
            if(null != up)
            {
                log.info( L10NUtils.format("005128", up.getName()) );
                up.updateUser(userModel);
            }
        }
        catch(Exception ex)
        {
            log.warn( L10NUtils.format("005129", up.getName()) );
        }
        
        try
        {
        	userDao.update(userModel);
        	
            // cache 수정 ..
        	userCache.put( new Element( userModel.getUserId(), userModel ));             
            if(previousUsername != null)
                userIdCache.remove(previousUsername);
            userIdCache.put(new Element( userModel.getUsername(), userModel.getUserId( )));        
            resetUserCounts();
        }
        catch(DataAccessException ex)
        {        	
            log.error(L10NUtils.format("005130", user), ex);
            throw ex;
        }
        //dispatchEvent(new UserEvent(com.jivesoftware.base.event.UserEvent.Type.MODIFIED, userModel, eventParams));
       //eventPublisher.publish(event);
		return userModel;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void switchCompanies(Company  company,  List<User> users ){
		
		Set<Long> userIds = Sets.newHashSetWithExpectedSize(users.size());		
		for( User u : users)
			userIds.add(u.getUserId());		
		userDao.switchCompanies( company.getCompanyId(), userIds );
		for( User u : users)
			userCache.remove(u.getUserId());
		
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
				log.debug( L10NUtils.getMessage( "005131" ) ) ; 
				appUser = userDao.create(sourcedUser);
			}
			userProviderCache.put(new Element(appUser.getUserId(), up.getName()));			
			return appUser;
		}		
		return null;
	}

	
    private User getUserFromCacheById(long id)
    {
    	
        if(userCache.get(id) != null)
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
            throw CodeableRuntimeException.newException(InvalidProviderUserException.class, 5132, null);
        if(null == user.getEmail())
            throw CodeableRuntimeException.newException(InvalidProviderUserException.class, 5133, null);
        else
            return;
    }
    
	
	protected void modifyUserPassword(User user, String password ){
		
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
                log.info( L10NUtils.format("005128", up.getName()) );
                up.updateUser(userModel);
            }
        }
        catch(Exception ex)
        {
            if(up != null)
            	log.warn( L10NUtils.format("005129", up.getName() ));
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
            log.error( L10NUtils.format("005130", user) , ex);
            throw ex;
        }
    	// fire event ;;
        //eventPublisher.publish(event);
    }

	protected UserProvider getUserProvider(User user) {
		if (providers.size() == 0)
			return null;

		String providerName = (String) userProviderCache.get(user.getUserId()).getValue();
		for (UserProvider up : providers) {
			if (up.getName().equals(providerName))
				return up;
		}

		for (UserProvider up : providers) {
			User sourcedUser = up.getUser(user);
			if (null != sourcedUser) {
				userProviderCache.put(new Element(user.getUserId(), up.getName()));
				updateCaches(sourcedUser);
				return up;
			}
		}
		return null;
	}

	private void updateCaches(User user) {
		long uid = user.getUserId();
		if (uid < 0L) {
			throw new IllegalArgumentException(L10NUtils.format("005134", uid));
		} else {
			this.userCache.put(new Element(uid, user));
			this.userIdCache.put(new Element(user.getUsername(), uid));
			return;
		}
	}
	
	private String getPasswordHash(User user) {
		String passwd;
		passwd = user.getPassword();
		if (null == passwd)
			return null;
		if (passwd.equals(""))
			return null;
		try {
			log.debug(user.getPassword()
					+ "=" 
					+ passwordEncoder.encodePassword(passwd, saltSource
							.getSalt(new ExtendedUserDetailsAdaptor(user))));
			return passwordEncoder.encodePassword(passwd,
					saltSource.getSalt(new ExtendedUserDetailsAdaptor(user)));
		} catch (Exception ex) {
			log.warn(L10NUtils.getMessage("005135"), ex);
		}
		return null;
	}
	
	private void wireTemplateDates(UserTemplate ut) {
		if (null == ut)
			return;
		if (null == ut.getCreationDate())
			ut.setCreationDate(new Date());
		if (null == ut.getModifiedDate())
			ut.setModifiedDate(new Date());
	}

	private String caseEmailAddress(User user) {
		return emailAddressCaseSensitive || user.getEmail() == null ? user
				.getEmail() : user.getEmail().toLowerCase();
	}

	protected UserTemplate translateFederated(UserTemplate usert) {
		if (null == usert)
			return null;
		if (usert.isFederated()) {
			usert.setGetPasswordHashSupported(false);
			usert.setSetEmailSuppoted(false);
			usert.setSetNameSupported(false);
			usert.setSetPasswordHashSupported(false);
			usert.setSetPasswordSupported(false);
			usert.setSetUsernameSupported(false);
		}
		return usert;
	}

	public List<User> findUsers(String nameOrEmail) {
		return userDao.findUsers(nameOrEmail);
	}

	public List<User> findUsers(String nameOrEmail, int startIndex, int numResults) {
		return userDao.findUsers(nameOrEmail, startIndex, numResults);
	}

	public int getFoundUserCount(String nameOrEmail) {
		return userDao.getFoundUserCount(nameOrEmail);
	}

	public int getUserCount(Company company) {
		return userDao.getUserCount(company);
	}
	
	public List<User> getUsers(Company company) {
		return userDao.getUsers(company);
	}

	public List<User> getUsers(Company company, int startIndex, int numResults) {
		return userDao.getUsers(company, startIndex, numResults);
	}

	public List<User> findUsers(Company company, String nameOrEmail) {
		return userDao.findUsers(company, nameOrEmail);
	}

	public List<User> findUsers(Company company, String nameOrEmail, int startIndex, int numResults) {
		return userDao.findUsers(company, nameOrEmail, startIndex, numResults);
	}

	public int getFoundUserCount(Company company, String nameOrEmail) {
		return userDao.getFoundUserCount(company, nameOrEmail);
	}

	public List<User> findUsersWithGroupFilter(Company company, Group group, String nameOrEmail) {
		 List<Long> IDs = userDao.findUserIds(company, group, nameOrEmail);
		 List<User> list = new ArrayList<User>(IDs.size());
		 for( Long userId : IDs)
		 {
			User user;
			try {
				user = getUser(userId);
				list.add(user);
			} catch (UserNotFoundException e) {
				log.warn(e);
			}			
		 }	 
		 return list;		
	}
	
	public List<User> findUsersWithGroupFilter(Company company, Group group, String nameOrEmail, int startIndex, int numResults) {
		 List<Long> IDs = userDao.findUserIds(company, group, nameOrEmail, startIndex, numResults);
		 List<User> list = new ArrayList<User>(IDs.size());
		 for( Long userId : IDs)
		 {
			User user;
			try {
				user = getUser(userId);
				list.add(user);
			} catch (UserNotFoundException e) {
				log.warn(e);
			}			
		 }	 
		 return list;			
	}
	
	public int getFoundUserCountWithGroupFilter(Company company, Group group, String nameOrEmail) {
		return userDao.getFoundUserCount(company, group, nameOrEmail);
	}	
}