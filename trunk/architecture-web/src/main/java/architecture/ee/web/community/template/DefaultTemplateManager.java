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
package architecture.ee.web.community.template;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Element;
import architecture.common.model.factory.AbstractObjectManager;
import architecture.common.user.Company;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.community.template.dao.TemplateDao;

public class DefaultTemplateManager extends AbstractObjectManager implements TemplateManager {

	private UserManager userManager ;

	private TemplateDao contentDao;
	
	/**
	 * @return contentDao
	 */
	public TemplateDao getContentDao() {
		return contentDao;
	}

	/**
	 * @param contentDao 설정할 contentDao
	 */
	public void setContentDao(TemplateDao contentDao) {
		this.contentDao = contentDao;
	}

	/**
	 * @return userManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * @param userManager 설정할 userManager
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public List<Template> getContent(Company company) {
		return getContent(company.getModelObjectType(), company.getCompanyId());
	}

	public List<Template> getContent(User user) {
		return getContent(user.getModelObjectType(), user.getCompanyId());
	}

	public List<Template> getContent(int objectType, long objectId) {
		List<Long> ids = contentDao.getContentIds(objectType, objectId);
		List<Template> list = new ArrayList<Template>(ids.size());
		for( Long contentId : ids ){
			try {
				list.add(getContent(contentId));
			} catch (Exception e) {}
		}	
		return list;
	}

	
	public List<Template> getContent(int objectType, long objectId, int startIndex, int maxResults) {
		List<Long> ids = contentDao.getContentIds(objectType, objectId, startIndex, maxResults);
		List<Template> list = new ArrayList<Template>(ids.size());
		for( Long contentId : ids ){
			try {
				list.add(getContent(contentId));
			} catch (Exception e) {}
		}	
		return list;
	}

	public int getContentCount(int objectType, long objectId) {
		return contentDao.getContentCount(objectType, objectId);
	}

	public Template getContent(long contentId) throws NotFoundException {
		Template content = null  ;
		if( objectCache.get(contentId) == null){
			try {
				content = getContentById(contentId);
				
				if( content.getCreator().getUserId() > 0 )
					content.setCreator( userManager.getUser(content.getCreator().getUserId()));
				
				if( content.getModifier().getUserId() > 0 )
					content.setModifier( userManager.getUser(content.getModifier().getUserId()));
								
				objectCache.put(new Element(contentId, content));
			} catch (Exception e) {
				 String msg = (new StringBuilder()).append("Unable to find image ").append(contentId).toString();
	             throw new NotFoundException(msg, e);
			}
		}else{
			content =  (Template) objectCache.get( contentId ).getValue();
		}		
		return content;
	}
	
	public Template getContentById(long contentId){
		return contentDao.getContent(contentId);
	}
	
}
