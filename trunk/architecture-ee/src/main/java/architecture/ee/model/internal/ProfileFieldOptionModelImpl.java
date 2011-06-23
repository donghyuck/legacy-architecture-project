package architecture.ee.model.internal;

import architecture.ee.model.ProfileFieldOptionModel;
import architecture.ee.user.profile.ProfileFieldOption;

public class ProfileFieldOptionModelImpl  extends BaseModelImpl<ProfileFieldOption> implements ProfileFieldOptionModel {

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
    
	public long getPrimaryKey() {
		return getFieldId();
	}

	public String toXmlString() {
		return null;
	}

	public int compareTo(ProfileFieldOption o) {
		return 0;
	}

	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPrimaryKey(long pk) {
		setFieldId(pk);
	}

}
