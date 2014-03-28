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

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.common.user.Company;
import architecture.ee.component.admin.AdminHelper;

public abstract class AbstractCompanyManager implements CompanyManager , EventSource {
	
	protected Log log = LogFactory.getLog(getClass());
	protected EventPublisher eventPublisher;
	protected boolean caseInsensitiveCompanyNameMatch;
	protected Cache companyCache;
    protected Cache companyIdCache ;
    
    /* (비Javadoc)
	 * @see architecture.user.CompanyManager#getDomainMatchers()
	 */
	


	protected Cache companyDomainCache ;
        
	public AbstractCompanyManager() {
        this.caseInsensitiveCompanyNameMatch = true;
        this.companyCache = AdminHelper.getCache("companyCache");
        this.companyIdCache = AdminHelper.getCache("companyIdCache");
	}
	
	public void setCaseInsensitiveCompanyNameMatch(
			boolean caseInsensitiveCompanyNameMatch) {
		this.caseInsensitiveCompanyNameMatch = caseInsensitiveCompanyNameMatch;
	}

	/**
	 * @return companyDomainCache
	 */
	public Cache getCompanyDomainCache() {
		return companyDomainCache;
	}

	
	/**
	 * @param companyDomainCache 설정할 companyDomainCache
	 */
	public void setCompanyDomainCache(Cache companyDomainCache) {
		this.companyDomainCache = companyDomainCache;
	}

	public void setCompanyCache(Cache companyCache) {
		this.companyCache = companyCache;
	}

	public void setCompanyIdCache(Cache companyIdCache) {
		this.companyIdCache = companyIdCache;
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}	
			    
    public Company getCompany(String name)
        throws CompanyNotFoundException
    {
    	String nameToUse = caseCompanyName(name);
    	if( companyIdCache.get(nameToUse)  != null ){
    		log.debug( companyIdCache.get(nameToUse )) ;
    		Long companyId = (Long)companyIdCache.get(nameToUse).getValue();    		
    		return getCompany(companyId);
    	}else{
    		Company c = lookupCompany(nameToUse);
    		companyIdCache.put( new Element( nameToUse, c.getCompanyId() ) );
    		return getCompany(c.getCompanyId());
    	}
    }

	public Company getCompany(long groupId) throws CompanyNotFoundException {
		Company company = getCompanyInCache(groupId);		
		if (company == null) {
			 company = lookupCompany(groupId);
			 companyCache.put(new Element(groupId, company));
		}
		return company;
	}

	protected Company getCompanyInCache(long companyId){
		if( companyCache.get(companyId) != null)
			return  (Company) companyCache.get( companyId ).getValue();
		else 
			return null;
	}
    
    protected String caseCompanyName(String name)
    {
        return caseInsensitiveCompanyNameMatch ? name.toLowerCase() : name;
    }
    
    
    //protected abstract Company lookupCompanyByDomainName(String name) throws CompanyNotFoundException;
    
    protected abstract Company lookupCompany(String name) throws CompanyNotFoundException;
	
    protected abstract Company lookupCompany(long groupId)  throws CompanyNotFoundException;
    
    protected boolean nameEquals(Company c1, Company c2){
    	return c1.getName() != null && c2.getName() != null && caseCompanyName(c1.getName()).equals(caseCompanyName(c2.getName()));
    }
    
    protected void companyDomainNameUpdated(String oldCompanyDomainName){
        companyDomainCache.remove(oldCompanyDomainName);	
    }
    
    protected void companyNameUpdated(String oldCompanyName){
        companyIdCache.remove(caseCompanyName(oldCompanyName));	
    }
    
    protected void clearCompanyFromCache(Company group){
    	companyCache.remove(group.getCompanyId());
    }
}
