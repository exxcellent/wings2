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
package org.wings.session;

import java.util.Map;

/**
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 */
public interface PropertyService {
    /**
     * Gets the session property indicated by the specified key.
     *
     * @param key the name of the session property.
     * @return the string value of the session property,
     *         or <code>null</code> if there is no property with that key.
     * @see org.wings.session.PropertyService#getProperties()
     */
    Object getProperty(String key);

    /**
     * Gets the session property indicated by the specified key.
     *
     * @param key the name of the session property.
     * @param def a default value.
     * @return the string value of the session property,
     *         or the default value if there is no property with that key.
     * @see org.wings.session.PropertyService#getProperties()
     */
    Object getProperty(String key, Object def);

    /**
     * Sets the session property indicated by the specified key.
     *
     * @param key   the name of the session property.
     * @param value the value of the session property.
     * @return the previous value of the session property,
     *         or <code>null</code> if it did not have one.
     * @see org.wings.session.PropertyService#getProperty(java.lang.String)
     * @see org.wings.session.PropertyService#getProperty(java.lang.String, java.lang.Object)
     */
    Object setProperty(String key, Object value);

    Map getProperties();

    Object removeProperty(String key);

    boolean containsProperty(String key);
}


