package architecture.ee.g11n.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Locale;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import architecture.ee.g11n.I18nText;
import architecture.ee.g11n.internal.I18nTextImpl;
import architecture.ee.jdbc.sequencer.dao.SequenceDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

public class JdbcI18nTextDao extends ExtendedJdbcDaoSupport implements I18nTextDao {

	
	class I18nTextRowMapper implements RowMapper<I18nText> {

		public I18nText mapRow(ResultSet rs, int rowNum) throws SQLException {
			I18nTextImpl c = new I18nTextImpl();			
			c.setTextId(rs.getLong(1));
			c.setLocaleCode(rs.getString(2));
			c.setTextKey(rs.getString(3));
			c.setText(rs.getString(4));
			c.setCreationDate(rs.getDate(5));
			c.setModifiedDate(rs.getDate(6));
			return c;
		}};
		
	private SequenceDao sequenceDao;
			 
	public void setSequenceDao(SequenceDao sequenceDao) {
			this.sequenceDao = sequenceDao;
	}

	public void createTexts(final List<I18nText> list) {
		
		getExtendedJdbcTemplate().batchUpdate(getBoundSql("FRAMEWORK_V2.INSERT_I18N_TEXTS").getSql(),			    
			new BatchPreparedStatementSetter(){
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						for( I18nText text : list ){
							long time = System.currentTimeMillis();
                            ps.setLong(1, sequenceDao.nextID("I18nText"));   
                            ps.setString(2, text.getLocaleCode());
                            ps.setString(3, text.getTextKey());
                            ps.setString(4, text.getText());
                            ps.setDate(5, new java.sql.Date(time));
                            ps.setDate(6, new java.sql.Date(time));
						}
					}
					public int getBatchSize() {
						return list.size();
					}});		
	}

	public void updateTexts(final List<I18nText> list) {
		getExtendedJdbcTemplate().batchUpdate(getBoundSql("FRAMEWORK_V2.UPDATE_I18N_TEXTS").getSql(),
			    new BatchPreparedStatementSetter(){
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						for( I18nText text : list ){
							long time = System.currentTimeMillis();
                            ps.setString(1, text.getText());                            
                            ps.setDate(2, new java.sql.Date(time));
                            ps.setLong(3, text.getTextId());
						}
					}
					public int getBatchSize() {
						return list.size();
					}});
	}

	public void deleteTexts(final List<I18nText> list) {
		getExtendedJdbcTemplate().batchUpdate(getBoundSql("FRAMEWORK_V2.DELETE_I18N_TEXTS").getSql(),
		    new BatchPreparedStatementSetter(){

				public void setValues(PreparedStatement ps, int i) throws SQLException {
					for( I18nText text : list){
						ps.setLong(1, text.getTextId());
					}
				}

				public int getBatchSize() {
					return list.size();
				}});		
	}

	public I18nText getText(long textID) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("FRAMEWORK_V2.SELECT_I18N_TEXT_BY_ID").getSql(), new Object[]{textID}, new int[]{Types.INTEGER}, new I18nTextRowMapper());
	}

	public List<I18nText> getTexts() {
		return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_ALL_I18N_TEXT").getSql(), new I18nTextRowMapper());
	}
	
	public List<I18nText> getTexts(Locale locale) {
		return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_I18N_TEXT_BY_LOCALE").getSql(), 
				new Object[]{locale.toString()}, 
				new int[]{Types.VARCHAR}, 
				new I18nTextRowMapper());
	}
	
/*
	public List<I18nText> getTexts(int objectType) {
		return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_I18N_TEXT_BY_OBJECTTYPE").getSql(), new Object[]{objectType}, new int[]{Types.INTEGER}, new I18nTextRowMapper());
	}

	public List<I18nText> getTexts(int objectType, long objectID) {
		return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_I18N_TEXT_BY_OBJECTTYPE_AND_OBJECTID").getSql(), new Object[]{objectType, objectID}, new int[]{Types.INTEGER, Types.INTEGER}, new I18nTextRowMapper());
	}

	public List<I18nText> getTexts(int objectType, String locale) {
		return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_I18N_TEXT_BY_OBJECTTYPE_AND_LOCALE").getSql(), new Object[]{objectType, locale}, new int[]{Types.INTEGER, Types.VARCHAR}, new I18nTextRowMapper());
	}*/

}
