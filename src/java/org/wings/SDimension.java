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
package org.wings;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParsePosition;

/**
 * This class hold preferred component sizes (dimensions).
 * <p/>
 * Web browsers support different notations for sizes. Absolute pixel values, realtive percentages (of the available
 * viewport) and special CSS like 'inherit' and 'auto' (Default).
 * This class if capable of handling all these cases.
 */
public class SDimension
        implements Serializable {

    /**
     * Integer constant for CSS dimension 'auto'.
     * This is the default width used by wings as well as by CSS capable browsers.
     */
    public final static int AUTO_VALUE = -1;

    /**
     * String constant for CSS dimension 'auto'.
     * This is the default width used by wings as well as by CSS capable browsers.
     */
    public final static String AUTO = "auto";

    /**
     * Integer constant for CSS dimension 'inherit'.
     * With this value the preferred size is inherited by the surrounding element.
     */
    public final static int INHERIT_VALUE = -2;

    /**
     * String for CSS dimension 'inherit'.
     * With this value the preferred size is inherited by the surrounding element.
     */
    public final static String INHERIT = "inherit";

    /**
     * A convenience String constant for a full size width/height ("100%").
     */
    public final static String FULL_SIZE = "100%";


    private final transient static Log log = LogFactory.getLog(SDimension.class);
    private int width = AUTO_VALUE;
    private int height = AUTO_VALUE;
    private boolean widthIsAbsolute = false;
    private boolean heightIsAbsolute = false;

    public SDimension() {
    }

    public SDimension(String width, String height) {
        setWidth(width);
        setHeight(height);
    }

    /**
     * Construct a new dimension with absolute values.
     * The value is converted to "<value>px".
     *
     * @param width  the width. If value is less than <code>0</code> width
     *               is set to <code>null</code>.
     * @param height the height. If value is less than <code>0</code> height
     *               is set to <code>null</code>.
     * @see #setSize(int,int)
     */
    public SDimension(int width, int height) {
        setSize(width, height);
    }

    /**
     * Set raw string value to be used inside the generated HTML code. S
     *
     * @param widthString Desired widthString. Should be some form of <code>123px</code> or <code>123%</code>
     */
    public void setWidth(String widthString) {
        this.width = getIntValue(widthString);
        this.widthIsAbsolute = isAbsoluteSize(widthString);
    }

    /**
     * Set width in pixel.
     * This appends "px" to the integer value.
     *
     * @param width Takes also {@link SDimension.AUTO} and {@link SDimension.INHERIT}
     */
    public void setWidth(int width) {
        setWidth(width, true);
    }


    /**
     * Set width in pixel.
     * This appends "px" to the integer value.
     *
     * @param width      Takes also {@link SDimension.AUTO} and {@link SDimension.INHERIT}
     * @param isAbsolute if true the param notaes a pixel width, otherwise
     *                   a relative percentage width.
     */
    public void setWidth(int width, boolean isAbsolute) {
        this.width = width;
        this.widthIsAbsolute = isAbsolute;
    }

    /**
     * The width dimension.
     *
     * @return i.e. "100%" or "230px" or "auto" or "inherit"
     */
    public String getWidth() {
        return toString(width, widthIsAbsolute);
    }

    /**
     * Is a widht defined or just AUTO value.
     *
     * @return true if a width other than {@link SDimension.AUTO} is defined
     */
    public boolean isWidthDefined() {
        return width != AUTO_VALUE;
    }

    /**
     * Get just the width as number without trailing unit.
     */
    public int getWidthInt() {
        return width;
    }

    /**
     * Is width int value a absolute (px) value or relative (%).
     *
     * @return true if {@link #getWidthInt()} is a px width, false if it is a percentage width
     */
    public boolean isWidthIsAbsolute() {
        return width >= 0 ?  widthIsAbsolute : false;
    }


    /**
     * Set height in pixel.
     * This appends "px" to the integer value.
     *
     * @param height Takes also {@link SDimension.AUTO} and {@link SDimension.INHERIT}
     */
    public void setHeight(int height) {
        setHeight(height, true);
    }

    /**
     * Set raw string value to be used inside the generated HTML code. S
     *
     * @param heightString Desired heightString. Should be some form of <code>123px</code> or <code>123%</code>
     */
    public void setHeight(String heightString) {
        this.height = getIntValue(heightString);
        this.heightIsAbsolute = isAbsoluteSize(heightString);
    }

    /**
     * Set height either as abolute value (pixel-value) or relative (percentage).
     *
     * @param height     Takes also {@link SDimension.AUTO} and {@link SDimension.INHERIT}
     * @param isAbsolute if true the param notaes a pixel width, otherwise
     *                   a relative percentage width.
     */
    public void setHeight(int height, boolean isAbsolute) {
        this.height = height;
        this.heightIsAbsolute = isAbsolute;
    }

    /**
     * The heiht dimension.
     *
     * @return i.e. "100%" or "230px" or "auto" or "inherit"
     */
    public String getHeight() {
        return toString(height, heightIsAbsolute);
    }

    /**
     * Is a height defined or just AUTO value.
     *
     * @return true if a height other than {@link SDimension.AUTO} is defined
     */
    public boolean isHeightDefined() {
        return height != AUTO_VALUE;
    }


    /**
     * Get just the height as number without trailing unit.
     */
    public int getHeightInt() {
        return height;
    }

    /**
     * Is geight int value a absolute (px) value or relative (%).
     *
     * @return true if {@link #getHeightInt()} is a px width, false if it is a percentage width
     */
    public boolean isHeightIsAbsolute() {
        return height >= 0 ? heightIsAbsolute : false;
    }


    /**
     * Extract number from string.
     *
     * @return extracted integer. f.e.: "120px" becomes 120
     */
    protected int getIntValue(String sizeString) {
        if (sizeString == null)
            return AUTO_VALUE;
        if (sizeString.trim().equalsIgnoreCase(AUTO))
            return AUTO_VALUE;
        if (sizeString.trim().equalsIgnoreCase(INHERIT))
            return INHERIT_VALUE;
        try {
            return new DecimalFormat().parse(sizeString, new ParsePosition(0)).intValue();
        } catch (Exception e) {
            log.warn("Can not parse [" + sizeString + "]", e);
        }
        return AUTO_VALUE;
    }

    protected String toString(int size, boolean isAbsolute) {
        if (size == AUTO_VALUE)
            return AUTO;
        else if (size == INHERIT_VALUE)
            return INHERIT;
        else if (isAbsolute)
            return Integer.toString(size) + "px";
        else
            return Integer.toString(size) + "%";
    }


    /**
     * Extract intented dimension type of contained in the string
     *
     * @return false, if String ends with "%", true otherwise
     */
    protected boolean isAbsoluteSize(String dimensionString) {
        if (dimensionString != null)
            return !dimensionString.trim().endsWith("%");
        else
            return true;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SDimension)) return false;

        final SDimension sDimension = (SDimension) o;

        if (this.getWidthInt() != sDimension.getWidthInt() || this.isWidthIsAbsolute() != sDimension.isWidthIsAbsolute())
            return false;
        if (this.getHeightInt() != sDimension.getHeightInt() || this.isHeightIsAbsolute() != sDimension.isHeightIsAbsolute())
            return false;
        return true;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return width;
    }

    /**
     * Set the size of this Dimension object to the specified width and height as absolute values.
     *
     * @see #setHeight(int,boolean)
     * @see #setWidth(int,boolean)
     */
    public void setSize(int width, int height) {
        setWidth(width, true);
        setHeight(height, true);
    }

    public String toString() {
        return "width: " + getWidth() + "; height: " + getHeight() + ";";
    }
}
