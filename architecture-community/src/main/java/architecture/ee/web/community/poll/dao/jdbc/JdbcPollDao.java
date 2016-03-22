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
package architecture.ee.web.community.poll.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.configureme.util.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.common.user.User;
import architecture.common.user.UserTemplate;
import architecture.ee.exception.NotFoundException;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.community.model.ContentObject.Status;
import architecture.ee.web.community.poll.DefaultPoll;
import architecture.ee.web.community.poll.Poll;
import architecture.ee.web.community.poll.PollOption;
import architecture.ee.web.community.poll.Vote;
import architecture.ee.web.community.poll.dao.PollDao;

public class JdbcPollDao extends ExtendedJdbcDaoSupport implements PollDao {

    private final RowMapper<Poll> pollMapper = new RowMapper<Poll>() {
	public Poll mapRow(ResultSet rs, int rowNum) throws SQLException {

	    DefaultPoll poll = new DefaultPoll();
	    poll.setPollId(rs.getLong("POLL_ID"));
	    poll.setObjectType(rs.getInt("OBJECT_TYPE"));
	    poll.setObjectId(rs.getLong("OBJECT_ID"));
	    poll.setUser(new UserTemplate(rs.getLong("USER_ID")));
	    poll.setName(rs.getString("NAME"));
	    poll.setDescription(rs.getString("DESCRIPTION"));
	    poll.setCreationDate(rs.getTimestamp("CREATION_DATE"));
	    poll.setModifiedDate(rs.getTimestamp("MODIFIED_DATE"));
	    poll.setStartDate(rs.getTimestamp("START_DATE"));
	    poll.setEndDate(rs.getTimestamp("END_DATE"));
	    poll.setExpireDate(rs.getTimestamp("EXPIRE_DATE"));
	    poll.setMode(rs.getLong("POLL_MODE"));
	    poll.setStatus(Status.valueOf(rs.getInt("STATUS")));
	    return poll;
	}
    };

    private final RowMapper<Vote> voteMapper = new RowMapper<Vote>() {
	public Vote mapRow(ResultSet rs, int rowNum) throws SQLException {
	    Vote option = new Vote(rs.getLong("OPTION_ID"), rs.getLong("USER_ID"), rs.getString("GUEST_ID"),
		    rs.getString("IP"), rs.getTimestamp("VOTE_DATE"));
	    return option;
	}
    };

    private final RowMapper<PollOption> pollOptionMapper = new RowMapper<PollOption>() {
	public PollOption mapRow(ResultSet rs, int rowNum) throws SQLException {
	    PollOption option = new PollOption(rs.getLong("OPTION_ID"), rs.getLong("POLL_ID"),
		    rs.getString("OPTION_TEXT"), rs.getInt("OPTION_INDEX"));
	    return option;
	}
    };

    private String pollSequencerName = "POLL";

    private String pollOptionSequencerName = "POLL_OPTION";

    protected long getNextPollId() {
	return getNextId(pollSequencerName);
    }

    protected long getNextPollOptionId() {
	return getNextId(pollOptionSequencerName);
    }

    @Override
    public int getPollCount() {
	return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_COMMUNITY.COUNT_ALL_POLLS").getSql(),
		Integer.class);
    }

    @Override
    public List<Long> getPollIds() {
	final List<Long> list = new ArrayList<Long>();
	getExtendedJdbcTemplate().query(getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_ALL_POLL_IDS").getSql(),
		new RowCallbackHandler() {
		    public void processRow(ResultSet rs) throws SQLException {
			long pollId = rs.getLong(1);
			list.add(pollId);
		    }
		});
	return list;
    }

    @Override
    public Poll getPollById(long pollId) throws NotFoundException {

	Poll poll = new DefaultPoll();
	try {
	    poll = getExtendedJdbcTemplate().queryForObject(
		    getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_POLL_BY_ID").getSql(), pollMapper,
		    new SqlParameterValue(Types.NUMERIC, pollId));
	} catch (DataAccessException e) {
	    throw new NotFoundException(
		    (new StringBuilder()).append("No poll found with id: ").append(pollId).toString());
	}
	/*
	 * List<PollOption> options = getExtendedJdbcTemplate().query(
	 * getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_POLL_OPTIONS").getSql(),
	 * pollOptionMapper, new SqlParameterValue(Types.NUMERIC, pollId) );
	 * poll.setOptions(options);
	 */
	return poll;
    }

    @Override
    public Poll createPoll(Poll poll) {
	DefaultPoll dp = (DefaultPoll) poll;
	if (dp.getPollId() < 0) {
	    dp.setPollId(getNextPollId());
	}
	getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.INSERT_POLL").getSql(),
		new SqlParameterValue(Types.NUMERIC, dp.getPollId()),
		new SqlParameterValue(Types.NUMERIC, dp.getObjectType()),
		new SqlParameterValue(Types.NUMERIC, dp.getObjectId()),
		new SqlParameterValue(Types.NUMERIC, dp.getUser().getUserId()),
		new SqlParameterValue(Types.VARCHAR, dp.getName()),
		new SqlParameterValue(Types.VARCHAR, dp.getDescription()),
		new SqlParameterValue(Types.NUMERIC, dp.getMode()),
		new SqlParameterValue(Types.TIMESTAMP, dp.getCreationDate()),
		new SqlParameterValue(Types.TIMESTAMP, dp.getModifiedDate()),
		new SqlParameterValue(Types.TIMESTAMP, dp.getStartDate()),
		new SqlParameterValue(Types.TIMESTAMP, dp.getEndDate()),
		new SqlParameterValue(Types.TIMESTAMP, dp.getExpireDate()),
		new SqlParameterValue(Types.NUMERIC, dp.getStatus().getIntValue()));
	return dp;
    }

    @Override
    public Poll updatePoll(Poll poll) {
	Date now = Calendar.getInstance().getTime();
	poll.setModifiedDate(now);
	getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_POLL").getSql(),
		new SqlParameterValue(Types.VARCHAR, poll.getName()),
		new SqlParameterValue(Types.VARCHAR, poll.getDescription()),
		new SqlParameterValue(Types.NUMERIC, poll.getMode()),
		new SqlParameterValue(Types.TIMESTAMP, poll.getModifiedDate()),
		new SqlParameterValue(Types.TIMESTAMP, poll.getStartDate()),
		new SqlParameterValue(Types.TIMESTAMP, poll.getEndDate()),
		new SqlParameterValue(Types.TIMESTAMP, poll.getExpireDate()),
		new SqlParameterValue(Types.NUMERIC, poll.getStatus().getIntValue()),
		new SqlParameterValue(Types.NUMERIC, poll.getPollId()));
	return poll;
    }

    @Override
    public Poll deletePoll(Poll poll) {
	return null;
    }

    @Override
    public int getPollCount(int objectType, long objectId) {
	return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_COMMUNITY.OBECT_POLL_COUNT").getSql(),
		Integer.class, new SqlParameterValue(Types.NUMERIC, objectType),
		new SqlParameterValue(Types.NUMERIC, objectId));
    }

    @Override
    public int getPollCount(User user) {
	return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_COMMUNITY.USER_POLL_COUNT").getSql(),
		Integer.class, new SqlParameterValue(Types.NUMERIC, user.getUserId()));
    }

    @Override
    public List<Long> getPollIds(int objectType, long objectId) {
	final List<Long> list = new ArrayList<Long>();
	getExtendedJdbcTemplate().query(getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_OBJECT_POLL_IDS").getSql(),
		new RowCallbackHandler() {
		    public void processRow(ResultSet rs) throws SQLException {
			long pollId = rs.getLong(1);
			list.add(pollId);
		    }
		}, new SqlParameterValue(Types.NUMERIC, objectType), new SqlParameterValue(Types.NUMERIC, objectId));
	return list;
    }

    @Override
    public List<Long> getPollIds(User user) {
	final List<Long> list = new ArrayList<Long>();
	getExtendedJdbcTemplate().query(getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_USER_POLL_IDS").getSql(),
		new RowCallbackHandler() {
		    public void processRow(ResultSet rs) throws SQLException {
			long pollId = rs.getLong(1);
			list.add(pollId);
		    }
		}, new SqlParameterValue(Types.NUMERIC, user.getUserId()));
	return list;
    }

    @Override
    public List<PollOption> getPollOptions(Poll poll) {
	List<PollOption> options = getExtendedJdbcTemplate().query(
		getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_POLL_OPTIONS").getSql(), pollOptionMapper,
		new SqlParameterValue(Types.NUMERIC, poll.getPollId()));
	return options;
    }

    @Override
    public void updatePollOptions(Poll poll, List<PollOption> options) {
	for (PollOption option : options) {
	    if (option.getOptionId() > 0) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_POLL_OPTION").getSql(),
			new SqlParameterValue(Types.VARCHAR, option.getOptionText()),
			new SqlParameterValue(Types.NUMERIC, option.getOptionIndex()),
			new SqlParameterValue(Types.NUMERIC, option.getOptionId()));
	    } else {
		option.setOptionId(getNextPollOptionId());
		option.setPollId(poll.getPollId());
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.INSERT_POLL_OPTION").getSql(),
			new SqlParameterValue(Types.NUMERIC, option.getOptionId()),
			new SqlParameterValue(Types.NUMERIC, option.getPollId()),
			new SqlParameterValue(Types.NUMERIC, option.getOptionIndex()),
			new SqlParameterValue(Types.VARCHAR, option.getOptionText()));
	    }
	}

    }

    @Override
    public void deletePollOptions(Poll poll, List<PollOption> options) {
	for (PollOption option : options) {
	    if (option.getOptionId() > 0 && option.getPollId() == poll.getPollId()) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_POLL_OPTION").getSql(),
			new SqlParameterValue(Types.NUMERIC, option.getOptionId()));
	    }
	}
    }

    public void deleteVote(Vote vote) {
	getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_VOTE").getSql(),
		new SqlParameterValue(Types.NUMERIC, vote.getOptionId()),
		new SqlParameterValue(Types.NUMERIC, vote.getUserId()),
		new SqlParameterValue(Types.VARCHAR, vote.getUniqueId()));
    }

    public void insertVote(Vote vote) {
	getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.INSERT_VOTE").getSql(),
		new SqlParameterValue(Types.NUMERIC, vote.getOptionId()),
		new SqlParameterValue(Types.NUMERIC, vote.getUserId()),
		new SqlParameterValue(Types.VARCHAR, vote.getUniqueId()),
		new SqlParameterValue(Types.TIMESTAMP, vote.getVoteDate()),
		new SqlParameterValue(Types.VARCHAR, vote.getIPAddress()));
    }

    public List<Vote> getVotes(Poll poll) {
	List<Vote> options = getExtendedJdbcTemplate().query(
		getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_VOTES_BY_POLL").getSql(), voteMapper,
		new SqlParameterValue(Types.NUMERIC, poll.getPollId()));
	return options;
    }

    public void batchPollVotes(final List<Vote> votes) {
	getExtendedJdbcTemplate().batchUpdate(getBoundSql("ARCHITECTURE_COMMUNITY.INSERT_VOTE").getSql(),
		new BatchPreparedStatementSetter() {
		    public void setValues(PreparedStatement ps, int i) throws SQLException {
			Vote vote = votes.get(i);
			log.debug("[" + i + "]" + vote);
			ps.setLong(1, vote.getOptionId());
			if (vote.getUserId() > 0)
			    ps.setLong(2, vote.getUserId());
			else
			    ps.setNull(2, Types.NUMERIC);
			if (StringUtils.isEmpty(vote.getUniqueId()))
			    ps.setNull(3, Types.VARCHAR);
			else
			    ps.setString(3, vote.getUniqueId());
			ps.setObject(4, new java.sql.Timestamp(vote.getVoteDate().getTime()), Types.TIMESTAMP);
			ps.setString(5, vote.getIPAddress());
		    }

		    public int getBatchSize() {
			return votes.size();
		    }
		});
    }

}
