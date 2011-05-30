package architecture.ee.component;

import java.io.Serializable;

public interface BaseModel<T> extends Cloneable, Comparable<T>, Serializable {

	public long getID();
	
	public void setID(long id);
	
	public Object clone();
	
	public String toXmlString();
	
	public String toString();
	
}
