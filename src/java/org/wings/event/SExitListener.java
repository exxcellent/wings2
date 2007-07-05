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

import java.util.EventListener;


/**
 * Exit listeners can be attached to the current session and will get notified when the session is closed.
 * They may throw an {@link ExitVetoException} to indicate that they do not want to allow the session
 * destroying. This veto exception has only effect if the session end was triggered by
 * {@link org.wings.session.Session#exit(String)}.
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 */

public interface SExitListener extends EventListener {
    /**
     * The current user session is about to be closed.
     * @throws ExitVetoException Thrown if client wants to abort an existing
     *  {@link org.wings.session.Session#exit(String)} session exit trigger.
     */
    void prepareExit(SExitEvent e) throws ExitVetoException;

}
