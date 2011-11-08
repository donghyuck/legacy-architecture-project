package architecture.ee.model.impl;

import java.io.Serializable;

import architecture.ee.model.ModelConstants;
import architecture.ee.model.ProfileFieldOptionModel;
import architecture.ee.user.profile.ProfileField;
import architecture.ee.user.profile.ProfileFieldOption;

/**
 * @author  donghyuck
 */
public class ProfileFieldOptionModelImpl  extends BaseModelObject<ProfileFieldOption> implements ProfileFieldOptionModel {

    private long fieldID;
    /**
	 * @uml.property  name="value"
	 */
    private String value;
    /**
	 * @uml.property  name="index"
	 */
    private int index;
    /**
	 * @uml.property  name="defaultOption"
	 */
    private boolean defaultOption;
    
    
    public long getFieldId()
    {
        return fieldID;
    }

    public void setFieldId(long fieldID)
    {
        this.fieldID = fieldID;
    }

    /**
	 * @return
	 * @uml.property  name="value"
	 */
    public String getValue()
    {
        return value;
    }

    /**
	 * @param value
	 * @uml.property  name="value"
	 */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
	 * @return
	 * @uml.property  name="index"
	 */
    public int getIndex()
    {
        return index;
    }

    /**
	 * @param index
	 * @uml.property  name="index"
	 */
    public void setIndex(int index)
    {
        this.index = index;
    }

    /**
	 * @return
	 * @uml.property  name="defaultOption"
	 */
    public boolean isDefaultOption()
    {
        return defaultOption;
    }

    /**
	 * @param defaultOption
	 * @uml.property  name="defaultOption"
	 */
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
