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
package architecture.ee.web.community.poll.dao;

import java.util.List;

import architecture.common.user.User;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.community.poll.Poll;
import architecture.ee.web.community.poll.PollOption;

public interface PollDao {

	public abstract Poll createPoll( Poll  poll);
	
	public abstract Poll updatePoll( Poll poll);
	
	public abstract Poll deletePoll( Poll poll);
	
	public abstract	Poll getPollById(long pollId) throws NotFoundException;
		
	public abstract  int getPollCount();
	
	public abstract  int getPollCount(int objectType, long objectId);
	
	public abstract  int getPollCount(User user);
	
	public abstract List<Long> getPollIds();
		
	public abstract List<Long> getPollIds(int objectType, long objectId);	
	
	public abstract List<Long> getPollIds(User user);	
	
	public abstract List<PollOption> getPollOptions(Poll poll);
	
	public abstract void updatePollOptions(Poll poll, List<PollOption> options);
	
	public abstract void deletePollOptions(Poll poll, List<PollOption> options);
	
}
