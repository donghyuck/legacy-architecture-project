package architecture.ee.web.attachment.dao.impl;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.attachment.Attachment;
import architecture.ee.web.attachment.dao.AttachmentDao;

public class JdbcAttachmentDao extends ExtendedJdbcDaoSupport implements AttachmentDao {

	public void insertAttachmentDownloads(List<AttachmentDownloadItem> list) {
		
		final List<AttachmentDownloadItem> itemsToUse = list;		
		getExtendedJdbcTemplate().batchUpdate(getBoundSql("FRAMEWORK_V2.INSERT_ATTACHMENT_DOWNLOAD" ).getSql(),
			    new BatchPreparedStatementSetter(){
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						for( AttachmentDownloadItem item : itemsToUse ){
							long time = System.currentTimeMillis();
                            ps.setLong( 1, item.getAttachmentId() );                  
                            ps.setDate( 2, new java.sql.Date( item.getDownloadDate().getTime() ));
                            ps.setInt( 3,  item.isDownloadCompleted() ? 1 : 0  );
						}
					}
					public int getBatchSize() {
						return itemsToUse.size();
					}});				
	}

	public List<Long> getAllAttachmentIds() {
		return getExtendedJdbcTemplate().query(
				getBoundSql("FRAMEWORK_V2.SELECT_ATTACHMENT_IDS" ).getSql(),
				new RowMapper<Long>(){
					public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getLong(1);
					}},
				new Object[]{},
				new int [] {Types.DATE }
		);
	}

	public void deleteAttachmentData() {
		getJdbcTemplate().update(
				getBoundSql("FRAMEWORK_V2.DELETE_ATTACHMENTS" ).getSql()		
		);
	}

	public List getTempAttachmentIdsWithCreationGreaterThan(Date date) {
		return null;
	}

	public void create(Attachment attachment) {
	
	}

	public void update(Attachment attachment) {
		
	}

	public void delete(Attachment attachment) {
		
	}

	public InputStream getAttachmentData(Attachment attachment) {
		return null;
	}

	public void saveAttachmentData(Attachment attachment,
			InputStream inputstream) {
		
	}

	public Attachment getByAttachmentId(long attachmentId) {
		return null;
	}

	public List getByObjectTypeAndObjectId(int objectType, long objectId) {
		return null;
	}

	public List retrieveOldTemporaryAttachments() {
		return null;
	}

}
