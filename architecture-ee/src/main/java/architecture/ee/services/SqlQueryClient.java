package architecture.ee.services;

import java.util.List;
import java.util.Map;

public interface SqlQueryClient {

    /*
     * public void exportToExcel(String catalogName, String schemaName, String
     * tableName);
     * 
     * public void exportToExcel(String catalogName, String schemaName, String
     * tableName, boolean async);
     * 
     * public void importFromExcel(String catalogName, String schemaName, String
     * tableName, String uri);
     * 
     * public void importFromExcel(String catalogName, String schemaName, String
     * tableName, String uri, boolean async);
     */

    /**
     * 하나 이상의 데이터를 List<Map> 으로 리턴한다.
     * 
     * @param statement
     * @param parameters
     * @return
     */
    public List<Map<String, Object>> list(String statement);

    /**
     * 하나 이상의 데이터를 List<Map> 으로 리턴한다.
     * 
     * @param statement
     * @param startIndex
     *            (0 부터 시작)
     * @param maxResults
     *            쿼리 결과의 startIndex 부터 maxResults 까지의 데이터를 읽어 데이터를 리턴한다. 쿼리 실행후에
     *            maxResults 값은 0 으로 변경된다. 따라서 0 으로 값을 지정하는 것은 페이지 처리를 하지 않는 것과
     *            동일한 결과를 가져온다.
     */
    public List<Map<String, Object>> list(String statement, int startIndex, int maxResults);

    /**
     * 하나 이상의 데이터를 List<Map> 으로 리턴한다.
     * 
     * @param statement
     * @param parameters
     * @return
     */
    public List<Map<String, Object>> list(String statement, Object... parameters);

    /**
     * 단일 데이터를 Map 으로 리턴한다.
     * 
     * @param statement
     * @param parameter
     * @return 데이터가 하나가 아니면 오류가 발생된다.
     */
    public Map<String, Object> uniqueResult(String statement, Object parameter);

    /**
     * 단일 데이터를 Map 으로 리턴한다.
     * 
     * @param statement
     * @return 데이터가 하나가 아니면 오류가 발생된다.
     */
    public Map<String, Object> uniqueResult(String statement);

    public <T> T uniqueResult(String statement, Class<T> elementType, Object... parameters);

    /**
     * 배치 모드로 쿼리를 실행한다. int[] 형태로 결과가 리턴된다.
     * 
     * @param statement
     * @param parameters
     * @return
     */
    public Object batchUpdate(String statement, List<Object[]> parameters);

    /**
     * 
     * @param statement
     * @return
     */
    public Object update(String statement);

    /**
     * 
     * @param statement
     * @param parameters
     * @return
     */
    public Object update(String statement, Object... parameters);

    /**
     * 
     * @param statement
     * @param parameters
     * @return
     */
    public Object update(String statement, Map<String, Object> parameters);

    /**
     * 
     * @param statement
     * @param values
     * @param types
     * @return
     */
    public Object update(String statement, Object[] values, int[] types);

    /**
     * 
     * @param statement
     * @param parameters
     * @return
     */
    public Object call(String statement, Object... parameters);

    /**
     * 인자로 전달된 SqlQueryCallback 인터페이스를 구현하는 객체의 doInSqlQuery 함수를 호출하고 그결과를 리턴한다.
     * 
     * @param action
     * @return
     */
    public <T> T execute(SqlQueryCallback<T> action);

    /**
     * 트랜잭션 지원없이 처리되며 scriptName 에 해당하는 스크립트의 특정 함수를 호출하고 그결과를 리턴한다.
     * 
     * @param scriptName
     * @param methodName
     * @param parameters
     * @return
     */
    public Object unitOfWorkWithoutTransaction(String scriptName, String methodName, Object... parameters);

    /**
     * 단일 트랜잭션으로 처리되며 scriptName 에 해당하는 스크립트의 특정 함수를 호출하고 그결과를 리턴한다.
     * 
     * @param scriptName
     * @param methodName
     * @param parameters
     * @return
     */
    public Object unitOfWork(String scriptName, String methodName, Object... parameters);

}
