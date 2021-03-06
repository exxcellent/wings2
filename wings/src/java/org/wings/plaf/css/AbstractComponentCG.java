/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.plaf.css;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.LowLevelEventListener;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDimension;
import org.wings.SIcon;
import org.wings.SPopupMenu;
import org.wings.SResourceIcon;
import org.wings.SAbstractIconTextCompound;
import org.wings.border.*;
import org.wings.dnd.DragSource;
import org.wings.io.Device;
import org.wings.io.StringBuilderDevice;
import org.wings.plaf.CGManager;
import org.wings.plaf.ComponentCG;
import org.wings.plaf.css.dwr.CallableManager;
import org.wings.script.ScriptListener;
import org.wings.session.BrowserType;
import org.wings.style.Style;
import org.wings.style.CSSAttributeSet;
import org.wings.style.CSSStyle;
import org.wings.util.SStringBuilder;

import javax.swing.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Partial CG implementation that is common to all ComponentCGs.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 */
public abstract class AbstractComponentCG implements ComponentCG, SConstants, Serializable {
    private static final Log log = LogFactory.getLog(AbstractComponentCG.class);
    /**
     * An invisible icon / graphic (spacer graphic)
     */
    private static SIcon BLIND_ICON;

    protected AbstractComponentCG() {
    }

    protected void writeTablePrefix(Device device, SComponent component) throws IOException {
        writeTablePrefix(device, component, null);
    }

    protected void writeTablePrefix(Device device, SComponent component, Map optionalAttributes) throws IOException {
        writePrefix(device, component, true, optionalAttributes);
    }

    protected void writeTableSuffix(Device device, SComponent component) throws IOException {
        writeSuffix(device, component, true);
    }

    protected void writeDivPrefix(Device device, SComponent component) throws IOException {
        writeDivPrefix(device, component, null);
    }

    protected void writeDivPrefix(Device device, SComponent component, Map optionalAttributes) throws IOException {
        writePrefix(device, component, false, optionalAttributes);
    }

    protected void writeDivSuffix(Device device, SComponent component) throws IOException {
        writeSuffix(device, component, false);
    }

    private void writePrefix(final Device device, final SComponent component, final boolean useTable, Map optionalAttributes) throws IOException {
        // This is the containing element of a component
        // it is responsible for styles, sizing...
        if (useTable) {
            device.print("<table"); // table
        }
        else {
            device.print("<div"); // div
        }

        // we cant render this here.
        //Utils.writeEvents(device, component, null);

        writeAllAttributes(device, component);

        // Render the optional attributes.
        Utils.optAttributes(device, optionalAttributes);

        if (useTable) {
            device.print("><tr><td"); // table
            final SStringBuilder styleString = new SStringBuilder();
            if (component.getBorder() != null) {
                Utils.createInlineStylesForInsets(styleString, component.getBorder().getInsets());
            }
            // das hier für height 100% der inneren Komponenten, funzt im Firefox, nicht im IE
            //styleString.append("width:100%;height:100%;vertical-align:top");
            Utils.optAttribute(device, "style", styleString);

            device.print(">"); // table
        }
        else {
            device.print(">"); // div
        }
    }

    private void writeSuffix(Device device, SComponent component, boolean useTable) throws IOException {
        if (useTable) {
            device.print("</td></tr></table>");
        }
        else {
            device.print("</div>");
        }
    }

    public static void writeAllAttributes(Device device, SComponent component) throws IOException {
        Utils.optAttribute(device, "class", component.getStyle());
        Utils.optAttribute(device, "id", component.getName());

        Utils.optAttribute(device, "style", getInlineStyles(component));

        if (component instanceof LowLevelEventListener) {
            Utils.optAttribute(device, "eid", component.getEncodedLowLevelEventId());
        }

        // Tooltip handling
        writeTooltipMouseOver(device, component);

        // Component popup menu
        writeContextMenu(device, component);
    }

    private static String getInlineStyles(SComponent component) {
        // write inline styles
        final SStringBuilder builder = new SStringBuilder();

        if (component instanceof DragSource)
            builder.append("position:relative;");

        Utils.appendCSSInlineSize(builder, component);  

        // Determine Style String
        Style allStyle = component.getDynamicStyle(SComponent.SELECTOR_ALL);
        if (component instanceof SAbstractIconTextCompound && ((SAbstractIconTextCompound)component).isSelected()) {
            // present, SComponent.getDynamicStyle() always is instance of CSSStyle
            CSSStyle selectedStyle = (CSSStyle)component.getDynamicStyle(SAbstractIconTextCompound.SELECTOR_SELECTED);
            if (selectedStyle != null) {
              if (allStyle != null) {
                  // make a copy to modify
                  allStyle = new CSSStyle(SComponent.SELECTOR_ALL, (CSSAttributeSet) allStyle);
                  allStyle.putAll(selectedStyle);
              } else {
                  allStyle = selectedStyle;
              }
            }
        }
        // Render Style string
        if (allStyle != null)
            builder.append(allStyle.toString());

        final SBorder border = component.getBorder();
        if (border != null) {
            if (border.getAttributes() != null)
                builder.append(border.getAttributes().toString());
        }
        else
            builder.append("border:none;padding:0px");

        return builder.toString();
    }

    protected void writeInlineScripts(Device device, SComponent component) throws IOException {
        boolean scriptTagOpen = false;
        for (int i = 0; i < component.getScriptListeners().length; i++) {
            ScriptListener scriptListener = component.getScriptListeners()[i];
            String script = scriptListener.getScript();
            if (script != null) {
                if (!scriptTagOpen) {
                    device.print("<script type=\"text/javascript\">");
                    scriptTagOpen = true;
                }
                device.print(script);
            }
        }
        if (scriptTagOpen)
            device.print("</script>");
    }

    /**
     * Write JS code for context menus. Common implementaton for MSIE and gecko.
     */
    protected static void writeContextMenu(Device device, SComponent component) throws IOException {
        final SPopupMenu menu = component.getComponentPopupMenu();
        if (menu != null) {
            final String componentId = menu.getName();
            final String popupId = componentId + "_pop";
            device.print(" onContextMenu=\"return wpm_menuPopup(event, '");
            device.print(popupId);
            device.print("');\" onMouseDown=\"return wpm_menuPopup(event, '");
            device.print(popupId);
            device.print("');\"");
        }
    }

    /**
     * Write DomTT Tooltip code. Common handler for MSIE and Gecko PLAF.
     */
    protected static void writeTooltipMouseOver(Device device, SComponent component) throws IOException {
        final String toolTipText = component != null ? component.getToolTipText() : null;
        if (toolTipText != null && toolTipText.length() > 0) {
            device.print(" onmouseover=\"return wu_toolTip(event, this)\"");
            Utils.optAttribute(device, "tip", toolTipText);
        }
    }

    /**
     * Install the appropriate CG for <code>component</code>.
     *
     * @param component the component
     */
    public void installCG(SComponent component) {
        Class clazz = component.getClass();
        while (clazz.getPackage() == null || !("org.wings".equals(clazz.getPackage().getName()) || "org.wingx".equals(clazz.getPackage().getName())))
            clazz = clazz.getSuperclass();
        String style = clazz.getName();
        style = style.substring(style.lastIndexOf('.') + 1);
        component.setStyle(style); // set default style name to component class (ie. SLabel).
        component.setBorder(SDefaultBorder.INSTANCE);

        if (Utils.isMSIE(component)) {
            final CGManager manager = component.getSession().getCGManager();
            Object value;
            int verticalOversize = 0;
            value = manager.getObject(style + ".verticalOversize", Integer.class);
            if (value != null)
                verticalOversize = ((Integer) value).intValue();
            int horizontalOversize = 0;
            value = manager.getObject(style + ".horizontalOversize", Integer.class);
            if (value != null)
                horizontalOversize = ((Integer) value).intValue();

            if (verticalOversize != 0 || horizontalOversize != 0)
                component.putClientProperty("oversize", new SEmptyBorder(verticalOversize, horizontalOversize, verticalOversize, horizontalOversize));
        }
    }

    /**
     * Uninstall the CG from <code>component</code>.
     *
     * @param component the component
     */
    public void uninstallCG(SComponent component) {
    }

    protected final SIcon getBlindIcon() {
        if (BLIND_ICON == null)
            BLIND_ICON = new SResourceIcon("org/wings/icons/blind.gif");
        return BLIND_ICON;
    }

    public void componentChanged(SComponent component) {
        component.putClientProperty("render-cache", null);
        //log.debug("invalidating = " + component.getName());

        InputMap inputMap = component.getInputMap();
        if (inputMap != null && inputMap.size() > 0) {
            if (!(inputMap instanceof VersionedInputMap)) {
                inputMap = new VersionedInputMap(inputMap);
                component.setInputMap(inputMap);
            }

            final VersionedInputMap versionedInputMap = (VersionedInputMap) inputMap;
            final Integer inputMapVersion = (Integer) component.getClientProperty("inputMapVersion");
            if (inputMapVersion == null || versionedInputMap.getVersion() != inputMapVersion.intValue()) {
                InputMapScriptListener.install(component);
                component.putClientProperty("inputMapVersion", new Integer(versionedInputMap.getVersion()));
            }
        }

        // Add script listener support.
        List scriptListenerList = component.getScriptListenerList();
        if (scriptListenerList != null && scriptListenerList.size() > 0) {
            // BSC START ----------------: Bad code : behaviour injection !!!
            /*

            HINT: Just try a
               - oldList = xx;
               - if (!currerntList.equals(oldList) !!!!!!!!!

            if (!(scriptListenerList instanceof VersionedList)) {
                scriptListenerList = new VersionedList(scriptListenerList);
                component.setScriptListenerList(scriptListenerList);
            }


            final VersionedList versionedList = (VersionedList) scriptListenerList;
            final Integer scriptListenerListVersion = (Integer) component.getClientProperty("scriptListenerListVersion");
            if (scriptListenerListVersion == null || versionedList.getVersion() != scriptListenerListVersion.intValue())
            */if (true) // BSC ------------ END
        {
            /* TODO: this code destroys the dwr functionality
            List removeCallables = new ArrayList();
            // Remove all existing - and maybe unusable - DWR script listeners.
            for (Iterator iter = CallableManager.getInstance().callableNames().iterator(); iter.hasNext();) {
                Object o = iter.next();
                if (o instanceof String) {
                    removeCallables.add(o);
                }
            }

            for (Iterator iter = removeCallables.iterator(); iter.hasNext(); ) {
                Object o = iter.next();
                if (o instanceof String) {
                    CallableManager.getInstance().unregisterCallable((String) o);
                }
            }
            */

            // Add DWR script listener support.
            ScriptListener[] scriptListeners = component.getScriptListeners();
            for (int i = 0; i < scriptListeners.length; i++) {
                if (scriptListeners[i] instanceof DWRScriptListener) {
                    DWRScriptListener scriptListener = (DWRScriptListener) scriptListeners[i];
                    CallableManager.getInstance().registerCallable(scriptListener.getCallableName(), scriptListener.getCallable());
                }
            }

            //component.putClientProperty("scriptListenerListVersion", new Integer(versionedList.getVersion()));
        }
        }
    }

    protected final boolean hasDimension(final SComponent component) {
        SDimension dim = component.getPreferredSize();
        return dim != null && (dim.getHeightInt() != SDimension.AUTO_INT || dim.getWidthInt() != SDimension.AUTO_INT);
    }

    /**
     * Caching code disabled for all LowLevelEventListeners, because event epochs won't change.
     */
    public final void write(final Device device, final SComponent component) throws IOException {
        if (!RenderHelper.getInstance(component).isCachingAllowed(component)) {
            // log.debug("Rendering = " + component.getName());
            renderAndWrapComponent(device, component);
            return;
        }
        String renderedCode = (String) component.getClientProperty("render-cache");
        if (renderedCode == null) {
            try {
                // TODO -- reuse and keep the string builder in the cache. Just check if empty.
                StringBuilderDevice cacheDevice = new StringBuilderDevice();
                renderAndWrapComponent(cacheDevice, component);
                renderedCode = cacheDevice.toString();
                component.putClientProperty("render-cache", renderedCode);
                log.debug("caching = " + component.getName());
            }
            catch (RuntimeException e) {
                log.fatal("Runtime exception during rendering", e);
                device.print("<blink>" + e.getClass().getName() + " during code generation of " + component.getName() + "(" + component.getClass().getName() + ")</blink>\n");
            }
        }
        else {
            log.debug("using cached code for = " + component.getName());
        }

        device.print(renderedCode);
    }

    private void renderAndWrapComponent(Device device, SComponent component) throws IOException {
        Utils.printDebug(device, "<!-- ").print(component.getName()).print(" -->");
        component.fireRenderEvent(SComponent.START_RENDERING);

        BorderCG.writeComponentBorderPrefix(device, component);

        writeInternal(device, component);

        writeInlineScripts(device, component);

        RenderHelper.getInstance(component).collectMenues(component);

        BorderCG.writeComponentBorderSufix(device, component);

        component.fireRenderEvent(SComponent.DONE_RENDERING);
        Utils.printDebug(device, "<!-- /").print(component.getName()).print(" -->");
    }

    public abstract void writeInternal(Device device, SComponent component) throws IOException;

    /**
     * @return true if current browser is microsoft exploder
     */
    protected final boolean isMSIE(final SComponent component) {
        return component.getSession().getUserAgent().getBrowserType() == BrowserType.IE;
    }
}
