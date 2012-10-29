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
package architecture.ee.web.struts2.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import architecture.ee.web.struts2.view.freemarker.ExtendedFreemarkerResult;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

import freemarker.template.TemplateException;

public class HttpStatusViewResult extends ExtendedFreemarkerResult {

    private int status;
    
    public HttpStatusViewResult(int status, String location)
    {
        this.status = 200;
        this.status = status;
        super.setLocation(location);
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public void doExecute(String location, ActionInvocation invocation)
        throws IOException, TemplateException
    {
        ActionContext ctx = invocation.getInvocationContext();
        HttpServletRequest req = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse res = (HttpServletResponse) ctx.get(ServletActionContext.HTTP_RESPONSE);
        res.setStatus(status);
        
        super.doExecute(location, invocation);
    }

    
}
