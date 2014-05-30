package architecture.ee.web.community.forum;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import architecture.common.user.User;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.common.util.DateUtils;
import architecture.ee.web.community.announce.Announce;
import architecture.ee.web.community.forum.impl.ForumImpl;
import architecture.ee.web.community.forum.impl.TopicImpl;
import architecture.ee.web.community.site.DefaultWebSiteManager;
import architecture.ee.web.community.struts2.action.PageAction;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteManager;
import architecture.ee.web.site.WebSiteNotFoundException;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;

public class ForumAction extends PageAction{

	private TopicManager topicManager;
	
	private ForumManager forumManager;

	private Long objectId = -1L;
	
	private Integer objectType = 0;
	
	private Long forumId = -1L;
	
	private Long topicId = -1L;
	
    private int pageSize = 0 ;
    
    private int startIndex = 0 ;  
    

	public ForumManager getForumManager() {
		return forumManager;
	}

	public void setForumManager(ForumManager forumManager) {
		this.forumManager = forumManager;
	}

	public TopicManager getTopicManager() {
    	
		return topicManager;
	}

	public void setTopicManager(TopicManager topicManager) {
		this.topicManager = topicManager;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Integer getObjectType() {
		return objectType;
	}

	public void setObjectType(Integer objectType) {
		this.objectType = objectType;
	}

	
	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public Long getForumId() {
		return forumId;
	}

	public void setForumId(Long forumId) {
		this.forumId = forumId;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	
	public Topic getTargetTopic() throws TopicNotFoundException  {
		if(this.topicId > 0){
			return topicManager.getTopic(topicId);
		}else{
			return topicManager.createTopic(getUser());
		}
	}
	
	public List<Topic> getTargetTopics(){
		ParamUtils.printParameter(request, log);
		log.debug( "startIndex= " + startIndex + ", pageSize=" + pageSize  );
		
		if( forumId < 1){
			return Collections.EMPTY_LIST;
		}
		if (pageSize > 0){
			return topicManager.getTopics(forumId, startIndex, pageSize);
		}else{
			return topicManager.getTopics(forumId);
		}
	}
	
	public int getTargetTopicCount(){
		if( forumId < 1){
			return 0 ;
		}
		return 	topicManager.getTopicCount(forumId);	
	}
	
	/*
	public int getTargetTopicViewCount(){
		if ( forumId < 1){
			return 0;
		}
		//log.debug("now topicId ============== " + topicId);
		//topicManager.increaseTopicViewCount(topicId); // increase
		return topicManager.getTopicViewCount(topicId);
	}
	*/
		
    public String updateTopicViewCount() throws TopicNotFoundException {  
    	Topic targetTopicToUse = getTargetTopic();
    	if(targetTopicToUse.getTopicId() > 0 ){
    		topicManager.increaseTopicViewCount(targetTopicToUse.getTopicId());
    	}
        return success();
    }  
	
    // 신규 등록 & 수정
	public String saveTopic() throws Exception {
		ParamUtils.printParameter(request, log);
		//Map map = ParamUtils.getParametersAsMap(request);
		Map map = ParamUtils.getJsonParameter(request, "item", Map.class);
		
		Integer selectedTopicId = (Integer)map.get("topicId");
		topicId = selectedTopicId.longValue();
	
		Integer selectedForumId = (Integer)map.get("forumId");
		forumId = selectedForumId.longValue();
		
		User sessionUser = getUser();
		
		String subject = (String)map.get("subject");
		String body = (String)map.get("content");
		String passwd = (String)map.get("passwd");
		
		Integer attachmentId = (Integer)map.get("attachmentId");
		String createName = sessionUser.getName();
		
		log.debug("세션 유저명 ============ " + createName);
		
		Topic targetTopic = getTargetTopic();
		
		targetTopic.setSubject(subject);
		targetTopic.setContent(body);
		targetTopic.setPasswd(passwd);
		targetTopic.setAttachmentId(attachmentId.longValue());
		targetTopic.setCreateName(createName);
		log.debug("forumId =============== " + forumId);
		targetTopic.setForumId(forumId);
		//log.debug(targetTopic);
		
		if(targetTopic.getTopicId() > 0)
		{
			topicManager.updateTopic(targetTopic);
			
		}else{
			topicManager.addTopic(targetTopic);
			
			Forum forum = new ForumImpl();
			forum.setForumId(targetTopic.getForumId());
			Integer totalCnt = getTargetTopicCount();
			forum.setTotalCnt(totalCnt.longValue());
			forumManager.updateForumAfterTopicAdd(forum);
		}
		return success();
	}
	
	public String deleteTopic() throws Exception {
		ParamUtils.printParameter(request, log);
		Map map = ParamUtils.getJsonParameter(request, "item", Map.class);
		
		if(topicId < 1){
			Integer selectedTopicId = (Integer)map.get("topicId");
			topicId = selectedTopicId.longValue();
		}
		if(forumId < 1){
			Integer selectedForumId = (Integer)map.get("forumId");
			forumId = selectedForumId.longValue();
		}
		
		Topic delTopic = new TopicImpl(getUser());
		delTopic.setTopicId(topicId);
		delTopic.setForumId(forumId);
		topicManager.deleteTopic(delTopic);
		
		Forum forum = new ForumImpl();
		forum.setForumId(delTopic.getForumId());
		Integer totalCnt = getTargetTopicCount();
		forum.setTotalCnt(totalCnt.longValue());
		forumManager.updateForumAfterTopicAdd(forum);
		
		return success();
	}
	
	//public void increase
	
	@Override
    public String execute() throws Exception {  
        return success();
    }
	
	public List<Forum> getTargetForums() throws WebSiteNotFoundException{
		
		
		//StringBuffer url = request.getRequestURL();
		
		WebSite nowSite = getWebSite(); //webSiteManager.getWebSiteByUrl(url.toString());
		
		long objectId = nowSite.getWebSiteId();
		log.debug("objectId === " + objectId);
		List<Forum> forumList = forumManager.getForums(30, objectId); //리스트
		for(int i= 0; i < forumList.size(); i++){
			Forum forum = forumList.get(i);
			log.debug("forumId : " + forum.getForumId());
			log.debug("forumName : " + forum.getBoardName());
		}
		//return getForumList();
		return  forumList;
	}
	
	public int getTargetForumsCount() throws WebSiteNotFoundException{
		
		WebSite nowSite = getWebSite(); 
		long objectId = nowSite.getWebSiteId();
		return forumManager.getForumCount(30, objectId); 
	}
}
