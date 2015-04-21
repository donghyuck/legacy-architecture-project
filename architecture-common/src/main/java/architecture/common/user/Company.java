
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

import architecture.common.model.BaseModelObject;

public interface Company extends BaseModelObject {
	
	/**
	 * @return companyId
	 */
	public abstract long getCompanyId();

	/**
	 * @param  companyId
	 */
	public abstract void setCompanyId(long companyId);
	

	public abstract String getDisplayName();
	
	public abstract void setDisplayName(String displayName);
	
	public String getDomainName();
	
	public void setDomainName(String domainName);
	
}
