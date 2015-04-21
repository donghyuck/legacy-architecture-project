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
package architecture.user.spring.config;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.ui.ModelMap;

import architecture.common.user.UserManager;
import architecture.ee.util.OutputFormat;
import architecture.user.RoleManager;
import architecture.user.security.spring.authentication.ExtendedAuthenticationProvider;
import architecture.user.security.spring.userdetails.ExtendedUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, proxyTargetClass = true, securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter  {

	@Autowired 
	@Qualifier("userManager")
	private UserManager userManager;

	@Autowired 
	@Qualifier("roleManager")
	private RoleManager roleManager;
	
	@Autowired 
	@Qualifier("passwordEncoder")
	private PasswordEncoder passwordEncoder;
	
	
	@Autowired 
	@Qualifier("passwordSaltSource")
	private SaltSource passwordSaltSource;
	
	
	public SecurityConfig() {
	}


	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
		.antMatchers("/decorators/**")
		.antMatchers("/images/**")
		.antMatchers("/fonts/**")
		.antMatchers("/includes/**")
		.antMatchers("/js/**")
		.antMatchers("/styles/**")
		.antMatchers("/index.*")
		.antMatchers("/main.*")
		.antMatchers(HttpMethod.POST, "/accounts/login");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests().antMatchers(
					"/main.do*", 
					"/accounts/**", 
					"/download/**", 
					"/display/**",
					"/connect/**", 
					"/data/**"
			).anonymous()
			.antMatchers("/secure").hasAnyRole("SYSTEM", "ADMIN")
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.loginPage("/accounts/login")
			.loginProcessingUrl("/accounts/authorize")
			.failureHandler(authenticationFailureHandler())
			.successHandler(authenticationSuccessHandler())
			.usernameParameter("username")
			.passwordParameter("password")
			.and()
		.logout()
			.invalidateHttpSession(true)
			.logoutUrl("/accounts/logout")
			.logoutSuccessUrl("/main.do")
			.and()
		.anonymous()
			.key("ANONYMOUS")
			.and()
		.exceptionHandling()
			.accessDeniedPage("/includes/jsp/unauthorized.jsp")
			.and()
		.requestCache()
			.requestCache(authenticationRequestCache())
			.and()
		.httpBasic()
		.authenticationEntryPoint(authenticationEntryPoint());		
	}
	
	protected AuthenticationSuccessHandler  authenticationSuccessHandler (){
		SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler = new SimpleUrlAuthenticationSuccessHandler(){
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
					throws IOException, ServletException { 				
				OutputFormat output = getOutputFormat( request, response );
				if( output == OutputFormat.JSON )
				{
					// Token 		
					String referer = request.getHeader("Referer");					
					Map model = new ModelMap();
					Map<String, String> item = new java.util.HashMap<String, String>() ; 
					item.put("success", "true");	
					if( StringUtils.isNotEmpty(referer))
						item.put("referer", referer );
					/*model.put("item", item);		
					request.setAttribute(WebApplicatioinConstants.MODEL_ATTRIBUTE, model);					
					if(output == OutputFormat.JSON ){
						JsonView view = new JsonView();		    
						view.setModelKey("item");			
						try {
							view.render(model, request, response);
						} catch (Exception e) {
						}
						return;
					}			*/
				}
				super.onAuthenticationSuccess(request, response, authentication );		
			}			
			protected OutputFormat getOutputFormat(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse) {
				String temp = httpservletrequest.getParameter("output");
				String formatString = StringUtils.defaultString(temp, "html");
				OutputFormat format = OutputFormat.stingToOutputFormat(formatString);
				return format;
			}			
		};
		return authenticationSuccessHandler; 
	}
	
	protected AuthenticationFailureHandler authenticationFailureHandler(){
		SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler("/includes/jsp/error.jsp");
		simpleUrlAuthenticationFailureHandler.setUseForward(true);
		return simpleUrlAuthenticationFailureHandler; 
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		
		auth.authenticationProvider(authenticationProviderBean());
	}

	@Bean(name = "authenticationRequestCache") 
	public HttpSessionRequestCache authenticationRequestCache(){
		HttpSessionRequestCache authenticationRequestCache = new HttpSessionRequestCache();
		authenticationRequestCache.setRequestMatcher(nonAjaxRequestMatcher());
		return authenticationRequestCache;
	}

	@Bean(name="nonAjaxRequestMatcher")
	public RequestMatcher nonAjaxRequestMatcher(){
		return new RequestMatcher(){			
			@Override
			public boolean matches(HttpServletRequest request) {
				if (!"XmlHttpRequest".equalsIgnoreCase(request	.getHeader("X-Requested-With"))) {
					return true;
				}
				OutputFormat format = OutputFormat.stingToOutputFormat(StringUtils.defaultString(	request.getParameter("output"), "html"));
				if (format == OutputFormat.HTML)
					return true;
				return false;
			}
		};
	}
	
	@Bean(name = "loginUrlAuthenticationEntryPoint")
	public AuthenticationEntryPoint loginUrlAuthenticationEntryPoint(){
		return new LoginUrlAuthenticationEntryPoint("/accounts/login");		
	}
	
	@Bean(name = "authenticationEntryPoint")
	public DelegatingAuthenticationEntryPoint authenticationEntryPoint(){
		LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> entryPoints = new LinkedHashMap<RequestMatcher, AuthenticationEntryPoint>();
		entryPoints.put(nonAjaxRequestMatcher(), loginUrlAuthenticationEntryPoint());
		DelegatingAuthenticationEntryPoint authenticationEntryPoint = new DelegatingAuthenticationEntryPoint(entryPoints);
		authenticationEntryPoint.setDefaultEntryPoint(new Http403ForbiddenEntryPoint());
		return authenticationEntryPoint;
	}
	
	@Bean(name = "authenticationProvider") 
	public ExtendedAuthenticationProvider authenticationProviderBean(){
		ExtendedAuthenticationProvider authenticationProvider = new ExtendedAuthenticationProvider();
		authenticationProvider.setUserManager(userManager);
		authenticationProvider.setUserDetailsService(userDetailsServiceBean());
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		authenticationProvider.setSaltSource(passwordSaltSource);
		return authenticationProvider;
	}
		
	@Bean(name = "userDetailsService") 
	@Override
	public UserDetailsService userDetailsServiceBean(){
		ExtendedUserDetailsService userDetailsService = new ExtendedUserDetailsService();
		userDetailsService.setUserManager(userManager);
		userDetailsService.setRoleManager(roleManager);
		return userDetailsService;
	}

}
