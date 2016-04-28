/*
 * Copyright 2016 donghyuck
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

package architecture.ee.test;

import org.junit.Test;

public class PasswordTest {

    public PasswordTest() {
	// TODO Auto-generated constructor stub
    }
	
	
	@Test
	public void testPasswordEncoder (){
	    
	org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder encoder = new org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder("SHA-256");
	String enc = encoder.encodePassword("1420", "");
	
		System.out.println(enc);
		
	}
}
