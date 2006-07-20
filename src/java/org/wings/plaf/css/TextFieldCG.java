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


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.SComponent;
import org.wings.SFormattedTextField;
import org.wings.STextField;
import org.wings.io.Device;
import org.wings.plaf.css.dwr.CallableManager;
import org.wings.script.JavaScriptEvent;
import org.wings.script.ScriptListener;
import org.wings.text.SAbstractFormatter;

import java.io.IOException;
import java.text.ParseException;

public final class TextFieldCG extends AbstractComponentCG implements
        org.wings.plaf.TextFieldCG {
    private final static transient Log log = LogFactory.getLog(TextFieldCG.class);

    private static final long serialVersionUID = 1L;

    public void componentChanged(SComponent component) {
        final STextField textField = (STextField) component;

        if (textField instanceof SFormattedTextField) {
            SFormattedTextField formattedTextField = (SFormattedTextField) textField;
            SAbstractFormatter formatter = formattedTextField.getFormatter();
            String name = "formatter_" + System.identityHashCode(formatter);
            if (!CallableManager.getInstance().containsCallable(name)) {
                CallableManager.getInstance().registerCallable(name, new CallableFormatter(formatter));
                textField.putClientProperty("callable", name);
                // keep a reference to the name, otherwise the callable will get garbage collected

                ScriptListener[] scriptListeners = textField.getScriptListeners();

                for (int i = 0; i < scriptListeners.length; i++) {
                    ScriptListener scriptListener = scriptListeners[i];
                    if (scriptListener instanceof DWRScriptListener)
                        textField.removeScriptListener(scriptListener);
                }

                textField.addScriptListener(new DWRScriptListener(JavaScriptEvent.ON_BLUR,
                                                                  "document.getElementById('{0}').style.color = '';" +
                                                                  name +
                                                                  ".validate(callback_{0}, document.getElementById('{0}').value)",
                                                                  "function callback_{0}(data) {\n" +
                                                                  "   if (!data && data != '') {\n" +
                                                                  "       document.getElementById('{0}').style.color = '#ff0000';\n" +
                                                                  "   }\n" +
                                                                  "   else\n" +
                                                                  "       document.getElementById('{0}').value = data;\n" +
                                                                  "}\n", new SComponent[] { textField }));
            }
        }
        else {
            super.componentChanged(component);
        }
    }

    public void writeInternal(final Device device,
                              final SComponent component)
            throws IOException {
        final STextField textField = (STextField) component;

        device.print("<input type=\"text\"");
        writeAllAttributes(device, component);
        Utils.optAttribute(device, "tabindex", textField.getFocusTraversalIndex());
        Utils.optAttribute(device, "size", textField.getColumns());
        Utils.optAttribute(device, "maxlength", textField.getMaxColumns());
        Utils.optFullSize(device, component);
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

        Utils.optAttribute(device, "value", textField.getText());
        device.print("/>");
    }

    public static class CallableFormatter {
        SAbstractFormatter formatter;

        public CallableFormatter(SAbstractFormatter formatter) {
            this.formatter = formatter;
        }

        public String validate(String text) {
            try {
                return formatter.valueToString(formatter.stringToValue(text));
            }
            catch (ParseException e) {
                return null;
            }
        }
    }
}