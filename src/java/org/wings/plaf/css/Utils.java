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

    private final static String ALIGN_CENTER = " align=\"center\"";
    private final static String ALIGN_LEFT = " align=\"left\"";
    private final static String ALIGN_RIGHT = " align=\"right\"";
    private final static String ALIGN_JUSTIFY = " align=\"justify\"";
    private final static String VALIGN_TOP = " valign=\"top\"";
    private final static String VALIGN_BOTTOM = " valign=\"bottom\"";
    private final static String VALIGN_BASELINE = " valign=\"baseline\"";

    public static void printTableHorizontalAlignment(Device d, int align)
            throws IOException {
        switch (align) {
            case SConstants.NO_ALIGN:
            case SConstants.LEFT:
                d.print(ALIGN_LEFT);
                break;
            case SConstants.CENTER:
                d.print(ALIGN_CENTER);
                break;
            case SConstants.RIGHT:
                d.print(ALIGN_RIGHT);
                break;
            case SConstants.JUSTIFY:
                d.print(ALIGN_JUSTIFY);
                break;
        }

    }

    public static void printTableVerticalAlignment(Device d, int align)
            throws IOException {
        switch (align) {
            case SConstants.NO_ALIGN:
            case SConstants.CENTER:
                break;
            case SConstants.TOP:
                d.print(VALIGN_TOP);
                break;
            case SConstants.BOTTOM:
                d.print(VALIGN_BOTTOM);
                break;
            case SConstants.BASELINE:
                d.print(VALIGN_BASELINE);
                break;
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

    public static void writeAttributes(Device d, SComponent component)
            throws IOException {

        java.awt.Color fgColor = component.getForeground();
        java.awt.Color bgcolor = component.getBackground();
        SFont font = component.getFont();
        SDimension dim = component.getPreferredSize();

        if (bgcolor != null)
            d.print("background-color:#").print(toColorString(bgcolor)).print(";");

        if (fgColor != null) {
            d.print("font-color:#").print(toColorString(fgColor)).print(";");
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
            if (dim.width != null) d.print("width:").print(dim.width).print(";");
            if (dim.height != null) d.print("height:").print(dim.height).print(";");
        }
    }

    public static void writeIconTextCompound(Device d, String icon, String text, int horizontal, int vertical)
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

    public static void innerPreferredSize(Device device, SDimension preferredSize) throws IOException {
        if (preferredSize != null) {
            device.print(" style=\"");
            if (preferredSize.getWidth() != null)
                device.print("width:100%;");
            if (preferredSize.getHeight() != null)
                device.print("height: 100%");
            device.print("\"");
        }
    }
}
