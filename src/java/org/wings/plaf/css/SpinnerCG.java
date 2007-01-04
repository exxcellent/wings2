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

import java.util.HashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import org.wings.SSpinner;
import org.wings.SSpinner.DefaultEditor;
import org.wings.SComponent;
import org.wings.SFormattedTextField;
import org.wings.SResourceIcon;

import org.wings.header.SessionHeaders;
import org.wings.io.Device;
import org.wings.plaf.css.dwr.CallableManager;
import org.wings.text.SAbstractFormatter;
import org.wings.util.SessionLocal;

public class SpinnerCG extends AbstractComponentCG implements org.wings.plaf.SpinnerCG {

    private static final Log log = LogFactory.getLog( SpinnerCG.class );

    private static final SResourceIcon iconNext = new SResourceIcon("org/wings/icons/SpinnerNext.gif");
    private static final SResourceIcon iconPrev = new SResourceIcon("org/wings/icons/SpinnerPrev.gif");

    private static final long serialVersionUID = 1L;

    private SessionLocal sessionLocal = new SessionLocal();

    public void installCG(SComponent component) {
        super.installCG(component);

        if (!CallableManager.getInstance().containsCallable("CallableSpinner")) {
            HashSet methodsToExpose = new HashSet();
            methodsToExpose.add("getValue");
            CallableManager.getInstance().registerCallable("CallableSpinner", getCallableSpinner(), methodsToExpose);
        }
    }

    public void uninstallCG(SComponent component) {
        CallableManager.getInstance().unregisterCallable("CallableSpinner");
    }

    public void writeInternal(final Device device, final SComponent component)
            throws IOException {
        final SSpinner spinner = (SSpinner) component;

        String key = getCallableSpinner().register(spinner);
        SFormattedTextField ftf = ((DefaultEditor)spinner.getEditor()).getTextField();

        device.print( "\n<table" );
        writeAllAttributes(device, component);
        device.print( "><tr><td>\n" );
        spinner.getEditor().write( device );
        device.print( "\n</td><td style=\"width:0px; font-size: 0px; line-height: 0\">\n" );
        device.print( "<img onclick=\"CallableSpinner.getValue('"+key+"','"+ftf.getName()+"',document.getElementById('"+ftf.getName()+"').value,'0',wingS.component.spinnerCallback)\" src=\"" + iconNext.getURL() + "\" style=\"display:block;vertical-align:bottom;\">\n");
        device.print( "<img onclick=\"CallableSpinner.getValue('"+key+"','"+ftf.getName()+"',document.getElementById('"+ftf.getName()+"').value,'1',wingS.component.spinnerCallback)\" src=\"" + iconPrev.getURL() + "\" style=\"display:block;vertical-align:top\">\n");
        device.print( "</td></tr></table>\n" );
    }

    protected CallableSpinner getCallableSpinner() {
        CallableSpinner callableSpinner = (CallableSpinner)this.sessionLocal.get();
        if (callableSpinner == null) {
            callableSpinner = new CallableSpinner();
            this.sessionLocal.set(callableSpinner);
        }
        return callableSpinner;
    }

    public final static class CallableSpinner {
        Map weakHashMap = new WeakHashMap();

        public static final int PREV = 1;
        public static final int NEXT = 0;

        public List getValue( String key, String name, String value, int type ) {
            log.debug( "getValue( " + key + ", " + name + ", " + value + ", " + type + " )" );

            List list = new LinkedList();

            SSpinner spinner = spinnerByKey(key);
            if ( spinner != null ) {
                list.add( name );
                try {

                    SAbstractFormatter formatter = ((DefaultEditor)spinner.getEditor()).getTextField().getFormatter();

                    Object newValue = formatter.stringToValue( value );

                    spinner.setValue( newValue );

                    Object objValue = null;
                    switch ( type ) {
                        case PREV:
                            objValue = spinner.getModel().getPreviousValue();
                            break;
                        case NEXT:
                            objValue = spinner.getModel().getNextValue();
                            break;
                        default:
                            objValue = spinner.getModel().getValue();
                    }

                    if ( objValue != null ) {
                        list.add( formatter.valueToString( objValue ) );
                        spinner.getModel().setValue( objValue );
                    } else {
                        list.add( "" );
                    }

                    if ( spinner.getChangeListeners().length > 1 ) {
                        list.add( String.valueOf( true ) );
                    }

                } catch ( java.text.ParseException pe ) {
                    pe.printStackTrace();
                } catch ( java.lang.IllegalArgumentException iae ) {
                    iae.printStackTrace();
                }
            }

            return list;
        }

        protected SSpinner spinnerByKey(String key) {
            for (Iterator iterator = weakHashMap.keySet().iterator(); iterator.hasNext();) {
                SSpinner spinner = (SSpinner)iterator.next();
                if (key.equals("" + System.identityHashCode(spinner)))
                    return spinner;
            }
            return null;
        }

        public String register(SSpinner model) {
            weakHashMap.put(model, model);
            return "" + System.identityHashCode(model);
        }
    }

    public long getSerialVersionUID() {
        return serialVersionUID;
    }

}
