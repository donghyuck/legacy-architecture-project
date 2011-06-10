package architecture.ee.model.internal;

import architecture.ee.model.BaseModel;


public abstract class BaseModelImpl<T> implements BaseModel<T> {
	
	public abstract Object clone();

	/**
	 * Sets the primary key of this account
	 * 
	 * @param pk
	 *            the primary key of this account
	 */
	public abstract void setPrimaryKey(long pk);
	
}
