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

import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.io.Device;

import java.io.IOException;

/**
 * @author hengels
 * @version $Revision$
 */
public abstract class IconTextCompound
        implements SConstants {
    public void writeCompound(Device device, SComponent component, int horizontal, int vertical) throws IOException {
        if (horizontal == NO_ALIGN)
            horizontal = RIGHT;
        if (vertical == NO_ALIGN)
            vertical = CENTER;
        boolean order = vertical == TOP || (vertical == CENTER && horizontal == LEFT);


        device.print("<table");
        Utils.innerPreferredSize(device, component.getPreferredSize());
        device.print(">");

        if (vertical == TOP && horizontal == LEFT ||
                vertical == BOTTOM && horizontal == RIGHT) {
            device.print("<tr><td align=\"left\" valign=\"top\">");
            first(device, order);
            device.print("</td><td></td></tr><tr><td></td><td align=\"right\" valign=\"bottom\">");
            last(device, order);
            device.print("</td></tr>");
        } else if (vertical == TOP && horizontal == RIGHT ||
                vertical == BOTTOM && horizontal == LEFT) {
            device.print("<tr><td></td><td align=\"right\" valign=\"top\">");
            first(device, order);
            device.print("</td></tr><tr><td align=\"left\" valign=\"bottom\">");
            last(device, order);
            device.print("</td><td></td></tr>");
        } else if (vertical == TOP && horizontal == CENTER ||
                vertical == BOTTOM && horizontal == CENTER) {
            device.print("<tr><td align=\"center\" valign=\"top\">");
            first(device, order);
            device.print("</td></tr><tr><td align=\"center\" valign=\"bottom\">");
            last(device, order);
            device.print("</td></tr>");
        } else if (vertical == CENTER && horizontal == LEFT ||
                vertical == CENTER && horizontal == RIGHT) {
            device.print("<tr><td align=\"left\">");
            first(device, order);
            device.print("</td><td align=\"right\">");
            last(device, order);
            device.print("</td></tr>");
        } else {
            System.out.println("WARNING");
            System.out.println("horizontal = " + horizontal);
            System.out.println("vertical = " + vertical);
        }
        device.print("</table>");
    }

    private void first(Device d, boolean order) throws IOException {
        if (order)
            text(d);
        else
            icon(d);
    }

    private void last(Device d, boolean order) throws IOException {
        if (!order)
            text(d);
        else
            icon(d);
    }

    protected abstract void text(Device d) throws IOException;

    protected abstract void icon(Device d) throws IOException;
}
