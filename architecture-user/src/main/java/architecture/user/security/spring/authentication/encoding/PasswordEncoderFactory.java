/*
 * Copyright 2012 Donghyuck, Son
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
package architecture.user.security.spring.authentication.encoding;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import architecture.common.lifecycle.ApplicationProperties;
import architecture.ee.component.admin.AdminHelper;

/**
 * @author  donghyuck
 */
public class PasswordEncoderFactory implements FactoryBean <PasswordEncoder> {

	public static final String DEFAUTL_ENCODING_ALGORITHM = "SHA-1";
	
	/**
	 * @uml.property  name="singleton"
	 */
	private boolean singleton = true ;
	
	private PasswordEncoder passwordEncoder = null;
	
	public PasswordEncoder getObject() throws Exception {
		if( passwordEncoder == null){
			//SHA 1, 256, 384, 512, MD4, MD5	
			ApplicationProperties setupProperties = AdminHelper.getRepository().getSetupApplicationProperties();
			String algorithm = setupProperties.get("security.authentication.encoding.algorithm");
			if(StringUtils.isEmpty(algorithm))
				algorithm = DEFAUTL_ENCODING_ALGORITHM ;			
			MessageDigestPasswordEncoder encoder = new MessageDigestPasswordEncoder(algorithm);

			this.passwordEncoder = encoder;
		}
		return passwordEncoder;
	}

	public Class<PasswordEncoder> getObjectType() {
		return PasswordEncoder.class;
	}

	/**
	 * @param singleton
	 * @uml.property  name="singleton"
	 */
	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}
	
	/**
	 * @return
	 * @uml.property  name="singleton"
	 */
	public boolean isSingleton() {
		return this.singleton;
	}

	
	
}
