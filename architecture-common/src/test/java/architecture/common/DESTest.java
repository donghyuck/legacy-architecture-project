package architecture.common;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class DESTest {

	private Log log = LogFactory.getLog(getClass());
	
	public static Key generateKey(String algorithm) throws NoSuchAlgorithmException {

		KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
		SecretKey key = keyGenerator.generateKey();
		return key;

	}

	@Test
	public void testDES() {
		
		Key key = null;
		String enc = null;
		
		try {
			
			// STEP1 : 키 생성
			key = generateKey("DES");
			
			// SETP2 : 암호화 모듈 준비
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
		    byte stringBytes[] = "hello".getBytes();
		    
		    // STEP 3 : 암호화
	        byte raw[] = cipher.doFinal(stringBytes);
	        enc = Hex.encodeHexString(raw);
	        log.debug("ENC:" + enc );
	        
		} catch (Exception e) {
			log.error(e);
		}

		try {
			
			// STEP 4 : 암호화 모듈 준비
			SecretKeySpec skeySpec = new SecretKeySpec(key.getEncoded(), "DES");
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);

			// STEP5 : 복호화
	        byte raw[] = Hex.decodeHex(enc.toCharArray());
	        byte stringBytes[] = cipher.doFinal(raw);
	        
	        log.debug("DEC:" + new String(stringBytes));
			
		} catch (Exception e) {
			log.error(e);
		} 
	}
}
