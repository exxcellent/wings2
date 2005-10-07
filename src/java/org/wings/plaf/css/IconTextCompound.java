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

import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDimension;
import org.wings.io.Device;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * @author hengels
 * @version $Revision$
 */
public abstract class IconTextCompound {
    private final static transient Log log = LogFactory.getLog(IconTextCompound.class);

    public void writeCompound(Device device, SComponent component, int horizontalTextPosition, int verticalTextPosition) throws IOException {
        // by default: text right side of icon
        if (horizontalTextPosition == SConstants.NO_ALIGN)
            horizontalTextPosition = SConstants.RIGHT;
        // by default: text center aligned in height with icon
        if (verticalTextPosition == SConstants.NO_ALIGN)
            verticalTextPosition = SConstants.CENTER;
        boolean renderTextFirst = verticalTextPosition == SConstants.TOP ||
                (verticalTextPosition == SConstants.CENTER && horizontalTextPosition == SConstants.LEFT);


        device.print("<table class=\"SLayout\"");
        SDimension prefSize = component.getPreferredSize();
        if (prefSize != null && (prefSize.getWidth() != null || prefSize.getHeight() != null)) {
            device.print(" style=\"width:100%;height:100%\"");
        }
        device.print(">");

        if (verticalTextPosition == SConstants.TOP && horizontalTextPosition == SConstants.LEFT ||
                verticalTextPosition == SConstants.BOTTOM && horizontalTextPosition == SConstants.RIGHT) {
            device.print("<tr><td align=\"left\" valign=\"top\" class=\"SLayout\">");
            first(device, renderTextFirst);
            device.print("</td><td class=\"SLayout\"></td></tr>");
            device.print("<tr><td class=\"SLayout\"></td><td align=\"right\" valign=\"bottom\" class=\"SLayout\">");
            last(device, renderTextFirst);
            device.print("</td></tr>");
        } else if (verticalTextPosition == SConstants.TOP && horizontalTextPosition == SConstants.RIGHT ||
                verticalTextPosition == SConstants.BOTTOM && horizontalTextPosition == SConstants.LEFT) {
            device.print("<tr><td class=\"SLayout\"></td><td align=\"right\" valign=\"top\" class=\"SLayout\">");
            first(device, renderTextFirst);
            device.print("</td></tr><tr><td align=\"left\" valign=\"bottom\" class=\"SLayout\">");
            last(device, renderTextFirst);
            device.print("</td><td class=\"SLayout\"></td></tr>");
        } else if (verticalTextPosition == SConstants.TOP && horizontalTextPosition == SConstants.CENTER ||
                verticalTextPosition == SConstants.BOTTOM && horizontalTextPosition == SConstants.CENTER) {
            device.print("<tr><td align=\"center\" valign=\"top\" class=\"SLayout\">");
            first(device, renderTextFirst);
            device.print("</td></tr><tr><td align=\"center\" valign=\"bottom\" class=\"SLayout\">");
            last(device, renderTextFirst);
            device.print("</td></tr>");
        } else if (verticalTextPosition == SConstants.CENTER && horizontalTextPosition == SConstants.LEFT ||
                verticalTextPosition == SConstants.CENTER && horizontalTextPosition == SConstants.RIGHT) {
            device.print("<tr><td align=\"left\" class=\"SLayout\">");
            first(device, renderTextFirst);
            device.print("</td><td align=\"right\" class=\"SLayout\">");
            last(device, renderTextFirst);
            device.print("</td></tr>");
        } else {
            log.warn("horizontal = " + horizontalTextPosition);
            log.warn("vertical = " + verticalTextPosition);
        }
        device.print("</table>");
    }

    private void first(Device device, boolean textFirst) throws IOException {
        if (textFirst) {
            // avoid that in case that horizontalAlignment of text is right, that the text itself
            // becomes right-aligned
            device.print("<div style=\"text-align:left\">");           
            text(device);
            device.print("</div>");
        } else
            icon(device);
    }

    private void last(Device device, boolean textFirst) throws IOException {
        first(device, !textFirst);
    }

    protected abstract void text(Device d) throws IOException;

    protected abstract void icon(Device d) throws IOException;
}
