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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.common.user.User;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.community.poll.dao.PollDao;

public class DefaultPollManager implements PollManager, EventSource {

	private Log log = LogFactory.getLog(getClass());
	
	private final LinkedList insertQueue = new LinkedList();
	
	private Cache pollCache;
	
	private EventPublisher eventPublisher;
		
	private PollDao pollDao;
	
	
	public DefaultPollManager() {
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	/**
	 * @param pollCache 설정할 pollCache
	 */
	public void setPollCache(Cache pollCache) {
		this.pollCache = pollCache;
	}

	/**
	 * @param pollDao 설정할 pollDao
	 */
	public void setPollDao(PollDao pollDao) {
		this.pollDao = pollDao;
	}

	public Poll createPoll(int objectType, long objectId, User user, String name) {
		
		DefaultPoll poll = new DefaultPoll(objectType, objectId, user, name);
		poll.setMode(0L);
		pollDao.createPoll(poll);
				
		// insert into db
		 pollCache.put(new Element(Long.valueOf(poll.getPollId()), poll));
		 
		// PollEvent event = new PollEvent(30, poll, poll.getJiveContainer(),
		// Collections.emptyMap());
		// PollEventDispatcher.getInstance().dispatchEvent(event);
		return poll;
	}
	
	

	public List<Poll> getPolls() {
		List<Long> ids = pollDao.getPollIds();
		List<Poll> polls = new ArrayList<Poll>();
		for( Long id : ids){
			try {
				polls.add(getPoll(id));
			} catch (Exception e) {
				log.warn(e);
			}
		}		
 		return polls;
	}

	public int getPollCount() {		
		return pollDao.getPollCount();
	}

	public Poll getPoll(long pollId) throws UnAuthorizedException,
			NotFoundException {
		Poll poll = getPollCacheById(pollId);
		if(poll == null){
			poll = pollDao.getPollById(pollId);
			pollCache.put(new Element(poll.getPollId(), poll));
		}
		return poll;
	}
	
	private Poll getPollCacheById( long pollId){
		if(pollCache.get(pollId) != null){
			return (Poll)pollCache.get(pollId).getValue();
		}
		return null;
	}
    

}
