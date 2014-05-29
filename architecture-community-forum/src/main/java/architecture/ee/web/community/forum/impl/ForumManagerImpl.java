package architecture.ee.web.community.forum.impl;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.user.UserManager;
import architecture.ee.web.community.forum.Forum;
import architecture.ee.web.community.forum.ForumManager;
import architecture.ee.web.community.forum.dao.ForumDao;

public class ForumManagerImpl implements ForumManager {
	
	private ForumDao forumDao; 
	private UserManager userManager;
	private Cache forumCache;
	protected Log log = LogFactory.getLog(getClass());
	
	
	private void updateCache( Forum forum ){
		forumCache.put(new Element(  forum.getForumId(), forum ));		
	}
	
	
	public ForumDao getForumDao() {
		return forumDao;
	}




	public void setForumDao(ForumDao forumDao) {
		this.forumDao = forumDao;
	}




	public UserManager getUserManager() {
		return userManager;
	}




	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}




	public Cache getForumCache() {
		return forumCache;
	}




	public void setForumCache(Cache forumCache) {
		this.forumCache = forumCache;
	}




	public void updateForumAfterTopicAdd(Forum forum) {
		forumDao.updateForumAfterTopicAdd(forum);
		updateCache(forum);
	}
	
	public List<Forum> getForums(long objectType, long objectId) {
		return forumDao.getForumList(objectType, objectId);
	}
	
	public int getForumCount(long objectType, long objectId) {
		return forumDao.getForumCount(objectType, objectId);
	}
}
