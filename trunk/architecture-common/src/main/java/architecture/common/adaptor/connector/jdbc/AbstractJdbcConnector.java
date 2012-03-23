package architecture.common.adaptor.connector.jdbc;

import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.JdbcUtils;

import architecture.common.adaptor.Connector;
import architecture.common.jdbc.JdbcType;
import architecture.common.jdbc.ParameterMapping;

public abstract class AbstractJdbcConnector extends JdbcDaoSupport implements Connector {
	
	protected Log log = LogFactory.getLog(getClass());
	
	protected abstract String getQueryString(String key);
	
	protected List<Map<String, Object>> pull(final String queryString){
		return getJdbcTemplate().queryForList(queryString);
	}

	protected List<Map<String, Object>> pull(final String queryString, Object... args){
		return getJdbcTemplate().queryForList(queryString, args);
	}
	
	protected List<Map<String, Object>> pull(final String queryString, final List<ParameterMapping> parameterMappings, final Object[] args){

		//log.debug("pulling..." );
		
		return getJdbcTemplate().query(queryString, args, new RowMapper<Map<String, Object>>(){	
			public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {				
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				Map<String, Object> mapOfColValues = createColumnMap(columnCount);				
				for (int i = 1; i <= columnCount; i++) {
					String key = getColumnKey(lookupColumnName(rsmd, i));
					Object obj = getColumnValue(rs, i);
					mapOfColValues.put(key, obj);
				}
				
				return mapOfColValues;
			}			
			
			protected Map<String, Object> createColumnMap(int columnCount) {
				return new LinkedHashMap<String, Object>(columnCount);
			}
			protected String getColumnKey(String columnName) {
				return columnName;
			}
			protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
				
				//LOG.debug( "parameterMappings :" + parameterMappings.size());
				
				for(ParameterMapping mapping : parameterMappings){					
					//LOG.debug( index + " mapping match :" + mapping.getIndex());					
					if(index == mapping.getIndex()){
						if( String.class == mapping.getJavaType() ){
							
							if(!StringUtils.isEmpty( mapping.getEncoding())){							
								
								String value = rs.getString(index);
								if(StringUtils.isEmpty(value))
									value = "";
								
								String[] encoding = StringUtils.split(mapping.getEncoding(), ">");								
								try {
									if( encoding.length == 2 )
										return new String(value.getBytes(encoding[0]), encoding[1]);
									else if ( encoding.length == 1 )
										return new String(value.getBytes(), encoding[0]);
								} catch (UnsupportedEncodingException e) {
									LOG.error(e);
									return value;
								}	
							}
							
						}else if (Long.class == mapping.getJavaType() ){
							String value = rs.getString(index);
							if(StringUtils.isEmpty(value))
								value = "0";
							
							return new Long(value);
						}
					}
				}
				return JdbcUtils.getResultSetValue(rs, index);
			}
		});
	}
	
	/**
	 * Batch ...
	 * @param queryString
	 * @param parameterMappings
	 * @param rows
	 * @return
	 */
	protected Object deliver(final String queryString, final List<ParameterMapping> parameterMappings, final List<Map<String, Object>> rows) { 
		
		int[] cnt = getJdbcTemplate().batchUpdate(queryString, new BatchPreparedStatementSetter(){
			public void setValues(PreparedStatement ps, int i) throws SQLException {
                Map<String, Object> row = rows.get(i);                
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
									LOG.error(e);
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
				return rows.size();
			}});
		int sum = 0 ;
		for( int c : cnt){
			sum = sum + c ;
		}		
		return sum;
	}
	
	protected Object deliver(final String queryString, final List<ParameterMapping> parameterMappings, final Map<String, Object> row) { 
		return getJdbcTemplate().update(queryString, new PreparedStatementSetter(){
			public void setValues(PreparedStatement ps) throws SQLException {
				
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
									LOG.error(e);
								}	
							}
                		}
                	}
                	
                	if (valueToUse == null)
                		ps.setNull(mapping.getIndex(), jdbcType.TYPE_CODE);
                	else
                		ps.setObject(mapping.getIndex(), valueToUse, jdbcType.TYPE_CODE);
                }				
				
			}});
	}
	
	protected Object deliver(final String queryString){
		return getJdbcTemplate().update(queryString);
	}
	
	public static String lookupColumnName(ResultSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
		String name = resultSetMetaData.getColumnLabel(columnIndex);
		if (name == null || name.length() < 1) {
			name = resultSetMetaData.getColumnName(columnIndex);
		}
		return name;
	}
}
