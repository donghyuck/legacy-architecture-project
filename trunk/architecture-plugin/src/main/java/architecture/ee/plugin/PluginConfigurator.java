package architecture.ee.plugin;

public interface PluginConfigurator
{
    public abstract void configure(ConfigurationContext configurationcontext);

    public abstract void destroy(ConfigurationContext configurationcontext);
}
