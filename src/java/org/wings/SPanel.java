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
 * An SPanel is basically a container that can be displayed with
 * its own CG. The components in the container are placed with a
 * Layout Manager.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SPanel
        extends SContainer {
    public SPanel() {
    }

    public SPanel(SLayoutManager l) {
        super(l);
    }

    public void setCG(PanelCG cg) {
        super.setCG(cg);
    }
}


