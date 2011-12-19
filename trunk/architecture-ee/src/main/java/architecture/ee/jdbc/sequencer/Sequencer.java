package architecture.ee.jdbc.sequencer;

/**
 * @author                 donghyuck
 */
public interface Sequencer
{

	public abstract String getName();
	
	public abstract long getNext() ;

    /**
	 * @return
	 */
    public abstract int getBlockSize();
    
    /**
	 * @param  blockSize
	 */
    public abstract void setBlockSize(int blockSize);
    
}