package architecture.ee.web.struts2.action;

import java.util.Map;

import architecture.ee.util.ApplicationHelper;
import architecture.ee.util.OutputFormat;
import architecture.ee.web.struts2.interceptor.RefererInterceptor;
import architecture.ee.web.util.ParamUtils;

import com.opensymphony.xwork2.Validateable;

public class LoginAction extends FrameworkActionSupport implements Validateable {
	
	private static final long serialVersionUID = -1349336895281229035L;

	protected String successURL;
    protected boolean loginBanned;
    protected boolean authzFailed;
    protected boolean authnFailed;
    protected boolean accountDisabled;
    protected boolean registrationRequired;
    
    public LoginAction()
    {
        loginBanned = false;
        authzFailed = false;
        authnFailed = false;
        accountDisabled = false;
        registrationRequired = false;
    }

	@Override
	public String execute() throws Exception {

		this.authnFailed = ParamUtils.getBooleanParameter(getRequest(), "authnFailed", false);
		this.authzFailed = ParamUtils.getBooleanParameter(getRequest(), "authzFailed", false);
				
		if(this.loginBanned)
        {
            addActionError(getText("login.err.banned_login.text"));
            return UNAUTHENTICATED;
        }

		if(this.authzFailed)
            if(isGuest())
            {
                addActionError(getText("login.wrn.notAuthToViewCnt.info"));
                return UNAUTHENTICATED;
            } else
            {
                addActionError(getText("login.err.notAuthToViewCnt.info"));
                return UNAUTHENTICATED;
            }
				
		// 로그인에 실패한 경우:
        if(authnFailed)
        {
            addActionError(getText("login.err.invalid_login.text"));
            return UNAUTHENTICATED;
        }
        
        if(this.accountDisabled)
        {
            addActionError(getText("login.err.account_disabled.text"));
            return UNAUTHENTICATED;
        }
        
        if(registrationRequired)
        {
            addActionError(getText("reg.yr_acct_not_validated.text"));
            return UNAUTHENTICATED;
        }
        
        
        if( isGuest() ){
        	if(log.isInfoEnabled()){
                if(log.isInfoEnabled())
                    log.info(String.format("Unauthenticated access attempt for resource %s by %s.", new Object[] {
                        request.getRequestURI(), request.getRemoteAddr()
                    }));
        	}        	
        	return UNAUTHENTICATED;
        	
        }else
        {        	
            getRedirects();
            addActionMessage(getText("login.success.welcome.text", new String[]{ getUser().isNameVisible() ? getUser().getName() : getUser().getUsername() })); 
            
            if( OutputFormat.stingToOutputFormat( getDataType() ) == OutputFormat.XML  )
            	return "success-ajax";
            
            return SUCCESS;
        }
	}
    
	
	private void getRedirects(){
		Map<String, Object> session = getSession();
		if(session.containsKey(RefererInterceptor.URL_REFERER_KEY)){
			this.successURL =(String)session.get(RefererInterceptor.URL_REFERER_KEY);
		}
		
		//if( successURL == null || AdminHelper.getApplicationBooleanProperty("framework.pageCache.enabled", false) || isGuestAllowed() ){
		//	
		//}
	}

    public boolean isGuestAllowed()
    {    	
        return !ApplicationHelper.getApplicationBooleanProperty("framework.auth.disallowGuest", false);
    }
    
}