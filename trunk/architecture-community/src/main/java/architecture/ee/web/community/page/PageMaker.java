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
package architecture.ee.web.community.page;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.ui.Model;

import architecture.ee.web.view.freemarker.FreeMarkerHelper;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.servlet.AllHttpScopesHashModel;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.HttpRequestParametersHashModel;
import freemarker.ext.servlet.HttpSessionHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

public class PageMaker {
	
	private Configuration configuration;
	
	private ServletContext ServletContext;
	
	
	private PageMaker( ) {
	}
	
	public static class Builder {
		
		private PageMaker maker ;
		private Model model;
		private HttpServletRequest request;
		private HttpServletResponse response;
		private Page page;
		/**
		 * 
		 */
		public Builder() {
			this.maker = new PageMaker();
		}
		
		public Builder configuration(Configuration configuration){
			this.maker.configuration = configuration;
			return this;
		}
		
		public Builder servletContext(ServletContext servletContext){
			this.maker.ServletContext = servletContext;
			return this;
		}
		public Builder model(Model model){
			this.model = model;
			return this;
		}
		public Builder request(HttpServletRequest request){
			this.request = request;
			return this;
		}
		public Builder response(HttpServletResponse response){
			this.response = response;
			return this;
		}
		public Builder page(Page page){
			this.page = page;
			return this;
		}
		
		public Page getPage(){
			return this.page;
		}
		
		public String buildPageBody() throws Exception {
			StringWriter stringWriter = new StringWriter();	
			maker.doRender(page, model.asMap(), request, response, stringWriter);
			return stringWriter.toString();
		}
	}
	
	
	public static Builder newBuilder(){
		return new Builder();
	}
	
	
	protected ObjectWrapper getObjectWrapper(Configuration configuration) {
		ObjectWrapper ow = configuration.getObjectWrapper();
		return (ow != null ? ow : ObjectWrapper.DEFAULT_WRAPPER);
	}
		
	/**
	 * @return servletContext
	 */
	public ServletContext getServletContext() {
		return ServletContext;
	}

	
	/**
	 * @return configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	protected void doRender(Page page, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response, Writer writer) throws Exception {
		ObjectWrapper wrapper = getObjectWrapper(configuration);
		FreeMarkerHelper.populateStatics((BeansWrapper)wrapper, model);
		SimpleHash fmModel = buildTemplateModel(model, wrapper, request, response) ;
		getPageTemplate(page).process(fmModel, writer);		
	}
	
	protected void doRender(Page page, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		doRender(page, model, request, response, response.getWriter());
	}
	
	protected Template getPageTemplate(Page page) throws IOException{

		Template  template = new Template ("page-body-"+ page.getBodyContent().getBodyId(), new StringReader(page.getBodyText()), getConfiguration());		
		return template;
	}
	
	protected SimpleHash buildTemplateModel(Map<String, Object> model, ObjectWrapper wrapper, HttpServletRequest request, HttpServletResponse response) {				
		AllHttpScopesHashModel fmModel = new AllHttpScopesHashModel(wrapper, getServletContext(), request);
		//fmModel.put(FreemarkerServlet.KEY_JSP_TAGLIBS, this.taglibFactory);		
		fmModel.put(FreemarkerServlet.KEY_APPLICATION, buildServletContextHashModel(wrapper, null));
		fmModel.put(FreemarkerServlet.KEY_SESSION, buildSessionModel(wrapper, request, response));
		fmModel.put(FreemarkerServlet.KEY_REQUEST, new HttpRequestHashModel(request, response, wrapper));
		fmModel.put(FreemarkerServlet.KEY_REQUEST_PARAMETERS, new HttpRequestParametersHashModel(request));
		fmModel.putAll(model);
		return fmModel;
	}
	
	
	protected void exposeModelAsRequestAttributes(Map<String, Object> model, HttpServletRequest request) throws Exception {
		for (Map.Entry<String, Object> entry : model.entrySet()) {
			String modelName = entry.getKey();
			Object modelValue = entry.getValue();
			if (modelValue != null) {
				request.setAttribute(modelName, modelValue);
			}
			else {
				request.removeAttribute(modelName);				
			}
		}
	}	
	
	protected ServletContextHashModel buildServletContextHashModel(ObjectWrapper wrapper, GenericServlet genericServlet){
		if( genericServlet == null ){
			GenericServlet servlet = new GenericServletAdapter();
			try {
				servlet.init(new DelegatingServletConfig());
			}
			catch (ServletException ex) {
				throw new BeanInitializationException("Initialization of GenericServlet adapter failed", ex);
			}
			return new ServletContextHashModel(genericServlet, wrapper );
		}else{	
			return new ServletContextHashModel(genericServlet, wrapper );
		}
	}
	
	protected HttpSessionHashModel buildSessionModel(ObjectWrapper wrapper, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			return new HttpSessionHashModel(session, wrapper);
		}
		else {
			return new HttpSessionHashModel(null, request, response, wrapper);
		}
	}
	
	@SuppressWarnings("serial")
	private static class GenericServletAdapter extends GenericServlet {

		@Override
		public void service(ServletRequest servletRequest, ServletResponse servletResponse) {
			// no-op
		}
	}

	/**
	 * Internal implementation of the {@link ServletConfig} interface,
	 * to be passed to the servlet adapter.
	 */
	private class DelegatingServletConfig implements ServletConfig {

		@Override
		public String getServletName() {
			return "PageMaker";
		}

		@Override
		public ServletContext getServletContext() {
			return PageMaker.this.getServletContext();
		}

		@Override
		public String getInitParameter(String paramName) {
			return null;
		}

		@Override
		public Enumeration<String> getInitParameterNames() {
			return Collections.enumeration(new HashSet<String>());
		}
	}
	
	
}
