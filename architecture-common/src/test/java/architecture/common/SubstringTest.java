package architecture.common;

import static org.junit.Assert.*;

import org.junit.Test;

public class SubstringTest {

	@Test
	public void test() {
		
		String text = "1234561234567";
		log(text.substring(0, 6));
		log(text.substring(6, 13));
		
	}

	
	public static void log(Object msg){
		System.out.println("> " + msg);
	}
}
