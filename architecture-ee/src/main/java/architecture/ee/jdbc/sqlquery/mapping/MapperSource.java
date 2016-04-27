/*
 * Copyright 2016 donghyuck
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

package architecture.ee.jdbc.sqlquery.mapping;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import architecture.common.jdbc.ParameterMapping;

public class MapperSource{

    private String ID;
    
    private String name;
    
    private List<ParameterMapping> mappedFields;

    private Class<?> mappedClass;
    
    public String getName(){
	return name;
    }
        
    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public List<ParameterMapping> getMappedFields() {
        return mappedFields;
    }

    
    public <T> RowMapper<T> createRowMapper(Class<T> requiredType){
	ParameterMappingRowMapper<T> mapper = new ParameterMappingRowMapper<T>(requiredType);
	mapper.mapperSource = this;
	return mapper;
    }

    
    public static class Builder {
	private MapperSource mappedRowMapper = new MapperSource();
	public Builder(Class mappedClass, List<ParameterMapping> mappedFields) {
	    mappedRowMapper.mappedClass = mappedClass;
	    mappedRowMapper.mappedFields = mappedFields;
	}	
	public Builder name(String name) {
	    mappedRowMapper.name = name;
	    return this;
	}
	public MapperSource build() {
	    assert mappedRowMapper.mappedClass != null;
	    assert mappedRowMapper.mappedFields != null;
	    return mappedRowMapper;
	}
    }
        

    public static class ParameterMappingRowMapper<T> implements RowMapper<T> {
	/** The class we are mapping to */
	
	private Class<T> mappedClass;
	
	private MapperSource mapperSource;

	public ParameterMappingRowMapper() {
	}
	
	public ParameterMappingRowMapper(Class<T> mappedClass) {
	    this.mappedClass = mappedClass;
	}
	
	public final Class<T> getMappedClass() {
	   return this.mappedClass;
	}
	
	protected Object getColumnValue(ResultSet rs, int index, ParameterMapping pm) throws SQLException {	
	    return JdbcUtils.getResultSetValue(rs, index, pm.getJavaType());
	}

	public T mapRow(ResultSet rs, int rowNum) throws SQLException {	    
	    T mappedObject = BeanUtils.instantiateClass(mapperSource.mappedClass, mappedClass );
	    BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
	    
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int columnCount = rsmd.getColumnCount();
	    
	    for (ParameterMapping mapping : mapperSource.mappedFields) {
		for (int index = 1; index <= columnCount; index++) {
		    String column = JdbcUtils.lookupColumnName(rsmd, index);
		    if( StringUtils.equals(column, mapping.getColumn())){
			bw.setPropertyValue(mapping.getProperty(), getColumnValue(rs, index, mapping));
			break;
		    }
		}		
	    }
	    
	    return mappedObject;
	}

    }
}
