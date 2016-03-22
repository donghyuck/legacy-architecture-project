/*
 * Copyright 2012, 2013 Donghyuck, Son
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
package architecture.ee.web.community.spring.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.connect.web.ConnectSupport;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.UserTemplate;
import architecture.ee.web.community.social.provider.ServiceProviderConfig;
import architecture.ee.web.community.social.provider.ServiceProviderHelper;
import architecture.ee.web.community.social.provider.connect.ConnectNotFoundException;
import architecture.ee.web.community.social.provider.connect.ConnectionFactoryLocator;
import architecture.ee.web.community.social.provider.connect.DefaultSocialConnect;
import architecture.ee.web.community.social.provider.connect.SocialConnect;
import architecture.ee.web.community.social.provider.connect.SocialConnect.Media;
import architecture.ee.web.community.social.provider.connect.SocialConnectManager;
import architecture.ee.web.community.social.provider.connect.Status;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

@Controller
@RequestMapping("/connect")
public class SocialConnectController implements InitializingBean {

    private static final Log log = LogFactory.getLog(SocialConnectController.class);

    private static final String DUPLICATE_CONNECTION_ATTRIBUTE = "social_addConnection_duplicate";

    private static final String PROVIDER_ERROR_ATTRIBUTE = "social_provider_error";

    private static final String AUTHORIZATION_ERROR_ATTRIBUTE = "social_authorization_error";

    private ConnectSupport connectSupport;

    private final UrlPathHelper urlPathHelper = new UrlPathHelper();

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Autowired
    @Qualifier("socialConnectManager")
    private SocialConnectManager socialConnectManager;

    @Autowired
    @Qualifier("freemarkerViewResolver")
    private ViewResolver freemarkerViewResolver;

    @Inject
    @Qualifier("userManager")
    UserManager userManager;

    @Inject
    @Qualifier("onetimeWebCache")
    Cache onetimeWebCache;

    @Inject
    @Qualifier("userDetailsService")
    UserDetailsService userDetailsService;

    private Object getOnetimeObject(String key) {
	if (onetimeWebCache.get(key) != null)
	    return onetimeWebCache.get(key).getValue();
	return null;
    }

    private void cleanUpOnetimeObject(String key) {
	if (onetimeWebCache.get(key) != null)
	    onetimeWebCache.remove(key);
    }

    private void setOnetimeObject(String key, Object value) {
	onetimeWebCache.put(new Element(key, value));
    }

    public SocialConnectController() {

    }

    @RequestMapping(value = "/list.json", method = RequestMethod.POST)
    @ResponseBody
    public Status connectionStatus(NativeWebRequest request, Model model) {
	setNoCache(request);
	return listConnectionStatus();
    }

    public static class SocialUserProfile {

	private User user;

	private UserProfile profile;

	private String imageUrl;

	public SocialUserProfile(User user, UserProfile profile) {
	    this.user = user;
	    this.profile = profile;
	}

	public User getUser() {
	    return user;
	}

	public void setUser(User user) {
	    this.user = user;
	}

	public UserProfile getProfile() {
	    return profile;
	}

	public void setProfile(UserProfile profile) {
	    this.profile = profile;
	}

	public String getImageUrl() {
	    return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
	    this.imageUrl = imageUrl;
	}

    }

    @RequestMapping(value = "/lookup.json", method = RequestMethod.POST)
    @ResponseBody
    public SocialUserProfile lookup(
	    @RequestParam(value = "onetime", defaultValue = "", required = false) String onetime,
	    HttpServletRequest request) {
	User user = SecurityHelper.getUser();
	SocialUserProfile sup = new SocialUserProfile(user, UserProfile.EMPTY);
	if (StringUtils.isEmpty(onetime)) {
	    onetime = (String) request.getSession().getAttribute("onetime");
	}
	if (!StringUtils.isEmpty(onetime) && getOnetimeObject(onetime) != null) {
	    SocialConnect sc = (SocialConnect) getOnetimeObject(onetime);
	    try {
		log.debug("search account user by:" + sc.getProviderId() + ", " + sc.getProviderUserId());
		User founduser = findUser(2, sc.getProviderId(), sc.getProviderUserId());
		sup.setUser(founduser);
	    } catch (UserNotFoundException e) {
	    }
	    try {
		UserProfile up = sc.getConnection().fetchUserProfile();
		sup.setImageUrl(sc.getConnection().getImageUrl());
		sup.setProfile(up);
	    } catch (Exception e) {
	    }
	}
	return sup;
    }

    @RequestMapping(value = "/signin.json", method = RequestMethod.POST)
    @ResponseBody
    public User signin(@RequestParam(value = "onetime", defaultValue = "", required = false) String onetime,
	    HttpServletRequest request) {
	User user = SecurityHelper.getUser();
	if (StringUtils.isEmpty(onetime)) {
	    onetime = (String) request.getSession().getAttribute("onetime");
	}
	log.debug("signin onetime:" + onetime);
	if (!StringUtils.isEmpty(onetime)) {
	    SocialConnect socialConnect = (SocialConnect) getOnetimeObject(onetime);
	    log.debug("signin account:" + socialConnect);
	    if (socialConnect != null) {
		log.debug("signin account user :" + socialConnect.getProviderUserId());
		User foundUser;
		try {
		    foundUser = findUser(2, socialConnect.getProviderId(), socialConnect.getProviderUserId());
		    log.debug("signin  foundUser :" + foundUser);
		    if (foundUser != null) {
			createSecurityContext(foundUser, request);
			UserTemplate template = new UserTemplate(foundUser);
			template.setLastLoggedIn(new Date());
			try {
			    userManager.updateUser(template);
			} catch (Exception e) {
			}

			try {
			    log.debug("UPDATE SOCIAL CONNECT DATA ");
			    SocialConnect existSocialConnect = socialConnectManager.getSocialConnect(foundUser,
				    socialConnect.getProviderId());
			    if (isUpdated(existSocialConnect, socialConnect.getConnection())) {
				setConnectionData(existSocialConnect, socialConnect.getConnection());
				socialConnectManager.updateSocialConnect(existSocialConnect);
			    }
			} catch (ConnectNotFoundException e) {
			    log.warn(e);
			}
			cleanUpOnetimeObject(onetime);
			request.getSession().removeAttribute("onetime");

			return foundUser;
		    }
		} catch (UserNotFoundException e1) {
		    log.debug(e1);
		}
	    }
	}
	return user;
    }

    @RequestMapping(value = "/{providerId}/authorize", method = RequestMethod.GET)
    public View authorize(@PathVariable String providerId, NativeWebRequest request, Model model) {
	setNoCache(request);
	return oauthRedirect(providerId, request);
    }

    public static class SocialNetwork {

	private ServiceProviderConfig config;
	private SocialConnect connect;

	public SocialNetwork(ServiceProviderConfig config) {
	    this.config = config;
	}

	public SocialConnect getConnect() {
	    return connect;
	}

	public void setConnect(SocialConnect connect) {
	    this.connect = connect;
	}

	public String getName() {
	    return this.config.getProvider();
	}

	public boolean isConnected() {
	    if (this.connect != null)
		return true;
	    return false;
	}

	public boolean isAllowSignin() {
	    return config.isAllowSignin();
	}

	/**
	 * @return allowSignup
	 */
	public boolean isAllowSignup() {
	    return config.isAllowSignup();
	}

    }

    @RequestMapping(value = "/all.json", method = RequestMethod.POST)
    @ResponseBody
    public List<SocialNetwork> connectionStatus2(NativeWebRequest request, Model model) {
	setNoCache(request);
	User user = SecurityHelper.getUser();

	List<SocialConnect> connections = socialConnectManager.findSocialConnects(user);
	List<ServiceProviderConfig> providers = ServiceProviderHelper.getAllServiceProviderConfig();
	List<SocialNetwork> list = new ArrayList<SocialNetwork>(providers.size());

	for (ServiceProviderConfig provider : providers) {
	    SocialNetwork social = new SocialNetwork(provider);
	    SocialConnect connect = getSocialConnectByProvider(connections, provider.getProvider());
	    if (connect != null) {
		social.setConnect(connect);
	    }
	    list.add(social);
	}

	return list;
    }

    protected SocialConnect getSocialConnectByProvider(List<SocialConnect> connections, String providerId) {
	SocialConnect connection = null;
	for (SocialConnect connect : connections) {
	    boolean match = architecture.common.util.StringUtils.equals(providerId.trim(),
		    connect.getProviderId().trim());
	    log.debug(providerId + ":" + connect.getProviderId() + "  - " + match);
	    if (match) {
		connection = connect;
		break;
	    }
	}
	return connection;
    }

    protected Status listConnectionStatus() {
	User user = SecurityHelper.getUser();
	List<SocialConnect> list = socialConnectManager.findSocialConnects(user);
	Status status = new Status();
	for (SocialConnect account : list) {
	    Connection<?> conn = account.getConnection();
	}
	status.setConnections(list);
	status.setMedia(ServiceProviderHelper.getAllServiceProviderConfig());
	return status;
    }

    /**
     * 
     * @param providerId
     * @param request
     * @return
     */
    protected RedirectView oauthRedirect(String providerId, NativeWebRequest request) {
	sessionStrategy.removeAttribute(request, PROVIDER_ERROR_ATTRIBUTE);
	ConnectionFactory<?> connectionFactory = ConnectionFactoryLocator.getConnectionFactory(providerId);
	MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
	try {
	    String callbackUrl = ServiceProviderHelper.getCallbackUrl(providerId);
	    connectSupport.setCallbackUrl(callbackUrl);
	    // String oauthUrl = connectSupport.buildOAuthUrl(connectionFactory,
	    // request, parameters);

	    // log.debug("oauth url:" + oauthUrl);

	    return new RedirectView(connectSupport.buildOAuthUrl(connectionFactory, request, parameters));
	} catch (Exception e) {
	    log.error("woops error..", e);
	    sessionStrategy.setAttribute(request, PROVIDER_ERROR_ATTRIBUTE, e);
	    return connectionStatusRedirect(providerId, request);
	}
    }

    @RequestMapping(value = "/{providerId}/profile.json", method = RequestMethod.POST)
    @ResponseBody
    public UserProfile profile(@PathVariable String providerId, NativeWebRequest request, Model model)
	    throws Exception {
	setNoCache(request);
	User user = SecurityHelper.getUser();
	SocialConnect account = socialConnectManager.getSocialConnect(user, providerId);
	return account.getConnection().fetchUserProfile();
    }

    /**
     * Render the status of the connections to the service provider to the user
     * as HTML in their web browser.
     */
    @RequestMapping(value = "/{providerId}", method = RequestMethod.GET)
    // @ResponseBody
    public Object connect(@PathVariable String providerId, NativeWebRequest request, Model model) {
	setNoCache(request);
	SocialConnectController.setOutputFormat(request);
	log.debug("status connections ==================");
	User user = SecurityHelper.getUser();
	SocialConnect account = null;
	if (!user.isAnonymous()) {
	    try {
		account = socialConnectManager.getSocialConnect(user, providerId);
		model.addAttribute("profile", account.getConnection().fetchUserProfile());
	    } catch (Exception e) {
		model.addAttribute("error", e.getMessage());
	    }
	} else {
	    String onetime = (String) sessionStrategy.getAttribute(request, "onetime");
	    log.debug("using onetime:" + onetime);
	    if (!StringUtils.isEmpty(onetime)) {
		account = (SocialConnect) getOnetimeObject(onetime);
		if (account != null) {

		    if (architecture.common.util.StringUtils.equals(account.getProviderId(), providerId)) {
			try {
			    log.debug("getting profile.");
			    UserProfile userProfile = account.getConnection().fetchUserProfile();
			    log.debug("user profile:" + userProfile);
			    model.addAttribute("profile", userProfile);
			} catch (Exception e) {
			    log.warn(e);
			    model.addAttribute("error", e.getMessage());
			}
		    } else {
			account = socialConnectManager.createSocialConnect(user,
				ServiceProviderHelper.toMedia(providerId));
		    }
		}
	    }
	}
	if (account == null) {
	    account = socialConnectManager.createSocialConnect(user, ServiceProviderHelper.toMedia(providerId));
	}
	model.addAttribute("user", user);
	model.addAttribute("connect", account);
	request.getNativeResponse(HttpServletResponse.class).setContentType("text/html;charset=UTF-8");
	return "/html/connect/social-status";
    }

    private User findUser(int objectType, String providerId, String providerUserId) throws UserNotFoundException {
	List<SocialConnect> list = socialConnectManager.findSocialConnects(objectType, providerId, providerUserId);
	if (list.size() > 0) {
	    long userId = list.get(0).getObjectId();
	    try {
		return userManager.getUser(userId);
	    } catch (UserNotFoundException e) {
	    }
	}
	throw new UserNotFoundException();
    }

    private void createSecurityContext(User userToUse, HttpServletRequest request) {
	if (userToUse.getUserId() > 0) {
	    UserDetails details = userDetailsService.loadUserByUsername(userToUse.getUsername());
	    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(details, null,
		    details.getAuthorities());
	    SecurityContextImpl context = new SecurityContextImpl();
	    context.setAuthentication(authentication);
	    SecurityContextHolder.setContext(context);
	    HttpSession httpsession = request.getSession(true);
	    httpsession.setAttribute("SPRING_SECURITY_CONTEXT", context);
	}
    }

    /**
     * Process the authorization callback from an OAuth 1 service provider.
     * Called after the user authorizes the connection, generally done by having
     * he or she click "Allow" in their web browser at the provider's site. On
     * authorization verification, connects the user's local account to the
     * account they hold at the service provider Removes the request token from
     * the session since it is no longer valid after the connection is
     * established.
     * 
     * Twitter
     */
    @RequestMapping(value = "/{providerId}", method = RequestMethod.GET, params = "oauth_token")
    public RedirectView oauth1Callback(@PathVariable String providerId, NativeWebRequest request) {
	try {
	    Media media = ServiceProviderHelper.toMedia(providerId);
	    OAuth1ConnectionFactory<?> connectionFactory = (OAuth1ConnectionFactory<?>) ConnectionFactoryLocator
		    .getConnectionFactory(media);
	    Connection<?> connection = connectSupport.completeConnection(connectionFactory, request);
	    log.debug("oauth1 connected.");

	    saveOrUpdate(providerId, connection, request);

	} catch (Exception e) {
	    sessionStrategy.setAttribute(request, PROVIDER_ERROR_ATTRIBUTE, e);
	    log.warn("Exception while handling OAuth1 callback (" + e.getMessage() + "). Redirecting to " + providerId
		    + " connection status page.");
	}
	return connectionStatusRedirect(providerId, request);
    }

    /**
     * Process the authorization callback from an OAuth 2 service provider.
     * Called after the user authorizes the connection, generally done by having
     * he or she click "Allow" in their web browser at the provider's site. On
     * authorization verification, connects the user's local account to the
     * account they hold at the service provider.
     * 
     * Facebook
     */
    @RequestMapping(value = "/{providerId}", method = RequestMethod.GET, params = "code")
    public RedirectView oauth2Callback(@PathVariable String providerId, NativeWebRequest request) {
	try {
	    Media media = ServiceProviderHelper.toMedia(providerId);
	    OAuth2ConnectionFactory<?> connectionFactory = (OAuth2ConnectionFactory<?>) ConnectionFactoryLocator
		    .getConnectionFactory(media);
	    Connection<?> connection = connectSupport.completeConnection(connectionFactory, request);
	    log.debug("oauth2 connected." + connection.getDisplayName());

	    saveOrUpdate(providerId, connection, request);

	} catch (Exception e) {
	    sessionStrategy.setAttribute(request, PROVIDER_ERROR_ATTRIBUTE, e);
	    log.warn("Exception while handling OAuth2 callback (" + e.getMessage() + "). Redirecting to " + providerId
		    + " connection status page.");
	}
	return connectionStatusRedirect(providerId, request);
    }

    private void saveOrUpdate(String providerId, Connection<?> connection, NativeWebRequest request) {
	User user = SecurityHelper.getUser();
	log.debug("save update social connect ============== " + connection.getDisplayName());
	if (!user.isAnonymous()) {
	    try {
		SocialConnect account = socialConnectManager.getSocialConnect(user, providerId);
		if (isUpdated(account, connection)) {
		    setConnectionData(account, connection);
		    socialConnectManager.updateSocialConnect(account);
		}
	    } catch (ConnectNotFoundException e) {
		socialConnectManager.createSocialConnect(user, connection);
	    }

	} else {
	    String onetime = UUID.randomUUID().toString();
	    log.debug("new onetime token:" + onetime);
	    SocialConnect account = socialConnectManager.createSocialConnect(user,
		    ServiceProviderHelper.toMedia(providerId));
	    log.debug("new connect:" + account.toString());
	    setConnectionData(account, connection);
	    setOnetimeObject(onetime, account);
	    log.debug("provider :" + account.getProviderUserId());
	    sessionStrategy.setAttribute(request, "onetime", onetime);
	}
	sessionStrategy.removeAttribute(request, PROVIDER_ERROR_ATTRIBUTE);
    }

    private boolean isUpdated(SocialConnect socialConnect, Connection<?> connection) {
	boolean updated = false;
	ConnectionData data = connection.createData();
	if (!architecture.common.util.StringUtils.equals(socialConnect.getProviderUserId(), data.getProviderUserId())) {
	    updated = true;
	    log.debug("ProviderUserId updated");
	} else if (!architecture.common.util.StringUtils.equals(socialConnect.getAccessToken(),
		data.getAccessToken())) {
	    updated = true;
	    log.debug("access token updated");
	} else if (!architecture.common.util.StringUtils.equals(socialConnect.getSecret(), data.getSecret())) {
	    updated = true;
	    log.debug("Secret updated");
	} else if (!architecture.common.util.StringUtils.equals(socialConnect.getDisplayName(),
		data.getDisplayName())) {
	    updated = true;
	    log.debug("DisplayName updated");
	} else if (!architecture.common.util.StringUtils.equals(socialConnect.getProfileUrl(), data.getProfileUrl())) {
	    updated = true;
	    log.debug("ProfileUrl updated");
	} else if (!architecture.common.util.StringUtils.equals(socialConnect.getImageUrl(), data.getImageUrl())) {
	    updated = true;
	    log.debug("ImageUrl updated");
	}
	return updated;
    }

    private void setConnectionData(SocialConnect connect, Connection<?> connection) {
	if (connect instanceof DefaultSocialConnect) {
	    Date now = new Date();
	    ConnectionData data = connection.createData();
	    log.debug("################# " + data.getProviderUserId());
	    DefaultSocialConnect connectToUse = (DefaultSocialConnect) connect;
	    connectToUse.setProviderUserId(data.getProviderUserId());
	    connectToUse.setAccessToken(data.getAccessToken());
	    connectToUse.setSecret(data.getSecret());
	    connectToUse.setDisplayName(data.getDisplayName());
	    connectToUse.setProfileUrl(data.getProfileUrl());
	    connectToUse.setImageUrl(data.getImageUrl());
	    connectToUse.setExpireTime(data.getExpireTime());
	    connectToUse.setModifiedDate(now);
	    log.debug("#################" + connectToUse);
	}
    }

    /**
     * Process an error callback from an OAuth 2 authorization as described at
     * http://tools.ietf.org/html/rfc6749#section-4.1.2.1. Called after upon
     * redirect from an OAuth 2 provider when there is some sort of error during
     * authorization, typically because the user denied authorization.
     */
    @RequestMapping(value = "/{providerId}", method = RequestMethod.GET, params = "error")
    public RedirectView oauth2ErrorCallback(@PathVariable String providerId, @RequestParam("error") String error,
	    @RequestParam(value = "error_description", required = false) String errorDescription,
	    @RequestParam(value = "error_uri", required = false) String errorUri, NativeWebRequest request) {
	Map<String, String> errorMap = new HashMap<String, String>();
	errorMap.put("error", error);
	if (errorDescription != null) {
	    errorMap.put("errorDescription", errorDescription);
	}
	if (errorUri != null) {
	    errorMap.put("errorUri", errorUri);
	}
	sessionStrategy.setAttribute(request, AUTHORIZATION_ERROR_ATTRIBUTE, errorMap);
	return connectionStatusRedirect(providerId, request);
    }

    protected SocialConnect getSocialConnect(User user, Media media) throws ConnectNotFoundException {
	return socialConnectManager.getSocialConnect(user, media.name().toLowerCase());
    }

    /**
     * Returns a RedirectView with the URL to redirect to after a connection is
     * created or deleted. Defaults to "/connect/{providerId}" relative to
     * DispatcherServlet's path. May be overridden to handle custom redirection
     * needs.
     * 
     * @param providerId
     *            the ID of the provider for which a connection was created or
     *            deleted.
     * @param request
     *            the NativeWebRequest used to access the servlet path when
     *            constructing the redirect path.
     */
    protected RedirectView connectionStatusRedirect(String providerId, NativeWebRequest request) {
	HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
	String path = "/connect/" + providerId + getPathExtension(servletRequest);
	if (prependServletPath(servletRequest)) {
	    path = servletRequest.getServletPath() + path;
	}
	log.debug(path);
	request.getNativeResponse(HttpServletResponse.class).setContentType("text/html;charset=UTF-8");
	return new RedirectView(path, true);
    }

    public void afterPropertiesSet() throws Exception {
	this.connectSupport = new ConnectSupport(sessionStrategy);
    }

    // internal helpers
    private boolean prependServletPath(HttpServletRequest request) {
	return !this.urlPathHelper.getPathWithinServletMapping(request).equals("");
    }

    private String getPathExtension(HttpServletRequest request) {
	String fileName = WebUtils.extractFullFilenameFromUrlPath(request.getRequestURI());
	String extension = StringUtils.getFilenameExtension(fileName);
	return extension != null ? "." + extension : "";
    }

    private void setNoCache(NativeWebRequest request) {
	HttpServletResponse response = request.getNativeResponse(HttpServletResponse.class);
	if (response != null) {
	    response.setHeader("Pragma", "no-cache");
	    response.setDateHeader("Expires", 1L);
	    response.setHeader("Cache-Control", "no-cache");
	    response.addHeader("Cache-Control", "no-store");
	}
    }

    protected static void setOutputFormat(NativeWebRequest request) {
	HttpServletRequest httprequest = request.getNativeRequest(HttpServletRequest.class);
	HttpServletResponse httpresponse = request.getNativeResponse(HttpServletResponse.class);
	httprequest.setAttribute("output", "json");
    }

    /*
     * 
     * 
     * private String getViewPath() { return viewPath; }
     */
}
