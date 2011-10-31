package architecture.ee.model.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import architecture.ee.i18n.I18nText;
import architecture.ee.model.ModelConstants;
import architecture.ee.model.ProfileFieldModel;
import architecture.ee.user.profile.ProfileField;
import architecture.ee.user.profile.ProfileFieldOption;
import architecture.ee.user.profile.ProfileFieldUtil;
import architecture.ee.util.I18nTextUtils;


public class ProfileFieldModelImpl extends AbstractModelObject<ProfileField> implements ProfileFieldModel {

	private static final String SINGLE_VALUE_KEY = "v";
    private String displayNameKey;
    private String descriptionKey;
    
	private long fieldId;
	private int fieldTypeId;
	private String name;
	private int index;
	private int registrationIndex;
	private boolean defaultField;
    private boolean visible;
    private boolean editable;
    private boolean required;
    private boolean filterable;
    private boolean searchable;
    private String externalMapping;
    private boolean externallyManaged;
    private boolean listValues;
    private List<ProfileFieldOption> options;
    private List profileFieldText;

    public ProfileFieldModelImpl() {
        profileFieldText = new ArrayList();
        registrationIndex = 0x80000000;
	}

	protected void initKeys()
    {
        displayNameKey = I18nTextUtils.generateResourceBundleKey(500, fieldId, 1);
        descriptionKey = I18nTextUtils.generateResourceBundleKey(500, fieldId, 2);
    }
    
    public List getProfileFieldText()
    {
        return profileFieldText;
    }

    public void setProfileFieldText(List textList)
    {
        profileFieldText = textList;
    }
    
    public void setOptions(List<ProfileFieldOption> options)
    {
        this.options = options;
    }
    
    public List<ProfileFieldOption> getOptions()
    {
        if(options == null)
            options = Collections.emptyList();
        return options;
    }
    
    
	public String getDisplayNameKey() {
		return displayNameKey;
	}


	public void setDisplayNameKey(String displayNameKey) {
		this.displayNameKey = displayNameKey;
	}


	public String getDescriptionKey() {
		return descriptionKey;
	}


	public void setDescriptionKey(String descriptionKey) {
		this.descriptionKey = descriptionKey;
	}


	public long getFieldId() {
		return fieldId;
	}


	public void setFieldId(long fieldId) {
		this.fieldId = fieldId;
		initKeys();
	}


	public int getFieldTypeId() {
		return fieldTypeId;
	}


	public void setFieldTypeId(int fieldTypeId) {
		this.fieldTypeId = fieldTypeId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public int getRegistrationIndex() {
		return registrationIndex;
	}


	public void setRegistrationIndex(int registrationIndex) {
		this.registrationIndex = registrationIndex;
	}


	public boolean isDefaultField() {
		return defaultField;
	}


	public void setDefaultField(boolean defaultField) {
		this.defaultField = defaultField;
	}


	public boolean isVisible() {
		return visible;
	}


	public void setVisible(boolean visible) {
		this.visible = visible;
	}


	public boolean isEditable() {
		return editable;
	}


	public void setEditable(boolean editable) {
		this.editable = editable;
	}


	public boolean isRequired() {
		return required;
	}


	public void setRequired(boolean required) {
		this.required = required;
	}


	public boolean isFilterable() {
		return filterable;
	}


	public void setFilterable(boolean filterable) {
		this.filterable = filterable;
	}


	public boolean isSearchable() {
		return searchable;
	}


	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}


	public String getExternalMappingString() {
		return externalMapping;
	}


	public void setExternalMappingString(String externalMapping) {
		this.externalMapping = externalMapping;
	}


	public Map<String, String> getExternalMapping()
    {
        if(isListValues())
            throw new IllegalStateException("This field is of list type - call getExternalMappings() instead");
        else
            return parseExternalMapping(externalMapping);
    }

    public List<Map<String, String>> getExternalMappings()
    {
        if(!isListValues())
            throw new IllegalStateException("This field is not of list type - call getExternalMapping() instead");
        else
            return parseExternalMappings(externalMapping);
    }

    public void setExternalMapping(Map<String, String> externalMapping)
    {
        if(isListValues())
        {
            throw new IllegalStateException("A list of mappings is required for field of list type");
        } else
        {
            this.externalMapping = flattenExternalMapping(externalMapping);
            return;
        }
    }

    public void setExternalMappings(List<Map<String, String>> externalMappings)
    {
        if(!isListValues())
        {
            throw new IllegalStateException("Only a single mapping is required for fields not of list type");
        } else
        {
            externalMapping = flattenExternalMappings(externalMappings);
            return;
        }
    }

    public void setSingleExternalMapping(String value)
    {
        if(isListValues())
        {
            throw new IllegalStateException("A list of mappings is required for field of list type");
        } else
        {
            Map<String, String> map = new HashMap<String, String>();
            map.put(SINGLE_VALUE_KEY, value);
            setExternalMapping(map);
            return;
        }
    }

    public void setSingleExternalMappings(List<String> values)
    {
        if(!isListValues())
            throw new IllegalStateException("Only a single mapping is required for fields not of list type");
        
        List<Map<String, String>> mappings = new ArrayList<Map<String, String>>();
        for(String value : values){
        	Map<String, String> map = new HashMap<String, String>();
        	 map.put(SINGLE_VALUE_KEY, value);
        }
        setExternalMappings(mappings);
    }

    public String getSingleExternalMapping()
    {
        return getExternalMapping().get(SINGLE_VALUE_KEY);
    }

    public List<String> getSingleExternalMappings()
    {
        List<String> mappings = new ArrayList<String>();
        for( Map<String, String> maps : getExternalMappings()){
        	mappings.add(maps.get(SINGLE_VALUE_KEY));        	
        }
        return mappings;
    }

    public boolean hasExternalMapping()
    {
        return externalMapping != null && !"".equals(externalMapping.trim());
    }

    public boolean hasSingleExternalMapping()
    {
        if(isListValues())
            return getExternalMappings().size() > 0 && (getExternalMappings().get(0)).containsKey("v");
        else
            return getExternalMapping().containsKey("v");
    }

	
	public boolean isExternallyManaged() {
		return externallyManaged;
	}


	public void setExternallyManaged(boolean externallyManaged) {
		this.externallyManaged = externallyManaged;
	}


	public boolean isListValues() {
		return listValues;
	}


	public void setListValues(boolean listValues) {
		this.listValues = listValues;
	}


	public Serializable getPrimaryKeyObject() {
		return getFieldId();
	}

	public void setPrimaryKeyObject(Serializable primaryKeyObj) {
		this.setFieldId(((Long)primaryKeyObj).longValue());		
	}

	public int getObjectType() {
		return ModelConstants.PROFILE_FIELD;
	}

	public int compareTo(ProfileField o) {
		return Integer.valueOf(getIndex()).compareTo(Integer.valueOf(((ProfileField)o).getIndex()));
	}

	@Override
	public Object clone() {
		return null;
	}
	
	
	/**
	 * 
	 * @param mappings
	 * @return
	 */
    private String flattenExternalMappings(List<Map<String, String>> mappings)
    {
        StringBuilder builder = new StringBuilder();
        for( Map<String, String> map : mappings){
        	String config = flattenExternalMapping(map);
            if(config != null)
            {
                builder.append(config);
                builder.append(";");
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    /**
     * 
     * @param mapping
     * @return
     */
    private String flattenExternalMapping(Map<String, String> mapping)
    {
        if(mapping == null || mapping.size() == 0)
            return null;
        StringBuilder builder = new StringBuilder();
        
		for (String attribute : mapping.keySet()) {
			builder.append(attribute);
			builder.append("|");
			builder.append(ProfileFieldUtil.escapeFieldDelimiters((String) mapping.get(attribute)));
			builder.append(",");
		}
        
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    
	
	/**
	 *  ; 구분자로 값들을 리스트로 리턴한다.
	 *  
	 * @param mappings
	 * @return
	 */
	private List<Map<String, String>> parseExternalMappings(String mappings)
    {
		
        String mappingArray[] = mappings.split(";");
        List<Map<String, String>> mappingsList = new ArrayList<Map<String, String>>();
        
        for(String mapping : mappingArray ){
        	Map<String, String> map = parseExternalMapping(mapping);
            if(!map.isEmpty())
                mappingsList.add(map);
        }
        return mappingsList;
    }

	/**
	 * , 을 구분자로 하여 Map 형식으로 리턴한다.
	 * 
	 * @param mappingString
	 * @return
	 */
	private Map<String, String> parseExternalMapping(String mappingString)
    {
        if(mappingString == null || mappingString.equals(""))
            return new HashMap<String, String>();
        
        Map<String, String> map = new HashMap<String, String>();
        String fields[] = mappingString.split(",");
        for(String field : fields){
        	String pair[] = field.split("\\|");
        	if(pair.length != 2)
        		throw new IllegalArgumentException((new StringBuilder()).append("Unable to parse external mapping '").append(field).append("'").toString());
        	 map.put(pair[0], ProfileFieldUtil.unEscapeFieldDelimiters(pair[1]));
        }
        return map;
    }

	public String getDisplayName(String localeCode) {
		I18nText text = I18nTextUtils.getText(profileFieldText, localeCode, 1);
        if(text != null)
            return text.getText();
        else
            return null;
	}

	public void setDisplayName(String localeCode, String displayName) {
		I18nTextUtils.setText(profileFieldText, localeCode, 1, displayName);
	}

	public String getDescription(String localeCode) {
		I18nText text = I18nTextUtils.getText(profileFieldText, localeCode, 2);
        if(text != null)
            return text.getText();
        else
            return null;
	}

	public void setDescription(String localeCode, String description) {
		 I18nTextUtils.setText(profileFieldText, localeCode, 2, description);
	}

}
