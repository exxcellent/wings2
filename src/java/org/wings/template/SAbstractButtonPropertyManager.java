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

import org.wings.SAbstractButton;
import org.wings.SComponent;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SAbstractButtonPropertyManager extends SAbstractIconTextCompoundPropertyManager {
    static final Class[] classes = {SAbstractButton.class};

    public SAbstractButtonPropertyManager() {
    }

    public void setProperty(SComponent comp, String name, String value) {
        SAbstractButton c = (SAbstractButton) comp;
        if (name.equals("ACCESSKEY"))
            c.setMnemonic(value);
        else if (name.equals("TARGET"))
            c.setEventTarget(value);
        else
            super.setProperty(comp, name, value);
    }

    public Class[] getSupportedClasses() {
        return classes;
    }
}


