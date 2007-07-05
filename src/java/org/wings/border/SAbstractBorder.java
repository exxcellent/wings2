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
package org.wings.border;

import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.style.CSSAttributeSet;
import org.wings.style.CSSProperty;
import org.wings.style.CSSStyleSheet;

import java.awt.*;

/**
 * This is a an abstract implementation of the <code>SBorder</code>
 * interface.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 */
public abstract class SAbstractBorder
    implements SBorder
{
    protected BorderSpec[] specs = new BorderSpec[] {
        new BorderSpec(),
        new BorderSpec(),
        null,
        null,
        new BorderSpec(),
        new BorderSpec(),
    };

    protected Insets insets;

    private CSSAttributeSet attributes = new CSSAttributeSet();
    protected SComponent component;

    public SAbstractBorder() {
        this(null, -1, null);
    }

    public SAbstractBorder(Color c, int thickness, Insets insets) {
        setInsets(insets);
        setColor(c);
        setThickness(thickness);
    }

    public SAbstractBorder(Insets insets) {
        this(null, -1, insets);
    }

    public SAbstractBorder(Color c) {
        this(c, 1, null);
    }

    public SAbstractBorder(int thickness) {
        this(null, thickness, null);
    }

    public void setComponent(SComponent newComponent) {
        if (this.component != newComponent) {
            if (this.component != null && this.component.getBorder() == this) {
                final SComponent origOwner = this.component;
                this.component = newComponent; // avoid loopback
                origOwner.setBorder(null);
            } else {
                this.component = newComponent;
            }
        }
    }

    /**
     * Set the insets/padding of the border.
     * <p>
     * <b>NOTE:</b> Some platforms (namely: Microsoft Internet Explorer) do not support padding in
     * rare components as they require a workaround.
     */
    public void setInsets(Insets insets) {
        this.insets = insets;
        attributes = null;
        if (component != null)
            component.getSession().getReloadManager().reload(component);
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
        return getColor(SConstants.TOP);
    }

    public Color getColor(int position) {
        return specs[position].color;
    }

    /**
     * sets the foreground color of the border
     */
    public void setColor(Color color) {
        setColor(color, SConstants.TOP);
        setColor(color, SConstants.LEFT);
        setColor(color, SConstants.RIGHT);
        setColor(color, SConstants.BOTTOM);
    }

    public void setColor(Color color, int position) {
        specs[position].color = color;
        attributes = null;
        if (component != null)
            component.getSession().getReloadManager().reload(component);
    }

    /**
     * set the thickness of the border
     * thickness must be > 0
     */
    public void setThickness(int thickness) {
        setThickness(thickness, SConstants.TOP);
        setThickness(thickness, SConstants.LEFT);
        setThickness(thickness, SConstants.RIGHT);
        setThickness(thickness, SConstants.BOTTOM);
    }

    public void setThickness(int thickness, int position) {
        specs[position].thickness = thickness;
        attributes = null;
        if (component != null)
            component.getSession().getReloadManager().reload(component);
    }

    /**
     * @return thickness in pixels
     */
    public final int getThickness() {
        return getThickness(SConstants.TOP);
    }

    public int getThickness(int position) {
        return specs[position].thickness;
    }

    /**
     * set the style of the border
     * style must be > 0
     */
    public void setStyle(String style) {
        setStyle(style, SConstants.TOP);
        setStyle(style, SConstants.LEFT);
        setStyle(style, SConstants.RIGHT);
        setStyle(style, SConstants.BOTTOM);
    }

    public void setStyle(String style, int position) {
        specs[position].style = style;
        attributes = null;
        if (component != null)
            component.getSession().getReloadManager().reload(component);
    }

    /**
     * @return style in pixels
     */
    public final String getStyle() {
        return getStyle(SConstants.TOP);
    }

    public String getStyle(int position) {
        return specs[position].style;
    }

    public CSSAttributeSet getAttributes() {
        if (attributes == null) {
            attributes = new CSSAttributeSet();
            if (insets != null) {
                if (insets.top == insets.left && insets.left == insets.right && insets.right == insets.bottom) {
                    attributes.put(CSSProperty.PADDING, insets.top + "px");
                }
                else {
                    if (insets.top > 0)
                        attributes.put(CSSProperty.PADDING_TOP, insets.top + "px");
                    if (insets.left > 0)
                        attributes.put(CSSProperty.PADDING_LEFT, insets.left + "px");
                    if (insets.right > 0)
                        attributes.put(CSSProperty.PADDING_RIGHT, insets.right + "px");
                    if (insets.bottom > 0)
                        attributes.put(CSSProperty.PADDING_BOTTOM, insets.bottom + "px");
                }
            }

            BorderSpec top = specs[SConstants.TOP];
            BorderSpec left = specs[SConstants.LEFT];
            BorderSpec right = specs[SConstants.RIGHT];
            BorderSpec bottom = specs[SConstants.BOTTOM];

            if (this instanceof SEmptyBorder)
                attributes.put(CSSProperty.BORDER, "none");
            if (top.thickness == left.thickness && left.thickness == right.thickness && right.thickness == bottom.thickness
                    && top.style != null && top.style.equals(left.style)
                    && left.style != null && left.style.equals(right.style)
                    && right.style != null && right.style.equals(bottom.style)
                    && ((top.color != null && top.color.equals(left.color)
                    && left.color != null && left.color.equals(right.color)
                    && right.color != null && right.color.equals(bottom.color))
                    || (top.color == null && right.color == null && left.color == null && bottom.color == null)))
            {
                attributes.put(CSSProperty.BORDER, top.thickness + "px " + top.style + " " + (top.color != null ? CSSStyleSheet.getAttribute(top.color) : ""));
            }
            else {
                if (top.thickness > 0 && top.style != null)
                    attributes.put(CSSProperty.BORDER_TOP, top.thickness + "px " + top.style + " " + (top.color != null ? CSSStyleSheet.getAttribute(top.color) : ""));

                if (left.thickness > 0 && left.style != null)
                    attributes.put(CSSProperty.BORDER_LEFT, left.thickness + "px " + left.style + " " + (left.color != null ? CSSStyleSheet.getAttribute(left.color) : ""));

                if (right.thickness > 0 && right.style != null)
                    attributes.put(CSSProperty.BORDER_RIGHT, right.thickness + "px " + right.style + " " + (right.color != null ? CSSStyleSheet.getAttribute(right.color) : ""));

                if (bottom.thickness > 0 && bottom.style != null)
                    attributes.put(CSSProperty.BORDER_BOTTOM, bottom.thickness + "px " + bottom.style + " " + (bottom.color != null ? CSSStyleSheet.getAttribute(bottom.color) : ""));
            }
        }
        return attributes;
    }


    static class BorderSpec {
        public int thickness;
        public String style;
        public Color color;
    }

    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
