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
import org.wings.text.SAbstractFormatter;
import org.wings.util.SessionLocal;
import org.wings.event.SParentFrameListener;
import org.wings.io.Device;
import org.wings.plaf.css.dwr.CallableManager;
import org.wings.script.JavaScriptEvent;
import org.wings.script.ScriptListener;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import org.wings.session.SessionManager;

public final class TextFieldCG extends AbstractComponentCG implements
        org.wings.plaf.TextFieldCG, SParentFrameListener {

    private static final long serialVersionUID = 1L;

    private SessionLocal callableFormatter = new SessionLocal();

    public void installCG( SComponent comp ) {
        super.installCG( comp );
        if ( comp instanceof SFormattedTextField ) {
            comp.addParentFrameListener( this );
        }
    }
    
    public void parentFrameAdded(org.wings.event.SParentFrameEvent e ) {
        if (!CallableManager.getInstance().containsCallable("ftextField"))
            CallableManager.getInstance().registerCallable("ftextField", getCallableFormatter());
    }
    
    public void parentFrameRemoved(org.wings.event.SParentFrameEvent e ) {}
    
    protected CallableFormatter getCallableFormatter() {
        CallableFormatter callableFormatter = (CallableFormatter)this.callableFormatter.get();
        if (callableFormatter == null) {
            callableFormatter = new CallableFormatter();
            this.callableFormatter.set(callableFormatter);
        }
        return callableFormatter;
    }

    public void componentChanged(SComponent component) {
        final STextField textField = (STextField) component;
        
        if (textField instanceof SFormattedTextField) {
            SFormattedTextField formattedTextField = ((SFormattedTextField)textField);

            ScriptListener[] scriptListeners = textField.getScriptListeners();
            
            for (int i = 0; i < scriptListeners.length; i++) {
                ScriptListener scriptListener = scriptListeners[i];
                if (scriptListener instanceof DWRScriptListener)
                    textField.removeScriptListener(scriptListener);
            }

            SAbstractFormatter formatter = formattedTextField.getFormatter();
            String key = getCallableFormatter().registerFormatter(formatter);

            String lastValue = formattedTextField.getFocusLostBehavior() == SFormattedTextField.COMMIT_OR_REVERT ?
                ", '" + textField.getText() + "'" : "";
            textField.addScriptListener(new DWRScriptListener(JavaScriptEvent.ON_BLUR,
                                                              "this.style.color = '';ftextField.validate(ftextFieldCallback, '" + key + "', '"+textField.getName()+"', this.value" + lastValue + ")"," "
                    , new SComponent[] { textField }));

        } else {
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
        Map formatters = new WeakHashMap();

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

        public String registerFormatter(SAbstractFormatter formatter) {
            formatters.put(formatter, formatter);
            return "" + System.identityHashCode(formatter);
        }
    }
}
