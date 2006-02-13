/**
 * 
 */
package org.wings.plaf.css.msie;

import java.io.IOException;

import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SFrame;
import org.wings.SPageScroller;
import org.wings.event.SParentFrameEvent;
import org.wings.event.SParentFrameListener;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Script;
import org.wings.io.Device;
import org.wings.plaf.css.Utils;
import org.wings.resource.ClasspathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.session.SessionManager;

/**
 * @author ole
 *
 */
public class PageScrollerCG extends org.wings.plaf.css.PageScrollerCG implements SParentFrameListener {
    private static final String FORMS_JS = (String) SessionManager
    .getSession().getCGManager().getObject("JScripts.form",
            String.class);


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

    protected void writeButtonStart(Device device, SComponent component, String value) throws IOException {
        device.print("<button class=\"borderless\" onclick=\"sendEvent(event,'");
        device.print(value);
        device.print("','");
        device.print(Utils.event(component));
        device.print("')\"");
     }
}
