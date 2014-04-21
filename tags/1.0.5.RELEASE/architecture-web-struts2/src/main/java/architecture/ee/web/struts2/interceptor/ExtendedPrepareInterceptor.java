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
package architecture.ee.web.struts2.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class ExtendedPrepareInterceptor extends MethodFilterInterceptor {

	private boolean alwaysInvokePrepare;
    private static final Log _log = LogFactory.getLog(ExtendedPrepareInterceptor.class);
    private static final String PREPARE_PREFIX = "prepare";
    private static final String ALT_PREPARE_PREFIX = "prepareDo";
    private static final String PREFIXES[] = {
    	PREPARE_PREFIX, ALT_PREPARE_PREFIX
    };
    
    public void setAlwaysInvokePrepare(String alwaysInvokePrepare)
    {
        this.alwaysInvokePrepare = Boolean.parseBoolean(alwaysInvokePrepare);
    }
    
	@Override
	public String doIntercept(ActionInvocation invocation)
	        throws Exception
	    {
	        Object action = invocation.getAction();
	        if(action instanceof Preparable)
	        {
	            try
	            {
	              //  InterceptorUtil.invokePrefixMethod(invocation, PREFIXES);
	            }
	            catch(Exception e)
	            {
	                _log.warn("an exception occured while trying to execute prefixed method", e);
	            }
	            if(alwaysInvokePrepare)
	                ((Preparable)action).prepare();
	        }
	        return invocation.invoke();
	    }

}
