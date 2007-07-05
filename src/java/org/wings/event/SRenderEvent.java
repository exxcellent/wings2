/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.event;

import org.wings.SComponent;

import java.util.EventObject;


/**
 * Event fired on render events in {@link SRenderListener}.
 * This event contains the event source (the component actually rendered).
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 */
public class SRenderEvent extends EventObject {

    /**
     * Constructs a new render event by noting down the source component.
     * @param source
     */
    public SRenderEvent(SComponent source) {
        super(source);
    }

    /**
     * @return Convenience getter for {@link #getSource()} 
     */
    public SComponent getSourceComponent() {
        return (SComponent) super.getSource();
    }

}

