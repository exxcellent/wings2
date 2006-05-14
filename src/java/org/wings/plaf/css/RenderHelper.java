// (c) copyright 2006 by eXXcellent solutions, Ulm. Author: bschmid

package org.wings.plaf.css;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.SComponent;
import org.wings.SPopupMenu;
import org.wings.SMenuBar;
import org.wings.SMenuItem;
import org.wings.io.StringBuilderDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.IOException;

/**
 * A tiny helper class whic collects information gained during rendering process and
 * needed later on during render. Cleared by <code>FrameCG</code> at start and end
 * of rendering.
 */
public final class RenderHelper {
    /**
     * Apache jakarta commons logger
     */
    private static final Log log = LogFactory.getLog(RenderHelper.class);
    private final List menus = new ArrayList();
    private final StringBuilderDevice menueRenderBuffer = new StringBuilderDevice();
    private boolean caching = true;

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
            log.error("IO Exception during writing into StringBuffer?!?", e);
        }
    }

    public void allowCaching() {
        caching = true;
        log.debug("allow caching");
    }

    public void forbidCaching() {
        caching = false;
        log.debug("forbid caching");
    }

    public boolean isCachingAllowed() {
        return caching;
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
