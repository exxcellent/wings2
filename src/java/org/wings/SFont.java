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

import java.io.Serializable;

/**
 * Defines and represents a customized font (font name, style and size).
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class SFont implements Serializable {
    /**
     * Plain font style for {@link SFont#setStyle(int)}. Can be combined with adding (i.e. SFont.BOLD+SFont.ITALIC)
     */
    public final static int PLAIN = java.awt.Font.PLAIN;
    /**
     * Italic font style for {@link SFont#setStyle(int)}. Can be combined with adding (i.e. SFont.BOLD+SFont.ITALIC)
     */
    public final static int ITALIC = java.awt.Font.ITALIC;
    /**
     * Bold font style for {@link SFont#setStyle(int)}. Can be combined with adding (i.e. SFont.BOLD+SFont.ITALIC)
     */
    public final static int BOLD = java.awt.Font.BOLD;

    /**
     * Default font size for {@link SFont} constructor.
     */
    public final static int DEFAULT_SIZE = -1;

    protected int style = PLAIN;
    protected String face = null;
    protected int size = DEFAULT_SIZE;

    public SFont() {
    }

    public SFont(int style) {
        setStyle(style);
    }

    /*
     * @parameter size if Integer.MIN_VALUE the size is ignored
     */
    public SFont(String face, int style, int size) {
        setFace(face);
        setStyle(style);
        setSize(size);
    }
    
    public void setFace(String f) {
        face = f;
        if (face != null && face.trim().length() == 0)
            face = null;
    }

    public String getFace() {
        return face;
    }

    public void setStyle(int s) {
        style = s;
    }

    public int getStyle() {
        return style;
    }

    public void setSize(int s) {
        size = s;
    }

    public int getSize() {
        return size;
    }
}


