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

import org.wings.SFont;
import org.wings.style.CSSProperty;
import org.wings.style.CSSAttributeSet;
import org.wings.style.CSSStyleSheet;

import java.awt.*;

/**
 * @version $Revision$
 */
public class STitledBorder extends SAbstractBorder {
    private SBorder border;

    private String title;
    private CSSAttributeSet titleAttributes = new CSSAttributeSet();

    /**
     * Constructor for STitledBorder.
     */
    public STitledBorder(SBorder border) {
        setBorder(border);
        attributes.put(CSSProperty.PADDING_TOP, "1em");
    }

    /**
     * Constructor for STitledBorder.
     *
     * @param border the border to use
     * @param title  the title to display
     */
    public STitledBorder(SBorder border, String title) {
        this(border);
        setTitle(title);
    }

    /**
     * Constructor for STitledBorder. Default border
     * type is {@link SEtchedBorder}, thickness 2
     */
    public STitledBorder(String title) {
        this(new SEtchedBorder(SEtchedBorder.LOWERED));
        border.setThickness(2);
        setTitle(title);
    }

    /**
     * Gets the border.
     *
     * @return Returns a SBorder
     */
    public SBorder getBorder() {
        return border;
    }

    /**
     * Sets the border.
     *
     * @param border The border to set
     */
    public void setBorder(SBorder border) {
        this.border = border;
        attributes = border.getAttributes();
    }

    /**
     * Gets the title.
     *
     * @return Returns a String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title The title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public CSSAttributeSet getTitleAttributes() {
        return titleAttributes;
    }

    /**
     * Gets the title color.
     *
     * @return Returns a Color
     */
    public Color getTitleColor() {
        return CSSStyleSheet.getForeground(titleAttributes);
    }

    /**
     * Sets the title color.
     *
     * @param color the title color to set
     */
    public void setTitleColor(Color color) {
        titleAttributes.put(CSSProperty.COLOR, CSSStyleSheet.getAttribute(color));
    }

    /**
     * Set the border color.
     */
    public void setTitleBackground(Color color) {
        titleAttributes.put(CSSProperty.BACKGROUND_COLOR, CSSStyleSheet.getAttribute(color));
    }

    /**
     * Get the border color.
     *
     * @return the color or <code>null</code>,
     *         if border is <code>null</code>
     * @see #setBorder(SBorder)
     */
    public Color getTitleBackground() {
        return CSSStyleSheet.getBackground(titleAttributes);
    }

    /**
     * Gets the title font.
     *
     * @return Returns a SFont
     */
    public SFont getTitleFont() {
        return CSSStyleSheet.getFont(titleAttributes);
    }

    /**
     * Sets the title font.
     *
     * @param titleFont The title font to set
     */
    public void setTitleFont(SFont titleFont) {
        titleAttributes.putAll(CSSStyleSheet.getAttributes(titleFont));
    }
}
