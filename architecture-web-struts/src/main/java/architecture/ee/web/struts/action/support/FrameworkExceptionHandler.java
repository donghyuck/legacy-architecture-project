package architecture.ee.web.struts.action.support;

import org.apache.struts.action.ExceptionHandler;

import architecture.common.exception.Codeable;

public class FrameworkExceptionHandler extends ExceptionHandler {

	@Override
	protected void logException(Exception e) {
		
		if( e instanceof Codeable ){
			
		} else {			
			
		}
		super.logException(e);
	}

}
