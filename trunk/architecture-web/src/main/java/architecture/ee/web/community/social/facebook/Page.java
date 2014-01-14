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


public class Page {

	private final String id;

	private final String name;

	private final String category;

	private final String link;

	private String description;

	private String about;

	private Location location;
	
	private String website;
	
	private String picture;

	private CoverPhoto cover;

	private String phone;
	
	private String affiliation;
	
	private String companyOverview;
	
	private int fanCount;
	
	private int likes;
	
	private int talkingAboutCount;

	private int checkins;

	private boolean canPost;
	
	private boolean isPublished;
	
	private boolean isCommunityPage;
	
	private boolean hasAddedApp;
	
	private Map<String, String> hours;
	
	public Page(String id, String name, String link, String category) {
		this.id = id;
		this.name = name;
		this.link = link;
		this.category = category;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String getLink() {
		return link;
	}

	public String getCategory() {
		return category;
	}
	
	public String getDescription() {
		return description;
	}

	public String getAbout() {
		return about;
	}

	public Location getLocation() {
		return location;
	}

	public String getWebsite() {
		return website;
	}

	/**
	 * The page's picture.
	 * @deprecated This method will be replaced in Spring 1.1.0 with a new version that returns an object with more details about the picture.
	 */
	@Deprecated
	public String getPicture() {
		return picture;
	}

	public CoverPhoto getCover() {
		return cover;
	}

	public String getPhone() {
		return phone;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public String getCompanyOverview() {
		return companyOverview;
	}
	
	/**
	 * @deprecated The fan_count property is no longer returned from Facebook's Graph API and so this will always be zero. This property will be removed in Spring Social Facebook 1.1.0.
	 */
	@Deprecated
	public int getFanCount() {
		return fanCount;
	}

	public int getLikes() {
		return likes;
	}

	public int getTalkingAboutCount() {
		return talkingAboutCount;
	}
	
	public int getCheckins() {
		return checkins;
	}

	/**
	 * Indicates whether or not the authenticated user can post on this page.
	 * @return true if the user can post to the page; false otherwise
	 */
	public boolean canPost() {
		return canPost;
	}
	
	/**
	 * @return true if the page has been published; false otherwise.
	 */
	public boolean isPublished() {
		return isPublished;
	}
	
	/**
	 * @return true if the page is a community page; false otherwise.
	 */
	public boolean isCommunityPage() {
		return isCommunityPage;
	}
	
	/**
	 * @return true if the page has added the app making the query in a Page tab; false otherwise.
	 */
	public boolean hasAddedApp() {
		return hasAddedApp;
	}

	public Map<String, String> getHours() {
		return hours;
	}
	
}
