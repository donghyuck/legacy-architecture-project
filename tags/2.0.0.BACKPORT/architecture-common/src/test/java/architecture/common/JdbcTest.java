package architecture.common;

import org.junit.Test;

import architecture.common.jdbc.JdbcType;


public class JdbcTest {

	@Test
	public void testJdbcType(){

		System.out.println(
			JdbcType.valueOf("date".toUpperCase())
		);
		
	}
}
