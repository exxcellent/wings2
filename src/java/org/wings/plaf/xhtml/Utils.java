/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.plaf.xhtml;

import java.io.IOException;

import java.awt.Color;

import org.wings.*; 
import org.wings.border.*;
import org.wings.style.*;
import org.wings.io.Device;

/**
 * @author Holger Engels
 * @version $Revision$
 */
public final class Utils implements SConstants
{

    final static char[] hexDigits = {
        '0' , '1' , '2' , '3' , '4' , '5' ,
        '6' , '7' , '8' , '9' , 'a' , 'b' ,
        'c' , 'd' , 'e' , 'f'};


    private Utils() {
    }

    public static void writeContainerContents(Device d, SContainer c)
        throws IOException
    {
        SLayoutManager layout = c.getLayout();

        if (layout != null) {
          layout.write(d);
        }
        else {
            for (int i=0; i < c.getComponentCount(); i++)
              c.getComponent(i).write(d);
        }
    }

    public static void writeHiddenComponent(Device d, String name, String value)
        throws IOException
    {
        d.print("<input type=\"hidden\" name=\"")
            .print(name).print("\" value=\"")
            .print(value).print("\" />"); 
    }


    public static String escapeSpecialChars(String text) {
        StringBuffer escaped = new StringBuffer(text.length());
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
            case '&' : escaped.append("&amp;"); break;
            case '<' : escaped.append("&lt;"); break;
            case '>' : escaped.append("&gt;"); break;
            case '"' : escaped.append("&quot;"); break;
            case '\n': escaped.append("<br />"); break; // HTML formatting
            default:   escaped.append(c);
            }
        }
        return escaped.toString();
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

    public static String toColorString(int rgb) {
        char[] buf = new char[6];
        int digits = 6;
        do {
            buf[--digits] = hexDigits[rgb & 15];
            rgb >>>= 4;
        }
        while (digits!=0);

        return new String(buf);
    }

    public static String toColorString(java.awt.Color c) {
        return toColorString(c.getRGB());
    }
    
    public static void printTableToolTip(Device s, SComponent c)
        throws IOException {
        String tooltip = null;
        if ( ( tooltip = c.getToolTipText() ) != null ) {
            s.print(" title=\""+tooltip+"\"");
        }
    }

    public static void printTableCellAlignment(Device s, SComponent c) 
        throws IOException {
          org.wings.plaf.css.Utils.printTableHorizontalAlignment(s, c.getHorizontalAlignment());
          org.wings.plaf.css.Utils.printTableVerticalAlignment(s, c.getVerticalAlignment());
    }

   /** Encolors the actual table cell with the background of the contained component. */
    public static void printTableCellColors(Device s, SComponent c) 
        throws IOException {
/*         if (c.getForeground()!=null)
             s.print(" COLOR=#").print(toColorString(c.getForeground()));
*/
        s.print(" style=\"");
        if (c.getBackground()!=null){
/*           s.print(" bgcolor=\"#")
                .print(toColorString(c.getBackground()))
                .print("\"");
 */
            s.print("background-color:#")
                .print(toColorString(c.getBackground()))
                .print(";"); 
        } 
        if (c.getForeground() != null) {
            s.print("font-color:#").print(toColorString(c.getForeground())).print(";");
            s.print("color:#").print(toColorString(c.getForeground())).print(";");
        }
        s.print("\""); 
    }

    public static void printTableCellSpan(Device s, SComponent c) {
    }


    public static void printTableCellAttributes(Device s, SComponent c) 
        throws IOException {
        printTableCellColors(s, c);
        printTableCellAlignment(s,c);
    }

    public static void printIcon(Device d, SIcon icon, String align) 
        throws IOException {
        if ( icon==null )
            return;

        d.print("<img src=\"");
        icon.getURL().write(d);
        d.print("\"");
        if (align != null)
            d.print(" valign=\"").print(align).print("\"");
        if ( icon.getIconWidth() > 0)
            d.print(" width=\"").print(icon.getIconWidth()).print("\"");
        if ( icon.getIconHeight() > 0)
            d.print(" height=\"").print(icon.getIconHeight()).print("\"");
        d.print(" border=\"0\">");

    }

    public static void printBlindIcon(Device d, SIcon icon, int height, 
                                      int width) throws IOException {
        d.print("<img src=\"").
            print(icon.getURL()).
            print("\"").
            print(" width=\"").print(width).print("\"").
            print(" height=\"").print(height).print("\"").
            print(" border=\"0\">");

    }
    
    
    /**
      * Test, if either background-, foreground color, font or border is set.
      * @return false, if no attribute was set, true otherwise.
      */
    public static boolean hasSpanAttributes(SComponent component) {
        return false;
     }
    
    /**
     * Write all span-attributes except the <i>class</i>.
     * @return null, if not attribute was set, otherwise the attributes 
     *         in css syntax otherwise.
     */
    public static void writeSpanAttributes(Device d, SComponent component)
    	throws IOException
     {
        if (!hasSpanAttributes(component))
           return; 
            
        Utils.writeAttributes(d, component);
     }
    

    /**
     * Write some attributes which are interesting for cellRendere Option Lists !
     * @return null, the attributes  in css syntax 
     */
    public static void writeAttributes(Device d, SComponent component)
    	throws IOException
     {
        java.awt.Color bgcolor = component.getBackground();
        java.awt.Color fgcolor = component.getForeground();
        SFont font = component.getFont();
        SBorder border = component.getBorder();
        SDimension dim = component.getPreferredSize();
        
        if (bgcolor != null) 
            d.print("background-color:#").print(toColorString(bgcolor)).print(";");
        if (fgcolor != null) {
            d.print("font-color:#").print(toColorString(fgcolor)).print(";");
            d.print("color:#").print(toColorString(fgcolor)).print(";");
        }
        
        if (font != null) {
            int style = font.getStyle();
            d.print("font-size:").print(font.getSize()).print("pt;");
            d.print("font-style:").print((style & java.awt.Font.ITALIC) > 0 ?"italic;":"normal;");
            d.print("font-weight:").print((style & java.awt.Font.BOLD) > 0 ?"bold;":"normal;");
            d.print("font-family:").print(font.getFace()).print(";");
        }
        
        if (dim != null) {
            if (dim.width != null) d.print("width:").print(dim.width).print(";");
            if (dim.height != null) d.print("height:").print(dim.height).print(";");
        }
     }

    static String[] alignment = new String[] { " aleft", " aright", " acenter", " ablock", " atop", " abottom", " amiddle" };

    public static void writeAlignment(Device d, SContainer container) throws IOException {
        int verticalAlignment = container.getVerticalAlignment();
        int horizontalAlignment = container.getHorizontalAlignment();
        if (verticalAlignment != -1) {
            d.print(alignment[verticalAlignment]);
        }
        if (horizontalAlignment != -1) {
            d.print(alignment[horizontalAlignment]);
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
