package architecture.ee.service;

import org.springframework.jdbc.support.lob.LobHandler;

import architecture.ee.jdbc.schema.Database;
import architecture.ee.jdbc.sqlquery.SqlQuery;
import architecture.ee.spring.jdbc.ExtendedJdbcTemplate;

/**
 * 손쉬운 데이터베이스 작업을 위하여 제공되는 SqlQuery 기반의 클라이언트  
 * 
 * 
 * @author donghyuck son
 *
 */
public interface SqlQueryClient {

	/**
	 * SqlQuery 객체를 리턴한다.
	 * 
	 * @return
	 */
	public SqlQuery getSqlQuery();
	
	/**
	 * 키(statement)에 해당하는 SqlQuery 객체를 리턴한다.
	 * 
	 * @param statement 키는 [카테고리].[쿼리아이디] 형태로 구성된다.
	 * @return
	 */
	public SqlQuery getSqlQuery(String statement);

	/**
	 * Springframework 에서 제공하는 JdbcTemplate 의 기능을 확장한 클래스 ExtendedJdbcTemplate 객체를 리턴한다.
	 * @return
	 */
	public ExtendedJdbcTemplate getExtendedJdbcTemplate();
	
	/**
	 * LOB 데이터를 처리하는 핸들러 객체를 리턴한다.
	 * @return
	 */
	public LobHandler getLobHandler();
	
	/**
	 * 키에해당하는 SQL 문을 리턴한다. 키는 [카테고리].[쿼리아이디] 형태로 구성된다.
	 * 
	 * @param statement 키는 [카테고리].[쿼리아이디] 형태로 구성된다.
	 * @return
	 */
	public String getSql(String statement);
	
	/**
	 * 데이터베이스의 테이블 스키마 정보를 리턴한다.
	 * 
	 * @param catalog 카탈로그 명
	 * @param schemaName 스키마 명
	 * @param tableName 테이블 명
	 * @return
	 */
	public Database getDatabase(String catalog, String schemaName, String tableName);
	
	/**
	 * 지정된 테이블의 데이터를 지정된 경로에 Excel 파일을 생성하여 저장한다.   
	 * 
	 * @param catalog 카탈로그 명
	 * @param schemaName 스키마 명
	 * @param tableName 테이블 명
	 * @param uri Excel 파일 위치
	 */
	public void exportToExcel(String catalog, String schemaName, String tableName, String uri);
		
	/**
	 * 지정된 경로에 Excel 파일을 읽어드려 지정된 테이블에 저장한다. 오직 (INSERT만을 지원한다.)
	 * 
	 * @param catalog 카탈로그 명
	 * @param schemaName 스키마 명
	 * @param tableName 테이블 명
	 * @param uri Excel 파일 위치
	 */
	public void importFromExcel(String catalog, String schemaName, String tableName, String uri);
	
}
