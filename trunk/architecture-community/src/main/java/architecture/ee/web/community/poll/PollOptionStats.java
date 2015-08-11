package architecture.ee.web.community.poll;

import java.util.List;

import architecture.common.user.User;

public class PollOptionStats {

	private PollOption pollOption;
	private int voteCount = 0 ;
	private List<User> voteUsers ;
	
	public PollOptionStats(PollOption pollOption) {
		super();
		this.pollOption = pollOption;
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

	public void setVoteUsers(List<User> voteUsers) {
		this.voteUsers = voteUsers;
	}

	
}
