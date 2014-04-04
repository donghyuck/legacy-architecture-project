package architecture.common;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;


public class AESTest {
/*
	private static String sKeyString = "" ;
	private static String message= "This is just an example";
	
	@Test
	public void testAESEncode() throws Exception {
				
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);
				
		SecretKey skey = kgen.generateKey();
		
		sKeyString = Hex.encodeHexString(skey.getEncoded());
		
		SecretKeySpec skeySpec = new SecretKeySpec(skey.getEncoded(), "AES");		
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(message.getBytes());
	    
		System.out.println("encrypted string: " + Hex.encodeHexString(encrypted));
	    
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
	    byte[] original =  cipher.doFinal(encrypted);
	    String originalString = new String(original);
	    
	    System.out.println("Original string: " + originalString + " " + Hex.encodeHexString(original));
	}
	
	@Test
	public void testAESDecode() throws Exception {		
		SecretKeySpec skeySpec = new SecretKeySpec(Hex.decodeHex(sKeyString.toCharArray()), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(message.getBytes());
	    System.out.println("encrypted string: " + Hex.encodeHexString(encrypted));
	    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
	    byte[] original =  cipher.doFinal(encrypted);
	    String originalString = new String(original);
	    System.out.println("Original string: " + originalString + " " + Hex.encodeHexString(original));
	    
	}
	*/
	
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
	    
	    System.out.println("encrypted string: [" + keyString + "] " + encString );
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
	    
	    System.out.println("decrypted string: [" + keyString + "] " + new String(decrypted));
	}
	
	
	
}
