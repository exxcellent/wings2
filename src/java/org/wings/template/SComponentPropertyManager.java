/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
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
import org.wings.style.AttributeSet;

import java.awt.*;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SComponentPropertyManager
        extends DefaultPropertyManager {
    static final Class[] classes = {SComponent.class};

    public SComponentPropertyManager() {
    }

    public void setProperty(SComponent comp, String name, String value) {
        if ("BACKGROUND".equals(name))
            comp.setBackground(Color.decode(value));
        else if ("FOREGROUND".equals(name))
            comp.setForeground(Color.decode(value));
        else if ("FONT".equals(name))
            comp.setFont(TemplateUtil.parseFont(value));
        else if ("TABINDEX".equals(name))
            comp.setFocusTraversalIndex(Integer.parseInt(value));
        else if ("STYLE".equals(name)) {
            PropertyValueConverter valueConverter = getValueConverter(AttributeSet.class);
            comp.setAttributes((AttributeSet) valueConverter.convertPropertyValue(value, AttributeSet.class));
        } else if ("CLASS".equals(name)) {
            comp.setStyle(value);
        } else {
            super.setProperty(comp, name, value);
        } // end of else

    }

    public Class[] getSupportedClasses() {
        return classes;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
