package tests.service;

import java.util.List;

import architecture.ee.exception.ApplicationException;

public interface DataService {
	
	public List<String> getTablenames() throws ApplicationException ;
	
	public List<String> getTablenames2() throws DataServiceException ;	
		
}
