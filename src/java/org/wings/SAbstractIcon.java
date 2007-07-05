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
 * Base class for icons.
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 */
public abstract class SAbstractIcon implements SIcon {

    /**
     * The width of the icon. This is the width it is rendered, not
     * the real width of the icon. A value <0 means, no width is rendered
     */
    protected int width = -1;

    /**
     * The height of the icon. This is the height it is rendered, not
     * the real width of the icon. A value <0 means, no height is rendered
     */
    protected int height = -1;

    /**
     * Title of icon, <code>""</code> if not set.
     */
    protected String title = null;


    protected SAbstractIcon() {
    }


    protected SAbstractIcon(int width, int height) {
        setIconWidth(width);
        setIconHeight(height);
    }

    /**
     * Returns the width of the icon.
     * @return the width or -1 if unknown
     */
    public int getIconWidth() {
        return width;
    }

    /**
     * Returns the height of the icon. 
     * @return the height or -1 if unknown
     */
    public int getIconHeight() {
        return height;
    }

    /**
     * Sets the width of the icon.
     * @param w the width of the icon
     */
    public void setIconWidth(int w) {
        width = w;
    }

    public void setIconHeight(int h) {
        height = h;
    }

    public String getIconTitle() {
        return (title!=null) ? title : "";
    }
    
    public void setIconTitle(String title) {
        this.title = title;
    }
}// SAbstractIcon

