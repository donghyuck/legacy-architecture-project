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
package architecture.common.user;

import java.util.List;

public interface CompanyManager {
	
	public abstract Company createCompany(String name) throws CompanyAlreadyExistsException;
	
	public abstract Company createCompany(String name, String displayName) throws CompanyAlreadyExistsException;

	public abstract Company createCompany(String name, String displayName, String description) throws CompanyAlreadyExistsException;
	
	public abstract Company createCompany(String name, String displayName, String domainName, String description) throws CompanyAlreadyExistsException;
	
	public abstract Company getCompany(long groupId) throws CompanyNotFoundException;

	public abstract Company getCompany(String name) throws CompanyNotFoundException;
	
	public abstract Company getCompanyByDomainName(String domainName) throws CompanyNotFoundException;
			
	public abstract List<DomainMatcher> getDomainMatchers();
	
	public abstract void updateCompany(Company group) throws CompanyNotFoundException, CompanyAlreadyExistsException;
	
	
	public abstract int getTotalCompanyCount();

	public abstract List<Company> getCompanies();

	public abstract List<Company> getCompanies(int startIndex, int numResults);
	
	
	public abstract int getTotalCompanyGroupCount(Company group);
	
	public abstract List<Group> getCompanyGroups(Company group);

	public abstract List<Group> getCompanyGroups(Company group, int startIndex, int numResults);
	
	
	public abstract int getTotalCompanyUserCount(Company company);	
	
	public abstract List<User> getCompanyUsers(Company company);

	public abstract List<User> getCompanyUsers(Company company, int startIndex, int numResults);
	
	
	public abstract List<User> findCompanyUsers(Company company, String nameOrEmail);
	
	public abstract List<User> findCompanyUsers(Company company, String nameOrEmail, int startIndex, int numResults);
	
	public abstract int getFoundCompanyUserCount(Company company, String nameOrEmail);
	
	
	
}
