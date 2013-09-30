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
package architecture.ee.web.struts2.action.admin;

import architecture.common.user.Company;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.user.CompanyManager;
import architecture.user.CompanyNotFoundException;

public class MainAction extends FrameworkActionSupport {
	
	private Long companyId = -1L ;
	
	private Company targetCompany ;
	
	private CompanyManager companyManager ;
	
	
	/**
	 * @return companyManager
	 */
	public CompanyManager getCompanyManager() {
		return companyManager;
	}
	/**
	 * @param companyManager 설정할 companyManager
	 */
	public void setCompanyManager(CompanyManager companyManager) {
		this.companyManager = companyManager;
	}
	public Long getCompanyId() {
		if( companyId == -1L ){
			companyId = getCompany().getCompanyId();
		}
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
    public Company getTargetCompany() {

		if (companyId == null)
			log.warn("Edit profile for unspecified company.");
		
		this.companyId = getCompanyId();
		
		if(targetCompany == null){
			try {
				targetCompany = companyManager.getCompany( (companyId).longValue() );
			} catch (CompanyNotFoundException e) {
				log.warn((new StringBuilder()).append("Could not load company object for id: ").append(companyId).toString());
				return null;
			}
		}
		return targetCompany ;
	}   
    
	@Override
    public String execute() throws Exception {  
        return success();
    }
}
