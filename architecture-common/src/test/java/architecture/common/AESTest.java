package architecture.common;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AESTest {
    	
    	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static String encString = "" ;
	
	@Test 
	public void testASEEncodeWithKey() throws Exception {
		
		String password = "1234";
		String username = "원문";
		byte[] seedB = password.getBytes();
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(seedB);		
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, sr); // 192 and 256 bits may not be available		
		// Generate the secret key specs.
		SecretKey skey = kgen.generateKey();
		String keyString = Hex.encodeHexString(skey.getEncoded());
		SecretKeySpec skeySpec = new SecretKeySpec(skey.getEncoded(), "AES");			
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
	    byte[] encrypted =  cipher.doFinal(username.getBytes());
	    
	    encString =  Hex.encodeHexString(encrypted);
	    
	    logger.info("encrypted string: [" + keyString + "] " + encString );
	}
	
	@Test 
	public void testASEDecodeWithKey() throws Exception {
		String password = "1234";
		byte[] seedB = password.getBytes();
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(seedB);
		
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, sr); // 192 and 256 bits may not be available
		
		// Generate the secret key specs.
		SecretKey skey = kgen.generateKey();
		String keyString = Hex.encodeHexString(skey.getEncoded());
		SecretKeySpec skeySpec = new SecretKeySpec(skey.getEncoded(), "AES");	
		
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
	    byte[] decrypted =  cipher.doFinal(Hex.decodeHex(encString.toCharArray()));
	    
	    logger.info("decrypted string: [" + keyString + "] " + new String(decrypted));
	}
	
	
	
}
