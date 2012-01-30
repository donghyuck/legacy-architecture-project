/*
 * Copyright 2010, 2011 INKIUM, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.ee.spring.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchUpdateUtils;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterBatchUpdateUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.ParsedSql;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.jdbc.support.lob.OracleLobHandler;

import architecture.ee.jdbc.util.JdbcHelper;
import architecture.ee.jdbc.util.JdbcHelper.DatabaseType;
import architecture.ee.jdbc.util.JdbcHelperFactory;

/**
 * 스프링프레임워크에서 제공하는 JdbcTemplate 을 확장한 클래스.
 * @author  DongHyuck, Son
 */
public class ExtendedJdbcTemplate extends JdbcTemplate {

	
	/**
	 * @author  donghyuck
	 */
	public static class ScrollablePreparedStatementCreator implements PreparedStatementCreator {
		
		private String sqlToUse;
		private Object params[];
		private int paramTypes[];
		private final int startIndex;
		private final int numResults;
		/**
		 */
		private JdbcHelper helper;

		public ScrollablePreparedStatementCreator(String sql, int startIndex, int numResults, Object args[], int[] types, JdbcHelper helper) {
			this.startIndex = startIndex;
			this.numResults = numResults;
			this.params = args;
			this.paramTypes = types;
			this.sqlToUse = sql;
			this.helper = helper;
		}

		/**
		 * 데이터베이스 제품에 따라 쿼리 결과에 대한 스크롤을 지원하도록 PreparedStatement 다르게 생성한다.
		 */
		public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			DatabaseType databaseType = helper.getDatabaseType();

			PreparedStatement ps;
			if (DatabaseType.mysql == databaseType) {
				StringBuilder builder = new StringBuilder(sqlToUse);
				builder.append(" LIMIT ").append(startIndex).append(",").append(numResults);
				ps = connection.prepareStatement(builder.toString());
			} else if (DatabaseType.postgresql == databaseType) {
				StringBuilder builder = new StringBuilder(sqlToUse);
				builder.append(" LIMIT ").append(numResults).append(" OFFSET ").append(startIndex);
				ps = connection.prepareStatement(builder.toString());
			} else {
				ps = helper.createScrollablePreparedStatement(connection, sqlToUse);
			}
			if (params != null) {
				PreparedStatementCreatorFactory pscf;
				if (paramTypes != null) {
					pscf = new PreparedStatementCreatorFactory(sqlToUse, paramTypes);
				} else {
					pscf = new PreparedStatementCreatorFactory(sqlToUse);
				}
				ps = pscf.newPreparedStatementCreator(params).createPreparedStatement(connection);
			}
			return ps;
		}
	}
	/**
	 * INNER CLASSES
	 */
	public static class ScrollableResultSetExtractor implements ResultSetExtractor {
		
		private int startIndex;
		private int numResults;
		private RowMapper mapper;
		/**
		 */
		private JdbcHelper helper;

		public ScrollableResultSetExtractor(int startIndex, int numResults, RowMapper mapper, JdbcHelper helper) {
			this.startIndex = startIndex;
			this.numResults = numResults;
			this.mapper = mapper;
			this.helper = helper;
		}

		public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
			
			ArrayList<Object> list = new ArrayList<Object>();
			DatabaseType databaseType = helper.getDatabaseType();
			if (DatabaseType.mysql == databaseType || DatabaseType.postgresql == databaseType) {
				for (int count = 0; rs.next(); count++)
					list.add(mapper.mapRow(rs, count));

			} else {
				helper.setFetchSize(rs, startIndex + numResults);
				helper.scrollResultSet(rs, startIndex);
				for (int i = 0; i < numResults && rs.next(); i++) {
					Object o = mapper.mapRow(rs, i);
					list.add(o);
				}
			}
			return list;
		}
	}

	public static final int DEFAULT_CACHE_LIMIT = 256;

	/**
	 */
	private volatile int cacheLimit = DEFAULT_CACHE_LIMIT;

	/** Cache of original SQL String to ParsedSql representation */
	private final Map<String, ParsedSql> parsedSqlCache = new LinkedHashMap<String, ParsedSql>( DEFAULT_CACHE_LIMIT, 0.75f, true) {
		@Override
		protected boolean removeEldestEntry(Map.Entry<String, ParsedSql> eldest) {
			return size() > getCacheLimit();
		}
	};

	/**
	 */
	private JdbcHelper jdbcHelper = null;
	/**
	 */
	private LobHandler lobHandler = null;
	
	public ExtendedJdbcTemplate(DataSource dataSource) {
		super(dataSource);
		
	}
	
	/**
	 * @return
	 */
	public LobHandler getLobHandler() {
		return lobHandler;
	}

	/**
	 * @param lobHandler
	 */
	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}

	/**
	 * @param jdbcHelper
	 */
	public void setJdbcHelper(JdbcHelper jdbcHelper) {
		this.jdbcHelper = jdbcHelper;
	}


	public void initialize(){
		if(jdbcHelper == null)
			this.jdbcHelper = JdbcHelperFactory.getJdbcHelper(getDataSource());
		
		if( lobHandler == null)
			if ( jdbcHelper.getDatabaseType() == DatabaseType.oracle ){
				lobHandler = new OracleLobHandler();
			}else{
			    lobHandler = new DefaultLobHandler();
			}
	}
	
	
	public Object executeScript(final boolean stopOnError, final Reader reader) {
		return execute(new ConnectionCallback<Object>() {
			public Object doInConnection(Connection connection)
					throws SQLException, DataAccessException {
				try {
					return runScript(connection, stopOnError, reader);
				} catch (IOException e) {
					return null;
				}
			}
		});
	}

	/**
	 * Return the maximum number of entries for this template's SQL cache.
	 */
	public int getCacheLimit() {
		return this.cacheLimit;
	}

	/**
	 * @return
	 */
	public JdbcHelper getJdbcHelper() {
		return jdbcHelper;
	}

	/**
	 * Obtain a parsed representation of the given SQL statement.
	 * <p>
	 * The default implementation uses an LRU cache with an upper limit of 256
	 * entries.
	 * 
	 * @param sql
	 *            the original SQL
	 * @return a representation of the parsed SQL statement
	 */
	protected ParsedSql getParsedSql(String sql) {
		if (getCacheLimit() <= 0) {
			return NamedParameterUtils.parseSqlStatement(sql);
		}
		synchronized (this.parsedSqlCache) {
			ParsedSql parsedSql = this.parsedSqlCache.get(sql);
			if (parsedSql == null) {
				parsedSql = NamedParameterUtils.parseSqlStatement(sql);
				this.parsedSqlCache.put(sql, parsedSql);
			}
			return parsedSql;
		}
	}

	/**
	 * Build a PreparedStatementCreator based on the given SQL and named
	 * parameters.
	 * <p>
	 * Note: Not used for the <code>update</code> variant with generated key
	 * handling.
	 * 
	 * @param sql
	 *            SQL to execute
	 * @param paramSource
	 *            container of arguments to bind
	 * @return the corresponding PreparedStatementCreator
	 */
	protected PreparedStatementCreator getNamedPreparedStatementCreator(String sql, SqlParameterSource paramSource) {
		ParsedSql parsedSql = getParsedSql(sql);
		String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
		Object[] params = NamedParameterUtils.buildValueArray(parsedSql,paramSource, null);
		int[] paramTypes = NamedParameterUtils.buildSqlTypeArray(parsedSql,paramSource);
		PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(sqlToUse, paramTypes);
		return pscf.newPreparedStatementCreator(params);
	}

	public <T> List<T> queryScrollable(String sql, int startIndex, int numResults, Class<T> elementType, Object[] args, int[] argTypes) {
		
		ScrollablePreparedStatementCreator creator = new ScrollablePreparedStatementCreator( sql, startIndex, numResults, args, argTypes, getJdbcHelper());
		
		ScrollableResultSetExtractor extractor = new ScrollableResultSetExtractor( startIndex, numResults, getSingleColumnRowMapper(elementType), getJdbcHelper());	
		
		return (java.util.List<T>)query(creator, extractor);
	}

	public <T> List<T> queryScrollable(String sql, int startIndex, int numResults, RowMapper<T> rowMapper, Object[] args, int[] argTypes) {
		
		ScrollablePreparedStatementCreator creator = new ScrollablePreparedStatementCreator( sql, startIndex, numResults, args, argTypes, getJdbcHelper());
		
		ScrollableResultSetExtractor extractor = new ScrollableResultSetExtractor( startIndex, numResults, rowMapper, getJdbcHelper());	
		
		return (java.util.List<T>)query(creator, extractor);
	}
	
	public List queryScrollable(String sql, int startIndex, int numResults, Object[] args, int[] argTypes) {		
		ScrollablePreparedStatementCreator creator = new ScrollablePreparedStatementCreator(sql, startIndex, numResults, args, argTypes, getJdbcHelper());		
		ScrollableResultSetExtractor extractor = new ScrollableResultSetExtractor(startIndex, numResults, getColumnMapRowMapper(), getJdbcHelper());
		return (List) query(creator, extractor);
	}

	protected Object runScript(Connection conn, boolean stopOnError, Reader reader) throws SQLException, IOException {

		StringBuffer command = null;

		List<Object> list = new ArrayList<Object>();

		try {
			LineNumberReader lineReader = new LineNumberReader(reader);
			String line = null;
			while ((line = lineReader.readLine()) != null) {
				if (command == null) {
					command = new StringBuffer();
				}
				String trimmedLine = line.trim();
				if (trimmedLine.startsWith("--")) {
					if (logger.isDebugEnabled())
						logger.debug(trimmedLine);
				} else if (trimmedLine.length() < 1
						|| trimmedLine.startsWith("//")) {
					// Do nothing
				} else if (trimmedLine.length() < 1
						|| trimmedLine.startsWith("--")) {
					// Do nothing
				} else if (trimmedLine.endsWith(";")) {
					command.append(line.substring(0, line.lastIndexOf(";")));
					command.append(" ");

					Statement statement = conn.createStatement();
					if (logger.isDebugEnabled()) {
						logger.debug("Executing SQL script command [" + command + "]");
					}

					boolean hasResults = false;
					if (stopOnError) {
						hasResults = statement.execute(command.toString());
					} else {
						try {
							statement.execute(command.toString());
						} catch (SQLException e) {
							if (logger.isDebugEnabled())
								logger.error("Error executing: " + command, e);
							throw e;
						}
					}
					ResultSet rs = statement.getResultSet();
					if (hasResults && rs != null) {
						RowMapperResultSetExtractor<Map<String, Object>> rse = new RowMapperResultSetExtractor<Map<String, Object>>(
								getColumnMapRowMapper());
						List<Map<String, Object>> rows = rse.extractData(rs);
						list.add(rows);
					}
					command = null;
				} else {
					command.append(line);
					command.append(" ");
				}
			}

			return list;
		} catch (SQLException e) {
			logger.error("Error executing: " + command, e);
			throw e;
		} catch (IOException e) {
			logger.error("Error executing: " + command, e);
			throw e;
		}
	}

	/**
	 * Specify the maximum number of entries for this template's SQL cache. Default is 256.
	 */
	public void setCacheLimit(int cacheLimit) {
		this.cacheLimit = cacheLimit;
	}

	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
		this.jdbcHelper = JdbcHelperFactory.getJdbcHelper(getDataSource());
	}
	
	
	// Methods from SimpleJdbcTemplate :
	public <T> List<T> query(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) throws DataAccessException {
		return query(sql, new MapSqlParameterSource(paramMap), rowMapper);
	}
	
	public <T> List<T> query(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper) throws DataAccessException {
		return query(getNamedPreparedStatementCreator(sql, paramSource), rowMapper);
	}

	public <T> List<T> queryForList(String sql, Map<String, ?> paramMap, Class<T> elementType) throws DataAccessException {
		return query(sql, new MapSqlParameterSource(paramMap), new SingleColumnRowMapper<T>(elementType));
	}
		
	public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
		return batchUpdate(sql, batchArgs, new int[0]);
	}

	public int[] batchUpdate(String sql, List<Object[]> batchArgs, int[] argTypes) {
		return BatchUpdateUtils.executeBatchUpdate(sql, batchArgs, argTypes, this);
	}
	
	public int[] batchUpdate(String sql, Map<String, ?>[] batchValues) {
		SqlParameterSource[] batchArgs = new SqlParameterSource[batchValues.length];
		int i = 0;
		for (Map<String, ?> values : batchValues) {
			batchArgs[i] = new MapSqlParameterSource(values);
			i++;
		}
		return batchUpdate(sql, batchArgs);
	}
	
	public int[] batchUpdate(String sql, SqlParameterSource[] batchArgs) {
		ParsedSql parsedSql = this.getParsedSql(sql);
		return NamedParameterBatchUpdateUtils.executeBatchUpdateWithNamedParameters(parsedSql, batchArgs, this);
	}
		
}