package architecture.common.event.config;

import architecture.common.event.spi.ListenerHandler;

import java.util.List;

/**
 * Specifies a listener handler configuration to use
 */
public interface ListenerHandlersConfiguration {
    List<ListenerHandler> getListenerHandlers();
}
