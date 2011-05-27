package architecture.ee.jdbc.sequencer.dao;

import java.util.Map;

import architecture.ee.jdbc.sequencer.JdbcSequencerFactory;
import architecture.ee.jdbc.sequencer.Sequencer;

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
		return 0;
	}

	public long currentID(String name) {
		return 0;
	}
	
}
