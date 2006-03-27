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


import org.wings.SIcon;
import org.wings.io.Device;
import java.io.IOException;

public abstract class AbstractLabelCG extends AbstractComponentCG {

    /**
     * Prefer other writeText method!
     */
    protected final void writeText(Device device, String text) throws IOException {
        writeText(device, text, false);
    }

    protected final void writeText(Device device, String text, boolean wordWrap) throws IOException {
        // white-space:nowrap seems to work in all major browser.
        // Except leading and trailing spaces!
        device.print("<span").print(wordWrap ? "" : " style=\"white-space:nowrap\"").print(">");

        if ((text.length() > 5) && (text.startsWith("<html>"))) {
            Utils.writeRaw(device, text.substring(6));
        } else {
            // NOTE: Quoting of spaces would be only necessary for trailing and leading spaces !!!
            // TODO: Quote only trailing/leading spaces
            Utils.quote(device, text, true, !wordWrap, false);
        }

        device.print("</span>");
    }

    protected final void writeIcon(Device device, SIcon icon) throws IOException {
        device.print("<img");
        Utils.optAttribute(device, "src", icon.getURL());
        Utils.optAttribute(device, "width", icon.getIconWidth());
        Utils.optAttribute(device, "height", icon.getIconHeight());
        device.print(" alt=\"");
        device.print(icon.getIconTitle());
        device.print("\"/>");
    }

}
