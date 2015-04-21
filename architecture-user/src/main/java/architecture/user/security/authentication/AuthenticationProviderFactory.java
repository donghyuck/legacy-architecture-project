/*
 * Copyright 2012 Donghyuck, Son
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
package architecture.user.security.authentication;

import architecture.common.util.ImplFactory;
/**
 *
 *
 * 
 * @author donghyuck
 */
public class AuthenticationProviderFactory {

	public static interface Implementation {		
		public AuthenticationProvider getSecurityContextAuthenticationProvider();		
	}
	
	private static Implementation impl = null;

	static 
	{
		impl = (Implementation)ImplFactory.loadImplFromKey(AuthenticationProviderFactory.Implementation.class);
	}

	public static AuthenticationProvider getSecurityContextAuthenticationProvider(){
		return impl.getSecurityContextAuthenticationProvider();
	}
	
}