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
package architecture.ee.web.security.spring.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.RequestMatcher;

import architecture.ee.util.OutputFormat;
import architecture.ee.web.util.ServletUtils;
/**
 *  http://distigme.wordpress.com/2012/11/01/ajax-and-spring-security-form-based-login/
 *  정보를 참조하여 클래스 구현.
 *  
 * @author  <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 *
 */
public class NonAjaxRequestMatcher implements RequestMatcher   {
    
    public boolean matches(HttpServletRequest request) {
                
        if( !"XmlHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))){
            return true;
        }
        
        OutputFormat format = ServletUtils.getOutputFormat(request, null);        
        if( format == OutputFormat.HTML )
            return true;
        
        return false;
    }

}
