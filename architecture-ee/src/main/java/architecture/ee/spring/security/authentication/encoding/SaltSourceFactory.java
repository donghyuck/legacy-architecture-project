package architecture.ee.spring.security.authentication.encoding;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.dao.SaltSource;

import architecture.common.lifecycle.ApplicationProperties;
import architecture.ee.admin.AdminHelper;
import architecture.ee.util.ApplicatioinConstants;

public class SaltSourceFactory implements FactoryBean <SaltSource>{

	private boolean singleton = true ;
	
	private SaltSource saltSource = null;
	
	public SaltSource getObject() throws Exception {
		
		if(saltSource == null){
		
			ApplicationProperties setupProperties = AdminHelper.getRepository().getSetupApplicationProperties();
			
			String userPropertyToUse = setupProperties.get(ApplicatioinConstants.SECURITY_AUTHENTICATION_ENCODING_SALT_PROP_NAME);
			
			if(StringUtils.isEmpty(userPropertyToUse))
			{
				userPropertyToUse = "getUsername" ;		
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

	public boolean isSingleton() {
		return singleton;
	}

}
