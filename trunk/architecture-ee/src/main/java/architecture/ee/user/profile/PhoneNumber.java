package architecture.ee.user.profile;


/**
 * @author  donghyuck
 */
public class PhoneNumber {
	
	/**
	 * @author                 donghyuck
	 */
	enum Type {
		/**
		 * @uml.property  name="hOME"
		 * @uml.associationEnd  
		 */
		HOME("home"), 
		/**
		 * @uml.property  name="wORK"
		 * @uml.associationEnd  
		 */
		WORK("work"), 
		/**
		 * @uml.property  name="fAX"
		 * @uml.associationEnd  
		 */
		FAX("fax"), 
		/**
		 * @uml.property  name="mOBILE"
		 * @uml.associationEnd  
		 */
		MOBILE("mobile"), 
		/**
		 * @uml.property  name="pAGER"
		 * @uml.associationEnd  
		 */
		PAGER("pager"), 
		/**
		 * @uml.property  name="oTHER"
		 * @uml.associationEnd  
		 */
		OTHER("other");
		
		/**
		 * @uml.property  name="name"
		 */
		private String name;

		private Type(String name) {
			this.name = name;
		}		
		
        /**
		 * @return
		 * @uml.property  name="name"
		 */
        public String getName()
        {
            return name;
        }

        public static Type fromName(String name)
        {
            if(HOME.getName().equals(name))
                return HOME;
            if(WORK.getName().equals(name))
                return WORK;
            if(FAX.getName().equals(name))
                return FAX;
            if(MOBILE.getName().equals(name))
                return MOBILE;
            if(PAGER.getName().equals(name))
                return PAGER;
            if(OTHER.getName().equals(name))
                return OTHER;
            else
                throw new IllegalArgumentException((new StringBuilder()).append("Unknown phone number typename: ").append(name).toString());
        }

        public String toString()
        {
            return getName();
        }
	}
	
    /**
	 * @uml.property  name="phoneNumber"
	 */
    private String phoneNumber;
    /**
	 * @uml.property  name="type"
	 * @uml.associationEnd  
	 */
    private Type type;	

    public PhoneNumber()
    {
    }

    public PhoneNumber(String phoneNumber, Type type)
    {
        this.phoneNumber = phoneNumber;
        this.type = type;
    }

    /**
	 * @param phoneNumber
	 * @uml.property  name="phoneNumber"
	 */
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    /**
	 * @return
	 * @uml.property  name="phoneNumber"
	 */
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setTypeString(String typeName)
    {
        type = Type.fromName(typeName);
    }

    public String getTypeString()
    {
        if(type != null)
            return type.getName();
        else
            return null;
    }

    /**
	 * @param type
	 * @uml.property  name="type"
	 */
    public void setType(Type type)
    {
        this.type = type;
    }

    /**
	 * @return
	 * @uml.property  name="type"
	 */
    public Type getType()
    {
        return type;
    }

    public boolean equals(Object o)
    {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        PhoneNumber that = (PhoneNumber)o;
        if(phoneNumber == null ? that.phoneNumber != null : !phoneNumber.equals(that.phoneNumber))
            return false;
        return type == that.type;
    }

    public int hashCode()
    {
        int result = phoneNumber == null ? 0 : phoneNumber.hashCode();
        result = 31 * result + (type == null ? 0 : type.hashCode());
        return result;
    }
}
