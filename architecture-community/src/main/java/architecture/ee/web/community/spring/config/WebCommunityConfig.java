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
package architecture.ee.web.community.spring.config;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;

import architecture.ee.web.spring.component.MultipartConfig;

import architecture.user.spring.annotation.ActiveUserWebArgumentResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { 
	"architecture.user.spring.controller", 
	"architecture.ee.web.spring.controller",
	"architecture.ee.web.community.spring.controller" })
@Import(MultipartConfig.class)
public class WebCommunityConfig extends WebMvcConfigurerAdapter {

    private Log log = LogFactory.getLog(getClass());

    public WebCommunityConfig() {
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	configurer.enable();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
	log.debug("overrid argument resolvers  register ...");
	argumentResolvers.add(new ServletWebArgumentResolverAdapter(new ActiveUserWebArgumentResolver()));
    }
}
