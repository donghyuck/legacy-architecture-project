package architecture.security.user.model;

import architecture.common.model.ModelObject;
import architecture.user.profile.UserProfile;

public interface UserProfileModel extends ModelObject<UserProfile> {
	
	public long getFieldId();
	
	public int getFieldType();
	
	public String getValueString();
}
