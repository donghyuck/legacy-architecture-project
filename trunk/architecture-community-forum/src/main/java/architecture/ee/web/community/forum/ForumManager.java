package architecture.ee.web.community.forum;

import java.util.List;

import architecture.ee.web.community.struts2.action.admin.ajax.SiteManagementAction;

public interface ForumManager {
	public void updateForumAfterTopicAdd(Forum forum);
	
	
	public List<Forum> getForums(long objectType, long objectId);
	
	public int getForumCount(long objectType, long objectId);
}
