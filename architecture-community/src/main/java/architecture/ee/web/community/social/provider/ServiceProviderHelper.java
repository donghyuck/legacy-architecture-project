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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.social.connect.web.ConnectSupport;
import org.springframework.social.connect.web.SessionStrategy;

import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.community.social.provider.connect.ConnectionFactoryLocator;
import architecture.ee.web.community.social.provider.connect.SocialConnect.Media;

public class ServiceProviderHelper {

    public static final String DEFAULT_FACEBOOK_SCOPE = "export_stream, read_stream, user_about_me, user_activities, user_education_history, user_friends, user_photos, user_work_history";

    private static final Log log = LogFactory.getLog(ConnectionFactoryLocator.class);

    public static boolean allowSignin(Media media) {
	return ApplicationHelper.getApplicationBooleanProperty(
		"components.social.providers." + media.name().toLowerCase() + ".allowSignin", false);
    }

    public static boolean allowSignup(Media media) {
	return ApplicationHelper.getApplicationBooleanProperty(
		"components.social.providers." + media.name().toLowerCase() + ".allowSignup", false);
    }

    public static List<Media> getAllMedia() {
	Collection<String> providers = ApplicationHelper.getRepository().getSetupApplicationProperties()
		.getChildrenNames("components.social.providers"); // ApplicationHelper.getConfigService().getLocalProperties(parent).getApplicationPropertyNames("components.social.providers");
	List<Media> media = new ArrayList<Media>(providers.size());
	for (String name : providers)
	    media.add(Media.valueOf(name.toUpperCase()));
	return media;
    }

    public static List<ServiceProviderConfig> getAllServiceProviderConfig() {
	Collection<String> providers = ApplicationHelper.getRepository().getSetupApplicationProperties()
		.getChildrenNames("components.social.providers");
	List<ServiceProviderConfig> infos = new ArrayList<ServiceProviderConfig>(providers.size());
	for (String name : providers) {
	    Media media = Media.valueOf(name.toUpperCase());
	    infos.add(new ServiceProviderConfig(name, getClientId(media), getClientSecret(media), getCallbackUrl(media),
		    getScope(media), allowSignin(media), allowSignup(media)));
	}
	return infos;
    }

    public static String getCallbackUrl(Media media) {
	return ApplicationHelper.getApplicationProperty(
		"components.social.providers." + media.name().toLowerCase() + ".callbackUrl", null);
    }

    public static String getCallbackUrl(String providerId) {
	return ApplicationHelper.getApplicationProperty("components.social.providers." + providerId + ".callbackUrl",
		null);
    }

    public static String getClientId(Media media) {
	return ApplicationHelper.getApplicationProperty(
		"components.social.providers." + media.name().toLowerCase() + ".clientId", null);
    }

    public static String getClientSecret(Media media) {
	return ApplicationHelper.getApplicationProperty(
		"components.social.providers." + media.name().toLowerCase() + ".clientSecret", null);
    }

    public static ConnectSupport getConnectSupport() {
	return new ConnectSupport();
    }

    public static ConnectSupport getConnectSupport(SessionStrategy sessionStrategy) {
	return new ConnectSupport(sessionStrategy);
    }

    public static String getScope(Media media) {
	return ApplicationHelper.getApplicationProperty(
		"components.social.providers." + media.name().toLowerCase() + ".scope", DEFAULT_FACEBOOK_SCOPE);
    }

    public static Media toMedia(String name) {
	return Media.valueOf(name.toUpperCase());
    }

}
