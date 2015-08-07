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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.community.page.Page;
import architecture.ee.web.community.poll.dao.PollDao;

public class DefaultPollManager implements PollManager, EventSource {

	private Log log = LogFactory.getLog(getClass());
	
	private final LinkedList insertQueue = new LinkedList();
	
	private Cache pollCache;
	
	private EventPublisher eventPublisher;
		
	private PollDao pollDao;
	
	private UserManager userManager;
	
	public DefaultPollManager() {
	}

	
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
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

	 @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
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
	

	 @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void updatePoll(Poll poll) throws NotFoundException {
		pollDao.updatePoll(poll);
		pollCache.remove(poll.getPollId());
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

	public Poll getPoll(long pollId) throws UnAuthorizedException, NotFoundException {
		Poll poll = getPollCacheById(pollId);
		if(poll == null){
			poll = pollDao.getPollById(pollId);
			setUserInPoll(poll);
			poll.setOptions( pollDao.getPollOptions(poll));
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

	@Override
	public int getPollCount(int objectType, long objectId) {
		return pollDao.getPollCount(objectType, objectId);
	}

	@Override
	public int getPollCount(User user) {
		return pollDao.getPollCount(user);
	}

	@Override
	public List<Poll> getPolls(int objectType, long objectId) {
		List<Long> ids = pollDao.getPollIds(objectType, objectId);
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

	@Override
	public List<Poll> getPolls(User user) {
		List<Long> ids = pollDao.getPollIds(user);
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

	@Override
	public List<PollOption> getPollOptions(Poll poll) throws NotFoundException {
		Poll target = getPoll(poll.getPollId());
		return target.getOptions();
	}


	protected void setUserInPoll(Poll poll){
		long userId = poll.getUser().getUserId();
		try {
			((DefaultPoll)poll).setUser(userManager.getUser(userId));
		} catch (UserNotFoundException e) {
		}		
	}

	 @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void setPollOptions(Poll poll, List<PollOption> options) {
		try {
			pollDao.updatePollOptions(poll, options);
			updatePoll(poll);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		
	}

	 @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void deletePollOptions(Poll poll, List<PollOption> options) {
		try {
			pollDao.deletePollOptions(poll, options);
			updatePoll(poll);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}



	@Override
	public int getVoteCount(Poll poll) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public int getVoteCount(Poll poll, long optionId) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public int getUserVoteCount(Poll poll) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public int getUserVoteCount(Poll poll, long optionId) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public int getAnomymousVoteCount(Poll poll) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public int getAnomymousVoteCount(Poll poll, long optionId) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public List<Vote> getUserVotes(Poll poll) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<Vote> getAnomymousVotes(Poll poll) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<Vote> getUserVotes(Poll poll, long optionId) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<Vote> getAnomymousVotes(Poll poll, long optionId) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void addUserVote(Poll poll, long optionId, User user) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void addUserVote(Poll poll, long optionId, User user, Date voteDate) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean hasUserVoted(Poll poll, User user) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean hasAnomyouseVoted(Poll poll, String username) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void addAnomymousVote(Poll poll, long optionId, String username) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void addAnomymousVote(Poll poll, long optionId, String username, Date voteDate) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public List<PollOption> getUserVotes(Poll poll, User user) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<PollOption> getAnomymousVotes(Poll poll, String username) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
