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

/**
 * SAbstractIcon.java
 * <p/>
 * <p/>
 * Created: Tue Nov 19 09:17:25 2002
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
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
     * 
     */
    protected SAbstractIcon() {
    }

    /**
     * 
     */
    protected SAbstractIcon(int width, int height) {
        setIconWidth(width);
        setIconHeight(height);
    }

    public int getIconWidth() {
        return width;
    }

    public int getIconHeight() {
        return height;
    }

    public void setIconWidth(int w) {
        width = w;
    }

    public void setIconHeight(int h) {
        height = h;
    }


}// SAbstractIcon

/*
   $Log$
   Revision 1.2  2004/11/24 18:13:20  blueshift
   TOTAL CLEANUP:
   - removed document me TODOs
   - updated/added java file headers
   - removed emacs stuff
   - removed deprecated methods

   Revision 1.1.1.1  2004/10/04 16:13:06  hengels
   o start development of wings 2

   Revision 1.1  2002/11/19 14:57:47  ahaaf
   o make icon dimensions modifiable (they are used now as render dimension)

*/
