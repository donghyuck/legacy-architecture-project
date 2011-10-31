package architecture.ee.model.impl;

import architecture.ee.model.ModelObject;

public abstract class AbstractModelObject<T> implements ModelObject<T>{

	private boolean isNew ;
	
	public AbstractModelObject() {

	}
		
	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	@Override
	public abstract Object clone();
	
	
	
}
