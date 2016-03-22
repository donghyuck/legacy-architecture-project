package architecture.ee.web.community.poll;

import java.util.List;

import architecture.common.user.User;

public class PollOptionStats {

    private PollOption pollOption;
    private int voteCount = 0;
    private int totalVoteCount = 0;
    private List<User> voteUsers;

    public PollOptionStats(PollOption pollOption) {
	super();
	this.pollOption = pollOption;
    }

    public int getTotalVoteCount() {
	return totalVoteCount;
    }

    public void setTotalVoteCount(int totalVoteCount) {
	this.totalVoteCount = totalVoteCount;
    }

    public int getVoteCount() {
	return voteCount;
    }

    public void setVoteCount(int voteCount) {
	this.voteCount = voteCount;
    }

    public List<User> getVoteUsers() {
	return voteUsers;
    }

    public PollOption getPollOption() {
	return pollOption;
    }

    public void setPollOption(PollOption pollOption) {
	this.pollOption = pollOption;
    }

    public void setVoteUsers(List<User> voteUsers) {
	this.voteUsers = voteUsers;
    }

    public String getVotePercentString() {
	if (voteCount > 0) {
	    int percent = (int) ((voteCount * 100.0f) / totalVoteCount);
	    return (new Integer(percent)).toString();
	}
	return "0";
    }

}
