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
package architecture.ee.web.community.struts2.action.ajax;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

import com.opensymphony.xwork2.Preparable;

public class MyDomainAction extends FrameworkActionSupport implements  Preparable {

	private static final String DOMAIN_NAME_KEY = "domainName";
	
	private String domainName ;
		
	/**
	 * @return domainName
	 */
	public String getDomainName() {
		if(StringUtils.isEmpty(domainName)){	
			if( getSession().containsKey(DOMAIN_NAME_KEY)){
				this.domainName = (String) getSession().get(DOMAIN_NAME_KEY);
			}else{
				this.domainName = request.getLocalName() ;
			}
		}		
		//session.containsKey("domainName")
		//session.keySet()
		//request.getLocalAddr()
		return domainName;
	}

	/**
	 * @param domainName 설정할 domainName
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getLocalDomainName(){
		return request.getLocalName();
	}
	
	public String execute() throws Exception {		
		HttpSession session = request.getSession(true);			
		
		if(StringUtils.isNotEmpty(domainName)){			
			
			//String domainNameInSession = (String) getSession().get(DOMAIN_NAME_KEY); // 
			String domainNameInSession = (String) session.getAttribute(DOMAIN_NAME_KEY);			
			log.debug("domainName: " + domainName);
			log.debug("domainNameInSession: " + domainNameInSession);
			log.debug(StringUtils.equals(domainName, domainNameInSession));
			
			if( !StringUtils.equals(domainName, domainNameInSession)){
				//getSession().put(DOMAIN_NAME_KEY, domainName);
				session.setAttribute(DOMAIN_NAME_KEY, domainName);
			}
		}
		 
		Enumeration names = session.getAttributeNames();
		 while( names.hasMoreElements() ){
			 String key = (String) names.nextElement();
			 Object value = session.getAttribute(key);
			 log.debug( key + "=" + value);
		 }
		 
		return success();
	}

	public void prepare() throws Exception {
		response.addHeader("Access-Control-Allow-Origin", "*");
		//response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
	}
	
}
