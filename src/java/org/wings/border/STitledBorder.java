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

import java.awt.Insets;
import org.wings.*;
import java.awt.Color;
import java.util.Map;
import org.wings.SConstants;
import org.wings.style.CSSAttributeSet;
import org.wings.style.Selector;

/**
 * Adds a border with a title to a component.
 *
 */
public class STitledBorder extends SAbstractBorder {

    /**
     * Use the default vertical orientation for the title text.
     */
    static public final int     DEFAULT_POSITION        = 0;
    /** Position the title above the border's top line. */
    static public final int     ABOVE_TOP       = 1;
    /** Position the title in the middle of the border's top line. */
    static public final int     TOP             = 2;
    /** Position the title below the border's top line. */
    static public final int     BELOW_TOP       = 3;
    /** Position the title above the border's bottom line. */
    static public final int     ABOVE_BOTTOM    = 4;
    /** Position the title in the middle of the border's bottom line. */
    static public final int     BOTTOM          = 5;
    /** Position the title below the border's bottom line. */
    static public final int     BELOW_BOTTOM    = 6;

        /**
     * Use the default justification for the title text.
     */
    static public final int     DEFAULT_JUSTIFICATION   = 0;
    /** Position title text at the left side of the border line. */
    static public final int     LEFT    = 1;
    /** Position title text in the center of the border line. */
    static public final int     CENTER  = 2;
    /** Position title text at the right side of the border line. */
    static public final int     RIGHT   = 3;
    /** Position title text at the left side of the border line
     *  for left to right orientation, at the right side of the 
     *  border line for right to left orientation.
     */
    static public final int     LEADING = 4;
    /** Position title text at the right side of the border line
     *  for left to right orientation, at the left side of the 
     *  border line for right to left orientation.
     */
    static public final int     TRAILING = 5;

    // Space between the border and the component's edge
    static protected final int EDGE_SPACING = 2;

    // Space between the border and text
    static protected final int TEXT_SPACING = 2;

    // Horizontal inset of text that is left or right justified
    static protected final int TEXT_INSET_H = 5;


    /**
     * Map of {@link Selector} to CSS {@link Style}s currently assigned to this component.
     */
    protected Map dynamicStyles;

    /**
     * Two different selectoren, one for fieldset and one for legend
     **/
    public static final Selector SELECTOR_FIELDSET = new Selector("Fieldset");
    public static final Selector SELECTOR_LEGEND = new Selector("Legend");

//private SBorder border;
    private String title;
    
    private SFont font = null;
    
    private Color color = null;
    
    private int titleJustification = DEFAULT_JUSTIFICATION;
    
    private SBorder border = null;
    
     /**
     * Constructor for STitledBorder.
     * 
     */
    public STitledBorder(SBorder border) {
        this(border, "");
    }


    /**
     * Constructor for STitledBorder. Default border
     * type is {@link SEtchedBorder}, thickness 2
     */
    public STitledBorder(String title) {
        this((SBorder) null, title);
    }

    /**
     * Constructor for STitledBorder.
     *
     * @param border the border to use
     * @param title  the title to display
     */
    public STitledBorder(SBorder border, String title) {
        this(border, title, DEFAULT_JUSTIFICATION, DEFAULT_POSITION);
    }
    
    /**
     * Constructor for STitledBorder
     * Creates a STitledBorder instance with the specified border, title, title-justification, and title-position.
     *
     * @param border the border to use
     * @param title  the title to display
     * @param titleJustification (not used)
     * @param titlePosition (not used)
     */
    
    public STitledBorder(SBorder border, String title, int titleJustification, int titlePosition) {
        this(border, title, titleJustification, titlePosition, null);
    }
    
    /**
     * Constructor for STitledBorder
     * Creates a STitledBorder instance with the specified border, title, title-justification, title-position, and title-font.
     *
     * @param border the border to use
     * @param title  the title to display
     * @param titleJustification
     * @param titlePosition (not used)
     * @param titleFont the font to used
     */

    public STitledBorder(SBorder border, String title, int titleJustification, int titlePosition, SFont titleFont) {
        this(border, title, titleJustification, titlePosition, titleFont, null);
    }

    /**
     * Constructor for STitledBorder
     * Creates a TitledBorder instance with the specified border, title, title-justification, title-position, title-font, and title-color.
     *
     * @param border the border to use
     * @param title  the title to display
     * @param titleJustification
     * @param titlePosition (not used)
     * @param titleFont the font to use
     * @param titleColor the color to use
     */

    public STitledBorder(SBorder border, String title, int titleJustification, int titlePosition, SFont titleFont, Color titleColor) {
        setBorder(border);
        setTitle(title);
        setTitleFont(titleFont);   
        setTitleColor(titleColor);
        setTitleJustification(titleJustification);
        
    }

    /**
     * Gets the border.
     *
     * @return Returns a SBorder
     * 
     */
    public SBorder getBorder() {
        return border;
    }

    /**
     * Sets the border.
     *
     * @param border The border to set
     * 
     */
    public void setBorder(SBorder border) {
       if ((component != null) &&  (isDifferent(this.border,border))) component.reload();
       this.border = border;
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
        if ((component != null) && (isDifferent(this.title,title))) component.reload();
        this.title = title;
    }
    
    /**
     * Sets the title-font of the titled border.
     *
     * @param titleFont - the font for the border title
     **/
    public void setTitleFont(SFont titleFont) {
        if ((component != null) && (isDifferent(this.font,titleFont))) component.reload();
        this.font = titleFont;
    }
    
    /**
     * Sets the title-color of the titled border.
     *
     * @param titleColor - the color for the border title
     **/
    public void setTitleColor(Color titleColor) {
        if ((component != null) &&  (isDifferent(this.color,titleColor))) component.reload();
        this.color = titleColor;
    }


    /**
     * Returns the title-font of the titled border.
     *
     **/
    public SFont getTitleFont() {
        return font;
    }

  
    /**
     * Returns the title-color of the titled border.
     *
     **/
    public Color getTitleColor() {
        return color;
    }

    /**
     * Returns the title-justification of the titled border.
     **/
    public int getTitleJustification() {
        return titleJustification;
    }


    /**
     * Sets the title-justification of the titled border.
     * @param titleJustification, Default, Center, Right, Left
     **/

    public void setTitleJustification(int titleJustification) {
       if ((component != null) && (this.titleJustification != titleJustification)) component.reload();
       this.titleJustification = titleJustification;
    }

    
    /**
     * use this method for changing a variable. if a new value is different
     * from the old value set the new one and notify e.g. the reloadmanager...
     */
    protected static boolean isDifferent(Object oldObject,
                                         Object newObject) {
        if (oldObject == newObject)
            return false;

        if (oldObject == null)
            return true;

        return !oldObject.equals(newObject);
    }

}
