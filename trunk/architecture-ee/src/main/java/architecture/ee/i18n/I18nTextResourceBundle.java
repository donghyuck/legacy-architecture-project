package architecture.ee.i18n;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.Set;

/**
 * @author   donghyuck
 */
public class I18nTextResourceBundle extends ListResourceBundle {

    /**
	 * @uml.property  name="contents"
	 */
    private Object contents[][];
    /**
	 * @uml.property  name="locale"
	 */
    private Locale locale;
    
    public final Set<String> keySet;
    
    
    public I18nTextResourceBundle(List<String[]> keyValues, Locale locale)
    {
        this.locale = super.getLocale();
        this.contents = new Object[keyValues.size()][2];
        Set<String> tempKeySet = new HashSet<String>();
        int index = 0;        
        for( String[] keyValue : keyValues ){
            this.contents[index][0] = keyValue[0];
            tempKeySet.add(keyValue[0]);
            this.contents[index][1] = keyValue[1];
            index++;
        }
        this.keySet = Collections.unmodifiableSet(tempKeySet);
    }
    
    public I18nTextResourceBundle(List<String[]> keyValues)
    {
        this(keyValues, null);
    }

    /**
	 * @return
	 * @uml.property  name="contents"
	 */
    public Object[][] getContents()
    {
        return contents;
    }

    /**
	 * @return
	 * @uml.property  name="locale"
	 */
    public Locale getLocale() {
        return locale;
    }
    
}
