package architecture.ee.spring.jdbc.support;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.lob.LobHandler;

import architecture.ee.jdbc.sequencer.incrementer.MaxValueIncrementer;
import architecture.ee.jdbc.sqlquery.factory.Configuration;
import architecture.ee.jdbc.sqlquery.mapping.BoundSql;
import architecture.ee.jdbc.sqlquery.mapping.MappedStatement;
import architecture.ee.spring.jdbc.ExtendedJdbcTemplate;

/**
 * @author   donghyuck
 */
public class ExtendedJdbcDaoSupport extends JdbcDaoSupport {	
	
	protected Log log = LogFactory.getLog(getClass());	

	/**
	 * @uml.property  name="maxValueIncrementer"
	 * @uml.associationEnd  
	 */
	private MaxValueIncrementer maxValueIncrementer = null;
	
	/**
	 * @uml.property  name="configuration"
	 * @uml.associationEnd  
	 */
	private Configuration configuration = null;

	public ExtendedJdbcDaoSupport() {
		super();
	}

	public ExtendedJdbcDaoSupport(Configuration configuration) {
		super();
		this.configuration = configuration;
	}

    protected boolean isMaxValueIncrementerSupport(){
    	if(maxValueIncrementer!=null)
    		return true;
    	return false;
    }

    protected long getNextId(String name){
    	return maxValueIncrementer.nextLongValue(name);
    }
    
	/**
	 * @param  sequenceDao
	 * @uml.property  name="maxValueIncrementer"
	 */
	public void setMaxValueIncrementer(MaxValueIncrementer sequenceDao) {
		this.maxValueIncrementer = sequenceDao;
	}
	
	/**
	 * @param  configuration
	 * @uml.property  name="configuration"
	 */
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
		if(isSetConfiguration()){
			MappedStatement stmt = configuration.getMappedStatement(statement);
			return stmt.getBoundSql(null);
		}
		return null;
	}
	
	protected BoundSql getBoundSqlWithAdditionalParameter(String statement, Object additionalParameter ){
		if(isSetConfiguration()){
			MappedStatement stmt = configuration.getMappedStatement(statement);
			return stmt.getBoundSql(null, additionalParameter);
		}
		return null;
	}

	protected BoundSql getBoundSql(String statement, Object ... params ){		
		if(isSetConfiguration()){
			MappedStatement stmt = configuration.getMappedStatement(statement);
			return stmt.getBoundSql(params);
		}
		return null;
	}	

	protected BoundSql getBoundSqlWithAdditionalParameter(String statement, Object parameters, Object additionalParameter ){
		if(isSetConfiguration()){
			MappedStatement stmt = configuration.getMappedStatement(statement);
			return stmt.getBoundSql(parameters, additionalParameter);
		}
		return null;
	}	

}