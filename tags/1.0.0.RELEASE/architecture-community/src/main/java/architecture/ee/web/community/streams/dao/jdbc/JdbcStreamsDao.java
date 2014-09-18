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
package architecture.ee.web.community.streams.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.community.streams.Photo;
import architecture.ee.web.community.streams.PhotoNotFoundException;
import architecture.ee.web.community.streams.dao.StreamsDao;
import architecture.ee.web.community.streams.impl.PhotoImpl;

public class JdbcStreamsDao extends ExtendedJdbcDaoSupport implements StreamsDao  {

	private final RowMapper<Photo> photoMapper = new RowMapper<Photo>(){
		public Photo mapRow(ResultSet rs, int rowNum) throws SQLException {
			PhotoImpl photo = new PhotoImpl(
				rs.getString("EXTERNAL_ID"),
				rs.getLong("IMAGE_ID"),
				true,
				rs.getLong("CREATOR")
			);	
			
			photo.setPublicShared( rs.getInt("PUBLIC_SHARED") == 1 ); 
			photo.setCreationDate(rs.getDate("CREATION_DATE"));
			photo.setModifiedDate(rs.getDate("MODIFIED_DATE"));					
			return photo;
		}		
	};
	
	public List<String> getPhotoIds() {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_ALL_STREAM_PHOTO_IDS").getSql(), String.class);	
	}

	public List<String> getPhotoIds(int objectType) {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_STREAM_PHOTO_IDS_BY_OBJECT_TYPE").getSql(), 				
				String.class, new SqlParameterValue (Types.NUMERIC, objectType ), new SqlParameterValue (Types.NUMERIC, objectType ));	
	}

	public List<String> getPhotoIds(int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_STREAM_PHOTO_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 				
				String.class, new SqlParameterValue (Types.NUMERIC, objectType ), new SqlParameterValue (Types.NUMERIC, objectId ));	
	}

	public Photo getPhotoStream(String photoId) throws PhotoNotFoundException {
		Photo photo = getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_STREAM_PHOTO_BY_ID").getSql(), photoMapper, new SqlParameterValue (Types.VARCHAR, photoId ));		
		return photo;		
	}

	public void addPhoto(Photo photo) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.CREATE_STREAM_PHOTO").getSql(), 	
				new SqlParameterValue (Types.VARCHAR, photo.getExternalId()), 
				new SqlParameterValue (Types.INTEGER, photo.getImageId() ), 
				new SqlParameterValue (Types.INTEGER, photo.isPublicShared() ? 1 : 0 ), 
				new SqlParameterValue (Types.INTEGER, photo.getCreator().getUserId()), 				
				new SqlParameterValue(Types.DATE, photo.getCreationDate()),
				new SqlParameterValue(Types.DATE, photo.getModifiedDate()));	
	}

	public void removePhoto(Photo photo) {
		getExtendedJdbcTemplate().update(
				getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_STREAM_PHOTO_BY_ID").getSql(), 	
				new SqlParameterValue (Types.VARCHAR, photo.getExternalId() ));		
	}

	public int getPhotoCount() {
		return getExtendedJdbcTemplate().queryForInt(
				getBoundSql("ARCHITECTURE_COMMUNITY.COUNT_ALL_STREAM_PHOTO").getSql());
	}

	public int getPhotoCount(int objectType) {
		return getExtendedJdbcTemplate().queryForInt(
				getBoundSql("ARCHITECTURE_COMMUNITY.COUNT_STREAM_PHOTO_BY_OBJECT_TYPE").getSql(), 
				new SqlParameterValue(Types.NUMERIC, objectType ));
	}

	public int getPhotoCount(int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForInt(
				getBoundSql("ARCHITECTURE_COMMUNITY.COUNT_STREAM_PHOTO_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				new SqlParameterValue(Types.NUMERIC, objectType ), 
				new SqlParameterValue(Types.NUMERIC, objectId ));
	}

	public List<String> getPhotoIds(int startIndex, int maxResults) {
		
		return getExtendedJdbcTemplate().queryScrollable(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_ALL_STREAM_PHOTO_IDS").getSql(), 
				startIndex, 
				maxResults, 
				new Object[0], 
				new int[0], 
				String.class);
		
	}

	public List<String> getPhotoIds(int objectType, int startIndex, int maxResults) {
		return getExtendedJdbcTemplate().queryScrollable(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_STREAM_PHOTO_IDS_BY_OBJECT_TYPE").getSql(), 
				startIndex, 
				maxResults, 
				new Object[ ] {objectType }, 
				new int[] {Types.NUMERIC}, 
				String.class);
	}

	public List<String> getPhotoIds(int objectType, long objectId, int startIndex, int maxResults) {
		return getExtendedJdbcTemplate().queryScrollable(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_STREAM_PHOTO_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				startIndex, 
				maxResults, 
				new Object[ ] {objectType, objectId }, 
				new int[] {Types.NUMERIC, Types.NUMERIC}, 
				String.class);
	}

	public List<String> getPhotoIdsByImage(long imageId) {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_STREAM_PHOTO_IDS_BY_IMAGE_ID").getSql(), 				
				String.class, new SqlParameterValue (Types.NUMERIC, imageId ));	
	}

	public void removePhotos(Image image) {
		getExtendedJdbcTemplate().update(
				getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_STREAM_PHOTOS_BY_IMAGE_ID").getSql(), 	
				new SqlParameterValue (Types.NUMERIC, image.getImageId() ));		
		
	}

}