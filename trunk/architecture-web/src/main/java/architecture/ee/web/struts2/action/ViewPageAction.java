package architecture.ee.web.struts2.action;

import architecture.ee.web.util.ParamUtils;

public class ViewPageAction extends ExtendedActionSupport {

	private String url = null;
	
	public String getUrl(){
		return url;
	}
	
	@Override
	public String execute() throws Exception {
		
		log.debug(getRequest());
		log.debug(getRequest().getParameterMap());
		
		url = ParamUtils.getParameter(getRequest(), "url");
		
		log.debug("url:" + url );
		
		return SUCCESS;
	}

}
