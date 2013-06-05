package architecture.ee.web.model;

import java.util.Map;

import architecture.common.model.ModelObject;
import architecture.ee.web.attachment.Attachment;

public interface AttachmentModel extends ModelObject<Attachment> {

	public long getAttachmentId();
	
	public void setAttachmentId(long attachementId);
	
	public int getObjectType ();
	
	public void setObjectType( int objectType );
	
	public long getObjectId();
	
	public void setObjectId(long objectId);
	
	public String getName();
	
	public void setName(String name);
	
	public int getSize();
	
	public void setSize(int size);
	
	public String getContentType();
	
	public void setContentType(String contentType);
	
	public Map<String, String> getProperties();
	
	public void setProperties(Map<String, String> properties);
	
	public int getDownloadCount();
	
	public void setDownloadCount(int downloadCount);
	
}