package architecture.ee.web.community.category;

import java.util.Date;

import architecture.common.cache.CacheSizes;

public class DefaultCategory implements Category {

    private long categoryId = -1L;
    private String name;
    private String description;
    private Date creationDate;
    private Date modifiedDate;
    private boolean internal;
    private boolean hidden;

    public long getCategoryId() {
	return categoryId;
    }

    public void setCategoryId(long categoryId) {
	this.categoryId = categoryId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Date getCreationDate() {
	return creationDate;
    }

    public void setCreationDate(Date creationDate) {
	this.creationDate = creationDate;
    }

    public Date getModifiedDate() {
	return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
	this.modifiedDate = modifiedDate;
    }

    public boolean isInternal() {
	return internal;
    }

    public void setInternal(boolean internal) {
	this.internal = internal;
    }

    public boolean isHidden() {
	return hidden;
    }

    public void setHidden(boolean hidden) {
	this.hidden = hidden;
    }

    public int getCachedSize() {
	return CacheSizes.sizeOfLong() + CacheSizes.sizeOfString(name) + CacheSizes.sizeOfString(description)
		+ CacheSizes.sizeOfBoolean() + CacheSizes.sizeOfBoolean() + CacheSizes.sizeOfDate()
		+ CacheSizes.sizeOfDate();
    }

}
