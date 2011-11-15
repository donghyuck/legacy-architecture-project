package architecture.ee.web.ui.navigator.taglib.el;


import javax.servlet.jsp.JspException;

import architecture.ee.web.ui.navigator.menu.MenuComponent;

import java.net.MalformedURLException;


/**
 * This tag acts the same as net.sf.navigator.taglib.DisplayMenuTag, except
 * that it allows JSTL Expressions in it's name and target attributes.
 *
 * @author Matt Raible
 * @version $Revision: 1.6 $ $Date: 2006/07/09 08:08:10 $
 */
public class DisplayMenuTag extends architecture.ee.web.ui.navigator.taglib.DisplayMenuTag {
    private String name;
    private String target;

    public void setName(String name) {
        this.name = name;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public DisplayMenuTag() {
        super();
        init();
    }

    private void init() {
        name = null;
        target = null;
    }

    public void release() {
        super.release();
        init();
    }

    public int doStartTag() throws JspException {
        evaluateExpressions();
        return super.doStartTag();
    }

    /**
     * Overrides the setPageLocation in parentTag to use JSTL to evaluate
     * the URL.  It's definitely ugly, so if you have a cleaner way, please
     * let me know!
     *
     * @param menu
     */
    protected void setPageLocation(MenuComponent menu)
    throws MalformedURLException, JspException {
        setLocation(menu);
        String url = menu.getLocation();

        if (url != null) {
            ExpressionEvaluator eval = new ExpressionEvaluator(this, pageContext);
            menu.setUrl(eval.evalString("url", url));
        }

        // do all contained menus
        MenuComponent[] subMenus = menu.getMenuComponents();

        if (subMenus.length > 0) {
            for (int i = 0; i < subMenus.length; i++) {
                this.setPageLocation(subMenus[i]);
            }
        }
    }

    private void evaluateExpressions() throws JspException {
        ExpressionEvaluator eval = new ExpressionEvaluator(this, pageContext);

        if (name != null) {
            super.setName(eval.evalString("name", name));
        }

        if (target != null) {
            super.setTarget(eval.evalString("target", target));
        }
    }
}
