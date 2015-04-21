package architecture.ee.upgrade;


/**
 * @author  donghyuck
 */
public class UpgradeTaskException extends Exception
{

    /**
	 * @uml.property  name="name"
	 */
    String name;
    /**
	 * @uml.property  name="version"
	 */
    int version;
    /**
	 * @uml.property  name="instructions"
	 */
    String instructions;
    
    public UpgradeTaskException(String name, int version, String instructions, Exception e)
    {
        super(e);
        this.name = name;
        this.version = version;
        this.instructions = instructions;
    }

    /**
	 * @return
	 * @uml.property  name="name"
	 */
    public String getName()
    {
        return name;
    }

    /**
	 * @return
	 * @uml.property  name="version"
	 */
    public int getVersion()
    {
        return version;
    }

    /**
	 * @return
	 * @uml.property  name="instructions"
	 */
    public String getInstructions()
    {
        return instructions;
    }

}

