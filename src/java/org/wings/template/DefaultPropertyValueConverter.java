/*
  $Id$
  (c) Copyright 2002 mercatis information systems GmbH

  Part of e-lib 
 
  This file contains unpublished, proprietary trade secret information of
  mercatis information systems GmbH. Use, transcription, duplication and
  modification are strictly prohibited without prior written consent of
  mercatis information systems GmbH.
  See http://www.mercatis.de
*/

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

import org.wings.Resource;
import org.wings.SDimension;
import org.wings.SFont;
import org.wings.SIcon;
import org.wings.plaf.ComponentCG;
import org.wings.plaf.LookAndFeel;
import org.wings.resource.ClasspathResource;
import org.wings.style.AttributeSet;
import org.wings.style.CSSStyleSheet;
import org.wings.style.StyleSheet;

import java.awt.*;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * <!--
 * DefaultPropertyValueConverter.java
 * Created: Tue Aug  6 17:30:51 2002
 * -->
 * <p/>
 * <p/>
 * <p/>
 * <p><b>(c)2002 <a href="http://www.mercatis.de">mercatis information systems GmbH</a></b></p>
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class DefaultPropertyValueConverter implements PropertyValueConverter {

    public static final DefaultPropertyValueConverter INSTANCE =
            new DefaultPropertyValueConverter();

    private final static Logger logger = Logger.getLogger("org.wings.template");

    /**
     * 
     */
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
            return LookAndFeel.makeIcon(value);
        }

        if (targetClass == Color.class) {
            return LookAndFeel.makeColor(value);
        }

        if (targetClass == SDimension.class) {
            return LookAndFeel.makeDimension(value);
        }

        if (targetClass == SFont.class) {
            return TemplateUtil.parseFont(value);
        }

        if (Resource.class.isAssignableFrom(targetClass)) {
            return new ClasspathResource(value);
        }

        if (AttributeSet.class.isAssignableFrom(targetClass)) {
            return LookAndFeel.makeAttributeSet(value);
        }

        if (StyleSheet.class.isAssignableFrom(targetClass)) {
            StyleSheet result;
            try {
                CSSStyleSheet styleSheet = new CSSStyleSheet();
                InputStream in = getClass().getClassLoader().getResourceAsStream(value);
                styleSheet.read(in);
                in.close();
                result = styleSheet;
            } catch (Exception e) {
                LookAndFeel.logger.log(Level.WARNING, null, e);
                result = null;
            }
            return result;
        }

        if (ComponentCG.class.isAssignableFrom(targetClass)) {
            return LookAndFeel.makeCG(value);
        }

        throw new UnsupportedOperationException("cannot create object of type " +
                targetClass.getName());
    }


}// DefaultPropertyValueConverter

/*
   $Log$
   Revision 1.3  2004/11/24 18:13:17  blueshift
   TOTAL CLEANUP:
   - removed document me TODOs
   - updated/added java file headers
   - removed emacs stuff
   - removed deprecated methods

   Revision 1.2  2004/11/20 16:33:51  hengels
   o moved resource implementations to a separate package

   Revision 1.1.1.1  2004/10/04 16:13:29  hengels
   o start development of wings 2

   Revision 1.3  2002/11/19 15:36:40  ahaaf
   o use LookAndFeel methods

   Revision 1.2  2002/11/01 14:17:50  ahaaf
   o add support for more types

   Revision 1.1  2002/08/06 16:45:55  ahaaf
   add DefaultPropertyManager using reflection and bean shell scripting support

*/
