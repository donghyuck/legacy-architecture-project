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
package architecture.ee.web.community.poll;

import java.util.Date;

import architecture.common.cache.CacheSizes;
import architecture.common.cache.Cacheable;

public class Vote implements Cacheable {

	private long userId;
	private String uniqueId;
	private long optionId;
	private Date voteDate;
	private String ip;
	


	/**
	 * @param pollId
	 * @param userId
	 * @param uniqueId
	 * @param optionIndex
	 * @param ip
	 */
	protected Vote(long optionId, long userId, String uniqueId,  String ip) {
		this.userId = userId;
		this.uniqueId = uniqueId;
		this.optionId = optionId;
		this.ip = ip;
		this.voteDate = new Date();
	}

	/**
	 * @param pollId
	 * @param userId
	 * @param uniqueId
	 * @param optionIndex
	 * @param ip
	 * @param voteDate
	 */
	public Vote(long optionId, long userId, String uniqueId, String ip, Date voteDate) {
		this.userId = userId;
		this.uniqueId = uniqueId;
		this.optionId = optionId;
		this.ip = ip;
		this.voteDate = voteDate;
	}
	/**
	 * @return ip
	 */
	public String getIPAddress() {
		return ip;
	}
	/**
	 * @param ip 설정할 ip
	 */
	public void setIPAddress(String ip) {
		this.ip = ip;
	}

	/**
	 * @return userId
	 */
	public long getUserId() {
		return userId;
	}




	/**
	 * @param userId 설정할 userId
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}




	/**
	 * @return uniqueId
	 */
	public String getUniqueId() {
		return uniqueId;
	}




	/**
	 * @param uniqueId 설정할 uniqueId
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}







	public long getOptionId() {
		return optionId;
	}

	public void setOptionId(long optionId) {
		this.optionId = optionId;
	}

	/**
	 * @return voteDate
	 */
	public Date getVoteDate() {
		return voteDate;
	}




	/**
	 * @param voteDate 설정할 voteDate
	 */
	public void setVoteDate(Date voteDate) {
		this.voteDate = voteDate;
	}




	@Override
	public int getCachedSize() {
		int size = CacheSizes.sizeOfObject();
		size += CacheSizes.sizeOfLong() * 2;
		size += CacheSizes.sizeOfInt();
		size += CacheSizes.sizeOfDate();
		size += uniqueId == null ? 0 : CacheSizes.sizeOfString(uniqueId);
		return size;
	}

}
