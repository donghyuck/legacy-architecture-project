package architecture.ee.model.internal;

import architecture.ee.model.BaseModel;


public abstract class BaseModelImpl<T> implements BaseModel<T> {
	
	public abstract Object clone();

}
