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
package org.wings.tree;

import org.wings.SComponent;
import org.wings.STree;

/**
 * Cell Renderer for {@link STree}. This is similar (almost the same) to the renderer
 * approach in swing.
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public interface STreeCellRenderer {
    SComponent getTreeCellRendererComponent(STree tree,
                                            Object value,
                                            boolean selected,
                                            boolean expanded,
                                            boolean leaf,
                                            int row,
                                            boolean hasFocus);
}


