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
    extends SAbstractBorder
{

    public static final String DOTTED = "dotted";
    public static final String DASHED = "dashed";
    public static final String SOLID = "solid";

    private String borderStyle = SOLID;

    /**
     * TODO: documentation
     *
     */
    public SLineBorder() {}

    /**
     * TODO: documentation
     *
     */
    public SLineBorder(int thickness) {
        super(thickness);
    }

    /**
     * TODO: documentation
     *
     */
    public SLineBorder(Color c) {
        super(c);
    }

    /**
     * TODO: documentation
     *
     */
    public SLineBorder(Color c, String borderStyle) {
        super(c);

        setBorderStyle(borderStyle);
    }

    /**
     * TODO: documentation
     *
     * @param thickness
     * @param insets
     */
    public SLineBorder(int thickness, Insets insets) {
        super(Color.black, thickness, insets);
    }

    /**
     * TODO: documentation
     *
     * @param style
     */
    public void setBorderStyle(String style) {
        this.borderStyle = style;
    }

    /**
     * TODO: documentation
     *
     * @return thickness in pixels
     */
    public final String getBorderStyle() { return borderStyle; }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
