package architecture.ee.spring.jdbc.support;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

/**
 * @author donghyuck
 */
public class AutomaticJdbcExtractor implements NativeJdbcExtractor {

    /**
     * @uml.property name="extractors"
     */
    private Map<String, NativeJdbcExtractor> extractors;
    /**
     * @uml.property name="defaultJdbcExtractor"
     */
    private NativeJdbcExtractor defaultJdbcExtractor;
    private NativeJdbcExtractor jdbcExtractor;
    private Log log = LogFactory.getLog(getClass());

    public boolean isNativeConnectionNecessaryForNativeStatements() {
	return true;
    }

    public boolean isNativeConnectionNecessaryForNativePreparedStatements() {
	return true;
    }

    public boolean isNativeConnectionNecessaryForNativeCallableStatements() {
	return true;
    }

    /**
     * When this method is called, the connection object passed in is checked to
     * determine what type of connection pooling is being used (based on class
     * prefix)
     * <p/>
     * <p>
     * Once we find out what pooling is used we then set the jdbcExtractor field
     * to the appropriate extractor and delegate all method calls to this
     * extractor
     * 
     * @param con
     * @return
     * @throws SQLException
     */

    public Connection getNativeConnection(Connection con) throws SQLException {
	return getJdbcExtractor(con).getNativeConnection(con);
    }

    private synchronized NativeJdbcExtractor getJdbcExtractor(Object o) {
	log.debug("getJdbcExtractor:" + o.getClass().getName());

	if (jdbcExtractor == null) {
	    String objClass = o.getClass().getName();
	    for (String classPrefix : extractors.keySet()) {
		if (objClass.indexOf(classPrefix) != -1) {
		    jdbcExtractor = (NativeJdbcExtractor) extractors.get(classPrefix);
		}
	    }
	    if (jdbcExtractor == null)
		jdbcExtractor = defaultJdbcExtractor;
	}

	return jdbcExtractor;
    }

    public Connection getNativeConnectionFromStatement(Statement stmt) throws SQLException {
	return getJdbcExtractor(stmt).getNativeConnectionFromStatement(stmt);
    }

    public Statement getNativeStatement(Statement stmt) throws SQLException {
	return getJdbcExtractor(stmt).getNativeStatement(stmt);
    }

    public PreparedStatement getNativePreparedStatement(PreparedStatement ps) throws SQLException {
	return getJdbcExtractor(ps).getNativePreparedStatement(ps);
    }

    public CallableStatement getNativeCallableStatement(CallableStatement cs) throws SQLException {
	return getJdbcExtractor(cs).getNativeCallableStatement(cs);
    }

    public ResultSet getNativeResultSet(ResultSet rs) throws SQLException {
	return getJdbcExtractor(rs).getNativeResultSet(rs);
    }

    /**
     * @return
     * @uml.property name="extractors"
     */
    public Map<String, NativeJdbcExtractor> getExtractors() {
	return extractors;
    }

    /**
     * @param extractors
     * @uml.property name="extractors"
     */
    public void setExtractors(Map<String, NativeJdbcExtractor> extractors) {
	this.extractors = extractors;
    }

    /**
     * @return
     * @uml.property name="defaultJdbcExtractor"
     */
    public NativeJdbcExtractor getDefaultJdbcExtractor() {
	return defaultJdbcExtractor;
    }

    /**
     * @param defaultJdbcExtractor
     * @uml.property name="defaultJdbcExtractor"
     */
    public void setDefaultJdbcExtractor(NativeJdbcExtractor defaultJdbcExtractor) {
	this.defaultJdbcExtractor = defaultJdbcExtractor;
    }

}
