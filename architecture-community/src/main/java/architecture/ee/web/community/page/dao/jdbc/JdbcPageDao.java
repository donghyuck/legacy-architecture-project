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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.SqlLobValue;

import architecture.common.user.SecurityHelper;
import architecture.common.user.UserTemplate;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.community.page.BodyContent;
import architecture.ee.web.community.page.BodyType;
import architecture.ee.web.community.page.DefaultBodyContent;
import architecture.ee.web.community.page.DefaultPage;
import architecture.ee.web.community.page.Page;
import architecture.ee.web.community.page.PageState;
import architecture.ee.web.community.page.PageVersion;
import architecture.ee.web.community.page.PageVersionHelper;
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
					).append(nextPageId).toString()
				);			
		}		
		
		// INSERT V2_PAGE
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.CREATE_PAGE").getSql(), 
			new SqlParameterValue(Types.NUMERIC, page.getPageId() ),
			new SqlParameterValue(Types.NUMERIC, page.getObjectType() ),
			new SqlParameterValue(Types.NUMERIC, page.getObjectId() ),
			new SqlParameterValue(Types.VARCHAR, page.getName() ),
			new SqlParameterValue(Types.NUMERIC, page.getVersionId()),
			new SqlParameterValue(Types.NUMERIC, page.getUser().getUserId()),
			new SqlParameterValue(Types.DATE, page.getCreationDate()),
			new SqlParameterValue(Types.DATE, page.getModifiedDate())
		);		
		
		insertPageVersion(page);
		
		insertPageBody(page);
		
		insertProperties(page);
	}
	
	private void insertProperties(Page page){
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
	
	private void updateProperties(final Page page){
		Map<String, String> oldProps = loadProperties(page);
		final List<String> deleteKeys = getDeletedPropertyKeys(oldProps, page.getProperties());
		final List<String> modifiedKeys = getModifiedPropertyKeys(oldProps, page.getProperties());
		final List<String> addedKeys = getAddedPropertyKeys(oldProps, page.getProperties());
		if( !deleteKeys.isEmpty() ){
			getExtendedJdbcTemplate().batchUpdate(
				getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_PAGE_PROPERTY_BY_NAME").getSql(), 
				new BatchPreparedStatementSetter(){
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						ps.setLong(1, page.getPageId());
						ps.setLong(2,  page.getVersionId());
						ps.setString(3, deleteKeys.get(i));
					}
					public int getBatchSize() {
						return deleteKeys.size();
					}					
				}
			);	
		}
		if( !modifiedKeys.isEmpty() ){
			getExtendedJdbcTemplate().batchUpdate(
				getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_PAGE_PROPERTY_BY_NAME").getSql(), 
				new BatchPreparedStatementSetter(){
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						String key = modifiedKeys.get(i);
						ps.setString(1, key);
						ps.setString(2, page.getProperties().get(key));
						ps.setLong(3, page.getPageId());
						ps.setLong(4,  page.getVersionId());
					}
					public int getBatchSize() {
						return modifiedKeys.size();
					}					
				}
			);	
		}
		if( !addedKeys.isEmpty() ){
			getExtendedJdbcTemplate().batchUpdate(
				getBoundSql("ARCHITECTURE_COMMUNITY.INSERT_PAGE_PROPERTY").getSql(), 
				new BatchPreparedStatementSetter(){
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setLong(1, page.getPageId());
						ps.setLong(2,  page.getVersionId());
						String key = addedKeys.get(i);
						String value = page.getProperty(key, null);
						
						log.debug("batch[" + key + "=" + value +  "]");
						
						ps.setString(3, key);
						ps.setString(4, value );
												
					}
					public int getBatchSize() {
						return addedKeys.size();
					}					
				}
			);	
		}				
	}
	
	private List<String> getDeletedPropertyKeys(Map<String, String> oldProps, Map<String, String> newProps){
		HashMap<String, String> temp = new HashMap<String, String>(oldProps);
		Set<String> oldKeys = temp.keySet();
		Set<String> newKeys = newProps.keySet();
		oldKeys.removeAll(newKeys);
		return Arrays.asList( 
			oldKeys.toArray( new String[oldKeys.size()] )
		); 
	}

	private List<String> getModifiedPropertyKeys(Map<String, String> oldProps, Map<String, String> newProps){
		HashMap<String, String> temp = new HashMap<String, String>(oldProps);
		Set<String> oldKeys = temp.keySet();
		Set<String> newKeys = newProps.keySet();
		oldKeys.retainAll(newKeys);
		List<String> modified = new ArrayList<String>();
		for( String key : oldKeys){
			if( newProps.get(key).equals(oldProps.get(key)))
				modified.add(key);
		}		
		return modified;
	}
	
	private List<String> getAddedPropertyKeys(Map<String, String> oldProps, Map<String, String> newProps){
		HashMap<String, String> temp = new HashMap<String, String>(oldProps);
		Set<String> oldKeys = temp.keySet();
		Set<String> newKeys = newProps.keySet();
		newKeys.removeAll(oldKeys);
		return Arrays.asList( 
			newKeys.toArray( new String[newKeys.size()] )
		); 
	}	
	
	private Map<String, String> loadProperties(Page page){
		return getExtendedJdbcTemplate().query(
			getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_PAGE_PROPERTIES").getSql(), 
			new Object[] { page.getPageId(), page.getVersionId() }, new ResultSetExtractor<Map<String, String>>(){
			public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {				
				Map<String, String> rows = new HashMap<String, String>();				
				while(rs.next()){
					String key = rs.getString(1);
					String value = rs.getString(2);
					rows.put(key, value);
				}
				return rows;
		}});	
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
			page.getSummary() == null ? new SqlParameterValue(Types.NULL, null) : new SqlParameterValue(Types.VARCHAR, page.getSummary()),
			new SqlParameterValue(Types.NUMERIC, page.getVersionId() <= 1 ? page.getUser().getUserId() : SecurityHelper.getUser().getUserId() ),
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
			getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_PAGE_STATE_TO_ARCHIVED").getSql(), 	
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
	
	public void update(Page page, boolean isNewVersion) {
		int prevVersionId = page.getVersionId();
		Date now = Calendar.getInstance().getTime();
		if( isNewVersion ){
			int maxVersionId = getExtendedJdbcTemplate().queryForInt(
					getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_MAX_PAGE_VERSION_NUMBER").getSql(), 
					new SqlParameterValue(Types.NUMERIC, page.getPageId() )	
				);
			page.setVersionId(maxVersionId + 1);			
		}
		
		page.setModifiedDate(now);
		// update page ...
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_PAGE").getSql(), 
			new SqlParameterValue(Types.NUMERIC, page.getPageId() ),
			new SqlParameterValue(Types.NUMERIC, page.getObjectType() ),
			new SqlParameterValue(Types.NUMERIC, page.getObjectId() ),
			new SqlParameterValue(Types.VARCHAR, page.getName() ),
			new SqlParameterValue(Types.NUMERIC, page.getVersionId()),
			new SqlParameterValue(Types.NUMERIC, page.getUser().getUserId()),
			new SqlParameterValue(Types.DATE, page.getModifiedDate()),
			new SqlParameterValue(Types.NUMERIC, page.getPageId() )
		);	
		
		updateProperties(page);
		if( isNewVersion ){
			insertPageVersion(page);			
			insertPageBody(page);			
		}else{
			updatePageVersion(page, prevVersionId);			
			updatePageBody(page, prevVersionId);		
		}		
	}
	
	private void updatePageVersion(Page page, int prevVersionId){
		Date now = Calendar.getInstance().getTime();
		
		if( page.getPageState() == PageState.PUBLISHED ){
			getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_PAGE_STATE_TO_ARCHIVED").getSql(), 
					new SqlParameterValue(Types.NUMERIC, page.getPageId() ),
					new SqlParameterValue(Types.NUMERIC, page.getVersionId())
			);
		}
		if( page.getVersionId() > 0){
			page.setModifiedDate(now);
			long modifierId = page.getUser().getUserId()<= 0L ? page.getUser().getUserId() : page.getUser().getUserId();
			// update page version
			getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_PAGE_VERSION").getSql(), 
					new SqlParameterValue(Types.VARCHAR, page.getPageState().name().toLowerCase() ),
					new SqlParameterValue(Types.VARCHAR, page.getTitle() ),
					new SqlParameterValue(Types.VARCHAR, page.getSummary() ),
					new SqlParameterValue(Types.NUMERIC, modifierId),
					new SqlParameterValue(Types.DATE, page.getModifiedDate()),
					new SqlParameterValue(Types.NUMERIC, page.getPageId() ),
					new SqlParameterValue(Types.NUMERIC, page.getVersionId())
			);			
			
		}
	}
	
	private void updatePageBody(Page page, int prevVersionId){
		long bodyId = -1L;
		
		try {
			bodyId = getExtendedJdbcTemplate().queryForLong(
					getBoundSql("ARCHITECTURE_COMMUNITY.SELETE_PAGE_BODY_ID").getSql(), 
					new SqlParameterValue(Types.NUMERIC, page.getPageId() ),	
					new SqlParameterValue(Types.NUMERIC, prevVersionId )	
				);
		} catch (EmptyResultDataAccessException e) {
		}
		if( page.getBodyText() != null ){
			if( bodyId != -1L ){
				final long bodyIdToUse = bodyId;
				getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_PAGE_BODY").getSql(), 
					new SqlParameterValue(Types.INTEGER, page.getBodyContent().getBodyType().getId() ),
					new SqlParameterValue(Types.VARCHAR, page.getBodyContent().getBodyText() ),	
					new SqlParameterValue(Types.NUMERIC, bodyIdToUse )
				);				
			}else{
				final long bodyIdToUse = getNextId("PAGE_BODY");
				getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.INSERT_PAGE_BODY").getSql(), 
						new SqlParameterValue(Types.NUMERIC, bodyIdToUse ),
						new SqlParameterValue(Types.NUMERIC, page.getPageId() ),	
						new SqlParameterValue(Types.INTEGER, page.getBodyContent().getBodyType().getId() ),
						new SqlParameterValue(Types.VARCHAR, page.getBodyContent().getBodyText() )						
					);
				getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.INSERT_PAGE_BODY_VERSION").getSql(), 
						new SqlParameterValue(Types.NUMERIC, bodyId ),	
						new SqlParameterValue(Types.NUMERIC, page.getPageId() ),
						new SqlParameterValue(Types.NUMERIC, prevVersionId )
				);
			}
		}
	}

	public void delete(Page page) {
		
		if( page.getVersionId() == -1){
			List<Long> bodyIds = getExtendedJdbcTemplate().queryForList(
					getBoundSql("ARCHITECTURE_COMMUNITY.SELETE_PAGE_BODY_IDS").getSql(), 
					Long.class, 
					new SqlParameterValue(Types.NUMERIC, page.getPageId() ));
			getExtendedJdbcTemplate().update(
					getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_PAGE_BODY_VERSIONS").getSql(), 
					new SqlParameterValue(Types.NUMERIC, page.getPageId() )
					);
			for( long bodyId : bodyIds){
				getExtendedJdbcTemplate().update(
						getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_PAGE_BODY").getSql(), 
						new SqlParameterValue(Types.NUMERIC, bodyId )
						);
			}
			getExtendedJdbcTemplate().update(
					getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_PAGE_VERSIONS").getSql(), 
					new SqlParameterValue(Types.NUMERIC, page.getPageId() )
					);		
			getExtendedJdbcTemplate().update(
					getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_PAGE_PROPERTIES").getSql(), 
					new SqlParameterValue(Types.NUMERIC, page.getPageId() )
					);
			getExtendedJdbcTemplate().update(
					getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_PAGE").getSql(), 
					new SqlParameterValue(Types.NUMERIC, page.getPageId() )
					);		
		}
	}

	public Page getPageById(long pageId) {		
		return getPageById(pageId, -1);
	}
	
	public Page getPageById(long pageId, int versionNumber) {
		return load( pageId, versionNumber <= 0 ? getPageVersion(pageId) : versionNumber);
	}
	
	private Page load( long pageId, int versionNumber ){
		if( pageId <= 0 )
			return null;
		
		final Page page = new DefaultPage();
		page.setPageId(pageId);
		page.setVersionId(versionNumber);
		getExtendedJdbcTemplate().query(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_PAGE_BY_ID_AND_VERSION").getSql(), 
				new ParameterizedRowMapper<Page>(){
					public Page mapRow(ResultSet rs, int rowNum)
							throws SQLException {						
						page.setName(rs.getString("NAME"));
						page.setObjectType(rs.getInt("OBJECT_TYPE"));
						page.setObjectId(rs.getLong("OBJECT_ID"));
						page.setPageState( PageState.valueOf( rs.getString("STATE").toUpperCase() ));
						page.setUser(new UserTemplate(rs.getLong("USER_ID")) );
						if(rs.wasNull())
							page.setUser(new UserTemplate(-1L));
						page.setTitle(rs.getString("TITLE"));
						page.setSummary(rs.getString("SUMMARY"));
						page.setCreationDate(rs.getDate("CREATION_DATE"));
						page.setModifiedDate(rs.getDate("MODIFIED_DATE"));						
						return page;
					}					
				},
				new SqlParameterValue(Types.NUMERIC, page.getPageId() ),
				new SqlParameterValue(Types.NUMERIC, page.getVersionId() )
		);
		
		if( page.getName() == null )
			return null;
		
		try {
			BodyContent bodyContent = getExtendedJdbcTemplate().queryForObject(
					getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_PAGE_BODY").getSql(), 
					bodyContentMapper,
					new SqlParameterValue(Types.NUMERIC, page.getPageId()), 
					new SqlParameterValue(Types.NUMERIC, page.getVersionId() )
				);
			page.setBodyContent(bodyContent);
		} catch (EmptyResultDataAccessException e) {			
		}	
		if( page.getBodyText() == null ){
			long bodyId = -1L;
			try {
				bodyId = getExtendedJdbcTemplate().queryForLong(
						getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_PAGE_BODY_VERSION").getSql(), 
						new SqlParameterValue(Types.NUMERIC, page.getPageId()), 
						new SqlParameterValue(Types.NUMERIC, page.getVersionId() )
						);
			} catch (EmptyResultDataAccessException e) {			
			}	
		}
		
		loadProperties(page);		
		return page;
	}
	
	public int getPageVersion(long pageId){
		
		PageVersion v = PageVersionHelper.getDeletedPageVersion(pageId);
		if( v == null)
			v = PageVersionHelper.getPublishedPageVersion(pageId);
		if(v == null)
			v = PageVersionHelper.getNewestPageVersion(pageId);
		if(v != null)
			return v.getVersionNumber();
		else
			return -1;		
	}
	
	


	public Page getPageByName(String name) {
		long pageId = -1L;
		try {
			pageId = getExtendedJdbcTemplate().queryForLong(
					getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_PAGE_ID_BY_NAME").getSql(), 		
					new SqlParameterValue(Types.VARCHAR, name )					
			);
		} catch (DataAccessException e) {
			return null;
		}
		
		return getPageById(pageId, -1);
	}

	public Page getPageByName(String name, int versionNumber) {
		long pageId = -1L;
		try {
			pageId = getExtendedJdbcTemplate().queryForLong(
					getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_PAGE_ID_BY_NAME").getSql(), 		
					new SqlParameterValue(Types.VARCHAR, name )					
			);
		} catch (DataAccessException e) {
			return null;
		}		
		return load( pageId, versionNumber );
	}

	public Page getPageByTitle(int objectType, long objectId, String title) {
		Long resutls[] = getExtendedJdbcTemplate().queryForObject(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_PAGE_BY_OBJECT_TYPE_AND_OBJECT_ID_AND_TITLE").getSql(), 	
				new ParameterizedRowMapper<Long[]>(){
					public Long[] mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new Long[]{ rs.getLong("PAGE_ID"), rs.getLong("VERSION_ID") };
					}					
				},
				new SqlParameterValue(Types.NUMERIC, objectType ),
				new SqlParameterValue(Types.NUMERIC, objectId ),
				new SqlParameterValue(Types.VARCHAR, title )
		);
		if( resutls == null || resutls.length == 0 ){
			return null;
		}
		return load(resutls[0].longValue(), resutls[1].intValue());
	}

	public int getPageCount(int objectType, long objectId) {		
		return getExtendedJdbcTemplate().queryForInt(
				getBoundSql("ARCHITECTURE_COMMUNITY.COUNT_PAGE_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 		
				new SqlParameterValue(Types.NUMERIC, objectType ),
				new SqlParameterValue(Types.NUMERIC, objectId )
		);
	}

	public List<Long> getPageIds(int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForList(				
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_PAGE_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 		
				Long.class,
				new SqlParameterValue(Types.NUMERIC, objectType ),
				new SqlParameterValue(Types.NUMERIC, objectId )
				);
	}

}
