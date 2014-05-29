package architecture.ee.web.community.forum.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.community.forum.Forum;
import architecture.ee.web.community.forum.Topic;
import architecture.ee.web.community.forum.dao.ForumDao;
import architecture.ee.web.community.forum.impl.ForumImpl;
import architecture.ee.web.community.forum.impl.TopicImpl;

public class JdbcForumDao extends ExtendedJdbcDaoSupport implements ForumDao {
	
	private ExtendedPropertyDao extendedPropertyDao;
	private String sequencerName = "BOARD";
	private String forumPropertyTableName = "V2_BOARD_PROPERTY";
	private String forumPropertyPrimaryColumnName = "BOARD_ID";
	
	private final RowMapper<Forum> forumMapper = new RowMapper<Forum>(){
		public Forum mapRow(ResultSet rs, int rowNum) throws SQLException {
			ForumImpl forum = new ForumImpl();
			forum.setObjectType(rs.getLong("OBJECT_TYPE"));
			forum.setObjectId(rs.getLong("OBJECT_ID"));
			forum.setForumId(rs.getLong("BOARD_ID"));
			forum.setBoardName(rs.getString("BOARD_NAME"));
			forum.setBoardDesc(rs.getString("BOARD_DESC"));
			forum.setCommentYn(rs.getInt("COMMENT_YN") == 1? true : false);
			forum.setFileYn(rs.getInt("FILE_YN") == 1 ? true : false);
			forum.setAnonyYn(rs.getInt("ANONY_YN") == 1 ? true : false);
			forum.setUseYn(rs.getInt("USE_YN") == 1 ? true : false);
			forum.setCreationDate(rs.getDate("CREATION_DATE"));
			forum.setCreateId(rs.getLong("CREATE_ID"));
			forum.setModifiedDate(rs.getDate("MODIFIED_DATE"));
			forum.setModifyId(rs.getLong("MODIFY_ID"));
			forum.setLastThreadDate(rs.getDate("LAST_THREAD_DATE"));
			forum.setTotalCnt(rs.getLong("TOTAL_CNT"));
			return forum;
			/*
			 * B.OBJECT_TYPE,
				       B.OBJECT_ID,
				       B.BOARD_ID,
				       B.BOARD_NAME,
				       B.BOARD_DESC,
				       B.COMMENT_YN,
				       B.FILE_YN,
				       B.ANONY_YN,
				       B.USE_YN,
				       B.CREATION_DATE,
				       B.CREATE_ID,
				       B.MODIFIED_DATE,
				       B.MODIFY_ID,
				       B.LAST_THREAD_DATE,
				       B.THREAD_SIZE,
				       B.TOTAL_CNT
			 * 
			 * 
			 * */
		}
	};
	
	
	public void updateForumAfterTopicAdd(Forum forum) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY_FORUM.UPDATE_FORUM_AFTER_TOPIC_ADD").getSql(), 	
				//new SqlParameterValue(Types.DATE, forum.getLastThreadDate()),
				new SqlParameterValue(Types.NUMERIC, forum.getTotalCnt()),
				new SqlParameterValue(Types.NUMERIC, forum.getForumId())
		);		
		setForumProperties(forum.getForumId(), forum.getProperties());		
	}

	public void setForumProperties (long forumId, Map<String, String> props){
		extendedPropertyDao.updateProperties(forumPropertyTableName, forumPropertyPrimaryColumnName, forumId, props);
	}
	
	public void deleteForumProperties(long forumId) {
		extendedPropertyDao.deleteProperties(forumPropertyTableName, forumPropertyPrimaryColumnName, forumId);
	}
	
	public Long nextId() {
		
		return getNextId(sequencerName);
	}
	

	public ExtendedPropertyDao getExtendedPropertyDao() {
		return extendedPropertyDao;
	}

	public void setExtendedPropertyDao(ExtendedPropertyDao extendedPropertyDao) {
		this.extendedPropertyDao = extendedPropertyDao;
	}

	public String getSequencerName() {
		return sequencerName;
	}

	public void setSequencerName(String sequencerName) {
		this.sequencerName = sequencerName;
	}

	public String getForumPropertyTableName() {
		return forumPropertyTableName;
	}

	public void setForumPropertyTableName(String forumPropertyTableName) {
		this.forumPropertyTableName = forumPropertyTableName;
	}

	public String getForumPropertyPrimaryColumnName() {
		return forumPropertyPrimaryColumnName;
	}

	public void setForumPropertyPrimaryColumnName(
			String forumPropertyPrimaryColumnName) {
		this.forumPropertyPrimaryColumnName = forumPropertyPrimaryColumnName;
	}
	
	public List<Forum> getForumList(long objectType, long objectId) {
		List<Forum> list = null;
		log.debug("getForumList ==================== START");
		list =  getExtendedJdbcTemplate().query(getBoundSql("ARCHITECTURE_COMMUNITY_FORUM.SELECT_FORUM_LIST_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(),
				forumMapper,
				new SqlParameterValue(Types.NUMERIC,objectType),
				new SqlParameterValue(Types.NUMERIC,objectId)
				);
		log.debug("getForumList ==================== DONE");
		return list;
	}

	public int getForumCount(long objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForInt(
				getBoundSql("ARCHITECTURE_COMMUNITY_FORUM.FORUM_COUNT_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				new SqlParameterValue(Types.NUMERIC, objectType ),
				new SqlParameterValue(Types.NUMERIC, objectId ));
	}
}
