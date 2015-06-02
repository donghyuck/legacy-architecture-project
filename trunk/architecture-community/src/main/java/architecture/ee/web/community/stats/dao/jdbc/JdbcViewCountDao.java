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
package architecture.ee.web.community.stats.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.community.stats.ViewCountManager.ViewCountInfo;
import architecture.ee.web.community.stats.dao.ViewCountDao;

public class JdbcViewCountDao extends ExtendedJdbcDaoSupport  implements ViewCountDao {

	public JdbcViewCountDao() {
	}

	@Override
	public int getViewCount(int objectType, long objectId, long parentObjectId) {
		
		int count = 0 ;
		
		try {
			count = getExtendedJdbcTemplate().queryForObject(
					getBoundSql("ARCHITECTURE_COMMUNITY.VIEW_COUNT_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 	
					Integer.class,
					new SqlParameterValue(Types.NUMERIC, objectType ),
					new SqlParameterValue(Types.NUMERIC, objectId )
			);
		} catch (IncorrectResultSizeDataAccessException e) {
			insertInitialViewCount(objectType, objectId, parentObjectId, 0 );
		}
		
		return count;
	}
	
	
	public void deleteViewCount(int objectType, long objectId){
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_VIEW_COUNT").getSql(), 
				new SqlParameterValue(Types.NUMERIC, objectType ),
				new SqlParameterValue(Types.NUMERIC, objectId ));
	}
	
	public void updateViewCounts(final List<ViewCountInfo> views){
		getExtendedJdbcTemplate().batchUpdate(
			
			getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_VIEW_COUNT").getSql(), 
			new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					ViewCountInfo c = views.get(i);
					ps.setInt(1,  c.getCount());
					ps.setInt(2, c.getObjectType());
					ps.setLong(3, c.getObjectId());
				}
				@Override
				public int getBatchSize() {
					return views.size();
				}}
		);
	}
	
	public void insertInitialViewCount( int objectType, long objectId, long parentObjectId, int count){
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.CREATE_VIEW_COUNT").getSql(), 
				new SqlParameterValue(Types.NUMERIC, objectType ),
				new SqlParameterValue(Types.NUMERIC, objectId ),
				//new SqlParameterValue(Types.NUMERIC, parentObjectId ),
				new SqlParameterValue(Types.NUMERIC, count )
		);
	}
}
