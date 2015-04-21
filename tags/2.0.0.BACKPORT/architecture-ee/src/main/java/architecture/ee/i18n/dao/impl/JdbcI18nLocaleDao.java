package architecture.ee.i18n.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import architecture.ee.i18n.I18nLocale;
import architecture.ee.i18n.dao.I18nLocaleDao;
import architecture.ee.i18n.internal.I18nLocaleImpl;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;


public class JdbcI18nLocaleDao extends ExtendedJdbcDaoSupport implements I18nLocaleDao {

	class I18nLocaleRowMapper implements RowMapper<I18nLocale> {

		public I18nLocale mapRow(ResultSet rs, int rowNum) throws SQLException {	
			I18nLocaleImpl c = new I18nLocaleImpl();			
			c.setLocaleId(rs.getLong(1));
			c.setLanguage(rs.getString(2));
			c.setCountry(rs.getString(3));
			c.setVariant(rs.getString(4));
			c.setEncoding(rs.getString(5));
			c.setCreationDate(rs.getDate(6));
			c.setModifiedDate(rs.getDate(7));
			return (I18nLocale)c;
		}};
		

	@Override
	protected void initDao() throws Exception {
		super.initDao();
	}

	public int getAvailableI18nLocaleCount() {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("ARCHITECTURE_FRAMEWORK.COUNT_ALL_LOCALE").getSql());
	}

	public List<I18nLocale> getAvailableI18nLocales() {
		return getExtendedJdbcTemplate().query(getBoundSql("ARCHITECTURE_FRAMEWORK.SELECT_ALL_LOCALE").getSql(), new I18nLocaleRowMapper() );	
	}

	public I18nLocale getI18nLocaleById(long localeId) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_FRAMEWORK.SELECT_LOCALE_BY_ID").getSql(), new Object[]{localeId}, new int[]{Types.INTEGER}, new I18nLocaleRowMapper());
	}

	public I18nLocale getI18nLocale(Locale locale, boolean createIfNotExist) {
		try {
			return getI18nLocale(locale);
		} catch (IncorrectResultSizeDataAccessException e) {
			if(createIfNotExist){
				return addI18nLocale(locale);
			}
			throw e;
		}
	}

	public I18nLocale getI18nLocale(Locale locale) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_FRAMEWORK.SELECT_LOCALE_BY_CODE").getSql(), 
			new Object[]{locale.getLanguage(), locale.getCountry() }, 
			new int[]{Types.VARCHAR, Types.VARCHAR}, new I18nLocaleRowMapper());
	}

	public I18nLocale addI18nLocale(Locale locale) {		
		long now = System.currentTimeMillis();
		I18nLocaleImpl impl = new I18nLocaleImpl();
		impl.setLocaleId(getNextId("I18N_LOCALE"));
		impl.setLanguage(locale.getLanguage());
		impl.setCountry(locale.getCountry());
		impl.setVariant(locale.getVariant());
		//impl.setEncoding(defaultEncoding);
		impl.setCreationDate(new Date(now));
		impl.setModifiedDate(new Date(now));
		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_FRAMEWORK.INSERT_LOCALE").getSql(), 
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
		return (I18nLocale)impl;
	}

	public void deleteI18nLocale(I18nLocale locale) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_FRAMEWORK.DELETE_LOCALE_BY_ID").getSql(), new Object[]{locale.getLocaleId()}, new int[]{Types.INTEGER});
	}

	public I18nLocale updateI18nLocale(I18nLocale locale) {
		java.sql.Date d = new java.sql.Date(System.currentTimeMillis());
		locale.setModifiedDate(d);
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_FRAMEWORK.UPDATE_LOCALE").getSql(), 
			new Object[]{locale.getEncoding(), d, locale.getLocaleId()},
			new int []{ Types.VARCHAR, Types.DATE, Types.INTEGER } );		
		return locale;
	} 

}
