package architecture.ee.web.struts2.action.common;

import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;

public class RedirectPageAction extends FrameworkActionSupport {

	private String url = null;
	
	public String getUrl(){
		return url;
	}
	
	public String execute() throws Exception {		
		url = ParamUtils.getParameter(getRequest(), "url");
		return SUCCESS;
	}

}
