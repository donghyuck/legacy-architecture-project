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
package architecture.user.web.struts2.action.admin;

import architecture.common.user.Company;
import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyNotFoundException;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

public class MainAction extends FrameworkActionSupport  {

	private Long targetCompanyId = -1L ;
	
	private Company targetCompany ;

	private CompanyManager companyManager ;
	
	/**
	 * @return targetCompanyId
	 */
	public Long getTargetCompanyId() {
		return targetCompanyId;
	}

	/**
	 * @param targetCompanyId 설정할 targetCompanyId
	 */
	public void setTargetCompanyId(Long targetCompanyId) {
		this.targetCompanyId = targetCompanyId;
	}
	
    public Company getTargetCompany() {

		if (targetCompanyId == null)
			log.warn("Edit profile for unspecified company.");
		
		if( this.targetCompanyId < 1 ){
			targetCompanyId = getUser().getCompany().getCompanyId();			
		}
		
		if(targetCompany == null){
			try {
				targetCompany = companyManager.getCompany( (targetCompanyId).longValue() );
			} catch (CompanyNotFoundException e) {
				log.warn((new StringBuilder()).append("Could not load company object for id: ").append(targetCompanyId).toString());
				return null;
			}
		}
		return targetCompany ;
	}   

    
	/**
	 * @param companyManager 설정할 companyManager
	 */
	public void setCompanyManager(CompanyManager companyManager) {
		this.companyManager = companyManager;
	}

	@Override
    public String execute() throws Exception{
        return success();
    }  
    
}
