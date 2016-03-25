/*
 * Copyright 2016 donghyuck
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

package architecture.ee.web.photo.album.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.common.user.User;
import architecture.common.user.UserTemplate;
import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.photo.album.Album;
import architecture.ee.web.photo.album.DefaultAlbum;
import architecture.ee.web.photo.album.dao.AlbumDao;

public class JdbcAlbumDao extends ExtendedJdbcDaoSupport implements AlbumDao {

    private ExtendedPropertyDao extendedPropertyDao;
    private String sequencerName = "ALBUM";
    private String albumPropertyTableName = "V2_ALBUM_PROPERTY";
    private String albumPropertyPrimaryColumnName = "ALBUM_ID";

    private final RowMapper<Album> albumMapper = new RowMapper<Album>() {
	public Album mapRow(ResultSet rs, int rowNum) throws SQLException {
	    long albumId = rs.getLong("ALBUM_ID");
	    DefaultAlbum album = new DefaultAlbum(albumId);
	    album.setName(rs.getString("NAME"));
	    album.setDescription(rs.getString("DESCRIPTION"));
	    album.setUser(new UserTemplate(rs.getLong("USER_ID")));
	    album.setShared(rs.getInt("SHARED") == 1 ? true : false);
	    album.setCollaborate(rs.getInt("COLLABORATE") == 1 ? true : false);
	    album.setCreationDate(rs.getDate("CREATION_DATE"));
	    album.setModifiedDate(rs.getDate("MODIFIED_DATE"));
	    return album;
	}
    };
    
    

    public JdbcAlbumDao() {

    }

    
    public String getSequencerName() {
        return sequencerName;
    }


    public void setSequencerName(String sequencerName) {
        this.sequencerName = sequencerName;
    }


    public String getAlbumPropertyTableName() {
        return albumPropertyTableName;
    }


    public void setAlbumPropertyTableName(String albumPropertyTableName) {
        this.albumPropertyTableName = albumPropertyTableName;
    }


    public String getAlbumPropertyPrimaryColumnName() {
        return albumPropertyPrimaryColumnName;
    }


    public void setAlbumPropertyPrimaryColumnName(String albumPropertyPrimaryColumnName) {
        this.albumPropertyPrimaryColumnName = albumPropertyPrimaryColumnName;
    }


    public ExtendedPropertyDao getExtendedPropertyDao() {
	return extendedPropertyDao;
    }

    public void setExtendedPropertyDao(ExtendedPropertyDao extendedPropertyDao) {
	this.extendedPropertyDao = extendedPropertyDao;
    }

    public Map<String, String> getAlbumProperties(long albumId) {
	return extendedPropertyDao.getProperties(albumPropertyTableName, albumPropertyPrimaryColumnName, albumId);
    }

    public void setAlbumProperties(long albumId, Map<String, String> props) {
	extendedPropertyDao.updateProperties(albumPropertyTableName, albumPropertyPrimaryColumnName, albumId, props);
    }

    public void deleteAlbumProperties(long albumId) {
	extendedPropertyDao.deleteProperties(albumPropertyTableName, albumPropertyPrimaryColumnName, albumId);
    }
    
    public Long nextAlbumId() {
	return getNextId(sequencerName);
    }


    public void saveOrUpdateAlbum(Album album) {
	if(album.getAlbumId() > 0 )
	{
	    getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.UPDATE_ALBUM").getSql(),
		new SqlParameterValue(Types.VARCHAR, album.getName()),
		new SqlParameterValue(Types.VARCHAR, album.getDescription()),
		new SqlParameterValue(Types.NUMERIC, album.isShared() ? 1 : 0),
		new SqlParameterValue(Types.NUMERIC, album.isCollaborate() ? 1 : 0),
		new SqlParameterValue(Types.TIMESTAMP, album.getModifiedDate()),
		new SqlParameterValue(Types.NUMERIC, album.getAlbumId()));
	    deleteAlbumProperties(album.getAlbumId());
	    if (album.getProperties().size() > 0)
		setAlbumProperties(album.getAlbumId(), album.getProperties());
	}else{
	    album.setAlbumId(nextAlbumId());
	    getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.INSERT_ALBUM").getSql(),
		new SqlParameterValue(Types.NUMERIC, album.getAlbumId()),
		new SqlParameterValue(Types.NUMERIC, album.getUser().getUserId()),
		new SqlParameterValue(Types.VARCHAR, album.getName()),
		new SqlParameterValue(Types.VARCHAR, album.getDescription()),
		new SqlParameterValue(Types.NUMERIC, album.isShared() ? 1 : 0),
		new SqlParameterValue(Types.NUMERIC, album.isCollaborate() ? 1 : 0),
		new SqlParameterValue(Types.TIMESTAMP, album.getCreationDate()),
		new SqlParameterValue(Types.TIMESTAMP, album.getModifiedDate()));	 
	    if (album.getProperties().size() > 0)
		setAlbumProperties(album.getAlbumId(), album.getProperties());	    
	}
    }

    public Album getAlbumById(long albumId) {
	Album album = getExtendedJdbcTemplate().queryForObject(
		getBoundSql("ARCHITECTURE_WEB.SELECT_ALBUM_BY_ID").getSql(), albumMapper,
		new SqlParameterValue(Types.NUMERIC, albumId));
	album.setProperties(getAlbumProperties(album.getAlbumId()));
	return album;
    }

    public int getAlbumCount(User user) {
	return getExtendedJdbcTemplate().queryForObject(
		getBoundSql("ARCHITECTURE_WEB.COUNT_ALBUM_BY_USER_ID").getSql(), Integer.class,
		new SqlParameterValue(Types.NUMERIC, user.getUserId()));
    }

    public List<Long> getAlbumIds(User user) {
	return getExtendedJdbcTemplate().queryForList(
		getBoundSql("ARCHITECTURE_WEB.SELECT_ALBUM_ID_BY_USER_ID").getSql(), Long.class,
		new SqlParameterValue(Types.NUMERIC, user.getUserId()));
    }

    public List<Long> getAlbumIds(User user, int startIndex, int pageSize) {
	return getExtendedJdbcTemplate().queryScrollable(
		getBoundSql("ARCHITECTURE_WEB.SELECT_ALBUM_ID_BY_USER_ID").getSql(), startIndex,
		pageSize, new Object[] { user.getUserId() }, new int[] { Types.NUMERIC },
		Long.class);
    }
   
}