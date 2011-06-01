package architecture.ee.model;

import java.io.Serializable;

public interface BaseModel<T> extends Cloneable, Comparable<T>, Serializable {

	/**
	 * Gets the primary key of this model instance.
	 *
	 * @return the primary key of this model instance
	 */
	public Serializable getPrimaryKeyObj();
	
	/**
	 * Creates a shallow clone of this model instance.
	 *
	 * @return the shallow clone of this model instance
	 */
	public Object clone();
	
	/**
	 * Gets the XML representation of this model instance.
	 *
	 * @return the XML representation of this model instance
	 */
	public String toXmlString();
	
}
