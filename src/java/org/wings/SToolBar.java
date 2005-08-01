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

import org.wings.plaf.ToolBarCG;

/**
 * The basic SToolBar is just an SPanel without an SLayoutManager.
 * However, the Plaf might choose to set a different layout manager.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SToolBar extends SPanel {
    /**
     * create a new SToolBar.
     */
    public SToolBar() {
        super(new SFlowLayout());
    }

    public void setCG(ToolBarCG cg) {
        super.setCG(cg);
    }
}


