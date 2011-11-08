package architecture.ee.util.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import architecture.ee.util.proxy.GenericCollectionProxy;
import architecture.ee.util.proxy.GenericProxyFactory;
import architecture.ee.util.proxy.GenericSetProxy;

/**
 * @author  donghyuck
 */
public class TimedExpirationMap implements Cache {

	
	private static class ExpirableValueWrapper {

		final Object key;
		final Object value;
		final long timestamp = System.currentTimeMillis();
		volatile boolean ignore;

		ExpirableValueWrapper(Object key, Object value) {
			ignore = false;
			this.key = key;
			this.value = value;
		}
	}
	 	

    /**
	 * @uml.property  name="maxLifetime"
	 */
    private final AtomicLong maxLifetime = new AtomicLong();
    private final AtomicLong lastChecked = new AtomicLong();
    private final AtomicBoolean expiring = new AtomicBoolean();
    private final long expirationPeriod;
    private final ConcurrentMap map = new ConcurrentHashMap();
    private final Queue expirationQueue = new ConcurrentLinkedQueue();
    
    /**
	 * @uml.property  name="name"
	 */
    private String name;	
	

    public TimedExpirationMap(String name, long lifetime, long expirationPeriod)
    {
        this.name = name;
        maxLifetime.set(lifetime);
        this.expirationPeriod = expirationPeriod;
    }

    public int size()
    {
        expireEntries();
        return map.size();
    }

    public boolean isEmpty()
    {
        return size() == 0;
    }

    public boolean containsKey(Object key)
    {
        expireEntries();
        return map.containsKey(key);
    }

    public boolean containsValue(Object value)
    {
        throw new UnsupportedOperationException();
    }

    public Object get(Object key)
    {
        expireEntries();
        ExpirableValueWrapper wrapper = (ExpirableValueWrapper)map.get(key);
        if(wrapper != null)
            return wrapper.value;
        else
            return null;
    }

    public Object put(Object key, Object value)
    {
        expireEntries();
        ExpirableValueWrapper wrapper = new ExpirableValueWrapper(key, value);
        ExpirableValueWrapper oldVal = (ExpirableValueWrapper)map.put(key, wrapper);
        expirationQueue.offer(wrapper);
        return processWrapper(oldVal);
    }

    public Object remove(Object key)
    {
        expireEntries();
        return processWrapper((ExpirableValueWrapper)map.remove(key));
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
	 * @param name
	 * @uml.property  name="name"
	 */
    public void setName(String name)
    {
        this.name = name;
    }

    public int getMaxCacheSize()
    {
        return 0;
    }

    public void setMaxCacheSize(int i)
    {
    }

    /**
	 * @return
	 * @uml.property  name="maxLifetime"
	 */
    public long getMaxLifetime()
    {
        return maxLifetime.get();
    }

    public void setMaxLifetime(long maxLifetime)
    {
        this.maxLifetime.set(maxLifetime);
    }

    public int getCacheSize()
    {
        return 0;
    }

    public long getCacheHits()
    {
        return 0L;
    }

    public long getCacheMisses()
    {
        return 0L;
    }

    private Object processWrapper(ExpirableValueWrapper wrapper)
    {
        if(wrapper != null)
        {
            wrapper.ignore = true;
            return wrapper.value;
        } else
        {
            return null;
        }
    }

    public void putAll(Map t)
    {
    	for(Map.Entry entry : (Set<Map.Entry>)t.entrySet()){
    		put(entry.getKey(), entry.getValue());
    	}
    }

    public void clear()
    {
        expirationQueue.clear();
        map.clear();
    }

    public Set keySet()
    {
        expireEntries();
        return Collections.unmodifiableSet(map.keySet());
    }

    public Collection values()
    {
        expireEntries();
        
        return Collections.unmodifiableCollection(
        	new GenericCollectionProxy(map.values(), new GenericProxyFactory(){

				public Object createProxy(Object obj) {		
					
					return ((ExpirableValueWrapper)obj).value;
					
				}        		
        	}));
    }

    public Set entrySet()
    {
        expireEntries();
        return Collections.unmodifiableSet(
        	new GenericSetProxy(
                map.entrySet(), 
                new GenericProxyFactory(){
					public Object createProxy(Object obj) {
						final java.util.Map.Entry entry = (java.util.Map.Entry)obj;
						
						return new java.util.Map.Entry() {
							
		                    public Object getKey()
		                    {
		                        return entry.getKey();
		                    }

		                    public Object getValue()
		                    {
		                        return ((ExpirableValueWrapper)entry.getValue()).value;
		                    }

		                    public Object setValue(Object value)
		                    {
		                        throw new UnsupportedOperationException();
		                    }
		                    
		                };
					}
                	
                }));
    
   
    }

    private void expireEntries()
    {
        long now = System.currentTimeMillis();
        if(now - lastChecked.get() >= expirationPeriod && expiring.compareAndSet(false, true))
            try
            {
                lastChecked.set(now);
                ExpirableValueWrapper value = null;
                do
                {
                    if((value = (ExpirableValueWrapper)expirationQueue.peek()) == null || now - value.timestamp < maxLifetime.get())
                        break;
                    expirationQueue.poll();
                    if(!value.ignore)
                        map.remove(value.key);
                } while(true);
            }
            finally
            {
                expiring.set(false);
            }
    }
}
