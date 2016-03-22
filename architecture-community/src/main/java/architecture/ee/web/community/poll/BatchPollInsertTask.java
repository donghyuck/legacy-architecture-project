package architecture.ee.web.community.poll;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.ee.web.community.poll.dao.PollDao;

public class BatchPollInsertTask {

    private Log log = LogFactory.getLog(BatchPollInsertTask.class);

    private LinkedList<Vote> voteQueue;

    private PollDao pollDao;

    private final Lock lock = new ReentrantLock();

    public void initialize() {

    }

    @Scheduled(fixedDelay = 60 * 1000)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void batchInserts() {

	log.debug("============ || running ...");
	Boolean isLock = false;
	if (voteQueue != null && voteQueue.size() > 0) {
	    try {
		isLock = lock.tryLock();
		List<Vote> votes = new ArrayList<Vote>(voteQueue.size());
		for (int i = 0; i < voteQueue.size(); i++) {
		    votes.add(voteQueue.removeLast());
		}
		pollDao.batchPollVotes(votes);
	    } finally {
		if (isLock)
		    lock.unlock();
	    }
	}
    }

    public LinkedList<Vote> getVoteQueue() {
	return voteQueue;
    }

    public void setVoteQueue(LinkedList<Vote> voteQueue) {
	this.voteQueue = voteQueue;
    }

    public PollDao getPollDao() {
	return pollDao;
    }

    public void setPollDao(PollDao pollDao) {
	this.pollDao = pollDao;
    }

}
