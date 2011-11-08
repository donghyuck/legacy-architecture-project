package architecture.ee.model;

import java.util.List;

import architecture.ee.user.profile.ProfileFieldValue;

/**
 * @author                 donghyuck
 */
public interface ProfileFieldValueModel  extends ModelObject<ProfileFieldValue> {

	/**
	 * @return
	 * @uml.property  name="fieldId"
	 */
	public long getFieldId();

	/**
	 * @param  fieldId
	 * @uml.property  name="fieldId"
	 */
	public void setFieldId(long fieldId);

	/**
	 * @return
	 * @uml.property  name="fieldTypeId"
	 */
	public int getFieldTypeId();

	/**
	 * @param  fieldTypeId
	 * @uml.property  name="fieldTypeId"
	 */
	public void setFieldTypeId(int fieldTypeId);

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

	public boolean isPrimaryValue();

	//public void setPrimaryValue(boolean isPrimaryValue);

	/**
	 * @return
	 * @uml.property  name="list"
	 */
	public boolean isList();

	/**
	 * @param  isList
	 * @uml.property  name="list"
	 */
	public void setList(boolean isList);

	/**
	 * @return
	 * @uml.property  name="values"
	 */
	public List<String> getValues();

	/**
	 * @param  objValues
	 * @uml.property  name="objectValues"
	 */
	public void setObjectValues(List<Object> objValues);

	/**
	 * @return
	 * @uml.property  name="objectValues"
	 */
	public List<Object> getObjectValues();

	/**
	 * @param  values
	 * @uml.property  name="values"
	 */
	public void setValues(List<String> values);

	public void setValues(List<String> values, int primaryIndex);

	public String getSimpleValue();

	/**
	 * @return
	 * @uml.property  name="objectValue"
	 */
	public Object getObjectValue();

	/**
	 * @param  objValue
	 * @uml.property  name="objectValue"
	 */
	public void setObjectValue(Object objValue);

	/**
	 * @return
	 * @uml.property  name="primaryIndex"
	 */
	public int getPrimaryIndex();
	
	/**
	 * @param  primaryIndex
	 * @uml.property  name="primaryIndex"
	 */
	public void setPrimaryIndex(int primaryIndex);

	public boolean isValueEqual(ProfileFieldValue pfv);
}
