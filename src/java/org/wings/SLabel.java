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

//import javax.swing.*;

import org.wings.plaf.LabelCG;
import org.wings.plaf.LabelCG;
import org.wings.plaf.LabelCG;

/**
 * A display area for a short text string or an image, or both.
 * You can specify where in the label's display area  the label's contents
 * are aligned by setting the vertical and horizontal alignment.
 * You can also specify the position of the text relative to the image.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SLabel
        extends SComponent
        implements SConstants {
    /**
     * The text to be displayed
     */
    protected String text;

    /**
     * The icon to be displayed
     */
    protected SIcon icon = null;

    protected SIcon disabledIcon = null;

    private int verticalTextPosition = CENTER;
    private int horizontalTextPosition = RIGHT;
    private int iconTextGap = 1;
    private boolean imageAbsBottom = false;

    /**
     * Creates a new <code>SLabel</code> instance with the specified text
     * (left alligned) and no icon.
     *
     * @param text The text to be displayed by the label.
     */
    public SLabel(String text) {
        this(text, null, LEFT);
    }

    /**
     * Creates a new <code>SLabel</code> instance with no text and no icon.
     */
    public SLabel() {
        this((String) null);
    }

    /**
     * Creates a new <code>SLabel</code> instance with the specified icon
     * (left alligned) and no text.
     *
     * @param icon The image to be displayed by the label.
     */
    public SLabel(SIcon icon) {
        this(icon, LEFT);
    }

    /**
     * Creates a new <code>SLabel</code> instance with the specified icon
     * (alligned as specified) and no text.
     *
     * @param icon                The image to be displayed by the label.
     * @param horizontalAlignment One of the following constants defined in
     *                            <code>SConstants</code>:
     *                            <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>.
     * @see SConstants
     */
    public SLabel(SIcon icon, int horizontalAlignment) {
        this(null, icon, horizontalAlignment);
    }

    /**
     * Creates a new <code>SLabel</code> instance with the specified icon
     * and the specified text (left alligned).
     *
     * @param text The text to be displayed by the label.
     * @param icon The image to be displayed by the label.
     */
    public SLabel(String text, SIcon icon) {
        setText(text);
        setIcon(icon);
        setHorizontalAlignment(LEFT);
    }

    /**
     * Creates a new <code>SLabel</code> instance with the specified icon
     * and the specified text (alligned as specified).
     *
     * @param text                The text to be displayed by the label.
     * @param icon                The image to be displayed by the label.
     * @param horizontalAlignment One of the following constants defined in
     *                            <code>SConstants</code>:
     *                            <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>.
     * @see SConstants
     */
    public SLabel(String text, SIcon icon, int horizontalAlignment) {
        setText(text);
        setIcon(icon);
        setHorizontalAlignment(horizontalAlignment);
    }

    /**
     * Creates a new <code>SLabel</code> instance with the specified text
     * (alligned as specified) and no icon.
     *
     * @param text                The text to be displayed by the label.
     * @param horizontalAlignment One of the following constants defined in
     *                            <code>SConstants</code>:
     *                            <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>.
     * @see SConstants
     */
    public SLabel(String text, int horizontalAlignment) {
        this(text, null, horizontalAlignment);
    }


    public void setImageAbsBottom(boolean t) {
        imageAbsBottom = t;
    }


    public boolean isImageAbsBottom() {
        return imageAbsBottom;
    }

    /**
     * Returns the horizontal position of the lable's text
     *
     * @return the position
     * @see SConstants
     * @see #setHorizontalTextPosition
     */
    public int getHorizontalTextPosition() {
        return horizontalTextPosition;
    }

    /**
     * Sets the horizontal position of the lable's text, relative to its icon.
     * <p/>
     * The default value of this property is CENTER.
     *
     * @param textPosition One of the following constants defined in
     *                     <code>SConstants</code>:
     *                     <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>.
     */
    public void setHorizontalTextPosition(int textPosition) {
        horizontalTextPosition = textPosition;
    }

    /**
     * Sets the vertical position of the lable's text, relative to its icon.
     * <p/>
     * The default value of this property is CENTER.
     *
     * @param textPosition One of the following constants defined in
     *                     <code>SConstants</code>:
     *                     <code>TOP</code>, <code>CENTER</code>, <code>BOTTOM</code>.
     */
    public void setVerticalTextPosition(int textPosition) {
        verticalTextPosition = textPosition;
    }

    /**
     * Returns the vertical position of the label's text
     *
     * @return the position
     * @see SConstants
     * @see #setVerticalTextPosition
     */
    public int getVerticalTextPosition() {
        return verticalTextPosition;
    }

    public void setIconTextGap(int gap) {
        iconTextGap = gap;
    }

    public int getIconTextGap() {
        return iconTextGap;
    }

    public void setIcon(SIcon i) {
        reloadIfChange(icon, i);
        icon = i;
    }

    public SIcon getIcon() {
        return icon;
    }

    public void setDisabledIcon(SIcon i) {
        reloadIfChange(disabledIcon, i);
        disabledIcon = i;
    }

    public SIcon getDisabledIcon() {
        return disabledIcon;
    }

    /**
     * Returns the text of the label
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of the label. If the value of text is null or an empty
     * string, nothing is displayed.
     *
     * @param t The new text
     */
    public void setText(String t) {
        reloadIfChange(text, t);
        text = t;
    }

    public void setCG(LabelCG cg) {
        super.setCG(cg);
    }
}


