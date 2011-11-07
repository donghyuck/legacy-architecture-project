package architecture.ee.i18n.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import architecture.ee.i18n.I18nText;
import architecture.ee.i18n.dao.I18nTextDao;
import architecture.ee.model.impl.I18nTextModelImpl;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

public class JdbcI18nTextDao extends ExtendedJdbcDaoSupport implements I18nTextDao {

	class I18nTextRowMapper implements RowMapper<I18nText> {
		
		public I18nText mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			I18nTextModelImpl impl = new I18nTextModelImpl();			
			impl.setTextId(rs.getLong(1));
			impl.setKey(rs.getString(2));
			impl.setText(rs.getString(3));
			impl.setCreationDate(rs.getDate(4));
			impl.setModifiedDate(rs.getDate(5));
			return impl;
		
		}}

	public List<I18nText> getTexts(long localizerId) {		
		return getExtendedJdbcTemplate().query(
				getBoundSql("FRAMEWORK_V2.SELECT_I18N_TEXT_BY_LOCALIZER_ID").getSql(), 
				new Object[]{localizerId}, 
				new int[]{Types.INTEGER}, 
				new I18nTextRowMapper());	
	}

	public void setTexts(final List<I18nText> texts) {
		
/*		getExtendedJdbcTemplate().batchUpdate(getBoundSql("FRAMEWORK_V2.UPDATE_I18N_OBJECT_TEXT").getSql(),
			    new BatchPreparedStatementSetter(){
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						for( I18nText text : texts ){
							long time = System.currentTimeMillis();
                            ps.setString(1, text.getText());                            
                            ps.setDate(2, new java.sql.Date(time));
                            ps.setLong(3, text.getTextId());
						}
					}
					public int getBatchSize() {
						return list.size();
					}});*/
		
	};
		
}
