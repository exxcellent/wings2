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
import org.wings.io.NullDevice;
import org.wings.plaf.CGManager;
import org.wings.script.ScriptListener;
import org.wings.style.Style;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Utils.java
 *
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 * @version $Revision$
 */
public final class Utils implements SConstants {
    /**
     * Print debug information in generated HTML
     */
    public static boolean PRINT_DEBUG = true;

    protected final static char[] hexDigits = {
        '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'a', 'b',
        'c', 'd', 'e', 'f'};

    protected Utils() {
    }

    /**
     * Renders a container using its Layout manager or fallback just one after another.
     */
    public static void renderContainer(Device d, SContainer c)
            throws IOException {
        SLayoutManager layout = c.getLayout();

        if (layout == null) {
            // lookup the default Layout Behaviour
            final CGManager manager = c.getSession().getCGManager();
            Object value;
            value = manager.getObject("SContainer.defaultLayoutBehaviour", String.class);
            if (value != null && value instanceof String) {
                if ("classic".equals(((String) value).toLowerCase())) {
                    layout = new SBoxLayout(SBoxLayout.VERTICAL);
                } else if ("standard".equals(((String) value).toLowerCase())) {
                    layout = new SFlowLayout(SFlowLayout.LEFT);
                } else if ("none".equals(((String) value).toLowerCase())) {
                    // just write out the components one after another
                    for (int i = 0; i < c.getComponentCount(); i++) {
                        c.getComponent(i).write(d);
                    }
                    return;
                } else { // fallback
                    layout = new SFlowLayout(SFlowLayout.LEFT);
                }
            } else {
                // Swing default LayoutManager is FlowLayout
                layout = new SFlowLayout(SFlowLayout.LEFT);
            }
            c.setLayout(layout);
        }
        layout.write(d);
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
     *
     * @param d     Output
     * @param align Please refer {@link SConstants}
     * @throws IOException
     */
    public static void printDivHorizontalAlignment(Device d, int align) throws IOException {
        printTableHorizontalAlignment(d, align);
    }

    /**
     * Horizontal alignment for TABLE cells. i.e. <code>align="center"</code>
     */
    private static void printTableHorizontalAlignment(Device d, int align)
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

    /**
     * Vertical alignment for TABLE cells. i.e. <code>valign="top"</code>
     */
    private static void printTableVerticalAlignment(Device d, int align)
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
        if (c != null) {
            printTableHorizontalAlignment(d, c.getHorizontalAlignment());
            printTableVerticalAlignment(d, c.getVerticalAlignment());
        }
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
     *
     * @param d         Output device
     * @param component Component having CSS attributes
     * @throws IOException
     * @deprecated Use #generateCSSComponentInlineStyle
     */
    public static void printCSSInlineStyleAttributes(Device d, SComponent component) throws IOException {
        if (component == null)
            return;
        StringBuffer styleString = generateCSSComponentInlineStyle(component);

        Utils.optAttribute(d, "style", styleString.toString());
    }

    public static StringBuffer generateCSSComponentInlineStyle(SComponent component) {
        StringBuffer styleString = new StringBuffer();
        if (component != null) {
            final Color fgColor = component.getForeground();
            final Color bgcolor = component.getBackground();
            final SFont font = component.getFont();
            final SDimension dim = component.getPreferredSize();

            if (bgcolor != null)
                styleString.append("background-color:#").append(toColorString(bgcolor)).append(";");

            if (fgColor != null) {
                styleString.append("color:#").append(toColorString(fgColor)).append(";");
            }

            if (font != null) {
                int style = font.getStyle();
                styleString.append("font-size:").append(font.getSize()).append("pt;");
                styleString.append("font-style:").append((style & Font.ITALIC) > 0 ? "italic;" : "normal;");
                styleString.append("font-weight:").append((style & Font.BOLD) > 0 ? "bold;" : "normal;");
                styleString.append("font-family:").append(font.getFace()).append(";");
            }

            if (dim != null) {
                if (dim.isWidthDefined()) styleString.append("width:").append(dim.getWidth()).append(";");
                if (dim.isHeightDefined()) styleString.append("height:").append(dim.getHeight()).append(";");
            }
        }
        return styleString;
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
     *
     * @param device        Device to print to
     * @param preferredSize Preferred sitze. May be null or contain null attributes
     * @deprecated Consolidate CSS Inline Styles Strings !!!
     */
    public static void printCSSInlinePreferredSize(Device device, SDimension preferredSize) throws IOException {
        if (preferredSize != null && (preferredSize.isWidthDefined() || preferredSize.isHeightDefined())) {
            device.print(" style=\"");
            device.print(generateCSSInlinePreferredSize(preferredSize));
            device.print("\"");
        }
    }

    /**
     * Generates a CSS Inline Style string for the passed SDimension.
     * <p>Sample: <code>width:100%;heigth=15px"</code>
     *
     * @param preferredSize Preferred sitze. May be null or contain null attributes
     * @return Style string. Sample: <code>width:100%;heigth=15px"</code>
     */
    public static StringBuffer generateCSSInlinePreferredSize(SDimension preferredSize) {
        StringBuffer styleString = new StringBuffer();
        if (preferredSize != null && (preferredSize.isWidthDefined() || preferredSize.isHeightDefined())) {
            if (preferredSize.isWidthDefined())
                styleString.append("width:").append(preferredSize.getWidth()).append(";");
            if (preferredSize.isHeightDefined())
                styleString.append("height:").append(preferredSize.getHeight()).append(";");
        }
        return styleString;
    }

    public static StringBuffer generateCSSInlineBorder(int borderSize) {
        StringBuffer styleString = new StringBuffer();
        if (borderSize > 0)
            styleString.append("border:").append(borderSize).append("px solid black;");
        else
            ; //styleString.append("border:none;"); Not necessary. Default
        return styleString;
    }

    /**
     * Prints a HTML style attribute with widht/height of 100%.
     * <p>Sample: <code> style="width:100%;"</code>
     *
     * @param device Device to print to
     */
    public static void printCSSInlineFullSize(Device device, SDimension preferredSize) throws IOException {
        if (preferredSize != null && (preferredSize.isWidthDefined() || preferredSize.isHeightDefined())) {
            device.print(" style=\"width:100%;height:100%\"");
        }
    }

    /**
     * writes an {X|HT}ML quoted string according to RFC 1866.
     * '"', '<', '>', '&'  become '&quot;', '&lt;', '&gt;', '&amp;'
     */
    // not optimized yet
    private static void quote(Device d, String s, boolean quoteNewline) throws IOException {
        if (s == null) return;
        char[] chars = s.toCharArray();
        char c;
        int last = 0;
        for (int pos = 0; pos < chars.length; ++pos) {
            c = chars[pos];
            // write special characters as code ..
            if (c < 32 || c > 127) {
                d.print(chars, last, (pos - last));
                if (c == '\n' && quoteNewline) {
                    d.print("<br>");
                } else {
                    d.print("&#");
                    d.print((int) c);
                    d.print(";");
                } // end of if ()
                last = pos + 1;
            } else
                switch (c) {
                    case '&':
                        d.print(chars, last, (pos - last));
                        d.print("&amp;");
                        last = pos + 1;
                        break;
                    case '"':
                        d.print(chars, last, (pos - last));
                        d.print("&quot;");
                        last = pos + 1;
                        break;
                    case '<':
                        d.print(chars, last, (pos - last));
                        d.print("&lt;");
                        last = pos + 1;
                        break;
                    case '>':
                        d.print(chars, last, (pos - last));
                        d.print("&gt;");
                        last = pos + 1;
                        break;
                        /*
                         * watchout: we cannot replace _space_ by &nbsp;
                         * since non-breakable-space is a different
                         * character: isolatin-char 160, not 32.
                         * This will result in a confusion in forms:
                         *   - the user enters space, presses submit
                         *   - the form content is written to the Device by wingS,
                         *     space is replaced by &nbsp;
                         *   - the next time the form is submitted, we get
                         *     isolatin-char 160, _not_ space.
                         * (at least Konqueror behaves this correct; mozilla does not)
                         *                                                       Henner
                         */
                }
        }
        d.print(chars, last, chars.length - last);
    }

    public static void writeRaw(Device d, String s) throws IOException {
        if (s == null) return;
        d.print(s);
    }

    /**
     * writes the given String to the device. The string is quoted, i.e.
     * for all special characters in *ML, their appropriate entity is
     * returned.
     * If the String starts with '<html>', the content is regarded being
     * HTML-code and is written as is (without the <html> tag).
     */
    public static void write(Device d, String s) throws IOException {
        if (s == null) return;
        if ((s.length() > 5) && (s.startsWith("<html>"))) {
            writeRaw(d, s.substring(6));
        } else {
            quote(d, s, false);
        }
    }

    /**
     * Prints an optional attribute. If the String value has a content
     * (value != null && value.length > 0), the attrib is added otherwise
     * it is left out
     */
    public static void optAttribute(Device d, String attr, Style value)
            throws IOException {
        if (value != null) {
            d.print(" ").print(attr).print("=\"").print(value.getName()).print("\"");
        }
    }

    /**
     * Prints an optional attribute. If the String value has a content
     * (value != null && value.length > 0), the attrib is added otherwise
     * it is left out
     */
    public static void optAttribute(Device d, String attr, String value)
            throws IOException {
        if (value != null && value.trim().length() > 0) {
            d.print(" ").print(attr).print("=\"");
            quote(d, value, true);
            d.print("\"");
        }
    }

    public static void childSelectorWorkaround(Device d, String style)
            throws IOException {
        d.print(" class=\"");
        d.print(style);
        d.print("\"");
    }

    /**
     * Prints an optional attribute. If the String value has a content
     * (value != null && value.length > 0), the attrib is added otherwise
     * it is left out
     */
    public static void optAttribute(Device d, String attr, Color value)
            throws IOException {
        if (value != null) {
            d.print(" ");
            d.print(attr);
            d.print("=\"");
            write(d, value);
            d.print("\"");
        }
    }

    /**
     * Prints an optional, renderable attribute.
     */
    public static void optAttribute(Device d, String attr, Renderable r)
            throws IOException {
        if (r != null) {
            d.print(" ");
            d.print(attr);
            d.print("=\"");
            r.write(d);
            d.print("\"");
        }
    }

    /**
     * Prints an optional attribute. If the integer value is greater than 0,
     * the attrib is added otherwise it is left out
     */
    public static void optAttribute(Device d, String attr, int value)
            throws IOException {
        if (value > 0) {
            d.print(" ");
            d.print(attr);
            d.print("=\"");
            d.print(String.valueOf(value));
            d.print("\"");
        }
    }

    /**
     * Prints an optional attribute. If the dimension value not equals <i>null</i>
     * the attrib is added otherwise it is left out
     */
    public static void optAttribute(Device d, String attr, SDimension value)
            throws IOException {
        if (value != null) {
            d.print(" ");
            d.print(attr);
            d.print("=\"");
            write(d, value.toString());
            d.print("\"");
        }
    }

    /**
     * writes the given java.awt.Color to the device. Speed optimized;
     * character conversion avoided.
     */
    public static void write(Device d, Color c) throws IOException {
        d.print("#");
        int rgb = (c == null) ? 0 : c.getRGB();
        int mask = 0xf00000;
        for (int bitPos = 20; bitPos >= 0; bitPos -= 4) {
            d.print(hexDigits[(rgb & mask) >>> bitPos]);
            mask >>>= 4;
        }
    }

    /**
     * writes anything Renderable
     */
    public static void write(Device d, Renderable r) throws IOException {
        if (r == null) return;
        r.write(d);
    }

    /*
     * testing purposes.
     */
    public static void main(String argv[]) throws Exception {
        Color c = new Color(255, 254, 7);
        Device d = new org.wings.io.StringBufferDevice();
        write(d, c);
        quote(d, "\nThis is a <abc> string \"; foo & sons\nmoin", true);
        d.print(String.valueOf(-42));
        d.print(String.valueOf(Integer.MIN_VALUE));

        write(d, "hello test&nbsp;\n");
        write(d, "<html>hallo test&nbsp;\n");

        d = new org.wings.io.NullDevice();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; ++i) {
            quote(d, "this is a little & foo", true);
        }
        System.err.println("took: " + (System.currentTimeMillis() - start)
                + "ms");
    }

    /**
     * Helper method for CGs to print out debug information in output stream.
     * If {@link #PRINT_DEBUG} prints the passed string and returns the current {@link Device}.
     * In other case omits ouput and returns a {@link NullDevice}
     *
     * @param d The original device
     * @return The original device or a {@link NullDevice}
     */
    public static Device printDebug(Device d, String s) throws IOException {
        if (PRINT_DEBUG)
            return d.print(s);
        else
            return NullDevice.DEFAULT;
    }

    /**
     * Prints a hierarchical idented newline if debug mode is enabled.
     * {@link #printNewline(org.wings.io.Device, org.wings.SComponent)}
     */
    public static Device printDebugNewline(Device d, SComponent currentComponent) throws IOException {
        if (PRINT_DEBUG)
            return printNewline(d, currentComponent);
        else
            return d;
    }

    /**
     * Prints a hierarchical idented newline. For each surrounding container of the passed component one ident level.
     */
    public static Device printNewline(Device d, SComponent currentComponent) throws IOException {
        if (currentComponent == null || PRINT_DEBUG == false) // special we save every ms handling for holger ;-)
            return d;
        d.print("\n");
        while (currentComponent.getParent() != null && currentComponent.getParent().getParent() != null) {
            d.print("\t");
            currentComponent = currentComponent.getParent();
        }
        return d;
    }

    /**
     * loads a script from disk through the classloader.
     *
     * @param path the path where the script can be found
     * @return the script as a String
     */
    public static String loadScript(String path) {
        InputStream in = null;
        BufferedReader reader = null;

        try {
            in = Utils.class.getClassLoader().getResourceAsStream(path);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            buffer.append("\n");

            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                in.close();
            } catch (Exception ign) {
            }
            try {
                reader.close();
            } catch (Exception ign1) {
            }
        }
    }

    /**
     * prints a String. Substitutes spaces with nbsp's
     * @param text
     * @return
     */
    public static String nonBreakingSpaces(String text) {
        return text.replace(' ', '\u00A0');
    }

}
