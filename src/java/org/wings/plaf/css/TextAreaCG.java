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
package org.wings.plaf.css;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.*;
import org.wings.io.Device;
import org.wings.plaf.Update;
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
            	String code = "wingS.request.sendEvent(event, true, " + !component.isReloadForced() + ");";
                JavaScriptListener javaScriptListener = new JavaScriptListener(event, code);
                component.addScriptListener(javaScriptListener);
                component.putClientProperty("onChangeSubmitListener", javaScriptListener);
            }
        } else if (clientProperty != null && clientProperty instanceof JavaScriptListener) {
        	component.removeScriptListener((JavaScriptListener) clientProperty);
        	component.putClientProperty("onChangeSubmitListener", null);
        }

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

    public Update getTextUpdate(STextArea textArea, String text) {
        return new TextUpdate(textArea, text);
    }

    protected class TextUpdate extends AbstractUpdate {

        private String text;

        public TextUpdate(SComponent component, String text) {
            super(component);
            this.text = text;
        }

        public Handler getHandler() {
            String exception = null;

            UpdateHandler handler = new UpdateHandler("value");
            handler.addParameter(component.getName());
            handler.addParameter(text == null ? "" : text);
            if (exception != null) {
                handler.addParameter(exception);
            }
            return handler;
        }

    }

}
