package architecture.ee.util.proxy;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class GenericCollectionProxy extends AbstractCollection implements Serializable {

	private Collection proxiedCollection;
	
	public GenericCollectionProxy(Collection col, GenericProxyFactory proxyFactory)
    {
        proxiedCollection = new ArrayList();
        for(Object aCol : col ){
        	Object o = proxyFactory.createProxy(aCol);
            if(o != null)
                proxiedCollection.add(o);
        }
    }
	
	@Override
	public Iterator iterator() {
		return proxiedCollection.iterator();
	}

	@Override
	public int size() {
		return proxiedCollection.size();
	}

}
