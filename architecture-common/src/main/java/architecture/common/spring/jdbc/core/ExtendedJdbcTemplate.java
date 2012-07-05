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
package architecture.common.spring.jdbc.core;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.jdbc.support.lob.OracleLobHandler;

import architecture.common.jdbc.JdbcType;
import architecture.common.jdbc.JdbcUtils;
import architecture.common.jdbc.ParameterMapping;
import architecture.common.jdbc.schema.DatabaseType;


/**
 * 스프링프레임워크에서 제공하는 JdbcTemplate 을 확장한 클래스.
 * @author   DongHyuck, Son
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


		private DatabaseType databaseType;
		private Log log = LogFactory.getLog(getClass());
		
		public ScrollablePreparedStatementCreator(String sql, int startIndex, int numResults, Object args[], int[] types, DatabaseType databaseType) {
			
			this.startIndex = startIndex;
			this.numResults = numResults;
			this.params = args;
			this.paramTypes = types;
			this.sqlToUse = sql;
			this.databaseType = databaseType;
		}
		
		/**
		 * 데이터베이스 제품에 따라 쿼리 결과에 대한 스크롤을 지원하도록 PreparedStatement 다르게 생성한다.
		 */
		public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			
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
				if (databaseType.scrollResultsSupported){
					return connection.prepareStatement(sqlToUse, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				}else{
					return connection.prepareStatement(sqlToUse);
				}
			}

			if (params != null) {
				PreparedStatementCreatorFactory pscf;
				if (paramTypes != null) {
					pscf = new PreparedStatementCreatorFactory(sqlToUse, paramTypes);
				} else {
					pscf = new PreparedStatementCreatorFactory(sqlToUse);
				}
				ps = pscf.newPreparedStatementCreator( params ).createPreparedStatement(connection);
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

		private DatabaseType databaseType;
		private Log log = LogFactory.getLog(getClass());

		public ScrollableResultSetExtractor(int startIndex, int numResults, RowMapper mapper, DatabaseType databaseType) {			
			this.startIndex = startIndex;
			this.numResults = numResults;
			this.mapper = mapper;
			this.databaseType = databaseType;
		}

		public Object extractData(ResultSet rs) throws SQLException, DataAccessException {			
			
			ArrayList<Object> list = new ArrayList<Object>();
			
			if (DatabaseType.mysql == databaseType || DatabaseType.postgresql == databaseType) {
				for (int count = 0; rs.next(); count++)
					list.add(mapper.mapRow(rs, count));

			} else {
				JdbcUtils.setFetchSize(databaseType, rs, startIndex + numResults);
				JdbcUtils.scrollResultSet(databaseType, rs, startIndex);
				for (int i = 0; i < numResults && rs.next(); i++) {
					Object o = mapper.mapRow(rs, i);
					list.add(o);
				}
			}
			return list;
		}
		
		public void setFetchSize(ResultSet rs, int fetchSize) {
			if (databaseType.fetchSizeSupported)
				try {
					rs.setFetchSize(fetchSize);
				} catch (Throwable t) {
					databaseType.fetchSizeSupported = false;
				}
		}

		public void scrollResultSet(ResultSet rs, int rowNumber) throws SQLException {
			if (databaseType.scrollResultsSupported) {
				if (rowNumber > 0) {
					rs.setFetchDirection(1000);
					rs.absolute(rowNumber);
				}
			} else {
				for (int i = 0; i < rowNumber; i++)
					rs.next();
			}
		}
	}

	
	public static class MappedPreparedStatementSetter implements PreparedStatementSetter {
		
		private List<ParameterMapping> parameterMappings ;
		private Map<String, Object> parameters;		
		private Log log = LogFactory.getLog(getClass());
		
		private MappedPreparedStatementSetter(Map<String, Object> parameters, List<ParameterMapping> parameterMappings) {
			this.parameters = parameters;
			this.parameterMappings = parameterMappings;
		}

		public void setValues(PreparedStatement ps) throws SQLException {
						log.debug(parameterMappings.size());
						
			for( ParameterMapping mapping : parameterMappings ){            	
				JdbcType jdbcType = mapping.getJdbcType();            	
            	Object valueToUse = parameters.get( mapping.getProperty() );
            	
            	log.debug("jdbcType="+jdbcType + ", value=" + valueToUse );            	
            	if( valueToUse == null && mapping.getJavaType() == Date.class ){
            		valueToUse = new Date();
            	}                 	
            	if( valueToUse instanceof Date && jdbcType == JdbcType.VARCHAR ){
            		valueToUse = DateFormatUtils.format((Date)valueToUse, mapping.getPattern());
            	}            
            	
            	if( valueToUse instanceof String && jdbcType == JdbcType.VARCHAR ){
            		String stringValue = (String)valueToUse;
            		if(!StringUtils.isEmpty( mapping.getEncoding())){
						if( !StringUtils.isEmpty(stringValue)){
							String[] encoding = StringUtils.split(mapping.getEncoding(), ">");		
							try {
								if( encoding.length == 2 )
									valueToUse =  new String(stringValue.getBytes(encoding[0]), encoding[1]);
								else if ( encoding.length == 1 )
									valueToUse =  new String(stringValue.getBytes(), encoding[0]);
							} catch (UnsupportedEncodingException e) {
								log.error(e);
							}	
						}
            		}
            	}
            	
            	if (valueToUse == null)
            		ps.setNull(mapping.getIndex(), jdbcType.TYPE_CODE);
            	else
            		ps.setObject(mapping.getIndex(), valueToUse, jdbcType.TYPE_CODE);            	
            }				
		}
		
	}

	/**
	 * @uml.property  name="databaseType"
	 * @uml.associationEnd  
	 */
	private DatabaseType databaseType;

	/**
	 * @uml.property  name="lobHandler"
	 */
	private LobHandler lobHandler = null;		
	
	public ExtendedJdbcTemplate(DataSource dataSource) {
		super(dataSource);		
	}

	/**
	 * @return
	 * @uml.property  name="lobHandler"
	 */
	public LobHandler getLobHandler() {
		return lobHandler;
	}
	
	/**
	 * @return
	 * @uml.property  name="databaseType"
	 */
	public DatabaseType getDatabaseType() {
		return databaseType;
	}
	
	/**
	 * @param lobHandler
	 * @uml.property  name="lobHandler"
	 */
	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}

	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
		initialize();
	}
	
	/**
	 * @param databaseType
	 * @uml.property  name="databaseType"
	 */
	public void setDatabaseType(DatabaseType databaseType){
		this.databaseType = databaseType;
	}
	
	public void initialize(){				 
		if(this.databaseType == null && this.getDataSource() != null){
			this.databaseType = JdbcUtils.getDatabaseType(getDataSource());
			
			if (databaseType == DatabaseType.oracle) {
				OracleLobHandler oracleLobHandler = new OracleLobHandler();
				oracleLobHandler.setNativeJdbcExtractor(getNativeJdbcExtractor());
				this.lobHandler = oracleLobHandler;
			} else {
				lobHandler = new DefaultLobHandler();
			}
		}				
	}
		
	// *********************************************
	// Public Methods for Scrollable
	// ********************************************
	protected PreparedStatementCreator newScrollablePreparedStatementCreator(String sql, int startIndex, int numResults, Object args[], int[] argTypes, DatabaseType databaseType) {
		return new ScrollablePreparedStatementCreator( sql, startIndex, numResults, args, argTypes, databaseType);
	}
	
	protected ResultSetExtractor newScrollableResultSetExtractor(int startIndex, int numResults, RowMapper mapper, DatabaseType databaseType){
		return new ScrollableResultSetExtractor( startIndex, numResults, mapper, databaseType );
	} 
	
	public <T> List<T> queryScrollable(String sql, int startIndex, int numResults, Object[] args, int[] argTypes, Class<T> elementType) {
		return (java.util.List<T>)query(
				newScrollablePreparedStatementCreator( sql, startIndex, numResults, args, argTypes, databaseType), 
				argTypes == null ? newArgPreparedStatementSetter(args) : newArgTypePreparedStatementSetter(args, argTypes), 
				newScrollableResultSetExtractor( startIndex, numResults, getSingleColumnRowMapper(elementType), databaseType));
	}

	public <T> List<T> queryScrollable(String sql, int startIndex, int numResults, Object[] args, int[] argTypes, RowMapper<T> rowMapper) {
		return (java.util.List<T>)query(
				newScrollablePreparedStatementCreator( sql, startIndex, numResults, args, argTypes, databaseType), 				
				argTypes == null ? newArgPreparedStatementSetter(args) : newArgTypePreparedStatementSetter(args, argTypes), 
				newScrollableResultSetExtractor( startIndex, numResults, rowMapper, databaseType));		
		
	}
	
	public List queryScrollable(String sql, int startIndex, int numResults, Object[] args, int[] argTypes) {		
		return (java.util.List)query(
				newScrollablePreparedStatementCreator( sql, startIndex, numResults, args, argTypes, databaseType), 
				argTypes == null ? newArgPreparedStatementSetter(args) : newArgTypePreparedStatementSetter(args, argTypes), 
				newScrollableResultSetExtractor( startIndex, numResults, getColumnMapRowMapper(), databaseType));		
	}
	
	public <T> List<T> queryScrollable(String sql, List<ParameterMapping> parameterMappings, int startIndex, int numResults, Map<String, Object> parameters, RowMapper<T> rowMapper) {
		
		return (java.util.List<T>)query(
				newScrollablePreparedStatementCreator( sql, startIndex, numResults, null, null, databaseType), 
				newMappedPreparedStatementSetter(parameters, parameterMappings), 
				newScrollableResultSetExtractor( startIndex, numResults, rowMapper, databaseType));		
		
	}
	
	// *********************************************
	// Public Methods for Extensions
	// *********************************************
	protected PreparedStatementSetter newMappedPreparedStatementSetter(Map<String, Object> parameters, List<ParameterMapping> parameterMappings) {
		return new MappedPreparedStatementSetter( parameters, parameterMappings );
	}		
	
	public <T> T queryForObject(String sql, List<ParameterMapping> parameterMappings, Map<String, Object> parameters, RowMapper<T> rowMapper) throws DataAccessException {				
		List<T> results = query(sql, newMappedPreparedStatementSetter(parameters, parameterMappings), new RowMapperResultSetExtractor<T>(rowMapper, 1));		
		return DataAccessUtils.requiredSingleResult(results);
	}
	
	public <T> List<T> queryForList(String sql, List<ParameterMapping> parameterMappings, Map<String, Object> parameters, RowMapper<T> rowMapper) throws DataAccessException {				
		return query(sql, newMappedPreparedStatementSetter(parameters, parameterMappings), new RowMapperResultSetExtractor<T>(rowMapper));		
	}
	
	// *********************************************
	// Public Methods for Update and Batch
	// *********************************************
	public int update (String sql, final List<ParameterMapping> parameterMappings, final Map<String, Object> parameters){
		return update(sql, newMappedPreparedStatementSetter(parameters, parameterMappings));
	}
	

	
	public int[] batchUpdate(String sql, final List<ParameterMapping> parameterMappings, final List<Map<String, Object>> parameters){		
		return batchUpdate(sql, new BatchPreparedStatementSetter(){
			public void setValues(PreparedStatement ps, int i) throws SQLException {
                Map<String, Object> row = parameters.get(i);                
                for( ParameterMapping mapping : parameterMappings ){     	
                	JdbcType jdbcType = mapping.getJdbcType();                	
                	Object value = row.get( mapping.getProperty() );
                	Object valueToUse = value;                	
                	
                	if( valueToUse == null && mapping.getJavaType() == Date.class ){
                		valueToUse = new Date();
                	}          
                	
                	if( valueToUse instanceof Date && jdbcType == JdbcType.VARCHAR ){
                		valueToUse = DateFormatUtils.format((Date)valueToUse, mapping.getPattern());
                	}                	
                	
                	if( valueToUse instanceof String && jdbcType == JdbcType.VARCHAR ){
                		String stringValue = (String)valueToUse;
                		if(!StringUtils.isEmpty( mapping.getEncoding())){
							if( !StringUtils.isEmpty(stringValue)){
								String[] encoding = StringUtils.split(mapping.getEncoding(), ">");		
								try {
									if( encoding.length == 2 )
										valueToUse =  new String(stringValue.getBytes(encoding[0]), encoding[1]);
									else if ( encoding.length == 1 )
										valueToUse =  new String(stringValue.getBytes(), encoding[0]);
									
								} catch (UnsupportedEncodingException e) {
									logger.error(e);
								}	
							}
                		}
                	}                	
                	if (valueToUse == null)
                		ps.setNull(mapping.getIndex(), jdbcType.TYPE_CODE);
                	else                	
                		ps.setObject(mapping.getIndex(), valueToUse, jdbcType.TYPE_CODE);
                }
			}
			public int getBatchSize() {
				return parameters.size();
			}});
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

}