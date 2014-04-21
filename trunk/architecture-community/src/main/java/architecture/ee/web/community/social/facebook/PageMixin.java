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
package architecture.ee.web.community.social.facebook;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class PageMixin {

	@JsonCreator
	PageMixin(
			@JsonProperty("id") String id, 
			@JsonProperty("name") String name, 
			@JsonProperty("link") String location,
			@JsonProperty("category") String category) {}

	@JsonProperty("description")
	String description;
	
	@JsonProperty("location")
	Location location;

	@JsonProperty("website")
	String website;
	
	@JsonProperty("picture")
	@JsonDeserialize(using=PictureDeserializer.class)
	String picture;
	
	@JsonProperty("cover")
	CoverPhoto cover;

	@JsonProperty("phone")
	String phone;

	@JsonProperty("affiliation")
	String affiliation;
	
	@JsonProperty("company_overview")
	String companyOverview;

	@JsonProperty("fan_count")
	int fanCount;

	@JsonProperty("likes")
	int likes;
	
	@JsonProperty("talking_about_count")
	int talkingAboutCount;

	@JsonProperty("checkins")
	int checkins;
	
	@JsonProperty("can_post")
	boolean canPost;
	
	@JsonProperty("is_published")
	private boolean isPublished;
	
	@JsonProperty("is_community_page")
	private boolean isCommunityPage;
	
	@JsonProperty("has_added_app")
	private boolean hasAddedApp;

	@JsonProperty("hours")
	private Map<String, String> hours;

}
