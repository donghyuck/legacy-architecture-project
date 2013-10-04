package architecture.user.profile;

import architecture.common.model.ModelObject;

public interface ProfileFieldOption extends ModelObject {

	/**
	 * @return
	 */
	public long getFieldId();

	/**
	 * @param fieldID
	 */
	public void setFieldId(long fieldID);

	/**
	 * @return
	 */
	public String getValue();

	/**
	 * @param value
	 */
	public void setValue(String value);

	/**
	 * @return
	 */
	public int getIndex();

	/**
	 * @param index
	 */
	public void setIndex(int index);

	/**
	 * @return
	 */
	public boolean isDefaultOption();

	/**
	 * @param defaultOption
	 */
	public void setDefaultOption(boolean defaultOption);
}
