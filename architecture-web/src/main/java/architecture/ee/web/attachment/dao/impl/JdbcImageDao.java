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

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.support.SqlLobValue;

import architecture.common.jdbc.schema.DatabaseType;
import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.jdbc.sqlquery.SqlQueryHelper;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.dao.ImageDao;
import architecture.ee.web.attachment.impl.ImageImpl;

public class JdbcImageDao  extends ExtendedJdbcDaoSupport implements ImageDao {

	private String sequencerName = "IMAGE";
	private ExtendedPropertyDao extendedPropertyDao;	
	private String imagementPropertyTableName = "V2_IMAGE_PROPERTY";
	private String imagePropertyPrimaryColumnName = "IMAGE_ID";
	

	public Map<String, String> getImageProperties(long imageId) {
		return extendedPropertyDao.getProperties(imagementPropertyTableName, imagePropertyPrimaryColumnName, imageId);
	}

	public void deleteImageProperties(long imageId) {
		extendedPropertyDao.deleteProperties(imagementPropertyTableName, imagePropertyPrimaryColumnName, imageId);
	}
	
	public void setImageProperties(long imageId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(imagementPropertyTableName, imagePropertyPrimaryColumnName, imageId, props);
	}
	
	
	public String getSequencerName() {
		return sequencerName;
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
	 * @return imagementPropertyTableName
	 */
	public String getImagementPropertyTableName() {
		return imagementPropertyTableName;
	}


	/**
	 * @param imagementPropertyTableName 설정할 imagementPropertyTableName
	 */
	public void setImagementPropertyTableName(String imagementPropertyTableName) {
		this.imagementPropertyTableName = imagementPropertyTableName;
	}


	/**
	 * @return imagePropertyPrimaryColumnName
	 */
	public String getImagePropertyPrimaryColumnName() {
		return imagePropertyPrimaryColumnName;
	}


	/**
	 * @param imagePropertyPrimaryColumnName 설정할 imagePropertyPrimaryColumnName
	 */
	public void setImagePropertyPrimaryColumnName(
			String imagePropertyPrimaryColumnName) {
		this.imagePropertyPrimaryColumnName = imagePropertyPrimaryColumnName;
	}


	public void setSequencerName(String sequencerName) {
		this.sequencerName = sequencerName;
	}
	
	private final RowMapper<Image> imageMapper = new RowMapper<Image>(){
		public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
			ImageImpl image = new ImageImpl();
			image.setImageId(rs.getLong("IMAGE_ID"));
			image.setObjectType(rs.getInt("OBJECT_TYPE"));
			image.setObjectId(rs.getLong("OBJECT_ID"));
			image.setName(rs.getString("FILE_NAME"));
			image.setSize(rs.getInt("FILE_SIZE"));
			image.setContentType(rs.getString("CONTENT_TYPE"));
			image.setCreationDate(rs.getTimestamp("CREATION_DATE"));
			image.setModifiedDate(rs.getTimestamp("MODIFIED_DATE"));			
			return image;
		}		
	};
	
	public Image createImage(Image image) {
		
		Image toUse = image;		
		if( toUse.getImageId() <1L){

			long imageId = getNextId(sequencerName);		
			if( image instanceof ImageImpl){
				ImageImpl impl = (ImageImpl)toUse;
				impl.setImageId(imageId);
			}			
			getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.CREATE_IMAGE").getSql(), 	
					new SqlParameterValue (Types.NUMERIC, imageId), 
					new SqlParameterValue (Types.INTEGER, image.getObjectType() ), 
					new SqlParameterValue (Types.INTEGER, image.getObjectId() ), 
					new SqlParameterValue (Types.VARCHAR, image.getName() ), 
					new SqlParameterValue (Types.INTEGER, image.getSize() ), 
					new SqlParameterValue (Types.VARCHAR, image.getContentType()), 
					new SqlParameterValue(Types.TIMESTAMP, image.getCreationDate()),
					new SqlParameterValue(Types.TIMESTAMP, image.getModifiedDate()));	
			
			if(!image.getProperties().isEmpty())
				setImageProperties(image.getImageId(), image.getProperties());	
		}		
		return  image;
	}

	public Image updateImage(Image image) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.UPDATE_IMAGE").getSql(), 	
				new SqlParameterValue (Types.NUMERIC, image.getImageId()), 
				new SqlParameterValue (Types.INTEGER, image.getObjectType() ), 
				new SqlParameterValue (Types.INTEGER, image.getObjectId() ), 
				new SqlParameterValue (Types.VARCHAR, image.getName() ), 
				new SqlParameterValue (Types.INTEGER, image.getSize() ), 
				new SqlParameterValue (Types.VARCHAR, image.getContentType()), 
				//new SqlParameterValue(Types.TIMESTAMP, image.getCreationDate()),
				new SqlParameterValue(Types.TIMESTAMP, image.getModifiedDate()),
				new SqlParameterValue (Types.NUMERIC, image.getImageId()) );	
		
		setImageProperties(image.getImageId(), image.getProperties());	
		
		return image;
	}
			
	public void deleteImage(Image image) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.DELETE_IMAGE_BY_ID").getSql(), 	
				new SqlParameterValue (Types.NUMERIC, image.getImageId()));
		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.DELETE_IMAGE_DATA_BY_ID").getSql(), 	
				new SqlParameterValue (Types.NUMERIC, image.getImageId()));	
		deleteImageProperties(image.getImageId());
	}

	public InputStream getImageInputStream(Image image) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_WEB.SELECT_IMAGE_DATA_BY_ID").getSql(), SqlQueryHelper.getInputStreamRowMapper(), new SqlParameterValue (Types.NUMERIC, image.getImageId()));		
	}
	
	public void saveImageInputStream(Image image, InputStream inputStream) {		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.DELETE_IMAGE_DATA_BY_ID").getSql(), new SqlParameterValue (Types.NUMERIC, image.getImageId()));		
		if( getExtendedJdbcTemplate().getDatabaseType() == DatabaseType.oracle ){						
			
			getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.CREATE_EMPTY_IMAGE_DATA").getSql(), new SqlParameterValue (Types.NUMERIC, image.getImageId()));
			getExtendedJdbcTemplate().update(
					getBoundSql("ARCHITECTURE_WEB.UPDATE_IMAGE_DATA").getSql(), 
					new Object[]{
						new SqlLobValue( inputStream , image.getSize(), getLobHandler()),
						image.getImageId()
					}, 
					new int[]{
						Types.BLOB,
						Types.NUMERIC
					});
		}else{			
			getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.CREATE_IMAGE_DATA").getSql(), 
					new SqlParameterValue ( Types.NUMERIC, image.getImageId()), 
					new SqlParameterValue ( Types.BLOB,  new SqlLobValue( inputStream , image.getSize(), getLobHandler() ) ) 
			);
		}		
	}

	public Image getImageById(long imageId) {				
		Image image =  getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_WEB.SELECT_IMAGE_BY_ID").getSql(), imageMapper, new SqlParameterValue (Types.NUMERIC, imageId ));			
		image.setProperties( this.getImageProperties(image.getImageId()) );
		return image;
		
	}
	
	public List<Long> getImageIds(int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("ARCHITECTURE_WEB.SELECT_IMAGE_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 				
				Long.class, new SqlParameterValue (Types.NUMERIC, objectType ), new SqlParameterValue (Types.NUMERIC, objectId ));	
	}
	
	public List<Long> getImageIds(int objectType, long objectId, int startIndex, int numResults) {
		return getExtendedJdbcTemplate().queryScrollable(
				getBoundSql("ARCHITECTURE_WEB.SELECT_IMAGE_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				startIndex, 
				numResults, 
				new Object[ ] {objectType, objectId }, 
				new int[] {Types.NUMERIC, Types.NUMERIC}, 
				Long.class);
	}

	public int getImageCount(int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForInt(
				getBoundSql("ARCHITECTURE_WEB.COUNT_IMAGE_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				new SqlParameterValue(Types.NUMERIC, objectType ), 
				new SqlParameterValue(Types.NUMERIC, objectId ));
	}

	@Override
	public long getImageUsage(int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForObject(
				getBoundSql("ARCHITECTURE_WEB.USAGE_IMAGE_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				Long.class,
				new SqlParameterValue(Types.NUMERIC, objectType ), 
				new SqlParameterValue(Types.NUMERIC, objectId ));
	}
	
}
