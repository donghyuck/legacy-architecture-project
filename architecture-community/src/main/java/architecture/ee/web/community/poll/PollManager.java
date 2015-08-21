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
import java.util.LinkedList;
import java.util.List;

import architecture.common.user.User;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.exception.NotFoundException;

public interface PollManager {
	
	public abstract LinkedList<Vote> getVoteQueue();
		
	public abstract Poll createPoll(int objectType, long objectId, User user, String name) throws UnAuthorizedException;
	
	public abstract void updatePoll(Poll poll) throws NotFoundException;
	
	public abstract Poll getPoll(long pollId) throws UnAuthorizedException, NotFoundException;
	
	public abstract int getPollCount();
	
	public abstract int getPollCount(int objectType, long objectId);
	
	public abstract int getPollCount(User user);
	
	public abstract List<Poll> getPolls();
	
	public abstract List<Poll> getPolls(int objectType, long objectId);
	
	public abstract List<Poll> getPolls(User user);
	
	public abstract List<PollOption> getPollOptions(Poll poll) throws NotFoundException;
	
	public abstract void setPollOptions(Poll poll, List<PollOption> options);
	
	public abstract void deletePollOptions(Poll poll, List<PollOption> options);
	
	public int getVoteCount(Poll poll) ;

	public int getVoteCount(Poll poll, long optionId) ;

	public int getUserVoteCount(Poll poll) ;

	public int getUserVoteCount(Poll poll, long optionId) ;

	public int getAnomymousVoteCount(Poll poll);

	public int getAnomymousVoteCount(Poll poll, long optionId);
	
	public List<Vote> getVotes(Poll poll);
	
	public List<User> getUserVotes(Poll poll) ;

	public List<String> getAnomymousVotes(Poll poll) ;

	public List<User> getUserVotes(Poll poll, long optionId) ;

	public List<String> getAnomymousVotes(Poll poll, long optionId);

	public void addUserVote(Poll poll, long optionId, User user, String IPAddress) throws PollException ;
	
	public void addUserVote(Poll poll, long optionId, User user, String IPAddress, Date voteDate) throws PollException ;

	public boolean hasUserVoted(Poll poll, User user);

	public boolean hasAnomyouseVoted(Poll poll, String username) ;

	public void addAnomymousVote(Poll poll, long optionId, String username, String IPAddress) throws PollException ;

	public void addAnomymousVote(Poll poll, long optionId, String username, String IPAddress, Date voteDate) throws PollException ;

	public List<PollOption> getUserVotes(Poll poll, User user) ;

	public List<PollOption> getAnomymousVotes(Poll poll, String username) ;
	
	public PollStats getPollStats(Poll poll, User user);
	
	
	/*
	public abstract void deletePoll(Poll poll) throws UnAuthorizedException, PollException;

	public abstract void deleteUserPolls(User user) throws UnAuthorizedException, PollException;




	public abstract int getPollCount(int objectType, long objectId)throws UnAuthorizedException;

	public abstract List<Poll> getPolls(int objectType, long objectId) throws UnAuthorizedException;

	public abstract List<Poll> getActivePolls();

	public abstract int getActivePollCount();

	public abstract List<Poll> getActivePolls(int objectType, long objectId) throws UnAuthorizedException;

	public abstract int getActivePollCount(int objectType, long objectId);

	public abstract List<Poll> getLivePolls();

	public abstract int getLivePollCount();

	public abstract List<Poll> getLivePolls(int objectType, long objectId) throws UnAuthorizedException;

	public abstract int getLivePollCount(int objectType, long objectId) throws UnAuthorizedException;

	
*/
	
}
