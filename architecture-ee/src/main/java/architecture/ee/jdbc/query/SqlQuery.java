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
package architecture.ee.jdbc.query;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

import architecture.ee.jdbc.recordset.Recordset;

public interface SqlQuery {

	public static final Class<Map> DEFAULT_RETURN_TYPE = Map.class;

	public SqlQuery setDataSource(DataSource dataSource);

	public SqlQuery reset();

	public SqlQuery setStatement(String catelogy, String key);
	
	public SqlQuery setStatement(String statement);

	public SqlQuery setString(String value);
	
	/**
	 * 
	 * @param values
	 * @param types java.sql.Types 에 정의된 데이터 유형
	 * @return
	 */
	public SqlQuery setParameters(Object[] values, int[] types);

	/**
	 * 쿼리 수행후 리턴되는 데이터 타입을 정의한다. uniqueResult(), list() 함수 사용하는 경우는 반듯이 리턴 타입을
	 * 정의해야 한다.
	 * 
	 * @param returnType
	 * @return
	 */
	public SqlQuery setReturnType(Class<?> returnType);

	/**
	 * 이 파라메터는 다이나믹으로 선언된 부분에만 적용되어 동적 쿼리 생성에 사용된다.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public SqlQuery setAdditionalParameter(String key, Object value);

	/**
	 * 
	 * @param startIndex
	 *            (0 부터 시작)
	 * @return
	 */
	public SqlQuery setStartIndex(int startIndex);

	/**
	 * 
	 * @param maxResults
	 * @return
	 */
	public SqlQuery setMaxResults(int maxResults);

	/**
	 * 
	 * @param statement
	 *            실행할 쿼리 이름
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String statement);

	/**
	 * 
	 * @param statement
	 *            실행할 쿼리 이름
	 * @param elementType
	 * @return
	 */
	public <T> List<T> queryForList(String statement, Class<T> elementType);

	/**
	 * 
	 * @param statement
	 *            실행할 쿼리 이름
	 * @param elementType
	 * @param params
	 * @param types
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String statement, Object[] params, int[] types);

	/**
	 * 
	 * @param statement
	 *            실행할 쿼리 이름
	 * @param elementType
	 * @param params
	 * @param paramTypes
	 * @return
	 */
	public <T> List<T> queryForList(String statement, Object[] params, int[] paramTypes, Class<T> elementType);

	/**
	 * 
	 * 
	 * @param statement
	 *            실행할 쿼리 이름
	 * @param elementType
	 */
	public Map<String, Object> queryForMap(String statement);

	/**
	 * 
	 * @param statement
	 *            실행할 쿼리 이름
	 * @param params
	 *            파라메터 객체 배열값
	 * @param paramTypes
	 *            파마케터 객체의 데이터 타입 (java.sql.Types 에 정의된 값)
	 * @return
	 */
	public Map<String, Object> queryForMap(String statement, Object[] params, int[] paramTypes);

	/**
	 * 매핑된 쿼리를 실행하고 결과 객체를 리턴한다.
	 * 
	 * @param statement
	 *            실행할 쿼리 이름
	 * @param elementType
	 *            리턴될 결과 객체 타입
	 * @return
	 */
	public <T> T queryForObject(String statement, Class<T> elementType);

	/**
	 * 
	 * @param statement
	 * @param params
	 * @param paramTypes
	 * @param rowMapper
	 * @return
	 */
	public <T> T queryForObject(String statement, Object[] params, int[] paramTypes, RowMapper<T> rowMapper );
	
	
	/**
	 * 매핑된 쿼리를 실행하고 결과 객체를 리턴한다.
	 * 
	 * @param statement
	 *            실행할 쿼리 이름
	 * @param elementType
	 *            리턴될 결과 객체 타입
	 * @param 파라메터
	 *            객체 배열값
	 * @param paramTypes
	 *            파마케터 객체의 데이터 타입 (java.sql.Types 에 정의된 값
	 * @return
	 */
	public <T> T queryForObject(String statement, Object[] params, int[] paramTypes, Class<T> elementType);

	public Object uniqueResult();
	
	public <T> T uniqueResult(Class<T> elementType);
	
	public <T> T uniqueResult(RowMapper<T> rowMapper);

	public List list();

	public Object executeScript();

	public SqlQuery addToBatch();

	/**
	 * SQL 배치를 실행한다.
	 */
	public int executeUpdate();

	public int[] executeBatchUpdate();

	/**
	 * 
	 * @param statement
	 *            SQL command to precompile
	 * @param recordset
	 *            Recordset containing the values to be set into the prepared
	 *            statement
	 * @param params
	 *            Array containing the names of the fields to use. The order
	 *            must match the place holders (those ? marks) in the prepared
	 *            statement
	 * @return The number of records affected
	 */
	public int executeUpdate(String statement, Recordset recordset, String[] params);

	public int[] executeBatchUpdate(String statement, Recordset recordset, String[] params);

}