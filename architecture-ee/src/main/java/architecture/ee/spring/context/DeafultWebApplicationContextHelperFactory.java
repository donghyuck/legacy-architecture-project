package architecture.ee.spring.context;
import architecture.ee.services.ApplicationHelper;
import architecture.ee.services.ApplicationHelperFactory.Implementation;

public class DeafultWebApplicationContextHelperFactory implements Implementation {
	/**
	 * @uml.property  name="helper"
	 * @uml.associationEnd  
	 */
	private ApplicationHelper helper = null;
	
	public ApplicationHelper getApplicationHelper() {	
		// 이부분은 숨여야할 필요성이 있다.
		if(helper == null){
			this.helper = new DefaultApplicationContextHelper();
		}		
		return helper;
	}
}
