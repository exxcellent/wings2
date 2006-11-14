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

import org.wings.*;
import org.wings.io.Device;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.awt.*;

/**
 * @author hengels
 */
public abstract class IconTextCompound {
    protected static final Log log = LogFactory.getLog(IconTextCompound.class);

    public void writeCompound(Device device, SComponent component, int horizontalTextPosition,
                              int verticalTextPosition, boolean writeAllAttributes) throws IOException {
        RenderHelper.getInstance(component).collectMenues(component);

        if (horizontalTextPosition == SConstants.NO_ALIGN)
            horizontalTextPosition = SConstants.RIGHT;
        if (verticalTextPosition == SConstants.NO_ALIGN)
            verticalTextPosition = SConstants.CENTER;
        if (verticalTextPosition == SConstants.CENTER && horizontalTextPosition == SConstants.CENTER)
            horizontalTextPosition = SConstants.RIGHT;
        int iconTextGap = getIconTextGap(component);

        boolean renderTextFirst = verticalTextPosition == SConstants.TOP ||
                (verticalTextPosition == SConstants.CENTER && horizontalTextPosition == SConstants.LEFT);

        device.print("<table");
        tableAttributes(device);
        device.print(">");

        if (verticalTextPosition == SConstants.TOP && horizontalTextPosition == SConstants.LEFT ||
                verticalTextPosition == SConstants.BOTTOM && horizontalTextPosition == SConstants.RIGHT) {
            final Insets insets = new Insets(0, 0, iconTextGap, iconTextGap);
            device.print("<tr><td align=\"left\" valign=\"top\"");
            Utils.optAttribute(device, "style", Utils.createInlineStylesForInsets(insets));
            device.print(">");
            first(device, renderTextFirst);
            device.print("</td><td></td></tr>");
            device.print("<tr><td></td><td align=\"right\" valign=\"bottom\">");
            last(device, renderTextFirst);
            device.print("</td></tr>");
        } else if (verticalTextPosition == SConstants.TOP && horizontalTextPosition == SConstants.RIGHT ||
                verticalTextPosition == SConstants.BOTTOM && horizontalTextPosition == SConstants.LEFT) {
            final Insets insets = new Insets(0, iconTextGap, iconTextGap, 0);
            device.print("<tr><td></td><td align=\"right\" valign=\"top\"");
            Utils.optAttribute(device, "style", Utils.createInlineStylesForInsets(insets));
            device.print(">");
            first(device, renderTextFirst);
            device.print("</td></tr><tr><td align=\"left\" valign=\"bottom\">");
            last(device, renderTextFirst);
            device.print("</td><td></td></tr>");
        } else if (verticalTextPosition == SConstants.TOP && horizontalTextPosition == SConstants.CENTER ||
                verticalTextPosition == SConstants.BOTTOM && horizontalTextPosition == SConstants.CENTER) {
            final Insets insets = new Insets(0, 0, iconTextGap, 0);
            device.print("<tr><td align=\"center\" valign=\"top\"");
            Utils.optAttribute(device, "style", Utils.createInlineStylesForInsets(insets));
            device.print(">");
            first(device, renderTextFirst);
            device.print("</td></tr><tr><td align=\"center\" valign=\"bottom\">");
            last(device, renderTextFirst);
            device.print("</td></tr>");
        } else if (verticalTextPosition == SConstants.CENTER && horizontalTextPosition == SConstants.LEFT ||
                verticalTextPosition == SConstants.CENTER && horizontalTextPosition == SConstants.RIGHT) {
            final Insets insets = new Insets(0, 0, 0, iconTextGap);
            device.print("<tr><td align=\"left\"");
            Utils.optAttribute(device, "style", Utils.createInlineStylesForInsets(insets));
            device.print(">");
            first(device, renderTextFirst);
            device.print("</td><td align=\"right\">");
            last(device, renderTextFirst);
            device.print("</td></tr>");
        } else {
            log.warn("horizontal = " + horizontalTextPosition);
            log.warn("vertical = " + verticalTextPosition);
        }
        device.print("</table>");
    }

    protected int getIconTextGap(SComponent component) {
        if (component instanceof SLabel)
            return ((SLabel)component).getIconTextGap();
        if (component instanceof SAbstractButton)
            return ((SAbstractButton)component).getIconTextGap();
        return 4;
    }

    protected void first(Device device, boolean textFirst) throws IOException {
        if (textFirst) {
            // avoid that in case that horizontalAlignment of text is right, that the text itself
            // becomes right-aligned
            device.print("<div style=\"text-align:left\">");
            text(device);
            device.print("</div>");
        } else
            icon(device);
    }

    protected void last(Device device, boolean textFirst) throws IOException {
        first(device, !textFirst);
    }

    protected abstract void text(Device d) throws IOException;

    protected abstract void icon(Device d) throws IOException;

    protected abstract void tableAttributes(Device d) throws IOException;
}
