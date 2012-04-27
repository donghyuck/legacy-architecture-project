package architecture.ee.jdbc.sequencer;

/**
 * @author                 donghyuck
 */
public interface Sequencer
{

	public abstract String getName();
	
	public abstract long getNext() ;

    public abstract int getBlockSize();
    
    public abstract void setBlockSize(int blockSize);
    
}