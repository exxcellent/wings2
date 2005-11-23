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

/**
 * Invisible component which may be used as a spacer in dynamic layout managers. 
 * Typically it is rendered as an invisible pixel.
 * 
 * @author bschmid
 */
public class SSpacer extends SComponent {

    /** C'tor of an invisible element. */
    public SSpacer(int width, int height) {
        setPreferredSize(new SDimension(Integer.toString(width), Integer.toString(height)));
    }

    /** C'tor of an invisible element. */
    public SSpacer(String width, String height) {
        setPreferredSize(new SDimension(width, height));
    }

}
