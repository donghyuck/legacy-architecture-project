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
package architecture.ee.web.community.social.tumblr.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BlogInfoMixin {

	@JsonProperty("title")
	private String title; // – string: the title of the blog

	@JsonProperty("posts")
	private int posts;

	@JsonProperty("name")
	private String name; // – string: the short name of the blog

	@JsonProperty("updated")
	private long updated;

	@JsonProperty("description")
	private String description;

	@JsonProperty("ask")
	private boolean ask;

	@JsonProperty("ask_anon")
	private boolean askAnon;

	@JsonProperty("likes")
	private int likes;

	@JsonProperty("url")
	private String url; // – string: the URL of the blog

	@JsonProperty("share_likes")
	private boolean shareLikes; // – boolean: indicates if this is the user's // primary blog
								
	@JsonProperty("primary")
	private boolean primary;
	
	@JsonProperty("followers")
	private int followers; // – number: total count of followers for this blog

	@JsonProperty("type")
	private String type; // – indicates whether a blog is public or private

	@JsonProperty("drafts")
	private int drafts; // – number: total count of followers for this blog
	

	@JsonProperty("messages")
	private int messages; // – number: total count of followers for this blog
	
	@JsonProperty("facebook_opengraph_enabled")
	@JsonDeserialize(using = BooleanYNDeserializer.class)
	private boolean facebookOpengraphEnabled;
	
	@JsonProperty("tweet") 
	@JsonDeserialize(using = BooleanYNDeserializer.class)
	boolean tweet;
	
	@JsonProperty("facebook") 
	@JsonDeserialize(using = BooleanYNDeserializer.class)
	boolean facebook;

}
