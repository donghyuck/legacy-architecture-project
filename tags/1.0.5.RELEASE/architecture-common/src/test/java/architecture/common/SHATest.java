package architecture.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;


public class SHATest {

	@Test
	public void testSHA(){
		
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
		
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
	}
}
