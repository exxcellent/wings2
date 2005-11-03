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
import org.wings.SDialog;
import org.wings.event.SInternalFrameEvent;
import org.wings.io.Device;
import org.wings.plaf.css.Utils;

public class DialogCG extends org.wings.plaf.css.DialogCG {

    /* (non-Javadoc)
     * @see org.wings.plaf.css.InternalFrameCG#writeWindowBar(org.wings.io.Device, org.wings.SInternalFrame)
     */
    protected void writeWindowBar(Device device, SDialog frame) throws IOException {
        String text = frame.getTitle();
        if (text == null)
            text = "wingS";
        device.print("<div class=\"WindowBar\">");
        device.print("<table class=\"SLayout\" width=\"100%\"><tr><td width=\"100%\" class=\"SLayout\"><div class=\"WindowBar_title\">");
        if (frame.getIcon() != null) {
            writeIcon(device, frame.getIcon(), WINDOWICON_CLASSNAME);
        }
        device.print(Utils.nonBreakingSpaces(text));
        device.print("</div></td>");
        if (frame.isClosable() && getCloseIcon() != null) {
            device.print("<td class=\"SLayout\">");
            writeWindowIcon(device, frame,
                    SInternalFrameEvent.INTERNAL_FRAME_CLOSED, getCloseIcon(), BUTTONICON_CLASSNAME);
            device.print("</td>");
        }
        device.print("<td class=\"SLayout\">&nbsp;&nbsp;</td></tr></table>");
        device.print("</div>");
    }

}
