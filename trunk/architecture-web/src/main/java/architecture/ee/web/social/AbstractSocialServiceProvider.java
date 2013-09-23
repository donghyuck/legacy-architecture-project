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
package architecture.ee.web.social;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public abstract class AbstractSocialServiceProvider implements SocialServiceProvider  {

	private OAuthService service;
	private String accessToken ;
	private String accessSecret;
	protected boolean isAuthorized = false;
	

	public AbstractSocialServiceProvider(OAuthService service) {
		this.service = service;
	}

	public OAuthService getOAuthService() {
		return service;
	}
	
	public String getAuthorizationUrl(){
		return service.getAuthorizationUrl(getRequestToken());
	}
	
	/**
	 * @return isAuthorized
	 */
	public boolean isAuthorized() {
		return isAuthorized;
	}

	/**
	 * @return accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * @param accessToken 설정할 accessToken
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * @return accessSecret
	 */
	public String getAccessSecret() {
		return accessSecret;
	}

	/**
	 * @param accessSecret 설정할 accessSecret
	 */
	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}
	
	protected Token getRequestToken(){
		return service.getRequestToken();
	}
	
	protected Token getAccessToken(String accessKey){
		Token accessToken = service.getAccessToken(getRequestToken(), new Verifier(accessKey));
		this.accessToken = accessToken.getToken();
		this.accessSecret = accessToken.getSecret();
		return accessToken;
	}
	
	protected Token getAccessToken(String token, String secret){
		Token accessToken = new Token( token, secret );
		return accessToken;
	}
	
}
