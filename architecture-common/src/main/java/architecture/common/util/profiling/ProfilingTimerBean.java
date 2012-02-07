package architecture.common.util.profiling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ProfilingTimerBean
    implements Serializable
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -2544236547702391434L;
	List<ProfilingTimerBean> children;
    ProfilingTimerBean parent;
    String resource;
    long startTime;
    long totalTime;
    long startMem;
    long totalMem;
    boolean hasMem;
    
    public ProfilingTimerBean(String resource)
    {
        children = new ArrayList<ProfilingTimerBean>();
        parent = null;
        hasMem = false;
        this.resource = resource;
    }

    protected void addParent(ProfilingTimerBean parent)
    {
        this.parent = parent;
    }

    public ProfilingTimerBean getParent()
    {
        return parent;
    }

    public void addChild(ProfilingTimerBean child)
    {
        children.add(child);
        child.addParent(this);
    }

    public void setStartTime()
    {
        startTime = System.currentTimeMillis();
    }

    public void setEndTime()
    {
        totalTime = System.currentTimeMillis() - startTime;
    }

    public void setStartMem()
    {
        startMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        hasMem = true;
    }

    public void setEndMem()
    {
        totalMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - startMem;
    }

    public String getResource()
    {
        return resource;
    }

    public String getPrintable(long minTime)
    {
        return getPrintable("", minTime);
    }

    protected String getPrintable(String indent, long minTime)
    {
        if(totalTime >= minTime)
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append(indent);
            buffer.append("[" + totalTime + "ms] ");
            if(hasMem)
            {
                buffer.append("[" + totalMem / 1024L + "KB used] ");
                buffer.append("[" + Runtime.getRuntime().freeMemory() / 1024L + "KB Free] ");
            }
            buffer.append("- " + resource);
            buffer.append("\n");
            for(ProfilingTimerBean childrenIt : children ){
            	childrenIt.getPrintable(indent + "  ", minTime );
            }
            return buffer.toString();
        } else
        {
            return "";
        }
    }

    public long getTotalTime()
    {
        return totalTime;
    }

}

