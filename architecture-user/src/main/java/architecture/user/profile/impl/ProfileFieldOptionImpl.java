package architecture.user.profile.impl;

import java.io.Serializable;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.user.profile.ProfileField;
import architecture.user.profile.ProfileFieldOption;

public class ProfileFieldOptionImpl   implements ProfileFieldOption{

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

	public int getModelObjectType() {
		return ModelTypeFactory.UNKNOWN.getId();
	}

	public int compareTo(ProfileField o) {
		return 0;
	}


	public int getCachedSize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
