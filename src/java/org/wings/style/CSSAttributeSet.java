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
package org.wings.style;

import org.wings.Renderable;
import org.wings.util.SStringBuilder;
import org.wings.io.Device;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * A straightforward implementation of CSSPropertySet using a hash map.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 */
public class CSSAttributeSet implements Renderable, Serializable, Cloneable {

    /** Empty immutable attribute set. */
    public static final CSSAttributeSet EMPTY_ATTRIBUTESET =
            new CSSAttributeSet() {
                private UnsupportedOperationException  doThrow() {
                    return new UnsupportedOperationException("cannot change values for the global EMPTY_ATTRIBUTESET. You attempted to modify this unmodifiable CSSPropertySet: create your own instance of a CSSPropertySet first!");
                }

                public String put(String name, String value) {
                    throw doThrow();
                }

                public boolean putAll(CSSAttributeSet attributes) {
                    throw doThrow();
                }
            };

    /** The map holding <code>CSSProperty</code> to <code>String</code>  */
    private HashMap map;

    /** Cached String representation of this attribute set. */
    private String cachedStringRepresentation;

    /**
     * create a CSSPropertySet from the given HashMap.
     */
    private CSSAttributeSet(HashMap map) {
        this.map = map;
    }

    /**
     * Creates a new, empty atribute set.
     */
    public CSSAttributeSet() {
    }

    public CSSAttributeSet(CSSProperty cssProperty, String cssPropertyValue) {
        put(cssProperty, cssPropertyValue);
    }

    /**
     * Creates a new attribute set based on a supplied set of attributes.
     *
     * @param source the set of attributes
     */
    public CSSAttributeSet(CSSAttributeSet source) {
        putAll(source);
    }

    /**
     * Checks whether the set of attributes is empty.
     *
     * @return true if the set is empty else false
     */
    public final boolean isEmpty() {
        return map == null || map.isEmpty();
    }

    /**
     * Gets a count of the number of attributes.
     *
     * @return the count
     */
    public final int size() {
        return map == null ? 0 : map.size();
    }

    public final void clear() {
        if (map != null) {
            map.clear();
        }
    }

    /**
     * Tells whether a given attribute is defined.
     *
     * @param name the attribute name
     * @return true if the attribute is defined
     */
    public final boolean contains(CSSProperty name) {
        return map != null && map.containsKey(name);
    }

    /**
     * Gets the Set of defined CSS property names.
     *
     * @return A set of {@link CSSProperty} for which this <code>CSSAttributeSet</code> contains a value.
     */
    public final Map properties() {
        return map != null ? Collections.unmodifiableMap(map) : Collections.EMPTY_MAP;
    }

    /**
     * Gets the value of an css property.
     *
     * @param property the attribute property
     * @return the value
     */
    public final String get(CSSProperty property) {
        return map == null ? null : (String) map.get(property);
    }

    /**
     * Adds an attribute to the list.
     *
     * @param name  the attribute name
     * @param value the attribute value
     */
    public String put(CSSProperty name, String value) {
        cachedStringRepresentation = null;
        if (map == null) {
            map = new HashMap(8);
        }

        if (value == null)
            return remove(name);
        return (String) map.put(name, value);
    }

    /**
     * Adds a set of attributes to the list.
     *
     * @param attributes the set of attributes to add
     */
    public boolean putAll(CSSAttributeSet attributes) {
        cachedStringRepresentation = null;
        if (map == null)
            map = new HashMap(8);

        boolean changed = false;
        for (Iterator iterator = attributes.properties().entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry)iterator.next();
            CSSProperty property = (CSSProperty)entry.getKey();
            changed = changed || (put(property, (String)entry.getValue()) != null);
        }
        return changed;
    }

    /**
     * Removes an attribute from the list.
     *
     * @param name the attribute name
     * @return The previous value for this CSS property
     */
    public String remove(CSSProperty name) {
        cachedStringRepresentation = null;
        return map == null ? null : (String) map.remove(name);
    }

    // --- Object methods ---------------------------------

    /**
     * Clones a set of attributes.
     *
     * @return the new set of attributes
     */
    public Object clone() {
        if (isEmpty()) {
            return new CSSAttributeSet();
        } else {
            return new CSSAttributeSet((HashMap) map.clone());
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final CSSAttributeSet that = (CSSAttributeSet)o;

        if (map != null ? !map.equals(that.map) : that.map != null) return false;

        return true;
    }

    public int hashCode() {
        return (map != null ? map.hashCode() : 0);
    }

//    /**
//     * Write style definition to the device. If include is true, write those
//     * contained in the {@link java.util.List}. If include is false, write those not contained
//     * in the {@link java.util.List}.
//     * Basically this is a filter on the styles, so we can separate styles for
//     * one logical component onto multiple real html elements.
//     */
//    private static void writeFiltered(Device d, Map map, Set l, boolean include) throws IOException {
//        if (l == null) l = Collections.EMPTY_SET;
//        if (map != null) {
//            Iterator names = map.entrySet().iterator();
//            while (names.hasNext()) {
//                Map.Entry next = (Map.Entry) names.next();
//                if ( !(l.contains(next.getKey()) ^ include) ) {
//                    d.print(next.getKey()).print(':')
//                            .print(next.getValue())
//                            .print(';');
//                }
//            }
//        }
//    }
//
//    /**
//     * Write style definition to the device. Write only those not contained
//     * in the set.
//     */
//    public static void writeExcluding(Device d, Map map, Set l) throws IOException {
//        writeFiltered(d, map, l, false);
//    }
//
//    /**
//     * Write style definition to the device. Write only those  contained
//     * in the set.
//     */
//    public static void writeIncluding(Device d, Map map, Set l) throws IOException {
//        writeFiltered(d, map, l, true);
//    }

    /**
     * Write style definition to the device
     */
    public void write(Device d)
            throws IOException {
        d.print(toString());
    }

    /**
     * Converts the attribute set to a String.
     *
     * @return the string
     */
    public String toString() {
        if (cachedStringRepresentation == null) {
            final SStringBuilder builder = new SStringBuilder();
            if (map != null) {
                Iterator names = map.entrySet().iterator();
                while (names.hasNext()) {
                    Map.Entry next = (Map.Entry) names.next();
                    builder.append(next.getKey());
                    builder.append(':');
                    builder.append(next.getValue());
                    builder.append(';');
                }
            }
            cachedStringRepresentation = builder.toString();
        }
        return cachedStringRepresentation;
    }
}


