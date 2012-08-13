package architecture.ee.plugin.dao.impl;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.LobHandler;

import architecture.ee.plugin.dao.PluginDao;
import architecture.ee.plugin.dao.PluginEntityObject;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

public class JdbcPluginDao extends ExtendedJdbcDaoSupport implements PluginDao {

	private String sequencerName = "PLUGIN";
		
	private static final ParameterizedRowMapper<PluginEntityObject> mapper = new ParameterizedRowMapper<PluginEntityObject>() {

        public PluginEntityObject mapRow(ResultSet rs, int rowNum)
            throws SQLException
        {
        	PluginEntityObject bean = new PluginEntityObject();
            bean.setCreationDate(rs.getDate("CREATION_DATE"));
            bean.setModifiedDate(rs.getDate("MODIFIED_DATE"));
            bean.setPluginId(rs.getLong("PLUGIN_ID"));
            bean.setName(rs.getString("NAME"));
            return bean;
        }
        
    };

	public PluginEntityObject create(PluginEntityObject entity) {
		
		long pluginId = getNextId(sequencerName);
		entity.setPluginId(pluginId);
		Date now = new Date();
		entity.setCreationDate(now);
		entity.setModifiedDate(now);
		getExtendedJdbcTemplate().update(
				getBoundSql("FRAMEWORK_PLUGIN_V2.INSERT_PLUGIN_DATA").getSql(), 
				new Object[]{entity.getPluginId(), entity.getName(), entity.getCreationDate(), entity.getModifiedDate()}, 
				new int[]{Types.INTEGER, Types.VARCHAR, Types.DATE, Types.DATE});		
		return entity;
	}
	

	public PluginEntityObject create(PluginEntityObject entity, int contentLength, InputStream content) {
		long pluginId = getNextId(sequencerName);
		entity.setPluginId(pluginId);
		Date now = new Date();
		entity.setCreationDate(now);
		entity.setModifiedDate(now);		
		getExtendedJdbcTemplate().update(
				getBoundSql("FRAMEWORK_PLUGIN_V2.INSERT_PLUGIN_DATA_WITH_BLOB").getSql(), 
				new Object[]{entity.getPluginId(), entity.getName(), entity.getCreationDate(), entity.getModifiedDate(), new SqlLobValue(content, contentLength, getLobHandler())}, 
				new int[]{Types.INTEGER, Types.VARCHAR, Types.DATE, Types.DATE, Types.BLOB});		
		return entity;
	}

	public void delete(PluginEntityObject entity) {
		getExtendedJdbcTemplate().update( getBoundSql("FRAMEWORK_PLUGIN_V2.DELETE_PLUGIN_DATA_BY_ID").getSql(), new Object[]{entity.getPluginId()} , new int[]{Types.INTEGER} );
	}

	public void delete(String pluginName) {
		getExtendedJdbcTemplate().update( getBoundSql("FRAMEWORK_PLUGIN_V2.DELETE_PLUGIN_DATA_BY_NAME").getSql(), new Object[]{pluginName} , new int[]{Types.VARCHAR} );
	}

	public PluginEntityObject getByName(String name) {
		try {
			return getExtendedJdbcTemplate().queryForObject( 
					getBoundSql("FRAMEWORK_PLUGIN_V2.SELECT_PLUGIN_DATA_BY_NAME").getSql(), 
					new Object[]{name}, 
					new int[]{Types.VARCHAR}, 
					mapper );
		} catch (EmptyResultDataAccessException e){
			return null;
		} 
	}

	public List<PluginEntityObject> getPluginEntityObjects() {
		return getExtendedJdbcTemplate().query(
				getBoundSql("FRAMEWORK_PLUGIN_V2.SELECT_ALL_PLUGIN_DATA_BY_NAME").getSql(), 
				mapper);
	}

	public void setPluginData(PluginEntityObject entity, int contentLength, InputStream content) {
		getExtendedJdbcTemplate().update(
				getBoundSql("FRAMEWORK_PLUGIN_V2.UPDATE_PLUGIN_DATA_WITH_BLOB_BY_ID").getSql(), 
				new Object[]{new SqlLobValue(content, contentLength, getLobHandler()), new Date(), entity.getPluginId()}, 
				new int[]{Types.BLOB, Types.DATE, Types.INTEGER});
	}

	public InputStream getPluginData(PluginEntityObject entity) {
		final LobHandler lobHandler = getLobHandler();
		return getExtendedJdbcTemplate().queryForObject(
				getBoundSql("FRAMEWORK_PLUGIN_V2.SELECT_PLUGIN_DATA_AS_STREAM_BY_ID").getSql(),
				new Object[]{entity.getPluginId()}, new int[]{Types.INTEGER},
				new ParameterizedRowMapper<InputStream>(){
					
					public InputStream mapRow(ResultSet rs, int rowNum) throws SQLException {
						return lobHandler.getBlobAsBinaryStream(rs, 1);
					}});
	}

	public boolean doesPluginTableExist() {
		try {
			return getExtendedJdbcTemplate().queryForInt(
					getBoundSql("FRAMEWORK_PLUGIN_V2.IS_EXIST").getSql()
			) != 0 ;
		} catch(BadSqlGrammarException e){
			// 테이블이 존재하지 않는 경우
			return false;
		} catch (DataAccessException e) {
			// 테이블이 존재하나 데이터가 없는 경우
			return true;
		}
	}

}
