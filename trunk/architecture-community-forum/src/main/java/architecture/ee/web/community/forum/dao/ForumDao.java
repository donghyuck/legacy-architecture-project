package architecture.ee.web.community.forum.dao;

import java.util.List;

import architecture.ee.web.community.forum.Forum;

public interface ForumDao {
	public void updateForumAfterTopicAdd(Forum forum);
	
	public List<Forum> getForumList(long objectType, long objectId);
	
	public int getForumCount(long objectType, long objectId);
}
