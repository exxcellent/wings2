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
import org.wings.SCardLayout;
import org.wings.SComponent;
import org.wings.SDimension;
import org.wings.SLayoutManager;
import org.wings.SNullLayout;
import org.wings.STemplateLayout;
import org.wings.SConstants;
import org.wings.border.STitledBorder;
import org.wings.dnd.DragSource;
import org.wings.io.Device;
import org.wings.plaf.css.AbstractLayoutCG;
import org.wings.plaf.css.Utils;
import java.io.IOException;

/**
 * This class surrounds every component in MSIE with a TABLE element
 * wearing the components id. (for styling)
 *
 * @author ole
 */
public final class PrefixAndSuffixDelegate extends org.wings.plaf.css.PrefixAndSuffixDelegate {

    public PrefixAndSuffixDelegate() {
    }

    /* non-javadoc: wraps every component into a TABLE element for MSIE. */
    public void writePrefix(final Device device, final SComponent component) throws IOException {
        SDimension prefSize = component.getPreferredSize();

        // --- This is a BAD idea: It streches compoennts which shouldnt be streched.
        // --- Positioning should be always a task of the surrounding layout manager
        // For centering or right-alignment we need we surrounding helper table to be stretched to full width in some cases.
        //if (componentAlignmentRequiresStretchedWrapper(component))
        //    prefSize = SDimension.FULLWIDTH;

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

        // Workaround for components i.e. inside root container
        if (handlingLayoutManagerDoesNotSupportAlignment(component)) {
            Utils.printTableCellAlignment(device, component, SConstants.TOP, SConstants.LEFT);
        }

        // Tooltip handling
        writeTooltipMouseOver(device, component);

        // Component popup menu
        writeContextMenu(device, component);

        device.print("><tr>"); // table
        AbstractLayoutCG.openLayouterCell(device, false, null, 0, component);
        // the following TD id is needed for the MSIE padding workaround! Refer to CSSStyleSheetWriter.
        device.print(" id=\"").print(component.getName()).print("_td\"");
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

    public void writeSuffix(final Device device, final SComponent component) throws IOException {
        component.fireRenderEvent(SComponent.DONE_RENDERING);
        AbstractLayoutCG.closeLayouterCell(device, false);
        device.print("</tr></table>");
        Utils.printDebug(device, "<!-- /").print(component.getName()).print(" -->");
    }


    /* *
     * If a component should be centered/right-aligned we need its surrounding cell to be stretched
     * to full width, so that we can render this alignment within the wrapper of the component itself.
     *
     * But for some surrounding layouts we can't to this not to break the functionality.
     * /
    private boolean componentAlignmentRequiresStretchedWrapper(final SComponent component) {
        final SDimension prefSize = component.getPreferredSize();
        final boolean isNotLeftAligned = (component.getHorizontalAlignment() == SConstants.CENTER ||
                    component.getHorizontalAlignment() == SConstants.RIGHT);
        final SLayoutManager containingManager = component.getParent() != null ? component.getParent().getLayout() : null;

        return prefSize == null && isNotLeftAligned && !(containingManager instanceof SFlowLayout);
    } */

    /* Componentns should be (horizontally) aligned by the surrounding layout manager.
       Some layout managers will not be able to do this. */
    private boolean handlingLayoutManagerDoesNotSupportAlignment(final SComponent component) {
        if (component == null || component.getParent() == null)
            return true;

        boolean alignmentIncapable = false;
        final SLayoutManager layoutManager = component.getParent().getLayout();
        alignmentIncapable |= layoutManager == null;
        alignmentIncapable |= layoutManager instanceof STemplateLayout;
        alignmentIncapable |= layoutManager instanceof SNullLayout;
        alignmentIncapable |= layoutManager instanceof SCardLayout;
        return alignmentIncapable;
    }

}
