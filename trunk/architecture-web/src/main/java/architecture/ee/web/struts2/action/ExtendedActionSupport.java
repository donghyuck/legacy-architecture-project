package architecture.ee.web.struts2.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import architecture.ee.security.AuthToken;
import architecture.ee.user.User;
import architecture.ee.util.AdminHelper;
import architecture.ee.util.SecurityHelper;
import architecture.ee.web.util.ServletUtils;

public class ExtendedActionSupport extends com.opensymphony.xwork2.ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware {

	public static final String CANCEL = "cancel";
	public static final String NOTFOUND = "notfound";
	public static final String UNAUTHORIZED = "unauthorized";
	public static final String DISABLED = "feature-disabled";
	public static final String UNAUTHENTICATED = "unauthenticated";

	protected Log log = LogFactory.getLog(getClass());
	
	protected HttpServletRequest request;

	protected HttpServletResponse response;

	protected Map<String, Object> session;

	private AuthToken authToken;

	private User user;

	
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public AuthToken getAuthToken() {
		if (null == authToken)
			authToken = SecurityHelper.getAuthToke();
		return authToken;
	}

    public final User getUser()
    {
    	
        if(null == user)
            try
            {
                User ju = SecurityHelper.getUser();
                if(ju != null)
                    user =  ju;//new ImmutableUser(SecurityHelper.getUser());
            }
            catch(Exception ex)
            {               
                log.warn("Unable to retrieve portal user from authentication context.");
            }
        return user;
    }
    
    public <T> T getComponnet(Class<T> requiredType) {
    	return AdminHelper.getApplicationHelper().getComponent(requiredType);
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

	public String input()
    {
        return "input";
    }
    
    public boolean isGuest()
    {
        return getAuthToken().isAnonymous();
    }
    
    public String getPageURL()
    {
        StringBuffer page = new StringBuffer();
        page.append(ServletUtils.getServletPath(ServletActionContext.getRequest()));
        String queryString = ServletActionContext.getRequest().getQueryString();
        if(queryString != null && !"".equals(queryString.trim()))
            page.append('?').append(queryString);
        return page.toString();
    }
}
