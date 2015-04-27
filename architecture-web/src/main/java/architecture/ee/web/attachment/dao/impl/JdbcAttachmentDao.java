package architecture.ee.web.attachment.dao.impl;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.support.SqlLobValue;

import architecture.common.jdbc.schema.DatabaseType;
import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.jdbc.sqlquery.SqlQueryHelper;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.attachment.Attachment;
import architecture.ee.web.attachment.dao.AttachmentDao;
import architecture.ee.web.attachment.impl.AttachmentImpl;

public class JdbcAttachmentDao extends ExtendedJdbcDaoSupport implements AttachmentDao {
	

	private final RowMapper<Attachment> attachmentMapper = new RowMapper<Attachment>(){
		public Attachment mapRow(ResultSet rs, int rowNum) throws SQLException {
			AttachmentImpl image = new AttachmentImpl();
			image.setAttachmentId(rs.getLong("ATTACHMENT_ID"));
			image.setObjectType(rs.getInt("OBJECT_TYPE"));
			image.setObjectId(rs.getLong("OBJECT_ID"));
			image.setName(rs.getString("FILE_NAME"));
			image.setSize(rs.getInt("FILE_SIZE"));
			image.setContentType(rs.getString("CONTENT_TYPE"));
			image.setCreationDate(rs.getDate("CREATION_DATE"));
			image.setModifiedDate(rs.getDate("MODIFIED_DATE"));			
			return image;
		}		
	};
		
	public void insertAttachmentDownloads(List<AttachmentDownloadItem> list) {		
		final List<AttachmentDownloadItem> itemsToUse = list;		
		getExtendedJdbcTemplate().batchUpdate(getBoundSql("ARCHITECTURE_WEB.INSERT_ATTACHMENT_DOWNLOAD" ).getSql(),
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

	
	private ExtendedPropertyDao extendedPropertyDao;	
	private String sequencerName = "ATTACHMENT";
	private String attachmentPropertyTableName = "V2_ATTACHMENT_PROPERTY";
	private String attachmentPropertyPrimaryColumnName = "ATTACHMENT_ID";
	
	public void setExtendedPropertyDao(ExtendedPropertyDao extendedPropertyDao) {
		this.extendedPropertyDao = extendedPropertyDao;
	}
	
	
	public String getSequencerName() {
		return sequencerName;
	}


	public void setSequencerName(String sequencerName) {
		this.sequencerName = sequencerName;
	}

	public ExtendedPropertyDao getExtendedPropertyDao() {
		return extendedPropertyDao;
	}


	public String getAttachmentPropertyTableName() {
		return attachmentPropertyTableName;
	}


	public void setAttachmentPropertyTableName(String attachmentPropertyTableName) {
		this.attachmentPropertyTableName = attachmentPropertyTableName;
	}


	public String getAttachmentPropertyPrimaryColumnName() {
		return attachmentPropertyPrimaryColumnName;
	}


	public void setAttachmentPropertyPrimaryColumnName(
			String attachmentPropertyPrimaryColumnName) {
		this.attachmentPropertyPrimaryColumnName = attachmentPropertyPrimaryColumnName;
	}


	public Map<String, String> getAttachmentProperties(long attachmentId) {
		return extendedPropertyDao.getProperties(attachmentPropertyTableName, attachmentPropertyPrimaryColumnName, attachmentId);
	}

	public void deleteAttachmentProperties(long attachmentId) {
		extendedPropertyDao.deleteProperties(attachmentPropertyTableName, attachmentPropertyPrimaryColumnName, attachmentId);
	}
	
	public void setAttachmentProperties(long attachmentId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(attachmentPropertyTableName, attachmentPropertyPrimaryColumnName, attachmentId, props);
	}

	public List<Long> getAllAttachmentIds() {
		return getExtendedJdbcTemplate().query(
				getBoundSql("ARCHITECTURE_WEB.SELECT_ATTACHMENT_IDS" ).getSql(),
				new RowMapper<Long>(){
					public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getLong(1);
					}}
		);
	}

	public void deleteAttachmentData() {
		getJdbcTemplate().update(
				getBoundSql("ARCHITECTURE_WEB.DELETE_ALL_ATTACHMENT_DATAS" ).getSql()		
		);
	}

	public Attachment createAttachment(Attachment attachment) {
		Attachment toUse = attachment;		
		if( toUse.getAttachmentId() <1L){
			long attachmentId = getNextId(sequencerName);
			toUse.setAttachmentId(attachmentId);
		}
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.INSERT_ATTACHMENT").getSql(), 	
					new SqlParameterValue (Types.NUMERIC, toUse.getAttachmentId()), 
					new SqlParameterValue (Types.INTEGER, toUse.getObjectType() ), 
					new SqlParameterValue (Types.INTEGER, toUse.getObjectId() ), 
					new SqlParameterValue (Types.VARCHAR, toUse.getContentType()), 
					new SqlParameterValue (Types.VARCHAR, toUse.getName() ), 
					new SqlParameterValue (Types.INTEGER, toUse.getSize() ), 					
					new SqlParameterValue(Types.DATE, toUse.getCreationDate()),
					new SqlParameterValue(Types.DATE, toUse.getModifiedDate()));		
		if(!attachment.getProperties().isEmpty())
			setAttachmentProperties(attachment.getAttachmentId(), attachment.getProperties());	
		
		return toUse;
		
	}

	public void updateAttachment(Attachment attachment) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.UPDATE_ATTACHMENT").getSql(), 	
				new SqlParameterValue (Types.INTEGER, attachment.getObjectType() ), 
				new SqlParameterValue (Types.INTEGER, attachment.getObjectId() ), 
				new SqlParameterValue (Types.VARCHAR, attachment.getContentType()), 
				new SqlParameterValue (Types.VARCHAR, attachment.getName() ), 
				new SqlParameterValue (Types.INTEGER, attachment.getSize() ), 
				new SqlParameterValue(Types.DATE, attachment.getCreationDate()),
				new SqlParameterValue(Types.DATE, attachment.getModifiedDate()),
				new SqlParameterValue (Types.NUMERIC, attachment.getAttachmentId()) );		
		setAttachmentProperties(attachment.getAttachmentId(), attachment.getProperties());	
	}

	public void deleteAttachment(Attachment attachment) {		
		getJdbcTemplate().update(
				getBoundSql("ARCHITECTURE_WEB.DELETE_ATTACHMENT" ).getSql(),
				new SqlParameterValue (Types.NUMERIC, attachment.getAttachmentId())
		);
		deleteAttachmentProperties(attachment.getAttachmentId());
	}

	public void deleteAttachmentData(Attachment attachment) {		
		getJdbcTemplate().update(
				getBoundSql("ARCHITECTURE_WEB.DELETE_ATTACHMENT_DATA_BY_ID" ).getSql(),
				new SqlParameterValue (Types.NUMERIC, attachment.getAttachmentId())
		);
	}
	
	public InputStream getAttachmentData(Attachment attachment) {		
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_WEB.SELECT_ATTACHMENT_DATA_BY_ID").getSql(), SqlQueryHelper.getInputStreamRowMapper(), new SqlParameterValue (Types.NUMERIC, attachment.getAttachmentId()));		
	}

	public void saveAttachmentData(Attachment attachment, InputStream inputstream) {

		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.DELETE_ATTACHMENT_DATA_BY_ID").getSql(), new SqlParameterValue (Types.NUMERIC, attachment.getAttachmentId()));		
		if( getExtendedJdbcTemplate().getDatabaseType() == DatabaseType.oracle ){									
			getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.CREATE_EMPTY_ATTACHMENT_DATA").getSql(), new SqlParameterValue (Types.NUMERIC, attachment.getAttachmentId()));
			getExtendedJdbcTemplate().update(
				getBoundSql("ARCHITECTURE_WEB.UPDATE_ATTACHMENT_DATA").getSql(), 
					new Object[]{
						new SqlLobValue( inputstream , attachment.getSize(), getLobHandler()),
						attachment.getAttachmentId()
				}, 
				new int[]{
					Types.BLOB,
					Types.NUMERIC
				});
		}else{			
			getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.CREATE_ATTACHMENT_DATA").getSql(), 
				new SqlParameterValue ( Types.NUMERIC, attachment.getAttachmentId()), 
				new SqlParameterValue ( Types.BLOB,  new SqlLobValue( inputstream , attachment.getSize(), getLobHandler() ) ) 
			);
		}
	}

	public Attachment getByAttachmentId(long attachmentId) {
		Attachment attach = getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_WEB.SELECT_ATTACHMENT_BY_ID").getSql(), attachmentMapper, new SqlParameterValue (Types.NUMERIC, attachmentId ));		
		attach.setProperties(this.getAttachmentProperties(attachmentId));
		return attach;
	}

	public List<Attachment> getByObjectTypeAndObjectId(int objectType, long objectId) {
		return getExtendedJdbcTemplate().query(
				getBoundSql("ARCHITECTURE_WEB.SELECT_ATTACHMENT_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				attachmentMapper,
				new SqlParameterValue (Types.NUMERIC, objectType ), new SqlParameterValue (Types.NUMERIC, objectId ));		
	}


	public int getAttachmentCount(int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForInt(
				getBoundSql("ARCHITECTURE_WEB.COUNT_ATTACHMENT_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				new SqlParameterValue(Types.NUMERIC, objectType ), 
				new SqlParameterValue(Types.NUMERIC, objectId ));
	}

	@Override
	public long getAttachmentUsage(int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForObject(
				getBoundSql("ARCHITECTURE_WEB.USAGE_ATTACHMENT_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				Long.class,
				new SqlParameterValue(Types.NUMERIC, objectType ), 
				new SqlParameterValue(Types.NUMERIC, objectId ));
	}

}
