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

import org.wings.SComponent;
import org.wings.SList;

/**
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 * @version $Revision$
 */
public class SListPropertyManager extends SComponentPropertyManager {
    static final Class[] classes = {SList.class};

    public SListPropertyManager() {
    }

    public void setProperty(SComponent comp, String name, String value) {
        SList l = (SList) comp;
        if (name.equals("SIZE")) {
            l.setVisibleRowCount(Integer.valueOf(value).intValue());
        } else
            super.setProperty(comp, name, value);
    }

    public Class[] getSupportedClasses() {
        return classes;
    }
}


