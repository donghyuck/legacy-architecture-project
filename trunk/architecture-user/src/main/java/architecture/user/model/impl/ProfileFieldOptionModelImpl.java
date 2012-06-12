package architecture.user.model.impl;

import java.io.Serializable;

import architecture.common.model.impl.BaseModelObject;
import architecture.ee.model.ModelConstants;
import architecture.user.model.ProfileFieldOptionModel;
import architecture.user.profile.ProfileField;
import architecture.user.profile.ProfileFieldOption;

/**
 * @author  donghyuck
 */
public class ProfileFieldOptionModelImpl  extends BaseModelObject<ProfileFieldOption> implements ProfileFieldOptionModel {

    private long fieldID;
    /**
	 */
    private String value;
    /**
	 */
    private int index;
    /**
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
	 */
    public String getValue()
    {
        return value;
    }

    /**
	 * @param value
	 */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
	 * @return
	 */
    public int getIndex()
    {
        return index;
    }

    /**
	 * @param index
	 */
    public void setIndex(int index)
    {
        this.index = index;
    }

    /**
	 * @return
	 */
    public boolean isDefaultOption()
    {
        return defaultOption;
    }

    /**
	 * @param defaultOption
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

	public int getCachedSize() {
		// TODO Auto-generated method stub
		return 0;
	}



}
