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
import java.util.Collections;
import java.util.List;

import architecture.ee.web.community.social.provider.ServiceProviderConfig;

public class Status {

    private List<ServiceProviderConfig> media;

    private List<SocialConnect> connections;

    public Status() {
	media = Collections.EMPTY_LIST;
	connections = Collections.EMPTY_LIST;
    }

    /**
     * @return media
     */
    public List<ServiceProviderConfig> getMedia() {
	return media;
    }

    /**
     * @param media
     *            설정할 media
     */
    public void setMedia(List<ServiceProviderConfig> media) {
	this.media = media;
    }

    /**
     * @return connections
     */
    public List<SocialConnect> getConnections() {
	return connections;
    }

    /**
     * @param connections
     *            설정할 connections
     */
    public void setConnections(List<SocialConnect> connections) {
	this.connections = connections;
    }

    public void setConnection(SocialConnect connection) {
	if (connections == null)
	    connections = new ArrayList<SocialConnect>();

	connections.add(connection);

    }

}
