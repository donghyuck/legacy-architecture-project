package architecture.ee.spring.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class ExtendedPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	private Log log = LogFactory.getLog(getClass());
	
	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		log.debug(propertyValue + "=" + propertyValue ); 
		return super.convertProperty(propertyName, propertyValue);
	}

	@Override
	protected String convertPropertyValue(String originalValue) {
		log.debug(originalValue);
		return super.convertPropertyValue(originalValue);
	}

}
