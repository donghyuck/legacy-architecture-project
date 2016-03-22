/*
 * Copyright 2012 Donghyuck, Son
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
package architecture.ee.jdbc.sqlquery;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

public interface SqlQuery {

    /**
     * 사용할 데이터소스를 설정한다.
     * 
     * @param dataSource
     * @return
     */
    public SqlQuery setDataSource(DataSource dataSource);

    /**
     * list(), queryForList() 함수 호출시에 데이터 페이징 처리를 위하여 사용된다. 쿼리 결과의 startIndex 부터
     * 데이터를 읽어 데이터를 리턴한다. 쿼리 실행후에 startIndex 값은 0 으로 변경된다.
     * 
     * @param startIndex
     *            (0 부터 시작)
     * @return
     */
    public SqlQuery setStartIndex(int startIndex);

    /**
     * list(), queryForList() 함수 호출시에 데이터 페이징 처리를 위하여 사용된다. 쿼리 결과의 startIndex 부터
     * maxResults 까지의 데이터를 읽어 데이터를 리턴한다. 쿼리 실행후에 maxResults 값은 0 으로 변경된다. 따라서 0
     * 으로 값을 지정하는 것은 페이지 처리를 하지 않는 것과 동일한 결과를 가져온다.
     * 
     * @param maxResults
     * @return
     */
    public SqlQuery setMaxResults(int maxResults);

    public SqlQuery setAdditionalParameters(Map<String, Object> additionalParameters);

    /** ====================================================== */
    /** OLD STYLE API */
    /** ====================================================== */

    /**
     * 단일 ROW 에 해당하는 elementType 형태의 값을 리턴한다. 하나 이상의 ROW 가 존재하는 경우, 리턴 컬럼이 하나
     * 이상인 경우 예외가 발생된다.
     * 
     * @param <T>
     * @param statement
     * @param elementType
     * @return
     */
    public <T> T queryForObject(String statement, Class<T> elementType);

    /**
     * (rowMapper 에 의하여 매핑되는) 단일 ROW 에 해당하는 객체를 리턴한다. 하나 이상의 ROW 가 존재하는 경우, 리턴
     * 컬럼이 하나 이상인 경우 예외가 발생된다.
     * 
     * @param statement
     * @param rowMapper
     * @return
     */
    public <T> T queryForObject(String statement, RowMapper<T> rowMapper);

    /**
     * 단일 ROW 에 해당하는 elementType 형태의 값을 리턴한다. 하나 이상의 ROW 가 존재하는 경우, 리턴 컬럼이 하나
     * 이상인 경우 예외가 발생된다.
     * 
     * @param <T>
     * @param statement
     * @param params
     * @param paramTypes
     * @param elementType
     * @return
     */
    public <T> T queryForObject(String statement, Object[] params, int[] paramTypes, Class<T> elementType);

    /**
     * (rowMapper 에 의하여 매핑되는) 단일 ROW 에 해당하는 객체를 리턴한다. 하나 이상의 ROW 가 존재하는 경우, 리턴
     * 컬럼이 하나 이상인 경우 예외가 발생된다.
     * 
     * @param statement
     * @param params
     * @param paramTypes
     * @param rowMapper
     * @return
     */
    public <T> T queryForObject(String statement, Object[] params, int[] paramTypes, RowMapper<T> rowMapper);

    /**
     * 단일 ROW 에 해당하는 데이터를 Map 에 넣어 리턴한다. 하나 이상의 ROW 가 존재하는 경우 예외가 발생된다.
     * 
     * @param statement
     * @return
     */
    public Map<String, Object> queryForMap(String statement);

    /**
     * 단일 ROW 에 해당하는 데이터를 Map 에 넣어 리턴한다. 하나 이상의 ROW 가 존재하는 경우 예외가 발생된다.
     * 
     * @param statement
     * @return
     */
    public Map<String, Object> queryForMap(String statement, Object[] params, int[] paramTypes);

    /**
     * statement 에 해당하는 결과 데이터를 List<Map<String, Object>> 형식으로 리턴한다.
     * 
     * @param statement
     * @return
     */
    public List<Map<String, Object>> queryForList(String statement);

    public List<Map<String, Object>> queryForList(String statement, Object[] params);

    public List<Map<String, Object>> queryForList(String statement, Object[] params, int[] paramTypes);

    public <T> List<T> queryForList(String statement, Class<T> elementType);

    public <T> List<T> queryForList(String statement, Object[] params, int[] paramTypes, Class<T> elementType);

    public <T> List<T> queryForList(String statement, RowMapper<T> rowMapper);

    public <T> List<T> queryForList(String statement, Object[] params, int[] paramTypes, RowMapper<T> rowMapper);

    public int update(String statement, Object[] values, int[] types);

    public int update(String statement);

    public int[] batchUpdate(String statement, List<Object[]> parameters);

    /** ====================================================== */
    /** NEW STYLE API */
    /** ====================================================== */

    /**
     * 단일 ROW 에 해당하는 elementType 형태의 값을 리턴한다. 하나 이상의 ROW 가 존재하는 경우 오류가 리턴된다.
     * 
     * @param <T>
     * @param statement
     * @param elementType
     * @return
     */
    public <T> T uniqueResult(String statement, Class<T> elementType);

    /**
     * (rowMapper 에 의하여 매핑되는) 단일 ROW 에 해당하는 객체를 리턴한다. 하나 이상의 ROW 가 존재하는 경우 오류가
     * 리턴된다.
     * 
     * @param <T>
     * @param statement
     * @param rowMapper
     * @return
     */
    public <T> T uniqueResult(String statement, RowMapper<T> rowMapper);

    /**
     * 단일 ROW 에 해당하는 elementType 형태의 값을 리턴한다. 하나 이상의 ROW 가 존재하는 경우 오류가 리턴된다.
     * 
     * @param <T>
     * @param statement
     * @param parameter
     * @param elementType
     * @return
     */
    public <T> T uniqueResult(String statement, Object parameter, Class<T> elementType);

    /**
     * (rowMapper 에 의하여 매핑되는) 단일 ROW 에 해당하는 객체를 리턴한다. 하나 이상의 ROW 가 존재하는 경우 오류가
     * 리턴된다.
     * 
     * @param <T>
     * @param statement
     * @param parameter
     * @param rowMapper
     * @return
     */
    public <T> T uniqueResult(String statement, Object parameter, RowMapper<T> rowMapper);

    public <T> List<T> list(String statement, Class<T> elementType);

    public <T> List<T> list(String statement, Class<T> elementType, Object... parameters);

    public <T> List<T> list(String statement, Class<T> elementType, Map<String, Object> parameters);

    public List<Map<String, Object>> list(String statement);

    public List<Map<String, Object>> list(String statement, Object... parameter);

    public List<Map<String, Object>> list(String statement, Map<String, Object> parameters);

    public int update(String statement, Object... parameters);

    public int executeUpdate(String statement);

    public int executeUpdate(String statement, Object parameters);

    /**
     * 스크립트 모드로 쿼리를 실행한다.
     * 
     * @param statement
     * @return
     */
    public Object executeScript(String statement);

    public Object call(String statement, Object... parameters);

}