package architecture.ee.web.community.forum.impl;

//import architecture.common.cache.Cache;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.ee.web.community.announce.Announce;
import architecture.ee.web.community.announce.AnnounceNotFoundException;
import architecture.ee.web.community.forum.Topic;
import architecture.ee.web.community.forum.TopicManager;
import architecture.ee.web.community.forum.TopicNotFoundException;
import architecture.ee.web.community.forum.dao.TopicDao;

public class TopicManagerImpl implements TopicManager {
	
	private TopicDao topicDao; 
	private UserManager userManager;
	private Cache topicCache;
	
	
	public TopicDao getTopicDao() {
		return topicDao;
	}

	public void setTopicDao(TopicDao topicDao) {
		this.topicDao = topicDao;
	}

	public Cache getTopicCache() {
		return topicCache;
	}

	public void setTopicCache(Cache topicCache) {
		this.topicCache = topicCache;
	}

	/**
	 * @return userManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * @param userManager �ㅼ젙��userManager
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	
	public void addTopic(Topic topic) {
		Long topicId = topic.getTopicId();
		if( topicId < 0 ){
			topicId =topicDao.nextId();
		}
		topic.setTopicId(topicId);
		topicDao.insert(topic);
		updateCache(topic);
	}
	
	private void updateCache( Topic topic ){
		topicCache.put(new Element( topic.getTopicId(), topic ));		
	}
	
	
	public Topic getTopic(long topicId) throws TopicNotFoundException {
		
		Topic topic = null;
		
		if( topicCache.get(topicId) != null){
			topic = (Topic) topicCache.get(topicId).getValue(); 
		}
		
		if(topic == null){ // if not in the cache, then fetch from DB
			topic = topicDao.load(topicId);
		
			User user;
			try{
				user = userManager.getUser(topic.getUserId());
				topic.setUser (user);
			}catch (UserNotFoundException e){
				
			}
			updateCache(topic);
		}
		return topic;
	}
	
	public Topic createTopic(User user) {
		//TopicImpl impl = new TopicImpl(-1L, 0, -1L , user);
		TopicImpl impl = new TopicImpl(user);
		return impl;
	}
	
	public Topic createTopic(User user, int objectType, long objectId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Topic> getTopics(long forumId) {
		
		List<Topic> list = topicDao.getTopics(forumId);

		return list;
	}
}
