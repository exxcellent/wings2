// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/Dialog.plaf'
/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
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

public class DialogCG extends org.wings.plaf.css.FormCG implements SConstants, org.wings.plaf.DialogCG {

//--- byte array converted template snippets.
    private final static byte[] __img_border_0 = "<img border=\"0\"".getBytes();
    private final static byte[] __ = "/>".getBytes();
    private final static byte[] __class_framebut = " class=\"framebutton\"><a href=\"".getBytes();
    private final static byte[] __a_td = "</a></td>".getBytes();
    private final static byte[] __class_framebut_1 = " class=\"framebutton\">".getBytes();

//--- properties of this plaf.
    private SIcon closeIcon;

    /**
     * Initialize properties from config
     */
    public DialogCG() {
        final CGManager manager = SessionManager.getSession().getCGManager();
        setCloseIcon((SIcon) manager.getObject("DialogCG.closeIcon", SIcon.class));
    }

    private void writeIcon(Device device, SIcon icon) throws IOException {
        device.write(__img_border_0);
        org.wings.plaf.Utils.optAttribute(device, "src", icon.getURL());
        org.wings.plaf.Utils.optAttribute(device, "width", icon.getIconWidth());
        org.wings.plaf.Utils.optAttribute(device, "height", icon.getIconHeight());
        device.write(__);
    }

    private void writeWindowIcon(Device device, SDialog dialog,
                                 int event, SIcon icon) throws IOException {
        RequestURL addr = dialog.getRequestURL();
        addr.addParameter(org.wings.plaf.css.Utils.event(dialog), event);

        device.write("<td".getBytes());
        org.wings.plaf.Utils.optAttribute(device, "width", icon.getIconWidth());

        device.write(__class_framebut);
        org.wings.plaf.Utils.write(device, addr);

        device.write("\">".getBytes());
        writeIcon(device, icon);

        device.write(__a_td);
    }


    public void write(final Device device, final SComponent component)
            throws IOException {
        device.print("<table border=\"0\" width=\"100%\" height=\"100%\"><tr><td align=\"center\" valign=\"middle\">\n");
        super.write(device, component);
        device.print("</td></tr></table>\n");
    }

    protected void writeContent(final Device device, final SComponent component) throws IOException {
        final SDialog dialog = (SDialog) component;
        device.print("<table><tr>");
        int cols = 0;
        String text = dialog.getTitle();
        if (text == null)
            text = "Dialog";

        // left icon
        if (dialog.getIcon() != null) {
            SIcon icon = dialog.getIcon();
            device.print("<th");
            org.wings.plaf.Utils.optAttribute(device, "width", icon.getIconWidth());
            device.write(__class_framebut_1);
            writeIcon(device, icon);
            device.print("</th>");
            cols++;
        }

        device.print("<th>");
        org.wings.plaf.Utils.write(device, text);
        device.print("</th>");
        cols++;

        if (dialog.isClosable() && closeIcon != null) {
            writeWindowIcon(device, dialog, SInternalFrameEvent.INTERNAL_FRAME_CLOSED, closeIcon);
            cols++;
        }

        device.print("</tr>");
        device.write("<tr><td colspan=\"".getBytes());
        org.wings.plaf.Utils.write(device, cols);
        device.write("\">".getBytes());
        super.writeContent(device, component);
        device.write("</td></tr></table>\n".getBytes());
    }

    public SIcon getCloseIcon() {
        return closeIcon;
    }

    public void setCloseIcon(SIcon closeIcon) {
        this.closeIcon = closeIcon;
    }
}
