package architecture.common;

import org.junit.Test;

import architecture.common.model.factory.ModelTypeFactory;

public class ModelTypeTest {

	
	@Test
	public void testFindModelType(){
		Integer id = ModelTypeFactory.getTypeIdFromCode("USER");
		try {
			System.out.println( id ) ;
		} catch (Exception e) {
		}
		
		id = ModelTypeFactory.getTypeIdFromCode("COMPANY");
		try {
			System.out.println( id ) ;
		} catch (Exception e) {
		}
	}
}
