package architecture.ee.i18n.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import architecture.common.util.StringUtils;
import architecture.ee.i18n.I18nLocalizer;
import architecture.ee.i18n.I18nTextResourceBundle;
import architecture.ee.i18n.dao.I18nLocaleDao;
import architecture.ee.i18n.dao.I18nLocalizerDao;
import architecture.ee.model.impl.I18nLocalizerModelImpl;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

/**
 * @author   donghyuck
 */
public class JdbcI18nLocalizerDao extends ExtendedJdbcDaoSupport implements I18nLocalizerDao{

	class LocalizerRowMapper implements RowMapper<I18nLocalizer> {

		public I18nLocalizer mapRow(ResultSet rs, int rowNum) throws SQLException {	
			
			I18nLocalizerModelImpl impl = new I18nLocalizerModelImpl();		
			impl.setLocalizerId(rs.getLong(1));
			impl.setLocaleId(rs.getLong(2));
			impl.setName(rs.getString(3));
			impl.setDescription(rs.getString(4));
			impl.setCreationDate(rs.getDate(5));
			impl.setModifiedDate(rs.getDate(6));
			
			log.debug(impl);
			return impl;
			
		}};

				
	private LocalizerRowMapper getLocalizerRowMapper () {
		return new LocalizerRowMapper();
	}
	
	/**
	 * @uml.property  name="i18nLocaleDao"
	 * @uml.associationEnd  
	 */
	private I18nLocaleDao i18nLocaleDao ;
	
		
	/**
	 * @return
	 * @uml.property  name="i18nLocaleDao"
	 */
	public I18nLocaleDao getI18nLocaleDao() {
		return i18nLocaleDao;
	}

	/**
	 * @param  i18nLocaleDao
	 * @uml.property  name="i18nLocaleDao"
	 */
	public void setI18nLocaleDao(I18nLocaleDao i18nLocaleDao) {
		this.i18nLocaleDao = i18nLocaleDao;
	}

	public int getAvailableI18nLocalizerCount() {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("FRAMEWORK_V2.COUNT_ALL_LOCALIZER").getSql());
	}

	public List<I18nLocalizer> getAvailableI18nLocalizers() {
		List<I18nLocalizer> list = getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_ALL_LOCALIZER").getSql(), getLocalizerRowMapper() );	
		for(I18nLocalizer localizer : list ){
			localizer.setI18nLocale(i18nLocaleDao.getI18nLocaleById(localizer.getLocalizerId()));
			localizer.setI18nTexts(getTexts(localizer.getLocalizerId()));
		}
		
		log.debug(list);
		
		return list;
	}

	public I18nLocalizer getI18nLocalizerById(long localizerId) {
		I18nLocalizer localizer = getExtendedJdbcTemplate().queryForObject(
				getBoundSql("FRAMEWORK_V2.SELECT_LOCALIZER_BY_ID").getSql(), 
				new Object[]{localizerId}, 
				new int[]{Types.INTEGER}, 
				getLocalizerRowMapper());
		localizer.setI18nLocale(i18nLocaleDao.getI18nLocaleById(localizer.getLocalizerId()));
		return localizer;
	}

	public I18nLocalizer addI18nLocalizer(I18nLocalizer localizer) {		
		long now = System.currentTimeMillis();
		localizer.setLocalizerId(getNextId("I18N_LOCALIZER"));
		localizer.setCreationDate(new Date(now));
		localizer.setModifiedDate(new Date(now));		
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.INSERT_LOCALIZER").getSql(), 
				new Object[]{
					localizer.getLocalizerId(),
					localizer.getI18nLocale().getLocaleId(),
					localizer.getName(),			
					localizer.getDescription(),
					new java.sql.Date(now),
					new java.sql.Date(now)},
				new int [] {Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.DATE, Types.DATE}	
			);
		
		localizer.setI18nLocale(i18nLocaleDao.getI18nLocaleById(localizer.getLocalizerId()));
		return localizer;
	}

	public I18nLocalizer updateI18nLocalizer(I18nLocalizer localizer) {
		java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
		localizer.setModifiedDate(now);		
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.UPDATE_LOCALIZER").getSql(), 
			new Object[]{			
			localizer.getI18nLocale().getLocaleId(), 
			localizer.getName(), 
			localizer.getDescription(), 
			now, 
			localizer.getLocalizerId()},
			new int []{ Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.DATE, Types.INTEGER } );		
		
		
		setTexts(localizer.getLocalizerId(), localizer.getI18nTexts());
		localizer.setI18nLocale(i18nLocaleDao.getI18nLocaleById(localizer.getLocalizerId()));
		return localizer;		
	}

	public void deleteI18nLocalizer(I18nLocalizer localizer) {
		deleteTexts(localizer.getLocalizerId());
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.DELETE_LOCALIZER_BY_ID").getSql(), new Object[]{localizer.getLocalizerId()}, new int[]{Types.INTEGER});
	}

	
	public ResourceBundle getResourceBundle(long localizerId){		
		Map<String, String> texts = getTexts(localizerId);
		List<String[]> keyValues = new ArrayList<String[]>();
		for(String key : texts.keySet()){
			keyValues.add(new String[]{key, texts.get(key)});
		}		
		I18nTextResourceBundle bundle = new I18nTextResourceBundle(keyValues); 		
		return bundle;
	}
	
	/**
	 * 
	 * @param localizerId
	 * @return
	 */
	public Map<String, String> getTexts(long localizerId) {		
		return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_I18N_TEXT_BY_LOCALIZER").getSql(), new Object[] { localizerId },
			new ResultSetExtractor<Map<String, String>>(){
			public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {				
				Map<String, String> rows = new HashMap<String, String>();				
				while(rs.next()){
					String key = rs.getString(2);
					String value = rs.getString(3);
					rows.put(key, value);
				}
				log.debug(">>" + rows);
				return rows;
			}});		
	}

	public void deleteTexts(long localizerId){
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.DELETE_I18N_TEXT_BY_LOCALIZER").getSql(), localizerId);
	}
	
	public void setTexts(long localizerId, Map<String, String> texts) {		
		deleteTexts(localizerId);		
		if(texts.size() > 0){
			final List<Object[]> copy = new ArrayList<Object[]>(texts.size());
        	Set<Map.Entry<String, String>> set = texts.entrySet();
        	SqlParameterSource[] batchArgs = new SqlParameterSource[set.size()];
        	for( Map.Entry<String, String> entry : set ){
                String key = entry.getKey();
                String value = entry.getValue();
                if(!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)){
                	copy.add(new Object[]{localizerId, key, value});
                }
        	}
        	getExtendedJdbcTemplate().batchUpdate(getBoundSql("FRAMEWORK_V2.INSERT_I18N_TEXT").getSql(), copy, new int[]{ Types.INTEGER, Types.VARCHAR, Types.VARCHAR} );
		}
	}

	public List<I18nLocalizer> getI18nLocalizersByName(String name) {
		
		log.debug("[" + name + "]");
		
		List<I18nLocalizer> list = getExtendedJdbcTemplate().query(
				getBoundSql("FRAMEWORK_V2.SELECT_LOCALIZER_BY_NAME").getSql(), 
				new Object[]{name}, 
				getLocalizerRowMapper() 
		);	
		
		log.debug("list<bf>" + list );
		
		for(I18nLocalizer localizer : list ){
			localizer.setI18nLocale(i18nLocaleDao.getI18nLocaleById(localizer.getLocalizerId()));
			localizer.setI18nTexts(getTexts(localizer.getLocalizerId()));
		}
		log.debug("list<af>" + list );
		
		return list;
	}

}
