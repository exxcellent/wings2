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
package org.wings.plaf.css.msie;

import java.io.IOException;

import org.wings.SInternalFrame;
import org.wings.event.SInternalFrameEvent;
import org.wings.io.Device;
import org.wings.plaf.css.Utils;

public class InternalFrameCG extends org.wings.plaf.css.InternalFrameCG {

    /* (non-Javadoc)
     * @see org.wings.plaf.css.InternalFrameCG#writeWindowBar(org.wings.io.Device, org.wings.SInternalFrame)
     */
    protected void writeWindowBar(Device device, SInternalFrame frame) throws IOException {
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

}
