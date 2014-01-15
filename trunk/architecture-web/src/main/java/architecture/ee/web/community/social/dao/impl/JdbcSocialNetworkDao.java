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
package architecture.ee.web.community.social.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.community.social.SocialNetwork;
import architecture.ee.web.community.social.SocialServiceProvider;
import architecture.ee.web.community.social.dao.SocialNetworkDao;
import architecture.ee.web.community.social.facebook.FacebookServiceProvider;
import architecture.ee.web.community.social.impl.SocailNetworkImpl;
import architecture.ee.web.community.social.twitter.TwitterServiceProvider;

public class JdbcSocialNetworkDao extends ExtendedJdbcDaoSupport implements SocialNetworkDao {

	private ExtendedPropertyDao extendedPropertyDao;	
	private String sequencerName = "SOCIAL_ACCOUNT";
	private String socialAccountPropertyTableName = "V2_SOCIAL_ACCOUNT_PROPERTY";
	private String socialAccountPropertyPrimaryColumnName = "ACCOUNT_ID";
	
	public void setExtendedPropertyDao(ExtendedPropertyDao extendedPropertyDao) {
		this.extendedPropertyDao = extendedPropertyDao;
	}
	
	private final RowMapper<SocialNetwork> socialAccountMapper = new RowMapper<SocialNetwork>(){		
		public SocialNetwork mapRow(ResultSet rs, int rowNum) throws SQLException {
			SocailNetworkImpl account = new SocailNetworkImpl();
			account.setSocialAccountId(rs.getLong("ACCOUNT_ID"));
			account.setServiceProviderName(rs.getString("SOCIAL_PROVIDER"));
			account.setObjectType(rs.getInt("OBJECT_TYPE"));
			account.setObjectId(rs.getLong("OBJECT_ID"));
			account.setAccessToken(rs.getString("ACCESS_TOKEN"));
			account.setAccessSecret(rs.getString("ACCESS_SECRET"));
			account.setSignedIn( rs.getInt("SIGNED") == 1 ); 
			account.setCreationDate(rs.getDate("CREATION_DATE"));
			account.setModifiedDate(rs.getDate("MODIFIED_DATE"));						
			
			if( !StringUtils.isEmpty( account.getServiceProviderName())){				
				SocialServiceProvider provider = null;				
				if( "twitter".toLowerCase().equals(account.getServiceProviderName()) ){					
					String callbackUrl = ApplicationHelper.getApplicationProperty("components.social.providers.twitter.callbackUrl", null);
					if( callbackUrl!=null ){
						provider = new TwitterServiceProvider(
							"4XebpD1MW3CQ8Koh7naQpg",
							"aFlMLXe7fsyE3EnZtTp1LdAHRqEMROqOFW8ldQNYc",
							callbackUrl
						);
					}else{
						provider = new TwitterServiceProvider(
							"4XebpD1MW3CQ8Koh7naQpg",
							"aFlMLXe7fsyE3EnZtTp1LdAHRqEMROqOFW8ldQNYc"
						);
					}					
				}else if ( "facebook".toLowerCase().equals(account.getServiceProviderName()) ){		
					String callbackUrl = ApplicationHelper.getApplicationProperty("components.social.providers.facebook.callbackUrl", null);
					String scope = ApplicationHelper.getApplicationProperty("components.social.providers.facebook.scope", FacebookServiceProvider.DEFAULT_SCOPE);
					provider = new FacebookServiceProvider(
							"251365428350280",
							"704f08c943c6dfdba328e08a10550d38",							
							callbackUrl,
							scope
					);
					
					account.setAccessSecret("");
					provider.setAccessSecret(account.getAccessSecret());
					
				}
								
				if(StringUtils.isNotEmpty( account.getAccessToken() )){
					provider.setAccessToken(account.getAccessToken());
				}
				if(StringUtils.isNotEmpty( account.getAccessSecret())){
					provider.setAccessSecret(account.getAccessSecret());
				}
				/*
				log.debug( 
						"getAccessToken: " + account.getAccessToken() + 
						"getAccessSecret: " + account.getAccessSecret()
				);
				log.debug( 
						"getAccessToken: " + provider.getAccessToken() + 
						"getAccessSecret: " + provider.getAccessSecret()
				);
				*/
				account.setSocialServiceProvider(provider);
				
				
			}			
			return account;
		}		
	};
	
		
	public String getSequencerName() {
		return sequencerName;
	}

	public void setSequencerName(String sequencerName) {
		this.sequencerName = sequencerName;
	}

	public ExtendedPropertyDao getExtendedPropertyDao() {
		return extendedPropertyDao;
	}


	public String getSocialAccountPropertyTableName() {
		return socialAccountPropertyTableName;
	}


	public void setSocialAccountPropertyTableName(String socialAccountPropertyTableName) {
		this.socialAccountPropertyTableName = socialAccountPropertyTableName;
	}


	public String getSocialAccountPropertyPrimaryColumnName() {
		return socialAccountPropertyPrimaryColumnName;
	}

	public void setSocialAccountPropertyPrimaryColumnName(
			String socialAccountPropertyPrimaryColumnName) {
		this.socialAccountPropertyPrimaryColumnName = socialAccountPropertyPrimaryColumnName;
	}

	public Map<String, String> getSocialAccountProperties(long accountId) {
		return extendedPropertyDao.getProperties(socialAccountPropertyTableName, socialAccountPropertyPrimaryColumnName, accountId);
	}

	public void deleteSocialAccountProperties(long accountId) {
		extendedPropertyDao.deleteProperties(socialAccountPropertyTableName, socialAccountPropertyPrimaryColumnName, accountId);
	}
	
	public void setSocialAccountProperties(long accountId, Map<String, String> props) {		
		extendedPropertyDao.updateProperties(socialAccountPropertyTableName, socialAccountPropertyPrimaryColumnName, accountId, props);
	}

	public List<Long> getSocialAccountIds( int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("ARCHITECTURE_WEB.SELECT_SOCIAL_ACCOUNT_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 				
				Long.class, new SqlParameterValue (Types.NUMERIC, objectType ), new SqlParameterValue (Types.NUMERIC, objectId ));	
	}
	
	public SocialNetwork getSocialAccountById(long socialAccountId) {
		SocialNetwork account = getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_WEB.SELECT_SOCIAL_ACCOUNT_BY_ID").getSql(), socialAccountMapper, new SqlParameterValue (Types.NUMERIC, socialAccountId ));		
		account.setProperties(getSocialAccountProperties( account.getSocialAccountId() ));
		return account;		
	}

	public void updateSocialAccount(SocialNetwork socialNetwork) {
		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.UPDATE_SOCIAL_ACCOUNT").getSql(), 	
				new SqlParameterValue (Types.VARCHAR, socialNetwork.getAccessToken()), 
				new SqlParameterValue (Types.VARCHAR, socialNetwork.getAccessSecret()), 
				new SqlParameterValue(Types.DATE, socialNetwork.getModifiedDate()),
				new SqlParameterValue (Types.NUMERIC, socialNetwork.getSocialAccountId()) );		
		
		deleteSocialAccountProperties(socialNetwork.getSocialAccountId());
		if(!socialNetwork.getProperties().isEmpty())
			setSocialAccountProperties(socialNetwork.getSocialAccountId(), socialNetwork.getProperties());
	}

	public SocialNetwork createSocialAccount(SocialNetwork socialNetwork) {
		SocialNetwork toUse = socialNetwork;		
		if( toUse.getSocialAccountId() <1L){
			long imageId = getNextId(sequencerName);		
			if( toUse instanceof SocailNetworkImpl){
				SocailNetworkImpl impl = (SocailNetworkImpl)toUse;
				impl.setSocialAccountId(imageId);
			}			
			getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.CREATE_SOCIAL_ACCOUNT").getSql(), 	
					new SqlParameterValue (Types.NUMERIC, imageId), 
					new SqlParameterValue (Types.INTEGER, toUse.getObjectType() ), 
					new SqlParameterValue (Types.INTEGER, toUse.getObjectId() ), 
					new SqlParameterValue (Types.VARCHAR, toUse.getServiceProviderName()), 
					new SqlParameterValue (Types.VARCHAR, toUse.getAccessToken()), 
					new SqlParameterValue (Types.VARCHAR, toUse.getAccessSecret()), 
					new SqlParameterValue (Types.INTEGER, toUse.isSignedIn() ? 1 : 0 ), 
					new SqlParameterValue(Types.DATE, toUse.getCreationDate()),
					new SqlParameterValue(Types.DATE, toUse.getModifiedDate()));	
		}		
		if(!toUse.getProperties().isEmpty())
			setSocialAccountProperties(toUse.getSocialAccountId(), toUse.getProperties());
		
		return  toUse;
	}

	public void deleteSocialAccount(SocialNetwork socialNetwork) {
		getExtendedJdbcTemplate().update(
			getBoundSql("ARCHITECTURE_WEB.DELETE_SOCIAL_ACCOUNT_BY_ID").getSql(), 	
			new SqlParameterValue (Types.NUMERIC, socialNetwork.getSocialAccountId() ));		
		deleteSocialAccountProperties(socialNetwork.getSocialAccountId());
	}

}