package architecture.ee.plugin;

public interface PluginListener
{
    public abstract void pluginCreated(String s, Plugin plugin);

    public abstract void pluginDestroyed(String s, Plugin plugin);
}
