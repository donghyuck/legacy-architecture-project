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
package architecture.ee.web.community.social.provider.connect;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.tumblr.connect.TumblrConnectionFactory;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import architecture.ee.web.community.social.provider.ServiceProviderConfig;
import architecture.ee.web.community.social.provider.ServiceProviderHelper;
import architecture.ee.web.community.social.provider.connect.ConnectionFactoryLocator.Implementation;
import architecture.ee.web.community.social.provider.connect.SocialConnect.Media;

public class DefaultConnectionFactoryLocator implements Implementation {

    private Log log = LogFactory.getLog(getClass());

    private ConnectionFactoryRegistry registry;

    public DefaultConnectionFactoryLocator() {

	this.registry = new ConnectionFactoryRegistry();
	for (ServiceProviderConfig provider : ServiceProviderHelper.getAllServiceProviderConfig()) {
	    Media media = Media.valueOf(provider.getProvider().toUpperCase());
	    if (log.isDebugEnabled()) {
		log.debug("clientId:" + provider.getProvider() + "\n" + "clientSecret:" + provider.getScope() + "\n"
			+ "callbackUrl" + provider.getCallbackUrl());
	    }

	    if (media == Media.FACEBOOK) {
		FacebookConnectionFactory factory = new FacebookConnectionFactory(provider.getClientId(),
			provider.getClientSecret());
		factory.setScope(provider.getScope());
		registry.addConnectionFactory(factory);
	    } else if (media == Media.TWITTER) {
		TwitterConnectionFactory factory = new TwitterConnectionFactory(provider.getClientId(),
			provider.getClientSecret());
		registry.addConnectionFactory(factory);
	    } else if (media == Media.TUMBLR) {
		TumblrConnectionFactory factory = new TumblrConnectionFactory(provider.getClientId(),
			provider.getClientSecret());
		registry.addConnectionFactory(factory);
	    } else if (media == Media.GOOGLE) {
		GoogleConnectionFactory factory = new GoogleConnectionFactory(provider.getClientId(),
			provider.getClientSecret());
		if (StringUtils.isNotBlank(provider.getScope())) {
		    factory.setScope(provider.getScope());
		}
		registry.addConnectionFactory(factory);
	    }
	}
    }

    public void refresh() {

    }

    public ConnectionFactoryRegistry getConnectionFactoryLocator() {
	return registry;
    }

    public <A> ConnectionFactory<A> getConnectionFactory(Class<A> apiType) {
	return registry.getConnectionFactory(apiType);
    }

    public ConnectionFactory<?> getConnectionFactory(String providerId) {
	return registry.getConnectionFactory(providerId);
    }

}