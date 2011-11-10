package architecture.ee.spring.security.authentication.encoding;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.dao.SaltSource;

import architecture.common.lifecycle.ApplicationProperties;
import architecture.ee.admin.AdminHelper;

public class SaltSourceFactory implements FactoryBean <SaltSource>{

	private boolean singleton = true ;
	
	private SaltSource saltSource = null;
	
	public SaltSource getObject() throws Exception {
		if(saltSource == null){
			ApplicationProperties setupProperties = AdminHelper.getRepository().getSetupApplicationProperties();
			String userPropertyToUse = setupProperties.get("security.authentication.encoding.salt");
			if(StringUtils.isEmpty(userPropertyToUse))
				userPropertyToUse = "getUsername" ;		
			
			ReflectionSaltSource source = new ReflectionSaltSource();
			source.setUserPropertyToUse(userPropertyToUse);
		}
		return saltSource;
	}

	public Class<SaltSource> getObjectType() {
		return SaltSource.class;
	}

	public boolean isSingleton() {
		return singleton;
	}

}
