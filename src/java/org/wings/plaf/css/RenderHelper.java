// (c) copyright 2006 by eXXcellent solutions, Ulm. Author: bschmid

package org.wings.plaf.css;

import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.SMenu;
import org.wings.resource.ResourceManager;

/**
 * A tiny helper class which collects information gained during rendering process and
 * needed later on during render. Cleared by <code>FrameCG</code> at start and end
 * of rendering.
 */
public final class RenderHelper {

    private static final boolean ALLOW_COMPONENT_CACHING =  // modify in resource.properties
            ((Boolean) ResourceManager.getObject("SComponent.renderCache", Boolean.class)).booleanValue();

    private int allowUsageOfCachedInstances = 0;

    public static RenderHelper getInstance(SComponent forComponent) {
        RenderHelper renderHelper =
        	(RenderHelper) forComponent.getSession().getProperty("css_plaf-render-helper");
        if (renderHelper == null) {
            renderHelper = new RenderHelper();
            forComponent.getSession().setProperty("css_plaf-render-helper", renderHelper);
        }
        return renderHelper;
    }

    public void reset() {
        allowUsageOfCachedInstances = 0;
    }

    public void allowCaching() {
        this.allowUsageOfCachedInstances --;
    }
    public void forbidCaching() {
        this.allowUsageOfCachedInstances ++;
    }

    public boolean isCachingAllowed(final SComponent component) {
        return ALLOW_COMPONENT_CACHING && allowUsageOfCachedInstances == 0 &&
            !(component.getSession().getReloadManager().isUpdateMode()) &&
            !(component instanceof SContainer || component instanceof SMenu) &&
            component.getComponentPopupMenu() == null &&
            component.getScriptListenerList().isEmpty();
    }

}
