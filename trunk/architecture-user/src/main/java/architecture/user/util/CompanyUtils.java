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
import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyNotFoundException;
import architecture.ee.util.ApplicationHelper;

public class CompanyUtils {
	
	public static Company getCompany(long companyId) throws CompanyNotFoundException{
		CompanyManager companyManger = getCompanyManager();
		return companyManger.getCompany(companyId);		
	}	
	
	public static Company getCompanyByDomainName(String domainName) throws CompanyNotFoundException{
		CompanyManager companyManger = getCompanyManager();
		return companyManger.getCompanyByDomainName(domainName);
	}		
	
	public static Company getDefaultCompany() throws CompanyNotFoundException {
		long companuId = getDefaultCompanyId();
		CompanyManager companyManger = getCompanyManager();
		Company company = companyManger.getCompany(companuId);
		return company;
	}
	
	public static CompanyManager getCompanyManager(){
		return ApplicationHelper.getComponent(CompanyManager.class);
	}
	
	public static Long getDefaultCompanyId(){
		long defaultCompanyId = ApplicationHelper.getApplicationLongProperty("components.company.default.companyId", 1L);
		return defaultCompanyId;
	}
	
	public static Long getDefaultMenuId(){
		long defaultMenuId = ApplicationHelper.getApplicationLongProperty("components.menu.default.menuId", 1L);
		return defaultMenuId;
	}
	
	public static boolean isAllowedGetByDomainName(){
		boolean getByDomainName = ApplicationHelper.getApplicationBooleanProperty("components.user.anonymous.company.getByDomainName", false);
		return getByDomainName;
	}

	public static boolean isallowedSignIn(Company company){
		return company.getBooleanProperty("allowedSignIn", true);
	}
	
	public static boolean isAllowedSignup(Company company){
		return company.getBooleanProperty("allowedSignup", true);
	}
	
}
