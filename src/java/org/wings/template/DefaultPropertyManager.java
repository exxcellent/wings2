/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.template;

import bsh.Interpreter;
import org.wings.SComponent;
import org.wings.session.SessionManager;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * DefaultPropertyManager.java
 * <p/>
 * <p/>
 * Created: Tue Aug  6 16:43:03 2002
 *
 * @author (c) mercatis information systems gmbh, 1999-2002
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class DefaultPropertyManager implements PropertyManager {

    private static final Logger logger = Logger.getLogger("org.wings.template");
    static final Class[] classes = {SComponent.class};

    public final HashMap propertyValueConverters = new HashMap();

    public static final DefaultPropertyValueConverter
            DEFAULT_PROPERTY_VALUE_CONVERTER = DefaultPropertyValueConverter.INSTANCE;

    private boolean scriptEnabled = false;

    /**
     *
     */
    public DefaultPropertyManager() {

    }

    protected Interpreter createInterpreter() {
        return new Interpreter();
    }

    public void setProperty(SComponent comp, String name, String value) {
        if (scriptEnabled && "SCRIPT".equals(name)) {
            Interpreter interpreter = createInterpreter();

            try {
                logger.finer("eval script " + value);

                interpreter.set("component", comp);
                interpreter.set("session", SessionManager.getSession());

                interpreter.eval(value);
            } catch (Exception ex) {
                ex.printStackTrace();
                // ignore it, maybe log it
            } // end of try-catch

// reset interpreter

        } // end of if ()


        Method[] methods = comp.getClass().getMethods();

        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];

            if (method.getName().startsWith("set") &&
                    name.equals(method.getName().substring(3).toUpperCase()) &&
                    method.getParameterTypes().length == 1) {

                Class paramType = method.getParameterTypes()[0];

                PropertyValueConverter valueConverter = getValueConverter(paramType);

                if (valueConverter != null) {
                    try {
                        //System.out.println("invoke " + method);
                        method.setAccessible(true);
                        method.invoke(comp,
                                new Object[]{valueConverter.convertPropertyValue(value, paramType)});
                        return;
                    } catch (Exception ex) {
                        // ignore it, maybe log it...
                    } // end of try-catch

                } // end of if ()
            } // end of if ()
        } // end of for (int i=0; i<; i++)
    }

    public void addPropertyValueConverter(PropertyValueConverter valueConverter,
                                          Class clazz) {

        propertyValueConverters.put(clazz, valueConverter);
    }

    protected PropertyValueConverter getValueConverter(Class clazz) {
        if (clazz == null) {
            return DEFAULT_PROPERTY_VALUE_CONVERTER;
        } // end of if ()

        if (propertyValueConverters.containsKey(clazz)) {
            return (PropertyValueConverter) propertyValueConverters.get(clazz);
        } // end of if ()

        return getValueConverter(clazz.getSuperclass());
    }

    public Class[] getSupportedClasses() {
        return classes;
    }
}
