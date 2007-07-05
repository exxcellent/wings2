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
package org.wings.plaf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.SComponent;
import org.wings.SIcon;
import org.wings.SLayoutManager;
import org.wings.session.PropertyService;
import org.wings.session.SessionManager;
import org.wings.session.Session;
import org.wings.style.Style;
import org.wings.style.StyleSheet;

import java.io.Serializable;

/**
 * The CGManager holds a reference to the current laf.
 * It delegates to a session related CGDefaults table, that is backed by
 * the laf's defaults.
 */
public class CGManager implements Serializable {
    private final transient static Log log = LogFactory.getLog(CGManager.class);

    private LookAndFeel lookAndFeel;
    private ResourceDefaults defaults = null;

    /**
     * Get an Object from the defaults table.
     * If the there's no value associated to the <code>key</code>, the request
     * is delegated to the laf's defaults table.
     *
     * @param key the lookup key
     */
    public Object get(String key) {
        return getDefaults().get(key);
    }

    /**
     * Get an Object from the defaults table.
     * If the there's no value associated to the <code>key</code>, the request
     * is delegated to the laf's defaults table.
     *
     * @param key the lookup key
     * @param clazz the class of the object in question
     */
    public Object getObject(String key, Class clazz) {
        return getDefaults().get(key, clazz);
    }

    /**
     * Get a ComponentCG from the defaults table.
     * If the there's no value associated to the <code>target</code>'s cgClassID, the request
     * is delegated to the laf's defaults table.
     *
     * @param target the SComponent
     */
    public ComponentCG getCG(SComponent target) {
        return (ComponentCG) getDefaults().get(target.getClass(), ComponentCG.class);
    }

    /**
     * Get a ComponentCG from the defaults table.
     * If the there's no value associated to the <code>target</code>'s cgClassID, the request
     * is delegated to the laf's defaults table.
     *
     * @param targetClass the SComponent class
     */
    public ComponentCG getCG(Class targetClass) {
        return (ComponentCG) getDefaults().get(targetClass, ComponentCG.class);
    }


    /**
     * Get a ComponentCG from the defaults table.
     * If the there's no value associated to the <code>target</code>'s cgClassID, the request
     * is delegated to the laf's defaults table.
     *
     * @param cgClassID the cg class id
     */
    public ComponentCG getCG(String cgClassID) {
        return (ComponentCG) getDefaults().get(cgClassID, ComponentCG.class);
    }

    /**
     * Get a LayoutManagerCG from the defaults table.
     * If the there's no value associated to the <code>target</code>'s cgClassID, the request
     * is delegated to the laf's defaults table.
     *
     * @param target the SLayoutManager
     */
    public LayoutCG getCG(SLayoutManager target) {
        return (LayoutCG) getDefaults().get(target.getClass(), LayoutCG.class);
    }

    /**
     * Get a StyleSheet from the defaults table.
     * If the there's no value associated to the <code>key</code>, the request
     * is delegated to the laf's defaults table.
     *
     * @param key the lookup key
     */
    public StyleSheet getStyleSheet(String key) {
        return (StyleSheet) getDefaults().get(key, StyleSheet.class);
    }

    /**
     * Get a Style from the defaults table.
     * If the there's no value associated to the <code>key</code>, the request
     * is delegated to the laf's defaults table.
     *
     * @param key the lookup key
     */
    public Style getStyle(String key) {
        return (Style) getDefaults().get(key, Style.class);
    }

    /**
     * Get a Icon from the defaults table.
     * If the there's no value associated to the <code>key</code>, the request
     * is delegated to the laf's defaults table.
     *
     * @param key the lookup key
     */
    public SIcon getIcon(String key) {
        return (SIcon) getDefaults().get(key, SIcon.class);
    }

    /**
     * Set the defaults table.
     *
     * @param defaults the defaults table
     */
    public void setDefaults(ResourceDefaults defaults) {
        this.defaults = defaults;
    }

    /**
     * Return the defaults table.
     *
     * @return the defaults table
     */
    public ResourceDefaults getDefaults() {
        if (defaults == null) {
            log.warn("defaults == null");
        }
        return defaults;
    }


    /**
     * Returns the current default look and feel.
     *
     * @return the current default look and feel
     * @see #setLookAndFeel
     */
    public LookAndFeel getLookAndFeel() {
        return lookAndFeel;
    }

    /**
     * Set the current default look and feel using a LookAndFeel object.
     *
     * @param newLookAndFeel the LookAndFeel object
     * @see #getLookAndFeel
     */
    public void setLookAndFeel(LookAndFeel newLookAndFeel) {
        lookAndFeel = newLookAndFeel;

        if (newLookAndFeel != null) {
            setDefaults(newLookAndFeel.createDefaults());
        } else {
            log.warn("lookandfeel == null");
            setDefaults(null);
        }

        // have the session fire a propertyChangeEvent regarding the new lookAndFeel
        Session session = SessionManager.getSession();
        if (session != null) {
            ((PropertyService) session).setProperty("lookAndFeel", "" + newLookAndFeel.hashCode());
        }
    }
}
