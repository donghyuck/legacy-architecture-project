package architecture.user.profile;

import java.util.List;

import architecture.common.model.ModelObject;

public interface ProfileFieldValue extends ModelObject {

    /**
     * @return
     */
    public long getFieldId();

    /**
     * @param fieldId
     */
    public void setFieldId(long fieldId);

    /**
     * @return
     */
    public int getFieldTypeId();

    /**
     * @param fieldTypeId
     */
    public void setFieldTypeId(int fieldTypeId);

    /**
     * @return
     */
    public String getValue();

    /**
     * @param value
     */
    public void setValue(String value);

    public boolean isPrimaryValue();

    // public void setPrimaryValue(boolean isPrimaryValue);

    /**
     * @return
     */
    public boolean isList();

    /**
     * @param isList
     */
    public void setList(boolean isList);

    /**
     * @return
     */
    public List<String> getValues();

    /**
     * @param objValues
     */
    public void setObjectValues(List<Object> objValues);

    /**
     * @return
     */
    public List<Object> getObjectValues();

    /**
     * @param values
     */
    public void setValues(List<String> values);

    public void setValues(List<String> values, int primaryIndex);

    public String getSimpleValue();

    /**
     * @return
     */
    public Object getObjectValue();

    /**
     * @param objValue
     */
    public void setObjectValue(Object objValue);

    /**
     * @return
     */
    public int getPrimaryIndex();

    /**
     * @param primaryIndex
     */
    public void setPrimaryIndex(int primaryIndex);

    public boolean isValueEqual(ProfileFieldValue pfv);

}
