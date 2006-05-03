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
import org.wings.SDimension;
import org.wings.style.*;
import org.wings.border.STitledBorder;
import org.wings.border.SBorder;
import org.wings.dnd.DragSource;
import org.wings.io.Device;
import org.wings.io.SStringBuilder;
import org.wings.plaf.css.AbstractLayoutCG;
import org.wings.plaf.css.Utils;
import java.io.IOException;
import java.util.*;

/**
 * This class surrounds every component in MSIE with a TABLE element
 * wearing the components id. (for styling)
 *
 * @author ole
 */
public final class PrefixAndSuffixDecorator extends org.wings.plaf.css.PrefixAndSuffixDecorator {

    private static final long serialVersionUID = 1L;

    public PrefixAndSuffixDecorator() {
    }

    /* non-javadoc: wraps every component into a TABLE element for MSIE. */
    public void writePrefix(final Device device, final SComponent component) throws IOException {
        final boolean isTitleBorder = component.getBorder() instanceof STitledBorder;
        SDimension prefSize = component.getPreferredSize();

        // --- This is a BAD idea: It streches compoennts which shouldnt be streched.
        // --- Positioning should be always a task of the surrounding layout manager
        // For centering or right-alignment we need we surrounding helper table to be stretched to full width in some cases.
        //if (componentAlignmentRequiresStretchedWrapper(component))
        //    prefSize = SDimension.FULLWIDTH;

        Utils.printDebugNewline(device, component);
        Utils.printDebug(device, "<!-- ").print(component.getName()).print(" -->");

        device.print("<div");
        final String classname = component.getStyle();
        Utils.optAttribute(device, "class", isTitleBorder ? classname + " STitledBorder" : classname);
        Utils.optAttribute(device, "id", component.getName());
        // Special handling: Mark Titled Borders for styling

        Utils.optAttribute(device, "style", getInlineStyles(component));

        if (component instanceof LowLevelEventListener) {
            LowLevelEventListener lowLevelEventListener = (LowLevelEventListener) component;
            device.print(" eid=\"")
                    .print(lowLevelEventListener.getEncodedLowLevelEventId()).print("\"");
        }

        // Workaround for components i.e. inside root container
        //if (handlingLayoutManagerDoesNotSupportAlignment(component)) {
            // Bad idea: Table align=left/right converts the table into a floating element!
            //MSIEUtils.printTableCellAlignment(device, component, SConstants.TOP, SConstants.LEFT);
        //}

        // Tooltip handling
        writeTooltipMouseOver(device, component);

        // Component popup menu
        writeContextMenu(device, component);

        device.print(">"); // div

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
        writeInlineScripts(device, component);
        device.print("</div>");
        Utils.printDebug(device, "<!-- /").print(component.getName()).print(" -->");
    }

    protected String getInlineStyles(SComponent component) {
        SDimension prefSize = component.getPreferredSize();
        SStringBuilder inlineStyles = Utils.generateCSSInlinePreferredSize(new SStringBuilder(), prefSize, component.getOversize(true), component.getOversize(false));
        if (component instanceof DragSource)
            inlineStyles.append("position:relative;");
        //Utils.appendCSSComponentInlineColorStyle(inlineStyles, component);
        Style allStyle = component.getDynamicStyle(SComponent.SELECTOR_ALL);
        if (allStyle != null)
            inlineStyles.append(allStyle.toString());
        SBorder border = component.getBorder();
        if (border != null && border.getAttributes() != null)
            inlineStyles.append(border.getAttributes().toString());
        return inlineStyles.toString();
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
    /*private boolean handlingLayoutManagerDoesNotSupportAlignment(final SComponent component) {
        if (component == null || component.getParent() == null)
            return true;

        boolean alignmentIncapable = false;
        final SLayoutManager layoutManager = component.getParent().getLayout();
        alignmentIncapable |= layoutManager == null;
        alignmentIncapable |= layoutManager instanceof STemplateLayout;
        alignmentIncapable |= layoutManager instanceof SNullLayout;
        alignmentIncapable |= layoutManager instanceof SCardLayout;
        return alignmentIncapable;
    } */

}
