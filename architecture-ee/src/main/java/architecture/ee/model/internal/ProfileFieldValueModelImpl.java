package architecture.ee.model.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.ee.user.profile.ProfileField;
import architecture.ee.user.profile.ProfileFieldValue;
import architecture.ee.user.profile.TypeConverter;
import architecture.ee.user.profile.TypeConverter.ConversionException;

public class ProfileFieldValueModelImpl extends BaseModelImpl<ProfileFieldValue> implements ProfileFieldValue {

	private Log log = LogFactory.getLog(getClass());
	
	private int primaryIndex;
	
	private long fieldId;
	
	private int fieldType;
	
	private String value;
	
	//private boolean isPrimaryValue;
	
	private boolean isList;
	
	private List<String> values;
	
	private ProfileFieldValueModelImpl() {
		primaryIndex = -1;
	}

    public ProfileFieldValueModelImpl(long fieldId, int typeId)
    {
        primaryIndex = -1;
        setFieldId(fieldId);
        setFieldTypeId(typeId);
    }

    public ProfileFieldValueModelImpl(ProfileField field)
    {
        this(field.getFieldId(), field.getFieldTypeId());
    }
    
	public long getFieldId() {
		return fieldId;
	}

	public void setFieldId(long fieldId) {
		if(fieldId < 1L)
        {
            throw new IllegalArgumentException("Field ID must be > 1");
        } else
        {
            this.fieldId = fieldId;
            return;
        }
	}

	public int getFieldTypeId() {
		return fieldType;
	}

	public void setFieldTypeId(int fieldType) {
        if(fieldType < 1)
        {
            throw new IllegalArgumentException("Type ID must be > 1");
        } else
        {
            this.fieldType = fieldType;
            return;
        }
	}

	public List<String> getValues() {		
		return values;
	}
	
	public void setObjectValues(List<Object> objValues) {
		setObjectValues(objValues, -1);
	}

    public void setObjectValues(List<Object> objValues, int primaryIndex)
    {
        if(value != null)
            throw new IllegalStateException("UserProfile has already been assigned a single value and can not be assigned a multilist of values as well.");
        assertTypeConfigured();
        
        List<String> stringVals = new ArrayList<String>(objValues.size());
        for( Object obj : objValues ){
        	try {
				
        		String val = getTypeConverter().convertToString(obj);
                if(val != null)
                    stringVals.add(val);
                
			} catch (ConversionException e) {
				throw new IllegalArgumentException(e);
			}
        }
        values = stringVals;
        this.primaryIndex = primaryIndex;
    }

    
    public List<Object> getObjectValues()
    {
        assertTypeConfigured();
        if(values != null)
        {
            List<Object> objValues = new ArrayList<Object>(values.size());
            for(String string : values ){
            	try
                {
                    objValues.add(getTypeConverter().convertFromString(string));
                }
                catch(TypeConverter.ConversionException e)
                {
                    log.error(e);
                }
            }
            return objValues;
        } else
        {
            return null;
        }
    }

    public void setValues(List<String> values)
    {
        setValues(values, -1);
    }

    public void setValues(List<String> values, int primaryIndex)
    {
        if(value != null)
        {
            throw new IllegalStateException("UserProfile has already been assigned a single value and can not be assigned a multilist of values as well.");
        } else
        {
            this.values = values;
            this.primaryIndex = primaryIndex;
            return;
        }
    }
	
	
	public String getValue() {
		return value;
	}
	
	public String getSimpleValue()
    {
        if(value == null)
            return "";
        else
            return value.split("\\|")[0];
    }

	public void setValue(String value)
    {
        if(values != null)
        {
            throw new IllegalStateException("UserProfile has already been assigned a multilist and can not be assigned a single value as well.");
        } else
        {
            this.value = value;
            return;
        }
    }


    public Object getObjectValue()
    {
        Object objVal = null;
        if(value != null)
            try
            {
                objVal = getTypeConverter().convertFromString(value);
            }
            catch(TypeConverter.ConversionException e)
            {
                log.error(e);
            }
        return objVal;
    }

    public void setObjectValue(Object objValue)
    {
        if(values != null)
            throw new IllegalStateException("UserProfile has already been assigned a multilist and can not be assigned a single value as well.");
        try
        {
            value = getTypeConverter().convertToString(objValue);
        }
        catch(TypeConverter.ConversionException e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    public int getPrimaryIndex()
    {
        return primaryIndex;
    }
    
    public void setPrimaryIndex(int primaryIndex){
    	this.primaryIndex = primaryIndex;
    }
    
	public boolean isPrimaryValue() {
		return primaryIndex == 1;
	}

	/**
	public void setPrimaryValue(boolean isPrimaryValue) {
		this.isPrimaryValue = isPrimaryValue;
	}*/

	public boolean isList() {
		return isList;
	}

	public void setList(boolean isList) {
		this.isList = isList;
	}
	
	
	private TypeConverter getTypeConverter()
    {
        TypeConverter converter = null;
        ProfileField.Type type = ProfileField.Type.valueOf(this.fieldType);
        if(type != null)
            converter = type.getConverter();
        return converter;
    }
	
    private void assertTypeConfigured()
    {
        if(getTypeConverter() == null)
            throw new IllegalStateException((new StringBuilder()).append("No valid type converter available for type id ").append(this.getFieldTypeId()).toString());
        else
            return;
    }
    
    public boolean isValueEqual(ProfileFieldValue pfv)
    {  	
    	
        if(pfv == null)
            return false;
        
        ProfileFieldValueModelImpl useTo = (ProfileFieldValueModelImpl)pfv;
        
        if(useTo.value == null && value != null)
            return false;
        if(value == null && useTo.value != null)
            return false;
        if(value != null && useTo.value != null && !value.equals(useTo.value))
            return false;
        if(values == null && useTo.values != null)
            return false;
        if(values != null && useTo.values == null)
            return false;
        return values == null || useTo.values == null || values.containsAll(useTo.values) && useTo.values.containsAll(values);
    }
    

	public long getPrimaryKey() {
		return -1L;
	}

	public String toXmlString() {
		return null;
	}

	public int compareTo(ProfileFieldValue o) {
		return 0;
	}

	@Override
	public Object clone() {
		return null;
	}

	@Override
	public void setPrimaryKey(long pk) {		
	}
	
}
