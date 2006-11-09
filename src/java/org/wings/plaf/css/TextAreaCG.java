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
import org.wings.script.JavaScriptEvent;
import org.wings.script.JavaScriptListener;

import java.io.IOException;

public final class TextAreaCG extends AbstractComponentCG implements
        org.wings.plaf.TextAreaCG {

    private static final long serialVersionUID = 1L;

    int horizontalOversize = 4;

    public int getHorizontalOversize() {
        return horizontalOversize;
    }

    public void setHorizontalOversize(int horizontalOversize) {
        this.horizontalOversize = horizontalOversize;
    }

    public void installCG(SComponent component) {
        super.installCG(component);
        if (isMSIE(component))
            component.putClientProperty("horizontalOversize", new Integer(horizontalOversize));
    }

    public void writeInternal(final Device device,
                      final SComponent _c)
            throws IOException {
        final STextArea component = (STextArea) _c;

        Object clientProperty = component.getClientProperty("onChangeSubmitListener");
        // If the application developer attached any SDocumentListeners to this
    	// STextArea, the surrounding form gets submitted as soon as the content
        // of this STextArea changed.
        if (component.getDocumentListeners().length > 1) {
        	// We need to test if there are at least 2 document
        	// listeners because each text component registers
        	// itself as a listener of its document as well.
            if (clientProperty == null) {
            	String event = JavaScriptEvent.ON_CHANGE;
            	String code = "wingS.request.submitForm(" + !component.isCompleteUpdateForced() + ",event);";
                JavaScriptListener javaScriptListener = new JavaScriptListener(event, code);
                component.addScriptListener(javaScriptListener);
                component.putClientProperty("onChangeSubmitListener", javaScriptListener);
            }
        } else if (clientProperty != null && clientProperty instanceof JavaScriptListener) {
        	component.removeScriptListener((JavaScriptListener) clientProperty);
        	component.putClientProperty("onChangeSubmitListener", null);
        }

        /*
         * a swing like way to write multiline labels
         */
        if (!component.isEditable() && (component.getLineWrap() == STextArea.NO_WRAP) && (component.getColumns() == 0) && (component.getRows() == 0)) {
               /* A second way could be to calculate rows and columns and generate a textarea, but this will be
                * very time consuming at large texts. But if this way makes to much trouble, the other will be quite equal */
            String text = component.getText();
            if (text != null) {
                device.print("<nobr>");               /* Should we really ignore everything ? */
                Utils.writeQuoted(device,text,true);    /* Write new text */
                device.print("</nobr>");
            }

        } else {
            SDimension preferredSize = component.getPreferredSize();
            boolean tableWrapping = Utils.isMSIE(component) && preferredSize != null && "%".equals(preferredSize.getWidthUnit());
            String actualWidth = null;
            if (tableWrapping) {
                actualWidth = preferredSize.getWidth();
                preferredSize.setWidth("100%");
                device.print("<table style=\"table-layout: fixed; width: " + actualWidth + "\"><tr>");
                device.print("<td style=\"padding-right: " + Utils.calculateHorizontalOversize(component, true) + "px\">");
            }
            device.print("<textarea");
            writeAllAttributes(device, component);
            if (tableWrapping)
                device.print(" wrapping=\"4\"");

            Utils.optAttribute(device, "tabindex", component.getFocusTraversalIndex());
            Utils.optAttribute(device, "cols", component.getColumns());
            Utils.optAttribute(device, "rows", component.getRows());
            Utils.writeEvents(device, component, null);

            switch (component.getLineWrap()) {
                case STextArea.VIRTUAL_WRAP:
                    device.print(" wrap=\"virtual\"");
                    break;
                case STextArea.PHYSICAL_WRAP:
                    device.print(" wrap=\"physical\"");
                    break;
            }

            if (!component.isEditable() || !component.isEnabled()) {
                device.print(" readonly=\"true\"");
            }

            if (component.isEnabled()) {
                device.print(" name=\"");
                Utils.write(device, Utils.event(component));
                device.print("\"");
            } else {
                device.print(" disabled=\"true\"");
            }

            if (component.isFocusOwner())
                Utils.optAttribute(device, "foc", component.getName());

            device.print(">");
            Utils.quote(device, component.getText(), false, false, false);
            device.print("</textarea>\n");
            if (tableWrapping) {
                preferredSize.setWidth(actualWidth);
                device.print("</td></tr></table>");
            }
        }
    }
}
