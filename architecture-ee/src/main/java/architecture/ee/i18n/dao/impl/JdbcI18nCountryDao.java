package architecture.ee.i18n.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import architecture.ee.i18n.I18nCountry;
import architecture.ee.i18n.dao.I18nCountryDao;
import architecture.ee.model.impl.I18nCountryModelImpl;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

public class JdbcI18nCountryDao extends ExtendedJdbcDaoSupport implements I18nCountryDao {

	class CountryRowMapper implements RowMapper<I18nCountry> {

		public I18nCountry mapRow(ResultSet rs, int rowNum) throws SQLException {	
			I18nCountryModelImpl c = new I18nCountryModelImpl();			
			c.setCountryId(rs.getLong(1));
			c.setName(rs.getString(2));
			c.setA2(rs.getString(3));
			c.setA3(rs.getString(4));
			c.setNumber(rs.getString(5));
			c.setIdd(rs.getString(6));			
			c.setEnabled( rs.getInt(7) == 1 ? true : false );			
			return c;
		}};

		
	private CountryRowMapper getCountryRowMapper () {
		return new CountryRowMapper();
	}
		
	public I18nCountry getCountryById(long ID) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("FRAMEWORK_V2.SELECT_COUNTRY_BY_ID").getSql(), new Object[]{ID}, new int[]{Types.INTEGER}, getCountryRowMapper ());
	}

	public I18nCountry findByName(String name) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("FRAMEWORK_V2.SELECT_COUNTRY_BY_NAME").getSql(), new Object[]{name}, new int[]{Types.VARCHAR}, getCountryRowMapper ());
	}

	public I18nCountry findByA2(String a2) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("FRAMEWORK_V2.SELECT_COUNTRY_BY_A2").getSql(), new Object[]{a2}, new int[]{Types.VARCHAR}, getCountryRowMapper ());
	}

	public I18nCountry findByA3(String a3) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("FRAMEWORK_V2.SELECT_COUNTRY_BY_A3").getSql(), new Object[]{a3}, new int[]{Types.VARCHAR}, getCountryRowMapper ());
	}

	public List<I18nCountry> findByEnabled(boolean enabled) {		
		int i = enabled ? 1 : 0 ;		
		return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_ALL_COUNTRY_WITH_ENABLED").getSql(), new Object[]{i}, new int[]{Types.INTEGER}, getCountryRowMapper() );		
	}

	public List<I18nCountry> findAll() {
		return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_ALL_COUNTRY").getSql(), getCountryRowMapper() );	
	}

	public List<I18nCountry> findAll(int start, int end) {	
		return (List<I18nCountry>)getExtendedJdbcTemplate().queryScrollable(getBoundSql("FRAMEWORK_V2.SELECT_ALL_COUNTRY").getSql(), start, end, getCountryRowMapper(), new Object [] {}, new int [] {});
	}

	public void deleteCountryById(long ID) {
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.DELETE_COUNTRY_BY_ID").getSql(), new Object [] {ID}, new int [] {Types.INTEGER});
	}

	public void updateCountry(I18nCountry country) {
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.UPDATE_COUNTRY").getSql(), 
			new Object [] {country.getName(), country.getA2(), country.getA3(), country.getNumber(), country.getIdd(), country.isEnabled()? 1 : 0, country.getCountryId()}, 
			new int [] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER});
	}

	public int allCountryCount() {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("FRAMEWORK_V2.COUNT_ALL_COUNTRY").getSql());
	}

	public int enabledCountryCount() {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("FRAMEWORK_V2.COUNT_ALL_COUNTRY_WITH_ENABLED").getSql(), new Object[]{1}, new int[]{Types.INTEGER});
	}

}
