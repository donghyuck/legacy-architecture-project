package architecture.ee.jdbc.sequencer.incrementer;

import java.util.Map;

import architecture.common.jdbc.incrementer.MaxValueIncrementer;
import architecture.common.jdbc.sequencer.Sequencer;
import architecture.ee.jdbc.sequencer.JdbcSequencer;
import architecture.ee.jdbc.sequencer.SequencerFactory;

/**
 * @author   donghyuck
 */
public class JdbcMaxValueIncrementer implements MaxValueIncrementer {
	
	/**
	 * @uml.property  name="factory"
	 * @uml.associationEnd  ls
	 * 
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
