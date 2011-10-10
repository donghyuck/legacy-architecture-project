package architecture.ee.web.struts2.action;

import architecture.ee.web.util.ParamUtils;

public class DynamicAction extends ExtendedActionSupport {

	private String path ;
	
	public String getPath(){
		return path;		
	}
	
	public void setPath(String path){
		this.path = path;
	}
		
	@Override
	public String execute() throws Exception {
		
		path = ParamUtils.getParameter(getRequest(), "url");
		
		return super.execute();
	}

}
