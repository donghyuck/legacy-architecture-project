package architecture.ee.spring.security.authentication.encoding;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import architecture.common.lifecycle.ApplicationProperties;
import architecture.ee.component.admin.AdminHelper;

public class PasswordEncoderFactory implements FactoryBean <PasswordEncoder> {

	public static final String DEFAUTL_ENCODING_ALGORITHM = "SHA-1";
	
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

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}
	
	public boolean isSingleton() {
		return this.singleton;
	}

	
	
}
