package architecture.ee.event;

import architecture.common.lifecycle.event.Event;

/**
 * @author  donghyuck
 */
public class PluginStateChangeEvent extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6725688776619308652L;

	/**
	 * @author   donghyuck
	 */
	public enum State {
		/**
		 * @uml.property  name="iNSTALLED"
		 * @uml.associationEnd  
		 */
		INSTALLED, 
		/**
		 * @uml.property  name="uNINSTALLED"
		 * @uml.associationEnd  
		 */
		UNINSTALLED, 
		/**
		 * @uml.property  name="uNLOADED"
		 * @uml.associationEnd  
		 */
		UNLOADED, 
		/**
		 * @uml.property  name="rESTART"
		 * @uml.associationEnd  
		 */
		RESTART
	};
	
	/**
	 * @uml.property  name="state"
	 * @uml.associationEnd  
	 */
	private State state;
	
	public PluginStateChangeEvent(Object source, State state) {
		super(source);
	}

	/**
	 * @return
	 * @uml.property  name="state"
	 */
	public State getState() {
		return state;
	}
	
}
