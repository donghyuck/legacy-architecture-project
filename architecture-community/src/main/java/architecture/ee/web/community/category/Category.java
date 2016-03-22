package architecture.ee.web.community.category;

import java.util.Date;

import architecture.common.cache.Cacheable;

public interface Category extends Cacheable {

    public long getCategoryId();

    public void setCategoryId(long categoryId);

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public Date getCreationDate();

    public void setCreationDate(Date creationDate);

    public Date getModifiedDate();

    public void setModifiedDate(Date modifiedDate);

    public boolean isInternal();

    public void setInternal(boolean internal);

    public boolean isHidden();

    public void setHidden(boolean hidden);

}
