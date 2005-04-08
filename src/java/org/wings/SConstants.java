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
 * This interface contains several constants use at several places in wingS.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public interface SConstants {
    /**
     * Character to separate epoch id from component id in lowleve event requests.
     */
    String UID_DIVIDER = "_";

    /**
     * Component alignment constant: Do not explicitly align.
     */
    int NO_ALIGN = -1;
    /**
     * Component alignment constant: Align left.
     */
    int LEFT_ALIGN = 0;
    /**
     * Component alignment constant: Align left.
     */
    int LEFT = LEFT_ALIGN;
    /**
     * Component alignment constant: Align right.
     */
    int RIGHT_ALIGN = 1;
    /**
     * Component alignment constant: Align right.
     */
    int RIGHT = RIGHT_ALIGN;
    /**
     * Component alignment constant: Center align.
     */
    int CENTER_ALIGN = 2;
    /**
     * Component alignment constant: Center align.
     */
    int CENTER = CENTER_ALIGN;
    /**
     * Component alignment constant: Block align -- stretch over full width.
     */
    int BLOCK_ALIGN = 3;
    /**
     * Component alignment constant: Vertically align at top.
     */
    int TOP_ALIGN = 4;
    /**
     * Component alignment constant: Vertically align at top.
     */
    int TOP = TOP_ALIGN;
    /**
     * Component alignment constant: Vertically align at bottom.
     */
    int BOTTOM_ALIGN = 5;
    /**
     * Component alignment constant: Vertically align at bottom.
     */
    int BOTTOM = BOTTOM_ALIGN;
    /**
     * Component alignment constant: Block align -- stretch over full width.
     */
    int JUSTIFY = BLOCK_ALIGN;
    /**
     * Align at font baseline. (Images).
     */
    int BASELINE = 6;

    int VERTICAL = 1;
    int HORIZONTAL = 0;

}


