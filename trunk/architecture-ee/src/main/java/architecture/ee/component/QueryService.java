package architecture.ee.component;

import org.springframework.jdbc.support.lob.LobHandler;

import architecture.ee.jdbc.query.SqlQuery;
import architecture.ee.spring.jdbc.ExtendedJdbcTemplate;

public interface QueryService {

	public SqlQuery getSqlQuery();
	
	public ExtendedJdbcTemplate getExtendedJdbcTemplate();
	
	public LobHandler getLobHandler();
	
	public String getSql(String statement);
	
}
