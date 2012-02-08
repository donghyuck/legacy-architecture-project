package architecture.ee.web.theme;

import java.util.Arrays;
import java.util.Comparator;

import architecture.ee.admin.AdminHelper;

public enum ThemeType {
	
	GLOBAL(0), URL(1), LOCALE(2) , VIEW(3);
	
	private int precedence;
		
    private ThemeType(int precedence) {
		this.precedence = precedence;
	}

    public int getPrecedence(){
    	return precedence;
    }
    
    public void setPrecedence(int precedence){
    	this.precedence = precedence;
    	String value = String.valueOf(precedence);
    	String name = (new StringBuilder()).append("theme.map.type.").append(name()).append(".priority").toString();
    	AdminHelper.getConfigService().setLocalProperty(name, value);
    }    
    
	public static ThemeType fromString(String name)
    {
        if(GLOBAL.toString().equals(name))
            return GLOBAL;
        
        for(ThemeType themeType : ThemeType.values()){
            if(themeType.toString().equals(name))
                return themeType;
        }
        return null;
    }
    
    public String toString()
    {
        return name();
    }

    public boolean equals(ThemeType o)
    {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        ThemeType themeType = o;
        return name() == null ? themeType.name() == null : name().equals(themeType.name());
    }

/*    public int hashCode()
    {
        return name() == null ? 0 : name().hashCode();
    }*/
    
    public static ThemeType[] getTypes()
    {    	
        Arrays.sort(ThemeType.values(), new Comparator<ThemeType>() {

            public int compare(ThemeType type1, ThemeType type2)
            {
                return type1.precedence - type2.precedence;
            }
        });
        
        ThemeType copy[] = new ThemeType[ThemeType.values().length];
        System.arraycopy(ThemeType.values(), 0, copy, 0, copy.length);
        return copy;
    }
    
}
