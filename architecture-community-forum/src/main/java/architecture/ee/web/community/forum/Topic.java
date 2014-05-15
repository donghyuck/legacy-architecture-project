/*
 * Copyright 2012, 2013 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.ee.web.community.forum;

import architecture.common.cache.Cacheable;
import architecture.common.model.NoNamedEntityModelObject;
import architecture.common.user.User;

public interface Topic  extends NoNamedEntityModelObject  {
	
	public Long getForumId() ;
	
	public Long getTopicId();
	
	public Long getUserId() ;
	
	public int getTotalReplies() ;
	
	public String getSubject() ;
	
	public String getContent() ;
	
	public String getPasswd() ;
	
	public Long getAttachmentId() ;
	
	public Long getViewCnt() ;
	
	public Long getModifyId() ;
	
	public Long getParentThreadId() ;
	
	public int getReplyDepth() ;
	
	public boolean isDelYn() ;
	
	public String getCreateName() ;
	
	public void setTopicId(Long topicId);
	
	public void setForumId(Long forumId) ;
	
	public void setUserId(Long userId);
	
	public void setTotalReplies(int totalReplies);
	
	public void setSubject(String subject) ;
	
	public void setContent(String content) ;
	
	public void setPasswd(String passwd) ;
	
	public void setAttachmentId(Long attachmentId) ;
	
	public void setViewCnt(Long viewCnt) ;
	
	public void setModifyId(Long modifyId) ;
	
	public void setParentThreadId(Long parentThreadId) ;
	
	public void setReplyDepth(int replyDepth) ;
	
	public void setDelYn(boolean delYn) ;
	
	public void setCreateName(String createName) ;
	
	public void setUser(User user);
	
	public User getUser() ;
}
