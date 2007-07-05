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
import org.wings.template.TemplateUtil;
import org.wings.template.PropertyValueConverter;
import org.wings.style.CSSAttributeSet;

import java.awt.*;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class SComponentPropertyManager extends DefaultPropertyManager {
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
            PropertyValueConverter valueConverter = getValueConverter(CSSAttributeSet.class);
            comp.setAttributes(SComponent.SELECTOR_ALL, (CSSAttributeSet) valueConverter.convertPropertyValue(value, CSSAttributeSet.class));
        } else if ("CLASS".equals(name)) {
            comp.setStyle(value);
        } else if ("ALIGN".equals(name)) {
            comp.setHorizontalAlignment(TemplateUtil.parseAlignment(value));
        } else if ("VALIGN".equals(name)) {
            comp.setVerticalAlignment(TemplateUtil.parseAlignment(value));
        } else {
            super.setProperty(comp, name, value);
        } // end of else

    }

    public Class[] getSupportedClasses() {
        return classes;
    }
}


