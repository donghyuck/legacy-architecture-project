package architecture.ee.upgrade;


public class UpgradeTaskException extends Exception
{

    String name;
    int version;
    String instructions;
    
    public UpgradeTaskException(String name, int version, String instructions, Exception e)
    {
        super(e);
        this.name = name;
        this.version = version;
        this.instructions = instructions;
    }

    public String getName()
    {
        return name;
    }

    public int getVersion()
    {
        return version;
    }

    public String getInstructions()
    {
        return instructions;
    }

}

