package architecture.ee.i18n.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.jdbc.core.RowMapper;

import architecture.ee.i18n.I18nText2;
import architecture.ee.i18n.I18nTextResourceBundle;
import architecture.ee.i18n.dao.I18nResourceBundleDao;
import architecture.ee.model.impl.I18nTextModel2Impl;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

public class JdbcI18nResourceBundleDao extends ExtendedJdbcDaoSupport  implements I18nResourceBundleDao {

	
	class I18nTextRowMapper implements RowMapper<I18nText2> {
		public I18nText2 mapRow(ResultSet rs, int rowNum) throws SQLException {
			I18nTextModel2Impl c = new I18nTextModel2Impl();			
			c.setTextId(rs.getLong(1));
			c.setLocaleCode(rs.getString(3));
			c.setTextKey(rs.getString(4));
			c.setText(rs.getString(5));
			c.setCreationDate(rs.getDate(6));
			c.setModifiedDate(rs.getDate(7));
			return (I18nText2)c;
		}};
		
	
	public I18nTextResourceBundle getResourceBundle(String bundleName, Locale locale) {		
		
		 List<I18nText2> texts = getText(bundleName, locale);
 
		 //log.debug( "resource bundle for " + l.toString());			 
		 List<String[]> keyValues = new ArrayList<String[]>();
         for(I18nText2 text:texts){
              keyValues.add(new String[]{text.getTextKey(), text.getText()});
          }
         //I18nTextResourceBundle bundle = new I18nTextResourceBundle(locale, keyValues);          
		 return null;		 
	}
	
	public List<I18nText2> getText(String bundleName, Locale locale){
		return getExtendedJdbcTemplate().query(
			getBoundSql("FRAMEWORK_V2.SELECT_I18N_TEXT_BY_RESOURCE_BUNDLE_KEY_AND_LOCALE").getSql(), 
			new Object[]{bundleName, locale.toString()}, 
			new int[]{Types.VARCHAR,Types.VARCHAR}, 
			new I18nTextRowMapper());
	}


}
