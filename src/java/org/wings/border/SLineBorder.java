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
package org.wings.border;

import java.awt.*;

/**
 * Draw a line border around a component.
 * <span style="border-style: solid; border-width: 3px;">LABEL</span>
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @version $Revision$
 */
public class SLineBorder
        extends SAbstractBorder {

    public static final String DOTTED = "dotted";
    public static final String DASHED = "dashed";
    public static final String SOLID = "solid";

    private String borderStyle = SOLID;

    public SLineBorder() {
        setBorderStyle(SOLID);
    }

    public SLineBorder(int thickness) {
        super(thickness);
        setBorderStyle(SOLID);
    }

    public SLineBorder(Color c) {
        super(c);
        setBorderStyle(SOLID);
    }

    public SLineBorder(Color c, String borderStyle) {
        super(c);
        setBorderStyle(borderStyle);
        setBorderStyle(SOLID);
    }

    public SLineBorder(int thickness, Insets insets) {
        super(Color.black, thickness, insets);
        setBorderStyle(SOLID);
    }

    public void setBorderStyle(String style) {
        this.borderStyle = style;
        attributes.put("border-style", style);
    }

    public final String getBorderStyle() { return borderStyle; }
}


