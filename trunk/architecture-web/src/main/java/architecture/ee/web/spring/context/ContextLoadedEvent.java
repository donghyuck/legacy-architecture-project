package architecture.ee.web.spring.context;

import org.springframework.context.ApplicationEvent;

public class ContextLoadedEvent extends ApplicationEvent {

	public ContextLoadedEvent(Object obj) {
		super(obj);
	}

}
