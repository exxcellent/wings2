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


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.*;
import org.wings.event.SInternalFrameEvent;
import org.wings.io.Device;
import org.wings.plaf.CGManager;
import org.wings.session.SessionManager;

import java.io.IOException;

public class InternalFrameCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.InternalFrameCG
{
    private final static transient Log log = LogFactory.getLog(InternalFrameCG.class);
    private static final String WINDOWICON_CLASSNAME = "WindowIcon";
    private static final String BUTTONICON_CLASSNAME = "WindowButton";
    private SIcon closeIcon;
    private SIcon deiconifyIcon;
    private SIcon iconifyIcon;
    private SIcon maximizeIcon;
    private SIcon unmaximizeIcon;

    /**
     * Initialize properties from config
     */
    public InternalFrameCG() {
        final CGManager manager = SessionManager.getSession().getCGManager();

        setCloseIcon((SIcon) manager.getObject("InternalFrameCG.closeIcon", SIcon.class));
        setDeiconifyIcon((SIcon) manager.getObject("InternalFrameCG.deiconifyIcon", SIcon.class));
        setIconifyIcon((SIcon) manager.getObject("InternalFrameCG.iconifyIcon", SIcon.class));
        setMaximizeIcon((SIcon) manager.getObject("InternalFrameCG.maximizeIcon", SIcon.class));
        setUnmaximizeIcon((SIcon) manager.getObject("InternalFrameCG.unmaximizeIcon", SIcon.class));
    }

    public void installCG(SComponent component) {
        super.installCG(component);
    }

    private void writeIcon(Device device, SIcon icon, String cssClass) throws IOException {
        device.print("<img");
        if (cssClass != null) {
            device.print(" class=\"");
            device.print(cssClass);
            device.print("\"");
        }
        device.print(" border=\"0\"");
        Utils.optAttribute(device, "src", icon.getURL());
        Utils.optAttribute(device, "width", icon.getIconWidth());
        Utils.optAttribute(device, "height", icon.getIconHeight());
        device.print("/>");
    }

    private void writeWindowIcon(Device device, SInternalFrame frame,
            int event, SIcon icon, String cssClass) throws IOException {
        boolean showAsFormComponent = frame.getShowAsFormComponent();

        // RequestURL addr = frame.getRequestURL();
        // addr.addParameter(Utils.event(frame), event);

        if (showAsFormComponent) {
            device.print("<button");
            if (cssClass != null) {
                device.print(" class=\"");
                device.print(cssClass);
                device.print("\"");
            }
            device.print(" name=\"").print(Utils.event(frame)).print(
                    "\" value=\"").print(event).print("\">");
        } else {
            device.print("<a");
            if (cssClass != null) {
                device.print(" class=\"");
                device.print(cssClass);
                device.print("\"");
            }
            device.print(" href=\"").print(
                    frame.getRequestURL().addParameter(
                            Utils.event(frame) + "=" + event).toString())
                    .print("\">");
        }
        writeIcon(device, icon, null);

        if (showAsFormComponent) {
            device.print("</button>");
        } else {
            device.print("</a>");
        }
    }


    public void writeContent(final Device device, final SComponent _c)
            throws IOException {
        final SInternalFrame component = (SInternalFrame) _c;

        SInternalFrame frame = component;

        writeWindowBar(device, frame);

        // write the actual content
        if (!frame.isIconified()) {
            device.print("<div class=\"WindowContent\">");
            Utils.renderContainer(device, frame);
            device.print("</div>");
        }
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
        device.print("<div class=\"WindowBar\">");
        if (frame.isIconified()) {
            // frame is rendered in taskbar
            if (frame.getIcon() != null) {
                writeIcon(device, frame.getIcon(), WINDOWICON_CLASSNAME);
            }
            if (deiconifyIcon != null) {
                device.print(text);
                writeWindowIcon(device, frame,
                        SInternalFrameEvent.INTERNAL_FRAME_DEICONIFIED, deiconifyIcon, "DeiconifyButton");
            } else {
                device.print("<a href=\"").print(
                        frame.getRequestURL().addParameter(
                                Utils.event(frame) + "=" + SInternalFrameEvent.INTERNAL_FRAME_DEICONIFIED).toString())
                        .print("\">");
                device.print(text);
                device.print("</a>");
            }
        } else {
            // frame is rendered in desktopPane
            // these following icons will be floated to the right by the style sheet...
            if (frame.isClosable() && closeIcon != null) {
                writeWindowIcon(device, frame,
                        SInternalFrameEvent.INTERNAL_FRAME_CLOSED, closeIcon, BUTTONICON_CLASSNAME);
            }
            if (frame.isIconifyable() && iconifyIcon != null) {
                writeWindowIcon(device, frame,
                        SInternalFrameEvent.INTERNAL_FRAME_ICONIFIED, iconifyIcon, BUTTONICON_CLASSNAME);
            }
            if (frame.isMaximizable() && !frame.isMaximized() && maximizeIcon != null) {
                writeWindowIcon(device, frame,
                        SInternalFrameEvent.INTERNAL_FRAME_MAXIMIZED, maximizeIcon, BUTTONICON_CLASSNAME);
            }
            device.print("<div class=\"WindowBar_title\">");
            // float right end
            if (frame.getIcon() != null) {
                writeIcon(device, frame.getIcon(), WINDOWICON_CLASSNAME);
            }
            device.print(text);
            device.print("</div>");
        }
        device.print("</div>");
    }

    private String getIconWidth(SIcon icon) {
        if (icon.getIconWidth() == -1)
            return "0%";
        else
            return "" + icon.getIconWidth();
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
