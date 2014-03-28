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
package architecture.ee.web.community.struts2.action.ajax;

import java.util.List;
import java.util.Map;

import architecture.common.user.authentication.UnAuthorizedException;
import architecture.common.util.DateUtils;
import architecture.ee.web.community.announce.Announce;
import architecture.ee.web.community.announce.AnnounceManager;
import architecture.ee.web.community.announce.AnnounceNotFoundException;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;

public class MyAnnouncementAction extends FrameworkActionSupport {

	//private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	
	private Long objectId = -1L; 
	
	private Integer objectType = 0;
	
	private AnnounceManager announceManager ;
	
	private Long announceId ;
		
	/**
	 * 
	 */
	public MyAnnouncementAction() {
	}

	/**
	 * @return announceId
	 */
	public Long getAnnounceId() {
		return announceId;
	}

	/**
	 * @param announceId 설정할 announceId
	 */
	public void setAnnounceId(Long announceId) {
		this.announceId = announceId;
	}


	/**
	 * @return objectId
	 */
	public Long getObjectId() {
		return objectId;
	}


	/**
	 * @param objectId 설정할 objectId
	 */
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}


	/**
	 * @return objectType
	 */
	public Integer getObjectType() {
		return objectType;
	}


	/**
	 * @param objectType 설정할 objectType
	 */
	public void setObjectType(Integer objectType) {
		this.objectType = objectType;
	}


	public List<Announce> getTargetAnnounces(){		
		if( objectType == 0 ){
			this.objectType = getCompany().getModelObjectType() ;
			this.objectId = getCompany().getCompanyId();
		}
		return announceManager.getAnnounces(objectType, objectId);
	}
	
	public Announce getTargetAnnounce() throws AnnounceNotFoundException{
		
		if( announceId > 0 ){
			return announceManager.getAnnounce(announceId);
		}else{
			return announceManager.createAnnounce(getUser(), getCompany().getModelObjectType() , getCompany().getCompanyId() );
		}		
	}
	
	/**
	 * @return announceManager
	 */
	public AnnounceManager getAnnounceManager() {
		return announceManager;
	}

	public int getTotalAnnounceCount(){
return 0;
	}

	/**
	 * @param announceManager 설정할 announceManager
	 */
	public void setAnnounceManager(AnnounceManager announceManager) {
		this.announceManager = announceManager;
	}


	@Override
	public String execute() throws Exception {
		return success();
	}
	
	public String update() throws Exception{
		
		if( isGuest() )
			throw new UnAuthorizedException("no permission.");		
		Map map = ParamUtils.getJsonParameter(request, "item", Map.class);
		
		if( announceId == null){
			Integer  selectedAnnounceId= (Integer)map.get("announceId");	
			announceId = selectedAnnounceId.longValue();
		}			

		String subject = (String)map.get("subject");
		String body = (String)map.get("body");
		String startDateString = (String)map.get("startDate");
		String endDateString = (String)map.get("endDate");
		
		log.debug( "startDateString:" + startDateString );
		log.debug( "endDateString:" + endDateString );		
		
		Announce targetAnnounce = getTargetAnnounce();		
		targetAnnounce.setSubject(subject);
		targetAnnounce.setBody(body);		
		
		try {			
			targetAnnounce.setStartDate( DateUtils.parseISODate(startDateString) );
		} catch (Exception ie) { }
				
		try {
			targetAnnounce.setEndDate(DateUtils.parseISODate(endDateString));
		} catch (Exception ie) {
		}		
		
		//log.debug(targetAnnounce);
		if(targetAnnounce.getAnnounceId() > 0 ){
			announceManager.updateAnnounce(targetAnnounce);		
		}else{
			announceManager.addAnnounce(targetAnnounce);		
		}		
		return success();
	}	
	
	public String delete() throws Exception{				
		announceManager.deleteAnnounce(announceId);				
		return success();
	}
	
}
