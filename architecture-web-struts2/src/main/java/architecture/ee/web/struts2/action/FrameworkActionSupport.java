package architecture.ee.web.struts2.action;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.security.authentication.AuthenticationProvider;

import architecture.common.user.User;
import architecture.common.user.authentication.AuthToken;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.util.OutputFormat;
import architecture.ee.web.struts2.util.FrameworkTextProvider;
import architecture.ee.web.util.CookieUtils;
import architecture.ee.web.util.ServletUtils;
import architecture.security.util.SecurityHelper;

import com.opensymphony.xwork2.util.ValueStack;

public class FrameworkActionSupport extends com.opensymphony.xwork2.ActionSupport 
        implements SessionAware, ServletRequestAware, ServletResponseAware {

	public static final String CANCEL = "cancel";
	public static final String NOTFOUND = "notfound";
	public static final String UNAUTHORIZED = "unauthorized";
	public static final String DISABLED = "feature-disabled";
	public static final String UNAUTHENTICATED = "unauthenticated";
	public static final String JSON = "json";
	public static final String XML = "xml";
		
	protected String dataType = OutputFormat.HTML.name().toLowerCase();

	protected final transient Log log = LogFactory.getLog(getClass());

	protected HttpServletRequest request;

	protected HttpServletResponse response;

	protected Map<String, Object> session;

	protected AuthenticationProvider authProvider;

	private final FrameworkTextProvider textProvider = new FrameworkTextProvider(getClass(), this);
	
	private AuthToken authToken;

	private User user;
	
	protected Map<String, Object> models = new LinkedHashMap<String, Object>();

	protected String view;
	
	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}
	
	public Map<String, Object> getModels() {
		return models;
	}

	public void setModels(Map<String, Object> models) {
		this.models = models;
	}
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType.toLowerCase();
	}

    public final void setAuthenticationProvider(AuthenticationProvider authProvider)
    {
        this.authProvider = authProvider;
    }
    
	public void setServletResponse(final HttpServletResponse response) {
		this.response = response;
	}

	public void setServletRequest(final HttpServletRequest request) {
		this.request = request;
	}

	public void setSession(final Map<String, Object> session) {
		this.session = session;
	}

	public void setAuthToken(AuthToken authToken) {
		this.authToken = authToken;
	}

	public AuthToken getAuthToken() {
		if ( null == authToken )
			authToken = SecurityHelper.getAuthToke();
		return authToken;
	}

	public final User getUser() {

		if (null == user)
			try {
				final User ju = SecurityHelper.getUser();
				if (ju != null)
					user = ju; // new ImmutableUser(SecurityHelper.getUser());
				
				// 
				//리턴된 사용자 정보를 수정할 수 없도록 변경 불가 형식의 객체를 리턴한다.
				//
			} catch (final Exception ex) {
				log.warn("Unable to retrieve portal user from authentication context.");
			}
		return user;
	}

	public <T> T getComponnet(final Class<T> requiredType) {
		return ApplicationHelper.getComponent(requiredType);
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public String input() {
		return "input";
	}

	public boolean isGuest() {
		return getAuthToken().isAnonymous();
	}

    protected String getGuestProperty(String name)
    {
        Cookie cookie = CookieUtils.getCookie(ServletActionContext.getRequest(), name);
        if(cookie != null)
            return cookie.getValue();
        else
            return null;
    }
    
    protected void setGuestProperty(String name, String value)
    {
        if(name != null && value != null)
            CookieUtils.setCookie(ServletActionContext.getRequest(), ServletActionContext.getResponse(), name, value);
    }
    
	public String getPageURL() {
		final StringBuffer page = new StringBuffer();
		page.append(ServletUtils.getServletPath(ServletActionContext.getRequest()));
		final String queryString = ServletActionContext.getRequest().getQueryString();
		if (queryString != null && !"".equals(queryString.trim()))
			page.append('?').append(queryString);
		
		return page.toString();
	}

	@Override
	public String getText(String key) {		
		return textProvider.getText(key);
	}

	@Override
	public String getText(String key, String[] args) {
		return textProvider.getText(key, args);
	}

	@Override
	public String getText(String key, String defaultValue, String[] args,
			ValueStack stack) {
		return textProvider.getText(key, defaultValue, args, stack);
	}

	@Override
	public String getText(String aTextName, String defaultValue) {
		// TODO Auto-generated method stub
		return textProvider.getText(aTextName, defaultValue);
	}

	@Override
	public String getText(String aTextName, String defaultValue, String obj) {
		// TODO Auto-generated method stub
		return textProvider.getText(aTextName, defaultValue, obj);
	}

	@Override
	public String getText(String aTextName, List<?> args) {
		// TODO Auto-generated method stub
		return textProvider.getText(aTextName, args);
	}

	@Override
	public String getText(String aTextName, String defaultValue, List<?> args) {
		// TODO Auto-generated method stub
		return textProvider.getText(aTextName, defaultValue, args);
	}

	@Override
	public String getText(String key, String defaultValue, String[] args) {
		// TODO Auto-generated method stub
		return textProvider.getText(key, defaultValue, args);
	}

	@Override
	public String getText(String key, String defaultValue, List<?> args,
			ValueStack stack) {
		// TODO Auto-generated method stub
		return textProvider.getText(key, defaultValue, args, stack);
	}

	@Override
	public ResourceBundle getTexts() {
		// TODO Auto-generated method stub
		return textProvider.getTexts();
	}

	@Override
	public ResourceBundle getTexts(String aBundleName) {
		// TODO Auto-generated method stub
		return textProvider.getTexts(aBundleName);
	}
		
}
