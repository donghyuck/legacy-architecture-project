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
package architecture.ee.web.community.social.tumblr;

import java.util.Collections;
import java.util.List;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TumblrApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;

import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.web.community.social.AbstractSocialServiceProvider;
import architecture.ee.web.community.social.SocialNetwork.Media;
import architecture.ee.web.community.social.tumblr.json.TumblrModule;
import architecture.ee.web.community.social.tumblr.json.TumblrResponse;
import architecture.ee.web.community.social.tumblr.json.UserInfoResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 
 * OAuth Consumer Key:  VdE7COcqx3c8qiXg45AowMPausYVvYDpBSIrx1jI6gPj1X5V8T
 * Secret Key:  SILFKJbnYTuFLWVhCnKiD1wpd4T6ahVC0HXTemu1AqT3X7iL0r
 * 
 * @author donghyuck
 *
 */
public class TumblrServiceProvider extends AbstractSocialServiceProvider {

	 private static final String USER_INFO_URL = "http://api.tumblr.com/v2/user/info";
	 
	 private static final String USER_DASHBOARD_URL = "http://api.tumblr.com/v2/user/dashboard";
	 
	 private static final String BLOG_URL = "http://api.tumblr.com/v2/blog/";
	 
	 private final String apiKey;
	 
	public Media getMedia() {
		return Media.TUMBLR;
	}

	public TumblrServiceProvider(String consumerKey, String consumerSecret) {
		super(new ServiceBuilder()
                                .provider(TumblrApi.class)
                                .apiKey(consumerKey)
                                .apiSecret(consumerSecret)
                                .debug()
                                .build());
		apiKey = consumerKey;
	}
	
	public TumblrServiceProvider(String consumerKey, String consumerSecret, String callbackUrl) {
		super(new ServiceBuilder()
                                .provider(TumblrApi.class)                               
                                .apiKey(consumerKey)
                                .apiSecret(consumerSecret)
                                .callback(callbackUrl)
                                .debug()
                                .build());
		apiKey = consumerKey;
	}
	
	
	public Token getTokenWithCallbackReturns(String oAuthToken, String oAuthVarifier){	
		return this.getAccessTokenWithCallbackReturns(oAuthToken, oAuthVarifier);
	}

	public Token getTokenWithCallbackReturns(String oAuthToken, String oAuthSecret, String oAuthVarifier){	
		Verifier verifier = new Verifier(oAuthVarifier) ;
		Token requestToken = new Token(oAuthToken, oAuthSecret);		
		return getOAuthService().getAccessToken(requestToken, verifier );
	}	
	
	public UserInfo getUserProfile(){
		Token accessToken = getAccessToken(getAccessToken(), getAccessSecret());
		OAuthRequest request = new OAuthRequest(Verb.GET,  USER_INFO_URL );
		getOAuthService().signRequest( accessToken, request);		
		Response response = request.send();		
		UserInfo userInfo;
		try {
			ObjectMapper mapper = getObjectMapper();
			TumblrResponse tumblrResponse =mapper.readValue( response.getBody(), TumblrResponse.class);			
			UserInfoResponse t = mapper.readValue(tumblrResponse.getResponseJson(), UserInfoResponse.class);		
			userInfo = t.getUser();
		}  catch (Exception e) {
			throw new UnAuthorizedException(e);
		}
		return userInfo;
	}
	
	public List<Post> getDashboardPosts() {
		Token accessToken = getAccessToken(getAccessToken(), getAccessSecret());
		OAuthRequest request = new OAuthRequest(Verb.GET,  USER_DASHBOARD_URL );
		getOAuthService().signRequest( accessToken, request);				
		Response response = request.send();		
		
		List<Post> list = Collections.EMPTY_LIST;
		try {			
			ObjectMapper mapper = getObjectMapper();
			TumblrResponse tumblrResponse = mapper.readValue( response.getBody(), TumblrResponse.class);		
			System.out.println(tumblrResponse.getResponseJson());		
			JsonNode blogNode = mapper.readTree(tumblrResponse.getResponseJson());
			Posts posts = mapper.readValue(tumblrResponse.getResponseJson(), Posts.class);
			list = posts.getPosts();
		}  catch (Exception e) {
			e.printStackTrace();
		}		
		return list;		
	}	
	
	public BlogInfo getBlogInfo(String blogName){
		Token accessToken = getAccessToken(getAccessToken(), getAccessSecret());
		OAuthRequest request = new OAuthRequest(Verb.GET,  blogPath(blogName, "/info") );
		request.addQuerystringParameter("api_key", apiKey );
		//getOAuthService().signRequest( accessToken, request);		
		Response response = request.send();				
		BlogInfo blogInfo;
		try {			
			ObjectMapper mapper = getObjectMapper();
			TumblrResponse tumblrResponse = mapper.readValue( response.getBody(), TumblrResponse.class);		
			JsonNode blogNode = mapper.readTree(tumblrResponse.getResponseJson());
			blogInfo = mapper.readValue(blogNode.get("blog").traverse(), BlogInfo.class);		
		}  catch (Exception e) {
			blogInfo = null;
		}		
		return blogInfo;
	}
	
	public String getBlogAvatar(String blogName){
		return getBlogAvatar(blogName, null);
	}
	
	public String getBlogAvatar(String blogName, Integer size){
		String pathExt = size == null ? "" : "/" + size.toString();
		return getRedirectUrl(blogPath(blogName, "/avatar" + pathExt )) ;
	}
		
	protected ObjectMapper createObjectMapper() {
		com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
		mapper.registerModule(new TumblrModule());
		return mapper;
	}
		
	protected String getRedirectUrl(String path) {
		OAuthRequest request = new OAuthRequest(Verb.GET,  path  );
		request.setFollowRedirects(false);
		Response response = request.send();
		if (response.getCode() == 301) {
			return response.getHeader("Location");        
		}
		return null;
	}
	
	private static String blogPath(String blogName, String extPath) {
		return BLOG_URL + blogUrl(blogName) + extPath;
    }	
	
	private static String blogUrl(String blogName) {
		return blogName.contains(".") ? blogName : blogName + ".tumblr.com";
    }
	
}
