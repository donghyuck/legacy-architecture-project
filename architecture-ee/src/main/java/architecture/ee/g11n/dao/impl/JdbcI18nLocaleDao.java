package architecture.ee.g11n.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import architecture.ee.g11n.I18nLocale;
import architecture.ee.g11n.dao.I18nLocaleDao;
import architecture.ee.model.internal.I18nLocaleModelImpl;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

public class JdbcI18nLocaleDao extends ExtendedJdbcDaoSupport implements I18nLocaleDao {

	class I18nLocaleRowMapper implements RowMapper<I18nLocale> {

		public I18nLocale mapRow(ResultSet rs, int rowNum) throws SQLException {
	
			I18nLocaleModelImpl c = new I18nLocaleModelImpl();			
			c.setLocaleId(rs.getLong(1));
			c.setLanguage(rs.getString(2));
			c.setCountry(rs.getString(3));
			c.setVariant(rs.getString(4));
			c.setEncoding(rs.getString(5));
			c.setCreationDate(rs.getDate(6));
			c.setModifiedDate(rs.getDate(7));
			return c;
		}};
		

	@Override
	protected void initDao() throws Exception {
		super.initDao();
	}

	public int getAvailableLocaleCount() {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("FRAMEWORK_V2.COUNT_ALL_LOCALE").getSql());
	}

	public List<I18nLocale> getAvailableLocales() {
		return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_ALL_LOCALE").getSql(), new I18nLocaleRowMapper() );	
	}

	public I18nLocale getLocaleById(long localeId) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("FRAMEWORK_V2.SELECT_LOCALE_BY_ID").getSql(), new Object[]{localeId}, new int[]{Types.INTEGER}, new I18nLocaleRowMapper());
	}

	public I18nLocale getLocale(Locale locale, boolean createIfNotExist) {
		try {
			return getLocale(locale);
		} catch (IncorrectResultSizeDataAccessException e) {
			if(createIfNotExist){
				return addLocale(locale);
			}
			throw e;
		}
	}

	public I18nLocale getLocale(Locale locale) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("FRAMEWORK_V2.SELECT_LOCALE_BY_CODE").getSql(), 
			new Object[]{locale.getLanguage(), locale.getCountry() }, 
			new int[]{Types.VARCHAR, Types.VARCHAR}, new I18nLocaleRowMapper());
	}

	public I18nLocale addLocale(Locale locale) {		
		long now = System.currentTimeMillis();
		I18nLocaleModelImpl impl = new I18nLocaleModelImpl();
		impl.setLocaleId(getNextId("I18nLocale"));
		impl.setLanguage(locale.getLanguage());
		impl.setCountry(locale.getCountry());
		impl.setVariant(locale.getVariant());
		//impl.setEncoding(defaultEncoding);
		impl.setCreationDate(new Date(now));
		impl.setModifiedDate(new Date(now));
		
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.INSERT_LOCALE").getSql(), 
			new Object[]{
				impl.getLocaleId(),
				impl.getLanguage(),
				impl.getCountry(),
				impl.getVariant(),
				//impl.getEncoding(),
				new java.sql.Date(now),
				new java.sql.Date(now)},
			new int [] {Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.DATE, Types.DATE}	
		);
		return impl;
	}

	public void deleteLocale(I18nLocale locale) {
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.DELETE_LOCALE_BY_ID").getSql(), new Object[]{locale.getLocaleId()}, new int[]{Types.INTEGER});
	}

	public I18nLocale updateLocale(I18nLocale locale) {
		java.sql.Date d = new java.sql.Date(System.currentTimeMillis());
		locale.setModifiedDate(d);
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.UPDATE_LOCALE").getSql(), 
			new Object[]{locale.getEncoding(), d, locale.getLocaleId()},
			new int []{ Types.VARCHAR, Types.DATE, Types.INTEGER } );		
		return locale;
	} 

}
