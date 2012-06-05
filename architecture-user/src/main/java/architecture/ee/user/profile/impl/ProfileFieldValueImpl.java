package architecture.ee.user.profile.impl;

import architecture.ee.model.impl.ProfileFieldValueModelImpl;
import architecture.ee.user.profile.ProfileField;
import architecture.ee.user.profile.ProfileFieldValue;

public class ProfileFieldValueImpl extends ProfileFieldValueModelImpl implements ProfileFieldValue{

	public ProfileFieldValueImpl(long fieldId, int typeId) {
		super(fieldId, typeId);
	}

	public ProfileFieldValueImpl(ProfileField field) {
		super(field);
	}

}
