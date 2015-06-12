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

import java.util.LinkedList;
import java.util.List;

import architecture.common.user.User;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.exception.NotFoundException;

public class DefaultPollManager implements PollManager {

	private final LinkedList insertQueue = new LinkedList();
	
	public DefaultPollManager() {
		// TODO 자동 생성된 생성자 스텁
	}

	public Poll createPoll(int objectType, long objectId, User user, String name) {
		DefaultPoll poll = new DefaultPoll(objectType, objectId, user, name);
		// insert into db
		// pollCache.put(Long.valueOf(poll.getID()), poll);
		// PollEvent event = new PollEvent(30, poll, poll.getJiveContainer(),
		// Collections.emptyMap());
		// PollEventDispatcher.getInstance().dispatchEvent(event);
		return poll;
	}
    

	@Override
	public void deletePoll(Poll poll) throws UnAuthorizedException,
			PollException {
		if (poll == null)
			throw new IllegalArgumentException("Cannot delete null poll");
		
	}

	@Override
	public void deleteUserPolls(User user) throws UnAuthorizedException,
			PollException {
		// TODO 자동 생성된 메소드 스텁
		
	}

	@Override
	public int getPollCount() {
		// TODO 자동 생성된 메소드 스텁
		return 0;
	}

	@Override
	public List getPolls() {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	@Override
	public int getPollCount(int objectType, long objectId) throws UnAuthorizedException {
		// TODO 자동 생성된 메소드 스텁
		return 0;
	}

	@Override
	public List getPolls(int objectType, long objectId) throws UnAuthorizedException {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	@Override
	public List getActivePolls() {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	@Override
	public int getActivePollCount() {
		// TODO 자동 생성된 메소드 스텁
		return 0;
	}

	@Override
	public List getActivePolls(int objectType, long objectId) throws UnAuthorizedException {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	@Override
	public int getActivePollCount(int objectType, long objectId) {
		// TODO 자동 생성된 메소드 스텁
		return 0;
	}

	@Override
	public List getLivePolls() {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	@Override
	public int getLivePollCount() {
		// TODO 자동 생성된 메소드 스텁
		return 0;
	}

	@Override
	public List getLivePolls(int objectType, long objectId) throws UnAuthorizedException {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	@Override
	public int getLivePollCount(int objectType, long objectId) throws UnAuthorizedException {
		// TODO 자동 생성된 메소드 스텁
		return 0;
	}

	@Override
	public Poll getPoll(long pollId) throws UnAuthorizedException, NotFoundException {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

}
