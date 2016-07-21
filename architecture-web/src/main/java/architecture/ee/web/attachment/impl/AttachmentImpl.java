package architecture.ee.web.attachment.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import architecture.common.cache.CacheSizes;
import architecture.common.model.ModelObjectType;
import architecture.common.model.v2.BaseModelObject;
import architecture.ee.web.attachment.Attachment;

public class AttachmentImpl extends BaseModelObject implements Attachment {

	private long userId = -1L;
	
    private long attachmentId = -1L;
	
    private String name;
	
	private String contentType ;
	
	private int objectType ;
	
	private long objectId;
	
	private int size = 0 ;
	
	private int downloadCount = 0;
	
	private Map<String, String> properties = new HashMap<String, String>();
	
	private InputStream inputStream;
	
	public Serializable getPrimaryKeyObject() {
		return attachmentId;
	}

	public long getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(long attachmentId) {
		this.attachmentId = attachmentId;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size= size;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public int getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}

	public int getCachedSize() {
		return CacheSizes.sizeOfLong() + CacheSizes.sizeOfString(getName())
				+ CacheSizes.sizeOfString(contentType)
				+ CacheSizes.sizeOfString(getDescription())
				+ CacheSizes.sizeOfInt()
				+ CacheSizes.sizeOfLong() 
				+ CacheSizes.sizeOfMap(properties)
				+ CacheSizes.sizeOfDate() + CacheSizes.sizeOfDate();
	}

	public int compareTo(Attachment o) {
		return 0;
	}

	public int getModelObjectType() {
		return ModelObjectType.ATTACHMENT.getKey();
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public InputStream getInputStream() throws IOException {
		return inputStream;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Attachement{");
	    sb.append("attachmentId=").append(attachmentId);
	    sb.append(",name=").append(getName());
	    sb.append(",contentType=").append(contentType);
	    sb.append("size=").append(size);
		sb.append("}");
		return sb.toString();
	}
}
