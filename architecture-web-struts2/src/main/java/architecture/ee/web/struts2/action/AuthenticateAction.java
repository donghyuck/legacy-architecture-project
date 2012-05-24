package architecture.ee.web.struts2.action;

import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.util.ParamUtils;

import com.opensymphony.xwork2.Validateable;

public class AuthenticateAction extends FrameworkActionSupport implements Validateable {
	
	protected String successURL;
	
    protected boolean loginBanned;
    
    // 권한이 없습니다.
    protected boolean authzFailed;
    
    // 인증실패 여부 확인
    protected boolean authnFailed;
    
    // 
    protected boolean accountDisabled;
    protected boolean registrationRequired;
    protected String format;
    
    public AuthenticateAction()
    {
        loginBanned = false;
        authzFailed = false;
        authnFailed = false;
        accountDisabled = false;
        registrationRequired = false;
        format = "xml";
    }
    
	public String getSuccessURL() {
		return successURL;
	}

	public void setSuccessURL(String successURL) {
		this.successURL = successURL;
	}

	public boolean isLoginBanned() {
		return loginBanned;
	}

	public void setLoginBanned(boolean loginBanned) {
		this.loginBanned = loginBanned;
	}

	public boolean isAuthzFailed() {
		return authzFailed;
	}

	public void setAuthzFailed(boolean authzFailed) {
		this.authzFailed = authzFailed;
	}

	public boolean isAuthnFailed() {
		return authnFailed;
	}

	public void setAuthnFailed(boolean authnFailed) {
		this.authnFailed = authnFailed;
	}

	public boolean isAccountDisabled() {
		return accountDisabled;
	}

	public void setAccountDisabled(boolean accountDisabled) {
		this.accountDisabled = accountDisabled;
	}

	public boolean isRegistrationRequired() {
		return registrationRequired;
	}

	public void setRegistrationRequired(boolean registrationRequired) {
		this.registrationRequired = registrationRequired;
	}

	@Override
	public String execute() throws Exception {		
		
		this.format = ParamUtils.getParameter(getRequest(), "format", "html");
		this.authnFailed = ParamUtils.getBooleanParameter(getRequest(), "authnFailed", false);
		this.authzFailed = ParamUtils.getBooleanParameter(getRequest(), "authzFailed", false);		
		
		return SUCCESS;
	}
    
    public boolean isGuestAllowed()
    {
        return !ApplicationHelper.getApplicationBooleanProperty("framework.auth.disallowGuest", false);
    }
}