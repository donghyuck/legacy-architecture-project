package architecture.user.profile;

import java.util.List;
import java.util.Map;

import architecture.common.model.ModelObject;


public interface ProfileField extends ModelObject {
	

	/**
	 * @author donghyuck
	 */
	enum Type {
		
		 /**
		 */
		BOOLEAN( 1, "boolean", "Boolean", "Example: Yes/No or True/False", false, true, new BooleanConverter()),
         /**
		 */
        DATETIME( 2, "datetime", "Date/Time", "Example: Jan 17, 1978 or 9:17pm.", true, true, new DateConverter()),
         /**
		 */
        DECIMAL( 3, "decimal", "Decimal", "Example: 183.032, 0.993 or 32395.9381.", true, true, new DecimalConverter()),
         /**
		 */
        EMAIL( 4, "email", "Email", "Example: alice@foo.com or bob@bar.org", true, true, new EmailConverter()),
         /**
		 */
        NUMBER( 5, "number", "Number", "Example: 183, 35 or 32,395.", true, true, new IntegerConverter()),
         /**
		 */
        LARGETEXT( 6, "largetext", "Large Text Field", "A paragraph or two of text, up to 4000 characters.", false, true, new StringConverter()),
         /**
		 */
        MULTILIST( 7, "multilist", "Multiple Select List", "A list of options where multiple options can be selected.", false, false, new StringConverter()),
         /**
		 */
        SINGLELIST( 8, "singlelist", "Single Select List", "A list of options where only one option can be selected.", false, false, new StringConverter()),
         /**
		 */
        TEXT( 9, "text", "Text Field", "One or two sentences of text.", true, true, new StringConverter()),
         /**
		 */
        URL( 10, "url", "URL", "Example: cnn.com or http://www.yoursite.com/", true, true, new URLConverter()),
         /**
		 */
        PHONE_NUMBER( 11, "phone", "Phone Number", "Example: (542) 222-1111", true, true, new PhoneNumberConverter()),
         /**
		 */
        ZIP_CODE( 12, "zip", "Zip Code", "Example: 98012-2222 or 98012", false, true, new ZipCodeConverter()),
         /**
		 */
        ADDRESS( 14, "address", "Address", "A complete address with street, country, postal code", false, true, new AddressConverter());
         
		
        private int ID;
        /**
		 */
        private String name;
        /**
		 */
        private String displayName;
        /**
		 */
        private String description;
        /**
		 */
        private TypeConverter converter;
        /**
		 */
        private boolean supportsList;
        /**
		 */
        private boolean supportsMapping;
        
        private Type(int ID, String name, String displayName, String description, boolean supportsList, 
                boolean supportsMapping, TypeConverter converter)
        {
            if(ID < 1)
            {
                throw new IllegalArgumentException("ID must be > 0");
            } else
            {
                this.ID = ID;
                this.name = name;
                this.displayName = displayName;
                this.description = description;
                this.supportsList = supportsList;
                this.supportsMapping = supportsMapping;
                this.converter = converter;
                return;
            }
        }
        
        public int getId()
        {
            return ID;
        }

        /**
		 * @return
		 */
        public String getName()
        {
            return name;
        }

        /**
		 * @return
		 */
        public String getDisplayName()
        {
            return displayName;
        }

        /**
		 * @return
		 */
        public String getDescription()
        {
            return description;
        }

        /**
		 * @return
		 */
        public boolean isSupportsMapping()
        {
            return supportsMapping;
        }

        /**
		 * @return
		 */
        public boolean isSupportsList()
        {
            return supportsList;
        }

        /**
		 * @return
		 */
        public TypeConverter getConverter()
        {
            return converter;
        }

        public static Type valueOf(int ID)
        {
            if(BOOLEAN.ID == ID)
                return BOOLEAN;
            if(DATETIME.ID == ID)
                return DATETIME;
            if(DECIMAL.ID == ID)
                return DECIMAL;
            if(EMAIL.ID == ID)
                return EMAIL;
            if(NUMBER.ID == ID)
                return NUMBER;
            if(LARGETEXT.ID == ID)
                return LARGETEXT;
            if(MULTILIST.ID == ID)
                return MULTILIST;
            if(SINGLELIST.ID == ID)
                return SINGLELIST;
            if(TEXT.ID == ID)
                return TEXT;
            if(URL.ID == ID)
                return URL;
            if(PHONE_NUMBER.ID == ID)
                return PHONE_NUMBER;
            if(ZIP_CODE.ID == ID)
                return ZIP_CODE;
            if(ADDRESS.ID == ID)
                return ADDRESS;
            else
                return null;
        }
	}	
	
	
	/**
	 * @return
	 */
	public String getDisplayNameKey();

	/**
	 * @param  displayNameKey
	 */
	public void setDisplayNameKey(String displayNameKey);

	/**
	 * @return
	 */
	public String getDescriptionKey();

	/**
	 * @param  descriptionKey
	 */
	public void setDescriptionKey(String descriptionKey);

	public String getDisplayName(String localeCode);

	public void setDisplayName(String localeCode, String displayName);

	public String getDescription(String localeCode);

	public void setDescription(String localeCode, String description);

	/**
	 * @return
	 */
	public long getFieldId();

	/**
	 * @param  fieldId
	 */
	public void setFieldId(long fieldId);

	/**
	 * @return
	 */
	public int getFieldTypeId();

	/**
	 * @param  fieldTypeId
	 */
	public void setFieldTypeId(int fieldTypeId);

	/**
	 * @return
	 */
	public String getName();

	/**
	 * @param  name
	 */
	public void setName(String name);

	/**
	 * @return
	 */
	public int getIndex();

	/**
	 * @param  index
	 */
	public void setIndex(int index);

	/**
	 * @return
	 */
	public int getRegistrationIndex();

	/**
	 * @param  registrationIndex
	 */
	public void setRegistrationIndex(int registrationIndex);

	/**
	 * @return
	 */
	public boolean isDefaultField();

	/**
	 * @param  defaultField
	 */
	public void setDefaultField(boolean defaultField);

	/**
	 * @return
	 */
	public boolean isVisible();

	/**
	 * @param  visible
	 */
	public void setVisible(boolean visible);

	/**
	 * @return
	 */
	public boolean isEditable();

	/**
	 * @param  editable
	 */
	public void setEditable(boolean editable);

	/**
	 * @return
	 */
	public boolean isRequired();

	/**
	 * @param  required
	 */
	public void setRequired(boolean required);

	/**
	 * @return
	 */
	public boolean isFilterable();

	/**
	 * @param  filterable
	 */
	public void setFilterable(boolean filterable);

	/**
	 * @return
	 */
	public boolean isSearchable();

	/**
	 * @param  searchable
	 */
	public void setSearchable(boolean searchable);

	/**
	 * @return
	 */
	public String getExternalMappingString();

	/**
	 * @param  externalMapping
	 */
	public void setExternalMappingString(String externalMapping);

	/**
	 * @return
	 */
	public Map<String, String> getExternalMapping();

	/**
	 * @return
	 */
	public List<Map<String, String>> getExternalMappings();

	/**
	 * @param  externalMapping
	 */
	public void setExternalMapping(Map<String, String> externalMapping);

	/**
	 * @param  externalMappings
	 */
	public void setExternalMappings(List<Map<String, String>> externalMappings);

	/**
	 * @param  value
	 */
	public void setSingleExternalMapping(String value);

	/**
	 * @param  values
	 */
	public void setSingleExternalMappings(List<String> values);

	/**
	 * @return
	 */
	public String getSingleExternalMapping();

	/**
	 * @return
	 */
	public List<String> getSingleExternalMappings();

	public boolean hasExternalMapping();

	public boolean hasSingleExternalMapping();

	/**
	 * @return
	 */
	public boolean isExternallyManaged();

	/**
	 * @param  externallyManaged
	 */
	public void setExternallyManaged(boolean externallyManaged);

	/**
	 * @return
	 */
	public boolean isListValues();

	/**
	 * @param  listValues
	 */
	public void setListValues(boolean listValues);

	/**
	 * @return
	 */
	public List<ProfileFieldOption> getOptions();

	/**
	 * @param  options
	 */
	public void setOptions(List<ProfileFieldOption> options);

	/**
	 * @return
	 */
	public List getProfileFieldText();

	/**
	 * @param  textList
	 */
	public void setProfileFieldText(List textList);
	
}
