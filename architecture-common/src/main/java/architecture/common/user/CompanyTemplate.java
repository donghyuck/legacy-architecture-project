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

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import architecture.common.cache.CacheSizes;
import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.BaseModelObjectSupport;

public class CompanyTemplate extends BaseModelObjectSupport implements Company {

    private Long companyId;
    private String displayName;
    private String domainName;
    private int memberCount;

    public CompanyTemplate() {
	this.companyId = -1L;
	this.displayName = "unknown";
	Date now = Calendar.getInstance().getTime();
	this.setCreationDate(now);
	this.setModifiedDate(now);
	this.memberCount = 0;
    }

    /**
     * @param companyId
     * @param displayName
     * @param domainName
     */
    public CompanyTemplate(Long companyId) {
	this.companyId = companyId;
    }

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    public long getCompanyId() {
	return companyId;
    }

    /**
     * @return domainName
     */
    public String getDomainName() {
	return domainName;
    }

    /**
     * @param domainName
     *            설정할 domainName
     */
    public void setDomainName(String domainName) {
	this.domainName = domainName;
    }

    @JsonIgnore
    public Serializable getPrimaryKeyObject() {
	return companyId;
    }

    @JsonIgnore
    public int getModelObjectType() {
	return ModelTypeFactory.getTypeIdFromCode("COMPANY");
    }

    public void setCompanyId(long companyId) {
	this.companyId = companyId;
    }

    /**
     * @return memberCount
     */
    public int getMemberCount() {
	return memberCount;
    }

    /**
     * @param memberCount
     *            설정할 memberCount
     */
    public void setMemberCount(int memberCount) {
	this.memberCount = memberCount;
    }

    @JsonIgnore
    public int getCachedSize() {
	return CacheSizes.sizeOfLong() + CacheSizes.sizeOfString(getName()) + CacheSizes.sizeOfString(getDisplayName())
		+ CacheSizes.sizeOfString(getDescription()) + CacheSizes.sizeOfDate() + CacheSizes.sizeOfDate();
    }

}
