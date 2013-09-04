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
import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.ee.util.ApplicationHelper;
import architecture.user.CompanyManager;
import architecture.user.CompanyNotFoundException;
import architecture.user.model.impl.CompanyImpl;

public class CompanyUtils {
	
	public static Company getCompany(long companyId) throws CompanyNotFoundException{
		if(companyId == -1L ){
			return new CompanyImpl();
		}		
		CompanyManager companyManger = ApplicationHelper.getComponent(CompanyManager.class);
		return companyManger.getCompany(companyId);		
	}	
}
