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
package architecture.ee.web.community.timeline.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.community.announce.Announce;
import architecture.ee.web.community.announce.AnnounceNotFoundException;
import architecture.ee.web.community.timeline.DefaultMedia;
import architecture.ee.web.community.timeline.DefaultTimeline;
import architecture.ee.web.community.timeline.Timeline;
import architecture.ee.web.community.timeline.TimelineNotFoundException;
import architecture.ee.web.community.timeline.dao.TimelineDao;

public class JdbcTimelineDao extends ExtendedJdbcDaoSupport  implements TimelineDao {

	private String sequencerName = "TIMELINE";
	
	private final RowMapper<Timeline> timelineMapper = new RowMapper<Timeline>(){
		public Timeline mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			DefaultTimeline timeline = new DefaultTimeline();
			timeline.setTimelineId(rs.getLong("TIMELINE_ID"));
			timeline.setObjectType(rs.getInt("OBJECT_TYPE"));
			timeline.setObjectId(rs.getLong("OBJECT_ID"));
			timeline.setHeadline(rs.getString("SUBJECT"));
			timeline.setBody(rs.getString("BODY"));
			timeline.setStartDate(rs.getDate("START_DATE"));
			timeline.setEndDate(rs.getDate("END_DATE"));		
			
			DefaultMedia media = new DefaultMedia(
				rs.getString("MEDIA_URL"),
				rs.getString("MEDIA_THUMBNAIL_URL"),
				rs.getString("MEDIA_CAPTION"),
				rs.getString("MEDIA_CREDIT")
			);			
			if( StringUtils.isNotEmpty( media.getUrl())){
				timeline.setMedia(media);				
			}			
			return timeline;
		}		
	};
	
	public JdbcTimelineDao() {
	}

	public Long nextId( ){
		return getNextId(sequencerName);
	}

	/**
	 * @return sequencerName
	 */
	public String getSequencerName() {
		return sequencerName;
	}

	/**
	 * @param sequencerName 설정할 sequencerName
	 */
	public void setSequencerName(String sequencerName) {
		this.sequencerName = sequencerName;
	}

	public Timeline getTimelineById(long timelineId) throws TimelineNotFoundException {
		try {
			Timeline timeline = getExtendedJdbcTemplate().queryForObject(
					getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_TIMELINE_BY_ID").getSql(), 
					timelineMapper, 
					new SqlParameterValue (Types.NUMERIC, timelineId ));
			return timeline;
		} catch (DataAccessException e) {
			throw new TimelineNotFoundException(e);
		}
	}

	public List<Long> getTimelineIds(int objectType, long objectId) {
		return getExtendedJdbcTemplate().query(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_TIMELINE_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID_WITH_DATE_DESC").getSql(),
				new RowMapper<Long>(){
					public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getLong(1);
					}
				},
				new SqlParameterValue (Types.NUMERIC, objectType ), new SqlParameterValue (Types.NUMERIC, objectId ));		
	}

	public int getTimelineCount(int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForInt(
				getBoundSql("ARCHITECTURE_COMMUNITY.COUNT_TIMELINE_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				new SqlParameterValue(Types.NUMERIC, objectType ),
				new SqlParameterValue(Types.NUMERIC, objectId ));
	}

	public void updateTimeline(Timeline timeline) {
		long timelineIdToUse = timeline.getTimelineId();
		if( timelineIdToUse< 1 ){
			timelineIdToUse = nextId();
			((DefaultTimeline)timeline).setTimelineId(timelineIdToUse);			
			getJdbcTemplate().update(
					getBoundSql("ARCHITECTURE_COMMUNITY.INSERT_TIMELINE" ).getSql(),
					new SqlParameterValue (Types.NUMERIC, timelineIdToUse),
					new SqlParameterValue (Types.NUMERIC, timeline.getObjectType()),
					new SqlParameterValue (Types.NUMERIC, timeline.getObjectId()),
					new SqlParameterValue (Types.VARCHAR, timeline.getHeadline()),
					new SqlParameterValue (Types.VARCHAR, timeline.getBody()),
					new SqlParameterValue (Types.DATE, timeline.getStartDate()),
					new SqlParameterValue (Types.DATE, timeline.getEndDate()),
					new SqlParameterValue (Types.VARCHAR, timeline.isHasMedia() ? null : timeline.getMedia().getUrl()),
					new SqlParameterValue (Types.VARCHAR, timeline.isHasMedia() ? null : timeline.getMedia().getCaption()),
					new SqlParameterValue (Types.VARCHAR, timeline.isHasMedia() ? null : timeline.getMedia().getCredit()),
					new SqlParameterValue (Types.VARCHAR, timeline.isHasMedia() ? null : timeline.getMedia().getThumbnailUrl())
				);				
		}else{
			getJdbcTemplate().update(
					getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_TIMELINE" ).getSql(),
					new SqlParameterValue (Types.VARCHAR, timeline.getHeadline()),
					new SqlParameterValue (Types.VARCHAR, timeline.getBody()),
					new SqlParameterValue (Types.DATE, timeline.getStartDate()),
					new SqlParameterValue (Types.DATE, timeline.getEndDate()),
					new SqlParameterValue (Types.VARCHAR, timeline.isHasMedia() ? null : timeline.getMedia().getUrl()),
					new SqlParameterValue (Types.VARCHAR, timeline.isHasMedia() ? null : timeline.getMedia().getCaption()),
					new SqlParameterValue (Types.VARCHAR, timeline.isHasMedia() ? null : timeline.getMedia().getCredit()),
					new SqlParameterValue (Types.VARCHAR, timeline.isHasMedia() ? null : timeline.getMedia().getThumbnailUrl()),
					new SqlParameterValue (Types.NUMERIC, timeline.getTimelineId())
				);					
		}
	}

	public void deleteTimeline(Timeline timeline) {
		getJdbcTemplate().update(
			getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_TIMELINE_BY_ID" ).getSql(),
			new SqlParameterValue (Types.NUMERIC, timeline.getTimelineId())
		);		
	}
	
	
}
