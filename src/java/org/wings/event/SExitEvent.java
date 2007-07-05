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

import org.wings.session.Session;

import java.util.EventObject;

/**
 * Event object fired to  {@link SExitListener}.
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 */
public class SExitEvent extends EventObject {

    /**
     * Constructs a exit event.
     * @param source Session is the source.
     */
    public SExitEvent(Session source) {
        super(source);
    }

    public Session getSourceSession() {
        return (Session) getSource();
    }

}

