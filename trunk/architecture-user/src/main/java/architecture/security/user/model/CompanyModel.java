package architecture.security.user.model;

import architecture.common.model.ModelObject;

public interface CompanyModel extends ModelObject<Company> {

	/**
	 * Returns the company ID of this company.
	 *
	 * @return the company ID of this company
	 */
	public long getCompanyId();

	/**
	 * Sets the company ID of this company.
	 *
	 * @param companyId the company ID of this company
	 */
	public void setCompanyId(long companyId);

	
	public String getCompanyName();
	
	public void setCompanyName(String name);
	
	
	/**
	 * Returns the max users of this company.
	 *
	 * @return the max users of this company
	 */
	public int getMaxUsers();

	/**
	 * Sets the max users of this company.
	 *
	 * @param maxUsers the max users of this company
	 */
	public void setMaxUsers(int maxUsers);
	
}
