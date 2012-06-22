package architecture.ee.jdbc.sequencer;

/**
 * @author                   donghyuck
 */
public interface Sequencer
{

	public abstract String getName();
	
	public abstract long getNext() ;

    /**
	 * @return
	 * @uml.property  name="blockSize"
	 */
    public abstract int getBlockSize();
    
    /**
	 * @param  blockSize
	 * @uml.property  name="blockSize"
	 */
    public abstract void setBlockSize(int blockSize);
    
}