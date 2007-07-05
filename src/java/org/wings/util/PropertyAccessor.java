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
package org.wings.util;

import java.lang.reflect.Method;

/**
 * little helper class to access properties via introspection. Quick
 * hack, not yet implemneted completely.
 */
public class PropertyAccessor {
    /**
     * determines.
     */
    public static boolean hasProperty(Object o, String name) {
        try {
            Method[] m = o.getClass().getMethods();
            String setterName = "set" + capitalize(name);
            for (int i = 0; i < m.length; ++i) {
                if (m[i].getParameterTypes().length == 1
                        && m[i].getName().equals(setterName)) {
                    // (maybe check, whether there is a getter here).
                    return true;
                }
            }
        } catch (Exception e) {}
        return false;
    }


    public static void setProperty(Object o, String name, Object value) {
        try {
            Method[] m = o.getClass().getMethods();
            String setterName = "set" + capitalize(name);
            for (int i = 0; i < m.length; ++i) {
                if (m[i].getParameterTypes().length == 1
                        && m[i].getName().equals(setterName)
                        && (value == null
                        || (m[i].getParameterTypes()[0]
                        .isAssignableFrom(value.getClass())))) {
                    m[i].invoke(o, new Object[]{value});
                }
            }
        } catch (Exception e) {}
    }


    public static void getProperty(Object o, String name, Object value) {
        // not yet implemented.
    }

    /**
     * captialize a string. A String 'foo' becomes 'Foo'. Used to
     * derive names of getters from the property name.
     */
    private static String capitalize(String s) {
        s = s.trim();
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}


