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
package architecture.common.lifecycle;

import architecture.common.util.ImplFactory;

/**
 * 
 * 
 * @author <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 */
public class ApplicationHelperFactory {

    public static interface Implementation {

	public abstract ApplicationHelper getApplicationHelper();

    }

    private static Implementation impl = null;

    static {
	impl = (Implementation) ImplFactory.loadImplFromKey(ApplicationHelperFactory.Implementation.class);
    }

    public static ApplicationHelper getApplicationHelper() {
	return impl.getApplicationHelper();
    }

}
