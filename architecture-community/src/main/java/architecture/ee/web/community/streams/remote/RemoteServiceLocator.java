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
package architecture.ee.web.community.streams.remote;

import org.apache.commons.lang.StringUtils;

import architecture.common.exception.ComponentDisabledException;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.community.streams.PhotoStreamsManager;

import com.caucho.hessian.client.HessianProxyFactory;

public class RemoteServiceLocator {

	public RemoteServiceLocator() {

	}
	
	public static final boolean enabled() {
		boolean enabeld = ApplicationHelper.getApplicationBooleanProperty("components.streams.remoting.enabled", false);
		return enabeld;
	}
	
	public static final PhotoStreamsManager locate() throws Exception {
		String urlName = ApplicationHelper.getApplicationProperty("components.streams.remoting.url", null);
		if( enabled() && StringUtils.isNotEmpty(urlName)){
			HessianProxyFactory factory = new HessianProxyFactory();
			return (PhotoStreamsManager) factory.create(PhotoStreamsManager.class, urlName);
		}else{
			throw new ComponentDisabledException();
		}
	}
	
	public static final PhotoStreamsManager locate(String urlName) throws Exception {
		HessianProxyFactory factory = new HessianProxyFactory();
		return (PhotoStreamsManager) factory.create(PhotoStreamsManager.class, urlName);
	}
}
