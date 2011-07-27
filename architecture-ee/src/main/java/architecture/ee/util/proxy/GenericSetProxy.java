package architecture.ee.util.proxy;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class GenericSetProxy extends AbstractSet
{

    private final Set proxiedSet = new HashSet();
    
    public GenericSetProxy(Set set, GenericProxyFactory factory)
    {
        Object e;
        for(Iterator i$ = set.iterator(); i$.hasNext(); proxiedSet.add(e))
        {
            Object o = i$.next();
            e = factory.createProxy(o);
        }

    }

    public Iterator iterator()
    {
        return proxiedSet.iterator();
    }

    public int size()
    {
        return proxiedSet.size();
    }

}

