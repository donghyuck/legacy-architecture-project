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
package architecture.user.dao;

import java.util.List;

import architecture.common.user.Company;
import architecture.common.user.DomainMatcher;

public interface CompanyDao {

	public void createCompany(Company company);
	
	public Company getCompanyByName(String name, boolean caseInsensitive) ;
	
	public Company getCompanyById(long companyId);
	
	public void updateCompany(Company company);
	
	//public Company getCompanyByDomainName(String domainName);

	public List<DomainMatcher>  getCompanyDomainMatchers();
	
	/**
	 * 제공되지 않는 기능
	 * @return
	 */
	public List<Company> getCompanies( );
	
	/**
	 * 제공되지 않는 기능
	 * @return
	 */
	public List<Company> getCompanies( int start, int maxResults );
	
	
	public int getCompanyCount();
	
	
	public List<Long> getAllCompanyIds() ;

	
	public abstract List<Long> getCompanyIds(int start, int maxResults);	
		
	
	/**
	 * Company 에 해당하는 모든 그룹 수를 리턴한다.
	 * @return
	 */
	public int getCompanyGroupCount(long companyId);
	
	
	/**
	 * Company 에 해당하는 모든 그룹 아이디를 리턴한다.
	 * 
	 * @return
	 */
	public abstract List<Long> getCompanyGroupIds(long companyId);
	
	/**
	 * Company 에 해당하는 모든 그룹 아이디를 리턴한다.
	 * 
	 * @return
	 */	
	public abstract List<Long> getCompanyGroupIds(long companyId, int start, int maxResults);		

	/*
	public int getCompanyUserCount(long companyId);
	
	public abstract List<Long> getCompanyUserIds(long companyId);
	
	public abstract List<Long> getCompanyUserIds(long companyId, int start, int maxResults);
	*/
	
	
}
