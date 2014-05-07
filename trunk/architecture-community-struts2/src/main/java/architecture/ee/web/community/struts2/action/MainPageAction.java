package architecture.ee.web.community.struts2.action;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import architecture.common.user.Company;
import architecture.ee.web.community.social.SocialNetwork;
import architecture.ee.web.community.social.SocialNetworkManager;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.util.WebSiteUtils;

import com.opensymphony.xwork2.Preparable;

public class MainPageAction extends PageAction implements Preparable {

	public static final String VIEW_HOMEPAGE = "homepage";

	public static final String VIEW_PERSONALIZED = "personalized";
	
	public static final String VIEW_STREAMS = "streams";

	private String view;

	private SocialNetworkManager socialNetworkManager;
	
	public MainPageAction() {
		
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

	public boolean hasCompanySocial () {
		 Company company = getCompany();
		 List<SocialNetwork> asl = socialNetworkManager.getSocialNetworks(company);
		 if( asl.size() > 0 ){
			 return true;
		 }else {
			 return false;
		 }
	}
	
	public List<SocialNetwork> getCompanySocials(){
		 try {
			Company company = getCompany();
			 return socialNetworkManager.getSocialNetworks(company);
		} catch (Exception e) {
			return Collections.EMPTY_LIST;
		}
	}
	
	public void prepare() throws Exception {
		if( StringUtils.isEmpty(view)){
			view = getApplicationProperty("view.html.page.main", VIEW_HOMEPAGE);
		}	
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getView() {
		return view;
	}

	@Override
	public String execute() throws Exception {
		if( view.equals( VIEW_HOMEPAGE ) ){
			WebSite webSiteUse = getWebSite();
			String templateToUse = webSiteUse.getProperty(WebSiteUtils.MAIN_PAGE_TEMPLATE_KEY, null);
			if( StringUtils.isNotEmpty(templateToUse)){
				setTemplate(templateToUse);
			}			
			log.debug( "template : " +  getTemplate()  );
			
			return VIEW_HOMEPAGE;
		}
		else	if(view.equals( VIEW_PERSONALIZED ) ){
			return VIEW_PERSONALIZED;	
		}	
		else	if( view.equals(VIEW_STREAMS)){
			return VIEW_STREAMS;
		}		
		return SUCCESS;
	}

}