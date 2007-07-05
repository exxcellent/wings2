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


/**
 * A scroller component offering several pages for selection.
 * <p/>
 * Apply i.e. to {@link SScrollPane} like <br/>
 * <code>
 *    scrollbar = new SPageScroller(Adjustable.VERTICAL);
 *    scrollPane.setHorizontalScrollBar(scrollbar, SScrollPaneLayout.NORTH);
 * </code>
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class SPageScroller extends SAbstractAdjustable {
    private static final int DEFAULT_DIRECT_PAGES = 10;

    private boolean marginVisible;

    private boolean stepVisible;

    /**
     * Actual amount of page clickables; depends on the number of elemnts in the
     * model and on the models extent.
     *
     * @see #setDirectPages
     */
    protected int directPages = DEFAULT_DIRECT_PAGES;

    /*
     * how to layout the scroller, vertical or horizontal
     * @see #setLayoutMode
     */
    protected int layoutMode;

    /**
     * contains the clickables forward, backward, first, last
     */
    protected SClickable[] clickables = new SClickable[4];

    /**
     * contains the direct page clickables. Size of this array is extend
     */
    protected SClickable[] directPageClickables;

    protected SLabel pageCountLabel = new SLabel();

    /**
     * Creates a scrollbar with the specified orientation,
     * value, extent, mimimum, and maximum.
     * The "extent" is the size of the viewable area. It is also known
     * as the "visible amount".
     *
     * @throws IllegalArgumentException if orientation is not one of VERTICAL, HORIZONTAL
     * @see #setOrientation
     * @see #setValue
     * @see #setVisibleAmount
     * @see #setMinimum
     * @see #setMaximum
     */
    public SPageScroller(int orientation, int value, int extent, int min, int max) {
        super(new SPagingBoundedRangeModel(value, extent, min, max));
        unitIncrement = extent;
        blockIncrement = extent;

        for (int i = 0; i < clickables.length; i++) {
            clickables[i] = new SClickable();
            clickables[i].setEventTarget(this);
        }

        setOrientation(orientation);
        setMarginVisible(false);
        setHorizontalAlignment(SConstants.CENTER);
        setVerticalAlignment(SConstants.CENTER);
        setEpochCheckEnabled(false);
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
    public SPageScroller(int orientation) {
        this(orientation, 0, 1, 0, 100);
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
    public SPageScroller() {
        this(SConstants.VERTICAL);
    }

    public SLabel getPageCountLabel() {
        return pageCountLabel;
    }

    protected void setPageCountText(int pages) {
        pageCountLabel.setText("/" + pages);
    }

    public int getLayoutMode() {
        return layoutMode;
    }

    /**
     * set how to layout components
     * {@link #VERTICAL} or {@link #HORIZONTAL}
     */
    public void setLayoutMode(int orientation) {
        switch (orientation) {
            case SConstants.VERTICAL:
            case SConstants.HORIZONTAL:
                layoutMode = orientation;
                break;
            default:
                throw new IllegalArgumentException("layout mode must be one of: VERTICAL, HORIZONTAL");
        }
    }

    /**
     * Sets the amount of page clickables to <code>count</code>.
     */
    public final int getDirectPages() {
        return directPages;
    }

    /**
     * Sets the amount of page clickables to <code>count</code>.
     *
     * @param count : New amount of page clickables.
     */
    public void setDirectPages(int count) {
        reloadIfChange(directPages, count);
        directPages = count;
    }

    public final int getPageCount() {
        // avoid division by zero
        if (getExtent() == 0)
            return 0;
        return ((getMaximum()) - getMinimum() + (getExtent() - 1)) / getExtent();
    }

    /**
     * gets the current page number according to the Position we are
     * in.
     *
     * @return the current page number
     */
    public final int getCurrentPage() {
        // avoid division by zero
        if (getExtent() == 0)
            return 0;

        return (getValue() - getMinimum() + getExtent() - 1) / getExtent();
    }

    protected String formatDirectPageLabel(int page) {
        return Integer.toString(page + 1);
    }


    public boolean isMarginVisible() {
        return marginVisible;
    }

    public void setMarginVisible(boolean marginVisible) {
        this.marginVisible = marginVisible;
    }

    public boolean isStepVisible() {
        return stepVisible;
    }

    public void setStepVisible(boolean stepVisible) {
        this.stepVisible = stepVisible;
    }

    /**
     * Set the visible amount of the scroller. This sets also the
     * unitincrement and the blockIncrement!
     *
     * @param value the new extent
     */
    public void setExtent(int value) {
        super.setExtent(value);
        unitIncrement = value;
        blockIncrement = value;
        // make sure we have a valid value!
        setValue(getValue());
    }

    /**
     * Set the visible amount of the scroller. This sets also the
     * unitincrement and the blockIncrement!
     *
     * @param value the new extent
     */
    public void setVisibleAmount(int value) {
        super.setVisibleAmount(value);
        unitIncrement = value;
        blockIncrement = value;
        // make sure we have a valid value!
        setValue(getValue());
    }

    /**
     * Set the current value of the scroller. The value will be
     * adjusted to a multiple of the extent.
     *
     * @param value the new value
     */
    public void setValue(int value) {
        super.setValue(value);
    }
}
