// (c) copyright 2006 by eXXcellent solutions, Ulm. Author: bschmid

package org.wings.plaf.css;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.SComponent;
import org.wings.SPopupMenu;
import org.wings.SMenuBar;
import org.wings.SMenuItem;
import org.wings.SContainer;
import org.wings.resource.ResourceManager;
import org.wings.script.ScriptListener;
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
    private final static boolean ALLOW_COMPONENT_CACHING =  // modify in resource.properties
            ((Boolean) ResourceManager.getObject("SComponent.renderCache", Boolean.class)).booleanValue();

    private final List menus = new ArrayList();
    private final List scripts = new ArrayList();
    private final StringBuilderDevice menueRenderBuffer = new StringBuilderDevice();
    // TODO(he): never used
    private int horizontalLayoutPadding = 0;
    private int verticalLayoutPadding = 0;
    private boolean allowUsageOfCachedInstances = true;
    private boolean incrementalUpdateMode = false;

    public void reset() {
        scripts.clear();
        menus.clear();
        menueRenderBuffer.reset();
    }

    public void addScript(String script) {
        scripts.add(script);
    }

    public List getCollectedScripts() {
        return scripts;
    }

    public void collectScripts(SComponent component) {
        ScriptListener[] scriptListeners = component.getScriptListeners();
        String script = null;
        for (int i = 0; i < scriptListeners.length; ++i) {
            ScriptListener scriptListener = scriptListeners[i];
            script = scriptListener.getScript();
            if (script != null) {
                addScript(script);
            }
        }
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
        this.allowUsageOfCachedInstances = true;
    }
    public void forbidCaching() {
        this.allowUsageOfCachedInstances = false;
    }

    public boolean isCachingAllowed(final SComponent component) {
        return ALLOW_COMPONENT_CACHING && allowUsageOfCachedInstances &&
                !(component instanceof SContainer);
    }

    public static RenderHelper getInstance(SComponent forComponent) {
        RenderHelper renderHelper = (RenderHelper) forComponent.getSession().getProperty("css_plaf-render-helper");
        if (renderHelper == null) {
            renderHelper = new RenderHelper();
            forComponent.getSession().setProperty("css_plaf-render-helper", renderHelper);
        }
        return renderHelper;
    }

    public int getHorizontalLayoutPadding() {
        return horizontalLayoutPadding;
    }

    public void setHorizontalLayoutPadding(int horizontalLayoutPadding) {
        this.horizontalLayoutPadding = horizontalLayoutPadding;
    }

    public int getVerticalLayoutPadding() {
        return verticalLayoutPadding;
    }

    public void setVerticalLayoutPadding(int verticalLayoutPadding) {
        this.verticalLayoutPadding = verticalLayoutPadding;
    }

    public boolean isIncrementalUpdateMode() {
        return incrementalUpdateMode;
    }

    public void setIncrementalUpdateMode(boolean enabled) {
        this.incrementalUpdateMode = enabled;
    }
}
