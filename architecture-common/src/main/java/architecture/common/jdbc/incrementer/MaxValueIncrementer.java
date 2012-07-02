package architecture.common.jdbc.incrementer;

public interface MaxValueIncrementer {
	
    public abstract long nextLongValue(int Id);

    public abstract long nextLongValue(String name);

    public abstract long currentLongValue(String name);
    
}
