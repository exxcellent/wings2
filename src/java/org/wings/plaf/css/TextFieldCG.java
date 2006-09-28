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
import org.wings.*;
import org.wings.resource.ClassPathResource;
import org.wings.style.CSSProperty;
import org.wings.text.SAbstractFormatter;
import org.wings.util.SessionLocal;
import org.wings.event.SParentFrameListener;
import org.wings.io.Device;
import org.wings.plaf.css.dwr.CallableManager;
import org.wings.script.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import org.wings.session.SessionManager;
import org.wings.session.BrowserType;

public final class TextFieldCG extends AbstractComponentCG implements
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
            comp.addScriptListener(new JavaScriptListener(JavaScriptEvent.ON_BLUR, "this.style.color = '';CallableFormatter.validate(ftextFieldCallback, this.getAttribute('formatter'), this.id, this.value, this.getAttribute('lastValue'))"));
        }
        if (isMSIE(comp))
            comp.putClientProperty("horizontalOversize", new Integer(horizontalOversize));
    }

    public void writeInternal(final Device device,
                              final SComponent component)
            throws IOException {
        final STextField textField = (STextField) component;

        device.print("<input type=\"text\"");

        SDimension preferredSize = component.getPreferredSize();
        boolean behaviour = Utils.isMSIE(component) && preferredSize != null && "100%".equals(preferredSize.getWidth());
        if (behaviour) {
            component.setAttribute("behavior", "url('-org/wings/plaf/css/layout.htc')");
            preferredSize.setWidth(Utils.calculateHorizontalOversize(component, false));
            //component.setAttribute("display", "none");
        }
        writeAllAttributes(device, component);
        if (behaviour) {
            preferredSize.setWidth("100%");
            component.setAttribute("behavior", null);
            Utils.optAttribute(device, "rule", "width");
        }

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

        if (textField instanceof SFormattedTextField) {
            SFormattedTextField formattedTextField = (SFormattedTextField)textField;
            String text = textField.getText();
            if (text == null)
                text = "";
            String lastValue = formattedTextField.getFocusLostBehavior() == SFormattedTextField.COMMIT_OR_REVERT ? text : null;
            Utils.optAttribute(device, "lastValue", lastValue);

            SAbstractFormatter formatter = formattedTextField.getFormatter();
            String key = callableFormatter.registerFormatter(formatter);
            Utils.optAttribute(device, "formatter", key);
        }
        Utils.optAttribute(device, "value", textField.getText());
        device.print("/>");
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
