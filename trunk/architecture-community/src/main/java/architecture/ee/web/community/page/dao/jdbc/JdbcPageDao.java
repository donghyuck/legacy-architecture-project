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
package architecture.ee.web.community.page.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.support.SqlLobValue;

import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.community.page.BodyContent;
import architecture.ee.web.community.page.BodyType;
import architecture.ee.web.community.page.DefaultBodyContent;
import architecture.ee.web.community.page.Page;
import architecture.ee.web.community.page.PageState;
import architecture.ee.web.community.page.dao.PageDao;

public class JdbcPageDao extends ExtendedJdbcDaoSupport  implements PageDao {

	
	private String sequencerName = "PAGE";
	private int DEFAULT_PAGE_VERSION = 1;

	private final RowMapper<BodyContent> bodyContentMapper = new RowMapper<BodyContent>(){
		public BodyContent mapRow(ResultSet rs, int rowNum) throws SQLException {
			DefaultBodyContent body = new DefaultBodyContent();
			body.setBodyId(rs.getLong("BODY_ID"));
			body.setPageId(rs.getLong("PAGE_ID"));
			body.setBodyType(
					BodyType.getBodyTypeById( rs.getInt("BODY_TYPE") )
			);
			body.setBodyText(rs.getString("BODY_TEXT"));
			return body;
		}		
	};
	
	public JdbcPageDao() {
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
	 * 새로운 페이지 생성
	 */
	public void create(Page page) {

		long nextPageId = getNextId(sequencerName);
		page.setPageId(nextPageId);
		page.setVersionId(DEFAULT_PAGE_VERSION);
		
		// binary body data handleing 
		
		String tempPrefix = ApplicationHelper.getApplicationProperty("components.page.temporaryPagePrefix", "tempPAGE-");
		if( page.getName() == null || page.getName().startsWith(tempPrefix)){
			page.setName(
					(new StringBuilder()).append(
						ApplicationHelper.getApplicationProperty("components.page.pagePrefix", "PAGE-")
					).toString()
				);			
		}		
		
		// INSERT V2_PAGE
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.CREATE_PAGE").getSql(), 
			new SqlParameterValue(Types.NUMERIC, page.getPageId() ),
			new SqlParameterValue(Types.NUMERIC, page.getObjectType() ),
			new SqlParameterValue(Types.NUMERIC, page.getObjectId() ),
			new SqlParameterValue(Types.VARCHAR, page.getName() ),
			new SqlParameterValue(Types.NUMERIC, page.getVersionId()),
			new SqlParameterValue(Types.NUMERIC, page.getUser()),
			new SqlParameterValue(Types.DATE, page.getCreationDate()),
			new SqlParameterValue(Types.DATE, page.getModifiedDate())
		);		
		
		insertPageVersion(page);
		
		insertPageBody(page);
		
		if( page.getProperties() != null && !page.getProperties().isEmpty() ){
			Map<String, String> properties = page.getProperties();
			final List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(properties.entrySet());
			final long pageId = page.getPageId();
			final long pageVersionId = page.getVersionId();
			getExtendedJdbcTemplate().batchUpdate(getBoundSql("ARCHITECTURE_COMMUNITY.INSERT_PAGE_PROPERTY").getSql(), 
				new BatchPreparedStatementSetter(){
					public int getBatchSize() {
						return entryList.size();
					}
					public void setValues(PreparedStatement ps, int index) throws SQLException {		
						Map.Entry<String, String> e = entryList.get(index);
						ps.setLong(1,  pageId);
						ps.setLong(1,  pageVersionId);
						ps.setString(2, e.getKey());
						ps.setString(3, e.getValue());
					}
				}
			);
		}
	}
	
	private void insertPageVersion(Page page){
		Date now = Calendar.getInstance().getTime();
		if( page.getVersionId() > 1 ){
			page.setModifiedDate(now);
		}
		if( page.getPageState() == PageState.PUBLISHED){
			// clean up on publish
			cleanupVersionsOnPublish(page);			
		}
		
		// INSERT V2_PAGE_VERSION
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.INSERT_PAGE_VERSION").getSql(), 
			new SqlParameterValue(Types.NUMERIC, page.getPageId() ),
			new SqlParameterValue(Types.NUMERIC, page.getVersionId() ),
			new SqlParameterValue(Types.VARCHAR, page.getPageState().name().toLowerCase()),
			new SqlParameterValue(Types.VARCHAR, page.getTitle()),
			page.getSummary()== null ? new SqlParameterValue(Types.NULL, null) : new SqlParameterValue(Types.VARCHAR, page.getSummary()),
			new SqlParameterValue(Types.NUMERIC, page.getVersionId() <= 1 ? page.getUser() : page.getUser() ),
			new SqlParameterValue(Types.DATE, page.getCreationDate()),
			new SqlParameterValue(Types.DATE, page.getModifiedDate())			
		);				
	}
	
	private void cleanupVersionsOnPublish(Page page){
		if( page.getVersionId() > 0 ){
			try {
				int pubishedVersion = getExtendedJdbcTemplate().queryForInt(
					getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_PUBLISHED_PAGE_VERSION_NUMBER").getSql(), 
					new SqlParameterValue(Types.NUMERIC, page.getPageId() )	
				);
				page.setVersionId(pubishedVersion + 1);
			} catch (EmptyResultDataAccessException e) {
				int maxArchiveId =  getExtendedJdbcTemplate().queryForInt(
					getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_MAX_ARCHIVED_PAGE_VERSION_NUMBER").getSql(), 
					new SqlParameterValue(Types.NUMERIC, page.getPageId() )	
				);
				if( maxArchiveId > 0 )
					page.setVersionId(maxArchiveId +1);
				else
					page.setVersionId(1);
			}			
			List<Long> toDelete = getExtendedJdbcTemplate().queryForList(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_DRAFT_PAGE_VERSIONS").getSql(), 	
				Long.class, new SqlParameterValue(Types.NUMERIC, page.getPageId() ));
			for( Long version : toDelete )
				deleteVersion( page, version.intValue() );			
		}
		getExtendedJdbcTemplate().update(
			getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_ARCHIVED_PAGE_STATE").getSql(), 	
			new SqlParameterValue(Types.NUMERIC, page.getPageId() ),
			new SqlParameterValue(Types.NUMERIC, page.getVersionId() )
		);
		
		getExtendedJdbcTemplate().update(
			getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_PAGE_VISION_NUMBER").getSql(), 	
			new SqlParameterValue(Types.NUMERIC, page.getVersionId() ),
			new SqlParameterValue(Types.NUMERIC, page.getPageId() )				
		);		
		
	}
	
	private void deleteVersion(Page page, int version){
		getExtendedJdbcTemplate().update(
			getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_PAGE_BODY_VERSION").getSql(),
			new SqlParameterValue(Types.NUMERIC, page.getPageId() ),
			new SqlParameterValue(Types.NUMERIC, version )			
		);		
		getExtendedJdbcTemplate().update(
			getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_PAGE_VERSION").getSql(),
			new SqlParameterValue(Types.NUMERIC, page.getPageId() ),
			new SqlParameterValue(Types.NUMERIC, version )			
		);
	}
	
	private void insertPageBody(final Page page){
		long bodyId = -1L;		
		if( page.getBodyText() != null ){			
			boolean newBodyRequired = false;
			if( page.getVersionId() == 1 ){
				newBodyRequired = true;
			}else{
				// load body from database ...
				List<BodyContent> results = getExtendedJdbcTemplate().query(
					getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_PAGE_BODY").getSql(), 
					bodyContentMapper, 
					new SqlParameterValue(Types.NUMERIC, page.getPageId()), 
					new SqlParameterValue(Types.NUMERIC, Integer.valueOf(page.getVersionId()	-1))
				);
				String preTextBody = null;
				for(BodyContent bodyContent : results ){
					bodyId = bodyContent.getBodyId();
					preTextBody = bodyContent.getBodyText();
				}				
				String textBody = page.getBodyText();
				if( preTextBody == null || !preTextBody.equals(textBody) )
					newBodyRequired = true;				
			}
			
			if(newBodyRequired){
				bodyId = getNextId("PAGE_BODY");
				getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.INSERT_PAGE_BODY").getSql(), 
					new SqlParameterValue(Types.NUMERIC, bodyId ),
					new SqlParameterValue(Types.NUMERIC, page.getPageId() ),
					new SqlParameterValue(Types.NUMERIC, page.getBodyContent().getBodyType().getId() ),
					new SqlParameterValue(Types.CLOB, new SqlLobValue(page.getBodyText(), getLobHandler() ) )
				);	
				getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.INSERT_PAGE_BODY_VERSION").getSql(), 
					new SqlParameterValue(Types.NUMERIC, bodyId ),
					new SqlParameterValue(Types.NUMERIC, page.getPageId() ),
					new SqlParameterValue(Types.NUMERIC, page.getVersionId() )
				);	
			}		
		}
	}
	
	public void update(Page page, boolean flag) {
		// TODO 자동 생성된 메소드 스텁
		
	}

	public void delete(Page page) {
		// TODO 자동 생성된 메소드 스텁
		
	}

	public Page getPageById(long pageId) {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	public Page getPageById(long pageId, int versionNumber) {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	public Page getPageByName(String name) {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	public Page getPageByName(String name, int versionNumber) {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	public Page getPageByTitle(int objectType, long objectId, String title) {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

}
