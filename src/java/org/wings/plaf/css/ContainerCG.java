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
package org.wings.plaf.css;


import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.style.CSSSelector;
import org.wings.io.Device;

public final class ContainerCG extends AbstractComponentCG implements
        org.wings.plaf.PanelCG {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public void write(final Device device, final SComponent component)
            throws java.io.IOException {
        Utils.renderContainer(device, (SContainer) component);
    }

    public CSSSelector mapSelector(SComponent addressedComponent, CSSSelector selector) {
        if (SContainer.SELECTOR_CONTENT.equals(selector)) {
            return addressedComponent.getComponentCssSelector();
        } else {
            return super.mapSelector(addressedComponent, selector);
        }
    }
}
