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
package org.wings;

import org.wings.plaf.ScrollBarCG;

/**
 * Represents a scroll bar as used in a {@link SScrollPane}.
 * In contrast to {@link SPageScroller} this class is a graphical scroller component.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 * @see SScrollPane
 */
public class SScrollBar extends SAbstractAdjustable {
    boolean marginVisisble;
    boolean stepVisisble;
    boolean blockVisisble;

    /**
     * Creates a scrollbar with the specified orientation, value, extent, mimimum, and maximum.
     * The "extent" is the size of the viewable area. It is also known as the "visible amount".
     * <p/>
     * Note: Use <code>setBlockIncrement</code> to set the block
     * increment to a size slightly smaller than the view's extent.
     * That way, when the user jumps the knob to an adjacent position,
     * one or two lines of the original contents remain in view.
     *
     * @throws IllegalArgumentException if orientation is not one of VERTICAL, HORIZONTAL
     * @see #setOrientation
     * @see #setValue
     * @see #setVisibleAmount
     * @see #setMinimum
     * @see #setMaximum
     */
    public SScrollBar(int orientation, int value, int extent, int min, int max) {
        super(value, extent, min, max);
        setOrientation(orientation);
    }

    /**
     * Creates a scrollbar with the specified orientation
     * and the following initial values:
     * <pre>
     * minimum = 0
     * maximum = 100
     * value = 0
     * extent = 10
     * </pre>
     */
    public SScrollBar(int orientation) {
        this(orientation, 0, 10, 0, 100);
    }

    /**
     * Creates a vertical scrollbar with the following initial values:
     * <pre>
     * minimum = 0
     * maximum = 100
     * value = 0
     * extent = 10
     * </pre>
     */
    public SScrollBar() {
        this(SConstants.VERTICAL);
    }

    public boolean isMarginVisisble() {
        return marginVisisble;
    }

    public void setMarginVisisble(boolean marginVisisble) {
        this.marginVisisble = marginVisisble;
    }

    public boolean isStepVisisble() {
        return stepVisisble;
    }

    public void setStepVisisble(boolean stepVisisble) {
        this.stepVisisble = stepVisisble;
    }

    public boolean isBlockVisisble() {
        return blockVisisble;
    }

    public void setBlockVisisble(boolean blockVisisble) {
        this.blockVisisble = blockVisisble;
    }

    public void setCG(ScrollBarCG cg) {
        super.setCG(cg);
    }
}
