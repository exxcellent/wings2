/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css;

import org.wings.SConstants;
import org.wings.io.Device;

import java.io.IOException;

/**
 * @author hengels
 * @version $Revision$
 */
public abstract class IconTextCompound
    implements SConstants {
    public void writeCompound(Device d, int horizontal, int vertical) throws IOException {
        if (horizontal == NO_ALIGN)
            horizontal = RIGHT;
        if (vertical == NO_ALIGN)
            vertical = CENTER;
        boolean order = vertical == TOP || (vertical == CENTER && horizontal == LEFT);

        d.print("<table>");
        if (vertical == TOP && horizontal == LEFT ||
            vertical == BOTTOM && horizontal == RIGHT) {
            d.print("<tr><td>");
            first(d, order);
            d.print("</td><td></td></tr><tr><td></td><td>");
            last(d, order);
            d.print("</td></tr>");
        }
        else if (vertical == TOP && horizontal == RIGHT ||
            vertical == BOTTOM && horizontal == LEFT) {
            d.print("<tr><td></td><td>");
            first(d, order);
            d.print("</td></tr><tr><td>");
            last(d, order);
            d.print("</td><td></td></tr>");
        }
        else if (vertical == TOP && horizontal == CENTER ||
            vertical == BOTTOM && horizontal == CENTER) {
            d.print("<tr><td>");
            first(d, order);
            d.print("</td></tr><tr><td>");
            last(d, order);
            d.print("</td></tr>");
        }
        else if (vertical == CENTER && horizontal == LEFT ||
            vertical == CENTER && horizontal == RIGHT) {
            d.print("<tr><td>");
            first(d, order);
            d.print("</td><td>");
            last(d, order);
            d.print("</td></tr>");
        }
        else {
            System.out.println("WARNING");
            System.out.println("horizontal = " + horizontal);
            System.out.println("vertical = " + vertical);
        }
        d.print("</table>");
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
