/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
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
import org.wings.SComponent;
import org.wings.SIcon;
import org.wings.SInternalFrame;
import org.wings.util.SStringBuilder;
import org.wings.event.SInternalFrameEvent;
import org.wings.io.Device;
import org.wings.resource.ResourceManager;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class InternalFrameCG extends AbstractComponentCG implements
        org.wings.plaf.InternalFrameCG {
    private static final long serialVersionUID = 1L;
    private final static Log log = LogFactory.getLog(InternalFrameCG.class);
    protected static final String WINDOWICON_CLASSNAME = "WindowIcon";
    protected static final String BUTTONICON_CLASSNAME = "WindowButton";
    private SIcon closeIcon;
    private SIcon deiconifyIcon;
    private SIcon iconifyIcon;
    private SIcon maximizeIcon;
    private SIcon unmaximizeIcon;

    /**
     * Initialize properties from config
     */
    public InternalFrameCG() {
        setCloseIcon((SIcon) ResourceManager.getObject("InternalFrameCG.closeIcon", SIcon.class));
        setDeiconifyIcon((SIcon) ResourceManager.getObject("InternalFrameCG.deiconifyIcon", SIcon.class));
        setIconifyIcon((SIcon) ResourceManager.getObject("InternalFrameCG.iconifyIcon", SIcon.class));
        setMaximizeIcon((SIcon) ResourceManager.getObject("InternalFrameCG.maximizeIcon", SIcon.class));
        setUnmaximizeIcon((SIcon) ResourceManager.getObject("InternalFrameCG.unmaximizeIcon", SIcon.class));
    }

    protected void writeIcon(Device device, SIcon icon, String cssClass) throws IOException {
        device.print("<img");
        if (cssClass != null) {
            device.print(" class=\"");
            device.print(cssClass);
            device.print("\"");
        }
        Utils.optAttribute(device, "src", icon.getURL());
        Utils.optAttribute(device, "width", icon.getIconWidth());
        Utils.optAttribute(device, "height", icon.getIconHeight());
        Utils.attribute(device, "alt", icon.getIconTitle());
        device.print("/>");
    }

    protected void writeWindowIcon(Device device, SInternalFrame frame,
                                   int event, SIcon icon, String cssClass) throws IOException {
        Utils.printButtonStart(device, frame, Integer.toString(event), true, frame.getShowAsFormComponent(), cssClass);
        device.print(">");
        writeIcon(device, icon, null);
        Utils.printButtonEnd(device, true);
    }

    protected void writeWindowIcon(Device device, SInternalFrame frame,
                                   int event, SIcon icon) throws IOException {
        writeWindowIcon(device, frame, event, icon, null);
    }


    public void writeInternal(final Device device, final SComponent _c)
            throws IOException {

        SInternalFrame frame = (SInternalFrame)_c;

        // Optional attribute to identify the internal frame for
        // SDialog and SOptionPane usage.
        Map optionalAttributes = new HashMap();
        optionalAttributes.put("SComponentClass", "org.wings.SInternalFrame");

        writeDivPrefix(device, frame, optionalAttributes);
        writeWindowBar(device, frame);
        // write the actual content
        if (!frame.isIconified()) {
            device.print("<div class=\"WindowContent\"");
            SStringBuilder contentArea = Utils.inlineStyles(frame.getDynamicStyle(SInternalFrame.SELECTOR_CONTENT));
            Utils.optAttribute(device, "style", contentArea);
            device.print(">");

            Utils.renderContainer(device, frame);
            device.print("</div>");
        }
        writeDivSuffix(device, frame);
    }

    /**
     * Convenience method to keep differences between default and msie
     * implementations small
     * @param device
     * @param frame
     * @throws IOException
     */
    protected void writeWindowBar(final Device device, SInternalFrame frame) throws IOException {
        String text = frame.getTitle();
        if (text == null)
            text = "wingS";

        device.print("<div class=\"WindowBar\" id=\"");
        device.print(frame.getName());
        device.print("_titlebar\"");

        SStringBuilder titleArea = Utils.inlineStyles(frame.getDynamicStyle(SInternalFrame.SELECTOR_TITLE));
        Utils.optAttribute(device, "style", titleArea);
        device.print(">");

        if (frame.isClosable() && closeIcon != null) {
            writeWindowIcon(device, frame,
                            SInternalFrameEvent.INTERNAL_FRAME_CLOSED, closeIcon, BUTTONICON_CLASSNAME);
        }
        if (frame.isMaximized() && unmaximizeIcon != null) {
            writeWindowIcon(device, frame,
                            SInternalFrameEvent.INTERNAL_FRAME_UNMAXIMIZED, unmaximizeIcon, BUTTONICON_CLASSNAME);
        }
        if (frame.isMaximizable() && !frame.isMaximized() && maximizeIcon != null) {
            writeWindowIcon(device, frame,
                            SInternalFrameEvent.INTERNAL_FRAME_MAXIMIZED, maximizeIcon, BUTTONICON_CLASSNAME);
        }
        if (frame.isIconified() && deiconifyIcon != null) {
            writeWindowIcon(device, frame,
                            SInternalFrameEvent.INTERNAL_FRAME_DEICONIFIED, deiconifyIcon, BUTTONICON_CLASSNAME);
        }
        if (frame.isIconifyable() && !frame.isIconified() && iconifyIcon != null) {
            writeWindowIcon(device, frame,
                            SInternalFrameEvent.INTERNAL_FRAME_ICONIFIED, iconifyIcon, BUTTONICON_CLASSNAME);
        }

        device.print("<div class=\"WindowBar_title\">"); // float right end

        if (frame.getIcon() != null) {
            writeIcon(device, frame.getIcon(), WINDOWICON_CLASSNAME);
        }
        device.print(text);
        device.print("</div>");

        device.print("</div>");
    }

    protected String getDragHandle(SComponent component) {
        return component.getName() + "_titlebar";
    }
    
    public SIcon getCloseIcon() {
        return closeIcon;
    }

    public void setCloseIcon(SIcon closeIcon) {
        this.closeIcon = closeIcon;
    }

    public SIcon getDeiconifyIcon() {
        return deiconifyIcon;
    }

    public void setDeiconifyIcon(SIcon deiconifyIcon) {
        this.deiconifyIcon = deiconifyIcon;
    }

    public SIcon getIconifyIcon() {
        return iconifyIcon;
    }

    public void setIconifyIcon(SIcon iconifyIcon) {
        this.iconifyIcon = iconifyIcon;
    }

    public SIcon getMaximizeIcon() {
        return maximizeIcon;
    }

    public void setMaximizeIcon(SIcon maximizeIcon) {
        this.maximizeIcon = maximizeIcon;
    }

    public SIcon getUnmaximizeIcon() {
        return unmaximizeIcon;
    }

    public void setUnmaximizeIcon(SIcon unmaximizeIcon) {
        this.unmaximizeIcon = unmaximizeIcon;
    }
}
