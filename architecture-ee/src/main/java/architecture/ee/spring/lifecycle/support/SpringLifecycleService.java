package architecture.ee.spring.lifecycle.support;

import org.springframework.context.Lifecycle;

import architecture.common.lifecycle.ComponentImpl;
import architecture.common.lifecycle.State;

public class SpringLifecycleService extends ComponentImpl implements Lifecycle {
	
	public boolean isRunning() {
		return ( getState() == State.STARTED || getState() == State.RUNNING ) ;
	}
}
