package architecture.common.event.config;

import java.util.List;

import architecture.common.event.spi.ListenerHandler;

/**
 * Specifies a listener handler configuration to use
 */
public interface ListenerHandlersConfiguration {
    List<ListenerHandler> getListenerHandlers();
}
