package architecture.ee.model.impl;

import java.io.Serializable;

import architecture.ee.model.ModelConstants;
import architecture.ee.model.ProfileFieldOptionModel;
import architecture.ee.user.profile.ProfileField;
import architecture.ee.user.profile.ProfileFieldOption;

public class ProfileFieldOptionModelImpl  extends BaseModelObject<ProfileFieldOption> implements ProfileFieldOptionModel {

    private long fieldID;
    private String value;
    private int index;
    private boolean defaultOption;
    
    
    public long getFieldId()
    {
        return fieldID;
    }

    public void setFieldId(long fieldID)
    {
        this.fieldID = fieldID;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public boolean isDefaultOption()
    {
        return defaultOption;
    }

    public void setDefaultOption(boolean defaultOption)
    {
        this.defaultOption = defaultOption;
    }

    public int compareTo(ProfileFieldOption o) {
		return 0;
	}

	public Serializable getPrimaryKeyObject() {
		return getFieldId();
	}

	public void setPrimaryKeyObject(Serializable primaryKeyObj) {
		setFieldId(((Long)primaryKeyObj).longValue());
	}

	public int getObjectType() {
		return ModelConstants.PROFILE_FIELD_OPTION;
	}

	public int compareTo(ProfileField o) {
		return 0;
	}

	@Override
	public Object clone() {
		return null;
	}



}
