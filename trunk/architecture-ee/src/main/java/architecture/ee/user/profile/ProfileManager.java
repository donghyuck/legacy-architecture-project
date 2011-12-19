package architecture.ee.user.profile;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import architecture.ee.security.authentication.UnauthorizedException;
import architecture.ee.user.User;

/**
 * @author                 donghyuck
 */
public interface ProfileManager {

	public abstract Map getProfile(User user) throws UnauthorizedException;

	public abstract void setProfile(User user, Collection collection)throws UnauthorizedException;

	public abstract void deleteProfileByUserId(long l)throws UnauthorizedException;

	public abstract void deleteProfileById(long l) throws UnauthorizedException;

	/**
	 * @return
	 */
	public abstract boolean isProfileImageEnabled();

	/**
	 * @param flag
	 * @throws UnauthorizedException
	 */
	public abstract void setProfileImageEnabled(boolean flag)throws UnauthorizedException;

	//public abstract void setProfileImage(User user, InputStream inputstream) throws AttachmentException, UnauthorizedException;

	public abstract InputStream getProfileImage(User user);

	public abstract void deleteProfileImage(User user) throws UnauthorizedException;

	public abstract Collection getProfileFieldTermsByFieldId(long l, boolean flag);

	/**
	 * @return
	 */
	public abstract boolean isFullNameEnabled();

	/**
	 * @param flag
	 * @throws UnauthorizedException
	 */
	public abstract void setFullNameEnabled(boolean flag) throws UnauthorizedException;

	/**
	 * @return
	 */
	public abstract boolean isPersonalizedHomepageEnabled();

	/**
	 * @param flag
	 * @throws UnauthorizedException
	 */
	public abstract void setPersonalizedHomepageEnabled(boolean flag) throws UnauthorizedException;

	/**
	 * @return
	 */
	public abstract boolean isAnonymousBrowsingPeoplePermitted();

	/**
	 * @param flag
	 * @throws UnauthorizedException
	 */
	public abstract void setAnonymousBrowsingPeoplePermitted(boolean flag) throws UnauthorizedException;
	
}
