package architecture.ee.g11n.dao;

import java.util.List;

import architecture.ee.g11n.Country;

public interface CountryDao {

	public abstract Country getCountryById(long ID);
		
	public abstract Country findByName(String name);
		
	public abstract Country findByA2(String a2);
		
	public abstract Country findByA3(String a3);
	
	public abstract List<Country> findByEnabled( boolean enabled );
	
	public abstract List<Country> findAll();
	
	public abstract List<Country> findAll(int start, int end);
	
	public abstract void deleteCountryById(long ID);
	
	public abstract void updateCountry ( Country country );
	
	public abstract int allCountryCount();
	
	public abstract int enabledCountryCount();
		
}
