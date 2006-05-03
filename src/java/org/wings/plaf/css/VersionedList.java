/*
 * $Id$
 * Copyright 2000,2006 wingS development team.
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

import java.util.*;

/**
 * The <code>VersionedList</code> is a wrapper for <code>List</code>s. Each modification
 * of the wrapper will increment the version counter. So you are able to detect changes
 * and further more the number of changes of the list.
 *
 * @author Roman R&auml;dle
 * @version $Revision$
 */
public class VersionedList extends LinkedList {

    // The serial version uid of the class.
    private static final long serialVersionUID = 1L;

    // The wrapped list.
    private List list;

    // The current version.
    private int version = 0;

    /**
     * New version list with an empty <code>ArrayList</code>
     * as wrapped list.
     */
    public VersionedList() {
        list = new ArrayList();
    }

    /**
     * New version list with the parameter list as wrapped
     * list.
     *
     * @param list The list that get wrapped.
     */
    public VersionedList(List list) {
        this.list = list;
    }

    /**
     * @see java.util.List#size()
     */
    public int size() {
        return list.size();
    }

    /**
     * @see java.util.List#isEmpty()
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * @see java.util.List#contains(Object)
     */
    public boolean contains(Object o) {
        return list.contains(o);
    }

    /**
     * @see java.util.List#iterator()
     */
    public Iterator iterator() {
        return list.iterator();
    }

    /**
     * @see java.util.List#toArray()
     */
    public Object[] toArray() {
        return list.toArray();
    }

    /**
     * @see java.util.List#toArray(Object[])
     */
    public Object[] toArray(Object[] a) {
        return list.toArray(a);
    }

    /**
     * @see java.util.List#add(Object)
     */
    public boolean add(Object o) {
        version++;
        return list.add(o);
    }

    /**
     * @see java.util.List#remove(Object)
     */
    public boolean remove(Object o) {
        version++;
        return list.remove(o);
    }

    /**
     * @see java.util.List#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection c) {
        return list.containsAll(c);
    }

    /**
     * @see java.util.List#addAll(java.util.Collection)
     */
    public boolean addAll(Collection c) {
        version++;
        return list.addAll(c);
    }

    /**
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    public boolean addAll(int index, Collection c) {
        version++;
        return list.addAll(index, c);
    }

    /**
     * @see java.util.List#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection c) {
        version++;
        return list.removeAll(c);
    }

    /**
     * @see java.util.List#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection c) {
        version++;
        return list.retainAll(c);
    }

    /**
     * @see java.util.List#clear()
     */
    public void clear() {
        version++;
        list.clear();
    }

    /**
     * @see java.util.List#equals(Object)
     */
    public boolean equals(Object o) {
        return list.equals(o);
    }

    /**
     * @see java.util.List#hashCode()
     */
    public int hashCode() {
        return list.hashCode();
    }

    /**
     * @see java.util.List#get(int)
     */
    public Object get(int index) {
        return list.get(index);
    }

    /**
     * @see java.util.List#set(int, Object)
     */
    public Object set(int index, Object element) {
        version++;
        return list.set(index, element);
    }

    /**
     * @see java.util.List#add(int, Object)
     */
    public void add(int index, Object element) {
        version++;
        list.add(index, element);
    }

    /**
     * @see java.util.List#remove(int)
     */
    public Object remove(int index) {
        version++;
        return list.remove(index);
    }

    /**
     * @see java.util.List#indexOf(Object)
     */
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    /**
     * @see java.util.List#lastIndexOf(Object)
     */
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    /**
     * @see java.util.List#listIterator()
     */
    public ListIterator listIterator() {
        return list.listIterator();
    }

    /**
     * @see java.util.List#listIterator(int)
     */
    public ListIterator listIterator(int index) {
        return list.listIterator(index);
    }

    /**
     * @see java.util.List#subList(int, int)
     */
    public List subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    /**
     * Returns the current version of the versioned
     * list.
     *
     * @return The current version of the versioned
     *         list.
     */
    public int getVersion() {
        return version;
    }
}
