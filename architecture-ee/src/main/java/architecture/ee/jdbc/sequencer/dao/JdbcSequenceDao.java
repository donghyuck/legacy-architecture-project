package architecture.ee.jdbc.sequencer.dao;

import java.util.Map;

import architecture.ee.jdbc.sequencer.JdbcSequencerFactory;
import architecture.ee.jdbc.sequencer.Sequencer;
import architecture.ee.jdbc.sequencer.internal.JdbcSequencer;

public class JdbcSequenceDao implements SequenceDao {
	
	private JdbcSequencerFactory factory ;	
	private Map<Integer, Sequencer> sequencers ;
	
	public JdbcSequenceDao(JdbcSequencerFactory factory) {
		this.factory = factory;
	}

	public void initialize(){
		sequencers = factory.getAllSequencer();
	}
	
	public long nextID(int i) {
		return sequencers.get(i).getNext();
	}

	public long nextID(String name) {
		for( Sequencer sequencer : sequencers.values()){
			if( name.equals(sequencer.getName()) )
				return sequencer.getNext();
		}
		
		JdbcSequencer sequencer = factory.createJdbcSequencer(0, name, 5);
		sequencers.put(sequencer.getSequencerId(), sequencer);
		return sequencer.getNext();		
	}

	public long currentID(String name) {
		return 0;
	}
	
}
