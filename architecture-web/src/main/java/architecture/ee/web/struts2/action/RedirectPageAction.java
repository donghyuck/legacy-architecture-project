package architecture.ee.web.struts2.action;

import architecture.ee.web.util.ParamUtils;

public class RedirectPageAction extends ExtendedActionSupport {

	private String url = null;
	
	public String getUrl(){
		return url;
	}
	
	@Override
	public String execute() throws Exception {
		
		url = ParamUtils.getParameter(getRequest(), "url");
		log.debug("url:" + url );
		return SUCCESS;
	}

}
