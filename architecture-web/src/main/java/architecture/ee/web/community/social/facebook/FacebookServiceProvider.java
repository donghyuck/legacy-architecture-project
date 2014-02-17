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
package architecture.ee.web.community.social.facebook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;

import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.web.community.social.AbstractSocialServiceProvider;
import architecture.ee.web.community.social.SocialNetwork.Media;
import architecture.ee.web.community.social.facebook.Post.PostType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class FacebookServiceProvider extends AbstractSocialServiceProvider {

	private static final String GRAPH_API_URL = "https://graph.facebook.com/";		
	public static final String DEFAULT_SCOPE = "export_stream, read_stream, user_about_me, user_activities, user_education_history, user_friends, user_photos, user_work_history";
	
	public FacebookServiceProvider(String clientId, String clientSecret,  String callbackUrl) {
		super(new ServiceBuilder()
                                .provider(FacebookApi.class)
                                .apiKey(clientId)
                                .apiSecret(clientSecret)
                                .callback(callbackUrl)
                                .debug()
                                .build());
	}

	public FacebookServiceProvider(String clientId, String clientSecret, String callbackUrl, String scope) {
		super(new ServiceBuilder()
                                .provider(FacebookApi.class)                               
                                .apiKey(clientId)
                                .apiSecret(clientSecret)
                                .callback(callbackUrl)
                                .scope(scope)
                                .debug()
                                .build());
	}
	
	
	/* (비Javadoc)
	 * @see architecture.ee.web.community.social.AbstractSocialServiceProvider#getAuthorizationUrl()
	 */
	@Override
	public String getAuthorizationUrl() {
		return getOAuthService().getAuthorizationUrl(null);
	}

	
	public Token getTokenWithCallbackReturns(String oAuthToken, String oAuthVarifier){	
		return this.getAccessTokenWithCallbackReturns("", oAuthVarifier);
	}	
	
	public FacebookProfile getUserProfile(){
		return getUserProfile("me");
	}
	
	public FacebookProfile getUserProfile(String userId){
		Token accessToken = getAccessToken(getAccessToken(), "");		
		OAuthRequest request = new OAuthRequest(Verb.GET,  GRAPH_API_URL + userId  );
		getOAuthService().signRequest( accessToken, request);		
		Response response = request.send();		
		FacebookProfile facebookProfile;
		try {
			facebookProfile = getObjectMapper().readValue( response.getBody(), FacebookProfile.class);
		}  catch (Exception e) {
			throw new UnAuthorizedException(e);
		}
		return facebookProfile;
	}

	public byte[] getUserProfileImage(){
		return getUserProfileImage("me");
	}

	public byte[] getUserProfileImage(String userId){
		Token accessToken = getAccessToken(getAccessToken(), "");		
		OAuthRequest request = new OAuthRequest(Verb.GET, GRAPH_API_URL +  userId + "/picture?type" + ImageType.NORMAL.toString().toLowerCase() );
		getOAuthService().signRequest(accessToken, request);				
		Response response = request.send();
		//response.getStream();
		try {
			return IOUtils.toByteArray(response.getStream());
		} catch (IOException e) {
			return null;
		}		
	}
	
	public List<Post> getHomeFeed() {
		return getHomeFeed(0, 25);
	}
	
	
	public java.util.List<Post> getHomeFeed(int offset, int limit) {		
		Token accessToken = getAccessToken(getAccessToken(), "");	
		OAuthRequest request = new OAuthRequest(Verb.GET, GRAPH_API_URL + "me/home" );
		request.addBodyParameter("offset", String.valueOf(offset) );
		request.addBodyParameter("limit" , String.valueOf(limit));
		getOAuthService().signRequest(accessToken, request);
		Response response = request.send();

		List<Post> posts;
		try {
			ObjectMapper mapper = getObjectMapper();
			JsonNode dataNode = mapper.readTree( response.getBody() );			
			JsonNode dataNode2 = dataNode.get("data");			
			posts = new ArrayList<Post>();		
			for (Iterator<JsonNode> iterator = dataNode2.iterator(); iterator.hasNext();) {
				JsonNode node = iterator.next();
				posts.add(
					deserializePost( mapper , null,  Post.class, (ObjectNode)node)
				);
			}
		} catch (Exception e) {
			return Collections.EMPTY_LIST;
		}			
		
		return posts;
	}

	@Override
	protected ObjectMapper createObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new FacebookModule());
		return mapper;
	}	

	protected <T> T deserializePost(ObjectMapper objectMapper, String postType, Class<T> type, ObjectNode node) {
		try {
			if (postType == null) {
				postType = determinePostType(node);
			}
			node.put("postType", postType); // used for polymorphic deserialization
			node.put("type", postType); // used to set Post's type property
			return objectMapper.readValue(node.traverse(), type);
		} catch (IOException shouldntHappen) {			
			log.debug("Error deserializing " + postType + " post",  shouldntHappen);
			return null;
		}
	}

	protected String determinePostType(ObjectNode node) {
		if (node.has("type")) {
			try {
				String type = node.get("type").textValue();
				PostType.valueOf(type.toUpperCase());
				return type;
			} catch (IllegalArgumentException e) {
				return "post";
			}
		}
		return "post";
	}

	public Media getMedia() {
		return Media.FACEBOOK;
	}
}
