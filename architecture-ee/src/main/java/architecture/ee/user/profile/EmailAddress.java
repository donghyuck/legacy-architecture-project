package architecture.ee.user.profile;


/**
 * @author  donghyuck
 */
public class EmailAddress {

	/**
	 * @author                 donghyuck
	 */
	enum Type { /**
	 * @uml.property  name="hOME"
	 * @uml.associationEnd  
	 */
	HOME("home"), /**
	 * @uml.property  name="wORK"
	 * @uml.associationEnd  
	 */
	WORK("work"), /**
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
	            if(OTHER.getName().equals(name))
	                return OTHER;
	            else
	                throw new IllegalArgumentException((new StringBuilder()).append("Unknown email address typename: ").append(name).toString());
	        }
		 
        public String toString()
        {
            return (new StringBuilder()).append("EmailAddress.Type: ").append(getName()).toString();
        }
	}
	

    /**
	 * @uml.property  name="email"
	 */
    private String email;
    /**
	 * @uml.property  name="type"
	 * @uml.associationEnd  
	 */
    private Type type;
    
	public EmailAddress()
    {
    }

    public EmailAddress(String email, Type type)
    {
        this.email = email;
        this.type = type;
    }

    /**
	 * @param email
	 * @uml.property  name="email"
	 */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
	 * @return
	 * @uml.property  name="email"
	 */
    public String getEmail()
    {
        return email;
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

    public String getTypeString()
    {
        return type == null ? null : type.getName();
    }

    public void setTypeString(String typeName)
    {
        type = Type.fromName(typeName);
    }

    public boolean equals(Object o)
    {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        EmailAddress that = (EmailAddress)o;
        if(email == null ? that.email != null : !email.equals(that.email))
            return false;
        return type == that.type;
    }

    public int hashCode()
    {
        int result = email == null ? 0 : email.hashCode();
        result = 31 * result + (type == null ? 0 : type.hashCode());
        return result;
    }

}
