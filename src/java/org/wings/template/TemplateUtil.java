/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.template;

import org.wings.SFont;
import org.wings.SConstants;

import java.util.StringTokenizer;

/**
 * Provides util methods for {@link org.wings.STemplateLayout} related implementation. 
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 */
public class TemplateUtil {

    private TemplateUtil() {
    }

    /**
     * Parses a font description in the format of <code>Arial,B,14</code>.
     * @param value A free text representation of a font in the format of <code>Arial,B,14</code>.
     * @return according SFont instance. Defaults to plain 12.
     */
    public static SFont parseFont(String value) {
        StringTokenizer s = new StringTokenizer(value, ",");
        String fontName = s.nextToken();
        String tmpFontType = s.nextToken().toUpperCase().trim();
        int fontType = SFont.PLAIN;
        if (tmpFontType.startsWith("B"))
            fontType = SFont.BOLD;
        else if (tmpFontType.startsWith("I"))
            fontType = SFont.ITALIC;

        int fontSize = 12;
        try {
            fontSize = Integer.parseInt(s.nextToken());
        } catch (Exception e) {}

        return new SFont(fontName, fontType, fontSize);
    }

    /**
     * Parses a free text string representation for alignments and returns
     * the according value.
     * @param alignmentValue A String like "Top" or "Center"
     * @return Alignment constant value or {@link SConstants#NO_ALIGN}
     */
    public static int parseAlignment(String alignmentValue) {
        if ("TOP".equalsIgnoreCase(alignmentValue))
            return SConstants.TOP_ALIGN;
        else if ("CENTER".equalsIgnoreCase(alignmentValue) ||
                "MIDDLE".equalsIgnoreCase(alignmentValue))
            return SConstants.CENTER_ALIGN;
        else if ("BOTTOM".equalsIgnoreCase(alignmentValue))
            return SConstants.BOTTOM_ALIGN;
        else if ("LEFT".equalsIgnoreCase(alignmentValue))
            return SConstants.LEFT_ALIGN;
        else if ("RIGHT".equalsIgnoreCase(alignmentValue))
            return SConstants.RIGHT_ALIGN;
        else
            return SConstants.NO_ALIGN;
    }

}

