package architecture.security.model;

import architecture.common.model.ModelObject;
import architecture.security.user.profile.UserProfile;

public interface UserProfileModel extends ModelObject<UserProfile> {
	
	public long getFieldId();
	
	public int getFieldType();
	
	public String getValueString();
}
