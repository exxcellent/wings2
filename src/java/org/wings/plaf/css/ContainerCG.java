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
package org.wings.plaf.css;


import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SContainer;
import org.wings.io.Device;

public class ContainerCG
        extends AbstractComponentCG
        implements SConstants, org.wings.plaf.PanelCG {
    public void writeContent(final Device device, final SComponent component)
            throws java.io.IOException {
        Utils.renderContainer(device, (SContainer) component);
    }
}
