package architecture.ee.web.attachment.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import architecture.common.cache.CacheSizes;
import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.EntityModelObjectSupport;
import architecture.ee.web.attachment.Attachment;

public class AttachmentImpl extends EntityModelObjectSupport implements Attachment {

    private long attachmentId = -1L;
	
    private String name;
	
	private String contentType ;
	
	private int objectType ;
	
	private long objectId;
	
	private int size = 0 ;
	
	private int downloadCount = 0;
		
	private InputStream inputStream;

	private Integer thumbnailSize = 0;
	
	private String thumbnailContentType = "png";
	
	@JsonIgnore
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

	public int getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}

	@JsonIgnore
	public int getCachedSize() {
		return CacheSizes.sizeOfLong() + CacheSizes.sizeOfString(getName())
				+ CacheSizes.sizeOfString(contentType)
				+ CacheSizes.sizeOfInt()
				+ CacheSizes.sizeOfLong() 
				+ CacheSizes.sizeOfMap(getProperties())
				+ CacheSizes.sizeOfDate() + CacheSizes.sizeOfDate();
	}

	public int compareTo(Attachment o) {
		return 0;
	}

	@JsonIgnore
	public int getModelObjectType() {
		return ModelTypeFactory.getTypeIdFromCode("ATTACHMENT");
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	@JsonIgnore
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
	

	public Integer getThumbnailSize() {
		return thumbnailSize;
	}

	public void setThumbnailSize(Integer thumbnailSize) {
		this.thumbnailSize = thumbnailSize;
	}

	public String getThumbnailContentType() {
		return thumbnailContentType;
	}

	public void setThumbnailContentType(String thumbnailContentType) {
		this.thumbnailContentType = thumbnailContentType;
	}
	
}