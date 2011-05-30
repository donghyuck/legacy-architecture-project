package architecture.ee.component.internal;

import architecture.ee.component.BaseModel;

public abstract class AbstractBaseModel<T> implements BaseModel<T> {
	
	public abstract Object clone();

}
