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

/**
 * Non synchronized, fast stack. This stack never shrinks its internal
 * data structure.
 *
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 */
public final class FastStack {
    private Object[] elements;
    private int pos;

    public FastStack(int initialElements) {
        pos = -1;
        elements = new Object[initialElements];
    }

    /**
     * clear the stack.
     */
    public void clear() {
        pos = -1;
    }

    public boolean isEmpty() {
        return pos == -1;
    }

    /**
     * push object to Stack.
     */
    public void push(Object o) {
        ++pos;
        if (pos == elements.length) resize();
        /*
         * debugging hint: if you get an IndexOutOfBoundException here,
         * maybe the last pop() operation was bogus in the first place ?
         * It is not checked there for speed reasons.
         */
        elements[pos] = o;
    }

    public Object peek() {
        return elements[pos];
    }

    /**
     * pop element from stack. Does not return the actual element. If you
     * need this, call peek().
     */
    public void pop() {
        --pos;
    }

    /**
     * we only increase the size.
     */
    private void resize() {
        Object[] newArray = new Object[elements.length * 2];
        System.arraycopy(elements, 0, newArray, 0, elements.length);
        elements = newArray;
    }
}


