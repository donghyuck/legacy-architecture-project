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
package architecture.ee.component.core.lifecycle;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ContextLoader;

import architecture.common.event.api.EventListener;
import architecture.common.exception.ComponentNotFoundException;
import architecture.common.license.License;
import architecture.common.license.LicenseManager;
import architecture.common.lifecycle.Component;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.Version;
import architecture.common.lifecycle.event.ApplicationPropertyChangeEvent;
import architecture.common.lifecycle.event.StateChangeEvent;
import architecture.common.lifecycle.service.AdminService;
import architecture.common.lifecycle.service.PluginService;
import architecture.common.spring.lifecycle.support.SpringLifecycleSupport;
import architecture.ee.component.admin.AdminHelper;
import architecture.ee.spring.lifecycle.SpringAdminService;
import net.anotheria.moskito.core.accumulation.Accumulators;
import net.anotheria.moskito.core.threshold.ThresholdStatus;
import net.anotheria.moskito.core.threshold.Thresholds;
import net.anotheria.moskito.core.threshold.guard.GuardedDirection;
import net.anotheria.moskito.core.threshold.guard.LongBarrierPassGuard;

/**
 * 
 * @author <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 *
 */

public class AdminServiceImpl extends SpringLifecycleSupport implements SpringAdminService {

    private ContextLoader contextLoader;

    private ServletContext servletContext;

    private ConfigurableApplicationContext applicationContext;

    private Version version;

    private ConfigService configService;

    public AdminServiceImpl() {
	super();
	setName("AdminService");
	this.contextLoader = null;
	this.configService = null;
	this.servletContext = null;
	this.applicationContext = null;
	this.version = new Version(2, 0, 0, Version.ReleaseStatus.Release, 1);
    }

    public ConfigService getConfigService() {
	return configService;
    }

    protected ConfigurableApplicationContext getBootstrapApplicationContext() {
	return architecture.common.lifecycle.bootstrap.Bootstrap.getBootstrapApplicationContext();
    }

    protected <T> T getBootstrapComponent(Class<T> requiredType) {
	return architecture.common.lifecycle.bootstrap.Bootstrap.getBootstrapComponent(requiredType);
    }

    public void setConfigService(ConfigService configService) {
	this.configService = configService;
    }

    public boolean isSetConfigService() {
	if (configService != null)
	    return true;
	return false;
    }

    public boolean isSetApplicationContext() {
	if (applicationContext != null) {
	    return applicationContext.isActive();
	}
	return false;
    }

    public void setContextLoader(ContextLoader contextLoader) {
	this.contextLoader = contextLoader;
    }

    public ContextLoader getContextLoader() {
	return contextLoader;
    }

    public boolean isSetContextLoader() {
	if (contextLoader != null)
	    return true;
	return false;
    }

    public ServletContext getServletContext() {
	return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
	this.servletContext = servletContext;
	if (getRepository().getState() != State.INITIALIZED) {
	    ((RepositoryImpl) getRepository()).setServletContext(servletContext);
	}
    }

    public boolean isSetServletContext() {
	if (servletContext != null)
	    return true;
	return false;
    }

    @Override
    protected void doInitialize() {
	addStateChangeListener(this);
    }

    @Override
    protected void doStart() {
	Thread currentThread = Thread.currentThread();
	ClassLoader oldLoader = currentThread.getContextClassLoader();
	LicenseManager licenseManager = getBootstrapComponent(LicenseManager.class);
	License license = licenseManager.getLicense();
	log.info(license.toString());

	// MethodInvoker invoker = new MethodInvoker();
	// 플러그인 기능은 평가판 라이선스에서는 제공하지 않는다.
	if (AdminHelper.isSetupComplete() && license.getType() != License.Type.EVALUATION) {
	    try {
		PluginService pluginService = getBootstrapComponent(PluginService.class);
		pluginService.prepare();
	    } catch (Exception e) {

	    }
	}

	setupMoskitoThresholds();

	// 컨텐스트를 로드합니다
	if (isSetServletContext() && isSetContextLoader()) {
	    try {
		this.applicationContext = (ConfigurableApplicationContext) getContextLoader()
			.initWebApplicationContext(getServletContext());
		this.applicationContext.start();
	    } finally {
		if (oldLoader != null)
		    currentThread.setContextClassLoader(oldLoader);
	    }

	}
    }

    private void setupMoskitoThresholds() {
	boolean isEnabled = this.getRepository().getSetupApplicationProperties()
		.getBooleanProperty("performance-monitoring.moskito.thresholds-setup", false);
	if (isEnabled || configService.getApplicationBooleanProperty("performance-monitoring.moskito.thresholds-setup",
		false)) {
	    log.debug("setup moskito threshold ...");
	    Thresholds.addMemoryThreshold("PermGenFree", "MemoryPool-PS Perm Gen-NonHeap", "Free",
		    new LongBarrierPassGuard(ThresholdStatus.GREEN, 1000 * 1000 * 5, GuardedDirection.UP), /* */
		    new LongBarrierPassGuard(ThresholdStatus.YELLOW, 1000 * 1000 * 5, GuardedDirection.DOWN), /* */
		    new LongBarrierPassGuard(ThresholdStatus.ORANGE, 1000 * 1000 * 2, GuardedDirection.DOWN), /* */
		    new LongBarrierPassGuard(ThresholdStatus.RED, 1000 * 1000 * 1, GuardedDirection.DOWN), /* */
		    new LongBarrierPassGuard(ThresholdStatus.PURPLE, 1000 * 1, GuardedDirection.DOWN) /* */
	    );
	    Thresholds.addMemoryThreshold("OldGenFree", "MemoryPool-PS Old Gen-Heap", "Free", /* */
		    new LongBarrierPassGuard(ThresholdStatus.GREEN, 1000 * 1000 * 100, GuardedDirection.UP), /* */
		    new LongBarrierPassGuard(ThresholdStatus.YELLOW, 1000 * 1000 * 50, GuardedDirection.DOWN), /* */
		    new LongBarrierPassGuard(ThresholdStatus.ORANGE, 1000 * 1000 * 10, GuardedDirection.DOWN), /* */
		    new LongBarrierPassGuard(ThresholdStatus.RED, 1000 * 1000 * 2, GuardedDirection.DOWN), /* */
		    new LongBarrierPassGuard(ThresholdStatus.PURPLE, 1000 * 1000 * 1, GuardedDirection.DOWN) /* */
	    );
	    Thresholds.addThreshold("ThreadCount", "ThreadCount", "ThreadCount", "Current", "default",
		    new LongBarrierPassGuard(ThresholdStatus.GREEN, 200, GuardedDirection.DOWN),
		    new LongBarrierPassGuard(ThresholdStatus.YELLOW, 200, GuardedDirection.UP),
		    new LongBarrierPassGuard(ThresholdStatus.ORANGE, 300, GuardedDirection.UP),
		    new LongBarrierPassGuard(ThresholdStatus.RED, 500, GuardedDirection.UP),
		    new LongBarrierPassGuard(ThresholdStatus.PURPLE, 1000, GuardedDirection.UP));
	}

	log.info("Configuring memory accumulators.");
	setupMemoryAccumulators();
	log.info("Configuring thread accumulators.");
	setupThreadAccumulators();
	log.info("Configuring session accumulators.");
	setupSessionCountAccumulators();
	log.info("Configuring url accumulators.");
	setupUrlAccumulators();
	setupCPUAccumulators();

    }

    private static void setupThreadAccumulators() {
	Accumulators.createAccumulator("ThreadCount", "ThreadCount", "ThreadCount", "current", "default");
	Accumulators.createAccumulator("ThreadStateRunnable-1m", "ThreadStates", "RUNNABLE", "current", "1m");
	Accumulators.createAccumulator("ThreadStateWaiting-1m", "ThreadStates", "WAITING", "current", "1m");
	Accumulators.createAccumulator("ThreadStateBlocked-1m", "ThreadStates", "BLOCKED", "current", "1m");
	Accumulators.createAccumulator("ThreadStateTimedWaiting-1m", "ThreadStates", "TIMED_WAITING", "current", "1m");

	Accumulators.createAccumulator("ThreadStateRunnable-5m", "ThreadStates", "RUNNABLE", "current", "5m");
	Accumulators.createAccumulator("ThreadStateWaiting-5m", "ThreadStates", "WAITING", "current", "5m");
	Accumulators.createAccumulator("ThreadStateBlocked-5m", "ThreadStates", "BLOCKED", "current", "5m");
	Accumulators.createAccumulator("ThreadStateTimedWaiting-5m", "ThreadStates", "TIMED_WAITING", "current", "5m");
    }

    private static void setupSessionCountAccumulators() {
	Accumulators.createAccumulator("SessionCount Cur Absolute", "SessionCount", "Sessions", "cur", "default");
	Accumulators.createAccumulator("SessionCount Cur 1h", "SessionCount", "Sessions", "cur", "1h");
	Accumulators.createAccumulator("SessionCount New 1h", "SessionCount", "Sessions", "new", "1h");
	Accumulators.createAccumulator("SessionCount Del 1h", "SessionCount", "Sessions", "del", "1h");
    }

    public static void setupMemoryAccumulators() {
	Accumulators.createMemoryAccumulator1m("PermGenFree 1m", "MemoryPool-PS Perm Gen-NonHeap", "Free");
	Accumulators.createMemoryAccumulator1m("PermGenFree MB 1m", "MemoryPool-PS Perm Gen-NonHeap", "Free MB");
	Accumulators.createMemoryAccumulator1m("OldGenFree 1m", "MemoryPool-PS Old Gen-Heap", "Free");
	Accumulators.createMemoryAccumulator1m("OldGenFree MB 1m", "MemoryPool-PS Old Gen-Heap", "Free MB");
	Accumulators.createMemoryAccumulator1m("OldGenUsed 1m", "MemoryPool-PS Old Gen-Heap", "Used");
	Accumulators.createMemoryAccumulator1m("OldGenUsed MB 1m", "MemoryPool-PS Old Gen-Heap", "Used MB");

	Accumulators.createMemoryAccumulator5m("PermGenFree 5m", "MemoryPool-PS Perm Gen-NonHeap", "Free");
	Accumulators.createMemoryAccumulator5m("PermGenFree MB 5m", "MemoryPool-PS Perm Gen-NonHeap", "Free MB");
	Accumulators.createMemoryAccumulator5m("OldGenFree 5m", "MemoryPool-PS Old Gen-Heap", "Free");
	Accumulators.createMemoryAccumulator5m("OldGenFree MB 5m", "MemoryPool-PS Old Gen-Heap", "Free MB");
	Accumulators.createMemoryAccumulator5m("OldGenUsed 5m", "MemoryPool-PS Old Gen-Heap", "Used");
	Accumulators.createMemoryAccumulator5m("OldGenUsed MB 5m", "MemoryPool-PS Old Gen-Heap", "Used MB");

	Accumulators.createMemoryAccumulator("PermGenFree 1h", "MemoryPool-PS Perm Gen-NonHeap", "Free", "1h");
	Accumulators.createMemoryAccumulator("PermGenFree MB 1h", "MemoryPool-PS Perm Gen-NonHeap", "Free MB", "1h");
	Accumulators.createMemoryAccumulator("OldGenFree 1h", "MemoryPool-PS Old Gen-Heap", "Free", "1h");
	Accumulators.createMemoryAccumulator("OldGenFree MB 1h", "MemoryPool-PS Old Gen-Heap", "Free MB", "1h");
	Accumulators.createMemoryAccumulator("OldGenUsed 1h", "MemoryPool-PS Old Gen-Heap", "Used", "1h");
	Accumulators.createMemoryAccumulator("OldGenUsed MB 1h", "MemoryPool-PS Old Gen-Heap", "Used MB", "1h");

    }

    public static void setupUrlAccumulators() {
	Accumulators.createUrlREQAccumulator("URL REQ 1m", "cumulated", "1m");
	Accumulators.createUrlREQAccumulator("URL REQ 5m", "cumulated", "5m");
	Accumulators.createUrlREQAccumulator("URL REQ 1h", "cumulated", "1h");

	Accumulators.createUrlAVGAccumulator("URL AVG 1m", "cumulated", "1m");
	Accumulators.createUrlAVGAccumulator("URL AVG 5m", "cumulated", "5m");
	Accumulators.createUrlAVGAccumulator("URL AVG 1h", "cumulated", "1h");

	Accumulators.createUrlTotalTimeAccumulator("URL Time 1m", "cumulated", "1m");
	Accumulators.createUrlTotalTimeAccumulator("URL Time 5m", "cumulated", "5m");
	Accumulators.createUrlTotalTimeAccumulator("URL Time 1h", "cumulated", "1h");

    }

    public static void setupCPUAccumulators() {
	Accumulators.createAccumulator("CPU Time 1m", "OS", "OS", "CPU Time", "1m"); // ,
										     // net.anotheria.moskito.core.stats.TimeUnit.SECONDS);
	Accumulators.createAccumulator("CPU Time 5m", "OS", "OS", "CPU Time", "5m"); // ,
										     // net.anotheria.moskito.core.stats.TimeUnit.SECONDS);
	Accumulators.createAccumulator("CPU Time 1h", "OS", "OS", "CPU Time", "1h"); // ,
										     // net.anotheria.moskito.core.stats.TimeUnit.SECONDS);
	// OS.OS.CPU Time/default/NANOSECONDS
    }

    @Override
    protected void doStop() {
	if (isSetApplicationContext()) {
	    this.applicationContext.stop();
	    if (isSetServletContext()) {
		contextLoader.closeWebApplicationContext(getServletContext());
	    } else {
		if (applicationContext instanceof org.springframework.context.support.AbstractApplicationContext)
		    ((org.springframework.context.support.AbstractApplicationContext) applicationContext).close();
	    }
	}
    }

    @Override
    public void destroy() {
	if (isSetApplicationContext()) {
	    if (isSetServletContext()) {
		contextLoader.closeWebApplicationContext(getServletContext());
	    } else {
		if (applicationContext instanceof org.springframework.context.support.AbstractApplicationContext)
		    ((org.springframework.context.support.AbstractApplicationContext) applicationContext).close();
	    }
	}
    }

    public ConfigurableApplicationContext getApplicationContext() {
	return this.applicationContext;
    }

    public void autowireComponent(Object obj) {
	if (isSetApplicationContext()) {
	    getApplicationContext().getAutowireCapableBeanFactory().autowireBeanProperties(obj,
		    AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
	}
    }

    public <T> T getComponent(Class<T> requiredType) {

	if (!isSetApplicationContext()) {
	    throw new IllegalStateException("");
	}

	if (requiredType == null) {
	    throw new ComponentNotFoundException("");
	}

	try {
	    return getApplicationContext().getBean(requiredType);
	} catch (NoSuchBeanDefinitionException e) {
	    throw new ComponentNotFoundException(e);
	}
    }

    public void refresh() {
	if (!isSetApplicationContext()) {
	    throw new IllegalStateException();
	}
	getApplicationContext().refresh();
    }

    public Version getVersion() {
	return this.version;
    }

    @EventListener
    public void onEvent(StateChangeEvent event) {
	Object source = event.getSource();
	if (source instanceof Component) {

	    if (source instanceof AdminService) {

	    } else {

	    }
	    log.debug(String.format("[%s] %s > %s", ((Component) source).getName(), event.getOldState().toString(),
		    event.getNewState().toString()));
	}
    }

    @EventListener
    public void onEvent(ApplicationPropertyChangeEvent event) {
	log.debug("property changed " + event.getOldValue() + ">" + event.getNewValue());
    }

    public boolean isReady() {
	if (isSetApplicationContext()) {
	    return true;
	}
	return false;
    }

}