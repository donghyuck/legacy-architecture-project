package architecture.security.user.spring.authentication.encoding;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import architecture.common.lifecycle.ApplicationProperties;
import architecture.ee.component.admin.AdminHelper;

public class PasswordEncoderFactory implements FactoryBean <PasswordEncoder> {

	public static final String DEFAUTL_ENCODING_ALGORITHM = "SHA-256";
	
	private boolean singleton = true ;
	
	private PasswordEncoder passwordEncoder = null;
	
	public PasswordEncoder getObject() throws Exception {
		if( passwordEncoder == null){
			//SHA 1, 256, 384, 512, MD4, MD5	
			ApplicationProperties setupProperties = AdminHelper.getRepository().getSetupApplicationProperties();
			
			String encodingToUse = setupProperties.get("security.authentication.encoding.algorithm");
			if(StringUtils.isEmpty(encodingToUse))
				encodingToUse = DEFAUTL_ENCODING_ALGORITHM ;			
			
			MessageDigestPasswordEncoder encoder = new MessageDigestPasswordEncoder(encodingToUse);
			this.passwordEncoder = encoder;
		}
		
		return passwordEncoder;
	}

	public Class<PasswordEncoder> getObjectType() {
		return PasswordEncoder.class;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}
	
	public boolean isSingleton() {
		return this.singleton;
	}

	
	
}
