/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings;

import java.io.Serializable;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SFont
        implements SConstants, Serializable {
    private static final boolean DEBUG = true;

    protected int type = FONT;

    protected int style = PLAIN;

    protected String face = null;

    protected int size = Integer.MIN_VALUE;

    public SFont() {}

    /*
     * @parameter size if Integer.MIN_VALUE the size is ignored
     */
    public SFont(String face, int style, int size) {
        setFace(face);
        setStyle(style);
        setSize(size);
    }

    public void setType(int t) {
        type = t;
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


