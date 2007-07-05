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
import org.wings.style.CSSAttributeSet;

import java.awt.*;
import java.io.Serializable;

/**
 * This is the interface for Borders.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public interface SBorder extends Serializable, Cloneable {
    /**
     * Sets the insets of this border. Insets describe the amount
     * of space 'around' the bordered component.
     *
     * @see #getInsets()
     */
    void setInsets(Insets insets);

    /**
     * Returns the insets of this border.
     *
     * @return Insets specification of the border.
     * @see #setInsets
     */
    Insets getInsets();

    /**
     * Get the color of the border.
     *
     * @return color
     */
    Color getColor();

    /**
     * Get the color of this border.
     *
     * @param color the color
     */
    void setColor(Color color);

    /**
     * Get the thickness in pixel for this border.
     *
     * @return thickness
     * @see #setThickness(int)
     */
    int getThickness();

    /**
     * Set the thickness in pixel for this border.
     *
     * @see #getThickness()
     */
    void setThickness(int thickness);

    /**
     * @return The CSS Attributes which need to be applied to the component to build up the border.
     */
    CSSAttributeSet getAttributes();

    /**
     * @param component The component owning this border
     */
    void setComponent(SComponent component);

    /**
     * Get the color of the border for one of SConstants.TOP, SConstants.LEFT, SConstants.RIGHT or SConstants.BOTTOM.
     * @param position SConstants.TOP, SConstants.LEFT, SConstants.RIGHT or SConstants.BOTTOM
     * @return the color
     */
    Color getColor(int position);

    /**
     * Set the color of the border for one of SConstants.TOP, SConstants.LEFT, SConstants.RIGHT or SConstants.BOTTOM.
     * @param position SConstants.TOP, SConstants.LEFT, SConstants.RIGHT or SConstants.BOTTOM
     * @param color the color
     */
    void setColor(Color color, int position);

    /**
     * Set the thickness of the border for one of SConstants.TOP, SConstants.LEFT, SConstants.RIGHT or SConstants.BOTTOM.
     * @param position SConstants.TOP, SConstants.LEFT, SConstants.RIGHT or SConstants.BOTTOM
     * @param thickness the thickness
     */
    void setThickness(int thickness, int position);

    /**
     * Get the thickness of the border for one of SConstants.TOP, SConstants.LEFT, SConstants.RIGHT or SConstants.BOTTOM.
     * @param position SConstants.TOP, SConstants.LEFT, SConstants.RIGHT or SConstants.BOTTOM
     * @return the thickness
     */
    int getThickness(int position);

    /**
     * Set the style of the border for one of SConstants.TOP, SConstants.LEFT, SConstants.RIGHT or SConstants.BOTTOM.
     * @param position SConstants.TOP, SConstants.LEFT, SConstants.RIGHT or SConstants.BOTTOM
     * @param style the style
     */
    void setStyle(String style, int position);

    /**
     * Get the style of the border for one of SConstants.TOP, SConstants.LEFT, SConstants.RIGHT or SConstants.BOTTOM.
     * @param position SConstants.TOP, SConstants.LEFT, SConstants.RIGHT or SConstants.BOTTOM
     * @return the style
     */
    String getStyle(int position);
}
