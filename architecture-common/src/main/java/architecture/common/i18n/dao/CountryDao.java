package architecture.common.i18n.dao;

import java.util.List;

import architecture.common.i18n.Country;

public interface CountryDao {

	public abstract Country getCountryById(long countryId);
	
	public abstract List<Country> findAll();
	
	public abstract int allCountryCount();
	
}
