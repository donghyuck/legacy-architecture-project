package architecture.ee.web.model.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import architecture.common.cache.CacheSizes;
import architecture.common.model.ModelObjectType;
import architecture.common.model.impl.BaseModelObject;
import architecture.ee.web.attachment.Attachment;
import architecture.ee.web.model.AttachmentModel;

public class AttachmentModelImpl extends BaseModelObject<Attachment>  implements AttachmentModel {

    private static final long serialVersionUID = 3961478724321254035L;
    
    private long attachmentId = -1L;
	
    private String name;
	
	private String contentType ;
	
	private int objectType ;
	
	private long objectId;
	
	private int size = 0 ;
	private int downloadCount = 0;
	
	private Map<String, String> properties = new HashMap<String, String>();
	
	public Serializable getPrimaryKeyObject() {
		return attachmentId;
	}

	public ModelObjectType getModelObjectType() {
		return ModelObjectType.ATTACHMENT;
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
		int size = 0 ;
		size += CacheSizes.sizeOfLong();
		size += CacheSizes.sizeOfMap(properties);
		return 0;
	}

	public int compareTo(Attachment o) {
		return 0;
	}
}