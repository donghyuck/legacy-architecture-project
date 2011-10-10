package architecture.ee.spring.event.support;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.common.event.spi.EventDispatcher;

public class EventSourceSupport implements EventSource {

	private EventDispatcher dispatcher;
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		// TODO Auto-generated method stub
		
	}

}
