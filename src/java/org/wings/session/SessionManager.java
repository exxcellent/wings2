/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.session;

import java.util.*;

/**
 * A global way to access the current session. FIXME: should this be
 * called SessionManager, then ?
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SessionManager
{
    private static final ThreadLocal currentSession = new ThreadLocal();

    /**
     * Get the Session that is currently associated with this Thread.
     *
     * @return the Session
     */
    public static Session getSession() 
    {
        return (Session) currentSession.get();
    }

    /**
     * Associate the Session with the current Thread.
     * This method must only be called by the SessionServlet before
     * a request is going to be dispatched.
     *
     * @param session the Session
     */
    public static void setSession(Session session) {
        currentSession.set(session);
    }

    /**
     * TODO: documentation
     */
    public static void removeSession() {
        currentSession.set(null);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
