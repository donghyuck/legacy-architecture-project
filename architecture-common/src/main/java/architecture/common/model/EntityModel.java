package architecture.common.model;

import java.io.Serializable;

import architecture.common.cache.Cacheable;

public interface EntityModel extends Cacheable {

    public Serializable getPrimaryKeyObject();

    public int getObjectType();

    public long getObjectId();

}
