package architecture.ee.model;

import architecture.ee.user.profile.ProfileFieldOption;

/**
 * @author                 donghyuck
 */
public interface ProfileFieldOptionModel extends ModelObject<ProfileFieldOption> {

    /**
	 * @return
	 * @uml.property  name="fieldId"
	 */
    public long getFieldId();

    /**
	 * @param  fieldID
	 * @uml.property  name="fieldId"
	 */
    public void setFieldId(long fieldID);

    /**
	 * @return
	 * @uml.property  name="value"
	 */
    public String getValue();

    /**
	 * @param  value
	 * @uml.property  name="value"
	 */
    public void setValue(String value);

    /**
	 * @return
	 * @uml.property  name="index"
	 */
    public int getIndex();

    /**
	 * @param  index
	 * @uml.property  name="index"
	 */
    public void setIndex(int index);

    /**
	 * @return
	 * @uml.property  name="defaultOption"
	 */
    public boolean isDefaultOption();

    /**
	 * @param  defaultOption
	 * @uml.property  name="defaultOption"
	 */
    public void setDefaultOption(boolean defaultOption);
    
}
