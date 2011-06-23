package architecture.ee.model;

import java.util.List;
import java.util.Map;

import architecture.ee.user.profile.ProfileField;
import architecture.ee.user.profile.ProfileFieldOption;

public interface ProfileFieldModel extends BaseModel<ProfileField> {
		
	public String getDisplayNameKey();

	public void setDisplayNameKey(String displayNameKey);

	public String getDescriptionKey() ;

	public void setDescriptionKey(String descriptionKey);

	public String getDisplayName(String localeCode);
	
	public void setDisplayName(String localeCode, String displayName);
	
	public String getDescription(String localeCode);
	
	public void setDescription(String localeCode, String description);
	
	public long getFieldId();

	public void setFieldId(long fieldId);

	public int getFieldTypeId();

	public void setFieldTypeId(int fieldTypeId);

	public String getName();

	public void setName(String name);

	public int getIndex() ;

	public void setIndex(int index) ;

	public int getRegistrationIndex();

	public void setRegistrationIndex(int registrationIndex);

	public boolean isDefaultField() ;

	public void setDefaultField(boolean defaultField);

	public boolean isVisible();

	public void setVisible(boolean visible);

	public boolean isEditable();

	public void setEditable(boolean editable);

	public boolean isRequired();

	public void setRequired(boolean required);

	public boolean isFilterable();

	public void setFilterable(boolean filterable) ;

	public boolean isSearchable();

	public void setSearchable(boolean searchable);

	public String getExternalMappingString() ;

	public void setExternalMappingString(String externalMapping) ;

	public Map<String, String> getExternalMapping();

    public List<Map<String, String>> getExternalMappings();

    public void setExternalMapping(Map<String, String> externalMapping);

    public void setExternalMappings(List<Map<String, String>> externalMappings);

    public void setSingleExternalMapping(String value);

    public void setSingleExternalMappings(List<String> values);

    public String getSingleExternalMapping();

    public List<String> getSingleExternalMappings();

    public boolean hasExternalMapping();

    public boolean hasSingleExternalMapping();
    
	public boolean isExternallyManaged() ;
	
	public void setExternallyManaged(boolean externallyManaged);

	public boolean isListValues();

	public void setListValues(boolean listValues) ;	
	
	public List<ProfileFieldOption> getOptions();
	
	public void setOptions(List<ProfileFieldOption> options);
	
	 public List getProfileFieldText();
	 
	 public void setProfileFieldText(List textList); 
}
