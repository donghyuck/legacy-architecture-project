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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.common.user.UserTemplate;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.community.page.DefaultPage;
import architecture.ee.web.community.page.DefaultPageVersion;
import architecture.ee.web.community.page.PageState;
import architecture.ee.web.community.page.PageVersion;
import architecture.ee.web.community.page.dao.PageVersionDao;

public class JdbcPageVersionDao extends ExtendedJdbcDaoSupport  implements PageVersionDao {

	private final RowMapper<PageVersion> pageVersionMapper = new RowMapper<PageVersion>(){
		public PageVersion mapRow(ResultSet rs, int rowNum) throws SQLException {
			DefaultPageVersion version = new DefaultPageVersion();
			version.setPage(new DefaultPage(rs.getLong("PAGE_ID")));
			version.setVersionNumber( rs.getInt("VERSION_ID"));
			version.setPageState(PageState.valueOf(rs.getString("STATE")));
			version.setCreationDate(rs.getDate("CREATION_DATE"));
			version.setModifiedDate(rs.getDate("MODIFIED_DATE"));
			version.setAuthor(new UserTemplate(rs.getLong("USER_ID")));
			return version;
		}		
	};
		
	public void update(PageVersion pageVersion) {
		if( pageVersion.getPageState() == PageState.PUBLISHED){
			getExtendedJdbcTemplate().update(
					getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_PAGE_STATE_TO_ARCHIVED").getSql(),	
					new SqlParameterValue(Types.NUMERIC, pageVersion.getPage().getPageId() ),
					new SqlParameterValue(Types.NUMERIC, pageVersion.getVersionNumber() )
			);
			getExtendedJdbcTemplate().update(
					getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_PAGE_STATE").getSql(),	
					new SqlParameterValue(Types.NUMERIC, pageVersion.getAuthor()),
					new SqlParameterValue(Types.DATE, pageVersion.getCreationDate()),
					new SqlParameterValue(Types.DATE, pageVersion.getModifiedDate()),
					new SqlParameterValue(Types.VARCHAR, pageVersion.getPageState().name().toLowerCase()),
					new SqlParameterValue(Types.NUMERIC, pageVersion.getPage().getPageId() ),
					new SqlParameterValue(Types.NUMERIC, pageVersion.getVersionNumber() )
			);
		}
	}

	public void delete(PageVersion pageVersion) {
		
	}
	
	public PageVersion getPageVersion(long pageId, int versionNumber) {
		return getExtendedJdbcTemplate().queryForObject(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_PAGE_BY_ID_AND_VERSION").getSql(),
				pageVersionMapper, 
				new SqlParameterValue(Types.NUMERIC, pageId ),
				new SqlParameterValue(Types.NUMERIC, versionNumber )
		);
	}

	public List<PageVersion> getPageVersions(long pageId) {
		return getExtendedJdbcTemplate().query(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_PAGE_VERSIONS").getSql(),
				pageVersionMapper,
				new SqlParameterValue(Types.NUMERIC, pageId )
		);
	}

	public List<Integer> getPageVersionIds(long pageId) {
		return getExtendedJdbcTemplate().queryForList(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_PAGE_VERSION_IDS").getSql(),
				Integer.class, 
				new SqlParameterValue(Types.NUMERIC, pageId )
				);
	}



}
