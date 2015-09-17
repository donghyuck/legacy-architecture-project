package architecture.user.permission;

import java.io.Serializable;

/**
* Represents a set of permissions that an entity has for an object in the system. For example,
* the rights that a user has for a category. Permissions are used by the protection proxy objects
* defined for each major component of the system to provide access rights.<p>
* <p/>
* A Permissions object is internally represented as a long with each bit indicating whether
* a particular permission is set. The constants defined by extensions of this class define the bit
* masks that can be used for permission operations. For example, the following code creates
* permissions:<pre>
* <p/>
* // Create a permissions object with only read permissions set to true.
* Permissions perms1 = new Permissions(Permissions.READ_FORUM);
* // Create a permissions object with read and system admin permissions set to true.
* Permissions perms2 = new Permissions(Permissions.READ_FORUM | Permissions.SYSTEM_ADMIN);</pre>
* <p/>
* If we were to view the bits of each variable, <tt>perms1</tt> would be
* <tt>0000000000000000000000000000000000000000000000000000000000000001</tt> and
* <tt>perms2</tt> would be
* <tt>0000000000000000000000000000000010000000000000000000000000000001</tt>.<p>
*/

public class Permissions implements Serializable {

	private static final long serialVersionUID = -8195862189097761722L;

	/**
	 * @author donghyuck son
	 */
	enum PermissionAtom {
		NONE            		 (0L),
        VIEW_ONLINE_STATUS       (0x100000000000000L),        
        USER_ADMINISTRATION      (0x200000000000000L),        
        GROUP_ADMINISTRATION     (0x400000000000000L),        
        SYSTEM_ADMINISTRATION    (0x800000000000000L),
        HOSTED_ADMINISTRATION    (0x10000000L),
        
        
        READ_DOCUMENT			 (16384L),
        READ_COMMENT			 (0x10000L),
        CREATE_DOCUMENT			 (0x40000L),
        CREATE_COMMENT			 (0x100000L),
        RATE_DOCUMENT			 (0x2000000L),
        CREATE_IMAGE			 (0x4000000L),
        CREATE_POLL              (16L),
        
        VOTE_IN_POLL             (1024L),
        CREATE_ANNOUNCEMENT		 (4096L),
        
        /**
		 */
        VIEW                     (1L);
		
		/**
		 */
		private long atomId;
        
        private PermissionAtom(long atomId) {
            this.atomId = atomId;
        }
        
        /**
		 * @return
		 */
        public long getAtomId()
        {
            return atomId;
        }
	}
	
    public static final long NONE                     = PermissionAtom.NONE.atomId;
    public static final long VIEW_ONLINE_STATUS       = PermissionAtom.VIEW_ONLINE_STATUS.atomId;
    public static final long USER_ADMINISTRATION      = PermissionAtom.USER_ADMINISTRATION.atomId;
    public static final long GROUP_ADMINISTRATION     = PermissionAtom.GROUP_ADMINISTRATION.atomId;
    public static final long SYSTEM_ADMINISTRATION    = PermissionAtom.SYSTEM_ADMINISTRATION.atomId;
    public static final long HOSTED_ADMINISTRATION    = PermissionAtom.HOSTED_ADMINISTRATION.atomId;
    public static final long CREATE_IMAGE             = PermissionAtom.CREATE_IMAGE.atomId;
    public static final long VIEW                     = PermissionAtom.VIEW.atomId;
    
	
	private long permissions;

	public Permissions(long permissions) {
		this.permissions = permissions;
	}

	public long value() {
		return permissions;
	}

	public Permissions(Permissions permissions1, Permissions permissions2) {
		permissions = permissions1.permissions | permissions2.permissions;
	}

	public boolean hasPermission(long permissionTypes) {
		return (permissions & permissionTypes) != 0L;
	}

	public void set(long permissionTypes, boolean value) {
		if (value) {
			permissions = permissions | permissionTypes;
		} else {
			permissionTypes = ~permissionTypes;
			permissions = permissions & permissionTypes;
		}
	}
}