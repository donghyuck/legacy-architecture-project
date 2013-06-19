package architecture.ee.web.struts2.action;

import org.apache.commons.lang.StringUtils;

import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

import com.opensymphony.xwork2.Preparable;

public class MainAction extends FrameworkActionSupport implements Preparable {

	public static final String VIEW_HOMEPAGE = "homepage";

	public static final String VIEW_PERSONALIZED = "personalized";

	private String view;

	public MainAction() {
		
	}

	public void prepare() throws Exception {

	}

	public void setView(String view) {
		this.view = view;
	}

	public String getView() {
		return view;
	}

	@Override
	public String execute() throws Exception {
		
		if( !StringUtils.isEmpty(view) && view.equals( VIEW_HOMEPAGE ) )
			return VIEW_HOMEPAGE;
		
		if( !StringUtils.isEmpty(view) && view.equals( VIEW_PERSONALIZED ) )
			return VIEW_PERSONALIZED;
		
		log.debug("main action called!!");
		
		return SUCCESS;
	}

}