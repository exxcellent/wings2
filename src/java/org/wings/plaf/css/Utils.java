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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.LowLevelEventListener;
import org.wings.Renderable;
import org.wings.RequestURL;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SContainer;
import org.wings.SDimension;
import org.wings.SFont;
import org.wings.SLayoutManager;
import org.wings.border.SBorder;
import org.wings.io.Device;
import org.wings.io.NullDevice;
import org.wings.resource.ResourceManager;
import org.wings.script.JavaScriptEvent;
import org.wings.script.JavaScriptListener;
import org.wings.script.ScriptListener;
import org.wings.session.BrowserType;
import org.wings.style.Style;
import org.wings.util.SStringBuilder;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Utils.java
 * <p/>
 * Helper class that collects static methods usable from CGs.
 *
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 * @version $Revision$
 */
public final class Utils {
    /**
     * Apache jakarta commons logger
     */
    private static final Log log = LogFactory.getLog(Utils.class);

    /**
     * Print debug information in generated HTML
     */
    public static boolean PRINT_DEBUG = ((Boolean) ResourceManager.getObject("SComponents.printDebug", Boolean.class)).booleanValue();
    public static boolean PRINT_PRETTY = ((Boolean) ResourceManager.getObject("SComponents.printPretty", Boolean.class)).booleanValue();

    protected final static char[] hexDigits = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'};

    protected Utils() {
    }

    /**
     * Default list of javascript events to exlcude in {@link #writeEvents(org.wings.io.Device, org.wings.SComponent, String[])}
     */
    public final static String[] EXCLUDE_ON_CLICK = new String[]{JavaScriptEvent.ON_CLICK};

    /**
     * Renders a container using its Layout manager or fallback just one after another.
     */
    public static void renderContainer(Device d, SContainer c) throws IOException {
        final SLayoutManager layout = c.getLayout();

        if (layout == null) {
            d.print("<tbody><tr><td>");
            // just write out the components one after another
            for (int i = 0; i < c.getComponentCount(); i++) {
                c.getComponent(i).write(d);
            }
            d.print("</td></tr></tbody>");
        } else {
            layout.write(d);
        }
    }

    /**
     * Render event listeners attached to the passed component exlucding types of supressed listeners
     *
     * @param device                      output device
     * @param c                           component to retrieve listeners from
     * @param suppressScriptListenerTypes Array of String i.e. <code>new String[] { JavaScriptEvent.ON_CLICK } )</code>
     */
    public static void writeEvents(final Device device, final SComponent c, final String[] suppressScriptListenerTypes)
            throws IOException {
        List types = new ArrayList();
        if (suppressScriptListenerTypes != null && suppressScriptListenerTypes.length > 0) {
            for (int i = 0; i < suppressScriptListenerTypes.length; i++) {
                types.add(suppressScriptListenerTypes[i].toLowerCase());
            }
        }
        ScriptListener[] listeners = c.getScriptListeners();
        if (listeners.length > 0) {
            Map eventScripts = new HashMap();
            for (int i = 0; i < listeners.length; i++) {
                final ScriptListener script = listeners[i];
                if (types.contains(script.getEvent().toLowerCase())) {
                    continue;
                }

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
                final String event = (String) it.next();
                final String code = (String) eventScripts.get(event);
                Utils.optAttribute(device, event, code);
            }
        }
    }

    /**
     * Encodes a low level event id for using it in a request parameter. Every
     * {@link LowLevelEventListener} should encode its LowLevelEventId before
     * using it in a request parameter. This encoding adds consistency checking
     * for outtimed requests ("Back Button")
     */
    public static String event(SComponent component) {
        return component.getEncodedLowLevelEventId();
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
    private static void printTableHorizontalAlignment(final Device d, final int align)
            throws IOException {
        if (align == SConstants.NO_ALIGN) {
            // d.print(" align=\"left\"");
        } else if (align == SConstants.LEFT) {
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
        if (align == SConstants.NO_ALIGN) {
            //d.print(" valign=\"center\"");
        } else if (align == SConstants.CENTER) {
            d.print(" valign=\"middle\"");
        } else if (align == SConstants.TOP) {
            d.print(" valign=\"top\"");
        } else if (align == SConstants.BOTTOM) {
            d.print(" valign=\"bottom\"");
        } else if (align == SConstants.BASELINE) {
            d.print(" valign=\"baseline\"");
        }
    }

    /**
     * Renders the alignment commands for a table cell (horzontal and vertical).
     * To ensure a consistent behaviour you have to pass the default alignment applied for <code>SConstants.NO_ALIGN</code>.
     *
     * @param defaultHorizontal default horizontal alignment to use is not aligned
     * @param defaultVertical   default vertical alignment to use if component is not aligned
     */
    public static void printTableCellAlignment(final Device d, final SComponent c,
                                               final int defaultHorizontal, final int defaultVertical)
            throws IOException {
        if (c != null) {
            final int horizontalAlignment = c.getHorizontalAlignment();
            final int verticalAlignment = c.getVerticalAlignment();
            printTableHorizontalAlignment(d, horizontalAlignment != SConstants.NO_ALIGN ? horizontalAlignment : defaultHorizontal);
            printTableVerticalAlignment(d, verticalAlignment != SConstants.NO_ALIGN ? verticalAlignment : defaultVertical);
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
     * Generates a SStringBuilder containing inlined CSS styles for the following properties of a SComponent:
     * <p><ul><li>Preffered Size</li><li>Font</li><li>Background- and Foregroud color.</li></ul>
     *
     * @param component Component to grab parameters from.
     */
    public static SStringBuilder generateCSSComponentInlineStyle(SComponent component) {
        final SStringBuilder styleString = new SStringBuilder();
        appendCSSInlineSize(styleString, component);
        appendCSSComponentInlineColorStyle(styleString, component);
        appendCSSComponentInlineFontStyle(styleString, component);
        return styleString;
    }

    /**
     * Append a inline CSS style definition for the passed component of the aspect foreground- and background color.
     *
     * @param styleString SStringBuilder to append to
     * @param component   Component to use as style source
     * @return The passed styleString
     */
    public static SStringBuilder appendCSSComponentInlineColorStyle(SStringBuilder styleString, final SComponent component) {
        if (component != null) {
            if (component.getBackground() != null) {
                styleString.append("background-color:#").append(toColorString(component.getBackground())).append(";");
            }

            if (component.getForeground() != null) {
                styleString.append("color:#").append(toColorString(component.getForeground())).append(";");
            }
        }
        return styleString;
    }

    /**
     * Append a inline CSS style definition for the passed component of the aspect font properties.
     *
     * @param styleString SStringBuilder to append to
     * @param component   Component to use as style source
     * @return The passed styleString
     */
    public static SStringBuilder appendCSSComponentInlineFontStyle(final SStringBuilder styleString, final SComponent component) {
        if (component != null && component.getFont() != null) {
            final SFont font = component.getFont();
            styleString.append("font-size:").append(font.getSize()).append("pt;");
            styleString.append("font-style:").append((font.getStyle() & SFont.ITALIC) > 0 ? "italic;" : "normal;");
            styleString.append("font-weight:").append((font.getStyle() & SFont.BOLD) > 0 ? "bold;" : "normal;");
            styleString.append("font-family:").append(font.getFace()).append(";");
        }
        return styleString;
    }

    /**
     * Appends a CSS inline style string for the preferred size of the passed component to the passed stringbuffer.
     * <p>Sample: <code>width:100%;heigth=15px"</code>
     */
    public static SStringBuilder appendCSSInlineSize(SStringBuilder styleString, SComponent pComponent) {
        SDimension preferredSize = pComponent.getPreferredSize();
        if (preferredSize != null) {
            //use special expression for IE
            if (Utils.isMSIE(pComponent)) {
                int oversizeHorizontal = getHorizontalOversize(pComponent);
                int oversizeVertical = getVerticalOversize(pComponent);
                if (preferredSize.getWidth() != SDimension.AUTO) {
                    boolean isPercent = preferredSize.getWidthUnit() != null && preferredSize.getWidthUnit().indexOf("%") != -1;
                    boolean isPixel = preferredSize.getWidthUnit() != null && preferredSize.getWidthUnit().indexOf("px") != -1;
                    if (oversizeHorizontal != 0 && isPercent) {
                        // faktor zwischen 0 und 1
                        double factor = Math.max(Math.min(preferredSize.getWidthInt(), 100) / 100.0, 0);
                        // size berechnen anhand des Parents - auf Clientseite
                        styleString.append("width:expression(((this.parentNode.clientWidth-").append(oversizeHorizontal).append(")");
                        if (factor < 1.0) {
                            styleString.append("*").append(factor);
                        }
                        styleString.append(")+'px');");
                    } else if (oversizeHorizontal != 0 && isPixel) {
                        // size darf nicht <0 sein.
                        int size = Math.max(preferredSize.getWidthInt() - oversizeHorizontal, 0);
                        styleString.append("width:").append(size).append("px;");

                    } else {
                        styleString.append("width:").append(preferredSize.getWidth()).append(";");
                    }
                }
                if (preferredSize.getHeight() != SDimension.AUTO) {
                    boolean isPercent = preferredSize.getHeightUnit() != null && preferredSize.getHeightUnit().indexOf("%") != -1;
                    boolean isPixel = preferredSize.getHeightUnit() != null && preferredSize.getHeightUnit().indexOf("px") != -1;
                    if (oversizeVertical != 0 && isPercent) {
                        // faktor zwischen 0 und 1
                        double factor = Math.max(Math.min(preferredSize.getHeightInt(), 100) / 100.0, 0);
                        // size berechnen anhand des Parents - auf Clientseite
                        styleString.append("height:expression(((this.parentNode.clientHeight-").append(oversizeVertical).append(")");
                        if (factor < 1.0) {
                            styleString.append("*").append(factor);
                        }
                        styleString.append(")+'px');");
                    } else if (oversizeVertical != 0 && isPixel) {
                        // size darf nicht <0 sein.
                        int size = Math.max(preferredSize.getHeightInt() - oversizeVertical, 0);
                        styleString.append("height:").append(size).append("px;");

                    } else {
                        styleString.append("height:").append(preferredSize.getHeight()).append(";");
                    }
                }

            } else {
                if (preferredSize.getWidth() != SDimension.AUTO) {
                    styleString.append("width:").append(preferredSize.getWidth()).append(';');
                }
                if (preferredSize.getHeight() != SDimension.AUTO) {
                    styleString.append("height:").append(preferredSize.getHeight()).append(';');
                }
            }
        }
        return styleString;
    }

    /**
     * Helper method to calculate the difference between border-box and content-box mode
     *
     * @param component the component to investigate
     * @return the horizontal oversize
     */
    private static int getVerticalOversize(SComponent component) {
        RenderHelper renderHelper = RenderHelper.getInstance(component);
        int oversize = renderHelper.getVerticalLayoutPadding();

        final SBorder border = component.getBorder();
        if (border != null) {
            oversize += border.getThickness(SConstants.TOP);
            oversize += border.getThickness(SConstants.BOTTOM);
            final Insets insets = border.getInsets();
            if (insets != null) {
                oversize += insets.top + insets.bottom;
            }
        }
        return oversize;
    }

    /**
     * Helper method to calculate the difference between border-box and content-box mode
     *
     * @param component the component to investigate
     * @return the vertical oversize
     */
    private static int getHorizontalOversize(SComponent component) {
        RenderHelper renderHelper = RenderHelper.getInstance(component);
        int oversize = renderHelper.getHorizontalLayoutPadding();

        final SBorder border = component.getBorder();
        if (border != null) {
            oversize += border.getThickness(SConstants.LEFT);
            oversize += border.getThickness(SConstants.RIGHT);
            final Insets insets = border.getInsets();
            if (insets != null) {
                oversize += insets.left + insets.right;
            }
        }
        return oversize;
    }

    public static SStringBuilder generateCSSInlineBorder(SStringBuilder styles, int borderSize) {
        if (borderSize > 0) {
            styles.append("border:").append(borderSize).append("px solid black;");
        } else {
            //styleString.append("border:none;"); Not necessary. Default
        }
        return styles;
    }

    /**
     * Prints a HTML style attribute with widht/height of 100% if the passed dimension defines a height or width..
     * <p>Sample: <code> style="width:100%;"</code>
     * <p/>
     * <p>This is typicall needed to stretch inner HTML element to expand to the full dimenstion defined
     * on an outer, sized HTML element. Otherwise the component would appear to small (as size is applied only
     * on the invisible outer limiting element)
     *
     * @param device        Device to print to
     * @param preferredSize trigger dimension
     */
    public static void printCSSInlineFullSize(Device device, SDimension preferredSize) throws IOException {
        if (preferredSize != null && (preferredSize.getWidth() != SDimension.AUTO || preferredSize.getHeight() != SDimension.AUTO))
        {
            // opera doesn't show height 100% when parent has no defined height
            if (preferredSize.getHeight() != SDimension.AUTO) {
                device.print(" style=\"width:100%;height:100%\"");
            } else {
                device.print(" style=\"width:100%\"");
            }
        }
    }

    /**
     * Prints a HTML style attribute with widht/height of 100% if the passed dimension defines a height or width..
     * <p>Sample: <code> style="width:100%;"</code>
     * <p/>
     * <p>This is typicall needed to stretch inner HTML element to expand to the full dimenstion defined
     * on an outer, sized HTML element. Otherwise the component would appear to small (as size is applied only
     * on the invisible outer limiting element)
     *
     * @param pSStringBuilder buffer to append to
     * @param pComponent      preferredSize trigger dimension
     */
    public static void appendCSSInlineFullSize(SStringBuilder pSStringBuilder, SComponent pComponent) {
        SDimension preferredSize = pComponent.getPreferredSize();
        if (preferredSize != null && (preferredSize.getWidth() != SDimension.AUTO || preferredSize.getHeight() != SDimension.AUTO))
        {
            pSStringBuilder.append("width:100%;height:100%;");
        }
    }

    /**
     * Writes an {X|HT}ML quoted string according to RFC 1866.
     * '"', '<', '>', '&'  become '&quot;', '&lt;', '&gt;', '&amp;'
     *
     * @param d              The device to print out on
     * @param s              the String to print
     * @param quoteNewline   should newlines be transformed into <code>&lt;br&gt;</code> tags
     * @param quoteSpaces    should spaces be transformed into <code>&amp;nbsp</code>  chars
     * @param quoteApostroph Quote apostroph <code>'</code> by <code>\'</code>
     * @throws IOException
     */
    public static void quote(final Device d, final String s, final boolean quoteNewline,
                             final boolean quoteSpaces, final boolean quoteApostroph)
            throws IOException {
        if (s == null) {
            return;
        }
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
            } else {
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
                         *
                         * But we must do this for IE, since it doesn't accept the
                         * white-space: pre; property...so conditionalize it.
                         *                                                       Ole
                         */
                    case ' ':
                        if (quoteSpaces) {
                            d.print(chars, last, (pos - last));
                            d.print("&nbsp;");
                            last = pos + 1;
                        }
                        break;
                        /* Needed for i.e. js-code. */
                    case '\'':
                        if (quoteApostroph) {
                            d.print(chars, last, (pos - last));
                            d.print("\\'");
                            last = pos + 1;
                        }
                        break;

                }
            }
        }
        d.print(chars, last, chars.length - last);
    }

    /**
     * write string as it is
     *
     * @param d
     * @param s
     * @throws IOException
     */
    public static void writeRaw(Device d, String s) throws IOException {
        if (s == null) {
            return;
        }
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
        writeQuoted(d, s, false);
    }


    /**
     * writes the given String to the device. The string is quoted, i.e.
     * for all special characters in *ML, their appropriate entity is
     * returned.
     * If the String starts with '<html>', the content is regarded being
     * HTML-code and is written as is (without the <html> tag).
     * It is possible to define the quoteNewline behavoiur
     */
    public static void writeQuoted(Device d, String s, boolean quoteNewline) throws IOException {
        if (s == null) {
            return;
        }
        if ((s.length() > 5) && (s.startsWith("<html>"))) {
            writeRaw(d, s.substring(6));
        } else {
            quote(d, s, quoteNewline, false, false);
        }
    }

    /**
     * Prints an optional attribute. If the String value has a content
     * (value != null && value.length > 0), the attrib is added otherwise
     * it is left out
     */
    public static void optAttribute(Device d, String attr, SStringBuilder value)
            throws IOException {
        optAttribute(d, attr, value != null ? value.toString() : null);
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
            quote(d, value, true, false, false);
            d.print("\"");
        }
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
     * Prints an empty attribute. I.e. alt=""
     */
    public static void emptyAttribute(Device d, String attr)
            throws IOException {
        if (attr != null) {
            d.print(" ").print(attr).print("=\"\"");
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
        if (r == null) {
            return;
        }
        r.write(d);
    }

    /*
     * testing purposes.
     */
    public static void main(String argv[]) throws Exception {
        Color c = new Color(255, 254, 7);
        Device d = new org.wings.io.StringBuilderDevice();
        write(d, c);
        quote(d, "\nThis is a <abc> string \"; foo & sons\nmoin", true, false, false);
        d.print(String.valueOf(-42));
        d.print(String.valueOf(Integer.MIN_VALUE));

        write(d, "hello test&nbsp;\n");
        write(d, "<html>hallo test&nbsp;\n");

        d = new org.wings.io.NullDevice();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; ++i) {
            quote(d, "this is a little & foo", true, false, false);
        }
        System.out.println("took: " + (System.currentTimeMillis() - start) + "ms");
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
        if (PRINT_DEBUG) {
            return d.print(s);
        } else {
            return NullDevice.DEFAULT;
        }
    }

    /**
     * Prints a hierarchical idented newline if debug mode is enabled.
     * {@link #printNewline(org.wings.io.Device, org.wings.SComponent)}
     */
    public static Device printDebugNewline(Device d, SComponent currentComponent) throws IOException {
        if (PRINT_DEBUG) {
            return printNewline(d, currentComponent);
        } else {
            return d;
        }
    }

    /**
     * Prints a hierarchical idented newline. For each surrounding container of the passed component one ident level.
     */
    public static Device printNewline(final Device d, SComponent currentComponent) throws IOException {
        // special we save every ms handling for holger ;-)
        /* (OL) I took out the test for PRINT_DEBUG, since
         * sometimes we just need newlines (example tabbedPane)
         * I hope Holger doesn't need that microsecond ;)
         */
        if (currentComponent == null) {
            return d;
        }
        d.print("\n");
        while (currentComponent.getParent() != null && currentComponent.getParent().getParent() != null) {
            d.print("\t");
            currentComponent = currentComponent.getParent();
        }
        return d;
    }

    /**
     * Prints a hierarchical idented newline. For each surrounding container of the passed component one ident level.
     */
    public static Device printNewline(final Device d, SComponent currentComponent, int offset) throws IOException {
        if (currentComponent == null || PRINT_DEBUG == false) // special we save every ms handling for holger ;-)
        {
            return d;
        }
        d.print("\n");
        if (PRINT_PRETTY) {
            while (currentComponent.getParent() != null && currentComponent.getParent().getParent() != null) {
                d.print("\t");
                currentComponent = currentComponent.getParent();
            }
        }
        while (offset > 0) {
            d.print("\t");
            offset--;
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
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            reader = new BufferedReader(new InputStreamReader(in));
            SStringBuilder buffer = new SStringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            buffer.append("\n");

            return buffer.toString();
        } catch (Exception e) {
            log.warn("Unable to load script '" + path + "'", e);
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
     */
    public static String nonBreakingSpaces(String text) {
        return text.replace(' ', '\u00A0');
    }


    /**
     * Takes a string, tokenizes it and appends the wordSuffix on each word.
     *
     * @param words      A list of words, may be <code>null</code>.
     * @param wordSuffix A suffix string to append to each word
     * @return modified string (<code>i.e. "slayout myclass","_box"</code>  gets <code>"slayout_box myclass_box"</code>).
     */
    public static String appendSuffixesToWords(String words, String wordSuffix) {
        if (words == null || words.length() == 0 || wordSuffix == null || wordSuffix.length() == 0) {
            return words;
        }

        // trivial case
        if (words.indexOf(" ") < 0) {
            return words + wordSuffix;
        }

        // more than one word
        StringTokenizer tokenizer = new StringTokenizer(words, " ");
        SStringBuilder returnValue = new SStringBuilder();
        while (tokenizer.hasMoreElements()) {
            returnValue.append(tokenizer.nextToken()).append(wordSuffix);
            if (tokenizer.hasMoreTokens()) {
                returnValue.append(" ");
            }
        }

        return returnValue.toString();
    }

    /**
     * Prepends the component style class set on the component to the existing style string.
     *
     * @param component   Component may be <code>null</code> and may have a <code>null</code> style string.
     * @param styleString The style string to append
     */
    public static SStringBuilder joinStyles(final SComponent component, final SStringBuilder styleString) {
        if (component != null && component.getStyle() != null) {
            if (styleString != null) {
                styleString.insert(0, " ");
                styleString.insert(0, component.getStyle());
                return styleString;
            } else {
                return new SStringBuilder(component.getStyle());
            }
        } else {
            return styleString;
        }
    }

    /**
     * Prepends the component style class set on the component to the existing style string.
     *
     * @param component   Component may be <code>null</code> and may have a <code>null</code> style string.
     * @param styleString The style string to append
     */
    public static String joinStyles(final SComponent component, final String styleString) {
        if (component != null && component.getStyle() != null) {
            if (styleString != null) {
                return component.getStyle() + " " + styleString;
            } else {
                return component.getStyle();
            }
        } else {
            return styleString != null ? styleString : "";
        }
    }

    public static void printButtonStart(Device device, SComponent eventTarget, String eventValue, boolean b, boolean showAsFormComponent) throws IOException {
        printButtonStart(device, eventTarget, eventValue, b, showAsFormComponent, null);
    }

    public static void printButtonStart(final Device device, final SComponent eventTarget, final String eventValue,
                                        final boolean enabled, final boolean formComponent, String cssClassName) throws IOException {
        if (formComponent) {
            if (!enabled) {
                device.print("<span");
                Utils.optAttribute(device, "class", cssClassName);
            } else {
                device.print("<a href=\"#\" onclick=\"sendEvent(event,'");
                device.print(eventValue == null ? "" : eventValue);
                device.print("','");
                device.print(Utils.event(eventTarget));
                device.print("'");
                device.print(applyOnClickListeners(eventTarget));
                device.print(")\"");
                Utils.writeEvents(device, eventTarget, new String[]{JavaScriptEvent.ON_CLICK});
                Utils.optAttribute(device, "class", cssClassName);
            }
        } else {
            if (!enabled) {
                device.print("<span");
                Utils.optAttribute(device, "class", cssClassName);
            } else {
                final RequestURL requestURL = eventTarget.getRequestURL();
                if (eventValue != null) {
                    requestURL.addParameter(Utils.event(eventTarget), eventValue);
                }
                device.print("<a href=\"");
                device.print(requestURL.toString());
                device.print("\"");
                Utils.optAttribute(device, "class", cssClassName);

                if (isMSIE(eventTarget)) {
                    device.print(" onclick=\"followLink('").print(requestURL.toString()).print("'");
                    device.print(applyOnClickListeners(eventTarget));
                    device.print(")\"");
                    Utils.writeEvents(device, eventTarget, EXCLUDE_ON_CLICK);
                } else {
                    Utils.writeEvents(device, eventTarget, null);
                }
            }
        }
    }

    public static void printButtonEnd(final Device device, final SComponent button,
                                      final String value, final boolean enabled) throws IOException {
        if (enabled) {
            device.print("</a>");
        } else {
            device.print("</span>");
        }
    }

    /**
     * Renders inline the onclick javascript methods for the <code>sendEvent</code> and <code>followLink</code>
     * method declared in <code>Form.js</code>.
     */
    public static SStringBuilder applyOnClickListeners(final SComponent component) {
        SStringBuilder script = new SStringBuilder();
        JavaScriptListener[] onClickListeners = getOnClickListeners(component);
        if (onClickListeners != null && onClickListeners.length > 0) {
            script.append(",new Array(");
            for (int i = 0; i < onClickListeners.length; i++) {
                if (i > 0) {
                    script.append(",");
                }
                if (onClickListeners[i].getScript() != null) {
                    script.append("function(){").append(onClickListeners[i].getScript()).append("}");
                } else {
                    script.append("function(){").append(onClickListeners[i].getCode()).append("}");
                }
            }
            script.append(")");
        }
        return script;
    }

    private static JavaScriptListener[] getOnClickListeners(final SComponent button) {
        ArrayList result = new ArrayList();
        ScriptListener[] listeners = button.getScriptListeners();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] instanceof JavaScriptListener) {
                JavaScriptListener jsListener = (JavaScriptListener) listeners[i];
                if (JavaScriptEvent.ON_CLICK.equals(jsListener.getEvent().toLowerCase())) {
                    result.add(jsListener);
                }
            }
        }
        return (JavaScriptListener[]) result.toArray(new JavaScriptListener[result.size()]);
    }

    public static SStringBuilder inlineStyles(Style tabAreaStyle) {
        if (tabAreaStyle != null) {
            SStringBuilder tabArea = new SStringBuilder();
            tabArea.append(tabAreaStyle.toString());
            return tabArea;
        } else {
            return null;
        }
    }

    /**
     * @return true if current browser is microsoft exploder
     */
    public static boolean isMSIE(SComponent component) {
        return component.getSession().getUserAgent().getBrowserType() == BrowserType.IE;
    }
}
