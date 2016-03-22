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

import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.dao.SaltSource;

import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.util.StringUtils;
import architecture.ee.component.admin.AdminHelper;
import architecture.ee.util.ApplicationConstants;

/**
 * @author donghyuck
 */
public class SaltSourceFactory implements FactoryBean<SaltSource> {

    /**
     * @uml.property name="singleton"
     */
    private boolean singleton = true;

    private SaltSource saltSource = null;

    public SaltSource getObject() throws Exception {

	if (saltSource == null) {

	    ApplicationProperties setupProperties = AdminHelper.getRepository().getSetupApplicationProperties();

	    String userPropertyToUse = setupProperties
		    .get(ApplicationConstants.SECURITY_AUTHENTICATION_ENCODING_SALT_PROP_NAME);

	    if (StringUtils.isEmpty(userPropertyToUse)) {
		userPropertyToUse = "getUsername";
	    }

	    ReflectionSaltSource source = new ReflectionSaltSource();
	    source.setUserPropertyToUse(userPropertyToUse);
	    this.saltSource = source;

	}
	return saltSource;
    }

    public Class<SaltSource> getObjectType() {
	return SaltSource.class;
    }

    /**
     * @return
     * @uml.property name="singleton"
     */
    public boolean isSingleton() {
	return singleton;
    }

}
