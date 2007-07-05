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
import org.wings.SFileChooser;
import org.wings.template.propertymanagers.SComponentPropertyManager;

/**
 * Property manager for a file chooser component.
 *
 */
public class SFileChooserPropertyManager
        extends SComponentPropertyManager {
    static final Class[] classes = {SFileChooser.class};

    public SFileChooserPropertyManager() {
    }

    public void setProperty(SComponent comp, String name, String value) {
        SFileChooser c = (SFileChooser) comp;
        if (name.equals("SIZE") || name.equals("COLS"))
            c.setColumns(Integer.parseInt(value));
        /* maxsize should be the maximum content length, according to
         * RFC 1867. So we should set the sessions' content length here.
         * But people often think this is the maximum number of columns
         * -- so just ignore it.
        else if ( name.equals("MAXSIZE") )
            c.getSession().setMaxContentLength(Integer.parseInt(value)*1024);
        */
        else if (name.equals("ACCEPT") || name.equals("FILTER"))
            c.setFileNameFilter(value);
        else
            super.setProperty(comp, name, value);
    }

    public Class[] getSupportedClasses() {
        return classes;
    }
}


