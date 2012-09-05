package architecture.ee.web.struts.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.exception.Codeable;
import architecture.common.exception.ComponentNotFoundException;
import architecture.ee.web.util.WebApplicationHelper;

@SuppressWarnings("deprecation")
public abstract class FrameworkActionSupport extends
		org.springframework.web.struts.ActionSupport {

	protected Log log;

	public FrameworkActionSupport() {
		log = LogFactory.getLog(getClass());
	}

	protected final <T> T getComponent(Class<T> requiredType)
			throws ComponentNotFoundException {
		return WebApplicationHelper.getComponent(requiredType);
	}

	protected final <T> T getComponent(String requiredName, Class<T> requiredType)
			throws ComponentNotFoundException {
		return WebApplicationHelper.getComponent(requiredName, requiredType);
	}

	protected final void autowireComponent(Object obj) {
		WebApplicationHelper.autowireComponent(obj);
	}
	
	protected boolean isCodeable (Throwable exception){
		if( exception instanceof Codeable )
			return true;
		else 
			return false;
	}


}
