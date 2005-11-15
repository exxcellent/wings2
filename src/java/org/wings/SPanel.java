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

import org.wings.plaf.PanelCG;

/**
 * An SPanel is a container that display and arranges the contained using the assigned {@link SLayoutManager}.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SPanel
        extends SContainer {

    /**
     * Creates a panel with the default layout manager.
     */
    public SPanel() {
    }

    /**
     * Creates a panel with the specified layout manager
     * @param l the layout manager for the panel
     */
    public SPanel(SLayoutManager l) {
        super(l);
    }

    public void setCG(PanelCG cg) {
        super.setCG(cg);
    }
}


