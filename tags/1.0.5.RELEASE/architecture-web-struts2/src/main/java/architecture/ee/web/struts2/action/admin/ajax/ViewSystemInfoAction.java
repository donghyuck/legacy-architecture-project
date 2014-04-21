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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import architecture.common.license.License;
import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.DatabaseInfo;
import architecture.common.lifecycle.MemoryInfo;
import architecture.common.lifecycle.SystemInfo;
import architecture.common.lifecycle.SystemInformationService;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.ws.Property;

public class ViewSystemInfoAction extends FrameworkActionSupport {
	
	private SystemInformationService systemInformationService ;	

	public SystemInformationService getSystemInformationService() {
		return systemInformationService;
	}

	public void setSystemInformationService(
			SystemInformationService systemInformationService) {
		this.systemInformationService = systemInformationService;
	}
	
	
	public SystemInfo getSystemInfo(){
		return systemInformationService.getSystemInfo();
	}

	public License getLicenseInfo(){		
		return systemInformationService.getLicenseInfo();
	}
	
	public MemoryInfo getMemoryInfo(){		
		return systemInformationService.getMemoryInfo();
	}

	
	public List<DatabaseInfo> getDatabaseInfos(){		
		return systemInformationService.getDatabaseInfos();
	}
	
	public List<Property> getSetupApplicationProperties(){
		ApplicationProperties props = ApplicationHelper.getRepository().getSetupApplicationProperties();
		Collection<String> names = props.getPropertyNames();		
    	List<Property> list = new ArrayList<Property>(names.size());
    	for( String name : names ){
    		Object value = props.get(name);
    		if( name.contains("password"))
    			value = "**********";
    		list.add( new Property(name, value) ) ;
    	}
    	return list;
	}
	
	public List<Property> getApplicationProperties(){
    	ConfigService configService = ApplicationHelper.getConfigService();
    	List<String> names =  configService.getApplicationPropertyNames();
    	List<Property> list = new ArrayList<Property>(names.size());
    	for( String name : names){
    		String value = configService.getApplicationProperty(name);
    		if( name.contains("password"))
    			value = "**********";
    		list.add( new Property(name, value) ) ;
    	}
    	return list;
	}
	
    @Override
    public String execute() throws Exception {  
        return success();
    }    
        
}
