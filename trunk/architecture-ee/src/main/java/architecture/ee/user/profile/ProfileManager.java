package architecture.ee.user.profile;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import architecture.ee.security.UnauthorizedException;
import architecture.ee.user.User;

public interface ProfileManager {

	public abstract Map getProfile(User user) throws UnauthorizedException;

	public abstract void setProfile(User user, Collection collection)throws UnauthorizedException;

	public abstract void deleteProfileByUserId(long l)throws UnauthorizedException;

	public abstract void deleteProfileById(long l) throws UnauthorizedException;

	public abstract boolean isProfileImageEnabled();

	public abstract void setProfileImageEnabled(boolean flag)throws UnauthorizedException;

	//public abstract void setProfileImage(User user, InputStream inputstream) throws AttachmentException, UnauthorizedException;

	public abstract InputStream getProfileImage(User user);

	public abstract void deleteProfileImage(User user) throws UnauthorizedException;

	public abstract Collection getProfileFieldTermsByFieldId(long l, boolean flag);

	public abstract boolean isFullNameEnabled();

	public abstract void setFullNameEnabled(boolean flag) throws UnauthorizedException;

	public abstract boolean isPersonalizedHomepageEnabled();

	public abstract void setPersonalizedHomepageEnabled(boolean flag) throws UnauthorizedException;

	public abstract boolean isAnonymousBrowsingPeoplePermitted();

	public abstract void setAnonymousBrowsingPeoplePermitted(boolean flag) throws UnauthorizedException;
	
}
