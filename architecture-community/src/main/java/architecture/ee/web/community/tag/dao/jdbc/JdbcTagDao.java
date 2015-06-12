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
package architecture.ee.web.community.tag.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.community.tag.ContentTag;
import architecture.ee.web.community.tag.DefaultContentTag;
import architecture.ee.web.community.tag.dao.TagDao;

public class JdbcTagDao extends ExtendedJdbcDaoSupport implements TagDao {

	protected static final RowMapper<ContentTag> conentTagRowMapper = new RowMapper<ContentTag>(){
		public ContentTag mapRow(ResultSet rs, int rowNum) throws SQLException {			
			return new DefaultContentTag(rs.getLong(1), rs.getString(2), rs.getTimestamp(3));
		}		
	};
	
	private ExtendedPropertyDao extendedPropertyDao;	
	private String sequencerName = "TAG";
	private String tagPropertyTableName = "V2_TAG_PROPERTY";
	private String tagPropertyPrimaryColumnName = "TAG_ID";
	
	public JdbcTagDao() {
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
	 * @return tagPropertyTableName
	 */
	public String getTagPropertyTableName() {
		return tagPropertyTableName;
	}

	/**
	 * @param tagPropertyTableName 설정할 tagPropertyTableName
	 */
	public void setTagPropertyTableName(String tagPropertyTableName) {
		this.tagPropertyTableName = tagPropertyTableName;
	}

	/**
	 * @return tagPropertyPrimaryColumnName
	 */
	public String getTagPropertyPrimaryColumnName() {
		return tagPropertyPrimaryColumnName;
	}

	/**
	 * @param tagPropertyPrimaryColumnName 설정할 tagPropertyPrimaryColumnName
	 */
	public void setTagPropertyPrimaryColumnName(String tagPropertyPrimaryColumnName) {
		this.tagPropertyPrimaryColumnName = tagPropertyPrimaryColumnName;
	}

	public void addTag(long tagId, int objectType, long objectId) {
		Date now = Calendar.getInstance().getTime();
		getExtendedJdbcTemplate().update(
				getBoundSql("ARCHITECTURE_COMMUNITY.INSERT_TAG").getSql(),
				new SqlParameterValue(Types.NUMERIC, tagId),
				new SqlParameterValue(Types.NUMERIC, objectType),
				new SqlParameterValue(Types.NUMERIC, objectId),				
				new SqlParameterValue(Types.TIMESTAMP, now));

	}

	public void removeTag(long tagId, int objectType, long objectId) {
		getExtendedJdbcTemplate().update(
				getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_TAG").getSql(),
				new SqlParameterValue(Types.NUMERIC, objectType),
				new SqlParameterValue(Types.NUMERIC, objectId),
				new SqlParameterValue(Types.NUMERIC, tagId)
		);		
	}
	
	public int countTags(long tagId) {
		return getExtendedJdbcTemplate().queryForObject(
				getBoundSql("ARCHITECTURE_COMMUNITY.COUNT_TAG").getSql(),
				Integer.class, new SqlParameterValue(Types.NUMERIC, tagId));
	}
	
	public List<Long> getTagIds(int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForList(
			getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_TAG_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), Long.class,
			new SqlParameterValue(Types.NUMERIC, objectType),
			new SqlParameterValue(Types.NUMERIC, objectId));
	}
	
	public ContentTag getContentTagById(long tagId){
		return getExtendedJdbcTemplate().queryForObject(
			getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_CONTENT_TAG_BY_ID").getSql(),
			conentTagRowMapper,
			new SqlParameterValue(Types.NUMERIC, tagId));		
	}
	
	public ContentTag getContentTagByName(String name){
		try {
			return getExtendedJdbcTemplate().queryForObject(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_CONTENT_TAG_BY_NAME").getSql(),
				conentTagRowMapper,
				new SqlParameterValue(Types.VARCHAR, name));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}		
	}	
	
	public void createContentTag(ContentTag tag) {
		if (tag.getName() == null || tag.getName().length() == 0) {
			throw new IllegalStateException("Tag must have a non null name.");
		} else {
			long tagId = tag.getTagId();
			if( tagId == -1L ){
				tagId = this.getNextId(getSequencerName());
				if( tag instanceof DefaultContentTag){
					((DefaultContentTag) tag).setTagId(tagId);
				}
			}
			getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.CREATE_CONTENT_TAG").getSql(), 	
					new SqlParameterValue (Types.NUMERIC, tagId),
					new SqlParameterValue (Types.VARCHAR, tag.getName()), 
					new SqlParameterValue(Types.TIMESTAMP, tag.getCreationDate())
			);
			return;
		}
	}
	
	public void deleteContentTag(long tagId) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_CONTENT_TAG").getSql(), 	
			new SqlParameterValue (Types.NUMERIC, tagId )
		);
	}
}
