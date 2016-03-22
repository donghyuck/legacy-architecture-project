package architecture.user.profile;

import architecture.common.model.ModelObject;

public interface UserProfile extends ModelObject {

    public long getFieldId();

    public int getFieldType();

    public String getValueString();

}
