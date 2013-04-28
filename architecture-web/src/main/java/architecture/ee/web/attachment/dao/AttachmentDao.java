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

    public abstract List getAllAttachmentIds();

    public abstract void deleteAttachmentData();

    public abstract List getTempAttachmentIdsWithCreationGreaterThan(Date date);

    public abstract void create(Attachment attachment);

    public abstract void update(Attachment attachment);

    public abstract void delete(Attachment attachment);

    public abstract InputStream getAttachmentData(Attachment attachment);

    public abstract void saveAttachmentData(Attachment attachment, InputStream inputstream);

    public abstract Attachment getByAttachmentId(long attachmentId);

    public abstract List getByObjectTypeAndObjectId(int objectType, long objectId);

    public abstract List retrieveOldTemporaryAttachments();
    
}
