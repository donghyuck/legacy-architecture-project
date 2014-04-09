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

import java.util.Date;
import java.util.List;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.user.User;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.web.community.announce.Announce;
import architecture.ee.web.community.announce.AnnounceManager;
import architecture.ee.web.community.announce.AnnounceNotFoundException;
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
		
		if( this.announceId > 0 ){
			return announceManager.getAnnounce(this.announceId);
		}else{
			return announceManager.createAnnounce(getUser(), this.objectType , this.objectId);
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
				
		//AnnounceForm form = ParamUtils.getJsonParameter(request, "item", AnnounceForm.class);
		
		 //com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();		
		// AnnounceForm form = mapper.readValue( ParamUtils.getParameter(request, "item"), AnnounceForm.class);
		 
		//log.debug( form.toString() );
		
		
		AnnounceForm form = ParamUtils.getJsonParameter(request, "item", AnnounceForm.class);
		this.announceId = form.getAnnounceId();		
		this.objectType = form.getObjectType();		
		this.prepareObjectTypeAndObjectId();		
		Announce targetAnnounce = getTargetAnnounce();
		
		
		log.debug(">>" + form.toString() );
		log.debug(">>" + form.getStartDate() );
		log.debug(">>" + form.getEndDate() );
		
		log.debug(">>" + ( form.getStartDate().getTime() > form.getEndDate().getTime() ) );
		
		
		targetAnnounce.setSubject(form.getSubject());
		targetAnnounce.setBody(form.getBody());	
		targetAnnounce.setStartDate( form.getStartDate());
		targetAnnounce.setEndDate(form.getEndDate());
		
		if(targetAnnounce.getAnnounceId() > 0 ){
			announceManager.updateAnnounce(targetAnnounce);		
		}else{
			announceManager.addAnnounce(targetAnnounce);		
			 this.announceId = targetAnnounce.getAnnounceId();
		}		
		return success();
	}	
	
	public String delete() throws Exception{				
		if( this.announceId > 0)
			announceManager.deleteAnnounce(announceId);			
		return success();
	}
	
	
	public static class AnnounceForm {
		
		private Long announceId = 0L;
		
		private Integer objectType = 0;
		
		private String subject ;
		
		private String body;
		
		private Date startDate ;
		
		private Date endDate;

		private Date modifiedDate;
		
		private Date creationDate;
		
		private User user;
		
		/**
		 * @return user
		 */
		public User getUser() {
			return user;
		}

		/**
		 * @param user 설정할 user
		 */
		public void setUser(User user) {
			this.user = user;
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

		/**
		 * @return subject
		 */
		public String getSubject() {
			return subject;
		}

		/**
		 * @param subject 설정할 subject
		 */
		public void setSubject(String subject) {
			this.subject = subject;
		}

		/**
		 * @return body
		 */
		public String getBody() {
			return body;
		}

		/**
		 * @param body 설정할 body
		 */
		public void setBody(String body) {
			this.body = body;
		}

		/**
		 * @return startDate
		 */
		public Date getStartDate() {
			return startDate;
		}

		/**
		 * @param startDate 설정할 startDate
		 */
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}

		/**
		 * @return endDate
		 */
		public Date getEndDate() {
			return endDate;
		}

		/**
		 * @param endDate 설정할 endDate
		 */
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
				

		/**
		 * @return modifiedDate
		 */
		public Date getModifiedDate() {
			return modifiedDate;
		}

		/**
		 * @param modifiedDate 설정할 modifiedDate
		 */
		public void setModifiedDate(Date modifiedDate) {
			this.modifiedDate = modifiedDate;
		}

		/**
		 * @return creationDate
		 */
		public Date getCreationDate() {
			return creationDate;
		}

		/**
		 * @param creationDate 설정할 creationDate
		 */
		public void setCreationDate(Date creationDate) {
			this.creationDate = creationDate;
		}

		/* (비Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("AnnounceForm [");
			if (announceId != null)
				builder.append("announceId=").append(announceId).append(", ");
			if (objectType != null)
				builder.append("objectType=").append(objectType).append(", ");
			if (subject != null)
				builder.append("subject=").append(subject).append(", ");
			if (body != null)
				builder.append("body=").append(body).append(", ");
			if (startDate != null)
				builder.append("startDate=").append(startDate).append(", ");
			if (endDate != null)
				builder.append("endDate=").append(endDate);
			builder.append("]");
			return builder.toString();
		}
	}
	
}
