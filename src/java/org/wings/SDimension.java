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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParsePosition;

/**
 * Holds preferred component sizes (dimensions).
 * <p/>
 * Web browsers support different notations for sizes. Absolute pixel values,
 * realtive percentages (of the available viewport) and
 * special CSS values as 'inherit' and 'auto' (Default).
 * This class if capable of handling all these cases.
 */
public class SDimension implements Serializable {

    /**
     * String constant for CSS dimension 'auto'.
     * This is the default width used by wings as well as by CSS capable browsers.
     */
    public final static String AUTO = "auto";

    /**
     * String for CSS dimension 'inherit'.
     * With this value the preferred size is inherited by the surrounding element.
     */
    public final static String INHERIT = "inherit";

    /**
     * Integer constant for CSS dimension 'auto'.
     * This is the default width used by wings as well as by CSS capable browsers.
     */
    public final static int AUTO_INT = -1;

    /**
     * Integer constant for CSS dimension 'inherit'.
     * With this value the preferred size is inherited by the surrounding element.
     */
    public final static int INHERIT_INT = -2;

    /**
     * Immutable SDimension constants for a component taking up the full available width.
     */
    public static final SDimension FULLWIDTH = new ImmutableSDimension("100%", null);
    /**
     * Immutable SDimension constants for a component taking up the full available height.
     */
    public static final SDimension FULLHEIGHT = new ImmutableSDimension(null, "100%");
    /**
     * Immutable SDimension constants for a component taking up the full available area.
     */
    public static final SDimension FULLAREA = new ImmutableSDimension("100%", "100%");
    /**
     * Immutable SDimension constants for a component taking up normal space
     */
    public static final SDimension AUTOAREA = new ImmutableSDimension(AUTO, AUTO);

    private final transient static Log log = LogFactory.getLog(SDimension.class);

    private int widthInt = AUTO_INT;
    private int heightInt = AUTO_INT;
    private String widthUnit;
    private String heightUnit;

    public SDimension() {
    }

    public SDimension(String width, String height) {
        setWidth(width);
        setHeight(height);
    }

    /**
     * Construct a new dimension with absolute values.
     * The value is interpreted as absolute pixel values.
     *
     * @param width  the preferred width in pixel. Use -1 for AUTO.
     * @param height the preferred height in pixel. Use -1 for AUTO.
     * @see #setSize(int,int)
     */
    public SDimension(int width, int height) {
        setSize(width, height);
    }

    /**
     * Construct a new dimension with absolute values.
     * The value is interpreted as absolute pixel values.
     *
     * @param width  the preferred width in pixel. You may pass <code>null</code>.
     * @param height the preferred height in pixel. You may pass <code>null</code>.
     * @see #setSize(int,int)
     */
    public SDimension(Integer width, Integer height) {
        setSize(width != null ? width.intValue() : AUTO_INT, height != null ? height.intValue() : AUTO_INT);
    }

    /**
     * Sets the preferred width via an string. Expects an dimension/unit compount i.e.
     * "120px" or "80%" but will assume px by default.
     * @param width A preferred witdth.
     */
    public void setWidth(String width) {
        this.widthInt = extractNumericValue(width);
        this.widthUnit = extractUnit(width);
    }

    /**
     * Sets the preferred height via an string. Expects an dimension/unit compount i.e.
     * "120px" or "80%" but will assume px by default.
     * @param height A preferred height.
     */
    public void setHeight(String height) {
        this.heightInt = extractNumericValue(height);
        this.heightUnit = extractUnit(height);
    }

    /**
     * Set width in pixel.
     * This appends "px" to the integer value.
     *
     * @param width if -1 set {@link SDimension#widthInt} to <code>null</code>
     */
    public void setWidth(int width) {
        widthInt = width;
        widthUnit = width < 0 ? null : "px";
    }

    /**
     * Set height in pixel.
     * This appends "px" to the integer value.
     *
     * @param height if -1 set {@link SDimension#heightInt} to <code>null</code>
     */
    public void setHeight(int height) {
        heightInt = height;
        heightUnit = height < 0 ? null : "px";
    }

    /**
     * @return The preferred width i.e. like <code>120px</code> or <code>80%</code> or <code>auto</code>
     */
    public String getWidth() {
        return toString(widthInt, widthUnit);
    }

    /**
     * @return The preferred height i.e. like <code>120px</code> or <code>80%</code> or <code>auto</code>
     */
    public String getHeight() {
        return toString(heightInt, heightUnit);
    }

    /**
     * Get just the width as number without trailing
     * unit.
     */
    public int getWidthInt() {
        return widthInt;
    }

    /**
     * Get just the height as number without trailing
     * unit.
     */
    public int getHeightInt() {
        return heightInt;
    }

    /**
     * @return The unit of the current width. Returns <code>null</code> for {@link #AUTO} and {@link #INHERIT}.
     * Otherwise always returns a non-null value!
     */
    public String getWidthUnit() {
        return widthInt < 0 ? null : (((widthUnit != null && widthUnit.length() > 0) ? widthUnit : "px")) ;
    }

    /**
     * @return The unit of the current height. Returns <code>null</code> for {@link #AUTO} and {@link #INHERIT}.
     * Otherwise always returns a non-null value!
     */
    public String getHeightUnit() {
        return heightInt < 0 ? null : (((heightUnit != null && heightUnit.length() > 0) ? heightUnit : "px"));
    }

    /**
     * Set the size of this Dimension object to the specified width and height
     * and append "px" to both values.
     *
     * @see #setHeight(int)
     * @see #setWidth(int)
     */
    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public String toString() {
        return "width: " + getWidth() + "; height: " + getHeight();
    }

    /**
     * Extract number from string.
     *
     * @return extracted integer. f.e.: "120px" becomes 120
     */
    protected int getIntValue(String sizeString) {
        if (sizeString == null) {
            return AUTO_INT;
        }
        if (sizeString.trim().equalsIgnoreCase(AUTO)) {
            return AUTO_INT;
        }
        if (sizeString.trim().equalsIgnoreCase(INHERIT)) {
            return INHERIT_INT;
        }
        try {
            return new DecimalFormat().parse(sizeString, new ParsePosition(0)).intValue();
        } catch (Exception e) {
            log.warn("Can not parse [" + sizeString + "]", e);
        }
        return AUTO_INT;
    }

    protected String toString(int size, String unit) {
        if (size == INHERIT_INT) {
            return INHERIT;
        } else if (size < 0) {
            return AUTO;
        } else if (unit != null && unit.length() > 0) {
            return Integer.toString(size) + unit;
        } else {
            // default: assume px
            return Integer.toString(size) + "px";
        }
    }

    /**
     * Extract number from string.
     *
     * @return extracted integer. f.e.: "120px" becomes 120 or {@link #AUTO_INT} if <code>null</code>
     */
    protected int extractNumericValue(String value) {
        if (value == null || value.equalsIgnoreCase(AUTO)) {
            return AUTO_INT;
        } else if (value.equalsIgnoreCase(INHERIT)) {
            return INHERIT_INT;
        } else {
            try {
                return new DecimalFormat().parse(value, new ParsePosition(0)).intValue();
            } catch (Exception e) {
                return AUTO_INT;
            }
        }
    }

    /**
     * Tries to extract unit from passed string. I.e. returtn "px" if you pass "120px".
     *
     * @param value A compound number/unit string. May be  <code>null</code>
     * @return The unit or  <code>null</code> if not possible.
     */
    private String extractUnit(String value) {
        if (value == null) {
            return null;
        } else {
            ParsePosition position = new ParsePosition(0);
            try {
                new DecimalFormat().parse(value, position).intValue();
                return value.substring(position.getIndex()).trim();
            } catch (Exception e) {
                return null;
            }
        }
    }

    private final static class ImmutableSDimension extends SDimension {
        public ImmutableSDimension(String width, String height) {
            super(width, height);
        }
    }
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SDimension)) {
            return false;
        }

        final SDimension sDimension = (SDimension) o;

        if (heightInt != sDimension.heightInt || widthInt != sDimension.widthInt) {
            return false;
        }
        if (heightUnit != null ? !heightUnit.equals(sDimension.heightUnit) : sDimension.heightUnit != null) {
            return false;
        }
        if (widthUnit != null ? !widthUnit.equals(sDimension.widthUnit) : sDimension.widthUnit != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        return widthInt;
    }
    
}
