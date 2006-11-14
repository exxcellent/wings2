// (c) copyright 2006 by eXXcellent solutions, Ulm. Author: bschmid

package org.wings.plaf.css;

import org.wings.LowLevelEventListener;
import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.SMenuBar;
import org.wings.SMenuItem;
import org.wings.SPopupMenu;
import org.wings.io.StringBuilderDevice;
import org.wings.resource.ResourceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A tiny helper class whic collects information gained during rendering process and
 * needed later on during render. Cleared by <code>FrameCG</code> at start and end
 * of rendering.
 */
public final class RenderHelper {
    private static final boolean ALLOW_COMPONENT_CACHING =  // modify in resource.properties
            ((Boolean) ResourceManager.getObject("SComponent.renderCache", Boolean.class)).booleanValue();

    private final List menus = new ArrayList();
    private final StringBuilderDevice menueRenderBuffer = new StringBuilderDevice();
    private boolean allowUsageOfCachedInstances = true;

    public void reset() {
        menus.clear();
        menueRenderBuffer.reset();
    }

    public List getCollectedMenues() {
        return menus;
    }

    public StringBuilderDevice getMenueRenderBuffer() {
        return menueRenderBuffer;
    }

    public void collectMenues(final SComponent component) {
        if (component.isVisible()) {
            final SPopupMenu componentMenu = component.getComponentPopupMenu();
            if (componentMenu != null && !menus.contains(componentMenu)) {
                addMenu(componentMenu);
            } else if (component instanceof SMenuBar) {
                SMenuBar menuBar = (SMenuBar) component;
                for (Iterator iterator = menuBar.getMenus().iterator(); iterator.hasNext();) {
                    SMenuItem menuItem = (SMenuItem) iterator.next();
                    addMenu(menuItem);
                }
            }
        }
    }

    private void addMenu(SComponent menuItem) {
        try {
            menus.add(menuItem);
            menuItem.putClientProperty("popup", Boolean.TRUE);
            menuItem.write(menueRenderBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e); // IMHO can not happen !
        }
    }

    public void allowCaching() {
        this.allowUsageOfCachedInstances = true;
    }
    public void forbidCaching() {
        this.allowUsageOfCachedInstances = false;
    }

    public boolean isCachingAllowed(final SComponent component) {
        return ALLOW_COMPONENT_CACHING && allowUsageOfCachedInstances &&
                !(component instanceof LowLevelEventListener || component instanceof SContainer);
    }

    public static RenderHelper getInstance(SComponent forComponent) {
        RenderHelper renderHelper = (RenderHelper) forComponent.getSession().getProperty("css_plaf-render-helper");
        if (renderHelper == null) {
            renderHelper = new RenderHelper();
            forComponent.getSession().setProperty("css_plaf-render-helper", renderHelper);
        }
        return renderHelper;
    }
}
