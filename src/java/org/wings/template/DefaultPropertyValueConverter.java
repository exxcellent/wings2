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
package org.wings.template;

import java.awt.Color;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.Resource;
import org.wings.SDimension;
import org.wings.SFont;
import org.wings.SIcon;
import org.wings.plaf.ComponentCG;
import org.wings.plaf.ResourceFactory;
import org.wings.resource.ClassPathResource;
import org.wings.style.CSSAttributeSet;
import org.wings.style.CSSStyleSheet;
import org.wings.style.StyleSheet;


/**
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 */
public class DefaultPropertyValueConverter implements PropertyValueConverter {

    public static final DefaultPropertyValueConverter INSTANCE =
            new DefaultPropertyValueConverter();

    private final transient static Log log = LogFactory.getLog(DefaultPropertyValueConverter.class);


    public DefaultPropertyValueConverter() {

    }
    // implementation of org.wings.template.PropertyValueConverter interface

    /**
     * Describe <code>convertPropertyValue</code> method here.
     *
     * @param value       an <code>Object</code> value
     * @param targetClass a <code>Class</code> value
     * @return <description>
     * @throws UnsupportedOperationException if an error occurs
     * @throws java.lang.UnsupportedOperationException
     *                                       <description>
     */
    public Object convertPropertyValue(String value, Class targetClass)
            throws UnsupportedOperationException {
        if (value == null || "null".equals(value)) {
            return null;
        } // end of if ()


        if (targetClass == String.class) {
            return value;
        } // end of if ()

        if (targetClass == Boolean.TYPE || targetClass == Boolean.class) {
            return Boolean.valueOf(value);
        }

        if (targetClass == Integer.TYPE || targetClass == Integer.class) {
            return Integer.valueOf(value);
        }
        if (targetClass == Long.TYPE || targetClass == Long.class) {
            return Long.valueOf(value);
        }
        if (targetClass == Short.TYPE || targetClass == Short.class) {
            return Short.valueOf(value);
        }
        if (targetClass == Byte.TYPE || targetClass == Byte.class) {
            return Byte.valueOf(value);
        }
        if (targetClass == Float.TYPE || targetClass == Float.class) {
            return Float.valueOf(value);
        }
        if (targetClass == Double.TYPE || targetClass == Double.class) {
            return Double.valueOf(value);
        }
        if (targetClass == Character.TYPE || targetClass == Character.class) {
            return new Character(value.charAt(0));
        }

        if (targetClass == StringBuffer.class) {
            return new StringBuffer(value);
        } // end of if ()

        if (SIcon.class.isAssignableFrom(targetClass)) {
            return ResourceFactory.makeIcon(value);
        }

        if (targetClass == Color.class) {
            return ResourceFactory.makeColor(value);
        }

        if (targetClass == SDimension.class) {
            return ResourceFactory.makeDimension(value);
        }

        if (targetClass == SFont.class) {
            return TemplateUtil.parseFont(value);
        }

        if (Resource.class.isAssignableFrom(targetClass)) {
            return new ClassPathResource(value);
        }

        if (CSSAttributeSet.class.isAssignableFrom(targetClass)) {
            return ResourceFactory.makeAttributeSet(value);
        }

        if (StyleSheet.class.isAssignableFrom(targetClass)) {
            StyleSheet result;
            try {
                CSSStyleSheet styleSheet = new CSSStyleSheet();
                InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(value);
                styleSheet.read(in);
                in.close();
                result = styleSheet;
            } catch (Exception e) {
                log.warn("Exception", e);
                result = null;
            }
            return result;
        }

        if (ComponentCG.class.isAssignableFrom(targetClass)) {
            return ResourceFactory.makeComponentCG(value);
        }

        throw new UnsupportedOperationException("cannot create object of type " +
                targetClass.getName());
    }


}