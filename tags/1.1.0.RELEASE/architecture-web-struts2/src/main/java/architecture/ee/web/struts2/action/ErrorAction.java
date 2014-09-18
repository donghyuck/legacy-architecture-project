package architecture.ee.web.struts2.action;

import architecture.common.util.StringUtils;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;

public class ErrorAction extends FrameworkActionSupport {
	
	private String status ;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public boolean isSetStatus(){
		if( !StringUtils.isEmpty(status) )
			return true;
		return false;
	}
	
	@Override
	public String execute() throws Exception {
		if(!StringUtils.isEmpty(ParamUtils.getParameter(request, "status"))){
			setStatus(ParamUtils.getParameter(request, "status"));
			return SUCCESS;
		}		
		return super.execute();
	}
		
}