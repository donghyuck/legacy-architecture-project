package architecture.ee.web.struts2.action;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import architecture.common.user.Company;
import architecture.ee.web.community.social.SocialAccount;
import architecture.ee.web.community.social.SocialAccountManager;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

import com.opensymphony.xwork2.Preparable;

public class MainAction extends FrameworkActionSupport implements Preparable {

	public static final String VIEW_HOMEPAGE = "homepage";

	public static final String VIEW_PERSONALIZED = "personalized";

	private String view;

	private SocialAccountManager socialAccountManager;
	
	public MainAction() {
		
	}

	/**
	 * @return socialAccountManager
	 */
	public SocialAccountManager getSocialAccountManager() {
		return socialAccountManager;
	}

	/**
	 * @param socialAccountManager 설정할 socialAccountManager
	 */
	public void setSocialAccountManager(SocialAccountManager socialAccountManager) {
		this.socialAccountManager = socialAccountManager;
	}

	public boolean hasCompanySocial () {
		 Company company = getCompany();
		 List<SocialAccount> asl = socialAccountManager.getSocialAccounts(company);
		 if( asl.size() > 0 ){
			 return true;
		 }else {
			 return false;
		 }
	}
	
	public List<SocialAccount> getCompanySocials(){
		 try {
			Company company = getCompany();
			 return socialAccountManager.getSocialAccounts(company);
		} catch (Exception e) {
			return Collections.EMPTY_LIST;
		}
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
		
		if( StringUtils.isEmpty(view)){
			return getApplicationProperty("view.html.page.main", SUCCESS);
		}else{
			if( view.equals( VIEW_HOMEPAGE ) )
				return VIEW_HOMEPAGE;
		
			if(view.equals( VIEW_PERSONALIZED ) )
				return VIEW_PERSONALIZED;	
		}
		return SUCCESS;
	}

}