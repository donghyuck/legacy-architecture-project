package architecture.security.user.profile.impl;

import architecture.security.model.impl.ProfileFieldValueModelImpl;
import architecture.security.user.profile.ProfileField;
import architecture.security.user.profile.ProfileFieldValue;

public class ProfileFieldValueImpl extends ProfileFieldValueModelImpl implements ProfileFieldValue{

	public ProfileFieldValueImpl(long fieldId, int typeId) {
		super(fieldId, typeId);
	}

	public ProfileFieldValueImpl(ProfileField field) {
		super(field);
	}

}
