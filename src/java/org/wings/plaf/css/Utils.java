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
import org.wings.io.Device;
import org.wings.script.ScriptListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Utils.java
 *
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 * @version $Revision$
 */
public final class Utils
        extends org.wings.plaf.Utils {

    final static char[] hexDigits = {
        '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'a', 'b',
        'c', 'd', 'e', 'f'};

    private Utils() {
    }

    /**
     * Renders a container using its Layout manager or fallback just one after another.
     */
    public static void renderContainer(Device d, SContainer c)
            throws IOException {
        SLayoutManager layout = c.getLayout();

        if (layout != null) {
            layout.write(d);
        } else {
            for (int i = 0; i < c.getComponentCount(); i++) {
                c.getComponent(i).write(d);
            }
        }
    }

    public static void writeEvents(Device d, SComponent c)
            throws IOException {
        ScriptListener[] listeners = c.getScriptListeners();
        if (listeners.length > 0) {
            Map eventScripts = new HashMap();
            for (int i = 0; i < listeners.length; i++) {
                final ScriptListener script = listeners[i];
                final String event = script.getEvent();
                String eventScriptCode = script.getCode();

                if (event == null
                        || event.length() == 0
                        || eventScriptCode == null
                        || eventScriptCode.length() == 0) {
                    continue;
                }

                if (eventScripts.containsKey(event)) {
                    String savedEventScriptCode = (String) eventScripts.get(event);
                    eventScriptCode = savedEventScriptCode
                            + (savedEventScriptCode.trim().endsWith(";") ? "" : ";")
                            + eventScriptCode;
                }
                eventScripts.put(event, eventScriptCode);
            }

            Iterator it = eventScripts.keySet().iterator();
            while (it.hasNext()) {
                String event = (String) it.next();
                String code = (String) eventScripts.get(event);
                d.print(" ");
                d.print(event);
                d.print("=\"");
                d.print(code);
                d.print("\"");
            }
        }
    }

    /*
      static String event(SComponent component, String lowLevelEventId) {
      if (component.getSession().getEventInvalidation() && component.getParentFrame() != null) {
      if (!(component instanceof LowLevelEventListener) || ((LowLevelEventListener)component).checkEpoch())
      return (component.getParentFrame().getEventEpoch()
      + SConstants.UID_DIVIDER
      + lowLevelEventId);
      }
      return lowLevelEventId;
      }
    */

    /**
     * Encodes a low level event id for using it in a request parameter. Every
     * {@link LowLevelEventListener} should encode its LowLevelEventId before
     * using it in a request parameter. This encoding adds consistency checking
     * for outtimed requests ("Back Button")
     */
    public static String event(SComponent component) {
        return component.getEncodedLowLevelEventId();
        //return event(component, component.getLowLevelEventId());
    }

    /**
     * HTML allows 4 values for align property of a div element.
     * @param d Output
     * @param align Please refer {@link SConstants}
     * @throws IOException
     */
    public static void printDivHorizontalAlignment(Device d, int align) throws IOException {
        printTableHorizontalAlignment(d, align);
    }

    // TODO ? may obsolete?
    public static void printTableHorizontalAlignment(Device d, int align)
            throws IOException {
        if (align == SConstants.NO_ALIGN || align == SConstants.LEFT) {
            d.print(" align=\"left\"");
        } else if (align == SConstants.CENTER) {
            d.print(" align=\"center\"");
        } else if (align == SConstants.RIGHT) {
            d.print(" align=\"right\"");
        } else if (align == SConstants.JUSTIFY) {
            d.print(" align=\"justify\"");
        }
    }

    // TODO ? may obsolete?
    public static void printTableVerticalAlignment(Device d, int align)
            throws IOException {
        if (align == SConstants.NO_ALIGN || align == SConstants.CENTER) {
        } else if (align == SConstants.TOP) {
            d.print(" valign=\"top\"");
        } else if (align == SConstants.BOTTOM) {
            d.print(" valign=\"bottom\"");
        } else if (align == SConstants.BASELINE) {
            d.print(" valign=\"baseline\"");
        }
    }

    public static void printTableCellAlignment(Device d, SComponent c)
            throws IOException {
        printTableHorizontalAlignment(d, c.getHorizontalAlignment());
        printTableVerticalAlignment(d, c.getVerticalAlignment());
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

    /**
     * Writes out the following CSS attributes as CSS inline style
     * <ul>
     * <li>Background/Foreground color</li>
     * <li>Font size,style,weight,family</li>
     * <li>Preferred Dimension height and width</li>
     * </ul>. Sample: <code> style="width:100%; color:#ff0000;"</code>
     * @param d Output device
     * @param component Component having CSS attributes
     * @throws IOException
     */
    public static void printCSSInlineStyleAttributes(Device d, SComponent component) throws IOException {
        d.print(" style=\"");

        java.awt.Color fgColor = component.getForeground();
        java.awt.Color bgcolor = component.getBackground();
        SFont font = component.getFont();
        SDimension dim = component.getPreferredSize();

        if (bgcolor != null)
            d.print("background-color:#").print(toColorString(bgcolor)).print(";");

        if (fgColor != null) {
            // not necessary? d.print("font-color:#").print(toColorString(fgColor)).print(";");
            d.print("color:#").print(toColorString(fgColor)).print(";");
        }
        if (font != null) {
            int style = font.getStyle();
            d.print("font-size:").print(font.getSize()).print("pt;");
            d.print("font-style:").print((style & java.awt.Font.ITALIC) > 0 ? "italic;" : "normal;");
            d.print("font-weight:").print((style & java.awt.Font.BOLD) > 0 ? "bold;" : "normal;");
            d.print("font-family:").print(font.getFace()).print(";");
        }

        if (dim != null) {
            if (dim.isWidthDefined()) d.print("width:").print(dim.getWidth()).print(";");
            if (dim.isHeightDefined()) d.print("height:").print(dim.getHeight()).print(";");
        }

        d.print("\"");
    }

    public static void printIconTextCompound(Device d, String icon, String text, int horizontal, int vertical)
            throws IOException {
        if (icon == null && text != null)
            d.print(text);
        else if (icon != null && text == null)
            d.print(icon);
        else if (icon != null && text != null) {
            boolean textFirst = vertical == TOP || (vertical == CENTER && horizontal == LEFT);
            String first = textFirst ? text : icon;
            String last = textFirst ? icon : text;

            d.print("<table>");
            if (vertical != TOP && horizontal == LEFT ||
                    vertical != BOTTOM && horizontal == RIGHT) {
                d.print("<tr><td>");
                write(d, first);
                d.print("</td><td></td></tr><tr><td></td><td>");
                write(d, last);
                d.print("</td></tr>");
            } else if (vertical != TOP && horizontal == RIGHT ||
                    vertical != BOTTOM && horizontal == LEFT) {
                d.print("<tr><td></td><td>");
                write(d, first);
                d.print("</td></tr><tr><td>");
                write(d, last);
                d.print("</td><td></td></tr>");
            } else if (vertical != TOP && horizontal == CENTER ||
                    vertical != BOTTOM && horizontal == CENTER) {
                d.print("<tr><td>");
                write(d, first);
                d.print("</td></tr><tr><td>");
                write(d, last);
                d.print("</td></tr>");
            } else if (vertical != CENTER && horizontal == LEFT ||
                    vertical != CENTER && horizontal == RIGHT) {
                d.print("<tr><td>");
                write(d, first);
                d.print("</td><td>");
                write(d, last);
                d.print("</td></tr>");
            }
            d.print("</table>");
        }
    }

    /**
     * Prints a HTML style attribute with widht/height of passed SDimension.
     * <p>Sample: <code> style="widht:100%;"</code>
     * @param device Device to print to
     * @param preferredSize Preferred sitze. May be null or contain null attributes
     */
    public static void printCSSInlinePreferredSize(Device device, SDimension preferredSize) throws IOException {
        if (preferredSize != null) {
            device.print(" style=\"");
            if (preferredSize.isWidthDefined())
                device.print("width:").print(preferredSize.getWidth()).print(";");
            if (preferredSize.isHeightDefined())
                device.print("height:").print(preferredSize.getHeight()).print(";");
            device.print("\"");
        }
    }
}
