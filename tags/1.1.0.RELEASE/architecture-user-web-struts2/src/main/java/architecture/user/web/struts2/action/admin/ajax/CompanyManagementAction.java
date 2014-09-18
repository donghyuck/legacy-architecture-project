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
package architecture.user.web.struts2.action.admin.ajax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import architecture.common.user.Company;
import architecture.common.user.CompanyAlreadyExistsException;
import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyNotFoundException;
import architecture.common.user.Group;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;
import architecture.ee.web.ws.Property;

public class CompanyManagementAction extends FrameworkActionSupport  {
	
	private Long companyId ;
	private Integer pageSize = 15;
	private Integer startIndex = 0 ;
	private Company targetCompany ;
	private CompanyManager companyManager ;
	
	
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}


	public void setTargetCompany(Company targetCompany) {
		this.targetCompany = targetCompany;
	}
	public CompanyManager getCompanyManager() {
		return companyManager;
	}
	public void setCompanyManager(CompanyManager companyManager) {
		this.companyManager = companyManager;
	}
	
	public int getTotalCompanyCount(){		
		int totalCount = companyManager.getTotalCompanyCount();
        return totalCount;
    }
	

    public Company getTargetCompany() {

		if (companyId == null)
			log.warn("Edit profile for unspecified company.");
		
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
    

    public List<Company> getCompanies(){    	
    	List<Company> list ;    	
        if( pageSize > 0 ){        
        	list = companyManager.getCompanies(startIndex, pageSize);
        }else{
        	list = companyManager.getCompanies();        
        }        
        return list;
    }
    

	public List<Property> getTargetCompanyProperty() {
		Map<String, String> properties = getTargetCompany().getProperties();
		List<Property> list = new ArrayList<Property>();
		for (String key : properties.keySet()) {
			String value = properties.get(key);
			list.add(new Property(key, value));
		}
		return list;
	}
	
	public int getTotalCompanyGroupCount(){
		return companyManager.getTotalCompanyGroupCount(getTargetCompany());		
	}
	
	public List<Group>getCompanyGroups(){
		return companyManager.getCompanyGroups(getTargetCompany());		
	}
	
	public String createCompany() throws Exception {		
		Map map = ParamUtils.getJsonParameter(request, "item", Map.class);		
		String name = (String)map.get("name");
		String displayName = (String)map.get("displayName");
		String description = (String)map.get("description");
		String domainName = (String)map.get("domainName");
		this.targetCompany = companyManager.createCompany(name, displayName, domainName, description);
		return success();	
	}

	public String updateCompany() throws Exception {	
		
		try {
			Company company = getTargetCompany();
			Map map = ParamUtils.getJsonParameter(request, "item", Map.class);
			String name = (String)map.get("name");
			String description = (String)map.get("description");		
			String displayName = (String)map.get("displayName");
			String domainName = (String)map.get("domainName");
			
			if(!StringUtils.isEmpty(displayName))
			    company.setDisplayName(displayName);			
			if(!StringUtils.isEmpty(name))
			    company.setName(name);
			if(!StringUtils.isEmpty(description))
			    company.setDescription(description);		
			if(!StringUtils.isEmpty(domainName))
			    company.setDomainName(domainName);				
			
			companyManager.updateCompany(company);		
			this.targetCompany = null;		
			return success();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Exception(e);
		}	
		
	}
	

	public String updateCompanyProperties() throws Exception {		
		Company group = getTargetCompany();
		Map<String, String> properties = group.getProperties();
		List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);		
		for (Map row : list) {
			String n = (String) row.get("name");
			String v = (String) row.get("value");
			properties.put(n, v);
		}		
		updateTargetCompanyProperties(group, group.getProperties());
		return success();	
	}
	
	public String deleteCompanyProperties() throws Exception {
		Company group = getTargetCompany();
		Map<String, String> properties = group.getProperties();
		List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);
		for (Map row : list) {
			String n = (String) row.get("name");
			String v = (String) row.get("value");
			properties.remove(n);
		}
		updateTargetCompanyProperties( group, properties );		
		return success();
	}

	protected void updateTargetCompanyProperties(Company company, Map<String, String> properties) throws CompanyNotFoundException, CompanyAlreadyExistsException {		
		if( (company.getProperties().size() > 0 &&  properties.size() == 0 ) || properties.size() > 0 ){
			company.setProperties(properties);
			this.targetCompany = company;
			companyManager.updateCompany(company);
		}		
	}
	
	public String execute() throws Exception {      	
		return success();
	}  
}
