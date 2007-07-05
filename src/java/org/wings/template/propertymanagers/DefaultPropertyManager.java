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
package org.wings.template.propertymanagers;

import bsh.Interpreter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.SComponent;
import org.wings.template.PropertyManager;
import org.wings.template.DefaultPropertyValueConverter;
import org.wings.template.PropertyValueConverter;
import org.wings.session.SessionManager;
import org.wings.session.Session;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * The default property handler for the <code>OBJECT</code> tags in STemplateLayout.
 *
 * @author (c) mercatis information systems gmbh, 1999-2002
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 */
public class DefaultPropertyManager implements PropertyManager {
    /**
     * Apache commons logger
     */
    private static final transient Log log = LogFactory.getLog(DefaultPropertyManager.class);

    static final Class[] classes = {SComponent.class};

    public final HashMap propertyValueConverters = new HashMap();

    public static final DefaultPropertyValueConverter
            DEFAULT_PROPERTY_VALUE_CONVERTER = DefaultPropertyValueConverter.INSTANCE;

    /**
     * Declare a value "true" for this string in web.xml to enable BeanScript support.
     */
    public final static String BEANSCRIPT_ENABLE = "wings.template.beanscript";

    public DefaultPropertyManager() {
    }

    protected Interpreter createInterpreter() {
        return new Interpreter();
    }

    public void setProperty(SComponent comp, String name, String value) {
        if ("SCRIPT".equals(name)) {
            Session session = SessionManager.getSession();
            final Boolean scriptEnabled = Boolean.valueOf((String) session.getProperty(BEANSCRIPT_ENABLE));

            if (scriptEnabled.booleanValue()) {
                final Interpreter interpreter = createInterpreter();

                try {
                    log.debug("eval script " + value);

                    interpreter.set("component", comp);
                    interpreter.set("session", session);

                    interpreter.eval(value);
                } catch (Exception ex) {
                    log.warn("Error on evaluating script "+value, ex);
                }
            } else {
                log.warn("BeanScript support not enabled. Define value 'true' for " +
                "property "+ DefaultPropertyManager.BEANSCRIPT_ENABLE+" in web.xml " +
                "to enable BeanScript support!");
            }
        }


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
                        method.setAccessible(true);
                        method.invoke(comp,
                                new Object[]{valueConverter.convertPropertyValue(value, paramType)});
                        return;
                    } catch (Exception ex) {
                        log.debug("Unable to invoke method "+method.getName()+" due to ",ex);
                    }
                }
            }
        } // end of for (int i=0; i<; i++)
    }

    public void addPropertyValueConverter(PropertyValueConverter valueConverter,
                                          Class clazz) {

        propertyValueConverters.put(clazz, valueConverter);
    }

    protected PropertyValueConverter getValueConverter(Class clazz) {
        if (clazz == null) {
            return DEFAULT_PROPERTY_VALUE_CONVERTER;
        }

        if (propertyValueConverters.containsKey(clazz)) {
            return (PropertyValueConverter) propertyValueConverters.get(clazz);
        }

        return getValueConverter(clazz.getSuperclass());
    }

    public Class[] getSupportedClasses() {
        return classes;
    }
}
