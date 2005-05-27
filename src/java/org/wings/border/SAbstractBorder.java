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
package org.wings.border;

import org.wings.style.CSSAttributeSet;
import org.wings.style.CSSStyleSheet;
import org.wings.style.CSSProperty;

import java.awt.*;

/**
 * This is a an abstract implementation of the <code>SBorder</code>
 * interface.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public abstract class SAbstractBorder
        implements SBorder {
    /**
     * the insets
     */
    private Insets insets;

    /**
     * border color
     */
    private Color color;

    /**
     * border thickness
     */
    private int thickness;

    protected CSSAttributeSet attributes = new CSSAttributeSet();

    public SAbstractBorder() {
        this(Color.black, 2, new Insets(0, 0, 0, 0));
    }

    public SAbstractBorder(Color c, int thickness, Insets insets) {
        setInsets(insets);
        setColor(c);
        setThickness(thickness);
    }

    public SAbstractBorder(Insets insets) {
        this(null, 0, insets);
    }

    public SAbstractBorder(Color c) {
        this(c, 2, new Insets(0, 0, 0, 0));
    }

    public SAbstractBorder(int thickness) {
        this(Color.black, thickness, new Insets(0, 0, 0, 0));
    }

    /**
     * set the insets of the border
     */
    public void setInsets(Insets insets) {
        this.insets = insets;
        if (insets != null) {
            attributes.put(CSSProperty.PADDING_TOP, insets.top + "px");
            attributes.put(CSSProperty.PADDING_LEFT, insets.left + "px");
            attributes.put(CSSProperty.PADDING_RIGHT, insets.right + "px");
            attributes.put(CSSProperty.PADDING_BOTTOM, insets.bottom + "px");
        }
        else {
            attributes.remove(CSSProperty.PADDING_TOP);
            attributes.remove(CSSProperty.PADDING_LEFT);
            attributes.remove(CSSProperty.PADDING_RIGHT);
            attributes.remove(CSSProperty.PADDING_BOTTOM);
        }
    }

    /**
     * @return the insets of the border
     */
    public final Insets getInsets() {
        return insets;
    }

    /**
     * sets the foreground color of the border
     */
    public Color getColor() {
        return color;
    }

    /**
     * sets the foreground color of the border
     */
    public void setColor(Color color) {
        this.color = color;
        attributes.put(CSSProperty.BORDER_COLOR, CSSStyleSheet.getAttribute(color));
    }

    /**
     * set the thickness of the border
     * thickness must be > 0
     */
    public void setThickness(int thickness) {
        this.thickness = thickness;
        attributes.put(CSSProperty.BORDER_WIDTH, thickness + "px");
    }

    public CSSAttributeSet getAttributes() {
        return attributes;
    }

    /**
     * @return thickness in pixels
     */
    public final int getThickness() {
        return thickness;
    }

}
