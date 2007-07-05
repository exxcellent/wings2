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
import org.wings.resource.ResourceManager;
import java.io.Serializable;
import java.util.Properties;

/**
 * A Look-and-Feel consists of a bunch of CGs and resource properties.
 * wingS provides a pluggable look-and-feel (laf or plaf) concept similar to that of Swing.
 * A certain plaf implementation normally adresses a specific browser platform.
 *
 * @see org.wings.plaf.ComponentCG
 */
public class LookAndFeel implements Serializable {
    private final transient static Log log = LogFactory.getLog(LookAndFeel.class);

    protected Properties properties;

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
    public ResourceDefaults createDefaults() {
        return new ResourceDefaults(ResourceManager.RESOURCES, properties);
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
}
