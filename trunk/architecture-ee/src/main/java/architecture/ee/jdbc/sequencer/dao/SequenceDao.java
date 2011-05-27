package architecture.ee.jdbc.sequencer.dao;

public interface SequenceDao {
	
    public abstract long nextID(int sequenceID);

    public abstract long nextID(String name);

    public abstract long currentID(String name);
    
}
