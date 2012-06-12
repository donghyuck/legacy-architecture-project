package architecture.user.profile.impl;

import architecture.user.model.impl.ProfileFieldValueModelImpl;
import architecture.user.profile.ProfileField;
import architecture.user.profile.ProfileFieldValue;

public class ProfileFieldValueImpl extends ProfileFieldValueModelImpl implements ProfileFieldValue{

	public ProfileFieldValueImpl(long fieldId, int typeId) {
		super(fieldId, typeId);
	}

	public ProfileFieldValueImpl(ProfileField field) {
		super(field);
	}

}
