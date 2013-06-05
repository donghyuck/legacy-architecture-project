package architecture.ee.web.struts2.action;

import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

import com.opensymphony.xwork2.Preparable;

public class MainAction extends FrameworkActionSupport implements Preparable {

	public static final String VIEW_HOMEPAGE = "homepage";

	public static final String VIEW_PERSONALIZED = "personalized";

	private String view;

	public MainAction() {
		this.view = VIEW_HOMEPAGE;
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
		log.debug("main action called!!");
		return SUCCESS;
	}

}