package architecture.ee.jdbc.sequencer.incrementer;

import java.util.Map;

import architecture.ee.jdbc.sequencer.Sequencer;
import architecture.ee.jdbc.sequencer.SequencerFactory;
import architecture.ee.jdbc.sequencer.impl.JdbcSequencer;

/**
 * @author   donghyuck
 */
public class JdbcMaxValueIncrementer implements MaxValueIncrementer {
	
	/**
	 * @uml.property  name="factory"
	 * @uml.associationEnd  
	 */
	private SequencerFactory factory ;	
	
	private Map<Integer, Sequencer> sequencers ;
	
	public JdbcMaxValueIncrementer(SequencerFactory factory) {
		this.factory = factory;
	}

	public void initialize(){
		sequencers = factory.getAllSequencer();
	}
	
	public long nextLongValue(int i) {
		return sequencers.get(i).getNext();
	}

	public long nextLongValue(String name) {
		for( Sequencer sequencer : sequencers.values()){
			if( name.equals(sequencer.getName()) )
				return sequencer.getNext();
		}		
		
		JdbcSequencer sequencer = factory.createJdbcSequencer(0, name, 5);
		sequencers.put(sequencer.getSequencerId(), sequencer);
		
		return sequencer.getNext();		
	}

	public long currentLongValue(String name) {
		return 0;
	}
	
}
