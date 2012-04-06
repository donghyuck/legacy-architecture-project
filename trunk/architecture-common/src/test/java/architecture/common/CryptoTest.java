package architecture.common;

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
import org.junit.Test;

public class CryptoTest {

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
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
			SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
			return secretKey;

		} else if ("DESede".equals(upper) || "TripleDES".equals(upper)) {
			KeySpec keySpec = new DESedeKeySpec(keyData);
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
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

}
