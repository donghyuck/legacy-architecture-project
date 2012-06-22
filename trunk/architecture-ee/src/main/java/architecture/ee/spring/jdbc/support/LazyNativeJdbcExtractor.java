package architecture.ee.spring.jdbc.support;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

/**
 * A class to lazily instantiate a native JDBC extractor. <p /> We need to lazily instantiate it because otherwise Spring will construct it for us, and users might get class not found errors (eg if they're not using Weblogic and Spring tries to load the WeblogicNativeJdbcExtractor, things get ugly).
 */

public class LazyNativeJdbcExtractor implements NativeJdbcExtractor {
	
	/**
	 * @uml.property  name="delegatedExtractor"
	 */
	private NativeJdbcExtractor delegatedExtractor;
	
	private Class<NativeJdbcExtractor> extractorClass;

	public LazyNativeJdbcExtractor() {

	}

	/**
	 * @param extractorClass
	 * @uml.property  name="extractorClass"
	 */
	public void setExtractorClass(Class<NativeJdbcExtractor> extractorClass) {
		this.extractorClass = extractorClass;
	}

	/**
	 * @return
	 * @uml.property  name="delegatedExtractor"
	 */
	private synchronized NativeJdbcExtractor getDelegatedExtractor() {
		try {
			if (delegatedExtractor == null) {
				delegatedExtractor = (NativeJdbcExtractor) extractorClass.newInstance();
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException( "Error occurred trying to instantiate a native extractor of type: " + extractorClass, e);
		} catch (InstantiationException e) {
			throw new RuntimeException( "Error occurred trying to instantiate a native extractor of type: " + extractorClass, e);
		}

		if (delegatedExtractor != null) {
			return delegatedExtractor;
		} else {
			throw new RuntimeException( "Error occurred trying to instantiate a native extractor of type: " + extractorClass);
		}
	}

	public boolean isNativeConnectionNecessaryForNativeStatements() {
		return getDelegatedExtractor().isNativeConnectionNecessaryForNativeStatements();
	}

	public boolean isNativeConnectionNecessaryForNativePreparedStatements() {
		return getDelegatedExtractor().isNativeConnectionNecessaryForNativePreparedStatements();
	}

	public boolean isNativeConnectionNecessaryForNativeCallableStatements() {
		return getDelegatedExtractor().isNativeConnectionNecessaryForNativeCallableStatements();
	}

	public Connection getNativeConnection(Connection con) throws SQLException {
		return getDelegatedExtractor().getNativeConnection(con);
	}

	public Connection getNativeConnectionFromStatement(Statement stmt)
			throws SQLException {
		return getDelegatedExtractor().getNativeConnectionFromStatement(stmt);
	}

	public Statement getNativeStatement(Statement stmt) throws SQLException {
		return getDelegatedExtractor().getNativeStatement(stmt);
	}

	public PreparedStatement getNativePreparedStatement(PreparedStatement ps)
			throws SQLException {
		return getDelegatedExtractor().getNativePreparedStatement(ps);
	}

	public CallableStatement getNativeCallableStatement(CallableStatement cs)
			throws SQLException {
		return getDelegatedExtractor().getNativeCallableStatement(cs);
	}

	public ResultSet getNativeResultSet(ResultSet rs) throws SQLException {
		return getDelegatedExtractor().getNativeResultSet(rs);
	}
}
