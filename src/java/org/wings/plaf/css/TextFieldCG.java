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
import org.wings.text.SAbstractFormatter;
import org.wings.io.Device;
import org.wings.plaf.Update;
import org.wings.plaf.css.dwr.CallableManager;
import org.wings.script.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class TextFieldCG extends AbstractComponentCG implements
        org.wings.plaf.TextFieldCG {

    private static final long serialVersionUID = 1L;

    private CallableFormatter callableFormatter = new CallableFormatter();
    int horizontalOversize = 4;

    public int getHorizontalOversize() {
        return horizontalOversize;
    }

    public void setHorizontalOversize(int horizontalOversize) {
        this.horizontalOversize = horizontalOversize;
    }

    public void installCG( SComponent comp ) {
        super.installCG( comp );
        if ( comp instanceof SFormattedTextField ) {
            if (!CallableManager.getInstance().containsCallable(callableFormatter.getName())) {
                CallableManager.getInstance().registerCallable(callableFormatter.getName(), callableFormatter);
            }
            comp.addScriptListener(new JavaScriptListener(JavaScriptEvent.ON_BLUR, "this.style.color = '';" +
                    "CallableFormatter.validate(wingS.component.ftextFieldCallback, this.getAttribute('formatter'), this.id, this.value, this.getAttribute('lastValue'))"));
        }
        if (isMSIE(comp))
            comp.putClientProperty("horizontalOversize", new Integer(horizontalOversize));
    }

    public void writeInternal(final Device device,
                              final SComponent component)
            throws IOException {
        final STextField textField = (STextField) component;

        Object clientProperty = textField.getClientProperty("onChangeSubmitListener");
        // If the application developer attached any SDocumentListeners to this
    	// STextField, the surrounding form gets submitted as soon as the content
        // of this STextField changed.
        if (textField.getDocumentListeners().length > 1) {
        	// We need to test if there are at least 2 document
        	// listeners because each text component registers
        	// itself as a listener of its document as well.
            if (clientProperty == null) {
            	String event = JavaScriptEvent.ON_CHANGE;
            	String code = "wingS.request.submitForm(" + !textField.isReloadForced() + ",event);";
                JavaScriptListener javaScriptListener = new JavaScriptListener(event, code);
                textField.addScriptListener(javaScriptListener);
                textField.putClientProperty("onChangeSubmitListener", javaScriptListener);
            }
        } else if (clientProperty != null && clientProperty instanceof JavaScriptListener) {
        	textField.removeScriptListener((JavaScriptListener) clientProperty);
        	textField.putClientProperty("onChangeSubmitListener", null);
        }

        SDimension preferredSize = component.getPreferredSize();
        boolean tableWrapping = Utils.isMSIE(component) && preferredSize != null && "%".equals(preferredSize.getWidthUnit());
        String actualWidth = null;
        if (tableWrapping) {
            actualWidth = preferredSize.getWidth();
            preferredSize.setWidth("100%");
            device.print("<table style=\"table-layout: fixed; width: " + actualWidth + "\"><tr>");
            device.print("<td style=\"padding-right: " + Utils.calculateHorizontalOversize(textField, true) + "px\">");
        }

        device.print("<input type=\"text\"");
        if (tableWrapping)
            device.print(" wrapping=\"4\"");

        Utils.optAttribute(device, "tabindex", textField.getFocusTraversalIndex());
        Utils.optAttribute(device, "size", textField.getColumns());
        Utils.optAttribute(device, "maxlength", textField.getMaxColumns());
        Utils.writeEvents(device, textField, null);
        if (textField.isFocusOwner())
            Utils.optAttribute(device, "foc", textField.getName());

        if (!textField.isEditable() || !textField.isEnabled()) {
            device.print(" readonly=\"true\"");
        }
        if (textField.isEnabled()) {
            device.print(" name=\"");
            Utils.write(device, Utils.event(textField));
            device.print("\"");
        } else {
            device.print(" disabled=\"true\"");
        }

        java.awt.Color orgColor = textField.getForeground();

        if (textField instanceof SFormattedTextField) {
            SFormattedTextField formattedTextField = (SFormattedTextField)textField;
            String text = textField.getText();
            if (text == null)
                text = "";
            if ( !formattedTextField.isEditValid() ) {
                textField.setForeground( java.awt.Color.RED );
            }

            SAbstractFormatter formatter = formattedTextField.getFormatter();
            String key = callableFormatter.registerFormatter(formatter);
            Utils.optAttribute(device, "formatter", key);
        }

        writeAllAttributes(device, component);

        textField.setForeground( orgColor );

        Utils.optAttribute(device, "value", textField.getText());
        device.print("/>");

        if (tableWrapping) {
            preferredSize.setWidth(actualWidth);
            device.print("</td></tr></table>");
        }
    }

    public Update updateText(STextField textfield, String text) {
    	return new ValueUpdate(textfield, text);
    }

    protected class ValueUpdate extends AbstractUpdate {

        private String value;

        public ValueUpdate(SComponent component, String value) {
            super(component);
            this.value = value;
        }

        public Handler getHandler() {
            UpdateHandler handler = new UpdateHandler("updateValue");
            handler.addParameter(component.getName());
            handler.addParameter(value);
            return handler;
        }

    }

    public static class CallableFormatter {
        Map formatters = new WeakHashMap();
        private final String name = new String("CallableFormatter");

        public List validate(String key, String name, String text, String lastValid) {
            String value = "";
            List list = new LinkedList();
            SAbstractFormatter formatter = formatterByKey(key);
            if ( formatter != null ) {
                list.add( name );
                try {
                    value = formatter.valueToString( formatter.stringToValue(text) );
                }
                catch (ParseException e) {
                    if (lastValid != null)
                        value = lastValid;
                }
            }
            list.add( value );
            return list;
        }

        protected SAbstractFormatter formatterByKey(String key) {
            for (Iterator iterator = formatters.keySet().iterator(); iterator.hasNext();) {
                SAbstractFormatter formatter = (SAbstractFormatter)iterator.next();
                if (key.equals("" + System.identityHashCode(formatter)))
                    return formatter;
            }
            return null;
        }

        private String registerFormatter(SAbstractFormatter formatter) {
            formatters.put(formatter, formatter);
            return "" + System.identityHashCode(formatter);
        }

        private String getName() {
            return name;
        }
    }
}
