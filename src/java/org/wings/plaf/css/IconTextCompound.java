/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
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

        if (component.getPreferredSize() != null)
            device.print("<table style=\"width:100%; height: 100%\">");
        else
            device.print("<table>");
        
        if (vertical == TOP && horizontal == LEFT ||
            vertical == BOTTOM && horizontal == RIGHT) {
            device.print("<tr><td>");
            first(device, order);
            device.print("</td><td></td></tr><tr><td></td><td>");
            last(device, order);
            device.print("</td></tr>");
        }
        else if (vertical == TOP && horizontal == RIGHT ||
            vertical == BOTTOM && horizontal == LEFT) {
            device.print("<tr><td></td><td>");
            first(device, order);
            device.print("</td></tr><tr><td>");
            last(device, order);
            device.print("</td><td></td></tr>");
        }
        else if (vertical == TOP && horizontal == CENTER ||
            vertical == BOTTOM && horizontal == CENTER) {
            device.print("<tr><td>");
            first(device, order);
            device.print("</td></tr><tr><td>");
            last(device, order);
            device.print("</td></tr>");
        }
        else if (vertical == CENTER && horizontal == LEFT ||
            vertical == CENTER && horizontal == RIGHT) {
            device.print("<tr><td>");
            first(device, order);
            device.print("</td><td>");
            last(device, order);
            device.print("</td></tr>");
        }
        else {
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
