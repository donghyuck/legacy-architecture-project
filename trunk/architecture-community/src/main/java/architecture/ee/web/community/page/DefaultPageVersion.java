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

import java.util.Calendar;
import java.util.Date;

import architecture.common.user.User;

public class DefaultPageVersion implements PageVersion {

	private int versionNumber;	
	private PageState pageState;
	private Date creationDate;
	private Date modifiedDate;
	private Page page;
	private User user;
	
	public DefaultPageVersion() {
		this.versionNumber = -1;
		this.pageState = PageState.INCOMPLETE;
		this.creationDate = Calendar.getInstance().getTime();
		this.modifiedDate = this.creationDate;
	}

	
	/**
	 * @return page
	 */
	public Page getPage() {
		return page;
	}


	/**
	 * @param page 설정할 page
	 */
	public void setPage(Page page) {
		this.page = page;
	}


	/**
	 * @return versionNumber
	 */
	public int getVersionNumber() {
		return versionNumber;
	}

	/**
	 * @param versionNumber 설정할 versionNumber
	 */
	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}

	/**
	 * @return pageState
	 */
	public PageState getPageState() {
		return pageState;
	}

	/**
	 * @param pageState 설정할 pageState
	 */
	public void setPageState(PageState pageState) {
		this.pageState = pageState;
	}

	/**
	 * @return creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate 설정할 creationDate
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate 설정할 modifiedDate
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}


	public User getAuthor() {
		// TODO 자동 생성된 메소드 스텁
		return user;
	}


	public void setAuthor(User author) {
		this.user = author;	
	}

	
}
