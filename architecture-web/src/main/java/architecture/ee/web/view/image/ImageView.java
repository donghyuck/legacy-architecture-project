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
package architecture.ee.web.view.image;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

public class ImageView implements View {

	protected static final int DEFAULT_BUFFER_SIZE = 4096;
	
	private byte imageData[];
	
	private String contentType;


	public ImageView(byte[] imageData, String contentType) {
		super();
		this.imageData = imageData;
		this.contentType = contentType;
	}

	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setContentType(contentType);
		response.getOutputStream().write(imageData);
		
	}
	
	public String getContentType() {
		return contentType;
	}
    
}
