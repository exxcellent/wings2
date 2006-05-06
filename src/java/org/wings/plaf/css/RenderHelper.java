// (c) copyright 2006 by eXXcellent solutions, Ulm. Author: bschmid

package org.wings.plaf.css;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.SComponent;
import org.wings.SPopupMenu;
import org.wings.io.StringBuilderDevice;

import java.util.ArrayList;
import java.util.List;
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
            if (componentMenu != null && menus.contains(componentMenu) == false) {
                try {
                    menus.add(componentMenu);
                    componentMenu.putClientProperty("popup", Boolean.TRUE);
                    componentMenu.write(menueRenderBuffer);
                } catch (IOException e) {
                    log.error("IO Exception during writing into StringBuffer?!?", e);
                }
            }
        }
    }
}
