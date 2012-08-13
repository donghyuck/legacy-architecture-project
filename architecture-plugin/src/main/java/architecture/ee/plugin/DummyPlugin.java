package architecture.ee.plugin;


public class DummyPlugin
    implements Plugin
{

    private String name;

    public DummyPlugin(String name)
    {
        this.name = name;
    }

    public void destroy()
    {
    }

    public boolean equals(Object o)
    {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
        {
            return false;
        } else
        {
            DummyPlugin that = (DummyPlugin)o;
            return name == null ? that.name != null : !name.equals(that.name);
        }
    }

    public int hashCode()
    {
        return name == null ? 0 : name.hashCode();
    }

    public void init()
    {
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("DummyPlugin");
        sb.append("{name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
