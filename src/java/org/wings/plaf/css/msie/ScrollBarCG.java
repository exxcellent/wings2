/**
 * 
 */
package org.wings.plaf.css.msie;

import org.wings.SComponent;
import org.wings.SFrame;
import org.wings.event.SParentFrameEvent;
import org.wings.event.SParentFrameListener;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Script;
import org.wings.io.Device;
import org.wings.plaf.css.Utils;
import org.wings.resource.ClasspathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.resource.ResourceManager;
import org.wings.session.SessionManager;
import java.io.IOException;

/**
 * @author ole
 *
 */
public class ScrollBarCG extends org.wings.plaf.css.ScrollBarCG implements SParentFrameListener {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String FORMS_JS = (String) ResourceManager.getObject("JScripts.form", String.class);


    public void installCG(SComponent component) {
        super.installCG(component);
        component.addParentFrameListener(this);
    }

    public void parentFrameAdded(SParentFrameEvent e) {
        SFrame parentFrame = e.getParentFrame();
        ClasspathResource res = new ClasspathResource(FORMS_JS, "text/javascript");
        String jScriptUrl = SessionManager.getSession().getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        parentFrame.addHeader(new Script("text/javascript", new DefaultURLResource(jScriptUrl)));
    }

    public void parentFrameRemoved(SParentFrameEvent e) {
    }

    /* (non-Javadoc)
     * @see org.wings.plaf.css.ScrollBarCG#writeButtonStart(org.wings.io.Device, org.wings.SScrollBar, java.lang.String)
     */
    protected void writeButtonStart(Device device, SComponent component, String value) throws IOException {
        device.print("<button class=\"borderless\" onclick=\"sendEvent(event,'");
        device.print(value);
        device.print("','");
        device.print(Utils.event(component));
        device.print("')\"");
    }
}
