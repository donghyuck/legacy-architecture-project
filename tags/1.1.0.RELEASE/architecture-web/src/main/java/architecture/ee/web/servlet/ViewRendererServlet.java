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
   
package architecture.ee.web.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.RequestToViewNameTranslator;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractView;
import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;
import org.springframework.web.util.NestedServletException;

import architecture.common.util.StringUtils;
import architecture.ee.util.OutputFormat;
import architecture.ee.web.util.ServletUtils;
import architecture.ee.web.util.WebApplicatioinConstants;
import architecture.ee.web.util.WebApplicationHelper;

public class ViewRendererServlet extends HttpServlet {
		       
    protected static final Log log = LogFactory.getLog(architecture.ee.web.servlet.ViewRendererServlet.class);
    
    protected RequestToViewNameTranslator translator = null ;

    protected String viewResolverName = null ;

    public ViewRendererServlet()
    {
        translator = new DefaultRequestToViewNameTranslator();
    }

    protected final void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws ServletException, IOException
    {
        processRequest(httpservletrequest, httpservletresponse);
    }

    protected final void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws ServletException, IOException
    {
        processRequest(httpservletrequest, httpservletresponse);
    }
            
    @Override
	public void init() throws ServletException {
    	String viewResolverNameString = getInitParameter("viewResolverName");
    	if( ! StringUtils.isEmpty(viewResolverNameString ))
    		viewResolverName = viewResolverNameString;
	}
    

	protected final void processRequest(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws ServletException, IOException
    {
        try
        {
            httpservletrequest.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, WebApplicationContextUtils.getWebApplicationContext(getServletContext()));
            renderView(httpservletrequest, httpservletresponse);
        }
        catch(ServletException servletexception)
        {
            throw servletexception;
        }
        catch(IOException ioexception)
        {
            throw ioexception;
        }
        catch(Exception exception)
        {
            throw new NestedServletException("View rendering failed", exception);
        }
    }

    protected void renderView(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws Exception
    {
        
    	View view = resolvingView(httpservletrequest, httpservletresponse);        
        if(view == null)
        {
            throw new ServletException("Could not complete render request: View is null");
        } else
        {
            Map map = resolveModel(httpservletrequest, httpservletresponse);            
            view.render(map, httpservletrequest, httpservletresponse);            
            return;
        }
    }

    protected Map resolveModel(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
    {
        Map map = (Map)httpservletrequest.getAttribute(WebApplicatioinConstants.MODEL_ATTRIBUTE);
        if(map != null)
            return map;
        else
            return Collections.EMPTY_MAP;
    }

    protected View resolvingView(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
    {
    	/**
    	 * 어트리뷰트에서 VIEW 를 꺼내온 경우 ContentType 값을 출력 포맷에 따라 설정하지 않는다.
    	 */
        View view = (View)httpservletrequest.getAttribute(WebApplicatioinConstants.VIEW_ATTRIBUTE);
        if( view == null ){        	
            try
            {
                String viewName = translator.getViewName(httpservletrequest);
                ViewResolver viewresolver = getViewResolver( viewResolverName );
                view = viewresolver.resolveViewName(viewName, httpservletrequest.getLocale());
            }
            catch(Exception exception)
            {
                log.error(exception);
            }
            
            if(view != null && view instanceof AbstractView ){        	
            	
            	OutputFormat format = ServletUtils.getOutputFormat(httpservletrequest, httpservletresponse);
            	if(log.isDebugEnabled())
            	    log.debug("architecture.ee.web.community.content: " + format );            	
            	if( format == OutputFormat.XML )
            	{
            		httpservletresponse.setContentType(null);            		
            		((AbstractView)view).setContentType("text/xml;charset=UTF-8");
            	} else if ( format == OutputFormat.HTML ){
            		httpservletresponse.setContentType(null);        
            		((AbstractView)view).setContentType("text/html;charset=UTF-8");
            	}
            }
        }
        
        if(log.isDebugEnabled()){
        	log.debug("response architecture.ee.web.community.content type: " + httpservletresponse.getContentType());
        	log.debug("architecture.ee.web.community.content type : " + view.getContentType());        	
        }
        return view;
    }
    
    protected ViewResolver getViewResolver(String name){
    	return  WebApplicationHelper.getComponent( name , org.springframework.web.servlet.ViewResolver.class);
    }
    
}
