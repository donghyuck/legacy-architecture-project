package architecture.user.dao;

import java.util.List;

import architecture.user.profile.ProfileField;
import architecture.user.profile.ProfileFieldOption;

public interface ProfileFieldDao {
	
    public abstract ProfileField createProfileField(ProfileField profilefield);

    public abstract ProfileField getProfileField(long fieldId);

    public abstract void editProfileField(ProfileField profilefield);

    public abstract void editProfileFieldOptions(ProfileField profilefield);

    public abstract List<ProfileFieldOption> getProfileFieldOptions(long fieldId);

    public abstract void deleteProfileField(long fieldId);

    public abstract List<ProfileField> getProfileFields();

    public abstract void setIndex(ProfileField profilefield, int index);
    
}
