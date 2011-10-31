package architecture.ee.i18n.dao;

import java.util.List;

import architecture.ee.i18n.I18nCountry;

public interface I18nCountryDao {

	public abstract I18nCountry getCountryById(long ID);
		
	public abstract I18nCountry findByName(String name);
		
	public abstract I18nCountry findByA2(String a2);
		
	public abstract I18nCountry findByA3(String a3);
	
	public abstract List<I18nCountry> findByEnabled( boolean enabled );
	
	public abstract List<I18nCountry> findAll();
	
	public abstract List<I18nCountry> findAll(int start, int end);
	
	public abstract void deleteCountryById(long ID);
	
	public abstract void updateCountry ( I18nCountry country );
	
	public abstract int allCountryCount();
	
	public abstract int enabledCountryCount();
		
}
