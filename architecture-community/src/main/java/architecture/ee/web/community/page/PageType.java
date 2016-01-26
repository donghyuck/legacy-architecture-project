package architecture.ee.web.community.page;

public enum PageType {
	NONE(0),
	TEXT(1),	
	QUOTE(2),
	PHOTO(3),
	LINK(4),
	SPREADSHEET(5);
	
	private int id;
	
	private PageType(int id){
		this.id = id ;
	}

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}
	public String toString() {
		StringBuilder builder = new StringBuilder("PageType:");
		 switch(id)
	        {
	        case 0: // '\001'
	            builder.append("NONE");
	            break;

	        case 1: // '\002'
	            builder.append("TEXT");
	            break;
	        case 2: // '\002'
	            builder.append("QUOTE");
	            break;
	        case 3: // '\002'
	            builder.append("PHOTO");
	            break;
	        case 4: // '\002'
	            builder.append("LINK");
	            break;	            
	        case 5: // '\002'
	            builder.append("SPREADSHEET");
	            break;	            	            
	        default:
	            builder.append("id=").append(id);
	            break;
	        }
	        return builder.toString();
	}
	public static PageType getPageTypeById(int typeId){
		for( PageType type : PageType.values()){
			if( type.getId() == typeId)
				return type;
		}
		return PageType.NONE;
	}
}
