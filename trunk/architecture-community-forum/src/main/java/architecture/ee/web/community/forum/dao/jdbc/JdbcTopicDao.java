package architecture.ee.web.community.forum.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.community.forum.Topic;
import architecture.ee.web.community.forum.TopicNotFoundException;
import architecture.ee.web.community.forum.dao.TopicDao;
import architecture.ee.web.community.forum.impl.TopicImpl;

public class JdbcTopicDao extends ExtendedJdbcDaoSupport implements TopicDao {
	
	private ExtendedPropertyDao extendedPropertyDao;
	private String sequencerName = "THREAD";
	private String topicPropertyTableName = "V2_THREAD";
	private String topicPropertyPrimaryColumnName = "THREAD_ID";
	
	private final RowMapper<Topic> topicMapper = new RowMapper<Topic>(){
		public Topic mapRow(ResultSet rs, int rowNum) throws SQLException {
			TopicImpl topic = new TopicImpl();
			topic.setForumId(rs.getLong("BOARD_ID"));
			topic.setTopicId(rs.getLong("THREAD_ID"));
			topic.setUserId(rs.getLong("CREATE_ID"));
			topic.setTotalReplies(rs.getInt("COMMENT_CNT"));
			topic.setSubject(rs.getString("THREAD_TITLE"));
			topic.setContent(rs.getString("THREAD_CONTENT"));
			topic.setPasswd(rs.getString("PASSWD"));
			topic.setAttachmentId(rs.getLong("ATTACHMENT_ID"));
			topic.setViewCnt(rs.getLong("VIEW_CNT"));
			topic.setModifyId(rs.getLong("MODIFY_ID"));
			topic.setParentThreadId(rs.getLong("PARENT_THREAD_ID"));
			topic.setReplyDepth(rs.getInt("REPLY_DEPTH"));
			topic.setDelYn(rs.getInt("DEL_YN") == 1);
			topic.setName(rs.getString("CREATE_NAME"));
			
			return topic;
		}
	};
	
	
	public Long nextId() {
		
		return getNextId(sequencerName);
	}
	
	/**
	 * @return extendedPropertyDao
	 */
	public ExtendedPropertyDao getExtendedPropertyDao() {
		return extendedPropertyDao;
	}
	/**
	 * @param extendedPropertyDao �ㅼ젙��extendedPropertyDao
	 */
	public void setExtendedPropertyDao(ExtendedPropertyDao extendedPropertyDao) {
		this.extendedPropertyDao = extendedPropertyDao;
	}
	
	/**
	 * @return sequencerName
	 */
	public String getSequencerName() {
		return sequencerName;
	}
	/**
	 * @param sequencerName �ㅼ젙��sequencerName
	 */
	public void setSequencerName(String sequencerName) {
		this.sequencerName = sequencerName;
	}
	
	
	public String getTopicPropertyTableName() {
		return topicPropertyTableName;
	}

	public void setTopicPropertyTableName(String topicPropertyTableName) {
		this.topicPropertyTableName = topicPropertyTableName;
	}

	public String getTopicPropertyPrimaryColumnName() {
		return topicPropertyPrimaryColumnName;
	}

	public void setTopicPropertyPrimaryColumnName(
			String topicPropertyPrimaryColumnName) {
		this.topicPropertyPrimaryColumnName = topicPropertyPrimaryColumnName;
	}
	
	public Map<String, String> getTopicProperties(long topicId){
		return extendedPropertyDao.getProperties(topicPropertyTableName, topicPropertyPrimaryColumnName, topicId);
	}

	public void insert(Topic topic) {
		
		long topicIdToUse = topic.getTopicId();
		if( topicIdToUse < 0){
			topicIdToUse = getNextId(sequencerName);
		}
		System.out.println("topicIdToUse ============== " + topicIdToUse);
		topic.setTopicId(topicIdToUse);
		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY_FORUM.INSERT_TOPIC").getSql(),
				new SqlParameterValue(Types.NUMERIC, topic.getForumId()),
				new SqlParameterValue(Types.NUMERIC, topic.getTopicId()),
				new SqlParameterValue(Types.VARCHAR, topic.getSubject()),
				new SqlParameterValue(Types.CLOB, topic.getContent()),
				new SqlParameterValue(Types.VARCHAR, topic.getPasswd()),
				new SqlParameterValue(Types.NUMERIC, topic.getAttachmentId()),
				new SqlParameterValue(Types.NUMERIC, topic.getViewCnt()),
				new SqlParameterValue(Types.NUMERIC, topic.getUserId()),
				new SqlParameterValue(Types.VARCHAR, topic.getCreateName()),
				new SqlParameterValue(Types.DATE, topic.getCreationDate()),
				new SqlParameterValue(Types.NUMERIC, topic.getParentThreadId()),
				new SqlParameterValue(Types.NUMERIC, topic.getReplyDepth()),
				new SqlParameterValue(Types.NUMERIC, topic.isDelYn() ? 1 : 0),
				new SqlParameterValue(Types.NUMERIC, topic.getTotalReplies())
				);
		
		setTopicProperties(topic.getTopicId(), topic.getProperties());
		
	}
	
	public void setTopicProperties (long topicId, Map<String, String> props){
		extendedPropertyDao.updateProperties(topicPropertyTableName, topicPropertyPrimaryColumnName, topicId, props);
	}
	
	public Topic load (long topicId) throws TopicNotFoundException {
		try{
			Topic topic = getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_COMMUNITY_FORUM.SELECT_TOPIC_BY_ID").getSql(),
					topicMapper,
					new SqlParameterValue(Types.NUMERIC, topicId));
			topic.setProperties( getTopicProperties(topicId));
			return topic;
		}catch(DataAccessException e){
			e.printStackTrace();
			throw new TopicNotFoundException(e);
		}
	}
	
	public List<Topic> getTopics (Long forumId){
		return getExtendedJdbcTemplate().query(
				getBoundSql("ARCHITECTURE_COMMUNITY_FORUM.SELECT_TOPICS_BY_FORUM_ID").getSql(),
				topicMapper,
				new SqlParameterValue (Types.NUMERIC, forumId ));	
	}
}
