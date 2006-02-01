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

import org.wings.plaf.LabelCG;

/**
 * Display area for a short text string or an image, or both.
 * <p/>
 * You can specify where in the label's display area  the label's contents
 * are aligned by setting the vertical and horizontal alignment.
 * You can also specify the position of the text relative to the image.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SLabel extends SComponent {
    
    protected String text;
    protected SIcon icon = null;
    protected SIcon disabledIcon = null;
    protected int verticalTextPosition = SConstants.CENTER;
    protected int horizontalTextPosition = SConstants.RIGHT;
    protected boolean wordWrap;

    /**
     * Creates a new <code>SLabel</code> instance with the specified text
     * (left alligned) and no icon.
     *
     * @param text The text to be displayed by the label.
     */
    public SLabel(String text) {
        this(text, null, SConstants.LEFT);
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
        this(icon, SConstants.LEFT);
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
        this(text, icon, SConstants.LEFT);
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

    /**
     * Defines the icon the component will display.
     * @param i
     */
    public void setIcon(SIcon i) {
        reloadIfChange(icon, i, ReloadManager.STATE);
        icon = i;
    }

    /**
     * Returns the icon the label displays.
     * @return the icon
     */
    public SIcon getIcon() {
        return icon;
    }

    /**
     * Set the icon that will be displayed if the label is disabled.
     * @param i
     */
    public void setDisabledIcon(SIcon i) {
        reloadIfChange(disabledIcon, i, ReloadManager.STATE);
        disabledIcon = i;
    }

    /**
     * Returns the icon that is displayed when the label is disabled.
     * @return the diabledIcon
     */
    public SIcon getDisabledIcon() {
        return disabledIcon;
    }

    /**
     * Returns the text of the label
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of the label. Nothing will be displayed if the text is an empty string or null.
     *
     * @param t The new text
     */
    public void setText(String t) {
        reloadIfChange(text, t, ReloadManager.STATE);
        text = t;
    }

    /**
     * Determiens if the label text word wrap inside the browser. Defaults to <code>false</code> (Swing).
     * @return <code>false</code> if the label should not word wrap an be in line as in Swing.
     */
    public boolean isWordWrap() {
        return wordWrap;
    }

    /**
     * Defines if the label is allowed to wrap.
     * @param wordWrap Set to <code>true</code> if you want labels to allow to break into more lines than passed.
     */
    public void setWordWrap(boolean wordWrap) {
        this.wordWrap = wordWrap;
    }

    public void setCG(LabelCG cg) {
        super.setCG(cg);
    }
}


