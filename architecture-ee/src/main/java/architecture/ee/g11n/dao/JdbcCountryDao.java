package architecture.ee.g11n.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import architecture.ee.g11n.Country;
import architecture.ee.g11n.internal.CountryImpl;
import architecture.ee.jdbc.sequencer.dao.SequenceDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

public class JdbcCountryDao extends ExtendedJdbcDaoSupport implements CountryDao {

	class CountryRowMapper implements RowMapper<Country> {

		public Country mapRow(ResultSet rs, int rowNum) throws SQLException {	
			CountryImpl c = new CountryImpl();			
			c.setCountryId(rs.getLong(1));
			c.setName(rs.getString(2));
			c.setA2(rs.getString(3));
			c.setA3(rs.getString(4));
			c.setNumber(rs.getString(5));
			c.setIdd(rs.getString(6));			
			c.setEnabled( rs.getInt(7) == 1 ? true : false );			
			return c;
		}};

	private SequenceDao sequenceDao;
		 
	public void setSequenceDao(SequenceDao sequenceDao) {
		this.sequenceDao = sequenceDao;
	}
		
	private CountryRowMapper getCountryRowMapper () {
		return new CountryRowMapper();
	}
		
	public Country getCountryById(long ID) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("FRAMEWORK_V2.SELECT_COUNTRY_BY_ID").getSql(), new Object[]{ID}, new int[]{Types.INTEGER}, getCountryRowMapper ());
	}

	public Country findByName(String name) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("FRAMEWORK_V2.SELECT_COUNTRY_BY_NAME").getSql(), new Object[]{name}, new int[]{Types.VARCHAR}, getCountryRowMapper ());
	}

	public Country findByA2(String a2) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("FRAMEWORK_V2.SELECT_COUNTRY_BY_A2").getSql(), new Object[]{a2}, new int[]{Types.VARCHAR}, getCountryRowMapper ());
	}

	public Country findByA3(String a3) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("FRAMEWORK_V2.SELECT_COUNTRY_BY_A3").getSql(), new Object[]{a3}, new int[]{Types.VARCHAR}, getCountryRowMapper ());
	}

	public List<Country> findByEnabled(boolean enabled) {		
		int i = enabled ? 1 : 0 ;		
		return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_ALL_COUNTRY_WITH_ENABLED").getSql(), new Object[]{i}, new int[]{Types.INTEGER}, getCountryRowMapper() );		
	}

	public List<Country> findAll() {
		return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_ALL_COUNTRY").getSql(), getCountryRowMapper() );	
	}

	public List<Country> findAll(int start, int end) {		
		return getExtendedJdbcTemplate().queryScrollable(getBoundSql("FRAMEWORK_V2.SELECT_ALL_COUNTRY)").getSql(), start, end, new Object [] {}, new int [] {});
	}

	public void deleteCountryById(long ID) {
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.DELETE_COUNTRY_BY_ID").getSql(), new Object [] {ID}, new int [] {Types.INTEGER});
	}

	public void updateCountry(Country country) {
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
