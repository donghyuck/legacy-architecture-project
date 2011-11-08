package architecture.ee.security.permission;

/**
 * @author                 donghyuck
 */
public enum PermissionType {
	
	/**
	 * @uml.property  name="aDDITIVE"
	 * @uml.associationEnd  
	 */
	ADDITIVE(1), /**
	 * @uml.property  name="nEGATIVE"
	 * @uml.associationEnd  
	 */
	NEGATIVE(2);

	/**
	 * @uml.property  name="id"
	 */
	private int id;

	private PermissionType(int id) {
		this.id = id;
	}

	public String toString() {
		return String.valueOf(id);
	}

	/**
	 * @return
	 * @uml.property  name="id"
	 */
	public int getId() {
		return this.id;
	}
	
}
