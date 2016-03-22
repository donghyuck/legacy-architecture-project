package architecture.user.dao;

import java.util.Collection;
import java.util.Map;

import architecture.common.user.User;
import architecture.user.profile.ProfileFieldValue;
import architecture.user.profile.ProfileFieldValueCount;

public interface UserProfileDao {

    public abstract Map<Long, ProfileFieldValue> getProfile(User user);

    public abstract void setProfile(User user, Collection<ProfileFieldValue> collection);

    public abstract void deleteProfileByUserID(long userId);

    public abstract void deleteProfileById(long fieldId);

    public abstract Collection<ProfileFieldValueCount> getProfileFieldTermsByFieldId(long fieldId, boolean flag);

}
