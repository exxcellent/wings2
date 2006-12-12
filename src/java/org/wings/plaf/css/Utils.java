/*
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
import org.wings.*;
import org.wings.border.SAbstractBorder;
import org.wings.border.SDefaultBorder;
import org.wings.externalizer.ExternalizeManager;
import org.wings.header.Link;
import org.wings.header.Script;
import org.wings.io.Device;
import org.wings.io.NullDevice;
import org.wings.resource.ClassPathResource;
import org.wings.resource.DefaultURLResource;
import org.wings.resource.ResourceManager;
import org.wings.script.JavaScriptDOMListener;
import org.wings.script.JavaScriptEvent;
import org.wings.script.JavaScriptListener;
import org.wings.script.ScriptListener;
import org.wings.session.BrowserType;
import org.wings.session.Session;
import org.wings.session.SessionManager;
import org.wings.style.Style;
import org.wings.util.SStringBuilder;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Utils.java
 * <p/>
 * Helper class that collects static methods usable from CGs.
 *
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 */
public final class Utils {
    /**
     * Apache jakarta commons logger
     */
    private static final Log log = LogFactory.getLog(Utils.class);

    /**
     * Print debug information in generated HTML
     */
    public final static boolean PRINT_DEBUG;
    public final static boolean PRINT_PRETTY;

    static {
        Session session = SessionManager.getSession();
        // Respect settings from resource.properties
        Boolean printDebug = (Boolean) ResourceManager.getObject("SComponent.printDebug", Boolean.class);
        Boolean printPretty = (Boolean) ResourceManager.getObject("SComponent.printPretty", Boolean.class);
        // May be overriden in i.e. web.xml. Hopefully we touch the class inside a session for the first time
        if (session != null) {
            if (session.getProperty("SComponent.printDebug") != null)
                printDebug = Boolean.valueOf((String) session.getProperty("SComponent.printDebug"));
            if (session.getProperty("SComponent.printPretty") != null)
                printPretty = Boolean.valueOf((String) session.getProperty("SComponent.printPretty"));
        }
        PRINT_DEBUG = printDebug.booleanValue();
        PRINT_PRETTY = printPretty.booleanValue();
    }

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
     * Default list of javascript events to exlcude in form case of
     * {@link #printButtonStart(org.wings.io.Device, org.wings.SComponent, String, boolean, boolean)}
     */
    private final static String[] EXCLUDE_ON_CLICK_MOUSEUP_MOUSEDOWN_MOUSEOUT = new String[]
            {JavaScriptEvent.ON_CLICK};

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
        }
        else {
            layout.write(d);
        }
    }

    /**
     * Render inline event listeners attached to the passed component exlucding types of suppressed listeners
     *
     * @param device                      output device
     * @param c                           component to retrieve listeners from
     * @param suppressScriptListenerTypes Array of String i.e. <code>new String[] { JavaScriptEvent.ON_CLICK } )</code>
     */
    public static void writeEvents(final Device device, final SComponent c, final String[] suppressScriptListenerTypes)
            throws IOException {
        if (!c.isEnabled())
            return;

        Set types = new HashSet();
        // Create set of strings (in lower case) defining the event types to be suppressed
        if (suppressScriptListenerTypes != null && suppressScriptListenerTypes.length > 0) {
            for (int i = 0; i < suppressScriptListenerTypes.length; i++) {
                types.add(suppressScriptListenerTypes[i].toLowerCase());
            }
        }
        ScriptListener[] listeners = c.getScriptListeners();
        if (listeners.length > 0) {
            Map eventScripts = new HashMap();
            // Fill map with script codes grouped by event type (key)
            for (int i = 0; i < listeners.length; i++) {
                final ScriptListener script = listeners[i];
                if (types.contains(script.getEvent().toLowerCase())) {
                    continue;
                }
                // If its a DOM event we are finished here
                if (script instanceof JavaScriptDOMListener) {
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

            // Print map of script codes grouped by event type (key)
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
        if (component instanceof SClickable)
            return ((SClickable)component).getEventTarget().getEncodedLowLevelEventId();
        else
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
        }
        else if (align == SConstants.LEFT) {
            d.print(" align=\"left\"");
        }
        else if (align == SConstants.CENTER) {
            d.print(" align=\"center\"");
        }
        else if (align == SConstants.RIGHT) {
            d.print(" align=\"right\"");
        }
        else if (align == SConstants.JUSTIFY) {
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
        }
        else if (align == SConstants.CENTER) {
            d.print(" valign=\"middle\"");
        }
        else if (align == SConstants.TOP) {
            d.print(" valign=\"top\"");
        }
        else if (align == SConstants.BOTTOM) {
            d.print(" valign=\"bottom\"");
        }
        else if (align == SConstants.BASELINE) {
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
        }
        while (digits != 0);

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
    public static SStringBuilder appendCSSInlineSize(SStringBuilder styleString, SComponent component) {
        if (component == null)
            return styleString;
        SDimension preferredSize = component.getPreferredSize();
        if (preferredSize != null) {
            boolean msie = isMSIE(component);
            if (msie && "px".equals(preferredSize.getWidthUnit())) {
                int oversize = calculateHorizontalOversize(component, false);
                styleString
                        .append("width:")
                        .append(preferredSize.getWidthInt() - oversize)
                        .append("px;");
            }
            else if (!SDimension.AUTO.equals(preferredSize.getWidthUnit()))
                styleString.append("width:").append(preferredSize.getWidth()).append(';');

            if (msie && "px".equals(preferredSize.getHeightUnit())) {
                int oversize = calculateVerticalOversize(component, false);
                styleString
                        .append("height:")
                        .append(preferredSize.getHeightInt() - oversize)
                        .append("px;");
            }
            else if (!SDimension.AUTO.equals(preferredSize.getHeightUnit()))
                styleString.append("height:").append(preferredSize.getHeight()).append(';');
        }
        return styleString;
    }

    public static SStringBuilder generateCSSInlineBorder(SStringBuilder styles, int borderSize) {
        if (borderSize > 0) {
            styles.append("border:").append(borderSize).append("px solid black;");
        }
        else {
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
            }
            else {
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
                }
                else {
                    d.print("&#");
                    d.print((int) c);
                    d.print(";");
                } // end of if ()
                last = pos + 1;
            }
            else {
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
        }
        else {
            quote(d, s, quoteNewline, false, false);
        }
    }

    /**
     * Prints an <b>optional</b> attribute. If the String value has a content
     * (value != null && value.length > 0), the attrib is added otherwise
     * it is left out
     */
    public static void optAttribute(Device d, String attr, SStringBuilder value)
            throws IOException {
        optAttribute(d, attr, value != null ? value.toString() : null);
    }

    /**
     * Prints an <b>optional</b> attribute. If the String value has a content
     * (value != null && value.length > 0), the attrib is added otherwise
     * it is left out
     */
    public static void optAttribute(Device d, String attr, String value)
            throws IOException {
        if (value != null && value.length() > 0) {
            d.print(" ").print(attr).print("=\"");
            quote(d, value, true, false, false);
            d.print("\"");
        }
    }

    /**
     * Prints an <b>mandatory</b> attribute. If the String value has a content
     * (value != null && value.length > 0), the attrib is added otherwise
     * it is left out
     */
    public static void attribute(Device d, String attr, String value) throws IOException {
            d.print(" ").print(attr).print("=\"");
            if (value != null)
               quote(d, value, true, false, false);
            d.print("\"");
    }

    /**
     * Prints an <b>optional</b> attribute. If the String value has a content
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
     * Prints an <b>optional</b> attribute. If the integer value is greater than 0,
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
     * Prints an <b>optional</b> attribute. If the dimension value not equals <i>null</i>
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
     * Prints all <b>optional</b> attributes that are contained in the
     * <code>Map</code>. The keys of the map should be instances
     * of <code>String</code> and the values one of the following
     * classes.<br/>
     * <ul>
     * <li>org.wings.util.SStringBuilder</li>
     * <li>java.lang.String</li>
     * <li>java.awt.Color</li>
     * <li>org.wings.Renderable</li>
     * <li>java.lang.Integer</li>
     * <li>org.wings.SDimension</li>
     * </ul>
     *
     * @param d          The device to print the <b>optional</b> attributes.
     * @param attributes The <b>optional</b> attributes. The key is the attribute
     *                   name and the value is the attribute value.
     * @throws IOException The exception maybe thrown if an error occurs
     *                     while trying to write to device.
     */
    public static void optAttributes(Device d, Map attributes) throws IOException {
        if (attributes != null) {
            for (Iterator iter = attributes.entrySet().iterator(); iter.hasNext();) {
                Map.Entry entries = (Map.Entry) iter.next();

                Object key = entries.getKey();
                if (key instanceof String) {
                    String attr = (String) key;

                    Object value = entries.getValue();
                    if (value instanceof SStringBuilder) {
                        Utils.optAttribute(d, attr, (SStringBuilder) value);
                    }
                    else if (value instanceof String) {
                        Utils.optAttribute(d, attr, (String) value);
                    }
                    else if (value instanceof Color) {
                        Utils.optAttribute(d, attr, (Color) value);
                    }
                    else if (value instanceof Renderable) {
                        Utils.optAttribute(d, attr, (Renderable) value);
                    }
                    else if (value instanceof Integer) {
                        Utils.optAttribute(d, attr, ((Integer) value).intValue());
                    }
                    else if (value instanceof SDimension) {
                        Utils.optAttribute(d, attr, (SDimension) value);
                    }
                }
            }
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
        }
        else {
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
        }
        else {
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

        if (PRINT_PRETTY) {
            SContainer current = currentComponent.getParent();
            while (current != null) {
                d.print("\t");
                current = current.getParent();
            }
        }
        return d;
    }

    /**
     * Prints a hierarchical idented newline. For each surrounding container of the passed component one ident level.
     */
    public static Device printNewline(final Device d, SComponent currentComponent, int offset) throws IOException {
        if (currentComponent == null) // special we save every ms handling for holger ;-)
            return d;

        d.print("\n");

        if (PRINT_PRETTY) {
            SContainer current = currentComponent.getParent();
            while (current != null) {
                d.print("\t");
                current = current.getParent();
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
        }
        catch (Exception e) {
            log.warn("Unable to load script '" + path + "'", e);
            return "";
        }
        finally {
            try {
                in.close();
            }
            catch (Exception ign) {
            }
            try {
                reader.close();
            }
            catch (Exception ign1) {
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
            }
            else {
                return new SStringBuilder(component.getStyle());
            }
        }
        else {
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
            }
            else {
                return component.getStyle();
            }
        }
        else {
            return styleString != null ? styleString : "";
        }
    }

    public static void printButtonStart(Device device, SComponent eventTarget, String eventValue,
            boolean b, boolean showAsFormComponent) throws IOException {
        printButtonStart(device, eventTarget, eventValue, b, showAsFormComponent, null);
    }

    public static void printButtonStart(final Device device, final SComponent component, final String eventValue,
            final boolean enabled, final boolean formComponent, String cssClassName) throws IOException {
        if (enabled)
            device.print("<a href=\"#\"");
        else
            device.print("<span");

        printClickability(device, component, eventValue, enabled, formComponent);
        Utils.optAttribute(device, "class", cssClassName);
    }

    public static void printButtonEnd(final Device device, final SComponent button, final String value,
            final boolean enabled) throws IOException {
        if (enabled)
            device.print("</a>");
        else
            device.print("</span>");
    }

    public static void printClickability(final Device device, final SComponent component, final String eventValue,
            final boolean enabled, final boolean formComponent) throws IOException {
        if (enabled) {
            boolean ajaxEnabled = !component.isReloadForced();

            // Render onclick JS listeners
            if (formComponent) {
                device.print(" onclick=\"wingS.request.submitForm(" + ajaxEnabled);
                device.print(",event,'");
            } else {
                device.print(" onclick=\"wingS.request.followLink(" + ajaxEnabled);
                device.print(",'");
            }

            device.print(Utils.event(component));
            device.print("','");
            device.print(eventValue == null ? "" : eventValue);
            device.print("'");
            device.print(collectJavaScriptListenerCode(component, JavaScriptEvent.ON_CLICK));
            device.print("); return false;\"");

            // Render remaining JS listeners
            Utils.writeEvents(device, component, EXCLUDE_ON_CLICK);
        }
    }

    /**
     * Renders inline the javascript code attached to the passed javascipt event type
     * on the component. Used to allow usage of javascript events by the framework
     * as well as by the application itself.
     * <p> For an example: See the <code>wingS.request.submitForm</code> and <code>wingS.request.followLink</code>
     * method declared in <code>wings.js</code>.
     *
     * @param component           The component wearing the event handler
     * @param javascriptEventType the event type declared in {@link JavaScriptEvent}
     * @return javascript code fragment n the form of <code>,new Array(function(){...},function(){...})</code>
     */
    public static SStringBuilder collectJavaScriptListenerCode(final SComponent component, final String javascriptEventType) {
        final SStringBuilder script = new SStringBuilder();
        JavaScriptListener[] eventListeners = getEventTypeListeners(component, javascriptEventType);
        if (eventListeners != null && eventListeners.length > 0) {
            for (int i = 0; i < eventListeners.length; ++i) {
                if (eventListeners[i].getCode() != null) {
                    if (i > 0) {
                        script.append(",");
                    }
                    script.append("function(){").append(eventListeners[i].getCode()).append("}");
                }
            }
            if (script.length() > 0) {
                script.insert(0, ",new Array(");
                script.append(")");
            }
        }
        return script;
    }

    /**
     * @param button          The component wearing the event handler
     * @param javaScriptEvent the event type declared in {@link JavaScriptEvent}
     * @return The attached listeners to event type
     */
    private static JavaScriptListener[] getEventTypeListeners(final SComponent button, final String javaScriptEvent) {
        ArrayList result = new ArrayList();
        ScriptListener[] listeners = button.getScriptListeners();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] instanceof JavaScriptListener) {
                JavaScriptListener jsListener = (JavaScriptListener) listeners[i];
                if (javaScriptEvent.equals(jsListener.getEvent().toLowerCase())) {
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
        }
        else {
            return null;
        }
    }

    /**
     * @return true if current browser is microsoft exploder
     */
    public static boolean isMSIE(SComponent component) {
        return component.getSession().getUserAgent().getBrowserType() == BrowserType.IE;
    }

    /**
     * @param insets The inset param to test
     * @return <code>true</code> if any valid inset greater zero is set
     */
    public static boolean hasInsets(Insets insets) {
        return insets != null && (insets.top > 0 || insets.left >0 || insets.right > 0 || insets.bottom > 0);
    }

    public static void optFullSize(Device device, SComponent component) throws IOException {
        SDimension dim = component.getPreferredSize();
        if (dim != null) {
            String width = dim.getWidth();
            boolean widthSet = width != null && !"".equals(width) && !SDimension.AUTO.equals(width);
            String height = dim.getHeight();
            boolean heightSet = height != null && !"".equals(height) && !SDimension.AUTO.equals(height);
            SStringBuilder style = new SStringBuilder();
            if (widthSet) {
                style.append("width:100%;");
            }
            if (heightSet) {
                style.append("height:100%;");
            }
            if (style.length() > 0)
                Utils.optAttribute(device, "style", style.toString());
        }
    }

    /**
     * Converts a hgap/vgap in according inline css padding style.
     *
     * @param insets The insets to generate CSS padding declaration
     * @return Empty or filled stringbuffer with padding declaration
     */
    public static SStringBuilder createInlineStylesForInsets(Insets insets) {
        return createInlineStylesForInsets(new SStringBuilder(), insets);
    }

    /**
     * Converts a hgap/vgap in according inline css padding style.
     *
     * @param styles Appender to append inset style.
     * @param insets The insets to generate CSS padding declaration
     * @return Empty or filled stringbuffer with padding declaration
     */
    public static SStringBuilder createInlineStylesForInsets(SStringBuilder styles, Insets insets) {
        if (insets != null && (insets.top > 0 || insets.left > 0 || insets.right > 0 || insets.bottom > 0)) {
            if (insets.top == insets.left && insets.left == insets.right && insets.right == insets.bottom) {
                styles.append("padding:").append(insets.top).append("px;");
            }
            else if (insets.top == insets.bottom && insets.left == insets.right) {
                styles.append("padding:").append(insets.top).append("px ").append(insets.right).append("px;");
            }
            else {
                styles.append("padding:").append(insets.top).append("px ").append(insets.right).append("px ")
                        .append(insets.bottom).append("px ").append(insets.left).append("px;");
            }
        }
        return styles;
    }

    public static int calculateHorizontalOversize(SComponent component, boolean percentageUnitOnly) {
        if (component != null && isMSIE(component) && component instanceof STextComponent) {
            SDimension preferredSize = component.getPreferredSize();
            if (preferredSize != null) {
                String widthUnit = preferredSize.getWidthUnit();
                if (!SDimension.AUTO.equals(widthUnit)) {
                    if (percentageUnitOnly && !"%".equals(widthUnit))
                        return 0;

                    SAbstractBorder border = (SAbstractBorder) component.getBorder();
                    if (border != SDefaultBorder.INSTANCE) {
                        int oversize = 0;
                        int thickness = border.getThickness(SConstants.LEFT);
                        if (thickness != -1)
                            oversize += thickness;
                        thickness = border.getThickness(SConstants.RIGHT);
                        if (thickness != -1)
                            oversize += thickness;
                        final Insets insets = border.getInsets();
                        if (insets != null) {
                            oversize += insets.left + insets.right;
                        }
                        return oversize;
                    }
                    else {
                        return ((Integer)component.getClientProperty("horizontalOversize")).intValue();
                    }
                }
            }
        }
        return 0;
    }

    public static int calculateVerticalOversize(SComponent component, boolean percentageUnitOnly) {
        if (component != null && isMSIE(component) && component instanceof STextComponent) {
            SDimension preferredSize = component.getPreferredSize();
            if (preferredSize != null) {
                String heightUnit = preferredSize.getHeightUnit();
                if (!SDimension.AUTO.equals(heightUnit)) {
                    if (percentageUnitOnly && !"%".equals(heightUnit))
                        return 0;

                    SAbstractBorder border = (SAbstractBorder) component.getBorder();
                    if (border != SDefaultBorder.INSTANCE) {
                        int oversize = 0;
                        int thickness = border.getThickness(SConstants.TOP);
                        if (thickness != -1)
                            oversize += thickness;
                        thickness = border.getThickness(SConstants.BOTTOM);
                        if (thickness != -1)
                            oversize += thickness;
                        final Insets insets = border.getInsets();
                        if (insets != null) {
                            oversize += insets.top + insets.bottom;
                        }
                        return oversize;
                    }
                    else {
                        return 4;
                    }
                }
            }
        }
        return 0;
    }

    public static Script createExternalizedJavaScriptHeader(Session session, String classPath) {
        ClassPathResource res = new ClassPathResource(classPath, "text/javascript");
        String jScriptUrl = session.getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        return new Script("text/javascript", new DefaultURLResource(jScriptUrl));
    }

    public static Link createExternalizedCSSHeader(Session session, String classPath) {
        ClassPathResource res = new ClassPathResource(classPath, "text/css");
        String cssUrl = session.getExternalizeManager().externalize(res, ExternalizeManager.GLOBAL);
        return new Link("stylesheet", null, "text/css", null, new DefaultURLResource(cssUrl));
    }
}
