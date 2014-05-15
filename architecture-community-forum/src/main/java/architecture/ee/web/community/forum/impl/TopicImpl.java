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
package architecture.ee.web.community.forum.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.BaseModelObjectSupport;
import architecture.common.user.User;
import architecture.ee.web.attachment.Attachment;
import architecture.ee.web.community.forum.Topic;

public class TopicImpl extends BaseModelObjectSupport  implements Topic {
	
	private Long ForumId;	// BOARD_ID
	private Long topicId; // THREAD_ID
	private Long userId ; // CREATE_ID
	private int totalReplies = 0 ; // COMMENT_CNT
	private String  subject  ; // THREAD_TITLE
	private String content; // THREAD_CONTENT
	private String passwd; // PASSWD
	private Long attachmentId; // ATTACHMENT_ID
	private Long viewCnt;	// VIEW_CNT
	private Long modifyId; // MODIFY_ID
	private Long parentThreadId; // PARENT_THREAD_ID
	private int replyDepth; // REPLY_DEPTH
	private boolean delYn; // DEL_YN
	private String createName; // CREATE_NAME
	private User user;
	
	/** constructors  **/
	
	public TopicImpl() {
		topicId = -1L;  // noNull values
		subject = "";
	}
	
	public TopicImpl(Long topicId) {
		this.topicId = topicId;
		subject = "";
	}
	
	public TopicImpl(User user){
		this.user = user;
		userId = user == null ? -1L : user.getUserId();
	}

	/* guess object type is not necessary
	public AnnounceImpl(Long announceId, int objectType, Long objectId, User user) {
		this.announceId = announceId;
		this.objectType = objectType;
		this.objectId = objectId;
		this.user = user;
		subject = "";
		attachments = new ArrayList<Attachment>();
		Date now = new Date();
		startDate = now;
		endDate = now;
		userId = user == null ? -1L : user.getUserId();				
		super.setCreationDate(now);
		super.setModifiedDate(now);
	}
	*/
	/** constructors  **/
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getForumId() {
		return ForumId;
	}

	public void setForumId(Long forumId) {
		ForumId = forumId;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getTotalReplies() {
		return totalReplies;
	}

	public void setTotalReplies(int totalReplies) {
		this.totalReplies = totalReplies;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public Long getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
	}

	public Long getViewCnt() {
		return viewCnt;
	}

	public void setViewCnt(Long viewCnt) {
		this.viewCnt = viewCnt;
	}

	public Long getModifyId() {
		return modifyId;
	}

	public void setModifyId(Long modifyId) {
		this.modifyId = modifyId;
	}

	public Long getParentThreadId() {
		return parentThreadId;
	}

	public void setParentThreadId(Long parentThreadId) {
		this.parentThreadId = parentThreadId;
	}

	public int getReplyDepth() {
		return replyDepth;
	}

	public void setReplyDepth(int replyDepth) {
		this.replyDepth = replyDepth;
	}

	public boolean isDelYn() {
		return delYn;
	}

	public void setDelYn(boolean delYn) {
		this.delYn = delYn;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Serializable getPrimaryKeyObject() {
		return topicId;
	}

	public int getModelObjectType() {
		return ModelTypeFactory.getTypeIdFromCode("FORUM_TOPIC");
	}

	public int getCachedSize() {
		return 0;
	}

	
}
