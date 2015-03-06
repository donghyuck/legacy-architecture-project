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
package architecture.user.spring.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import architecture.common.user.Company;
import architecture.common.user.CompanyAlreadyExistsException;
import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyNotFoundException;
import architecture.common.user.CompanyTemplate;
import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.spring.controller.WebDataController.ItemList;
import architecture.ee.web.ws.Property;
import architecture.ee.web.ws.Result;
import architecture.user.GroupManager;
import architecture.user.RoleManager;


@Controller ("secure-user-data-controller")
@RequestMapping("/secure/data")
public class SecureUserDataController {

	private static final Log log = LogFactory.getLog(SecureUserDataController.class);
	
	@Inject
	@Qualifier("roleManager")
	private RoleManager roleManager ;
	
	@Inject
	@Qualifier("userManager")
	private UserManager userManager ;

	@Inject
	@Qualifier("groupManager")
	private GroupManager groupManager ;
	
	@Inject
	@Qualifier("companyManager")
	private CompanyManager companyManager;
	
	
	public SecureUserDataController() {
		// TODO 자동 생성된 생성자 스텁
	}

	@RequestMapping(value="/mgmt/role/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ItemList getRoleList(
			@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
			@RequestParam(value="pageSize", defaultValue="0", required=false ) Integer pageSize,
			NativeWebRequest request) throws NotFoundException {		
		
		User user = SecurityHelper.getUser();	
		int totalCount = roleManager.getTotalRoleCount();		
		return new ItemList( roleManager.getRoles(), totalCount );
	
	}
	
	
	@RequestMapping(value="/mgmt/company/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ItemList getCompanyList(
			@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
			@RequestParam(value="pageSize", defaultValue="0", required=false ) Integer pageSize,
			NativeWebRequest request) throws NotFoundException {			
		
		User user = SecurityHelper.getUser();	
		int totalCount = companyManager.getTotalCompanyCount();
		if( pageSize > 0 ){			
			return new ItemList( companyManager.getCompanies(startIndex, pageSize), totalCount );
		}else{
			return new ItemList( companyManager.getCompanies(), totalCount );
		}
	}

	
	@RequestMapping(value="/mgmt/company/create.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result createCompany(
			@RequestBody CompanyTemplate newCompany,
			NativeWebRequest request) throws CompanyAlreadyExistsException {			
		
		User user = SecurityHelper.getUser();		
		companyManager.createCompany(newCompany.getName(), newCompany.getDisplayName(), newCompany.getDomainName(), newCompany.getDescription());		
		return Result.newResult();
	}	
	
	@RequestMapping(value="/mgmt/company/update.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result updateCompany(
			@RequestBody CompanyTemplate newCompany,
			NativeWebRequest request) throws CompanyNotFoundException, CompanyAlreadyExistsException {					
		User user = SecurityHelper.getUser();			
		Company oldCompany = companyManager.getCompany(newCompany.getCompanyId());
		if( !StringUtils.equals(newCompany.getName(), oldCompany.getName())){
			oldCompany.setName(newCompany.getName());
		}
		if( !StringUtils.equals(newCompany.getDisplayName(), oldCompany.getDisplayName())){
			oldCompany.setDisplayName(newCompany.getDisplayName());
		}		
		if( !StringUtils.equals(newCompany.getDomainName(), oldCompany.getDomainName())){
			oldCompany.setDomainName(newCompany.getDomainName());
		}	
		if( !StringUtils.equals(newCompany.getDescription(), oldCompany.getDescription())){
			oldCompany.setDescription(newCompany.getDescription());
		}	
		companyManager.updateCompany(oldCompany);		
		return Result.newResult();
	}	

	@RequestMapping(value="/mgmt/company/properties/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<Property> getCompanyPropertyList(
			@RequestParam(value="companyId", defaultValue="0", required=true ) Long companyId,
			NativeWebRequest request) throws CompanyNotFoundException {			
		
		User user = SecurityHelper.getUser();	
		Company company = companyManager.getCompany(companyId);		
		return toPropertyList(company.getProperties());
	}	

	@RequestMapping(value="/mgmt/company/properties/update.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result updateCompanyPropertyList(
			@RequestBody List<Property> newProperties,
			@RequestParam(value="companyId", defaultValue="0", required=true ) Long companyId,
			NativeWebRequest request) throws CompanyNotFoundException, CompanyAlreadyExistsException {			
		
		User user = SecurityHelper.getUser();	
		Company company = companyManager.getCompany(companyId);		
		Map<String, String> properties = company.getProperties();	
		for (Property property : newProperties) {
			properties.put(property.getName(), property.getValue().toString());
		}	
		if( newProperties.size() > 0){
			companyManager.updateCompany(company);
		}
		return Result.newResult();
	}
	
	@RequestMapping(value="/mgmt/company/properties/delete.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result deleteCompanyPropertyList(
			@RequestBody List<Property> newProperties,
			@RequestParam(value="companyId", defaultValue="0", required=true ) Long companyId,
			NativeWebRequest request) throws CompanyNotFoundException, CompanyAlreadyExistsException {			
		
		User user = SecurityHelper.getUser();	
		Company company = companyManager.getCompany(companyId);		
		Map<String, String> properties = company.getProperties();	
		for (Property property : newProperties) {
			properties.remove(property.getName());
		}
		if( newProperties.size() > 0){
			companyManager.updateCompany(company);
		}
		return Result.newResult();
	}
		
	@RequestMapping(value="/mgmt/company/groups/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ItemList getCompanyGroups(
			@RequestParam(value="companyId", defaultValue="0", required=true ) Long companyId,
			@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
			@RequestParam(value="pageSize", defaultValue="0", required=false ) Integer pageSize,
			NativeWebRequest request) throws CompanyNotFoundException, CompanyAlreadyExistsException {			
		
		User user = SecurityHelper.getUser();	
		Company company = companyManager.getCompany(companyId);		
		int totalCount = companyManager.getTotalCompanyGroupCount(company);
		companyManager.getCompanyGroups(company);
		if( pageSize > 0 ){			
			return new ItemList( companyManager.getCompanyGroups(company, startIndex, pageSize), totalCount );
		}else{
			return new ItemList( companyManager.getCompanyGroups(company), totalCount );
		}
	}	
	
	@RequestMapping(value="/mgmt/company/users/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ItemList getCompanyUsers(
			@RequestParam(value="companyId", defaultValue="0", required=true ) Long companyId,
			@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
			@RequestParam(value="pageSize", defaultValue="0", required=false ) Integer pageSize,
			NativeWebRequest request) throws CompanyNotFoundException, CompanyAlreadyExistsException {			
		
		User user = SecurityHelper.getUser();	
		
		Company company = companyManager.getCompany(companyId);		
		int totalCount = companyManager.getTotalCompanyUserCount(company);
		if( pageSize > 0 ){			
			return new ItemList( companyManager.getCompanyUsers(company, startIndex, pageSize), totalCount );
		}else{
			return new ItemList( companyManager.getCompanyUsers(company), totalCount );
		}
	}		
	
	protected List<Property> toPropertyList (Map<String, String> properties){
		List<Property> list = new ArrayList<Property>();
		for (String key : properties.keySet()) {
			String value = properties.get(key);
			list.add(new Property(key, value));
		}
		return list;
	}
}
