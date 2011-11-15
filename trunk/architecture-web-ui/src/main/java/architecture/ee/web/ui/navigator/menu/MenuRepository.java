package architecture.ee.web.ui.navigator.menu;

import java.util.Set;

/**
 * @author DEPeart
 *
 */
public interface MenuRepository {
	
    public abstract Set getMenuNames();

    public abstract MenuComponent getMenu(String name);

    public abstract void load() throws Exception;

    public abstract void reload() throws Exception;
    
}
