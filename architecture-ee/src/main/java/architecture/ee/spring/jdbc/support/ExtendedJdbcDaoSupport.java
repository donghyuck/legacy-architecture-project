package architecture.ee.spring.jdbc.support;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.lob.LobHandler;

import architecture.ee.jdbc.query.factory.Configuration;
import architecture.ee.jdbc.query.mapping.BoundSql;
import architecture.ee.jdbc.query.mapping.MappedStatement;
import architecture.ee.spring.jdbc.ExtendedJdbcTemplate;

public class ExtendedJdbcDaoSupport extends JdbcDaoSupport {	
	
	protected Log log = LogFactory.getLog(getClass());	
	private Configuration configuration;
			
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public boolean isSetConfiguration(){
		if (configuration == null)
			return false;
		else 
			return true;
	}
	
	protected ExtendedJdbcTemplate createJdbcTemplate(DataSource dataSource) {
		return new ExtendedJdbcTemplate(dataSource);
	}

	public ExtendedJdbcTemplate getExtendedJdbcTemplate(){
		return (ExtendedJdbcTemplate) getJdbcTemplate();
	} 
	
	protected void initTemplateConfig() {
        log.debug("initTemplateConfig");
        getExtendedJdbcTemplate().initialize();
	}

	public LobHandler getLobHandler(){
		return getExtendedJdbcTemplate().getLobHandler();
	} 
	
	public void setLobHandler(LobHandler lobHandler){
		getExtendedJdbcTemplate().setLobHandler(lobHandler);
	} 
	
	protected BoundSql getBoundSql(String statement ){
		return getBoundSql(statement, null);
	}
	protected BoundSql getBoundSql(String statement, Object[] params ){		
		if(isSetConfiguration()){
			MappedStatement stmt = configuration.getMappedStatement(statement);
			return stmt.getBoundSql(params);
		}
		return null;
	}
}
