package architecture.ee.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import architecture.ee.exception.ApplicationException;

public class ErrorTest {

	@Test
	public void testApplicaitonException() {
		
		Class requiredType = ApplicationException.class;
		
		try {
			Constructor constructor  = requiredType.getConstructor( new Class[]{ String.class } );			
			Object obj  = constructor.newInstance("hello");
//			/((CodeableException)obj).setErrorCode(1234);
		} catch (SecurityException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
		
	}
	
}
