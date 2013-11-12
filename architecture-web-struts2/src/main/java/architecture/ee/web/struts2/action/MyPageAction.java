package architecture.ee.web.struts2.action;

import java.util.Collections;
import java.util.List;

import architecture.ee.web.social.SocialAccount;
import architecture.ee.web.social.SocialAccountManager;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

import com.opensymphony.xwork2.Preparable;

public class MyPageAction extends FrameworkActionSupport implements Preparable {

	private SocialAccountManager socialAccountManager;
	
	public MyPageAction() {
		
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

	public boolean hasSocialMedia () {
		 List<SocialAccount> asl = socialAccountManager.getSocialAccounts(getUser());
		 if( asl.size() > 0 ){
			 return true;
		 }else {
			 return false;
		 }
	}
	
	public List<SocialAccount> getSocialMedias(){
		 try {
			 return socialAccountManager.getSocialAccounts(getUser());
		} catch (Exception e) {
			return Collections.EMPTY_LIST;
		}
	}
	
	public void prepare() throws Exception {

	}
	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}

}