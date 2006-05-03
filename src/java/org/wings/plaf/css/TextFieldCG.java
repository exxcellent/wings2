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
import org.wings.SFormattedTextField;
import org.wings.STextField;
import org.wings.io.Device;
import org.wings.io.SStringBuilder;
import org.wings.plaf.css.dwr.CallableManager;
import org.wings.script.ScriptListener;
import org.wings.text.SAbstractFormatter;

import java.io.IOException;

public class TextFieldCG extends AbstractComponentCG implements
        org.wings.plaf.TextFieldCG {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public void componentChanged(SComponent component) {
        final STextField textField = (STextField) component;

        if (textField instanceof SFormattedTextField) {
            SFormattedTextField formattedTextField = (SFormattedTextField) textField;
            SAbstractFormatter formatter = formattedTextField.getFormatter();
            String name = "formatter_" + System.identityHashCode(formatter);
            if (!CallableManager.getInstance().containsCallable(name)) {
                CallableManager.getInstance().registerCallable(name, formatter);
                textField.putClientProperty("callable", name);
                // keep a reference to the name, otherwise the callable will get garbage collected

                ScriptListener[] scriptListeners = textField.getScriptListeners();

                for (int i = 0; i < scriptListeners.length; i++) {
                    ScriptListener scriptListener = scriptListeners[i];
                    if (scriptListener instanceof DWRScriptListener)
                        textField.removeScriptListener(scriptListener);
                }

                textField.addScriptListener(new DWRScriptListener("onblur",
                        "document.getElementById('{0}').getElementsByTagName('INPUT')[0].style.color = '';" +
                        name +
                        ".validate(callback_{0}, document.getElementById('{0}').getElementsByTagName('INPUT')[0].value)",
                        "function callback_{0}(data) {\n" +
                        "   if (!data && data != '') {\n" +
                        "       document.getElementById('{0}').getElementsByTagName('INPUT')[0].focus();\n" +
                        "       document.getElementById('{0}').getElementsByTagName('INPUT')[0].style.color = '#ff0000';\n" +
                        "   }\n" +
                        "   else\n" +
                        "       document.getElementById('{0}').getElementsByTagName('INPUT')[0].value = data;\n" +
                        "}\n", new SComponent[] { textField }));
            }
        }
    }

    public void write(final Device device,
                      final SComponent component)
            throws IOException {
        final STextField textField = (STextField) component;

        device.print("<input type=\"text\"");
        Utils.optAttribute(device, "size", textField.getColumns());
        Utils.optAttribute(device, "maxlength", textField.getMaxColumns());

        // build special style for input elememts
        // background/foreground/font is specified in the default css (gecko-optional.css, msie-optional)
        // So it is not possible to specify this css properties in the inner div.
        // TODO This is fast fix hack. Check if there is a better solution 
        SStringBuilder tStyle = new SStringBuilder();
        Utils.appendCSSComponentInlineColorStyle(tStyle, component);
        Utils.appendCSSComponentInlineFontStyle(tStyle, component);
        Utils.appendCSSInlineFullSize(tStyle, component);
        Utils.optAttribute(device, "style", tStyle);

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
        Utils.optAttribute(device, "tabindex", textField.getFocusTraversalIndex());

        if (textField.isFocusOwner())
            Utils.optAttribute(device, "focus", textField.getName());

        Utils.writeEvents(device, textField);

        Utils.optAttribute(device, "value", textField.getText());
        device.print("/>");
    }
}
