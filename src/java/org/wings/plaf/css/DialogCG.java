// DO NOT EDIT! Your changes will be lost: generated from '/home/hengels/jdevel/wings/src/org/wings/plaf/css1/Dialog.plaf'
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

public class DialogCG extends org.wings.plaf.css.FormCG implements SConstants, org.wings.plaf.DialogCG {

//--- byte array converted template snippets.

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
        device.print("<img");
        Utils.optAttribute(device, "src", icon.getURL());
        Utils.optAttribute(device, "width", icon.getIconWidth());
        Utils.optAttribute(device, "height", icon.getIconHeight());
        device.print("/>");
    }

    private void writeWindowIcon(Device device, SDialog dialog,
                                 int event, SIcon icon) throws IOException {
        boolean showAsFormComponent = dialog.getShowAsFormComponent();

        RequestURL addr = dialog.getRequestURL();
        addr.addParameter(Utils.event(dialog), event);

        device.print("<th");
        Utils.optAttribute(device, "width", getIconWidth(icon));
        device.print(">");

        if (showAsFormComponent)
            device.print("<button name=\"")
                    .print(Utils.event(dialog))
                    .print("\" value=\"")
                    .print(event)
                    .print("\">");
        else
            device.print("<a href=\"")
                    .print(dialog.getRequestURL()
                    .addParameter(Utils.event(dialog) + "=" + event).toString())
                    .print("\">");

        writeIcon(device, icon);

        if (showAsFormComponent)
            device.print("</button>");
        else
            device.print("</a>");

        device.print("</th>");
    }

    private String getIconWidth(SIcon icon) {
        if (icon.getIconWidth() == -1)
            return "0%";
        else
            return "" + icon.getIconWidth();
    }

    public void write(final Device device, final SComponent component)
            throws IOException {
        SDialog dialog = (SDialog) component;
        device.print("<table border=\"0\" width=\"100%\" height=\"100%\"><tr><td align=\"center\" valign=\"middle\">");
        super.writeContent(device, dialog);
        device.print("</td></tr></table>\n");
    }

    protected void renderContainer(Device device, SForm component) throws IOException {
        super.write(device, component);
    }

    protected void writeContent(final Device device, final SComponent component) throws IOException {
        final SDialog dialog = (SDialog) component;

        String text = dialog.getTitle();
        int columns = 0;
        if (text == null)
            text = "Dialog";

        device.print("<table");
        Utils.printCSSInlinePreferredSize(device, component.getPreferredSize());
        device.print(">\n<tr>");

        // left icon
        if (dialog.getIcon() != null) {
            SIcon icon = dialog.getIcon();
            device.print("<th");
            Utils.optAttribute(device, "width", getIconWidth(icon));
            device.print(">");
            writeIcon(device, icon);
            device.print("</th>");
            ++columns;
        }

        device.print("<th col=\"title\">&nbsp;");
        Utils.write(device, text);
        device.print("</th>");
        ++columns;

        if (dialog.isClosable() && closeIcon != null) {
            writeWindowIcon(device, dialog,
                    SInternalFrameEvent.INTERNAL_FRAME_CLOSED, closeIcon);
            ++columns;
        }
        device.print("</tr>");

        // write the actual content
        device.print("<tr><td colspan=\"");
        device.print(String.valueOf(columns));
        device.print("\">");
        Utils.renderContainer(device, dialog);
        device.print("</td></tr>");
        device.print("</table>\n");
    }

    public SIcon getCloseIcon() {
        return closeIcon;
    }

    public void setCloseIcon(SIcon closeIcon) {
        this.closeIcon = closeIcon;
    }
}
