package architecture.ee.upgrade.post;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class PostUpgradeTaskConverter implements Converter {

    PostUpgradeTaskConverter()
    {
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context)
    {
        PostUpgradeConfig.Upgrade.Task task = (PostUpgradeConfig.Upgrade.Task)source;
        writer.addAttribute("property", String.valueOf(task.getProperty()));
        writer.addAttribute("action", task.getAction());
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context)
    {
        String property = reader.getAttribute("property");
        String action = reader.getAttribute("action");
        return new PostUpgradeConfig.Upgrade.Task(action, property);
    }

    public boolean canConvert(Class type)
    {
        return type == PostUpgradeConfig.Upgrade.Task.class;
    }
}
