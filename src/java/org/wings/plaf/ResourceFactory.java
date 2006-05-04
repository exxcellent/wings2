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

import java.awt.Color;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.Resource;
import org.wings.SDimension;
import org.wings.SIcon;
import org.wings.SResourceIcon;
import org.wings.plaf.css.CGDecorator;
import org.wings.resource.ClasspathResource;
import org.wings.style.CSSAttributeSet;
import org.wings.style.CSSProperty;
import org.wings.style.CSSStyleSheet;
import org.wings.style.StyleSheet;

public class ResourceFactory extends ResourceDefaults {
    private final transient static Log log = LogFactory.getLog(ResourceFactory.class);

    private static final Map WRAPPERS;
    static {
        WRAPPERS = new HashMap();
        WRAPPERS.put(Boolean.TYPE, Boolean.class);
        WRAPPERS.put(Character.TYPE, Character.class);
        WRAPPERS.put(Byte.TYPE, Byte.class);
        WRAPPERS.put(Short.TYPE, Short.class);
        WRAPPERS.put(Integer.TYPE, Integer.class);
        WRAPPERS.put(Long.TYPE, Long.class);
        WRAPPERS.put(Float.TYPE, Float.class);
        WRAPPERS.put(Double.TYPE, Double.class);
    }

    private static final Map finalResources = Collections.synchronizedMap(new HashMap());

    /**
     * 
     */
    private final Properties _properties;

    public ResourceFactory(Properties properties) {
        super(null);
        _properties = properties;
    }

    public Object get(Object key, Class type) {
        Object value = get(key);
        if (value != null)
            return value;

        String property;
        if (key instanceof Class) {
            Class clazz = (Class) key;
            do {
                property = _properties.getProperty(clazz.getName());
                clazz = clazz.getSuperclass();
            } while (property == null && clazz != null);
        } else
            property = _properties.getProperty(key.toString());

        if (property == null) {
            put(key, null);
            return null;
        }

        if (ComponentCG.class.isAssignableFrom(type))
            value = makeComponentCG(property, _properties.getProperty("AbstractComponentCG.PrefixAndSuffixDecorator"));
        else if (LayoutCG.class.isAssignableFrom(type))
            value = makeLayoutCG(property);
        else if (type.isAssignableFrom(SIcon.class))
            value = makeIcon(property);
        else if (type.isAssignableFrom(Resource.class))
            value = makeResource(property);
        else if (type.isAssignableFrom(CSSAttributeSet.class))
            value = makeAttributeSet(property);
        else if (type.isAssignableFrom(StyleSheet.class))
            value = makeStyleSheet(property);
        else if (type.isAssignableFrom(Color.class))
            value = makeColor(property);
        else if (type.isAssignableFrom(SDimension.class))
            value = makeDimension(property);
        else if (type.isAssignableFrom(Class.class))
            value = makeClass(property);
        else
            value = makeObject(property, type);

        put(key, value);
        return value;
    }

    /**
     * Create a CG instance.
     *
     * @param className the full qualified class name of the CG
     * @return a new CG instance
     */
    public static Object makeComponentCG(String className, String decoratorClassName) {
      ComponentCG result = (ComponentCG)finalResources.get(className);
      if (result == null) {
          try {
              Class cgClass = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
              result = (ComponentCG)cgClass.newInstance();

              Class decoratorClass = decoratorClassName != null ? makeClass(decoratorClassName) : null;
              if (decoratorClass != null) {
                  CGDecorator decorator = (CGDecorator)decoratorClass.newInstance();
                  decorator.setDelegate(result);
                  result = decorator;
              }
              finalResources.put(className, result);
          } catch (Exception ex) {
              log.fatal(null, ex);
          }
      }
      return result;
    }

    /**
     * Create a CG instance.
     *
     * @param className the full qualified class name of the CG
     * @return a new CG instance
     */
    public static Object makeLayoutCG(String className) {
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
                log.info("Unable to decode color string "+colorString, ex);
                return null;
            }
        }
        return null;
    }

    /**
     * Utility method that creates a dimension from a dimension string separated by comma 
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
     * Utility method that creates an CSSPropertySet from a String
     *
     * @param string attributes string
     * @return a newly allocated CSSPropertySet
     */
    public static CSSAttributeSet makeAttributeSet(String string) {
        CSSAttributeSet attributes = new CSSAttributeSet();
        StringTokenizer tokens = new StringTokenizer(string, ";");
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            int pos = token.indexOf(":");
            if (pos >= 0) {
                attributes.put(new CSSProperty(token.substring(0, pos)), token.substring(pos + 1));
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
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
            result.read(in);
            in.close();
            return result;
        } catch (Exception e) {
            log.warn("Exception", e);
        }
        return null;
    }

    /**
     * Utility method that creates a Class from a resource
     * located realtive to the given base class. Uses the ClassLoader
     * of the LookAndFeel
     *
     * @param className name of the class
     * @return a class instance
     */
    public static Class makeClass(String className) {
        Class result = (Class)finalResources.get(className);
        if (result == null) {
            try {
                result = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
                finalResources.put(className, result);
            }
            catch (ClassNotFoundException e) {
                log.warn("Exception", e);
            }
        }
        return result;
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
                    clazz = (Class) WRAPPERS.get(clazz);
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
}
