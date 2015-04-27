package architecture.ee.web.attachment.dao;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import architecture.common.util.DateUtils;
import architecture.ee.web.attachment.Attachment;

public interface AttachmentDao {

    public static class AttachmentDownloadItem
    {
        private long attachmentId;
        private Date downloadDate;
        private boolean downloadCompleted;

        public AttachmentDownloadItem(long attachmentId, Date downloadDate, boolean downloadCompleted)
        {
            this.attachmentId = attachmentId;
            this.downloadDate = DateUtils.clone(downloadDate);
            this.downloadCompleted = downloadCompleted;
        }
        
        public long getAttachmentId()
        {
            return attachmentId;
        }

        public Date getDownloadDate()
        {
            return DateUtils.clone(downloadDate);
        }

        public boolean isDownloadCompleted()
        {
            return downloadCompleted;
        }
    }    
	
    public abstract void insertAttachmentDownloads(List<AttachmentDownloadItem> list);

    public abstract List<Long> getAllAttachmentIds();

    public abstract int getAttachmentCount(int objectType, long objectId);
    
    public abstract void deleteAttachmentData();

    public abstract void deleteAttachmentData(Attachment attachment);
    
    public abstract Attachment createAttachment(Attachment attachment);

    public abstract void updateAttachment(Attachment attachment);

    public abstract void deleteAttachment(Attachment attachment);

    public abstract InputStream getAttachmentData(Attachment attachment);

    public abstract void saveAttachmentData(Attachment attachment, InputStream inputstream);

    public abstract Attachment getByAttachmentId(long attachmentId);

    public abstract List<Attachment> getByObjectTypeAndObjectId(int objectType, long objectId);
    
    public long getAttachmentUsage(int objectType, long objectId) ;
    
}
