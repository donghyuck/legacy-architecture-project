package architecture.ee.spring.jdbc.support;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.util.LinkedCaseInsensitiveMap;

import architecture.common.jdbc.ResultMapping;
import architecture.common.util.StringUtils;

public class MappedColumnMapRowMapper implements RowMapper<Map<String, Object>> {

	private static final Log log = LogFactory.getLog(MappedColumnMapRowMapper.class);
	
	private List<ResultMapping> parameterMappings ;

	public MappedColumnMapRowMapper(List<ResultMapping> parameterMappings) {
		super();
		this.parameterMappings = parameterMappings;
	}

	
	public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Map<String, Object> mapOfColValues = createColumnMap(columnCount);
		for (int i = 1; i <= columnCount; i++) {
			String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
			Object obj = getColumnValue(rs, i);
			mapOfColValues.put(key, obj);
		}
		return mapOfColValues;
	
	}

	
	/**
	 * Create a Map instance to be used as column map.
	 * <p>By default, a linked case-insensitive Map will be created.
	 * @param columnCount the column count, to be used as initial
	 * capacity for the Map
	 * @return the new Map instance
	 * @see org.springframework.util.LinkedCaseInsensitiveMap
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Object> createColumnMap(int columnCount) {
		return new LinkedCaseInsensitiveMap<Object>(columnCount);
	}

	/**
	 * Determine the key to use for the given column in the column Map.
	 * @param columnName the column name as returned by the ResultSet
	 * @return the column key to use
	 * @see java.sql.ResultSetMetaData#getColumnName
	 */
	protected String getColumnKey(String columnName) {
		return columnName;
	}

	
	/**
	 * Retrieve a JDBC object value for the specified column.
	 * <p>The default implementation uses the <code>getObject</code> method.
	 * Additionally, this implementation includes a "hack" to get around Oracle
	 * returning a non standard object for their TIMESTAMP datatype.
	 * @param rs is the ResultSet holding the data
	 * @param index is the column index
	 * @return the Object returned
	 * @see org.springframework.jdbc.support.JdbcUtils#getResultSetValue
	 */
	protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
		
		for(ResultMapping mapping : parameterMappings){
			
			//log.debug("using parameter mappings ");
			
			if(index == mapping.getIndex()){
				
				// 1. java type is stirng 
				if( String.class == mapping.getJavaType() ){					
					String value = rs.getString(index);					
										
					// 1.1 cipher 
					if( !StringUtils.isEmpty(value) && !StringUtils.isEmpty( mapping.getCipher())){
						try {
							String transformation = mapping.getCipher();
							boolean isMessageDigest = StringUtils.equals(transformation, "MessageDigest");
							
							if(isMessageDigest){
								MessageDigest md = MessageDigest.getInstance(mapping.getCipherKeyAlg());
								byte[] digest = md.digest(Utf8.encode(value));						        
								if(!StringUtils.isEmpty( mapping.getEncoding())){
									if(mapping.getEncoding().toUpperCase().equals("BASE64")){
										return Utf8.decode(Base64.encode(digest));
									}
								}else{
									return new String(Hex.encodeHexString(digest));
								}
							
							}else{							
								Cipher cipher = Cipher.getInstance(transformation);
								SecretKeySpec skeySpec = new SecretKeySpec(Hex.decodeHex(mapping.getCipherKey().toCharArray()), mapping.getCipherKeyAlg());
								cipher.init(Cipher.DECRYPT_MODE, skeySpec);	
								byte raw[] ;							
								if(!StringUtils.isEmpty( mapping.getEncoding())){
									String enc = mapping.getEncoding();
									if(enc.toUpperCase().equals("BASE64")){								
								        raw = Base64.decode(value.getBytes());
									}else{
										raw = value.getBytes();
									}
								}else{
									raw= value.getBytes();
								}								
						        byte stringBytes[] = cipher.doFinal(raw);	        
						        return new String(stringBytes);	
							}
						} catch (Exception e) {
							log.error(e);
						}								
						return value;
					}
					
					if(!StringUtils.isEmpty( mapping.getEncoding())){								
						String[] encoding = StringUtils.split(mapping.getEncoding(), ">");								
						try {
							if( encoding.length == 2 )
								return new String(value.getBytes(encoding[0]), encoding[1]);
							else if ( encoding.length == 1 ){
								return new String(value.getBytes(), encoding[0] );
							}
						} catch (UnsupportedEncodingException e) {
							log.error(e);
							return value;
						}	
					}
					
				}else if (Long.class == mapping.getJavaType() ){
					String value = rs.getString(index);
					if(StringUtils.isEmpty(value))
						value = "0";							
					return new Long(value);
				}else if (Integer.class == mapping.getJavaType() ){
					String value = rs.getString(index);
					if(StringUtils.isEmpty(value))
						value = "0";							
					return new Integer(value);
				}else if (Double.class == mapping.getJavaType()){
					String value = rs.getString(index);
					if(StringUtils.isEmpty(value))
						value = "0";	
					return new Double(value);
				}
			}
		}
		
		return JdbcUtils.getResultSetValue(rs, index);
	}	
	
}
