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
package architecture.ee;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public class SocialTest {

    public SocialTest() {
	// TODO 자동 생성된 생성자 스텁
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

	TextEncryptor enc = Encryptors.noOpText();
	System.out.println("50153444-RgtZ4gNQtUtCHSnPgKuqxvqwO2piFQzET76iGjUtc");
	System.out.println(enc.decrypt("50153444-RgtZ4gNQtUtCHSnPgKuqxvqwO2piFQzET76iGjUtc"));
    }

}
