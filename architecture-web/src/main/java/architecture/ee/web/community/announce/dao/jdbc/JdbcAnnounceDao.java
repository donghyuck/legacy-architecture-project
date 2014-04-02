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
package architecture.ee.web.community.announce.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.community.announce.Announce;
import architecture.ee.web.community.announce.AnnounceNotFoundException;
import architecture.ee.web.community.announce.dao.AnnounceDao;
import architecture.ee.web.community.announce.impl.AnnounceImpl;

public class JdbcAnnounceDao extends ExtendedJdbcDaoSupport implements AnnounceDao {

	
	private ExtendedPropertyDao extendedPropertyDao;	
	private String sequencerName = "ANNOUNCE";
	private String announcePropertyTableName = "V2_ANNOUNCE_PROPERTY";
	private String announcePropertyPrimaryColumnName = "ANNOUNCE_ID";
	
	private final RowMapper<Announce> announceMapper = new RowMapper<Announce>(){
		public Announce mapRow(ResultSet rs, int rowNum) throws SQLException {
			AnnounceImpl announce = new AnnounceImpl();
			announce.setAnnounceId(rs.getLong("ANNOUNCE_ID"));
			announce.setObjectType(rs.getInt("OBJECT_TYPE"));
			announce.setObjectId(rs.getLong("OBJECT_ID"));
			announce.setUserId(rs.getLong("USER_ID"));
			announce.setSubject(rs.getString("SUBJECT"));
			announce.setBody(rs.getString("BODY"));
			announce.setStartDate(rs.getTimestamp("START_DATE"));
			announce.setEndDate(rs.getTimestamp("END_DATE"));		
			announce.setCreationDate(rs.getDate("CREATION_DATE"));
			announce.setModifiedDate(rs.getDate("MODIFIED_DATE"));					
			return announce;
		}		
	};
		/**
		 * 			ANNOUNCE_ID				INTEGER NOT NULL,
			OBJECT_TYPE						INTEGER NOT NULL,
			OBJECT_ID							INTEGER NOT NULL,	 		
			USER_ID								INTEGER NOT NULL,	 		
			SUBJECT								VARCHAR2(255) NOT NULL,
			BODY								VARCHAR2(255) NOT NULL,
			START_DATE						DATE DEFAULT  SYSDATE NOT NULL,
			END_DATE							DATE DEFAULT  SYSDATE NOT NULL,
			STATUS								NUMBER(1, 0)  DEFAULT 0, 
			CREATION_DATE					DATE DEFAULT  SYSDATE NOT NULL,
			MODIFIED_DATE					DATE DEFAULT  SYSDATE NOT NULL,
		 */
	
	public Long nextId( ){
		return getNextId(sequencerName);
	}
	
	/**
	 * @return extendedPropertyDao
	 */
	public ExtendedPropertyDao getExtendedPropertyDao() {
		return extendedPropertyDao;
	}
	/**
	 * @param extendedPropertyDao 설정할 extendedPropertyDao
	 */
	public void setExtendedPropertyDao(ExtendedPropertyDao extendedPropertyDao) {
		this.extendedPropertyDao = extendedPropertyDao;
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
	/**
	 * @return announcePropertyTableName
	 */
	public String getAnnouncePropertyTableName() {
		return announcePropertyTableName;
	}
	/**
	 * @param announcePropertyTableName 설정할 announcePropertyTableName
	 */
	public void setAnnouncePropertyTableName(String announcePropertyTableName) {
		this.announcePropertyTableName = announcePropertyTableName;
	}
	/**
	 * @return announcePropertyPrimaryColumnName
	 */
	public String getAnnouncePropertyPrimaryColumnName() {
		return announcePropertyPrimaryColumnName;
	}
	/**
	 * @param announcePropertyPrimaryColumnName 설정할 announcePropertyPrimaryColumnName
	 */
	public void setAnnouncePropertyPrimaryColumnName(
			String announcePropertyPrimaryColumnName) {
		this.announcePropertyPrimaryColumnName = announcePropertyPrimaryColumnName;
	}
	

	public Map<String, String> getAnnounceProperties(long announceId) {
		return extendedPropertyDao.getProperties(announcePropertyTableName, announcePropertyPrimaryColumnName, announceId);
	}

	public void deleteAnnounceProperties(long announceId) {
		extendedPropertyDao.deleteProperties(announcePropertyTableName, announcePropertyPrimaryColumnName, announceId);
	}
	
	public void setAnnounceProperties(long announceId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(announcePropertyTableName, announcePropertyPrimaryColumnName, announceId, props);
	}
	public Announce load(long announceId) throws AnnounceNotFoundException {		
		try {
			Announce announce = getExtendedJdbcTemplate().queryForObject(
					getBoundSql("ARCHITECTURE_WEB.SELECT_ANNOUNCE_BY_ID").getSql(), 
					announceMapper, 
					new SqlParameterValue (Types.NUMERIC, announceId ));
			announce.setProperties( getAnnounceProperties(announceId) );
			return announce;
		} catch (DataAccessException e) {
			throw new AnnounceNotFoundException(e);
		}
	}
	public void update(Announce annoucne) {
		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.UPDATE_ANNOUNCE").getSql(), 	
				new SqlParameterValue (Types.VARCHAR, annoucne.getSubject()), 
				new SqlParameterValue (Types.VARCHAR, annoucne.getBody() ), 
				new SqlParameterValue(Types.TIMESTAMP, annoucne.getStartDate()),
				new SqlParameterValue(Types.DATE, annoucne.getEndDate()),
				new SqlParameterValue(Types.DATE, annoucne.getModifiedDate()),
				new SqlParameterValue (Types.NUMERIC, annoucne.getAnnounceId()) );		
		
		setAnnounceProperties(annoucne.getAnnounceId(), annoucne.getProperties());		
	}
	
	public void insert(Announce announce) {
		
		long announceIdToUse = announce.getAnnounceId();
		if( announceIdToUse < 0 )
			announceIdToUse = getNextId(sequencerName);
		announce.setAnnounceId(announceIdToUse);
		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.INSERT_ANNOUNCE").getSql(), 	
				new SqlParameterValue (Types.NUMERIC, announce.getAnnounceId()),
				new SqlParameterValue (Types.NUMERIC, announce.getObjectType()),
				new SqlParameterValue (Types.NUMERIC, announce.getObjectId()),
				new SqlParameterValue (Types.NUMERIC, announce.getUserId()),
				new SqlParameterValue (Types.VARCHAR, announce.getSubject()), 
				new SqlParameterValue (Types.VARCHAR, announce.getBody() ), 
				new SqlParameterValue(Types.TIMESTAMP, announce.getStartDate()),
				new SqlParameterValue(Types.TIMESTAMP, announce.getEndDate()),
				new SqlParameterValue(Types.DATE, announce.getCreationDate()),
				new SqlParameterValue(Types.DATE, announce.getModifiedDate()) );				
		setAnnounceProperties(announce.getAnnounceId(), announce.getProperties());				
		
	}	
	
	public void delete(Announce annoucne) {
		getJdbcTemplate().update(
				getBoundSql("ARCHITECTURE_WEB.DELETE_ANNOUNCE" ).getSql(),
				new SqlParameterValue (Types.NUMERIC, annoucne.getAnnounceId())
		);
		deleteAnnounceProperties(annoucne.getAnnounceId());		
	}
	
	public void move(Long fromId, Long toId) {
		
	}
	
	public List<Long> getAnnounceIdsForUser(long userId) {
		return getExtendedJdbcTemplate().query(
				getBoundSql("ARCHITECTURE_WEB.SELECT_ANNOUNCE_IDS_BY_USER_ID").getSql(),
				new RowMapper<Long>(){
					public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getLong(1);
					}
				},
				new SqlParameterValue (Types.NUMERIC, userId ));		
		
	}
	
	public List<Long> getAnnounceIds(int objectType, long objectId) {
		return getExtendedJdbcTemplate().query(
				getBoundSql("ARCHITECTURE_WEB.SELECT_ANNOUNCE_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(),
				new RowMapper<Long>(){
					public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getLong(1);
					}
				},
				new SqlParameterValue (Types.NUMERIC, objectType ), new SqlParameterValue (Types.NUMERIC, objectId ));		
	}
	
	public List<Long> getAnnounceIds() {
		return getExtendedJdbcTemplate().query(
				getBoundSql("ARCHITECTURE_WEB.SELECT_ALL_ANNOUNCE_IDS" ).getSql(),
				new RowMapper<Long>(){
					public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getLong(1);
					}}
		);
	}
	
	
	
	
}
