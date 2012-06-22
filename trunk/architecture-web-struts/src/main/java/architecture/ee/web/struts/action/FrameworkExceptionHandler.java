package architecture.ee.web.struts.action;

import org.apache.struts.action.ExceptionHandler;

import architecture.ee.exception.ApplicationException;

public class FrameworkExceptionHandler extends ExceptionHandler {

	@Override
	protected void logException(Exception e) {
		
		if(e instanceof ApplicationException){
			
		}
		
		super.logException(e);
	}

}
