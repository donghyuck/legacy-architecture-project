package architecture.common.event.spring;

import architecture.common.event.api.EventListenerRegistrar;
import architecture.common.event.config.ListenerHandlersConfiguration;
import architecture.common.event.spi.ListenerHandler;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Convenience class that registers/unregisters beans that implement the {@code EventListener} interfaces, or has
 * method(s) annotated with the {@code EventListener} annotation with the {@code EventListenerRegistrar} on bean
 * creation and bean destruction.
 * <p>
 * {@code EventListenerRegistrarBeanProcessor} is implemented as a Spring {@code BeanPostProcessor}, which means that it
 * gets called whenever a bean is created or destroyed in the application context. Because we need to get callbacks for
 * all beans that get created, it is important to inject the minimum of dependencies through CI, as the
 * {@code EventListenerRegistrarBeanProcessor} can only be created AFTER all of its dependencies have been created.
 * <p>
 * For this reason, the <em>name</em> of the {@code EventListenerRegistrar} bean is injected, <em>not</em> the
 * {@code EventListenerRegistrar} instance itself. While the {@code EventListenerRegistrarBeanProcessor} does not have
 * the {@code EventListenerRegistrar} yet, all beans that should be registered are stored in the
 * {@code listenersToBeRegistered} map. When the {@code EventListenerRegistrarBeanProcessor} gets hold of the
 * {@code EventListenerRegistrar}, all beans in {@code listenersToBeRegistered} are registered and the map is cleared.
 *
 * @since 2.3.0
 */
public class EventListenerRegistrarBeanProcessor implements DestructionAwareBeanPostProcessor, BeanFactoryAware, Ordered, ApplicationListener {

    /**
     * These are blacklisted because components in these plugins are known to register themselves with the
     * EventPublisher. We're skipping them to save time on startup. Functionally, nothing would go wrong if we
     * did try to auto-register them as the EventPublisher will only register a listener once.
     */
    private static final Set<String> BLACKLISTED_PLUGIN_KEYS = ImmutableSet.of(
            "com.atlassian.upm.atlassian-universal-plugin-manager-plugin",
            "com.atlassian.activeobjects.activeobjects-plugin",
            "com.atlassian.applinks.applinks-plugin",
            "com.atlassian.crowd.embedded.admin",
            "com.atlassian.oauth.admin",
            "com.atlassian.oauth.consumer",
            "com.atlassian.oauth.consumer.sal",
            "com.atlassian.oauth.serviceprovider",
            "com.atlassian.oauth.serviceprovider.sal",
            "com.atlassian.plugins.rest.atlassian-rest-module",
            "com.atlassian.soy.soy-template-plugin",
            "com.atlassian.templaterenderer.api",
            "com.atlassian.templaterenderer.atlassian-template-renderer-velocity1.6-plugin",
            "com.atlassian.auiplugin"
    );

    private static final Logger LOG = LoggerFactory.getLogger(EventListenerRegistrarBeanProcessor.class);

    private final String eventListenerRegistrarBeanName;
    private final ListenerHandlersConfiguration listenerHandlersConfiguration;

    private final Map<String, Object> listenersToBeRegistered = Maps.newHashMap();
    private final Multimap<String, Object> eventListenersFromPlugins = HashMultimap.create();

    private ConfigurableBeanFactory beanFactory;
    private EventListenerRegistrar eventListenerRegistrar;
    private boolean ignoreFurtherBeanProcessing;

    public EventListenerRegistrarBeanProcessor(String eventListenerRegistrarBeanName, ListenerHandlersConfiguration listenerHandlersConfiguration) {
        this.eventListenerRegistrarBeanName = checkNotNull(eventListenerRegistrarBeanName);
        this.listenerHandlersConfiguration = checkNotNull(listenerHandlersConfiguration);
    }

    @Override
    public int getOrder() {
        // process EventListenerRegistrarBeanProcessor as early as possible to guarantee that we don't get passed any AOP-ed proxies.
        return 1;
    }

    /*
    @PluginEventListener
    public void onPluginModuleEnabled(PluginModuleEnabledEvent event) {
        Plugin plugin = event.getModule().getPlugin();

        if (BLACKLISTED_PLUGIN_KEYS.contains(plugin.getKey())) {
            return;
        }

        ModuleDescriptor moduleDescriptor = event.getModule();

        if (isSuitablePluginModule(moduleDescriptor)) {

            // moduleDescriptor.getModule creates a new instance every time it's called. There's no point
            // in looking for @EventListener annotations.
            if (moduleDescriptorReturnsNewInstanceEveryTime(moduleDescriptor)) {
                return;
            }

            try {
                Object module = moduleDescriptor.getModule();

                try {
                    if (canBeRegisteredAsAListener(moduleDescriptor.getKey(), module)) {
                        eventListenersFromPlugins.put(plugin.getKey(), module);
                        registerListener(moduleDescriptor.getKey(), module);
                    }
                } catch (Throwable t) {
                    if (!(t instanceof NoClassDefFoundError)) {
                        LOG.info("Error registering eventlisteners for module " + moduleDescriptor
                                .getCompleteKey() + "; skipping.", t);
                    } else {
                        LOG.debug("Skipping " + moduleDescriptor
                                .getCompleteKey() + " because not all referenced classes are visible from" +
                                " the classloader.");
                    }
                }
            } catch (Exception e) {
                // if there's no module to get, we don't need to scan for event listeners on the module
                return;
            }
        }
    }
    */
/*
    private static boolean isSuitablePluginModule(ModuleDescriptor moduleDescriptor) {
        final Class<? extends ModuleDescriptor> moduleDescriptorClass = moduleDescriptor.getClass();
        final Class moduleClass = moduleDescriptor.getModuleClass();

        return !moduleDescriptorClass.equals(ComponentImportModuleDescriptor.class) &&
                moduleClass != null && !moduleClass.equals(Void.class);
    }
*/
    /*
    @PluginEventListener
    public void onPluginDisabled(PluginDisabledEvent event) {
        Plugin plugin = event.getPlugin();
        Collection<Object> listeners = eventListenersFromPlugins.get(plugin.getKey());
        if (listeners != null) {
            for (Object eventListener : listeners) {
                eventListenerRegistrar.unregister(eventListener);
            }
            eventListenersFromPlugins.removeAll(plugin.getKey());
        }
    }

    @PluginEventListener
    public void onPluginModuleDisabled(PluginModuleDisabledEvent event) {
        ModuleDescriptor moduleDescriptor = event.getModule();
        Object module = null;
        try {
            module = moduleDescriptor.getModule();
        } catch (Exception e) {
            // there is no module to get; it would not have been registered anyway.
        }
        if (module != null && eventListenersFromPlugins.remove(moduleDescriptor.getPluginKey(), module)) {
            eventListenerRegistrar.unregister(module);
        }
    }

    private static boolean moduleDescriptorReturnsNewInstanceEveryTime(ModuleDescriptor moduleDescriptor) {
        return moduleDescriptor.getModule() != moduleDescriptor.getModule();
    }
    */

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ContextRefreshedEvent) {
            // Once the spring context has been refreshed the only beans to be processed from that point onwards will be prototypes.
            // These should not be listening for Events in the first place, and so we can safely ignore them.
            ignoreFurtherBeanProcessing = true;
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals(eventListenerRegistrarBeanName)) {
            eventListenerRegistrar = (EventListenerRegistrar) bean;
            if (isAListener(this)) {
                // If there is a ListenerHandler for @PluginEventListener, then register ourself as a listener
                eventListenerRegistrar.register(this);
            }

            for (Object object : listenersToBeRegistered.values()) {
                eventListenerRegistrar.register(object);
            }

            listenersToBeRegistered.clear();
        }
        return bean;
    }

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        unregisterListener(bean, beanName);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (!ignoreFurtherBeanProcessing && canBeRegisteredAsAListener(beanName, bean)) {
            registerListener(beanName, bean);
        }
        return bean;
    }

    private boolean canBeRegisteredAsAListener(String beanName, Object bean) {
        if (isAListener(bean)) {
            try {
                // The cost of merging is relatively high, which can have a _huge_ impact for large numbers of prototype beans. eg Hibernate Validation
                // we only care about singleton beans; the prototype beans typically have a short lifespan
                return beanFactory.getMergedBeanDefinition(beanName).isSingleton();
            } catch (NoSuchBeanDefinitionException e) {
                // no bean with that name; must be an anonymous bean, so register it anyway.
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableBeanFactory) beanFactory;
    }

    private void registerListener(String beanName, Object bean) {
        LOG.debug("Registering {} instance as an eventlistener", beanName);
        if (eventListenerRegistrar != null) {
            eventListenerRegistrar.register(bean);
        } else {
            listenersToBeRegistered.put(beanName, bean);
        }
    }

    private void unregisterListener(Object bean, String beanName) {
        if (eventListenerRegistrar != null) {
            eventListenerRegistrar.unregister(bean);
        } else {
            listenersToBeRegistered.remove(beanName);
        }
    }

    private boolean isAListener(Object object) {
        for (ListenerHandler handler : listenerHandlersConfiguration.getListenerHandlers()) {
            if (!handler.getInvokers(object).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean requiresDestruction(Object bean) {
	return false;
    }
    
}
