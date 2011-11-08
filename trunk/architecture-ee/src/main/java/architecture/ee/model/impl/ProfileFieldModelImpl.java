package architecture.ee.model.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import architecture.ee.i18n.I18nText2;
import architecture.ee.model.ModelConstants;
import architecture.ee.model.ProfileFieldModel;
import architecture.ee.user.profile.ProfileField;
import architecture.ee.user.profile.ProfileFieldOption;
import architecture.ee.user.profile.ProfileFieldUtil;
import architecture.ee.util.I18nTextUtils;


/**
 * @author  donghyuck
 */
public class ProfileFieldModelImpl extends BaseModelObject<ProfileField> implements ProfileFieldModel {

	private static final String SINGLE_VALUE_KEY = "v";
    /**
	 * @uml.property  name="displayNameKey"
	 */
    private String displayNameKey;
    /**
	 * @uml.property  name="descriptionKey"
	 */
    private String descriptionKey;
    
	/**
	 * @uml.property  name="fieldId"
	 */
	private long fieldId;
	/**
	 * @uml.property  name="fieldTypeId"
	 */
	private int fieldTypeId;
	/**
	 * @uml.property  name="name"
	 */
	private String name;
	/**
	 * @uml.property  name="index"
	 */
	private int index;
	/**
	 * @uml.property  name="registrationIndex"
	 */
	private int registrationIndex;
	/**
	 * @uml.property  name="defaultField"
	 */
	private boolean defaultField;
    /**
	 * @uml.property  name="visible"
	 */
    private boolean visible;
    /**
	 * @uml.property  name="editable"
	 */
    private boolean editable;
    /**
	 * @uml.property  name="required"
	 */
    private boolean required;
    /**
	 * @uml.property  name="filterable"
	 */
    private boolean filterable;
    /**
	 * @uml.property  name="searchable"
	 */
    private boolean searchable;
    /**
	 * @uml.property  name="externalMapping"
	 */
    private String externalMapping;
    /**
	 * @uml.property  name="externallyManaged"
	 */
    private boolean externallyManaged;
    /**
	 * @uml.property  name="listValues"
	 */
    private boolean listValues;
    /**
	 * @uml.property  name="options"
	 */
    private List<ProfileFieldOption> options;
    /**
	 * @uml.property  name="profileFieldText"
	 */
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
    
    /**
	 * @return
	 * @uml.property  name="profileFieldText"
	 */
    public List getProfileFieldText()
    {
        return profileFieldText;
    }

    /**
	 * @param textList
	 * @uml.property  name="profileFieldText"
	 */
    public void setProfileFieldText(List textList)
    {
        profileFieldText = textList;
    }
    
    /**
	 * @param options
	 * @uml.property  name="options"
	 */
    public void setOptions(List<ProfileFieldOption> options)
    {
        this.options = options;
    }
    
    /**
	 * @return
	 * @uml.property  name="options"
	 */
    public List<ProfileFieldOption> getOptions()
    {
        if(options == null)
            options = Collections.emptyList();
        return options;
    }
    
    
	/**
	 * @return
	 * @uml.property  name="displayNameKey"
	 */
	public String getDisplayNameKey() {
		return displayNameKey;
	}


	/**
	 * @param displayNameKey
	 * @uml.property  name="displayNameKey"
	 */
	public void setDisplayNameKey(String displayNameKey) {
		this.displayNameKey = displayNameKey;
	}


	/**
	 * @return
	 * @uml.property  name="descriptionKey"
	 */
	public String getDescriptionKey() {
		return descriptionKey;
	}


	/**
	 * @param descriptionKey
	 * @uml.property  name="descriptionKey"
	 */
	public void setDescriptionKey(String descriptionKey) {
		this.descriptionKey = descriptionKey;
	}


	/**
	 * @return
	 * @uml.property  name="fieldId"
	 */
	public long getFieldId() {
		return fieldId;
	}


	/**
	 * @param fieldId
	 * @uml.property  name="fieldId"
	 */
	public void setFieldId(long fieldId) {
		this.fieldId = fieldId;
		initKeys();
	}


	/**
	 * @return
	 * @uml.property  name="fieldTypeId"
	 */
	public int getFieldTypeId() {
		return fieldTypeId;
	}


	/**
	 * @param fieldTypeId
	 * @uml.property  name="fieldTypeId"
	 */
	public void setFieldTypeId(int fieldTypeId) {
		this.fieldTypeId = fieldTypeId;
	}


	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name
	 * @uml.property  name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return
	 * @uml.property  name="index"
	 */
	public int getIndex() {
		return index;
	}


	/**
	 * @param index
	 * @uml.property  name="index"
	 */
	public void setIndex(int index) {
		this.index = index;
	}


	/**
	 * @return
	 * @uml.property  name="registrationIndex"
	 */
	public int getRegistrationIndex() {
		return registrationIndex;
	}


	/**
	 * @param registrationIndex
	 * @uml.property  name="registrationIndex"
	 */
	public void setRegistrationIndex(int registrationIndex) {
		this.registrationIndex = registrationIndex;
	}


	/**
	 * @return
	 * @uml.property  name="defaultField"
	 */
	public boolean isDefaultField() {
		return defaultField;
	}


	/**
	 * @param defaultField
	 * @uml.property  name="defaultField"
	 */
	public void setDefaultField(boolean defaultField) {
		this.defaultField = defaultField;
	}


	/**
	 * @return
	 * @uml.property  name="visible"
	 */
	public boolean isVisible() {
		return visible;
	}


	/**
	 * @param visible
	 * @uml.property  name="visible"
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}


	/**
	 * @return
	 * @uml.property  name="editable"
	 */
	public boolean isEditable() {
		return editable;
	}


	/**
	 * @param editable
	 * @uml.property  name="editable"
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}


	/**
	 * @return
	 * @uml.property  name="required"
	 */
	public boolean isRequired() {
		return required;
	}


	/**
	 * @param required
	 * @uml.property  name="required"
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}


	/**
	 * @return
	 * @uml.property  name="filterable"
	 */
	public boolean isFilterable() {
		return filterable;
	}


	/**
	 * @param filterable
	 * @uml.property  name="filterable"
	 */
	public void setFilterable(boolean filterable) {
		this.filterable = filterable;
	}


	/**
	 * @return
	 * @uml.property  name="searchable"
	 */
	public boolean isSearchable() {
		return searchable;
	}


	/**
	 * @param searchable
	 * @uml.property  name="searchable"
	 */
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}


	public String getExternalMappingString() {
		return externalMapping;
	}


	public void setExternalMappingString(String externalMapping) {
		this.externalMapping = externalMapping;
	}


	/**
	 * @return
	 * @uml.property  name="externalMapping"
	 */
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

	
	/**
	 * @return
	 * @uml.property  name="externallyManaged"
	 */
	public boolean isExternallyManaged() {
		return externallyManaged;
	}


	/**
	 * @param externallyManaged
	 * @uml.property  name="externallyManaged"
	 */
	public void setExternallyManaged(boolean externallyManaged) {
		this.externallyManaged = externallyManaged;
	}


	/**
	 * @return
	 * @uml.property  name="listValues"
	 */
	public boolean isListValues() {
		return listValues;
	}


	/**
	 * @param listValues
	 * @uml.property  name="listValues"
	 */
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
		I18nText2 text = I18nTextUtils.getText(profileFieldText, localeCode, 1);
        if(text != null)
            return text.getText();
        else
            return null;
	}

	public void setDisplayName(String localeCode, String displayName) {
		I18nTextUtils.setText(profileFieldText, localeCode, 1, displayName);
	}

	public String getDescription(String localeCode) {
		I18nText2 text = I18nTextUtils.getText(profileFieldText, localeCode, 2);
        if(text != null)
            return text.getText();
        else
            return null;
	}

	public void setDescription(String localeCode, String description) {
		 I18nTextUtils.setText(profileFieldText, localeCode, 2, description);
	}

}
