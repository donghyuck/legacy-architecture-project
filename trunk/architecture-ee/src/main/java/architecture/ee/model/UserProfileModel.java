package architecture.ee.model;

import architecture.ee.user.profile.UserProfile;

public interface UserProfileModel extends ModelObject<UserProfile> {
	
	public long getFieldId();
	
	public int getFieldType();
	
	public String getValueString();
}
