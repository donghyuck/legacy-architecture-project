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

import java.io.Serializable;

public class PollOption implements Serializable {
	
	private long optionId;
	private String optionText;
	private int optionIndex;
	private long pollId;
	private int voteCount;
	
	public PollOption() {
		optionId = -1L;
		optionIndex = 1;
		pollId = -1L;
		voteCount = 0;
	}

	/**
	 * @param pollId
	 * @param optionId
	 * @param optionText
	 * @param optionIndex
	 */
	public PollOption(long optionId, long pollId, String optionText, int optionIndex) {
		this.pollId = pollId;
		this.optionId = optionId;
		this.optionText = optionText;
		this.optionIndex = optionIndex;
		this.voteCount = 0;
	}

	public PollOption(long pollId, String optionText, int optionIndex) {
		this.pollId = pollId;
		this.optionId = -1L;
		this.optionText = optionText;
		this.optionIndex = optionIndex;
		this.voteCount = 0;
	}
	
	public int getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}

	/**
	 * @return optionId
	 */
	public long getOptionId() {
		return optionId;
	}
	/**
	 * @param optionId 설정할 optionId
	 */
	public void setOptionId(long optionId) {
		this.optionId = optionId;
	}
	/**
	 * @return optionText
	 */
	public String getOptionText() {
		return optionText;
	}
	/**
	 * @param optionText 설정할 optionText
	 */
	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}
	/**
	 * @return optionIndex
	 */
	public int getOptionIndex() {
		return optionIndex;
	}
	/**
	 * @param optionIndex 설정할 optionIndex
	 */
	public void setOptionIndex(int optionIndex) {
		this.optionIndex = optionIndex;
	}
	/**
	 * @return pollId
	 */
	public long getPollId() {
		return pollId;
	}
	/**
	 * @param pollId 설정할 pollId
	 */
	public void setPollId(long pollId) {
		this.pollId = pollId;
	}

	@Override
	public String toString() {
		return "PollOption [optionId=" + optionId + ", optionText=" + optionText + ", optionIndex=" + optionIndex
				+ ", pollId=" + pollId + "]";
	}
	
	
	
}
