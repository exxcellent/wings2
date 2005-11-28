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
package org.wings.plaf.css.msie;

import org.wings.LowLevelEventListener;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDimension;
import org.wings.SLayoutManager;
import org.wings.SFlowLayout;
import org.wings.border.STitledBorder;
import org.wings.dnd.DragSource;
import org.wings.io.Device;
import org.wings.plaf.css.AbstractLayoutCG;
import org.wings.plaf.css.Utils;
import java.io.IOException;

/**
 * @author ole
 */
public class PrefixAndSuffixDelegate extends org.wings.plaf.css.PrefixAndSuffixDelegate {

    public PrefixAndSuffixDelegate() {
    }

    /* non-javadoc: wraps every component into a TABLE element for MSIE. */
    public void writePrefix(Device device, SComponent component) throws IOException {
        SDimension prefSize = component.getPreferredSize();

        // For centering or right-alignment we need we surrounding helper table to be stretched to full width in some cases.
        if (componentAlignmentRequiresStretchedWrapper(component))
            prefSize = SDimension.FULLWIDTH;

        StringBuffer inlineStyles = Utils.generateCSSInlinePreferredSize(prefSize);
        Utils.appendCSSComponentInlineColorStyle(inlineStyles, component);

        Utils.printDebugNewline(device, component);
        Utils.printDebug(device, "<!-- ").print(component.getName()).print(" -->");
        
        device.print("<table id=\"").print(component.getName()).print("\"");
        // Special handling: Mark Titled Borders for styling
        if (component.getBorder() instanceof STitledBorder) {
            Utils.optAttribute(device, "class", component.getStyle() + " STitledBorder SContainer");
        } else {
            Utils.optAttribute(device, "class", component.getStyle() + " SContainer");
        }
        if (component instanceof DragSource) {
            inlineStyles.append("position:relative;");
        }
        Utils.optAttribute(device, "style", inlineStyles.toString());

        if (component instanceof LowLevelEventListener) {
            LowLevelEventListener lowLevelEventListener = (LowLevelEventListener) component;
            device.print(" eid=\"")
                    .print(lowLevelEventListener.getEncodedLowLevelEventId()).print("\"");
        }

        // Tooltip handling
        writeTooltipMouseOver(device, component);

        // Key bindings
        handleKeyBindings(component);

        // Component popup menu
        writeContextMenu(device, component);

        device.print("><tr>"); // table
        AbstractLayoutCG.openLayouterCell(device, false, 0, 0, 0, component);
        device.print(">");

        // Special handling: Render title of STitledBorder
        // TODO Attention - This may break CSS selectors as a new element is introduced inbetween.
        // Maybe move out of DIV with the componentn ID.
        if (component.getBorder() instanceof STitledBorder) {
            STitledBorder titledBorder = (STitledBorder) component.getBorder();
            device.print("<div class=\"STitledBorderLegend\" style=\"");
            titledBorder.getTitleAttributes().write(device);
            device.print("\">");
            device.print(titledBorder.getTitle());
            device.print("</div>");
        }

        component.fireRenderEvent(SComponent.START_RENDERING);
    }

    public void writeSuffix(Device device, SComponent component) throws IOException {
        component.fireRenderEvent(SComponent.DONE_RENDERING);
        AbstractLayoutCG.closeLayouterCell(device, false);
        device.print("</tr></table>");
        Utils.printDebug(device, "<!-- /").print(component.getName()).print(" -->");
    }


    /**
     * If a component should be centered/right-aligned we need its surrounding cell to be stretched
     * to full width, so that we can render this alignment within the wrapper of the component itself.
     *
     * But for some surrounding layouts we can't to this not to break the functionality.
     */
    private boolean componentAlignmentRequiresStretchedWrapper(SComponent component) {
        final SDimension prefSize = component.getPreferredSize();
        final boolean isNotLeftAligned = (component.getHorizontalAlignment() == SConstants.CENTER ||
                    component.getHorizontalAlignment() == SConstants.RIGHT);
        final SLayoutManager containingManager = component.getParent() != null ? component.getParent().getLayout() : null;

        return prefSize == null && isNotLeftAligned && !(containingManager instanceof SFlowLayout);
    }

}
