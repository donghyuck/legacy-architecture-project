package architecture.ee.web.model;

import architecture.ee.model.ModelObject;
import architecture.ee.web.theme.ThemeMap;
import architecture.ee.web.theme.ThemeType;

public interface ThemeMapModel extends ModelObject<ThemeMap> {

	public String getThemeName();
	
	public ThemeType getType();

	public long getNumValue();

	public void setNumValue(long numValue);

	public String getStringValue();

	public void setStringValue(String stringValue);

}
