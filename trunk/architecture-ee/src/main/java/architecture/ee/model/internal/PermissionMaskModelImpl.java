package architecture.ee.model.internal;

import architecture.ee.security.PermissionMask;

public class PermissionMaskModelImpl extends BaseModelImpl<PermissionMask> implements PermissionMask {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7976074911449979062L;
	private String name;
    private int mask;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMask() {
		return mask;
	}
	public void setMask(int mask) {
		this.mask = mask;
	}
	public long getPrimaryKey() {
		return -1L;
	}
	public String toXmlString() {
		return null;
	}

	@Override
	public Object clone() {
		return null;
	}
	@Override
	public void setPrimaryKey(long pk) {		
	}
	public int compareTo(PermissionMask o) {
		return 0;
	}
}
