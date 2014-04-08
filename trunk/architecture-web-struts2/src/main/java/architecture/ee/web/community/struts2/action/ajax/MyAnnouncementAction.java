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

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.web.community.announce.Announce;
import architecture.ee.web.community.announce.AnnounceManager;
import architecture.ee.web.community.announce.AnnounceNotFoundException;
import architecture.ee.web.community.announce.impl.AnnounceImpl;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;

public class MyAnnouncementAction extends FrameworkActionSupport {

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
		return announceManager.getAnnounces(objectType, objectId);
	}
	
	public Announce getTargetAnnounce() throws AnnounceNotFoundException{
		
		if( announceId > 0 ){
			return announceManager.getAnnounce(announceId);
		}else{
			return announceManager.createAnnounce(getUser(), objectType , objectId);
		}		
	}
	
	/**
	 * @return announceManager
	 */
	public AnnounceManager getAnnounceManager() {
		return announceManager;
	}

	public int getTotalAnnounceCount(){
		return announceManager.countAnnounce(objectType, objectId);
	}

	/**
	 * @param announceManager 설정할 announceManager
	 */
	public void setAnnounceManager(AnnounceManager announceManager) {
		this.announceManager = announceManager;
	}

	private void prepareObjectTypeAndObjectId(){
		if( this.isGuest() ){
			 this.objectType = ModelTypeFactory.getTypeIdFromCode("WEBSITE");
			this.objectId = getWebSite().getWebSiteId();
		}else{
			if( objectType == 0 ){
				this.objectType = getUser().getCompany().getModelObjectType() ;
				this.objectId = getUser().getCompany().getCompanyId();
			} else if ( objectType == ModelTypeFactory.getTypeIdFromCode("WEBSITE")){
				this.objectId = getWebSite().getWebSiteId();
			} else if ( objectType == ModelTypeFactory.getTypeIdFromCode("COMPANY")){
				this.objectId = getUser().getCompany().getCompanyId();
			}	
		}
	}

	@Override
	public String execute() throws Exception {
		prepareObjectTypeAndObjectId();	
		return success();
	}
	
	public String update() throws Exception{
		
		if( isGuest() )
			throw new UnAuthorizedException("no permission.");		
				
		Announce form = ParamUtils.getJsonParameter(request, "item", AnnounceImpl.class);
		if( form.getAnnounceId() > 0 ){
			this.announceId = form.getAnnounceId();
		}
		if( form.getObjectType() > 0 ){
			this.objectType = form.getObjectType();
		}
		
		prepareObjectTypeAndObjectId();		
		Announce targetAnnounce = getTargetAnnounce();
		targetAnnounce.setSubject(form.getSubject());
		targetAnnounce.setBody(form.getBody());	
		targetAnnounce.setStartDate( form.getStartDate());
		targetAnnounce.setEndDate(form.getEndDate());
		
		if(targetAnnounce.getAnnounceId() > 0 ){
			announceManager.updateAnnounce(targetAnnounce);		
		}else{
			announceManager.addAnnounce(targetAnnounce);		
		}		
		return success();
	}	
	
	public String delete() throws Exception{				
		if( this.announceId > 0)
			announceManager.deleteAnnounce(announceId);			
		return success();
	}
	
}
