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

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import architecture.common.user.Company;
import architecture.common.user.CompanyManager;
import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.ee.exception.NotFoundException;


@Controller ("secure-user-data-controller")
@RequestMapping("/secure/data")
public class SecureUserDataController {

	private static final Log log = LogFactory.getLog(SecureUserDataController.class);
	
	
	@Inject
	@Qualifier("userManager")
	private UserManager userManager ;
	
	@Inject
	@Qualifier("companyManager")
	private CompanyManager companyManager;
	
	
	public SecureUserDataController() {
		// TODO 자동 생성된 생성자 스텁
	}


	@RequestMapping(value="/mgmt/company/list.json",method={RequestMethod.POST} )
	@ResponseBody
	public CompanyList getCompanyList(
			@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
			@RequestParam(value="pageSize", defaultValue="0", required=false ) Integer pageSize,
			NativeWebRequest request) throws NotFoundException {			
		
		User user = SecurityHelper.getUser();	
		int totalCount = companyManager.getTotalCompanyCount();
		if( pageSize > 0 ){			
			return new CompanyList( companyManager.getCompanies(startIndex, pageSize), totalCount );
		}else{
			return new CompanyList( companyManager.getCompanies(), totalCount );
		}
	}


	public static class CompanyList {
		
		private List<Company> companies ;
		
		private int totalCount ;

		/**
		 * @param companies
		 * @param totalCount
		 */
		public CompanyList(List<Company> companies, int totalCount) {
			super();
			this.companies = companies;
			this.totalCount = totalCount;
		}
		/**
		 * @return companies
		 */
		public List<Company> getCompanies() {
			return companies;
		}
		/**
		 * @param companies 설정할 companies
		 */
		public void setCompanies(List<Company> companies) {
			this.companies = companies;
		}
		/**
		 * @return totalCount
		 */
		public int getTotalCount() {
			return totalCount;
		}
		/**
		 * @param totalCount 설정할 totalCount
		 */
		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}	
		
	}	

}
