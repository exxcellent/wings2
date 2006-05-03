/*
 * $Id$
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

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.LowLevelEventListener;
import org.wings.SComponent;
import org.wings.SDimension;
import org.wings.SPopupMenu;
import org.wings.script.ScriptListener;
import org.wings.style.Style;
import org.wings.plaf.ComponentCG;
import org.wings.border.STitledBorder;
import org.wings.border.SBorder;
import org.wings.dnd.DragSource;
import org.wings.io.Device;

/**
 * @author ole
 */
public class PrefixAndSuffixDecorator
    implements CGDecorator
{
    private static final long serialVersionUID = 1L;
    protected static final transient Log log = LogFactory.getLog(PrefixAndSuffixDecorator.class);
    protected ComponentCG delegate;

    public PrefixAndSuffixDecorator() {
    }

    public PrefixAndSuffixDecorator(ComponentCG delegate) {
        this.delegate = delegate;
    }

    public ComponentCG getDelegate() {
        return delegate;
    }

    public void setDelegate(ComponentCG delegate) {
        this.delegate = delegate;
    }

    public void installCG(SComponent c) {
        delegate.installCG(c);
    }

    public void uninstallCG(SComponent c) {
        delegate.uninstallCG(c);
    }

    public void componentChanged(SComponent c) {
        delegate.componentChanged(c);
    }

    public void write(Device device, SComponent component) throws IOException {
        boolean wantsPrefixAndSuffix = delegate.wantsPrefixAndSuffix(component);
        if (wantsPrefixAndSuffix)
            writePrefix(device, component);

        delegate.write(device, component);

        if (wantsPrefixAndSuffix)
            writeSuffix(device, component);
    }

    public boolean wantsPrefixAndSuffix(SComponent component) {
        return false;
    }

    public void writePrefix(Device device, SComponent component) throws IOException {
        final SDimension prefSize = component.getPreferredSize();
        final StringBuffer cssInlineStyle = new StringBuffer();
        final boolean isTitleBorder = component.getBorder() instanceof STitledBorder;

        Utils.printDebugNewline(device, component);
        Utils.printDebug(device, "<!-- ").print(component.getName()).print(" -->");

        //------------------------ OUTER DIV

        // This is the outer DIV element of a component
        // it is responsible for Postioning (i.e. it take up all free space around to i.e. center
        // the inner div inside this free space
        device.print("<div");
        final String classname = component.getStyle();
        Utils.optAttribute(device, "class", isTitleBorder ? classname + " STitledBorder" : classname);
        Utils.optAttribute(device, "id", component.getName());

        // if sizes are spec'd in percentages, we need the outer box to have full size...skipped for now
//        final boolean isHeightPercentage = prefSize != null && prefSize.getHeightUnit() != null && prefSize.getHeightUnit().indexOf("%") != -1;
//        final boolean isWidthPercentage = prefSize != null && prefSize.getWidthUnit() != null && prefSize.getWidthUnit().indexOf("%") != -1;
//        // special case of special case: if the component with relative size is vertically aligned, we must avoid 100% heigth
//        final boolean isVAligned = (component.getVerticalAlignment() == SConstants.CENTER
//                || component.getVerticalAlignment() == SConstants.BOTTOM);
//        if (isHeightPercentage && isVAligned == false) {
//            cssInlineStyle.append("height:100%;");
//        }
//        if (isWidthPercentage) {
//            cssInlineStyle.append("width:100%;");
//        }

        // Output collected inline CSS style
//        Utils.optAttribute(device, "style", cssInlineStyle);
//        device.print(">"); // div

        //------------------------ INNER DIV

        // This is the inner DIV around each component.
        // It is responsible for component size, and other styles.
//        device.print("<div");
//        Utils.optAttribute(device, "class", isTitleBorder ? component.getStyle() + " STitledBorder" : component.getStyle());         // Special handling: Mark Titled Borders for styling

        writeInlineStyles(device, component);

        if (component instanceof LowLevelEventListener) {
            Utils.optAttribute(device, "eid", ((LowLevelEventListener) component).getEncodedLowLevelEventId());
        }

        // Tooltip handling
        writeTooltipMouseOver(device, component);

        // Component popup menu
        writeContextMenu(device, component);

        device.print(">"); // div

        // Special handling: Render title of STitledBorder
        if (isTitleBorder) {
            STitledBorder titledBorder = (STitledBorder) component.getBorder();
            device.print("<div class=\"STitledBorderLegend\" style=\"");
            titledBorder.getTitleAttributes().write(device);
            device.print("\">");
            device.print(titledBorder.getTitle());
            device.print("</div>");
        }

        component.fireRenderEvent(SComponent.START_RENDERING);
    }

    public void writeSuffix(Device device, SComponent component) throws IOException {
        component.fireRenderEvent(SComponent.DONE_RENDERING);
        writeInlineScripts(device, component);
        device.print("</div>");
        Utils.printDebug(device, "<!-- /").print(component.getName()).print(" -->");
    }

    protected void writeInlineStyles(Device device, SComponent component) throws IOException {
        // write inline styles
        device.print("style=\"");
        if (component instanceof DragSource)
            device.print("position:relative;");
        Utils.appendCSSInlineSize(device, component.getPreferredSize());
        Style allStyle = component.getDynamicStyle(SComponent.SELECTOR_ALL);
        if (allStyle != null)
            allStyle.write(device);
        SBorder border = component.getBorder();
        if (border != null && border.getAttributes() != null)
            border.getAttributes().write(device);
        device.print("\"");
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
        final String toolTipText = component.getToolTipText();
        if (toolTipText != null) {
            device.print(" onmouseover=\"return makeTrue(domTT_activate(this, event, 'content', '");
            // javascript needs even more & special quoting
            // FIXME: do this more efficiently
            Utils.quote(device, toolTipText.replaceAll("\'","\\\\'"), true, true, true);
            device.print("', 'predefined', 'default'));\"");
        }
    }

}
