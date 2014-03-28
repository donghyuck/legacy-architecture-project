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
package architecture.ee.web.attachment.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.ImageLink;
import architecture.ee.web.attachment.dao.ImageLinkDao;

public class JdbcImageLinkDao extends ExtendedJdbcDaoSupport implements ImageLinkDao {

	private final RowMapper<ImageLink> imageLinkMapper = new RowMapper<ImageLink>(){
		public ImageLink mapRow(ResultSet rs, int rowNum) throws SQLException {
			ImageLink link = new ImageLink(
				rs.getString("LINK_ID"),	
				rs.getLong("IMAGE_ID"),
				rs.getInt("PUBLIC_SHARED") == 1 
			);			
			return link;
		}		
	};
	
	public ImageLink getImageLinkByImageId(Long imageId) {				
		ImageLink link =  getExtendedJdbcTemplate().queryForObject(
				getBoundSql("ARCHITECTURE_WEB.SELECT_IMAGE_LINK_BY_IMAGE_ID").getSql(), 
				imageLinkMapper, 
				new SqlParameterValue (Types.INTEGER, imageId ));			
		return link;		
	}
	
	public ImageLink getImageLink(String linkId) {				
		ImageLink link =  getExtendedJdbcTemplate().queryForObject(
				getBoundSql("ARCHITECTURE_WEB.SELECT_IMAGE_LINK_BY_LINK_ID").getSql(), 
				imageLinkMapper, 
				new SqlParameterValue (Types.VARCHAR, linkId ));			
		return link;		
	}
	
	public void saveImageLink(ImageLink link) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.INSERT_IMAGE_LINK").getSql(), 	
					new SqlParameterValue (Types.VARCHAR, link.getLinkId() ), 
					new SqlParameterValue (Types.INTEGER, link.getImageId() ), 
					new SqlParameterValue (Types.INTEGER, link.isPublicShared() ? 1 : 0  ) );
	}	
	
	public void removeImageLink(ImageLink link) {
		getExtendedJdbcTemplate().update(
				getBoundSql("ARCHITECTURE_WEB.DELETE_IMAGE_LINK_BY_LINK_ID").getSql(), 	
				new SqlParameterValue (Types.VARCHAR, link.getLinkId() ));		
	}
	
	public void removeImageLink(Image image) {
		getExtendedJdbcTemplate().update(
				getBoundSql("ARCHITECTURE_WEB.DELETE_IMAGE_LINK_BY_IMAGE_ID").getSql(), 	
				new SqlParameterValue (Types.NUMERIC, image.getImageId() ));		
		
	}
}
