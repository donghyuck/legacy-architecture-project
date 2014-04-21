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
package architecture.ee.web.struts2.action.support;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.opensymphony.xwork2.Preparable;

public class FileUploadAction extends FrameworkActionSupport implements Preparable {
	
	private List<File> uploads = new ArrayList<File>();
	private List<String> uploadFileNames = new ArrayList<String>();
	private List<String> uploadContentTypes = new ArrayList<String>();

	protected boolean isMultiPart() {
		HttpServletRequest request = getRequest();
		if ( request instanceof MultiPartRequestWrapper ) 
			return true;
		else
			return false;		
	}
	
	protected MultiPartRequestWrapper getMultiPartRequestWrapper(){
		HttpServletRequest request = getRequest();
		MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) request;			
		return multiWrapper;		
	}

	public void prepare() throws Exception {
		
	}
	
    public String execute() throws Exception {      	
        return success();
    }  
    
}
