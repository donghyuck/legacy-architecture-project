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
package architecture.ee.web.template;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Element;
import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.AbstractModelManagerSupport;
import architecture.common.user.Company;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.template.dao.TemplateDao;

public class DefaultTemplateManager extends AbstractModelManagerSupport implements TemplateManager {

	private UserManager userManager ;

	private TemplateDao templateDao;
	
	/**
	 * @return contentDao
	 */
	public TemplateDao getTemplateDao() {
		return templateDao;
	}

	/**
	 * @param contentDao 설정할 contentDao
	 */
	public void setTemplateDao(TemplateDao templateDao) {
		this.templateDao = templateDao;
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

	public List<Template> getTemplate(Company company) {
		 List<Template> list = getTemplate(ModelTypeFactory.getTypeIdFromCode("COMPANY"), company.getCompanyId());
		
		 return list;
	}

	public List<Template> getTemplate(User user) {
		List<Template> list = getTemplate(ModelTypeFactory.getTypeIdFromCode("USER"), user.getUserId());
		return list;
	}

	public List<Template> getTemplate(int objectType, long objectId) {
		List<Long> ids = templateDao.getTemplateIds(objectType, objectId);
		List<Template> list = new ArrayList<Template>(ids.size());
		for( Long contentId : ids ){
			try {
				list.add(getTemplate(contentId));
			} catch (Exception e) {}
		}	
		return list;
	}

	
	public List<Template> getTemplate(int objectType, long objectId, int startIndex, int maxResults) {
		List<Long> ids = templateDao.getTemplateIds(objectType, objectId, startIndex, maxResults);
		List<Template> list = new ArrayList<Template>(ids.size());
		for( Long contentId : ids ){
			try {
				list.add(getTemplate(contentId));
			} catch (Exception e) {}
		}	
		return list;
	}

	public int getTemplateCount(int objectType, long objectId) {
		return templateDao.getTemplateCount(objectType, objectId);
	}

	public Template getTemplate(long contentId) throws NotFoundException {
		Template content = null  ;
		if( objectCache.get(contentId) == null){
			try {
				content = getTemplateById(contentId);
				
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
	
	public Template getTemplateById(long templateId){
		return templateDao.getTemplate(templateId);
	}
	
}
