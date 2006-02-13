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


import org.wings.*;
import org.wings.event.SInternalFrameEvent;
import org.wings.io.Device;
import org.wings.plaf.CGManager;
import org.wings.session.SessionManager;

import java.io.IOException;

public class DialogCG extends FormCG implements org.wings.plaf.DialogCG {
    protected static final String WINDOWICON_CLASSNAME = "WindowIcon";
    protected static final String BUTTONICON_CLASSNAME = "WindowButton";

    private SIcon closeIcon;

    /**
     * Initialize properties from config
     */
    public DialogCG() {
        final CGManager manager = SessionManager.getSession().getCGManager();
        setCloseIcon((SIcon) manager.getObject("DialogCG.closeIcon", SIcon.class));
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
        device.print(" alt=\"");
        device.print(icon.getIconTitle());
        device.print("\"/>");
    }

    // TODO: writeButtonStart
    protected void writeWindowIcon(Device device, SDialog frame,
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
        final SDialog component = (SDialog) _c;
        SDialog frame = component;
        writeWindowBar(device, frame);

        device.print("<div class=\"WindowContent\">");
        super.writeContent(device, _c);
        device.print("</div>");
    }


    protected void writeWindowBar(final Device device, SDialog frame) throws IOException {
        String text = frame.getTitle();
        if (text == null)
            text = "wingS";
        device.print("<div class=\"WindowBar\">");

        // frame is rendered in desktopPane
        // these following icons will be floated to the right by the style sheet...
        if (frame.isClosable() && closeIcon != null) {
            writeWindowIcon(device, frame,
                    SInternalFrameEvent.INTERNAL_FRAME_CLOSED, closeIcon, BUTTONICON_CLASSNAME);
        }
        device.print("<div class=\"WindowBar_title\">");
        // float right end
        if (frame.getIcon() != null) {
            writeIcon(device, frame.getIcon(), WINDOWICON_CLASSNAME);
        }
        device.print(text);
        device.print("</div>");

        device.print("</div>");
    }

    public SIcon getCloseIcon() {
        return closeIcon;
    }

    public void setCloseIcon(SIcon closeIcon) {
        this.closeIcon = closeIcon;
    }
}
