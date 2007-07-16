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
package org.wings.plaf.css.msie;

import org.wings.SInternalFrame;
import org.wings.event.SInternalFrameEvent;
import org.wings.io.Device;
import org.wings.plaf.css.Utils;

import java.io.IOException;

public final class InternalFrameCG extends org.wings.plaf.css.InternalFrameCG {

    private static final long serialVersionUID = 1L;

    /* (non-Javadoc)
     * @see org.wings.plaf.css.InternalFrameCG#writeWindowBar(org.wings.io.Device, org.wings.SInternalFrame)
     */
    protected void writeWindowBar(final Device device, SInternalFrame frame) throws IOException {
        String text = frame.getTitle();
        if (text == null)
            text = "wingS";

        device.print("<div class=\"WindowBar\" id=\"");
        device.print(frame.getName());
        device.print("_titlebar\">");
        device.print("<table width=\"100%\"><tr><td width=\"100%\"><div class=\"WindowBar_title\">");
        if (frame.getIcon() != null) {
            writeIcon(device, frame.getIcon(), WINDOWICON_CLASSNAME);
        }
        device.print(Utils.nonBreakingSpaces(text));
        device.print("</div></td>");
        if (frame.isIconifyable() && !frame.isIconified() && getIconifyIcon() != null) {
            device.print("<td>");
            writeWindowIcon(device, frame,
                    SInternalFrameEvent.INTERNAL_FRAME_ICONIFIED, getIconifyIcon());
            device.print("</td>");
        }
        if (frame.isIconified() && getDeiconifyIcon() != null) {
            device.print("<td>");
            writeWindowIcon(device, frame,
                    SInternalFrameEvent.INTERNAL_FRAME_DEICONIFIED, getDeiconifyIcon());
            device.print("</td>");
        }
        if (frame.isMaximizable() && !frame.isMaximized() && getMaximizeIcon() != null) {
            device.print("<td>");
            writeWindowIcon(device, frame,
                    SInternalFrameEvent.INTERNAL_FRAME_MAXIMIZED, getMaximizeIcon());
            device.print("</td>");
        }
        if (frame.isMaximized() && getUnmaximizeIcon() != null) {
            device.print("<td>");
            writeWindowIcon(device, frame,
                    SInternalFrameEvent.INTERNAL_FRAME_UNMAXIMIZED, getUnmaximizeIcon());
            device.print("</td>");
        }
        if (frame.isClosable() && getCloseIcon() != null) {
            device.print("<td>");
            writeWindowIcon(device, frame,
                    SInternalFrameEvent.INTERNAL_FRAME_CLOSED, getCloseIcon());
            device.print("</td>");
        }
        device.print("<td>&nbsp;&nbsp;</td></tr></table>");
        device.print("</div>");
    }
}
