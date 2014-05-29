package architecture.ee.web.community.forum.impl;

//import architecture.common.cache.Cache;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	protected Log log = LogFactory.getLog(getClass());
	
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
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void addTopic(Topic topic) {
		Long topicId = topic.getTopicId();
		if( topicId < 0 ){
			topicId =topicDao.nextId();
		}
		topic.setTopicId(topicId);
		log.debug("=============== addTopic : " + topicId);
		topicDao.insert(topic);
		//updateCache(topic);
		log.debug("=================  addTopic done : ");
	}
	
	public void updateTopic(Topic topic) {
		log.debug("=============== updateTopic : " + topic.getTopicId());
		topicDao.update(topic);
		updateCache(topic);
	}
	
	
	private void updateCache( Topic topic ){
		topicCache.put(new Element( topic.getTopicId(), topic ));		
	}
	
	public void deleteTopic(Topic topic) {
		log.debug("dele userId          ============================ " + topic.getUserId());
		topicDao.delete(topic);
		topicCache.remove(topic.getTopicId());
	}
	
	
	public Topic getTopic(long topicId) throws TopicNotFoundException {
		
		Topic topic = null;
		
		// check whether in the cache
		if( topicCache.get(topicId) != null){
			topic = (Topic) topicCache.get(topicId).getValue(); 
		}
		
		if(topic == null){ // if not exist in the cache, then fetch from DB
			topic = topicDao.load(topicId);
		
			User user;
			try{
				user = userManager.getUser(topic.getUserId());
				topic.setUser (user);
			}catch (UserNotFoundException e){
				
			}
			updateCache(topic); // put in the cache
		}
		// increase view cnt
		//topicDao.increaseTopicViewCount(topicId); 
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
		System.out.println("getTopics ==============" + forumId);
		
		List <Long> topicIds = topicDao.getTopicIds(forumId);
		
		List <Topic> list = new ArrayList<Topic>();
		
		for( Long topicId : topicIds){
			try{
				Topic topic = getTopic(topicId);
				list.add(topic);
				log.debug("=========== list에 넣습니다. : " + topic.getSubject());
			} catch (TopicNotFoundException e){
				log.warn(e);
			}
		}
		log.debug("============= 리스트 생성 완료 ==================");
		//List<Topic> list = topicDao.getTopics(forumId);
		return list;
	}
	
	public int getTopicCount(long forumId) {
		return topicDao.getTopicCount(forumId);
	}
	
	public int getTopicViewCount(long topicId){
		return topicDao.getTopicViewCount(topicId);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void increaseTopicViewCount(long topicId) {
		topicDao.increaseTopicViewCount(topicId); 
		log.debug("increaseTopicViewCount  ====================== done ");
		topicCache.remove(topicId);
	}
}
