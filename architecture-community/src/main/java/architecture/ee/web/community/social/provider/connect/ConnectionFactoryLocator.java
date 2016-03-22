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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;

import architecture.ee.web.community.social.provider.connect.SocialConnect.Media;

public class ConnectionFactoryLocator {

    public static interface Implementation {

	public ConnectionFactoryRegistry getConnectionFactoryLocator();

	public <A> ConnectionFactory<A> getConnectionFactory(Class<A> apiType);

	public ConnectionFactory<?> getConnectionFactory(String providerId);

    }

    private static Implementation impl = null;

    static {
	impl = new DefaultConnectionFactoryLocator();
    }

    public static List<Media> registeredProviderMedia() {
	Set<String> set = getConnectionFactoryLocator().registeredProviderIds();
	List<Media> media = new ArrayList<Media>(set.size());
	for (String name : set) {
	    media.add(Media.valueOf(name.toUpperCase()));
	}
	return media;
    }

    public static ConnectionFactoryRegistry getConnectionFactoryLocator() {
	return impl.getConnectionFactoryLocator();
    }

    public static <A> ConnectionFactory<A> getConnectionFactory(Class<A> apiType) {
	return impl.getConnectionFactory(apiType);
    }

    public static ConnectionFactory<?> getConnectionFactory(String providerId) {
	return impl.getConnectionFactory(providerId);
    }

    public static ConnectionFactory<?> getConnectionFactory(Media media) {
	return impl.getConnectionFactory(media.name().toLowerCase());
    }

}
