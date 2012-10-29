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
   
package architecture.ee.web.util;

public interface WebApplicatioinConstants extends architecture.ee.util.ApplicationConstants {
	
	public static final String THEMES_ROOT_ENV_KEY = "themes.root";	
	
	/** APPLICATION PROPERTY KEY */	
	public static final String THTMES_CACHE_TIMEOUT_PROP_NAME = "themes.missCache.timeout";	
	public static final String THTMES_LOCATION_PROP_NAME = "themes.location";		
	public static final String THTMES_EXCLUDES_PATTERN_PARAMETER_NAME_PROP_NAME  = "themes.excludes.pattern.parameterName";	
	public static final String THTMES_EXCLUDES_PATTERN_PARAMETER_VALUE_PROP_NAME = "themes.excludes.pattern.parameterValue";	
	public static final String THTMES_URI_HEADER_PROP_NAME  = "themes.uriHeader";	
		
	/** ATTACHEMENT PROPERTY KEY */
	public static final String ATTACHMENTS_MAX_ATTACHMENTS_PER_MESSAGE = "attachments.maxAttachmentsPerMessage";	
	public static final String ATTACHMENTS_MAX_ATTACHMENT_SIZE = "attachments.maxAttachmentSize";
		
	/** VIEW RENDER PROPERTY KEY */
	public static final String VIEW_FREEMARKER_ENABLED = "view.render.freemarker.enabled" ;	
	public static final String VIEW_FREEMARKER_DEBUG = "view.render.freemarker.debug" ;	
	public static final String VIEW_FREEMARKER_SOURCE_LOCATION = "view.render.freemarker.source.location" ;	
	
	public static final String VIEW_VELOCITY_ENABLED   = "view.render.velocity.enabled" ;	
    public static final String VIEW_ATTRIBUTE  = (architecture.ee.web.servlet.ViewRendererServlet.class).getName() + ".VIEW";    
    public static final String MODEL_ATTRIBUTE  = (architecture.ee.web.servlet.ViewRendererServlet.class).getName() + ".MODEL";
	    
}