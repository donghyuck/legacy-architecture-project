/*
 * SimplePermissionsAdapter.java
 *
 * Created on April 30, 2002, 1:27 AM
 */
package architecture.ee.web.ui.navigator.example;

import java.util.ArrayList;

import architecture.ee.web.ui.navigator.menu.MenuComponent;
import architecture.ee.web.ui.navigator.menu.PermissionsAdapter;



/**
 *
 * @author  ssayles
 */
public class SimplePermissionsAdapter implements PermissionsAdapter {
    //~ Instance fields ========================================================

    private ArrayList menuNames;

    //~ Constructors ===========================================================

    /**
     * Creates a new instance of SimplePermissionAdapter
     */
    public SimplePermissionsAdapter(String[] theMenuNames) {
        menuNames = new ArrayList();

        if (theMenuNames != null) {
            for (int i = 0; i < theMenuNames.length; i++) {
                menuNames.add(theMenuNames[i]);
            }
        }
    }

    //~ Methods ================================================================

    /**
     * If the menu is allowed, this should return true.
     *
     * @return whether or not the menu is allowed.
     */
    public boolean isAllowed(MenuComponent menu) {
        return !menuNames.contains(menu.getName());
    }
}
