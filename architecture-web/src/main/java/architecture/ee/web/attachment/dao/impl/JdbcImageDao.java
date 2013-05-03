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

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.support.SqlLobValue;

import architecture.common.jdbc.schema.DatabaseType;
import architecture.common.util.io.SharedByteArrayOutputStream;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.dao.ImageDao;
import architecture.ee.web.attachment.impl.ImageImpl;

public class JdbcImageDao  extends ExtendedJdbcDaoSupport implements ImageDao {

	private String sequencerName = "IMAGE";
	
	private final RowMapper<Image> imageMapper = new RowMapper<Image>(){
		public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
			ImageImpl image = new ImageImpl();
			image.setImageId(rs.getLong("IMAGE_ID"));
			image.setObjectType(rs.getInt("OBJECT_TYPE"));
			image.setObjectId(rs.getLong("OBJECT_ID"));
			image.setName(rs.getString("FILE_NAME"));
			image.setSize(rs.getInt("FILE_SIZE"));
			image.setContentType(rs.getString("CONTENT_TYPE"));
			image.setCreationDate(rs.getDate("CREATION_DATE"));
			image.setModifiedDate(rs.getDate("MODIFIED_DATE"));			
			return image;
		}		
	};
	
	private final RowMapper<InputStream> inputStreamMapper = new RowMapper<InputStream>(){		
		public InputStream mapRow(ResultSet rs, int rowNum) throws SQLException {
			InputStream in = null;
			InputStream inputstream;
			try
            {
                in = rs.getBinaryStream(1);
                SharedByteArrayOutputStream out = new SharedByteArrayOutputStream();
                byte buf[] = new byte[1024];
                int len;
                while((len = in.read(buf)) >= 0) 
                    out.write(buf, 0, len);
                out.flush();
                inputstream = out.getInputStream();
            }
            catch(IOException ioe)
            {
                throw new SQLException(ioe.getMessage());
            }
			return inputstream;
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
					new SqlParameterValue(Types.DATE, image.getCreationDate()),
					new SqlParameterValue(Types.DATE, image.getModifiedDate()));	
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
				new SqlParameterValue(Types.DATE, image.getCreationDate()),
				new SqlParameterValue(Types.DATE, image.getModifiedDate()),
				new SqlParameterValue (Types.NUMERIC, image.getImageId()) );	
		return image;
	}
			
	public void deleteImage(Image image) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.DELETE_IMAGE_BY_ID").getSql(), 	
				new SqlParameterValue (Types.NUMERIC, image.getImageId()));
		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.DELETE_IMAGE_DATA_BY_ID").getSql(), 	
				new SqlParameterValue (Types.NUMERIC, image.getImageId()));	
	}

	public InputStream getImageInputStream(Image image) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_WEB.SELECT_IMAGE_DATA_BY_ID").getSql(), inputStreamMapper, new SqlParameterValue (Types.NUMERIC, image.getImageId()));		
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
			
			
			/**
			.update(getBoundSql("ARCHITECTURE_WEB.UPDATE_IMAGE_DATA").getSql(), 
					new SqlParameterValue (Types.BLOB,  new SqlLobValue( inputStream , image.getSize(), getLobHandler())), 
					new SqlParameterValue (Types.NUMERIC, image.getImageId())
			);	
			**/

		}else{			
			getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.CREATE_IMAGE_DATA").getSql(), 
					new SqlParameterValue ( Types.NUMERIC, image.getImageId()), 
					new SqlParameterValue ( Types.BLOB,  new SqlLobValue( inputStream , image.getSize(), getLobHandler() ) ) 
			);
		}		
	}

	public Image getImageById(long imageId) {		
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_WEB.SELECT_IMAGE_BY_ID").getSql(), imageMapper, new SqlParameterValue (Types.NUMERIC, imageId ));			
	}
	
	
	
}
