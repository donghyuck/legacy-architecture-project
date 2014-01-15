package architecture.ee.web.struts2.action;

import java.util.Collections;
import java.util.List;

import architecture.ee.web.community.social.SocialNetwork;
import architecture.ee.web.community.social.SocialNetworkManager;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

import com.opensymphony.xwork2.Preparable;

public class MyPageAction extends FrameworkActionSupport implements Preparable {

	private SocialNetworkManager socialNetworkManager;
	
	public MyPageAction() {
		
	}

	/**
	 * @return socialNetworkManager
	 */
	public SocialNetworkManager getSocialAccountManager() {
		return socialNetworkManager;
	}

	/**
	 * @param socialNetworkManager 설정할 socialNetworkManager
	 */
	public void setSocialAccountManager(SocialNetworkManager socialNetworkManager) {
		this.socialNetworkManager = socialNetworkManager;
	}

	public boolean hasSocialMedia () {
		 List<SocialNetwork> asl = socialNetworkManager.getSocialNetworks(getUser());
		 if( asl.size() > 0 ){
			 return true;
		 }else {
			 return false;
		 }
	}
	
	public List<SocialNetwork> getSocialMedias(){
		 try {
			 return socialNetworkManager.getSocialNetworks(getUser());
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