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

import java.io.Serializable;

public class BlogInfo  implements Serializable  {

	private String title; // – string: the title of the blog
	private int posts;	
	private String name; // – string: the short name of the blog
	private long updated;
	private String description;
	private int queue;
	private boolean ask ;
	private boolean askAnon;
	private int likes ;
	
	private boolean shareLikes;
		
	private String url; // – string: the URL of the blog
	private boolean primary; // – boolean: indicates if this is the user's primary blog
	private boolean admin;
	private int followers; // – number: total count of followers for this blog

	private String type; //  – indicates whether a blog is public or private

	private int drafts;
	private int messages;
	private boolean facebookOpengraphEnabled;
	private boolean tweet; // – number: indicate if posts are tweeted auto, Y, N
	private boolean facebook; // – indicate if posts are sent to facebook Y, N
	
	/**
	 * @return shareLikes
	 */
	public boolean isShareLikes() {
		return shareLikes;
	}
	/**
	 * @param shareLikes 설정할 shareLikes
	 */
	public void setShareLikes(boolean shareLikes) {
		this.shareLikes = shareLikes;
	}
	/**
	 * @return queue
	 */
	public int getQueue() {
		return queue;
	}
	/**
	 * @param queue 설정할 queue
	 */
	public void setQueue(int queue) {
		this.queue = queue;
	}
	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title 설정할 title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return posts
	 */
	public int getPosts() {
		return posts;
	}
	/**
	 * @param posts 설정할 posts
	 */
	public void setPosts(int posts) {
		this.posts = posts;
	}
	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name 설정할 name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return updated
	 */
	public long getUpdated() {
		return updated;
	}
	/**
	 * @param updated 설정할 updated
	 */
	public void setUpdated(long updated) {
		this.updated = updated;
	}
	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description 설정할 description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return ask
	 */
	public boolean isAsk() {
		return ask;
	}
	/**
	 * @param ask 설정할 ask
	 */
	public void setAsk(boolean ask) {
		this.ask = ask;
	}
	/**
	 * @return askAnon
	 */
	public boolean isAskAnon() {
		return askAnon;
	}
	/**
	 * @param askAnon 설정할 askAnon
	 */
	public void setAskAnon(boolean askAnon) {
		this.askAnon = askAnon;
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
	 * @return url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url 설정할 url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return primary
	 */
	public boolean isPrimary() {
		return primary;
	}
	/**
	 * @param primary 설정할 primary
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
	/**
	 * @return followers
	 */
	public int getFollowers() {
		return followers;
	}
	/**
	 * @param followers 설정할 followers
	 */
	public void setFollowers(int followers) {
		this.followers = followers;
	}
	
	/**
	 * @return tweet
	 */
	public boolean isTweet() {
		return tweet;
	}
	/**
	 * @param tweet 설정할 tweet
	 */
	public void setTweet(boolean tweet) {
		this.tweet = tweet;
	}
	/**
	 * @return facebook
	 */
	public boolean isFacebook() {
		return facebook;
	}
	/**
	 * @param facebook 설정할 facebook
	 */
	public void setFacebook(boolean facebook) {
		this.facebook = facebook;
	}
	/**
	 * @return type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type 설정할 type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	
	/**
	 * @return admin
	 */
	public boolean isAdmin() {
		return admin;
	}
	/**
	 * @param admin 설정할 admin
	 */
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	/**
	 * @return facebookOpengraphEnabled
	 */
	public boolean isFacebookOpengraphEnabled() {
		return facebookOpengraphEnabled;
	}
	/**
	 * @param facebookOpengraphEnabled 설정할 facebookOpengraphEnabled
	 */
	public void setFacebookOpengraphEnabled(boolean facebookOpengraphEnabled) {
		this.facebookOpengraphEnabled = facebookOpengraphEnabled;
	}
	
	/**
	 * @return drafts
	 */
	public int getDrafts() {
		return drafts;
	}
	/**
	 * @param drafts 설정할 drafts
	 */
	public void setDrafts(int drafts) {
		this.drafts = drafts;
	}
	/**
	 * @return messages
	 */
	public int getMessages() {
		return messages;
	}
	/**
	 * @param messages 설정할 messages
	 */
	public void setMessages(int messages) {
		this.messages = messages;
	}
	/* (비Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BlogInfo [");
		if (title != null)
			builder.append("title=").append(title).append(", ");
		builder.append("posts=").append(posts).append(", ");
		if (name != null)
			builder.append("name=").append(name).append(", ");
		builder.append("updated=").append(updated).append(", ");
		if (description != null)
			builder.append("description=").append(description).append(", ");
		builder.append("queue=").append(queue).append(", ask=").append(ask)
				.append(", askAnon=").append(askAnon).append(", likes=")
				.append(likes).append(", shareLikes=").append(shareLikes)
				.append(", ");
		if (url != null)
			builder.append("url=").append(url).append(", ");
		builder.append("primary=").append(primary).append(", admin=")
				.append(admin).append(", followers=").append(followers)
				.append(", ");
		if (type != null)
			builder.append("type=").append(type).append(", ");
		builder.append("drafts=").append(drafts).append(", messages=")
				.append(messages).append(", facebookOpengraphEnabled=")
				.append(facebookOpengraphEnabled).append(", tweet=")
				.append(tweet).append(", facebook=").append(facebook)
				.append("]");
		return builder.toString();
	}
	
	
    
}
