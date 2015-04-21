package architecture.user.profile;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import architecture.common.user.User;
import architecture.common.user.authentication.UnAuthorizedException;

/**
 * @author                 donghyuck
 */
public interface ProfileManager {

	public abstract Map getProfile(User user) throws UnAuthorizedException;

	public abstract void setProfile(User user, Collection collection)throws UnAuthorizedException;

	public abstract void deleteProfileByUserId(long l)throws UnAuthorizedException;

	public abstract void deleteProfileById(long l) throws UnAuthorizedException;

	/**
	 * @return
	 */
	public abstract boolean isProfileImageEnabled();

	/**
	 * @param flag
	 * @throws UnAuthorizedException
	 */
	public abstract void setProfileImageEnabled(boolean flag)throws UnAuthorizedException;

	//public abstract void setProfileImage(User user, InputStream inputstream) throws AttachmentException, UnauthorizedException;

	public abstract InputStream getProfileImage(User user);

	public abstract void deleteProfileImage(User user) throws UnAuthorizedException;

	public abstract Collection getProfileFieldTermsByFieldId(long l, boolean flag);

	/**
	 * @return
	 */
	public abstract boolean isFullNameEnabled();

	/**
	 * @param flag
	 * @throws UnAuthorizedException
	 */
	public abstract void setFullNameEnabled(boolean flag) throws UnAuthorizedException;

	/**
	 * @return
	 */
	public abstract boolean isPersonalizedHomepageEnabled();

	/**
	 * @param flag
	 * @throws UnAuthorizedException
	 */
	public abstract void setPersonalizedHomepageEnabled(boolean flag) throws UnAuthorizedException;

	/**
	 * @return
	 */
	public abstract boolean isAnonymousBrowsingPeoplePermitted();

	/**
	 * @param flag
	 * @throws UnAuthorizedException
	 */
	public abstract void setAnonymousBrowsingPeoplePermitted(boolean flag) throws UnAuthorizedException;
	
}
