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
package architecture.ee.web.community.page;

public class DefaultBodyContent implements BodyContent {

	private long bodyId;
	
	private long pageId;
	
	private BodyType bodyType;
	
	private String bodyText;
	
	public DefaultBodyContent() {
	}
	
	

	/**
	 * @return bodyText
	 */
	public String getBodyText() {
		return bodyText;
	}



	/**
	 * @param bodyText 설정할 bodyText
	 */
	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
	}



	/**
	 * @return bodyId
	 */
	public long getBodyId() {
		return bodyId;
	}

	/**
	 * @param bodyId 설정할 bodyId
	 */
	public void setBodyId(long bodyId) {
		this.bodyId = bodyId;
	}

	/**
	 * @return pageId
	 */
	public long getPageId() {
		return pageId;
	}

	/**
	 * @param pageId 설정할 pageId
	 */
	public void setPageId(long pageId) {
		this.pageId = pageId;
	}

	/**
	 * @return bodyType
	 */
	public BodyType getBodyType() {
		return bodyType;
	}

	/**
	 * @param bodyType 설정할 bodyType
	 */
	public void setBodyType(BodyType bodyType) {
		this.bodyType = bodyType;
	}

	
}
