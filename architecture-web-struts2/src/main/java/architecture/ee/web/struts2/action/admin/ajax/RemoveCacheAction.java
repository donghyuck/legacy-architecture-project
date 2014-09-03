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
package architecture.ee.web.struts2.action.admin.ajax;

import org.apache.commons.lang.StringUtils;

import architecture.ee.component.admin.AdminHelper;
import architecture.ee.web.struts2.action.admin.ajax.ViewCacheAction.CacheStats;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

public class RemoveCacheAction extends FrameworkActionSupport  {

	private String targetName;
	
	public RemoveCacheAction() {
		// TODO 자동 생성된 생성자 스텁
	}


	/**
	 * @return targetName
	 */
	public String getTargetName() {
		return targetName;
	}


	/**
	 * @param targetName 설정할 targetName
	 */
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}


	public net.sf.ehcache.Cache getTargetCache(){
		return AdminHelper.getCacheManager().getCache(targetName);
	}
	
	public CacheStats getTargetCacheStats(){
		return new CacheStats(getTargetCache());
	}
	
	public String[] getCacheNames () {		
		return AdminHelper.getCacheManager().getCacheNames();
	}
	
    public String execute() throws Exception {  
    	
    	log.debug( "targetName " +  targetName );
    	
    	if( StringUtils.isNotEmpty(targetName) && this.getTargetCache()!=null){
    		getTargetCache().removeAll();
    	} 
    	
        return success();
    }     
}
