/*
 * Copyright 2012, 2013 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.ee.web.community.spring.controller;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import architecture.common.user.CompanyManager;
import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.community.announce.Announce;
import architecture.ee.web.community.announce.AnnounceManager;
import architecture.ee.web.community.announce.AnnounceNotFoundException;
import architecture.ee.web.community.announce.impl.DefaultAnnounce;
import architecture.ee.web.community.page.PageManager;
import architecture.ee.web.community.streams.PhotoStreamsManager;
import architecture.ee.web.logo.LogoManager;
import architecture.ee.web.model.DataSourceRequest;
import architecture.ee.web.model.ItemList;
import architecture.ee.web.navigator.MenuRepository;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteManager;
import architecture.ee.web.site.WebSiteNotFoundException;
import architecture.ee.web.util.WebSiteUtils;

@Controller("secure-community-website-mgmt-data-controller")
@RequestMapping("/secure/data")
public class SecureWebsiteMgmtDataController {

    private static final Log log = LogFactory.getLog(SecureWebsiteMgmtDataController.class);

    @Inject
    @Qualifier("photoStreamsManager")
    private PhotoStreamsManager photoStreamsManager;

    @Inject
    @Qualifier("imageManager")
    private ImageManager imageManager;

    @Inject
    @Qualifier("attachmentManager")
    private AttachmentManager attachmentManager;

    @Inject
    @Qualifier("announceManager")
    private AnnounceManager announceManager;

    @Inject
    @Qualifier("pageManager")
    private PageManager pageManager;

    @Inject
    @Qualifier("menuRepository")
    private MenuRepository menuRepository;

    @Inject
    @Qualifier("webSiteManager")
    private WebSiteManager webSiteManager;

    @Inject
    @Qualifier("companyManager")
    private CompanyManager companyManager;

    @Inject
    @Qualifier("logoManager")
    private LogoManager logoManager;

    public SecureWebsiteMgmtDataController() {
    }

    private static final ISO8601DateFormat formatter = new ISO8601DateFormat();
    
    public Date getISO8601Date(String date){
	try {
	    return formatter.parse(date);
	} catch (ParseException e) {
	    return null;
	}
    }
    
    /**
    * Announcement management.
    * 
    **/
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/mgmt/site/announce/list.json", method = RequestMethod.POST)
    @ResponseBody
    public ItemList getWebsiteAnnouncementList(@RequestBody DataSourceRequest request) throws WebSiteNotFoundException {	
	
	User user = SecurityHelper.getUser();	
	
	WebSite site = webSiteManager.getWebSiteById( request.getObjectId() );		
	
	Date startDate = null ;
	Date endDate = null ;	
	List<Announce> items = Collections.EMPTY_LIST;
	int totalCount = 0;
	if( request.getData().containsKey("startDate") ){
	    startDate = getISO8601Date(request.getData().get("startDate").toString());
	}
	if( request.getData().containsKey("endDate") ){
	    endDate = getISO8601Date(request.getData().get("endDate").toString());
	}
	
	if(startDate != null && endDate != null)
	{
	    items = announceManager.getAnnounces(30, site.getWebSiteId(), startDate, endDate);
	    totalCount = items.size();
	}else{
	    items = announceManager.getAnnounces(30, site.getWebSiteId());
	    totalCount = items.size();
	}	
	return new ItemList(items, totalCount);	
    }
        

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/mgmt/site/announce/update.json", method = RequestMethod.POST)
    @ResponseBody
    public Announce saveAnnounce(@RequestBody DefaultAnnounce announce, NativeWebRequest request)
	    throws AnnounceNotFoundException, WebSiteNotFoundException {
	
	User user = SecurityHelper.getUser();
	if (announce.getUser() == null && announce.getAnnounceId() == 0)
	    announce.setUser(user);

	if (user.isAnonymous() || user.getUserId() != announce.getUser().getUserId())
	    throw new UnAuthorizedException();

	
	Announce target;
	if (announce.getAnnounceId() > 0) {
	    target = announceManager.getAnnounce(announce.getAnnounceId());
	} else {
	    if (announce.getObjectType() == 30 && announce.getObjectId() == 0L) {
		announce.setObjectId( WebSiteUtils.getWebSite(request.getNativeRequest(HttpServletRequest.class)).getWebSiteId());
	    } else if (announce.getObjectType() == 1 && announce.getObjectId() == 0L) {
		announce.setObjectId(user.getCompanyId());
	    }
	    target = announceManager.createAnnounce(user, announce.getObjectType(), announce.getObjectId());
	}

	target.setSubject(announce.getSubject());
	target.setBody(announce.getBody());
	target.setStartDate(announce.getStartDate());
	target.setEndDate(announce.getEndDate());
	
	if (target.getAnnounceId() > 0) {
	    announceManager.updateAnnounce(target);
	} else {
	    announceManager.addAnnounce(target);
	}
	
	return target;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/mgmt/site/announce/delete.json", method = RequestMethod.POST)
    @ResponseBody
    public Boolean destoryAnnounce(
	    @RequestParam(value = "announceId", defaultValue = "0", required = true) Long announceId,
	    NativeWebRequest request) {
	User user = SecurityHelper.getUser();

	return true;
    }
    
}
