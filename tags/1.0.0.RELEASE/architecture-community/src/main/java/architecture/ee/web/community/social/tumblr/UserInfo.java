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
package architecture.ee.web.community.social.tumblr;

import java.util.List;

import architecture.ee.web.community.social.UserProfile;

public class UserInfo  implements UserProfile  {
		
	private String name ;
	private String defaultPostFormat;
	private int likes ;
	private int following ;
	private List<BlogInfo> blogInfos;
	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param following
	 * @param defaultPostFormat
	 * @param name
	 * @param likes
	 * @param blogInfos
	 */
	public UserInfo(int following, String defaultPostFormat, String name, int likes, List<BlogInfo> blogInfos) {
		this.following = following;
		this.defaultPostFormat = defaultPostFormat;
		this.name = name;
		this.likes = likes;
		this.blogInfos = blogInfos;
	}
	/**
	 * @param name 설정할 name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return defaultPostFormat
	 */
	public String getDefaultPostFormat() {
		return defaultPostFormat;
	}
	/**
	 * @param defaultPostFormat 설정할 defaultPostFormat
	 */
	public void setDefaultPostFormat(String defaultPostFormat) {
		this.defaultPostFormat = defaultPostFormat;
	}
	/**
	 * @return likes
	 */
	public int getLikes() {
		return likes;
	}
	/**
	 * @param likes 설정할 likes
	 */
	public void setLikes(int likes) {
		this.likes = likes;
	}
	/**
	 * @return following
	 */
	public int getFollowing() {
		return following;
	}
	/**
	 * @param following 설정할 following
	 */
	public void setFollowing(int following) {
		this.following = following;
	}
	/**
	 * @return blogInfos
	 */
	public List<BlogInfo> getBlogs() {
		return blogInfos;
	}
	/**
	 * @param blogInfos 설정할 blogInfos
	 */
	public void setBlogs(List<BlogInfo> blogInfos) {
		this.blogInfos = blogInfos;
	}
	/* (비Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserInfo [");
		if (name != null)
			builder.append("name=").append(name).append(", ");
		if (defaultPostFormat != null)
			builder.append("defaultPostFormat=").append(defaultPostFormat)
					.append(", ");
		builder.append("likes=").append(likes).append(", following=")
				.append(following).append(", ");
		if (blogInfos != null)
			builder.append("blogInfos=").append(blogInfos);
		builder.append("]");
		return builder.toString();
	}

	public String getPrimaryKeyString() {
		return getName();
	}
}
