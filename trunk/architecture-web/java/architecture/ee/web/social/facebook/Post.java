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
package architecture.ee.web.social.facebook;

import java.util.Collections;
import java.util.Date;
import java.util.List;


public class Post {
	
	private final String id;

	private final Reference from;

	private final Date createdTime;

	private final Date updatedTime;

	private List<Reference> to;
	
	private String message;
	
	private String picture;
	
	private String link;
		
	private String name;
	
	private String caption;
	
	private String description;
	
	private String icon;
	
	private Reference application;
	
	private PostType type;
	
	private int likeCount;

	private List<Comment> comments;

	public Post(String id, Reference from, Date createdTime, Date updatedTime) {
		this.id = id;
		this.from = from;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

	public String getId() {
		return id;
	}

	public Reference getFrom() {
		return from;
	}

	public List<Reference> getTo() {
		return to;
	}

	public String getCaption() {
		return caption;
	}

	public String getMessage() {
		return message;
	}

	/**
	 * The page's picture.
	 * @deprecated This method will be replaced in Spring 1.1.0 with a new version that returns an object with more details about the picture.
	 */
	@Deprecated
	public String getPicture() {
		return picture;
	}

	public String getLink() {
		return link;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getIcon() {
		return icon;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public Reference getApplication() {
		return application;
	}

	public PostType getType() {
		return type;
	}
	
	public int getLikeCount() {
		return likeCount;
	}

	/**
	 * The most recent comments for the post.
	 */
	public List<Comment> getComments() {
		if (comments != null) {
			return comments;
		} else {
			return Collections.emptyList();
		}
	}

	public static enum PostType { POST, CHECKIN, LINK, NOTE, PHOTO, STATUS, VIDEO, SWF, MUSIC }
	
}

