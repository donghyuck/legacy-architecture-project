package architecture.ee.web.community.poll;

import java.io.Serializable;
import java.util.List;

public class PollStats implements Serializable {

    private Poll poll;
    private int voteCount;
    private List<PollOptionStats> pollOptionStats;
    private boolean userVoted;
    private List<PollOption> userVotes;

    public PollStats(Poll poll) {
	super();
	this.poll = poll;
    }

    public Poll getPoll() {
	return poll;
    }

    public boolean isUserVoted() {
	return userVoted;
    }

    public void setUserVoted(boolean userVoted) {
	this.userVoted = userVoted;
    }

    public List<PollOption> getUserVotes() {
	return userVotes;
    }

    public void setUserVotes(List<PollOption> userVotes) {
	this.userVotes = userVotes;
    }

    public void setPoll(Poll poll) {
	this.poll = poll;
    }

    public int getVoteCount() {
	return voteCount;
    }

    public void setVoteCount(int voteCount) {
	this.voteCount = voteCount;
    }

    public List<PollOptionStats> getPollOptionStats() {
	return pollOptionStats;
    }

    public void setPollOptionStats(List<PollOptionStats> pollOptionStats) {
	this.pollOptionStats = pollOptionStats;
    }

}
