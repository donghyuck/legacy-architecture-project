package architecture.ee.spring.event.support;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.common.event.spi.EventDispatcher;

/**
 * @author   donghyuck
 */
public class EventSourceSupport implements EventSource {

	/**
	 * @uml.property  name="dispatcher"
	 * @uml.associationEnd  
	 */
	private EventDispatcher dispatcher;
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		// TODO Auto-generated method stub
		
	}

}
