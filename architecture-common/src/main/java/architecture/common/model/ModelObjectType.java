package architecture.common.model;

public enum ModelObjectType {
    Unknown(0, "unknown"),
	User(3, "user"),
	Group(4, "group");
	
	public static ModelObjectType ALL_TYPES[] = values();
	
	public static ModelObjectType INDEXABLE_TYPES[] = new ModelObjectType[]{User, Group};
	
	public static ModelObjectType valueOf(int typeId){
		
		for( ModelObjectType t : ALL_TYPES ){
			if(t.getTypeId() == typeId)
				return t;
		}
		
		return Unknown;		
	}
	
	public static ModelObjectType resolve(String typeName){
		if(typeName == null)
			return null;
		for( ModelObjectType t : ALL_TYPES ){
			if( t.toString().toLowerCase().equals(typeName.toLowerCase()) )
				return t;
		}
		return Unknown;
	}
		
    private int typeId;
    private String code;
   
	private ModelObjectType(int typeId, String code) {
		this.typeId = typeId;
		this.code = code;
	}
	
    public int getTypeId()
    {
        return typeId;
    }

    public int getKey()
    {
        return typeId;
    }

    public String getCode()
    {
        return code;
    }
    
}
