package tests.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import tests.service.DataService;
import tests.service.DataServiceException;
import architecture.ee.exception.ApplicationException;
import architecture.ee.spring.jdbc.support.SqlQueryDaoSupport;


@Transactional
public class DataServiceImpl extends SqlQueryDaoSupport implements DataService  {

	public List<String> getTablenames2() throws DataServiceException {
		// 80001 : 테이블 이름 조회 오류 
		try {
			String statement = "ICAP.SELECT_ALL_TABLE_NAMES";			
			return getSqlQuery().list(statement, String.class );
		} catch (Exception e) {
			throw new DataServiceException (80001, e);
		}
	}
	
	public List<String> getTablenames() throws ApplicationException {
				
		// 80001 : 테이블 이름 조회 오류 
		try {
			String statement = "ICAP.SELECT_ALL_TABLE_NAMES";			
			return getSqlQuery().list(statement, String.class );
		} catch (Exception e) {
			throw new ApplicationException (80001, e);
		}		
	}
	
}
