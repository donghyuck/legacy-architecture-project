package architecture.ee.spring.context.support;

import architecture.common.lifecycle.ApplicationHelper;
import architecture.common.lifecycle.ApplicationHelperFactory.Implementation;
import architecture.ee.bootstrap.Bootstrap;
import architecture.ee.spring.context.internal.ApplicationHelperImpl;

public class ApplicationHelperFactoryImpl implements Implementation {

	private ApplicationHelper helper = null;
	
	public ApplicationHelper getApplicationHelper() {	
		// 이부분은 숨여야할 필요성이 있다.
		if(helper == null){
			//AdminService adminService = Bootstrap.getBootstrapApplicationContext().getBean(AdminService.class);
			helper = new ApplicationHelperImpl(Bootstrap.getBootstrapApplicationContext());
			//helper.getComponent(AdminService.class).addStateChangeListener(helper);
		}
		return helper;
	}

}
