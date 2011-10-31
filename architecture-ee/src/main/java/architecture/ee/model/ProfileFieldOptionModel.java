package architecture.ee.model;

import architecture.ee.user.profile.ProfileFieldOption;

public interface ProfileFieldOptionModel extends ModelObject<ProfileFieldOption> {

    public long getFieldId();

    public void setFieldId(long fieldID);

    public String getValue();

    public void setValue(String value);

    public int getIndex();

    public void setIndex(int index);

    public boolean isDefaultOption();

    public void setDefaultOption(boolean defaultOption);
    
}
