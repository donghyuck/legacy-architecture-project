package architecture.ee.g11n.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Locale;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import architecture.ee.g11n.I18nText;
import architecture.ee.g11n.dao.I18nTextDao;
import architecture.ee.g11n.internal.I18nTextImpl;
import architecture.ee.jdbc.sequencer.dao.SequenceDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

public class JdbcI18nTextDao extends ExtendedJdbcDaoSupport implements I18nTextDao {

	
	class I18nTextRowMapper implements RowMapper<I18nText> {

		public I18nText mapRow(ResultSet rs, int rowNum) throws SQLException {
			I18nTextImpl c = new I18nTextImpl();			
			c.setTextId(rs.getLong(1));
			c.setObjectType(rs.getInt(2));
			c.setObjectId(rs.getLong(3));
			c.setObjectAttribute(rs.getInt(4));
			c.setLocaleCode(rs.getString(5));
			c.setText(rs.getString(6));
			c.setCreationDate(rs.getDate(7));
			c.setModifiedDate(rs.getDate(8));
			return c;
		}};
		
	private SequenceDao sequenceDao;
			 
	public void setSequenceDao(SequenceDao sequenceDao) {
			this.sequenceDao = sequenceDao;
	}

	public void createTexts(final List<I18nText> list) {
		
		getExtendedJdbcTemplate().batchUpdate(getBoundSql("FRAMEWORK_V2.INSERT_I18N_OBJECT_TEXT").getSql(),			    
			new BatchPreparedStatementSetter(){
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						for( I18nText text : list ){
							long time = System.currentTimeMillis();
                            ps.setLong(1, sequenceDao.nextID("I18nText"));   
                            ps.setInt(2, text.getObjectType());
                            ps.setLong(3, text.getObjectId());
                            ps.setInt(4, text.getObjectAttribute());
                            ps.setString(5, text.getLocaleCode());
                            ps.setString(6, text.getText());
                            ps.setDate(7, new java.sql.Date(time));
                            ps.setDate(8, new java.sql.Date(time));
						}
					}
					public int getBatchSize() {
						return list.size();
					}});		
	}

	public void updateTexts(final List<I18nText> list) {
		getExtendedJdbcTemplate().batchUpdate(getBoundSql("FRAMEWORK_V2.UPDATE_I18N_OBJECT_TEXT").getSql(),
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
		getExtendedJdbcTemplate().batchUpdate(getBoundSql("FRAMEWORK_V2.DELETE_I18N_OBJECT_TEXT").getSql(),
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
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("FRAMEWORK_V2.SELECT_I18N_OBJECT_TEXT_BY_ID").getSql(), new Object[]{textID}, new int[]{Types.INTEGER}, new I18nTextRowMapper());
	}

	public List<I18nText> getTexts() {
		return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_ALL_I18N_OBJECT_TEXT").getSql(), new I18nTextRowMapper());
	}
	
	public List<I18nText> getTexts(Locale locale) {
		return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_I18N_OBJECT_TEXT_BY_LOCALE").getSql(), 
				new Object[]{locale.toString()}, 
				new int[]{Types.VARCHAR}, 
				new I18nTextRowMapper());
	}

	public List<I18nText> getTexts(int objectType) {
		return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_I18N_OBJECT_TEXT_BY_OBJECT_TYPE").getSql(), new Object[]{objectType}, new int[]{Types.INTEGER}, new I18nTextRowMapper());
	}

	public List<I18nText> getTexts(int objectType, long objectID) {
		return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_I18N_OBJECT_TEXT_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), new Object[]{objectType, objectID}, new int[]{Types.INTEGER, Types.INTEGER}, new I18nTextRowMapper());
	}

	public List<I18nText> getTexts(int objectType, String locale) {
		return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_I18N_OBJECT_TEXT_BY_OBJECT_TYPE_AND_LOCALE").getSql(), new Object[]{objectType, locale}, new int[]{Types.INTEGER, Types.VARCHAR}, new I18nTextRowMapper());
	}

}
