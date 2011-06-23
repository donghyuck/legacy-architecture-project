package architecture.ee.security;

import java.io.Serializable;

public class Permissions implements Serializable {

	enum PermissionAtom {
		NONE            (0L),
        VIEW_ONLINE_STATUS       (0x100000000000000L),        
        USER_ADMINISTRATION      (0x200000000000000L),        
        GROUP_ADMINISTRATION     (0x400000000000000L),        
        SYSTEM_ADMINISTRATION    (0x800000000000000L),
        HOSTED_ADMINISTRATION    (0x10000000L),
        ACCESS                   (1L);
		
		private long atomId;
        
        private PermissionAtom(long atomId) {
            this.atomId = atomId;
        }
        
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
    public static final long ACCESS                   = PermissionAtom.ACCESS.atomId;
    
	
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