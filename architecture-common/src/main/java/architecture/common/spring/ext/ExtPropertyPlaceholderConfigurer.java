package architecture.common.spring.ext;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class ExtPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    protected String convertProperty(String propertyName, String propertyValue) {
	return super.convertProperty(propertyName, propertyValue);
    }

    protected String convertPropertyValue(String originalValue) {
	return super.convertPropertyValue(originalValue);
    }

}
