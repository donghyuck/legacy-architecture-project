package architecture.common.event.internal;

import architecture.common.event.config.ListenerHandlersConfiguration;
import architecture.common.event.spi.ListenerHandler;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * <p>The default configuration that only uses the {@link com.atlassian.event.internal.AnnotatedMethodsListenerHandler}.</p>
 * <p>Products that need to remain backward compatible will have to override this configuration</p>
 */
public class ListenerHandlerConfigurationImpl implements ListenerHandlersConfiguration {
    public List<ListenerHandler> getListenerHandlers() {
        return Lists.<ListenerHandler>newArrayList(new AnnotatedMethodsListenerHandler());
    }
}
