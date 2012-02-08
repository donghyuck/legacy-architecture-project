package architecture.ee.web.model.impl;

import java.io.Serializable;

import architecture.ee.model.impl.BaseModelObject;
import architecture.ee.web.model.ThemeMapModel;
import architecture.ee.web.theme.ThemeMap;
import architecture.ee.web.theme.ThemeType;

public class ThemeMapModelImpl extends BaseModelObject<ThemeMap> implements ThemeMapModel, ThemeMap  {

	private String themeName;
	
    private ThemeType type;
    
    private long numValue;
    
    private String stringValue;    
    
    public ThemeMapModelImpl(ThemeType type) {
		this.type = type;
	}

	public int getObjectType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object clone() {
		return null;
	}

	@Override
	public Serializable getPrimaryKeyObject() {
		return null;
	}

	public int compareTo(ThemeMap o) {
		// TODO Auto-generated method stub
		return 0;
	}


    public boolean equals(Object o)
    {
        if(this == o)
            return true;
        
        if(o == null || getClass() != o.getClass())
            return false;
        
        ThemeMap themeMap = (ThemeMap)o;
        if(numValue != themeMap.getNumValue())
            return false;
        if(stringValue == null ? themeMap.getStringValue() != null : !stringValue.equals(themeMap.getStringValue()))
            return false;
        return type == null ? themeMap.getType() == null : type.equals(themeMap.getType());
    }

    public int hashCode()
    {
        int result = type == null ? 0 : type.hashCode();
        result = 31 * result + (int)(numValue ^ numValue >>> 32);
        result = 31 * result + (stringValue == null ? 0 : stringValue.hashCode());
        return result;
    }

    public ThemeType getType()
    {
        return type;
    }

    public long getNumValue()
    {
        return numValue;
    }

    public void setNumValue(long numValue)
    {
        this.numValue = numValue;
    }

    public String getStringValue()
    {
        return stringValue;
    }

    public void setStringValue(String stringValue)
    {
        this.stringValue = stringValue;
    }

	public String getThemeName() {
		return themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}
    
    
	
}
