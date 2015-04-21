package architecture.user.profile;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

public abstract class BeanPropsExternalMapper implements ExternalMapper {

	private final Class profileValueObjectClass;
	private volatile List<String> propertyNames;
	
	public BeanPropsExternalMapper(Class provileValueObjectClass) {
		propertyNames = null;
		profileValueObjectClass = provileValueObjectClass;
	}

	public List<String> getObjectFieldMappingKeys() {
		if (propertyNames == null) {
			propertyNames = new ArrayList<String>();
			PropertyDescriptor descriptors[] = BeanUtils.getPropertyDescriptors(profileValueObjectClass);
			
			for( PropertyDescriptor descriptor : descriptors){
				if (!descriptor.getName().endsWith("String") && !descriptor.getName().equals("class"))
					propertyNames.add(descriptor.getName());
			}
		}
		return propertyNames;
	}

	public boolean isSingleFieldMapper() {
		return false;
	}


}
