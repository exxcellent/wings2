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

import org.wings.SimpleURL;

/**
 * This is a thread save global stack.
 *
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 */
public class AnchorRenderStack {
    /**
     * 10 should be sufficient, so that we never need resizing. Usually
     * this will not go over 2
     */
    private final static int INITIAL_STACK_DEPTH = 10;

    /*
     * state for the ClickableRenderComponent.
     */
    private final static ThreadLocal eventURLStack = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return new FastStack(INITIAL_STACK_DEPTH);
        }
    };

    /**
     * reset the internal stacks. This should be done everytime a complete
     * frame is rendered (maybe in some finally { }) to make sure the internal
     * stacks do not fill up.
     */
    public static void reset() {
        ((FastStack) eventURLStack.get()).clear();
    }

    /**
     * Push a new URL.
     */
    public static void push(SimpleURL url, String target) {
        FastStack s = (FastStack) eventURLStack.get();
        s.push(new AnchorProperties(url, target));
    }

    public static void push(String formEventName, String formEventValue) {
        FastStack s = (FastStack) eventURLStack.get();
        s.push(new AnchorProperties(formEventName, formEventValue));
    }


    public static void pop() {
        FastStack s = (FastStack) eventURLStack.get();
        s.pop();
    }

    /**
     * returns the topmost request URL on the stack or 'null', if there
     * is no such element.
     */
    public static AnchorProperties get() {
        FastStack s = (FastStack) eventURLStack.get();
        return s.isEmpty() ? null : (AnchorProperties) s.peek();
    }

    public static Object clear() {
        Object oldValue = eventURLStack.get();
        eventURLStack.set(new FastStack(INITIAL_STACK_DEPTH));
        return oldValue;
    }

    public static void set(Object stack) {
        eventURLStack.set(stack);
    }


}


