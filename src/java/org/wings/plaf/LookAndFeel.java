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
package org.wings.plaf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.Resource;
import org.wings.SDimension;
import org.wings.SIcon;
import org.wings.SResourceIcon;
import org.wings.resource.ClasspathResource;
import org.wings.style.AttributeSet;
import org.wings.style.CSSStyleSheet;
import org.wings.style.StyleSheet;

import java.awt.*;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * A Look-and-Feel consists of a bunch of CGs and resource properties.
 * wingS provides a pluggable look-and-feel (laf or plaf) concept similar to that of Swing.
 * A certain plaf implementation normally adresses a specific browser.
 *
 * @see org.wings.plaf.ComponentCG
 */
public class LookAndFeel
        implements Serializable {
    private final transient static Log log = LogFactory.getLog(LookAndFeel.class);

    private static Map wrappers = new HashMap();

    static {
        wrappers.put(Boolean.TYPE, Boolean.class);
        wrappers.put(Character.TYPE, Character.class);
        wrappers.put(Byte.TYPE, Byte.class);
        wrappers.put(Short.TYPE, Short.class);
        wrappers.put(Integer.TYPE, Integer.class);
        wrappers.put(Long.TYPE, Long.class);
        wrappers.put(Float.TYPE, Float.class);
        wrappers.put(Double.TYPE, Double.class);
    }

    protected Properties properties;

    private static final Map finalResources = Collections.synchronizedMap(new HashMap());

    /**
     * Instantiate a laf using the war's classLoader.
     *
     * @param properties the configuration of the laf
     */
    public LookAndFeel(Properties properties) {
        this.properties = properties;
    }

    /**
     * Return a unique string that identifies this look and feel, e.g.
     * "konqueror"
     */
    public String getName() {
        return properties.getProperty("lookandfeel.name");
    }

    /**
     * Return a one line description of this look and feel implementation,
     * e.g. "Optimized for KDE's Konqueror Browser".
     */
    public String getDescription() {
        return properties.getProperty("lookandfeel.description");
    }

    /**
     * create a fresh CGDefaults map. One defaults map per Session is generated
     * in its CGManager. It is necessary to create a fresh defaults map, since
     * it caches values that might be modified within the sessions. A prominent
     * example of changed values per sessions are the CG's themselves:
     * CG-properties might be changed per session...
     *
     * @return the laf's defaults
     */
    public CGDefaults createDefaults() {
        return new ResourceFactory();
    }

    /**
     * Create a CG instance.
     *
     * @param className the full qualified class name of the CG
     * @return a new CG instance
     */
    public static Object makeCG(String className) {
        Object result = finalResources.get(className);
        if (result == null) {
            try {
                Class cgClass = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
                result = cgClass.newInstance();
                finalResources.put(className, result);
            } catch (Exception ex) {
                log.fatal(null, ex);
            }
        }
        return result;
    }

    /**
     * Utility method that creates an java.awt.Color from a html color hex string
     *
     * @return the create color
     */
    public static Color makeColor(String colorString) {
        if (colorString != null) {
            try {
                return Color.decode(colorString.trim());
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * Utility method that creates an java.awt.Color from a html color hex string
     *
     * @return the create color
     */
    public static SDimension makeDimension(String dimensionString) {
        if (dimensionString != null) {
            int commaIndex = dimensionString.indexOf(',');
            if (commaIndex > 0) {
                return new SDimension(dimensionString.substring(0, commaIndex),
                        dimensionString.substring(commaIndex + 1));
            }
        }
        return null;
    }

    /**
     * Utility method that creates an Icon from a resource
     * located realtive to the given base class. Uses the ClassLoader
     * of the LookAndFeel
     *
     * @param fileName of the image file
     * @return a newly allocated Icon
     */
    public static SIcon makeIcon(String fileName) {
        SIcon result = (SIcon) finalResources.get(fileName);
        if (result == null) {
            result = new SResourceIcon(fileName);
            finalResources.put(fileName, result);
        }
        return result;
    }

    /**
     * Utility method that creates an AttributeSet from a String
     *
     * @param string attributes string
     * @return a newly allocated AttributeSet
     */
    public static AttributeSet makeAttributeSet(String string) {
        AttributeSet attributes = new AttributeSet();
        StringTokenizer tokens = new StringTokenizer(string, ";");
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            int pos = token.indexOf(":");
            if (pos >= 0) {
                attributes.put(token.substring(0, pos),
                        token.substring(pos + 1));
            }
        }
        return attributes;
    }

    /**
     * Utility method that creates a styleSheet from a string
     *
     * @param resourceName styleSheet as a string
     * @return the styleSheet
     */
    public static Resource makeResource(String resourceName) {
        Resource result = (Resource) finalResources.get(resourceName);
        if (result == null) {
            result = new ClasspathResource(resourceName);
            finalResources.put(resourceName, result);
        }
        return result;
    }

    /**
     * Utility method that creates a stylesheet object from a resource
     *
     * @return the styleSheet
     */
    public static StyleSheet makeStyleSheet(String resourceName) {
        try {
            CSSStyleSheet result = new CSSStyleSheet();
            InputStream in = LookAndFeel.class.getClassLoader().getResourceAsStream(resourceName);
            result.read(in);
            in.close();
            return result;
        } catch (Exception e) {
            log.warn("Exception", e);
        }
        return null;
    }

    /**
     * Utility method that creates an Object of class <code>clazz</code>
     * using the single String arg constructor.
     *
     * @param value object as a string
     * @param clazz class of the object
     * @return the object
     */
    public static Object makeObject(String value, Class clazz) {
        Object result;
        try {
            if (value.startsWith("new ")) {
                int bracket = value.indexOf("(");
                String name = value.substring("new ".length(), bracket);
                clazz = Class.forName(name, true, Thread.currentThread().getContextClassLoader());
                result = clazz.newInstance();
            } else {
                if (clazz.isPrimitive())
                    clazz = (Class) wrappers.get(clazz);
                Constructor constructor = clazz.getConstructor(new Class[]{String.class});
                result = constructor.newInstance(new Object[]{value});
            }
        } catch (NoSuchMethodException e) {
            log.fatal(value + " : " + clazz.getName()
                    + " doesn't have a single String arg constructor", e);
            result = null;
        } catch (Exception e) {
            log.error(e.getClass().getName() + " : " + value, e);
            result = null;
        }
        return result;
    }

    /**
     * Returns a string that displays and identifies this
     * object's properties.
     *
     * @return a String representation of this object
     */
    public String toString() {
        return "[" + getDescription() + " - " + getClass().getName() + "]";
    }


    class ResourceFactory extends CGDefaults {

        public ResourceFactory() {
            super(null);
        }

        public Object get(Object key, Class type) {
            Object value = get(key);
            if (value != null)
                return value;

            String property;
            if (key instanceof Class) {
                Class clazz = (Class) key;
                do {
                    property = properties.getProperty(clazz.getName());
                    clazz = clazz.getSuperclass();
                } while (property == null && clazz != null);
            } else
                property = properties.getProperty(key.toString());

            if (property == null) {
                put(key, null);
                return null;
            }

            if (ComponentCG.class.isAssignableFrom(type) || LayoutCG.class.isAssignableFrom(type))
                value = makeCG(property);
            else if (type.isAssignableFrom(SIcon.class))
                value = makeIcon(property);
            else if (type.isAssignableFrom(Resource.class))
                value = makeResource(property);
            else if (type.isAssignableFrom(AttributeSet.class))
                value = makeAttributeSet(property);
            else if (type.isAssignableFrom(StyleSheet.class))
                value = makeStyleSheet(property);
            else if (type.isAssignableFrom(Color.class))
                value = makeColor(property);
            else if (type.isAssignableFrom(SDimension.class))
                value = makeDimension(property);
            else
                value = makeObject(property, type);

            put(key, value);
            return value;
        }
    }
}


