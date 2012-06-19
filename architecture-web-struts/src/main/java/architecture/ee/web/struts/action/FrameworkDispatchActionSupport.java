package architecture.ee.web.struts.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.exception.ComponentNotFoundException;
import architecture.ee.web.util.WebApplicationHelper;

public abstract class FrameworkDispatchActionSupport extends org.springframework.web.struts.DispatchActionSupport {

	protected Log log;

	public FrameworkDispatchActionSupport() {
		log = LogFactory.getLog(getClass());
	}

	protected final <T> T getComponent(Class<T> requiredType) throws ComponentNotFoundException {
		return WebApplicationHelper.getComponent(requiredType);
	}

	protected final void autowireComponent(Object obj) {
		WebApplicationHelper.autowireComponent(obj);
	}

}
