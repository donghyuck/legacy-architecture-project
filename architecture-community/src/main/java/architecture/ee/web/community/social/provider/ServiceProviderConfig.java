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
package architecture.ee.web.community.social.provider;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ServiceProviderConfig implements Serializable {

    private String provider;
    private String clientId;
    private String clientSecret;
    private String callbackUrl;
    private String scope;
    boolean allowSignin;
    boolean allowSignup;

    /**
     * @param provider
     * @param clientId
     * @param clientSecret
     * @param callbackUrl
     * @param scope
     * @param allowSignin
     * @param allowSignup
     */
    public ServiceProviderConfig(String provider, String clientId, String clientSecret, String callbackUrl,
	    String scope, boolean allowSignin, boolean allowSignup) {
	this.provider = provider;
	this.clientId = clientId;
	this.clientSecret = clientSecret;
	this.callbackUrl = callbackUrl;
	this.scope = scope;
	this.allowSignin = allowSignin;
	this.allowSignup = allowSignup;
    }

    /**
     * @return callbackUrl
     */
    @JsonIgnore
    public String getCallbackUrl() {
	return callbackUrl;
    }

    /**
     * @return clientId
     */
    @JsonIgnore
    public String getClientId() {
	return clientId;
    }

    /**
     * @return clientSecret
     */
    @JsonIgnore
    public String getClientSecret() {
	return clientSecret;
    }

    /**
     * @return provider
     */
    public String getProvider() {
	return provider;
    }

    /**
     * @return scope
     */
    @JsonIgnore
    public String getScope() {
	return scope;
    }

    /**
     * @return allowSignin
     */
    public boolean isAllowSignin() {
	return allowSignin;
    }

    /**
     * @return allowSignup
     */
    public boolean isAllowSignup() {
	return allowSignup;
    }
}
