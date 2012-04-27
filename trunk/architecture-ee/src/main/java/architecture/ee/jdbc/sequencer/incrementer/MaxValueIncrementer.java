package architecture.ee.jdbc.sequencer.incrementer;

public interface MaxValueIncrementer {
	
    public abstract long nextLongValue(int ID);

    public abstract long nextLongValue(String name);

    public abstract long currentLongValue(String name);
    
}
