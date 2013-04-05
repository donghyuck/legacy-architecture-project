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
package architecture.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.common.exception.CodeableException;
import architecture.common.user.Company;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.user.dao.CompanyDao;
import architecture.user.model.impl.CompanyImpl;

public class DefaultCompanyManager extends AbstractCompanyManager {

	private CompanyDao companyDao;
	private GroupManager groupManager;	
	private UserManager userManager;
	
	
	
	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public CompanyDao getCompanyDao() {
		return companyDao;
	}

	public void setCompanyDao(CompanyDao companyDao) {
		this.companyDao = companyDao;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public Company createCompany(String name) throws CompanyAlreadyExistsException {
		try
        {
            getCompany(name);
            throw new CompanyAlreadyExistsException();
        }
        catch(CompanyNotFoundException unfe)
        {
        	Company company = new CompanyImpl();
        	company.setDescription(name);
        	company.setName(name);
        	company.setDisplayName(name);
        	Date companyCreateDate = new Date();
        	company.setCreationDate(companyCreateDate);
        	company.setModifiedDate(companyCreateDate);
        	companyDao.createCompany(company);
        	return company;
        }
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public Company createCompany(String name, String displayName) throws CompanyAlreadyExistsException {
		try
        {
			getCompany(name);
            throw new CompanyAlreadyExistsException();
        }
        catch(CompanyNotFoundException unfe)
        {
        	Company company = new CompanyImpl();
        	company.setDescription(name);
        	company.setDisplayName(displayName);
        	company.setName(name);
        	Date groupCreateDate = new Date();
        	company.setCreationDate(groupCreateDate);
        	company.setModifiedDate(groupCreateDate);
        	companyDao.createCompany(company);
        	return company;
        }
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public Company createCompany(String name, String displayName, String description) throws CompanyAlreadyExistsException {
		try
        {
			getCompany(name);
            throw new CompanyAlreadyExistsException();
        }
        catch(CompanyNotFoundException unfe)
        {
        	Company company = new CompanyImpl();
        	company.setDescription(description);
        	company.setDisplayName(displayName);
        	company.setName(name);
        	Date groupCreateDate = new Date();
        	company.setCreationDate(groupCreateDate);
        	company.setModifiedDate(groupCreateDate);
        	companyDao.createCompany(company);
        	return company;
        }
	}

	
	
	public int getTotalCompanyCount() {
		try {
			return companyDao.getCompanyCount();
		} catch (Exception e) {
			log.error(e);
		}
		return 0 ;
	}

	public List<Company> getCompanies() {
		List<Long> companyIds = companyDao.getAllCompanyIds();
		List<Company> list = new ArrayList<Company>(companyIds.size());
		for( Long companyId : companyIds )
			try {
				list.add(getCompany(companyId));
			} catch (CompanyNotFoundException e) {}		
		return list;
	}

	public List<Company> getCompanies(int startIndex, int numResults) {
		List<Long> companyIDs = companyDao.getCompanyIds(startIndex, numResults );
		List<Company> list = new ArrayList<Company>(companyIDs.size());
		for( Long companyId : companyIDs )
			try {
				list.add(getCompany(companyId));
			} catch (CompanyNotFoundException e) {}
		
		return list;
	}

	public void updateCompany(Company company) throws CompanyNotFoundException,
			CompanyAlreadyExistsException {
		
		Company original = companyDao.getCompanyById(company.getCompanyId());
		if (original == null)
			throw CodeableException.newException(CompanyAlreadyExistsException.class, 5141, company.getCompanyId()); //new GroupNotFoundException();
		
		String oldCompanyName = null;
		String newCompanyName = null;
		
		if( !nameEquals(original, company)){
			try{
				Company checked = getCompany(caseCompanyName(company.getName()));
			if( checked.getCompanyId() == company.getCompanyId() ){
				throw CodeableException.newException(CompanyAlreadyExistsException.class, 5143); //new GroupAlreadyExistsException("Group with this name already exists.");
			}
			}catch(CompanyNotFoundException e){
				oldCompanyName = original.getName();
				newCompanyName = company.getName();
			}
		}		
		company.setModifiedDate(new Date());
		companyDao.updateCompany(company);
		if( oldCompanyName != null && newCompanyName != null ){
			companyNameUpdated(oldCompanyName);
		}
		clearCompanyFromCache(company);		
	}

	@Override
	protected Company lookupCompany(String name)
			throws CompanyNotFoundException {
		Company g = companyDao.getCompanyByName(name, caseInsensitiveCompanyNameMatch);
	    if(g == null)
	        throw CodeableException.newException(CompanyNotFoundException.class, 5142, name);//new GroupNotFoundException((new StringBuilder()).append("No group found for with name ").append(name).toString());
	    else
	        return g;
	}

	@Override
	protected Company lookupCompany(long groupId)
			throws CompanyNotFoundException {
	    if(groupId == -2L)
	        return null ; //new RegisteredUsersGroup();
	    Company group = companyDao.getCompanyById(groupId);
	    if(group == null)
	        throw CodeableException.newException(CompanyNotFoundException.class, 5141, groupId); //new GroupNotFoundException((new StringBuilder()).append("No group found for with id ").append(groupId).toString());
	    else
	        return group;
	}

	public int getTotalCompanyGroupCount(Company group) {
		return companyDao.getCompanyGroupCount(group.getCompanyId());
	}

	public List<Group> getCompanyGroups(Company company) {
		List<Long> groupIds =  companyDao.getCompanyGroupIds(company.getCompanyId());
		List<Group> groups = new ArrayList<Group>(groupIds.size());		
		for(long groupId:groupIds){
			Group group;
			try {
				group =groupManager.getGroup(groupId);
				groups.add(group);
			} catch (GroupNotFoundException e) {				
			}			
		}		
		return groups;
	}

	public List<Group> getCompanyGroups(Company company, int startIndex, int numResults) {
		List<Long> groupIds = companyDao.getCompanyGroupIds(company.getCompanyId(), startIndex, numResults);
		List<Group> groups = new ArrayList<Group>(groupIds.size());		
		for(long groupId:groupIds){
			Group group;
			try {
				group =groupManager.getGroup(groupId);
				groups.add(group);
			} catch (GroupNotFoundException e) {				
			}			
		}		
		return groups;
	}

	public int getTotalCompanyUserCount(Company company) {
		return userManager.getUserCount(company);
	}

	public List<User> getCompanyUsers(Company company) {
		return userManager.getUsers(company);
	}

	public List<User> getCompanyUsers(Company company, int startIndex, int numResults) {
		return userManager.getUsers(company, startIndex, numResults);
	}

	public List<User> findCompanyUsers(Company company, String nameOrEmail) {
		return userManager.findUsers(company, nameOrEmail);
	}

	public List<User> findCompanyUsers(Company company, String nameOrEmail,
			int startIndex, int numResults) {
		return userManager.findUsers(company, nameOrEmail, startIndex, numResults);
	}

	public int getFoundCompanyUserCount(Company company, String nameOrEmail) {
		return userManager.getFoundUserCount(company, nameOrEmail);
	}

}
