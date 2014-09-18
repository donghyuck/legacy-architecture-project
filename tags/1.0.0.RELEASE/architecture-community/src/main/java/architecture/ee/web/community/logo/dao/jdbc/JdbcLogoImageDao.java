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
package architecture.ee.web.community.logo.dao.jdbc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.support.SqlLobValue;

import architecture.common.jdbc.schema.DatabaseType;
import architecture.ee.jdbc.sqlquery.SqlQueryHelper;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.community.logo.DefaultLogoImage;
import architecture.ee.web.community.logo.dao.LogoImageDao;
import architecture.ee.web.logo.LogoImage;
import architecture.ee.web.logo.LogoImageNotFoundException;

public class JdbcLogoImageDao extends ExtendedJdbcDaoSupport implements LogoImageDao {

	private final RowMapper<LogoImage> logoMapper = new RowMapper<LogoImage>(){
		public LogoImage mapRow(ResultSet rs, int rowNum) throws SQLException {			
			DefaultLogoImage image = new DefaultLogoImage();
			image.setLogoId(rs.getLong("LOGO_ID"));
			image.setObjectType(rs.getInt("OBJECT_TYPE"));
			image.setObjectId(rs.getLong("OBJECT_ID"));
			image.setFilename(rs.getString("FILE_NAME"));
			image.setPrimary((rs.getInt("PRIMARY_IMAGE") == 1 ? true : false));
			image.setImageSize(rs.getInt("FILE_SIZE"));
			image.setImageContentType(rs.getString("CONTENT_TYPE"));
			image.setCreationDate(rs.getTimestamp("CREATION_DATE"));
			image.setModifiedDate(rs.getTimestamp("MODIFIED_DATE"));			
			return image;			
		}		
	};
	
	private String sequencerName = "LOGO";
	
	public JdbcLogoImageDao() {
		
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

	public Long nextId( ){
		return getNextId(sequencerName);
	}

	public void addLogoImage(LogoImage logoImage, File file) {
		
		try {
			LogoImage toUse = logoImage;					
			long logoIdToUse = logoImage.getLogoId();
			if( logoIdToUse < 1 ){
				logoIdToUse = getNextId(sequencerName);
				logoImage.setLogoId(logoIdToUse);
			}			
			if( toUse.getImageSize() == 0)
				toUse.setImageSize((int) FileUtils.sizeOf(file));
				getExtendedJdbcTemplate().update(
					getBoundSql("ARCHITECTURE_COMMUNITY.RESET_LOGO_IMAGE_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 	
					new SqlParameterValue (Types.INTEGER, toUse.getObjectType() )	,
					new SqlParameterValue (Types.NUMERIC, toUse.getObjectId() )	
			);			
				
			getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.CREATE_LOGO_IMAGE").getSql(), 	
				new SqlParameterValue (Types.NUMERIC, logoImage.getLogoId()),
				new SqlParameterValue(Types.NUMERIC, logoImage.getObjectType()),
				new SqlParameterValue(Types.NUMERIC, logoImage.getObjectId()),
				new SqlParameterValue(Types.NUMERIC, logoImage.isPrimary() ? 1 : 0),
				new SqlParameterValue(Types.VARCHAR, logoImage.getFilename()),
				new SqlParameterValue(Types.NUMERIC, logoImage.getImageSize()),
				new SqlParameterValue(Types.VARCHAR, logoImage.getImageContentType()),
				new SqlParameterValue(Types.DATE, logoImage.getModifiedDate()),
				new SqlParameterValue(Types.DATE, logoImage.getCreationDate())
			);			
			
			//log.debug("file:" + file.getAbsolutePath());
			
			updateImageImputStream(logoImage, FileUtils.openInputStream(file) );
		} catch (DataAccessException e) {
		} catch (IOException e) {
		}		
	}	

	protected void updateImageImputStream(LogoImage logoImage, InputStream inputStream){
		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_LOGO_IMAGE_DATA_BY_ID").getSql(), new SqlParameterValue (Types.NUMERIC, logoImage.getLogoId()));		
		if( getExtendedJdbcTemplate().getDatabaseType() == DatabaseType.oracle ){				
			getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.INSERT_EMPTY_LOGO_IMAGE_DATA").getSql(), new SqlParameterValue (Types.NUMERIC, logoImage.getLogoId()));
			getExtendedJdbcTemplate().update(
					getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_LOGO_IMAGE_DATA").getSql(), 
					new Object[]{
						new SqlLobValue( inputStream , logoImage.getImageSize(), getLobHandler()),
						logoImage.getLogoId()
					}, 
					new int[]{
						Types.BLOB,
						Types.NUMERIC
					});
		}else{			
			getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.INSERT_LOGO_IMAGE_DATA").getSql(), 
					new SqlParameterValue ( Types.NUMERIC, logoImage.getLogoId()), 
					new SqlParameterValue ( Types.BLOB,  new SqlLobValue( inputStream , logoImage.getImageSize(), getLobHandler() ) ) 
			);
		}				
	}

	public void removeLogoImage(LogoImage logoImage) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_LOGO_IMAGE_BY_ID").getSql(), new SqlParameterValue (Types.NUMERIC, logoImage.getLogoId()));		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_LOGO_IMAGE_DATA_BY_ID").getSql(),  new SqlParameterValue (Types.NUMERIC, logoImage.getLogoId()));			
	}

	public InputStream getInputStream(LogoImage logoImage) throws IOException {
		return getExtendedJdbcTemplate().queryForObject(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_LOGO_IMAGE_DATA_BY_ID").getSql(), 
				SqlQueryHelper.getInputStreamRowMapper(), 
				new SqlParameterValue (Types.NUMERIC, logoImage.getLogoId()));		
	}

	public Long getPrimaryLogoImageId(int objectType, long objectId)
			throws LogoImageNotFoundException {
		try{
			return getExtendedJdbcTemplate().queryForLong(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_PRIMARY_LOGO_IMAGE_ID_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(),
				new SqlParameterValue (Types.NUMERIC, objectType ),
				new SqlParameterValue (Types.NUMERIC, objectId )	);			
		} catch (DataAccessException e) {
			throw new LogoImageNotFoundException(e);
		}
	}

	public LogoImage getLogoImageById(long logoId) throws LogoImageNotFoundException {
		try{
			return getExtendedJdbcTemplate().queryForObject(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_LOGO_IMAGE_BY_ID").getSql(), 
				logoMapper, 
				new SqlParameterValue (Types.NUMERIC, logoId ));			
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new LogoImageNotFoundException(e);
		}
	}

	public List<Long> getLogoImageIds(int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForList(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_LOGO_IMAGE_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 				
				Long.class, 
				new SqlParameterValue(Types.NUMERIC, objectType ),
				new SqlParameterValue(Types.NUMERIC, objectId ));
	}

	public int getLogoImageCount(int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForInt(
				getBoundSql("ARCHITECTURE_COMMUNITY.COUNT_LOGO_IMAGE_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				new SqlParameterValue(Types.NUMERIC, objectType ),
				new SqlParameterValue(Types.NUMERIC, objectId ))
				;
	}

}
