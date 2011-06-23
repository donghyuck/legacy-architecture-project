package architecture.ee.user.profile;

public class EmailAddress {

	enum Type { HOME("home"), WORK("work"), OTHER("other");
		
		private String name;

		private Type(String name) {
			this.name = name;
		}
		
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
	

    private String email;
    private Type type;
    
	public EmailAddress()
    {
    }

    public EmailAddress(String email, Type type)
    {
        this.email = email;
        this.type = type;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

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
