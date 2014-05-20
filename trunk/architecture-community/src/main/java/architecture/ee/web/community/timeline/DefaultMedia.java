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
package architecture.ee.web.community.timeline;

import architecture.common.cache.CacheSizes;

public class DefaultMedia implements Media{

	private String url;
	private String thumbnailUrl;
	private String caption;
	private String credit;
	
	public DefaultMedia() {
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
	 * @return thumbnailUrl
	 */
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	/**
	 * @param thumbnailUrl 설정할 thumbnailUrl
	 */
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	/**
	 * @return caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param caption 설정할 caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * @return credit
	 */
	public String getCredit() {
		return credit;
	}

	/**
	 * @param credit 설정할 credit
	 */
	public void setCredit(String credit) {
		this.credit = credit;
	}

	public int getCachedSize() {
		return CacheSizes.sizeOfString(this.url)
				+ CacheSizes.sizeOfString(this.thumbnailUrl)
				+ CacheSizes.sizeOfString(this.caption)
				+ CacheSizes.sizeOfString(this.credit);
	}

}
