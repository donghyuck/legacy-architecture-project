package architecture.ee.web.struts2.action;

import com.opensymphony.xwork2.Preparable;

public class MainAction extends ExtendedActionSupport implements Preparable {

	public static final String VIEW_HOMEPAGE = "homepage";
	public static final String VIEW_PERSONALIZED = "personalized";
	
	private String view;
	
	
	public MainAction() {
	}

	public void prepare() throws Exception {
		
	}
	
    public String getView()
    {
        return view;
    }

	@Override
	public String execute() throws Exception {
		
		
		return SUCCESS;
	}
    
    
    
}
