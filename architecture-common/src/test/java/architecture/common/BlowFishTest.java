/*
 * Copyright 2012, 2013 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.common;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlowFishTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    public BlowFishTest() {
	// TODO 자동 생성된 생성자 스텁
    }

    @Test
    public void testBlowfish() throws Exception {
	String Key = "password";
	byte[] KeyData = Key.getBytes();
	SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
	Cipher cipher = Cipher.getInstance("Blowfish");
	cipher.init(Cipher.ENCRYPT_MODE, KS);

	// encrypt message
	String inputText = "MyTextToEncrypt";
	byte[] encrypted = cipher.doFinal(inputText.getBytes());
	String encryptedString = Hex.encodeHexString(encrypted);
	log.debug("encrypted : " + encryptedString);

	cipher.init(Cipher.DECRYPT_MODE, KS);
	byte[] decrypt = cipher.doFinal(Hex.decodeHex(encryptedString.toCharArray()));
	log.debug("decrypted	: " + new String(decrypt));

    }

}
