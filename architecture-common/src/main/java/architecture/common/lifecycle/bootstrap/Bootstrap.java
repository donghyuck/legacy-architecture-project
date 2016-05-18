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
package architecture.common.lifecycle.bootstrap;

import javax.servlet.ServletContext;

import org.springframework.context.ConfigurableApplicationContext;

import architecture.common.lifecycle.State;
import architecture.common.util.ImplFactory;

/**
 * @author donghyuck
 */
public class Bootstrap {

    public static interface Implementation {

	public <T> T getBootstrapComponent(Class<T> requiredType);

	public ConfigurableApplicationContext getBootstrapApplicationContext();

	public void boot(ServletContext servletContext);

	public void shutdown(ServletContext servletContext);

	public State getState();
    }

    private static Implementation impl = null;

    static {
	impl = (Implementation) ImplFactory.loadImplFromKey(Bootstrap.Implementation.class);
    }

    /**
     * 타입에 해당하는 부스트랩 컴포넌트를 리턴한다.
     * 
     * @param requiredType
     * @return
     */
    public static <T> T getBootstrapComponent(Class<T> requiredType) {
	
	
	return impl.getBootstrapComponent(requiredType);
    }

    /**
     * 부스트랩 애플리케이션 컨텍스트를 리턴한다.
     * 
     * @return
     */
    public static ConfigurableApplicationContext getBootstrapApplicationContext() {
	// 향후에 프로퍼티에 읽어 올수 있도록 수정.
	return impl.getBootstrapApplicationContext();
    }

    /**
     * 부스트랩 클래스로더를 리턴한다.
     * 
     * @return
     */
    public static ClassLoader getBootstrapContextClassLoader() {
	return getBootstrapApplicationContext().getClassLoader();
    }

    /**
     * 등록된 모든 부스트랩 컴포넌트 이름을 리턴한다.
     * 
     * @return
     */
    public static String[] getBootstrapComponentNames() {
	return impl.getBootstrapApplicationContext().getBeanDefinitionNames();
    }

    /**
     * 어드민 서비스를 시작한다.
     * 
     * @param servletContext
     */
    public static final void boot(ServletContext servletContext) {
	impl.boot(servletContext);
    }

    /**
     * 어드민 서비스를 종료한다.
     * 
     * @param servletContext
     */
    public static final void shutdown(ServletContext servletContext) {
	impl.shutdown(servletContext);
    }

    /**
     * 어드민 서비스 상태를 확인한다.
     * 
     * @return
     */
    public static final State getState() {
	return impl.getState();
    }
}