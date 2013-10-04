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
package architecture.user.util;

import architecture.common.user.Company;
import architecture.ee.util.ApplicationHelper;
import architecture.user.CompanyManager;
import architecture.user.CompanyNotFoundException;

public class CompanyUtils {
	
	public static Company getCompany(long companyId) throws CompanyNotFoundException{
		CompanyManager companyManger = ApplicationHelper.getComponent(CompanyManager.class);
		return companyManger.getCompany(companyId);		
	}	
	
	public static Company getCompanyByDomainName(String domainName) throws CompanyNotFoundException{
		CompanyManager companyManger = ApplicationHelper.getComponent(CompanyManager.class);
		return companyManger.getCompanyByDomainName(domainName);
	}	
	
	
	public static Company getDefaultCompany() throws CompanyNotFoundException {
		long companuId = ApplicationHelper.getApplicationLongProperty("components.user.anonymous.company.defaultId", 1L);
		CompanyManager companyManger = ApplicationHelper.getComponent(CompanyManager.class);
		Company company = companyManger.getCompany(companuId);
		return company;
	}
	//boolean getByDomainName = ApplicationHelper.getApplicationBooleanProperty("components.user.anonymous.company.getByDomainName", false);
		
}
