/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
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
package org.wings.template;

import org.wings.SComponent;

/**
 * Property Manager for template managed components.
 * Components managed by TemplateLayout - Managers may have certain
 * properties which can be set from parameters given in the template
 * page, e.g. a 'text' parameter for a 'Button'-Component.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public interface PropertyManager {

    /**
     * Describe <code>setProperty</code> method here.
     *
     * @param comp  a <code>SComponent</code> value
     * @param name  a <code>String</code> value
     * @param value a <code>String</code> value
     */
    public void setProperty(SComponent comp, String name, String value);

    /**
     * Describe <code>getSupportedClasses</code> method here.
     *
     * @return a <code>Class[]</code> value
     */
    public Class[] getSupportedClasses();
}


