package architecture.ee.model;

import java.util.List;

import architecture.ee.user.profile.ProfileFieldValue;

public interface ProfileFieldValueModel  extends ModelObject<ProfileFieldValue> {

	public long getFieldId();

	public void setFieldId(long fieldId);

	public int getFieldTypeId();

	public void setFieldTypeId(int fieldTypeId);

	public String getValue();

	public void setValue(String value);

	public boolean isPrimaryValue();

	//public void setPrimaryValue(boolean isPrimaryValue);

	public boolean isList();

	public void setList(boolean isList);

	public List<String> getValues();

	public void setObjectValues(List<Object> objValues);

	public List<Object> getObjectValues();

	public void setValues(List<String> values);

	public void setValues(List<String> values, int primaryIndex);

	public String getSimpleValue();

	public Object getObjectValue();

	public void setObjectValue(Object objValue);

	public int getPrimaryIndex();
	
	public void setPrimaryIndex(int primaryIndex);

	public boolean isValueEqual(ProfileFieldValue pfv);
}
