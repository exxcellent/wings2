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

import org.wings.SComponent;
import org.wings.SIcon;
import org.wings.SLabel;
import org.wings.io.Device;

public class LabelCG extends AbstractComponentCG implements
        org.wings.plaf.LabelCG {
    public LabelCG() {
    }

    public void writeContent(final Device device, final SComponent component)
            throws IOException {
        final SLabel label = (SLabel) component;
        final String text = label.getText();
        final SIcon icon = label.isEnabled() ? label.getIcon() : label.getDisabledIcon();
        final int horizontalTextPosition = label.getHorizontalTextPosition();
        final int verticalTextPosition = label.getVerticalTextPosition();
        if (icon == null && text != null) {
            writeText(device, text);
        }
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
            }.writeCompound(device, component, horizontalTextPosition, verticalTextPosition);
        }
    }

    protected void writeText(Device device, String text) throws IOException {
        Utils.write(device, text);
    }

    protected void writeIcon(Device device, SIcon icon) throws IOException {
        device.print("<img");
        Utils.optAttribute(device, "src", icon.getURL());
        Utils.optAttribute(device, "width", icon.getIconWidth());
        Utils.optAttribute(device, "height", icon.getIconHeight());
        device.print(" alt=\"");
        device.print(icon.getIconTitle());
        device.print("\"/>");
    }
}
