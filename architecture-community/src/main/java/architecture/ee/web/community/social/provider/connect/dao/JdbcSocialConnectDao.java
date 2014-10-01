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
package architecture.ee.web.community.social.provider.connect.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.community.social.provider.connect.DefaultSocialConnect;
import architecture.ee.web.community.social.provider.connect.SocialConnect;
import architecture.ee.web.community.social.provider.connect.SocialConnect.Media;

public class JdbcSocialConnectDao extends ExtendedJdbcDaoSupport implements SocialConnectDao {

	private final TextEncryptor textEncryptor;
	
	private final SocialConnectMapper socialConnectMapper = new SocialConnectMapper();
	
	private final class SocialConnectMapper implements RowMapper<SocialConnect> {		
		
		@Override
		public SocialConnect mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new DefaultSocialConnect(
				rs.getLong("CONNECT_ID"),
				rs.getInt("OBJECT_TYPE"),
				rs.getLong("OBJECT_ID"),	
				Media.valueOf(rs.getString("MEDIA_TYPE").toUpperCase()),
				rs.getString("PROVIDER_ID"), 
				rs.getString("PROVIDER_USER_ID"), 
				rs.getString("DISPLAY_NAME"), 
				rs.getString("PROFILE_URL"), 
				rs.getString("IMAGE_URL"),
				decrypt(rs.getString("ACCESS_TOKEN")), 
				decrypt(rs.getString("SECRET")), 
				decrypt(rs.getString("REFRESH_TOKEN")), 
				expireTime(rs.getLong("EXPIRE_TIME")),	
				rs.getDate("CREATION_DATE"),
				rs.getDate("MODIFIED_DATE")
			);
		}		
		private String decrypt(String encryptedText) {
			return encryptedText != null ? textEncryptor.decrypt(encryptedText) : encryptedText;
		}			
		private Long expireTime(long expireTime) {
			return expireTime == 0 ? null : expireTime;
		}
	}
	
	public JdbcSocialConnectDao() {
		this(Encryptors.noOpText());
	}
	
	public JdbcSocialConnectDao(TextEncryptor textEncryptor) {
		super();
		this.textEncryptor = textEncryptor;
	}
	


	@Override
	public SocialConnect getSocialConnectById(long socialConnectId) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_SOCIAL_CONNECT_BY_ID").getSql(), socialConnectMapper, new SqlParameterValue (Types.NUMERIC, socialConnectId ));
	}

	@Override
	public void updateSocialConnect(SocialConnect socialConnect) {
		getExtendedJdbcTemplate().update(
			getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_SOCIAL_CONNECT").getSql(), 	
			new SqlParameterValue (Types.VARCHAR, socialConnect.getDisplayName()), 
			new SqlParameterValue (Types.VARCHAR, socialConnect.getProfileUrl()), 
			new SqlParameterValue (Types.VARCHAR, socialConnect.getImageUrl()), 
			new SqlParameterValue (Types.VARCHAR, encrypt(socialConnect.getAccessToken())), 
			new SqlParameterValue (Types.VARCHAR, encrypt(socialConnect.getSecret())), 
			new SqlParameterValue (Types.VARCHAR, encrypt(socialConnect.getRefreshToken())), 
			new SqlParameterValue (Types.NUMERIC, socialConnect.getExpireTime()),
			new SqlParameterValue(Types.DATE, socialConnect.getModifiedDate()),
			new SqlParameterValue (Types.NUMERIC, socialConnect.getSocialConnectId()));				
	}

	@Override
	public SocialConnect addSocialConnect(SocialConnect socialConnect) {
		SocialConnect toUse = socialConnect;		
		if( toUse.getSocialConnectId() <1L){
			long socialConnectId = getNextId("SOCIAL_CONNECT");		
			if( toUse instanceof DefaultSocialConnect){
				DefaultSocialConnect impl = (DefaultSocialConnect)toUse;
				impl.setSocialConnectId(socialConnectId);
				//		"select coalesce(max(rank) + 1, 1) as rank from " + tablePrefix + "UserConnection where userId = ? and providerId = ?", new Object[]{ userId, data.getProviderId() }, Integer.class);				
			}			
			/*
			int rank = getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_MAX_RANK_FROM_SOCIAL_CONNECT").getSql(),
					Integer.class,
					new SqlParameterValue (Types.NUMERIC, toUse.getObjectType() ), 
					new SqlParameterValue (Types.NUMERIC, toUse.getObjectId()), 
					new SqlParameterValue (Types.VARCHAR, toUse.getProviderId() )	
			);		
			*/
			getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.CREATE_SOCIAL_CONNECT").getSql(), 	
					new SqlParameterValue (Types.NUMERIC, toUse.getSocialConnectId()), 
					new SqlParameterValue (Types.INTEGER, toUse.getObjectType() ), 
					new SqlParameterValue (Types.INTEGER, toUse.getObjectId() ), 
					new SqlParameterValue (Types.VARCHAR, toUse.getMedia().name().toLowerCase()), 
					//new SqlParameterValue (Types.INTEGER, rank ), 
					new SqlParameterValue (Types.VARCHAR, toUse.getDisplayName()), 
					new SqlParameterValue (Types.VARCHAR, toUse.getProfileUrl()), 
					new SqlParameterValue (Types.VARCHAR, toUse.getImageUrl()), 
					new SqlParameterValue (Types.VARCHAR, encrypt(toUse.getAccessToken())), 
					new SqlParameterValue (Types.VARCHAR, encrypt(toUse.getSecret())), 
					new SqlParameterValue (Types.VARCHAR, encrypt(toUse.getRefreshToken())), 
					new SqlParameterValue (Types.NUMERIC, toUse.getExpireTime()),
					new SqlParameterValue(Types.DATE, toUse.getCreationDate()),
					new SqlParameterValue(Types.DATE, toUse.getModifiedDate())
			);	
		}	
		return toUse;
	}

	@Override
	public void removeSocialConnect(SocialConnect socialConnect) {
		getExtendedJdbcTemplate().update(
			getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_SOCIAL_CONNECT_BY_ID").getSql(), 	
			new SqlParameterValue (Types.NUMERIC, socialConnect.getSocialConnectId() ));			
	}

	@Override
	public void removeSocialConnects(int objectType, long objectId) {
		getExtendedJdbcTemplate().update(
				getBoundSql("ARCHITECTURE_COMMUNITY.DELETE_SOCIAL_CONNECT_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 	
				new SqlParameterValue (Types.NUMERIC, objectType ),
				new SqlParameterValue (Types.NUMERIC, objectId ));				
	}

	private String encrypt(String text) {
		return text != null ? textEncryptor.encrypt(text) : text;
	}

	public List<Long> getObjectIds(int objectType, String providerId, String providerUserId) {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_SOCIAL_CONNECT_OBJECT_IDS_BY_OBJECT_TYPE").getSql(), 				
			Long.class, 
			new SqlParameterValue (Types.NUMERIC, objectType ), 
			new SqlParameterValue (Types.VARCHAR, providerId ), 
			new SqlParameterValue (Types.VARCHAR, providerUserId ));	
	}

	@Override
	public List<Long> getObjectIds(int objectType, String providerId, Set<String> providerUserIds) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("objectType", objectType);
		parameters.addValue("providerId", providerId);
		
		return null;
	}


	@Override
	public List<Long> getSocialConnectIds(int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_SOCIAL_CONNECT_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 				
				Long.class, new SqlParameterValue (Types.NUMERIC, objectType ), new SqlParameterValue (Types.NUMERIC, objectId ));	
	}
	
	@Override
	public List<Long> getSocialConnectIds(int objectType, long objectId, String providerId) {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_SOCIAL_CONNECT_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID_AND_PROVIDER").getSql(), 				
				Long.class, new SqlParameterValue (Types.NUMERIC, objectType ), new SqlParameterValue (Types.NUMERIC, objectId ), new SqlParameterValue (Types.VARCHAR, providerId ));	
	}

	@Override
	public Long getSocialConnectId(int objectType, long objectId, String providerId, String providerUserId) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_SOCIAL_CONNECT_ID_BY_OBJECT_TYPE_AND_OBJECT_ID_AND_PROVIDER").getSql(), 				
				Long.class, 
				new SqlParameterValue (Types.NUMERIC, objectType ), 
				new SqlParameterValue (Types.NUMERIC, objectId ), 
				new SqlParameterValue (Types.VARCHAR, providerId ), 
				new SqlParameterValue (Types.VARCHAR, providerUserId ));	
	}

	@Override
	public Long findPrimaryConnect(int objectType, long objectId, String providerId) {
		return null;
	}

	@Override
	public void removeSocialConnect(int objectType, long objectId,String providerId, String providerUserId) {
	
	}

	@Override
	public void removeSocialConnects(int objectType, long objectId, String providerId) {
		
	}
	
}
