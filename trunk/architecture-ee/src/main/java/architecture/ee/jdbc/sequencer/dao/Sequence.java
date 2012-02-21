package architecture.ee.jdbc.sequencer.dao;

public interface Sequence {
	
    public abstract long nextID(int sequenceID);

    public abstract long nextID(String name);

    public abstract long currentID(String name);
    
}
