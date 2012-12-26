package architecture.user.model.impl;

import java.io.Serializable;

import architecture.common.cache.CacheSizes;
import architecture.common.model.ModelObjectType;
import architecture.common.model.impl.BaseModelObject;
import architecture.user.model.Company;
import architecture.user.model.CompanyModel;

public class CompanyModelImpl extends BaseModelObject<Company> implements CompanyModel {

	private long companyId = -1L;
	private String companyName ;
	private int maxUsers = 0;
	
	public Serializable getPrimaryKeyObject() {
		return companyId;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getMaxUsers() {
		return maxUsers;
	}

	public void setMaxUsers(int maxUsers) {
		this.maxUsers = maxUsers;
	}
	
	public int compareTo(Company o) {
		return 0;
	}


	public ModelObjectType getModelObjectType() {
		return ModelObjectType.COMPANY;
	}
	
	public int getCachedSize() {
		int size = 0 ;
		size += CacheSizes.sizeOfLong();
		size += CacheSizes.sizeOfString(companyName);
		size += CacheSizes.sizeOfInt();
		size += CacheSizes.sizeOfDate();
		size += CacheSizes.sizeOfDate();		
		return size;
	}
	
}
