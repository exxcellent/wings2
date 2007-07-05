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
package org.wings.template.propertymanagers;

import org.wings.SComponent;
import org.wings.STextArea;
import org.wings.template.propertymanagers.SComponentPropertyManager;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class STextAreaPropertyManager
        extends SComponentPropertyManager {
    static final Class[] classes = {STextArea.class};

    public STextAreaPropertyManager() {
    }

    public void setProperty(SComponent comp, String name, String value) {
        STextArea c = (STextArea) comp;
        if (name.equals("COLS"))
            c.setColumns(Integer.parseInt(value));
        else if (name.equals("ROWS"))
            c.setRows(Integer.parseInt(value));
        else
            super.setProperty(comp, name, value);
    }

    public Class[] getSupportedClasses() {
        return classes;
    }
}


