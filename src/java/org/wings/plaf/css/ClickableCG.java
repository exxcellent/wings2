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


import java.io.IOException;

import org.wings.SClickable;
import org.wings.SComponent;
import org.wings.SIcon;
import org.wings.io.Device;

public class ClickableCG extends LabelCG implements org.wings.plaf.ButtonCG {

    public void writeContent(final Device device, final SComponent component)
            throws IOException {
        final SClickable button = (SClickable) component;

        if (button.getShowAsFormComponent()) {
            writeButtonStart(device, button);
            device.print(" name=\"");
            Utils.write(device, button.getEventTarget().getEncodedLowLevelEventId());
            device.print("\" value=\"");
            Utils.write(device, button.getEvent());
            device.print("\"");
            Utils.optAttribute(device, "tabindex", button.getFocusTraversalIndex());
        } else {
            device.print("<a href=\"");
            Utils.write(device, button.getURL());
            device.print("\"");
        }

        if (!button.isEnabled())
            device.print(" disabled=\"true\"");

        Utils.writeEvents(device, button);
        device.print(">");

        final String text = button.getText();
        final SIcon icon = getIcon(button);

        if (icon == null && text != null)
            writeText(device, text);
        else if (icon != null && text == null)
            writeIcon(device, icon);
        else if (icon != null && text != null) {
            new IconTextCompound() {
                protected void text(Device d) throws IOException {
                    writeText(d, text);
                }

                protected void icon(Device d) throws IOException {
                    writeIcon(d, icon);
                }
            }.writeCompound(device, component, button.getHorizontalTextPosition(), button.getVerticalTextPosition());
        }

        if (button.getShowAsFormComponent())
            device.print("</button>");
        else
            device.print("</a>");
    }

    protected void writeButtonStart(Device device, SClickable button) throws IOException {
        device.print("<button");
    }

    protected SIcon getIcon(SClickable abstractButton) {
        if (abstractButton.isSelected()) {
            return abstractButton.isEnabled()
                    ? abstractButton.getSelectedIcon()
                    : abstractButton.getDisabledSelectedIcon();
        } else {
            return abstractButton.isEnabled()
                    ? abstractButton.getIcon()
                    : abstractButton.getDisabledIcon();
        }
    }
}
