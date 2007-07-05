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
package org.wings;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Swing-like card layout.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class SCardLayout
        extends SAbstractLayoutManager {
    protected HashMap tab = new HashMap();

    /**
     * Creates a new card layout
     */
    public SCardLayout() {
    }

    public void addComponent(SComponent c, Object constraint, int index) {
        if (tab.size() > 0)
            c.setVisible(false);
        tab.put((constraint != null) ? constraint : c.getName(), c);
    }

    public void removeComponent(SComponent c) {
        for (Iterator e = tab.keySet().iterator(); e.hasNext();) {
            Object key = e.next();
            if (tab.get(key) == c) {
                // if removing the current visible element fall back to previous one
                if (c.isVisible() && (c.getParent() != null)) {
                    next(c.getParent());
                }                
                tab.remove(key);
                return;
            }
        }
    }

    /**
     * Make sure that the Container really has a CardLayout installed.
     * Otherwise havoc can ensue!
     */
    void checkLayout(SContainer parent) {
        if (parent.getLayout() != this) {
            throw new IllegalArgumentException("wrong parent for CardLayout");
        }
    }

    /**
     * Flips to the first card of the container.
     *
     * @param parent the name of the parent container
     *               in which to do the layout.
     */
    public void first(SContainer parent) {
        checkLayout(parent);
        for (int i = 0; i < parent.getComponentCount(); i++)
            if (i > 0)
                parent.getComponent(i).setVisible(false);
            else
                parent.getComponent(i).setVisible(true);
    }

    /**
     * Flips to the next card of the specified container. If the
     * currently visible card is the last one, this method flips to the
     * first card in the layout.
     *
     * @param parent the name of the parent container
     *               in which to do the layout.
     */
    public void next(SContainer parent) {
        checkLayout(parent);
        int ncomponents = parent.getComponentCount();
        for (int i = 0; i < ncomponents; i++) {
            SComponent comp = parent.getComponent(i);
            if (comp.isVisible()) {
                comp.setVisible(false);
                comp = parent.getComponent((i + 1 < ncomponents) ? i + 1 : 0);
                comp.setVisible(true);
                return;
            }
        }
    }

    /**
     * Flips to the previous card of the specified container. If the
     * currently visible card is the first one, this method flips to the
     * last card in the layout.
     *
     * @param parent the name of the parent container
     *               in which to do the layout.
     */
    public void previous(SContainer parent) {
        checkLayout(parent);
        int ncomponents = parent.getComponentCount();
        for (int i = 0; i < ncomponents; i++) {
            SComponent comp = parent.getComponent(i);
            if (comp.isVisible()) {
                comp.setVisible(false);
                comp = parent.getComponent((i > 0) ? i - 1 : ncomponents - 1);
                comp.setVisible(true);
                return;
            }
        }
    }

    /**
     * Flips to the last card of the container.
     *
     * @param parent the name of the parent container
     *               in which to do the layout.
     */
    public void last(SContainer parent) {
        checkLayout(parent);
        int ncomponents = parent.getComponentCount();
        for (int i = 0; i < ncomponents; i++) {
            if (i < ncomponents - 1)
                parent.getComponent(i).setVisible(false);
            else
                parent.getComponent(i).setVisible(true);
        }
    }


    /**
     * Flips to the component
     */
    public void show(SComponent comp) {
        for (Iterator en = tab.values().iterator(); en.hasNext();) {
            SComponent c = (SComponent) en.next();
            c.setVisible(false);
        }
        comp.setVisible(true);
    }

    /**
     * Flips to the component
     */
    public void show(Object constraint) {
        SComponent visibleComponent = (SComponent) tab.get(constraint);
        if (visibleComponent != null) {
            for (Iterator en = tab.values().iterator(); en.hasNext();) {
                SComponent c = (SComponent) en.next();
                c.setVisible(false);
            }
            visibleComponent.setVisible(true);
        }
    }

    /**
     * Flips to the component that was added to this layout with the
     * specified <code>name</code>, using <code>addLayoutComponent</code>.
     * If no such component exists, then nothing happens.
     *
     * @param parent the name of the parent container
     *               in which to do the layout.
     * @param name   the component name.
     */
    public void show(SContainer parent, Object name) {
        checkLayout(parent);
        SComponent next = (SComponent) tab.get(name);
        if ((next != null) && !next.isVisible()) {
            for (int i = 0; i < parent.getComponentCount(); i++)
                parent.getComponent(i).setVisible(false);
            next.setVisible(true);
        }
    }


    public SComponent getVisibleComponent() {
        for (Iterator en = tab.values().iterator(); en.hasNext();) {
            SComponent c = (SComponent) en.next();
            if (c.isVisible())
                return c;
        }
        return null;
    }


    public Object getVisibleConstraint() {
        for (Iterator en = tab.keySet().iterator(); en.hasNext();) {
            Object constraint = en.next();
            SComponent c = (SComponent) tab.get(constraint);
            if (c.isVisible())
                return constraint;
        }
        return null;
    }
}


