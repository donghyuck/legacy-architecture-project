package architecture.ee.plugin;

public class PluginRequiresRebuildResult implements ConfiguratorResult {
	
    private static final PluginRequiresRebuildResult instance = new PluginRequiresRebuildResult();

    public static PluginRequiresRebuildResult getPluginRequiresRebuildResult()
    {
        return instance;
    }

    private PluginRequiresRebuildResult()
    {
    }
}
