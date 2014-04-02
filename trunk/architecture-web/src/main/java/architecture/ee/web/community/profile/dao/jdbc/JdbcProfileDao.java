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
package architecture.ee.web.community.profile.dao.jdbc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.support.SqlLobValue;

import architecture.common.jdbc.schema.DatabaseType;
import architecture.ee.jdbc.sqlquery.SqlQueryHelper;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.community.ProfileImageNotFoundException;
import architecture.ee.web.community.profile.DefaultProfileImage;
import architecture.ee.web.community.profile.ProfileImage;
import architecture.ee.web.community.profile.dao.ProfileDao;

public class JdbcProfileDao extends ExtendedJdbcDaoSupport  implements ProfileDao {
	
	private String sequencerName = "PROFILE_IMAGE";
			
	private final RowMapper<ProfileImage> imageMapper = new RowMapper<ProfileImage>(){
		public ProfileImage mapRow(ResultSet rs, int rowNum) throws SQLException {			
			DefaultProfileImage image = new DefaultProfileImage();
			image.setProfileImageId(rs.getLong("PROFILE_IMAGE_ID"));
			image.setUserId(rs.getLong("USER_ID"));
			image.setFilename(rs.getString("FILE_NAME"));
			image.setPrimary((rs.getInt("PRIMARY_IMAGE") == 1 ? true : false));
			image.setImageSize(rs.getInt("FILE_SIZE"));
			image.setImageContentType(rs.getString("CONTENT_TYPE"));
			image.setCreationDate(rs.getTimestamp("CREATION_DATE"));
			image.setModifiedDate(rs.getTimestamp("MODIFIED_DATE"));			
			return image;			
		}		
	};
	
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

	public void removeProfileImage(ProfileImage image) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_PROFILE_IMAGE_BY_ID").getSql(), 	
				new SqlParameterValue (Types.NUMERIC, image.getProfileImageId()));
		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_PROFILE_IMAGE_DATA_BY_ID").getSql(), 	
				new SqlParameterValue (Types.NUMERIC, image.getProfileImageId()));		
	}

	public void addProfileImage(ProfileImage image, File file) {
		try {		
			ProfileImage toUse = image;			
			if( toUse.getProfileImageId()<1L){
				long imageId = getNextId(sequencerName);		
				if( image instanceof DefaultProfileImage){
					DefaultProfileImage impl = (DefaultProfileImage)toUse;
					impl.setProfileImageId(imageId);
				}	
			}else{
				Date now = Calendar.getInstance().getTime();
				toUse.setModifiedDate(now);
			}

			if( toUse.getImageSize() == 0)
				toUse.setImageSize((int) FileUtils.sizeOf(file));
				getExtendedJdbcTemplate().update(
					getBoundSql("ARCHITECTURE_COMMUNITY.RESET_PROFILE_IMAGE_BY_USER").getSql(), 	
					new SqlParameterValue (Types.INTEGER, toUse.getUserId() )	
			);
			getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.INSERT_PROFILE_IMAGE").getSql(), 	
						new SqlParameterValue (Types.NUMERIC, toUse.getProfileImageId()), 
						new SqlParameterValue (Types.INTEGER, toUse.getUserId() ), 
						new SqlParameterValue (Types.VARCHAR, toUse.getFilename() ), 
						new SqlParameterValue (Types.INTEGER, toUse.getImageSize()), 
						new SqlParameterValue (Types.VARCHAR, toUse.getImageContentType()), 
						new SqlParameterValue(Types.TIMESTAMP, toUse.getCreationDate()),
						new SqlParameterValue(Types.TIMESTAMP, toUse.getModifiedDate()));						
			updateProfileImageImputStream(image, FileUtils.openInputStream(file) );
			
		} catch (IOException e) {
		}
	}

	public ProfileImage getProfileImageById(Long profileImageId) throws ProfileImageNotFoundException {
		try{
			return getExtendedJdbcTemplate().queryForObject(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_PROFILE_IMAGE_BY_ID").getSql(), 
				imageMapper, 
				new SqlParameterValue (Types.NUMERIC, profileImageId ));			
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ProfileImageNotFoundException(e);
		}
	}

	public List<Long> getProfileImageIds(Long userId) {
		return getExtendedJdbcTemplate().queryForList(
			getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_PROFILE_IMAGE_IDS_BY_USER").getSql(), 				
			Long.class, 
			new SqlParameterValue (Types.NUMERIC, userId ));	
	}

	public Integer getProfileImageCount(Long userId) {
		return getExtendedJdbcTemplate().queryForInt(
				getBoundSql("ARCHITECTURE_COMMUNITY.COUNT_PROFILE_IMAGE_BY_USER").getSql(), 
				new SqlParameterValue(Types.NUMERIC, userId ));
	}

	public InputStream getInputStream(ProfileImage image) {
		return getExtendedJdbcTemplate().queryForObject(
			getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_PROFILE_IMAGE_DATA_BY_ID").getSql(), 
			SqlQueryHelper.getInputStreamRowMapper(), 
			new SqlParameterValue (Types.NUMERIC, image.getProfileImageId()));				
	}
	
	protected void updateProfileImageImputStream(ProfileImage image, InputStream inputStream){
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_PROFILE_IMAGE_DATA_BY_ID").getSql(), new SqlParameterValue (Types.NUMERIC, image.getProfileImageId()));		
		if( getExtendedJdbcTemplate().getDatabaseType() == DatabaseType.oracle ){				
			getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.INSERT_EMPTY_PROFILE_IMAGE_DATA").getSql(), new SqlParameterValue (Types.NUMERIC, image.getProfileImageId()));
			getExtendedJdbcTemplate().update(
					getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_PROFILE_IMAGE_DATA").getSql(), 
					new Object[]{
						new SqlLobValue( inputStream , image.getImageSize(), getLobHandler()),
						image.getProfileImageId()
					}, 
					new int[]{
						Types.BLOB,
						Types.NUMERIC
					});
		}else{			
			getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.INSERT_PROFILE_IMAGE_DATA").getSql(), 
					new SqlParameterValue ( Types.NUMERIC, image.getProfileImageId()), 
					new SqlParameterValue ( Types.BLOB,  new SqlLobValue( inputStream , image.getImageSize(), getLobHandler() ) ) 
			);
		}				
	}

	public Long getPrimaryProfileImageByUser(Long userId)
			throws ProfileImageNotFoundException {
		try{
			return getExtendedJdbcTemplate().queryForLong(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_PRIMARY_PROFILE_IMAGE_ID_BY_USER").getSql(), 
				imageMapper, 
				new SqlParameterValue (Types.NUMERIC, userId ));			
		} catch (DataAccessException e) {
			throw new ProfileImageNotFoundException(e);
		}
	}
}