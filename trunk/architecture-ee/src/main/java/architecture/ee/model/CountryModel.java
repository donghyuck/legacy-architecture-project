package architecture.ee.model;

import architecture.ee.g11n.Country;

public interface CountryModel  extends BaseModel<Country> {

public String getName();
	
	/**
	 * Gets the primary key of this account.
	 * 
	 * @return the primary key of this account
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this account
	 * 
	 * @param pk
	 *            the primary key of this account
	 */
	public void setPrimaryKey(long pk);

		
	
    public void setCountryId(long countryId);

    public long getCountryId();

	public void setName(String name);
	
	public String getA2();
	
	public void setA2(String a2);
	
	public String getA3();
	
	public void setA3(String a3);
	
	public String getNumber();
	
	public void setNumber(String number);
	
	public String getIdd();
	
	public void setIdd(String idd);
	
	public boolean isEnabled();
	
	public void setEnabled(boolean enabled);	
		
	
	
	
	public Object clone();

	public int compareTo(Country country);

	public int hashCode();

	public String toString();

	public String toXmlString();
	
}
