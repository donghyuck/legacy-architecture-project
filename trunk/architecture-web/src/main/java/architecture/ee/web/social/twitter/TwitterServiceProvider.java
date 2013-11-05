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
package architecture.ee.web.social.twitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;

import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.web.social.AbstractSocialServiceProvider;

public class TwitterServiceProvider extends AbstractSocialServiceProvider {

	private static final String ACCOUNT_VERIFY_CREDENTIALS_URL =  "https://api.twitter.com/1.1/account/verify_credentials.json";
	
	private static final String STATUSES_USER_TIMELINE_URL = "https://api.twitter.com/1.1/statuses/user_timeline.json";
	
	public TwitterServiceProvider(String clientId, String clientSecret) {
		super(new ServiceBuilder()
                                .provider(TwitterApi.class)
                                .apiKey(clientId)
                                .apiSecret(clientSecret)
                                .build());
	}

	public TwitterServiceProvider(String clientId, String clientSecret, String callbackUrl) {
		super(new ServiceBuilder()
                                .provider(TwitterApi.class)                               
                                .apiKey(clientId)
                                .apiSecret(clientSecret)
                                .callback(callbackUrl)
                                .build());
	}
	
	public TwitterProfile authenticate(String accessKey) throws UnAuthorizedException {
		Token accessToken = getAccessToken(accessKey);		
		return verifyCredentials(accessToken);
	}
	
	
	public Token getTokenWithCallbackReturn(String oAuthToken, String oAuthVarifier){	
		return this.getAccessTokenWithCallbackReturn(oAuthToken, oAuthVarifier);
	}
	
	
	public TwitterProfile authenticate() throws UnAuthorizedException {
		Token accessToken = getAccessToken(getAccessToken(), getAccessSecret());
		return verifyCredentials(accessToken);
	}
	
	protected TwitterProfile verifyCredentials(Token accessToken) throws UnAuthorizedException {		
		OAuthRequest request = new OAuthRequest(Verb.GET, ACCOUNT_VERIFY_CREDENTIALS_URL);
		getOAuthService().signRequest(accessToken, request);
		Response response = request.send();
		    
		if( response.getCode() != 200)
			throw new UnAuthorizedException();
		else
			super.isAuthorized = true;
		com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
		mapper.addMixInAnnotations(TwitterProfile.class, TwitterProfileMixin.class);
		try {
			return mapper.readValue(response.getBody(), TwitterProfile.class);
		} catch (Exception e) {
			throw new UnAuthorizedException(e);
		}
	}
	
	public List<Tweet> getUserTimeline(){
		if( super.isAuthorized = false ){
			authenticate();
		}
		Token accessToken = getAccessToken(getAccessToken(), getAccessSecret());
		OAuthRequest request = new OAuthRequest(Verb.GET, STATUSES_USER_TIMELINE_URL);
		getOAuthService().signRequest(accessToken, request);
		Response response = request.send();
		com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();		
		mapper.addMixInAnnotations(Tweet.class, TweetMixin.class);
		try {
			return mapper.readValue(response.getBody(), TweetList.class);
		} catch (Exception e) {
			
		}
		return Collections.EMPTY_LIST;
	}

	@SuppressWarnings("serial")
	private static class TweetList extends ArrayList<Tweet>{
		
	}

	
}
