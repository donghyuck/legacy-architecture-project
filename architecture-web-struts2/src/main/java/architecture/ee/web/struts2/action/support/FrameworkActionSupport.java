package architecture.ee.web.struts2.action.support;

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

import architecture.common.exception.Codeable;
import architecture.common.exception.ComponentNotFoundException;
import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.user.Company;
import architecture.common.user.User;
import architecture.common.user.authentication.AuthToken;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.util.OutputFormat;
import architecture.ee.web.navigator.Menu;
import architecture.ee.web.navigator.MenuComponent;
import architecture.ee.web.navigator.MenuNotFoundException;
import architecture.ee.web.navigator.MenuRepository;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.struts2.interceptor.OutputFormatAware;
import architecture.ee.web.struts2.interceptor.WebSiteAware;
import architecture.ee.web.struts2.util.FrameworkTextProvider;
import architecture.ee.web.util.CookieUtils;
import architecture.ee.web.util.ServletUtils;
import architecture.ee.web.util.WebApplicationHelper;
import architecture.ee.web.util.WebSiteUtils;
import architecture.user.security.authentication.AuthenticationProviderFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.ParameterNameAware;
import com.opensymphony.xwork2.util.ValueStack;

public class FrameworkActionSupport extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware, ParameterNameAware, OutputFormatAware, WebSiteAware {
   
	public static final String CANCEL = "cancel";
	public static final String NOTFOUND = "notfound";
	public static final String UNAUTHORIZED = "unauthorized";
	public static final String DISABLED = "feature-disabled";
	public static final String UNAUTHENTICATED = "unauthenticated";
	public static final Long DEFAULT_MENU_ID = 1L;

	protected final transient Log log = LogFactory.getLog(getClass());

	protected HttpServletRequest request;

	protected HttpServletResponse response;

	protected Map<String, Object> session;

	protected AuthenticationProvider authProvider;

	private OutputFormat outputFormat = OutputFormat.HTML ;
	
	private final FrameworkTextProvider textProvider = new FrameworkTextProvider(getClass(), this);
	
	private AuthToken authToken;

	private User user;
	
	private WebSite webSite;
	
	private Menu webSiteMenu;
	
	private MenuRepository menuRepository ;
	
    protected Map<String, Object> models = new LinkedHashMap<String, Object>();
	
    public final void setAuthenticationProvider(AuthenticationProvider authProvider)
    {
        this.authProvider = authProvider;
    }
    
	public void setMenuRepository(MenuRepository menuRepository) {
		this.menuRepository = menuRepository;
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
	
	public String getReferer(){		
		
		return request.getHeader("Referer");
	}
	
	public OutputFormat getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(OutputFormat outputFormat) {
		this.outputFormat = outputFormat;
	}
	
	/**
	 * @return webSite
	 */
	public WebSite getWebSite() {
		return webSite;
	}

	/**
	 * @param webSite 설정할 webSite
	 */
	public void setWebSite(WebSite webSite) {
		this.webSite = webSite;
		try {
			this.webSiteMenu = WebSiteUtils.getWebSiteMenu(webSite);
		} catch (MenuNotFoundException e) {
			log.error(e);
		}
	}

	public MenuComponent getWebSiteMenu(String name) throws MenuNotFoundException { 
		if( webSiteMenu != null ){
			return menuRepository.getMenuComponent(webSiteMenu, name);
		}else{
			throw new MenuNotFoundException();
		}
	}

	public MenuComponent getWebSiteMenu(String name, String child) throws MenuNotFoundException { 
		if( webSiteMenu != null ){
			MenuComponent parentMenu = getWebSiteMenu(name);
			MenuComponent selectedMenu = null;
			for( MenuComponent childMenu : parentMenu.getComponents() )
			{
				if( child.equals( childMenu.getName() ) ){
					selectedMenu = childMenu;		
					break;
				}
				if( childMenu.getComponents().size() > 0 ){
					for( MenuComponent childMenu2 : childMenu.getComponents() ){
						if( child.equals( childMenu2.getName() ) ){
							selectedMenu = childMenu2;		
							break;
						}
					}
				}
			}
			return selectedMenu;		
		}else{
			throw new MenuNotFoundException();
		}
	}
	
	public AuthToken getAuthToken() {
		if ( null == authToken )
			authToken = AuthenticationProviderFactory.getSecurityContextAuthenticationProvider().getAuthToken();
		return authToken;
	}

    protected MenuRepository getMenuRepository() {
		return menuRepository;
	}

	public String input(){
        if( getOutputFormat() == OutputFormat.JSON ){
            return  OutputFormat.JSON.name().toLowerCase() + "-" + INPUT ;
        } else if ( getOutputFormat() == OutputFormat.XML ){ 
            return  OutputFormat.XML.name().toLowerCase() + "-" + INPUT ;
        }
       return INPUT;
    }
   
    public String success(){
    	if( log.isDebugEnabled())
    		log.debug("output=" + getOutputFormat() );    	
        if( getOutputFormat() == OutputFormat.JSON ){
            return  OutputFormat.JSON.name().toLowerCase() + "-" + SUCCESS ;
        } else if ( getOutputFormat() == OutputFormat.XML ){ 
            return  OutputFormat.XML.name().toLowerCase() + "-" + SUCCESS ;
        }        
       return SUCCESS;
	}
    
   
    
    public final Company getCompany(){
    	return getUser().getCompany();
    }
	
	public final User getUser() {
		if (null == user)
			try {
				final User ju = AuthenticationProviderFactory.getSecurityContextAuthenticationProvider().getUser();
				if (ju != null)
					user = ju; // new ImmutableUser(SecurityHelper.getUser());				
				//리턴된 사용자 정보를 수정할 수 없도록 변경 불가 형식의 객체를 리턴한다.
				//
			} catch (final Exception ex) {
				if( log.isWarnEnabled() )
					log.warn("Unable to retrieve portal user from authentication context.");
			}
		return user;
	}
		
	protected int getModelTypeId(String code){
		return ModelTypeFactory.getTypeIdFromCode(code);
	}
	protected final <T> T getComponent(Class<T> requiredType) throws ComponentNotFoundException {
		return WebApplicationHelper.getComponent(requiredType);
	}

	protected final <T> T getComponent(String requiredName, Class<T> requiredType) throws ComponentNotFoundException {	
		return WebApplicationHelper.getComponent(requiredName, requiredType);
	}
	
	protected final String getApplicationProperty(String name, String defaultValue){		
		return ApplicationHelper.getApplicationProperty(name, defaultValue);
	}
	
	protected final boolean getApplicationBooleanProperty(String name, boolean defaultValue){
		return ApplicationHelper.getApplicationBooleanProperty(name, defaultValue);
	}
	
	protected final  int getApplicationIntProperty(String name, int defaultValue){		
		return ApplicationHelper.getApplicationIntProperty(name, defaultValue);
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
    
	protected boolean isCodeable (Throwable exception){
		if( exception instanceof Codeable )
			return true;
		else 
			return false;
	}
	
	public String getPageURL() {
		final StringBuffer page = new StringBuffer();
		page.append(ServletUtils.getServletPath(ServletActionContext.getRequest()));
		final String queryString = ServletActionContext.getRequest().getQueryString();
		if (queryString != null && !"".equals(queryString.trim()))
			page.append('?').append(queryString);		
		return page.toString();
	}
	
	
	
	public boolean acceptableParameterName(String parameterName) {
		return !parameterName.contains(".");
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
		return textProvider.getText(aTextName, defaultValue);
	}

	@Override
	public String getText(String aTextName, String defaultValue, String obj) {
		return textProvider.getText(aTextName, defaultValue, obj);
	}

	@Override
	public String getText(String aTextName, List<?> args) {
		return textProvider.getText(aTextName, args);
	}

	@Override
	public String getText(String aTextName, String defaultValue, List<?> args) {
		return textProvider.getText(aTextName, defaultValue, args);
	}

	@Override
	public String getText(String key, String defaultValue, String[] args) {
		return textProvider.getText(key, defaultValue, args);
	}

	@Override
	public String getText(String key, String defaultValue, List<?> args, ValueStack stack) {
		return textProvider.getText(key, defaultValue, args, stack);
	}

	@Override
	public ResourceBundle getTexts() {
		return textProvider.getTexts();
	}

	@Override
	public ResourceBundle getTexts(String aBundleName) {
		return textProvider.getTexts(aBundleName);
	}

}