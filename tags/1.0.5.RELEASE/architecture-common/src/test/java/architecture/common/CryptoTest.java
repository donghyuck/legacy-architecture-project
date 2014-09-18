package architecture.common;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class CryptoTest {

	private Log log = LogFactory.getLog(getClass());
	
	@Test
	public void testSEED() throws Exception {

		String password = "inkium";
		byte[] passwordBytes = password.getBytes();
		int len = passwordBytes.length;
		byte[] keyBytes = new byte[16];
		if (len >= 16) {
			System.arraycopy(passwordBytes, 0, keyBytes, 0, 16);
		} else {
			System.arraycopy(passwordBytes, 0, keyBytes, 0, len);
			for (int i = 0; i < (16 - len); i++) {
				keyBytes[len + i] = passwordBytes[i % len];
			}
		}
		Key key = generateKey("AES", keyBytes);
		String transformation = "AES/ECB/PKCS5Padding";
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] plain = password.getBytes();
		byte[] encrypt = cipher.doFinal(plain);
		System.out.println("원문 : " + Hex.encodeHexString(plain));
		System.out.println("암호 : " + Hex.encodeHexString(encrypt));
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decrypt = cipher.doFinal(encrypt);
		System.out.println("복호 : " + Hex.encodeHexString(decrypt));

	}

	public static Key generateKey(String algorithm, byte[] keyData)
			throws NoSuchAlgorithmException, InvalidKeyException,
			InvalidKeySpecException {

		String upper = algorithm.toUpperCase();

		if ("DES".equals(upper)) {
			KeySpec keySpec = new DESKeySpec(keyData);
			SecretKeyFactory secretKeyFactory = SecretKeyFactory
					.getInstance(algorithm);
			SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
			return secretKey;

		} else if ("DESede".equals(upper) || "TripleDES".equals(upper)) {
			KeySpec keySpec = new DESedeKeySpec(keyData);
			SecretKeyFactory secretKeyFactory = SecretKeyFactory
					.getInstance(algorithm);
			SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
			return secretKey;
		} else {
			SecretKeySpec keySpec = new SecretKeySpec(keyData, algorithm);
			return keySpec;
		}

	}

	public static Key generateKey(String algorithm)
			throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
		SecretKey key = keyGenerator.generateKey();
		return key;
	}
	
	@Test
	public void testEncodeDES() {
		String source = "7201161010002";
		String transformation = "DES/ECB/PKCS5Padding";
		InputStream resourceIs = null;
		String keyString = null;
		ObjectInputStream in = null;
		Key key = null;
		try {
			resourceIs = getClass().getClassLoader().getResourceAsStream("SecreteKey.ser");
			in = new ObjectInputStream(resourceIs);
			key = (Key) in.readObject();		
			keyString = Hex.encodeHexString(key.getEncoded());
			in.close();
		} catch (Exception e) {
			log.error(e);
		} finally {
			try {
				if (resourceIs != null)
					resourceIs.close();
				if (in != null)
					in.close();
			} catch (Exception e) {
				log.error(e);
			}
		}
		
		String base64 = null;
		try{			
		    Cipher cipher = Cipher.getInstance(transformation);
		    cipher.init(1, key);
		    byte stringBytes[] = source.getBytes("UTF8");
		    
	        byte raw[] = cipher.doFinal(stringBytes);
	        
	        BASE64Encoder encoder = new BASE64Encoder();
	        base64 = encoder.encode(raw);
	        
	        log.debug("HEX:" + Hex.encodeHexString(raw));
	        log.debug("BASE64:" +base64);
		}
	    catch(Exception e){
	    	log.error(e);
	    }
	    
	    // keyAlg ,transformation=DES/ECB/PKCS5Padding 
	    
	    try {
	    	log.debug("key str:" + keyString);
			SecretKeySpec skeySpec = new SecretKeySpec(Hex.decodeHex(keyString.toCharArray()), "DES");
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			base64 = "KTyhyD/m9OICvoPKTQWPyg==";
			BASE64Decoder decoder = new BASE64Decoder();
			
	        byte raw[] = decoder.decodeBuffer(base64);
	        
	        String s = new String(raw, "UTF8");
	        log.debug("base64 :" + s);
	        byte stringBytes[] = cipher.doFinal(raw);

	        String target = new String(stringBytes);
	        
	        log.debug("decoded:" + target);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}


	//@Test
	public void testGetSecurityKey() {
		InputStream resourceIs = null;
		ObjectInputStream in = null;
		Key key = null;
		try {
			resourceIs = getClass().getClassLoader().getResourceAsStream("SecreteKey.ser");
			in = new ObjectInputStream(resourceIs);
			key = (Key) in.readObject();
			
			String keyString = Hex.encodeHexString(key.getEncoded());
			log.debug("Algorithm:" + key.getAlgorithm());
			log.debug("Key:" + keyString );
			in.close();
		} catch (Exception e) {
			log.error(e);
		} finally {
			try {
				if (resourceIs != null)
					resourceIs.close();
				if (in != null)
					in.close();
			} catch (Exception e) {
				log.error(e);
			}
		}

	}
	
	

}
