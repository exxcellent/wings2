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

import org.wings.SDialog;
import org.wings.SComponent;
import org.wings.event.SInternalFrameEvent;
import org.wings.io.Device;
import org.wings.plaf.css.RenderHelper;
import org.wings.plaf.css.Utils;
import org.wings.util.SStringBuilder;

import java.io.IOException;

public final class DialogCG extends org.wings.plaf.css.DialogCG {

    private static final long serialVersionUID = 1L;

    public void writeInternal(final Device device, final SComponent _c) throws IOException {
        super.writeInternal(device, _c);

        final SStringBuilder script = new SStringBuilder();
        script.append("    function hideSelectBoxes() {\n");
        script.append("        for (var i = 0; i < document.forms.length; i++) {\n");
        script.append("            for (var e = 0; e < document.forms[i].length; e++) {\n");
        script.append("                if (document.forms[i].elements[e].tagName == 'SELECT') {\n");
        script.append("                    document.forms[i].elements[e].style.visibility = 'hidden';\n");
        script.append("                }\n");
        script.append("            }\n");
        script.append("        }\n");
        script.append("    }\n");
        script.append("    hideSelectBoxes();\n");

        RenderHelper.getInstance(_c).addScript(script.toString());
    }

    /* (non-Javadoc)
    * @see org.wings.plaf.css.InternalFrameCG#writeWindowBar(org.wings.io.Device, org.wings.SInternalFrame)
    */
    protected void writeWindowBar(Device device, SDialog frame) throws IOException {
        String text = frame.getTitle();
        if (text == null)
            text = "wingS";
        device.print("<div class=\"WindowBar\">");
        device.print("<table width=\"100%\"><tr><td width=\"100%\"><div class=\"WindowBar_title\">");
        if (frame.getIcon() != null) {
            writeIcon(device, frame.getIcon(), WINDOWICON_CLASSNAME);
        }
        device.print(Utils.nonBreakingSpaces(text));
        device.print("</div></td>");
        if (frame.isClosable() && getCloseIcon() != null) {
            device.print("<td>");
            writeWindowIcon(device, frame,
                    SInternalFrameEvent.INTERNAL_FRAME_CLOSED, getCloseIcon(), BUTTONICON_CLASSNAME);
            device.print("</td>");
        }
        device.print("<td>&nbsp;&nbsp;</td></tr></table>");
        device.print("</div>");
    }

}
