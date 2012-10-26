package architecture.ee.web.struts2.action.common;

import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

public class LoginAction extends FrameworkActionSupport {

    @Override
    public String execute() throws Exception {
        return input();
    }
	
    
}