/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.plaf;

import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDimension;
import org.wings.SPopupMenu;
import org.wings.border.SBorder;
import org.wings.border.STitledBorder;
import org.wings.io.Device;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Partial CG implementation that is common to all ComponentCGs.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public abstract class AbstractComponentCG
    implements ComponentCG, SConstants, Serializable {
    protected final static Logger logger = Logger.getLogger("org.wings.plaf");

    protected AbstractComponentCG() {
    }

    /**
     * Install the appropriate CG for <code>component</code>.
     *
     * @param component the component
     */
    public void installCG(SComponent component) {
        String className = component.getClass().getName();
        className = className.substring(className.lastIndexOf(".") + 1);

        // default style class name
        String style = component.getClass().getName();
        style = style.substring(style.lastIndexOf('.') + 1);

        CGManager manager = component.getSession().getCGManager();
        String value = (String)manager.getObject("style" + ".style", String.class);
        if (value == null)
            value = style;
        component.setStyle(value);
    }

    /**
     * Uninstall the CG from <code>component</code>.
     *
     * @param component the component
     */
    public void uninstallCG(SComponent component) {
    }

    public final void write(Device device, SComponent component) throws IOException {
        if (!component.isVisible())
            return;
        writePrefix(device, component);
        writeContent(device, component);
        writePostfix(device, component);
    }

    public String mapSelector(String selector) {
        return selector;
    }

    protected void writePrefix(Device device, SComponent component) throws IOException {
        device
            .print("<div id=\"")
            .print(component.getComponentId())
            .print("\" class=\"")
            .print(component.getStyle());

        final SDimension dim = component.getPreferredSize();
        if (dim != null)
            device
                .print("\" style=\"width:")
                .print(dim.width)
                .print("; height:")
                .print(dim.height);

        String toolTip = component.getToolTipText();
        if (toolTip != null)
            device.print("\" onmouseover=\"return makeTrue(domTT_activate(this, event, 'content', '")
                .print(toolTip)
                .print("', 'predefined', 'default'));");


        SPopupMenu menu = component.getComponentPopupMenu();
        if (menu != null) {
            String componentId = menu.getComponentId();
            String popupId = componentId + "_pop";
            String hookId = component.getComponentId();

            device.print("\" onclick=\"Menu.prototype.toggle(null,'");
            org.wings.plaf.Utils.write(device, hookId);
            device.print("','");
            org.wings.plaf.Utils.write(device, popupId);
            device.print("')");
        }

        device
            .print("\">\n");

        final SBorder border = component.getBorder();
        if (border instanceof STitledBorder) {
            STitledBorder titledBorder = (STitledBorder)border;

            device.print("<div class=\"legend\" style=\"");
            titledBorder.getTitleAttributes().write(device);
            device.print("\">");
            device.print(titledBorder.getTitle());
            device.print("</div>");
        }

        component.fireRenderEvent(SComponent.START_RENDERING);
    }

    protected void writeContent(Device device, SComponent component) throws IOException {
    }

    protected void writePostfix(Device device, SComponent component) throws IOException {
        component.fireRenderEvent(SComponent.DONE_RENDERING);

        boolean backup = component.getInheritsPopupMenu();
        component.setInheritsPopupMenu(false);

        if (component.getComponentPopupMenu() != null)
            component.getComponentPopupMenu().write(device);

        component.setInheritsPopupMenu(backup);

        device
            .print("</div><!-- ")
            .print(component.getComponentId())
            .print("-->\n");
    }
}
