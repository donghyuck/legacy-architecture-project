package architecture.ee.user.profile;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

public abstract class BeanPropsExternalMapper implements ExternalMapper {

	private final Class profileValueObjectClass;
	private volatile List propertyNames;
	
	public BeanPropsExternalMapper(Class provileValueObjectClass) {
		propertyNames = null;
		profileValueObjectClass = provileValueObjectClass;
	}

	public List getObjectFieldMappingKeys() {
		if (propertyNames == null) {
			propertyNames = new ArrayList();
			PropertyDescriptor descriptors[] = BeanUtils.getPropertyDescriptors(profileValueObjectClass);
			PropertyDescriptor arr$[] = descriptors;
			int len$ = arr$.length;
			for (int i$ = 0; i$ < len$; i$++) {
				PropertyDescriptor descriptor = arr$[i$];
				if (!descriptor.getName().endsWith("String")
						&& !descriptor.getName().equals("class"))
					propertyNames.add(descriptor.getName());
			}

		}
		return propertyNames;
	}

	public boolean isSingleFieldMapper() {
		return false;
	}


}
