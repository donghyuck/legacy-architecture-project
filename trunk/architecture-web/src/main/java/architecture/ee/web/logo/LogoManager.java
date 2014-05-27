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
package architecture.ee.web.logo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import architecture.common.user.Company;
import architecture.ee.web.site.WebSite;

public interface LogoManager {
		
	public void addLogoImage(LogoImage logoImage, File file);
	
	public void removeLogoImage(LogoImage logoImage) throws LogoImageNotFoundException ;
	
	public LogoImage getLogoImageById(Long logoId)  throws LogoImageNotFoundException ;
	
	public LogoImage getPrimaryLogoImage(int objectType, long objectId) throws LogoImageNotFoundException ;
	
	public List<LogoImage> getLogoImages(int objectType, long objectId);
	
	public int getLogoImageCount(int objectType, long objectId);
	
	public InputStream getImageInputStream(LogoImage logoImage )  throws IOException ;
	
	public InputStream getImageThumbnailInputStream(LogoImage image, int width, int height ) ;
	
	public LogoImage getPrimaryLogoImage(Company company)  throws LogoImageNotFoundException  ;
	
	public LogoImage getPrimaryLogoImage(WebSite site)  throws LogoImageNotFoundException  ;
	
	public List<LogoImage> getLogoImages(Company company);
	
	public int getLogoImageCount(Company company);
	
	public List<LogoImage> getLogoImages(WebSite site);
	
	public int getLogoImageCount(WebSite site);
	
}
