package architecture.ee.web.community.forum;

import java.util.List;

import architecture.common.user.User;

public interface TopicManager {
	
	public abstract Topic createTopic(User user);
	
	public abstract Topic createTopic(User user, int objectType, long objectId);
	
	public abstract void addTopic(Topic topic);
	
	public abstract void updateTopic(Topic topic);
	
	public abstract void deleteTopic(Topic topic);
	
	public abstract Topic getTopic(long topicId) throws TopicNotFoundException;
	
	public abstract List<Topic> getTopics(long forumId);
	
	public abstract int getTopicCount(long forumId) ;

	public abstract int getTopicViewCount(long topicId);
	
	public abstract void increaseTopicViewCount(long topicId);
	
}
