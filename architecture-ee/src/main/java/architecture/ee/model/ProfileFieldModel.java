package architecture.ee.model;

import java.util.List;
import java.util.Map;

import architecture.ee.user.profile.AddressConverter;
import architecture.ee.user.profile.BooleanConverter;
import architecture.ee.user.profile.DateConverter;
import architecture.ee.user.profile.DecimalConverter;
import architecture.ee.user.profile.EmailConverter;
import architecture.ee.user.profile.IntegerConverter;
import architecture.ee.user.profile.PhoneNumberConverter;
import architecture.ee.user.profile.ProfileField;
import architecture.ee.user.profile.ProfileFieldOption;
import architecture.ee.user.profile.StringConverter;
import architecture.ee.user.profile.TypeConverter;
import architecture.ee.user.profile.URLConverter;
import architecture.ee.user.profile.ZipCodeConverter;

/**
 * @author                 donghyuck
 */
public interface ProfileFieldModel extends ModelObject<ProfileField> {


	/**
	 * @author                 donghyuck
	 */
	enum Type {
		
		 /**
		 * @uml.property  name="bOOLEAN"
		 * @uml.associationEnd  
		 */
		BOOLEAN( 1, "boolean", "Boolean", "Example: Yes/No or True/False", false, true, new BooleanConverter()),
         /**
		 * @uml.property  name="dATETIME"
		 * @uml.associationEnd  
		 */
        DATETIME( 2, "datetime", "Date/Time", "Example: Jan 17, 1978 or 9:17pm.", true, true, new DateConverter()),
         /**
		 * @uml.property  name="dECIMAL"
		 * @uml.associationEnd  
		 */
        DECIMAL( 3, "decimal", "Decimal", "Example: 183.032, 0.993 or 32395.9381.", true, true, new DecimalConverter()),
         /**
		 * @uml.property  name="eMAIL"
		 * @uml.associationEnd  
		 */
        EMAIL( 4, "email", "Email", "Example: alice@foo.com or bob@bar.org", true, true, new EmailConverter()),
         /**
		 * @uml.property  name="nUMBER"
		 * @uml.associationEnd  
		 */
        NUMBER( 5, "number", "Number", "Example: 183, 35 or 32,395.", true, true, new IntegerConverter()),
         /**
		 * @uml.property  name="lARGETEXT"
		 * @uml.associationEnd  
		 */
        LARGETEXT( 6, "largetext", "Large Text Field", "A paragraph or two of text, up to 4000 characters.", false, true, new StringConverter()),
         /**
		 * @uml.property  name="mULTILIST"
		 * @uml.associationEnd  
		 */
        MULTILIST( 7, "multilist", "Multiple Select List", "A list of options where multiple options can be selected.", false, false, new StringConverter()),
         /**
		 * @uml.property  name="sINGLELIST"
		 * @uml.associationEnd  
		 */
        SINGLELIST( 8, "singlelist", "Single Select List", "A list of options where only one option can be selected.", false, false, new StringConverter()),
         /**
		 * @uml.property  name="tEXT"
		 * @uml.associationEnd  
		 */
        TEXT( 9, "text", "Text Field", "One or two sentences of text.", true, true, new StringConverter()),
         /**
		 * @uml.property  name="uRL"
		 * @uml.associationEnd  
		 */
        URL( 10, "url", "URL", "Example: cnn.com or http://www.yoursite.com/", true, true, new URLConverter()),
         /**
		 * @uml.property  name="pHONE_NUMBER"
		 * @uml.associationEnd  
		 */
        PHONE_NUMBER( 11, "phone", "Phone Number", "Example: (542) 222-1111", true, true, new PhoneNumberConverter()),
         /**
		 * @uml.property  name="zIP_CODE"
		 * @uml.associationEnd  
		 */
        ZIP_CODE( 12, "zip", "Zip Code", "Example: 98012-2222 or 98012", false, true, new ZipCodeConverter()),
         /**
		 * @uml.property  name="aDDRESS"
		 * @uml.associationEnd  
		 */
        ADDRESS( 14, "address", "Address", "A complete address with street, country, postal code", false, true, new AddressConverter());
         
		
        private int ID;
        /**
		 * @uml.property  name="name"
		 */
        private String name;
        /**
		 * @uml.property  name="displayName"
		 */
        private String displayName;
        /**
		 * @uml.property  name="description"
		 */
        private String description;
        /**
		 * @uml.property  name="converter"
		 * @uml.associationEnd  
		 */
        private TypeConverter converter;
        /**
		 * @uml.property  name="supportsList"
		 */
        private boolean supportsList;
        /**
		 * @uml.property  name="supportsMapping"
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
		 * @uml.property  name="name"
		 */
        public String getName()
        {
            return name;
        }

        /**
		 * @return
		 * @uml.property  name="displayName"
		 */
        public String getDisplayName()
        {
            return displayName;
        }

        /**
		 * @return
		 * @uml.property  name="description"
		 */
        public String getDescription()
        {
            return description;
        }

        /**
		 * @return
		 * @uml.property  name="supportsMapping"
		 */
        public boolean isSupportsMapping()
        {
            return supportsMapping;
        }

        /**
		 * @return
		 * @uml.property  name="supportsList"
		 */
        public boolean isSupportsList()
        {
            return supportsList;
        }

        /**
		 * @return
		 * @uml.property  name="converter"
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
	 * @uml.property  name="displayNameKey"
	 */
	public String getDisplayNameKey();

	/**
	 * @param  displayNameKey
	 * @uml.property  name="displayNameKey"
	 */
	public void setDisplayNameKey(String displayNameKey);

	/**
	 * @return
	 * @uml.property  name="descriptionKey"
	 */
	public String getDescriptionKey();

	/**
	 * @param  descriptionKey
	 * @uml.property  name="descriptionKey"
	 */
	public void setDescriptionKey(String descriptionKey);

	public String getDisplayName(String localeCode);

	public void setDisplayName(String localeCode, String displayName);

	public String getDescription(String localeCode);

	public void setDescription(String localeCode, String description);

	/**
	 * @return
	 * @uml.property  name="fieldId"
	 */
	public long getFieldId();

	/**
	 * @param  fieldId
	 * @uml.property  name="fieldId"
	 */
	public void setFieldId(long fieldId);

	/**
	 * @return
	 * @uml.property  name="fieldTypeId"
	 */
	public int getFieldTypeId();

	/**
	 * @param  fieldTypeId
	 * @uml.property  name="fieldTypeId"
	 */
	public void setFieldTypeId(int fieldTypeId);

	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName();

	/**
	 * @param  name
	 * @uml.property  name="name"
	 */
	public void setName(String name);

	/**
	 * @return
	 * @uml.property  name="index"
	 */
	public int getIndex();

	/**
	 * @param  index
	 * @uml.property  name="index"
	 */
	public void setIndex(int index);

	/**
	 * @return
	 * @uml.property  name="registrationIndex"
	 */
	public int getRegistrationIndex();

	/**
	 * @param  registrationIndex
	 * @uml.property  name="registrationIndex"
	 */
	public void setRegistrationIndex(int registrationIndex);

	/**
	 * @return
	 * @uml.property  name="defaultField"
	 */
	public boolean isDefaultField();

	/**
	 * @param  defaultField
	 * @uml.property  name="defaultField"
	 */
	public void setDefaultField(boolean defaultField);

	/**
	 * @return
	 * @uml.property  name="visible"
	 */
	public boolean isVisible();

	/**
	 * @param  visible
	 * @uml.property  name="visible"
	 */
	public void setVisible(boolean visible);

	/**
	 * @return
	 * @uml.property  name="editable"
	 */
	public boolean isEditable();

	/**
	 * @param  editable
	 * @uml.property  name="editable"
	 */
	public void setEditable(boolean editable);

	/**
	 * @return
	 * @uml.property  name="required"
	 */
	public boolean isRequired();

	/**
	 * @param  required
	 * @uml.property  name="required"
	 */
	public void setRequired(boolean required);

	/**
	 * @return
	 * @uml.property  name="filterable"
	 */
	public boolean isFilterable();

	/**
	 * @param  filterable
	 * @uml.property  name="filterable"
	 */
	public void setFilterable(boolean filterable);

	/**
	 * @return
	 * @uml.property  name="searchable"
	 */
	public boolean isSearchable();

	/**
	 * @param  searchable
	 * @uml.property  name="searchable"
	 */
	public void setSearchable(boolean searchable);

	/**
	 * @return
	 * @uml.property  name="externalMappingString"
	 */
	public String getExternalMappingString();

	/**
	 * @param  externalMapping
	 * @uml.property  name="externalMappingString"
	 */
	public void setExternalMappingString(String externalMapping);

	/**
	 * @return
	 * @uml.property  name="externalMapping"
	 */
	public Map<String, String> getExternalMapping();

	/**
	 * @return
	 * @uml.property  name="externalMappings"
	 */
	public List<Map<String, String>> getExternalMappings();

	/**
	 * @param  externalMapping
	 * @uml.property  name="externalMapping"
	 */
	public void setExternalMapping(Map<String, String> externalMapping);

	/**
	 * @param  externalMappings
	 * @uml.property  name="externalMappings"
	 */
	public void setExternalMappings(List<Map<String, String>> externalMappings);

	/**
	 * @param  value
	 * @uml.property  name="singleExternalMapping"
	 */
	public void setSingleExternalMapping(String value);

	/**
	 * @param  values
	 * @uml.property  name="singleExternalMappings"
	 */
	public void setSingleExternalMappings(List<String> values);

	/**
	 * @return
	 * @uml.property  name="singleExternalMapping"
	 */
	public String getSingleExternalMapping();

	/**
	 * @return
	 * @uml.property  name="singleExternalMappings"
	 */
	public List<String> getSingleExternalMappings();

	public boolean hasExternalMapping();

	public boolean hasSingleExternalMapping();

	/**
	 * @return
	 * @uml.property  name="externallyManaged"
	 */
	public boolean isExternallyManaged();

	/**
	 * @param  externallyManaged
	 * @uml.property  name="externallyManaged"
	 */
	public void setExternallyManaged(boolean externallyManaged);

	/**
	 * @return
	 * @uml.property  name="listValues"
	 */
	public boolean isListValues();

	/**
	 * @param  listValues
	 * @uml.property  name="listValues"
	 */
	public void setListValues(boolean listValues);

	/**
	 * @return
	 * @uml.property  name="options"
	 */
	public List<ProfileFieldOption> getOptions();

	/**
	 * @param  options
	 * @uml.property  name="options"
	 */
	public void setOptions(List<ProfileFieldOption> options);

	/**
	 * @return
	 * @uml.property  name="profileFieldText"
	 */
	public List getProfileFieldText();

	/**
	 * @param  textList
	 * @uml.property  name="profileFieldText"
	 */
	public void setProfileFieldText(List textList);

}
