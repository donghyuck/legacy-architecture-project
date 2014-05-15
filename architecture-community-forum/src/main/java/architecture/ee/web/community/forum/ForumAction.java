package architecture.ee.web.community.forum;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import architecture.common.user.authentication.UnAuthorizedException;
import architecture.common.util.DateUtils;
import architecture.ee.web.community.announce.Announce;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;

public class ForumAction extends  FrameworkActionSupport{

	private TopicManager topicManager;
	
	private Long objectId = -1L;
	
	private Integer objectType = 0;
	
	private Long forumId = -1L;
	
	private Long topicId;
	
	public Topic getTargetTopic() throws Exception {

		if(topicId > 0){
			return topicManager.getTopic(topicId);
		}else{
			return topicManager.createTopic(getUser());
		}
	}
	
	public List<Topic> getTargetTopics(){
		log.debug(printParameter(request));
		/*if( objectType == 0 ){
			this.objectType = getCompany().getModelObjectType() ;
			this.objectId = getCompany().getCompanyId();
		}*/
		Map map = ParamUtils.getParametersAsMap(request);
		//Map map = ParamUtils.getJsonParameter(request, "item", Map.class);
		Long forumId = (Long)map.get("forumId");
		
		return topicManager.getTopics(forumId);
	}
		
	public String saveTopic() throws Exception {
		log.debug(printParameter(request));
		Map map = ParamUtils.getParametersAsMap(request);
		//Map map = ParamUtils.getJsonParameter(request, "item", Map.class);
		
		if ( topicId == null){ // ?? 
			Integer selectedTopicId = (Integer)map.get("topicId");
			topicId = selectedTopicId.longValue();
		}
		
		String subject = (String)map.get("subject");
		String body = (String)map.get("content");
		String passwd = (String)map.get("passwd");
		Long attachmentId = (Long)map.get("attachmentId");
		String createName = (String)map.get("createName");
		
		Topic targetTopic = getTargetTopic();
		targetTopic.setSubject(subject);
		targetTopic.setContent(body);
		targetTopic.setPasswd(passwd);
		targetTopic.setAttachmentId(attachmentId);
		targetTopic.setCreateName(createName);
		log.debug(targetTopic);
		
		if(targetTopic.getTopicId() > 0)
		{
			//topicManager.updateTopic(targetTopic);
		}else{
			topicManager.addTopic(targetTopic);
		}
		return success();
	}
	
	/**
	 * request String 반환
	 * @param request
	 * @return
	 */
	public static String printParameter(HttpServletRequest request){
    	StringBuffer sb = new StringBuffer();
    	Enumeration e = request.getParameterNames();

    	sb.append("\n ==================== printParameter ====================");
    	while(e.hasMoreElements()) {
    	    String key   = (String)e.nextElement();
    	    String value = request.getParameter(key);
    	    sb.append("\n ==== "+key+ " : " + value+" ");
    	}
    	sb.append("\n ==================== printParameter ====================\n");

    	return sb.toString();
    }
}
