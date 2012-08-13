package architecture.ee.plugin;

public class RequireRestartResult implements ConfiguratorResult {
	
	private static final RequireRestartResult INSTANCE = new RequireRestartResult();
	
    public static RequireRestartResult getRequireRestartResult()
    {
        return INSTANCE;
    }

    private RequireRestartResult()
    {
    }

    public String toString()
    {
        return "RequireRestartResult";
    }
    
}
