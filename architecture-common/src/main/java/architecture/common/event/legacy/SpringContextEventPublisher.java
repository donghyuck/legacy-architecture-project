package architecture.common.event.legacy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import architecture.common.event.Event;
import architecture.common.event.EventListener;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>An event listener for all Spring {@link org.springframework.context.ApplicationEvent application events} so that they get published to the actual
 * Spring context.<p>
 * <p>Simply register this against your Spring context to handle event the same 'old' way</p>
 *
 * @since 2.0.0
 */
public class SpringContextEventPublisher implements ApplicationContextAware, EventListener {
    private ApplicationContext applicationContext;

    public void handleEvent(Event event) {
        checkNotNull(applicationContext).publishEvent(event);
    }

    public Class[] getHandledEventClasses() {
        return new Class[]{Event.class};
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = checkNotNull(applicationContext);
    }

    @Override
    public String toString() {
        return "SpringContextEventPublisher{applicationContext=" + applicationContext + '}';
    }
}
