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
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.primitives.ArrayLongList;
import org.apache.commons.collections.primitives.LongList;
import org.apache.commons.lang.StringUtils;
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
import architecture.ee.web.community.poll.dao.PollDao;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class DefaultPollManager implements PollManager, EventSource {

	
	private static final Log log = LogFactory.getLog(DefaultPollManager.class);
	
	private final LinkedList<Vote> insertQueue = new LinkedList<Vote>();
	
	private Cache pollCache;
	
	private Cache voteCache;
	
	private EventPublisher eventPublisher;
		
	private PollDao pollDao;
	
	private UserManager userManager;
	
	public DefaultPollManager() {
	}

	public LinkedList<Vote> getVoteQueue(){
		return insertQueue;
	}	

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}



	public Cache getVoteCache() {
		return voteCache;
	}

	public void setVoteCache(Cache voteCache) {
		this.voteCache = voteCache;
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	public Cache getPollCache() {
		return pollCache;
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

	public int getVoteCount(Poll poll) {
		List<Vote> votes = getVotes(poll);
		return votes.size();
	}

	public int getVoteCount(Poll poll, long optionId) {
		List<Vote> votes = getVotes(poll);
		int count = 0;
		for(Vote vote : votes){
			if(vote.getOptionId() == optionId)
				count ++ ;
		}
		return count;
	}

	public int getUserVoteCount(Poll poll) {
		List<Vote> votes = getVotes(poll);
		int count = 0;
		for(Vote vote : votes){
			if(vote.getUserId() > 0)
				count ++ ;
		}
		return count;
	}

	public int getUserVoteCount(Poll poll, long optionId) {
		List<Vote> votes = getVotes(poll);
		int count = 0;
		for(Vote vote : votes){
			if(vote.getOptionId() == optionId && vote.getUserId() > 0)
				count ++ ;
		}
		return count;
	}

	public int getAnomymousVoteCount(Poll poll) {
		List<Vote> votes = getVotes(poll);
		int count = 0;
		for(Vote vote : votes){
			if(vote.getUserId() == -1L)
				count ++ ;
		}
		return count;
	}

	public int getAnomymousVoteCount(Poll poll, long optionId) {
		List<Vote> votes = getVotes(poll);
		int count = 0;
		for(Vote vote : votes){
			if(vote.getOptionId() == optionId &&  vote.getUserId() == -1L)
				count ++ ;
		}
		return count;
	}

	
	private List<Vote> getVotesFromCache(Poll poll){
		if( voteCache.get(poll.getPollId()) != null){
			return (List<Vote>)voteCache.get(poll.getPollId()).getValue();
		}else{
			return null;
		} 
	}
	
	private List<Vote> loadVotes(Poll poll){
		List<Vote> votes = pollDao.getVotes(poll);
		voteCache.put(new Element(poll.getPollId(), votes));
		return votes;
	}
	
	public List<Vote> getVotes(Poll poll){
		List<Vote> votes =getVotesFromCache(poll);
		if(votes == null)
			votes = loadVotes(poll);
		return votes;
	}
	
	public List<User> getUserVotes(Poll poll) {
		List<Vote> votes = getVotes(poll);
		LongList list = new ArrayLongList();
		for(Vote vote : votes){
			if( vote.getUserId() != -1L )
				list.add(vote.getUserId());
		}
		return toUserList(list);
	}
	
	private List<User> toUserList(LongList userIds ){
		List<User> users = new ArrayList<User>();
		for(long userId : userIds.toArray()){
			try {
				users.add(userManager.getUser(userId));
			} catch (UserNotFoundException e) {
			}
		}
		return users;
	}
	

	public List<String> getAnomymousVotes(Poll poll) {
		List<Vote> votes = getVotes(poll);
		List<String> list = new ArrayList<String>();
		for(Vote vote : votes){
			if( vote.getUserId() == -1L )
				list.add(vote.getUniqueId());
		}
		return list;
	}

	public List<User> getUserVotes(Poll poll, long optionId) {
		List<Vote> votes = getVotes(poll);
		LongList list = new ArrayLongList();
		for(Vote vote : votes){
			if( optionId == vote.getOptionId() && vote.getUserId() != -1L )
				list.add(vote.getUserId());
		}
		return toUserList(list);
	}

	public List<String> getAnomymousVotes(Poll poll, long optionId) {
		List<Vote> votes = getVotes(poll);
		List<String> list = new ArrayList<String>();
		for(Vote vote : votes){
			if( optionId == vote.getOptionId() && vote.getUserId() == -1L )
				list.add(vote.getUniqueId());
		}
		return list;
	}

	public void addUserVote(Poll poll, long optionId, User user, String IPAddress) throws PollException {
		boolean optionExist = false;
		for( PollOption option : poll.getOptions()) {
			if( option.getOptionId() == optionId ){
				optionExist = true;
				break;
			}
		}
		if(!optionExist)
			throw new IllegalArgumentException("option is not valid.");
		if(user == null)
			throw new IllegalArgumentException("user cannot be null.");
		
		Date now = new Date();
		if(poll.getStartDate().compareTo(now)>0 || poll.getEndDate().compareTo(now) < 0 ){
			throw new PollException("Cannot add vote : Poll is not active");
		}
		
		Vote vote = new Vote(optionId, user.getUserId(), null, IPAddress, new Date() );
		List<Vote> votes = getVotes(poll);
		if( (poll.getMode() & Poll.MULTIPLE_SELECTIONS_ALLOWED) == 0L) {
			for(Vote v : votes){
				if( v.getUserId() == user.getUserId() ){
					throw new PollException("Cannot not add vote : MULTIPLE_SELECTIONS_ALLOWED is not enabled");
				}
			}
		}

		for(Vote v : votes){
			if( v.getUserId() == user.getUserId() && now.getTime() - v.getVoteDate().getTime() <= 2000L  ){
				throw new PollException("Cannot not add vote : Existing votes are older then 2 seconds");
			}
		}		
		votes.add(vote);
		voteCache.put(new Element(poll.getPollId(), votes));
		getVoteQueue().add(vote);		
	}

	public void addUserVote(Poll poll, long optionId, User user, String IPAddress, Date voteDate) throws PollException {
		boolean optionExist = false;
		for( PollOption option : poll.getOptions()) {
			if( option.getOptionId() == optionId ){
				optionExist = true;
				break;
			}
		}
		if(!optionExist)
			throw new IllegalArgumentException("option is not valid.");
		if(user == null)
			throw new IllegalArgumentException("user cannot be null.");
		
		Date now = new Date();
		if(poll.getStartDate().compareTo(now)>0 || poll.getEndDate().compareTo(now) < 0 ){
			throw new PollException("Cannot add vote : Poll is not active");
		}
		
		Vote vote = new Vote(optionId, user.getUserId(), null, IPAddress, voteDate );
		
		List<Vote> votes = getVotes(poll);
		if( (poll.getMode() & Poll.MULTIPLE_SELECTIONS_ALLOWED) == 0L) {
			for(Vote v : votes){
				if( v.getUserId() == user.getUserId() ){
					throw new PollException("Cannot not add vote : MULTIPLE_SELECTIONS_ALLOWED is not enabled");
				}
			}
		}

		for(Vote v : votes){
			if( v.getUserId() == user.getUserId() && now.getTime() - v.getVoteDate().getTime() <= 2000L  ){
				throw new PollException("Cannot not add vote : Existing votes are older then 2 seconds");
			}
		}
		
		votes.add(vote);
		voteCache.put(new Element(poll.getPollId(), votes));
		getVoteQueue().add(vote);		
	}

	public boolean hasUserVoted(Poll poll, User user) {
		if( user == null)
			throw new IllegalArgumentException("user cannot be null.");
		List<Vote> votes = getVotes(poll);
		for(Vote vote : votes)
		{
			if( vote.getUserId() == user.getUserId())
				return true;
		}
		
		return false;
	}

	public boolean hasAnomyouseVoted(Poll poll, String username) {
		if( username == null)
			throw new IllegalArgumentException("user unique name be null.");
		List<Vote> votes = getVotes(poll);
		for(Vote vote : votes)
		{
			if( username.equals(vote.getUniqueId()))
				return true;
		}		
		return false;
	}

	public void addAnomymousVote(Poll poll, long optionId, String username, String IPAddress) throws PollException {
		boolean optionExist = false;
		for( PollOption option : poll.getOptions()) {
			if( option.getOptionId() == optionId ){
				optionExist = true;
				break;
			}
		}
		if(!optionExist)
			throw new IllegalArgumentException("option is not valid.");
		if(StringUtils.isEmpty(username))
			throw new IllegalArgumentException("unique Id cannot be null.");		
		Date now = new Date();
		if(poll.getStartDate().compareTo(now)>0 || poll.getEndDate().compareTo(now) < 0 ){
			throw new PollException("Cannot add vote : Poll is not active");
		}		
		Vote vote = new Vote(optionId, -1L, username, IPAddress, new Date());
		List<Vote> votes = getVotes(poll);
		if( (poll.getMode() & Poll.MULTIPLE_SELECTIONS_ALLOWED) == 0L) {
			for(Vote v : votes){
				if( username.equals(v.getUniqueId()) ){
					throw new PollException("Cannot not add vote : MULTIPLE_SELECTIONS_ALLOWED is not enabled");
				}
			}
		}	
		votes.add(vote);
		voteCache.put(new Element(poll.getPollId(), votes));
		getVoteQueue().add(vote);		
	}


	public void addAnomymousVote(Poll poll, long optionId, String username, String IPAddress, Date voteDate) throws PollException {
		boolean optionExist = false;
		for( PollOption option : poll.getOptions()) {
			if( option.getOptionId() == optionId ){
				optionExist = true;
				break;
			}
		}
		if(!optionExist)
			throw new IllegalArgumentException("option is not valid.");
		if(StringUtils.isEmpty(username))
			throw new IllegalArgumentException("unique Id cannot be null.");
		
		Date now = new Date();
		if(poll.getStartDate().compareTo(now)>0 || poll.getEndDate().compareTo(now) < 0 ){
			throw new PollException("Cannot add vote : Poll is not active");
		}
		
		Vote vote = new Vote(optionId, -1L, username, IPAddress, voteDate);
		List<Vote> votes = getVotes(poll);
		if( (poll.getMode() & Poll.MULTIPLE_SELECTIONS_ALLOWED) == 0L) {
			for(Vote v : votes){
				if( username.equals(v.getUniqueId()) ){
					throw new PollException("Cannot not add vote : MULTIPLE_SELECTIONS_ALLOWED is not enabled");
				}
			}
		}	
		votes.add(vote);
		voteCache.put(new Element(poll.getPollId(), votes));
		getVoteQueue().add(vote);			
	}


	public List<PollOption> getUserVotes(Poll poll, User user) {
		if(user == null)
			throw new IllegalArgumentException("user cannot be null.");
		List<Vote> votes = getVotes(poll);
		LongList list = new ArrayLongList();
		List<PollOption> options = new ArrayList<PollOption>();
		
		for(Vote vote : votes){
			if(vote.getUserId() == user.getUserId() && !list.contains(vote.getOptionId()) )
			{
				list.add(vote.getOptionId());
			}
		}
		for(PollOption option : poll.getOptions())
		{
			if(list.contains(option.getOptionId()))
				options.add(option);
		}
		return options;
	}


	public List<PollOption> getAnomymousVotes(Poll poll, String username) {
		if(username == null)
			throw new IllegalArgumentException("user unique id cannot be null.");
		List<Vote> votes = getVotes(poll);
		LongList list = new ArrayLongList();
		List<PollOption> options = new ArrayList<PollOption>();
		for(Vote vote : votes){
			if(username.equals(vote.getUniqueId()) && !list.contains(vote.getOptionId()) )
			{
				list.add(vote.getOptionId());
			}
		}
		for(PollOption option : poll.getOptions())
		{
			if(list.contains(option.getOptionId()))
				options.add(option);
		}
		return options;
	}

	@Override
	public PollStats getPollStats(Poll poll, User user) {
		
		PollStats ps = new PollStats(poll);
		ps.setVoteCount(this.getVoteCount(poll));
		
		if( !user.isAnonymous()){
			ps.setUserVoted(this.hasUserVoted(poll, user));
			ps.setUserVotes(this.getUserVotes(poll, user));
		}else{
			ps.setUserVotes(Collections.EMPTY_LIST);
		}
		
		List<PollOptionStats> list = new ArrayList<PollOptionStats>(ps.getPoll().getOptions().size());
		int totalVoteCount = getVoteCount(poll);
		for(PollOption po : ps.getPoll().getOptions())
		{	
			PollOptionStats pos = new PollOptionStats(po);
			pos.setTotalVoteCount(totalVoteCount);
			pos.setPollOption(po);
			pos.setVoteCount(this.getVoteCount(poll, po.getOptionId()));
			pos.setVoteUsers(this.getUserVotes(poll, po.getOptionId()));
			list.add(pos);
		}
		
		ps.setPollOptionStats(list);
		return ps;
	}
	
}
