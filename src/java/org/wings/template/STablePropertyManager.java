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
import org.wings.SDimension;
import org.wings.STable;

import java.awt.*;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class STablePropertyManager
        extends SComponentPropertyManager {
    static final Class[] classes = {STable.class};

    public STablePropertyManager() {
    }

    public void setProperty(SComponent comp, String name, String value) {
        STable t = (STable) comp;
        if (name.equals("GRID"))
            t.setShowGrid(Boolean.valueOf(value).booleanValue());
        else if (name.equals("CELLSPACING"))
            t.setIntercellSpacing(new SDimension(value, value));
        else if (name.equals("CELLPADDING"))
            t.setIntercellPadding(new SDimension(value, value));
        else if (name.equals("SELECTION_FOREGROUND"))
            t.setSelectionForeground(Color.decode(value));
        else if (name.equals("SELECTION_BACKGROUND"))
            t.setSelectionBackground(Color.decode(value));
        else
            super.setProperty(comp, name, value);
    }

    public Class[] getSupportedClasses() {
        return classes;
    }
}


