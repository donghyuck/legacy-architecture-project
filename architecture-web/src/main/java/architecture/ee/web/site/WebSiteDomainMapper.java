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
package architecture.ee.web.site;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class WebSiteDomainMapper implements Serializable {
	
	private long webSiteId ;
	private String[] domains ;
	
	/**
	 * @param domain
	 * @param webSiteId
	 */
	public WebSiteDomainMapper(long webSiteId, String domain) {
		this.domains = StringUtils.split(domain);
		this.webSiteId = webSiteId;
	}

	/**
	 * @return webSiteId
	 */
	public long getWebSiteId() {
		return webSiteId;
	}

	/**
	 * @return domain
	 */
	public String[] getDomains() {
		return domains;
	}
	
	public boolean isMatch(String domain){
		for(String str1 : domains){
			if( StringUtils.equalsIgnoreCase(str1, domain) ){
				return true;
			}
		}
		return false;
	}
	
}
