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
package org.wings.plaf.xhtml.css1;

import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SFont;
import org.wings.io.Device;

import java.awt.*;
import java.io.IOException;

/**
 * Utils.java
 * <p/>
 * <p/>
 * Created: Thu Oct 28 16:23:53 1999
 *
 * @author Holger Engels
 * @version $Revision$
 */
public final class Utils
        implements SConstants {
    final static char[] hexDigits = {
        '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'a', 'b',
        'c', 'd', 'e', 'f'
    };

    private Utils() {
    }

    public static String toColorString(int rgb) {
        char[] buf = new char[6];
        int digits = 6;
        do {
            buf[--digits] = hexDigits[rgb & 15];
            rgb >>>= 4;
        } while (digits != 0);

        return new String(buf);
    }

    public static String toColorString(java.awt.Color c) {
        return toColorString(c.getRGB());
    }

    public static void writeStyleAttribute(Device d, String prefix, String style)
            throws IOException {
        if (style == null)
            return;
        writeStyleAttribute(d, prefix, style, "");
    }

    public static void writeStyleAttribute(Device d, String prefix, String style, String postfix)
            throws IOException {
        if (style == null)
            return;
        writeStyleAttribute(d, prefix + style + postfix);
    }

    public static void writeStyleAttribute(Device d, String style)
            throws IOException {
        if (style == null)
            return;

        d.print(" class=\"");
        d.print(style);
        d.print("\"");
    }

    /**
     * Write component class and all other style attributes including border.
     * The <i>span</i>-tag is used.
     *
     * @param d         the device to write to
     * @param component the component to get style and bg-color from
     */
    public static void writeSpanWithStyleAttributePrefix(Device d, SComponent component)
            throws IOException {
        String style = component.getStyle();
        boolean hasAttr = org.wings.plaf.xhtml.Utils.hasSpanAttributes(component);
        if (style == null && !hasAttr)
            return;

        d.print("<span ");
        if (hasAttr) {
            d.print("style=\"");
            org.wings.plaf.xhtml.Utils.writeSpanAttributes(d, component);
            d.print("\" ");
        }
        writeStyleAttribute(d, style);
        d.print(">");
    }


    /**
     * Write ending <tt>&ls;/span&gt;</tt>-attribute, if
     * component has either style or background-color defined.
     *
     * @param d         the device to write to
     * @param component the component to get style and bg-color from
     */
    public static void writeSpanWithStyleAttributePostfix(Device d, SComponent component)
            throws IOException {
        if (
                component.getStyle() == null &&
                org.wings.plaf.xhtml.Utils.hasSpanAttributes(component)
        )
            return;

        d.print("</span>");
    }

    public static void writeSpanWithStyleAttributePrefix(Device d, String style)
            throws IOException {
        if (style == null)
            return;

        d.print("<span");
        Utils.writeStyleAttribute(d, style);
        d.print(">");
    }

    public static void writeSpanWithStyleAttributePostfix(Device d, String style)
            throws IOException {
        if (style == null)
            return;

        d.print("</span>");
    }

    public static void writeDivWithStyleAttributePrefix(Device d, String style)
            throws IOException {
        if (style == null)
            return;

        d.print("<div");
        Utils.writeStyleAttribute(d, style);
        d.print(">");
    }

    public static void writeDivWithStyleAttributePostfix(Device d, String style)
            throws IOException {
        if (style == null)
            return;

        d.print("</div>");
    }

    static void writeHiddenComponent(Device d, String name, String value)
            throws IOException {
        d.print("<input type=\"hidden\" name=\"").
                print(name).print("\" value=\"").
                print(value).print("\" />\n");
    }

    public static String toHexString(int rgb) {
        char[] buf = new char[6];
        int digits = 6;
        do {
            buf[--digits] = hexDigits[rgb & 15];
            rgb >>>= 4;
        } while (digits != 0);

        return new String(buf);
    }

    public static String toHexString(java.awt.Color c) {
        return toHexString(c.getRGB());
    }

    public static void writeFontPrefix(Device d, SFont font)
            throws IOException {
        writeFontPrefix(d, font, null);
    }

    public static void writeFontPrefix(Device d, SFont font, Color color)
            throws IOException {
        if (font == null && color == null)
            return;

        String face = null;
        int style = PLAIN;
        int size = Integer.MIN_VALUE;
        if (font != null) {
            face = font.getFace();
            style = font.getStyle();
            size = font.getSize();
        }
        d.print("<font");

        if (face != null)
            d.print(" face=\"").print(face).print("\"");

        if (size > Integer.MIN_VALUE) {
            d.print(" size=");
            if (size > 0)
                d.print("+");
            d.print(size);
        }

        if (color != null)
            d.print(" color=#").print(toColorString(color));

        d.print(">");

        if ((style & ITALIC) != 0)
            d.print("<i>");
        if ((style & BOLD) != 0)
            d.print("<b>");
    }

    public static void writeFontPostfix(Device d, SFont font)
            throws IOException {
        writeFontPostfix(d, font, null);
    }

    public static void writeFontPostfix(Device d, SFont font, Color color)
            throws IOException {
        if (font == null && color == null)
            return;

        int style = PLAIN;
        if (font != null)
            style = font.getStyle();

        if ((style & BOLD) != 0)
            d.print("</b>");
        if ((style & ITALIC) != 0)
            d.print("</i>");
        d.print("</font>");
    }
}


