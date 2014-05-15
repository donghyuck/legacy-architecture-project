package architecture.ee.web.community.forum.dao;

import java.util.List;

import architecture.ee.web.community.forum.Topic;
import architecture.ee.web.community.forum.TopicNotFoundException;


public interface TopicDao {

	public abstract Topic load(long topicId) throws TopicNotFoundException ; 
	
	public abstract Long nextId(); // 
	
	//public abstract void update( Topic topic );
	
	public abstract void insert( Topic topic );
	
	//public abstract void delete( Topic topic );
	
	// public abstract void move( Long fromId, Long toId);
	
	public abstract List<Topic> getTopics (Long forumId);
	
	

}
