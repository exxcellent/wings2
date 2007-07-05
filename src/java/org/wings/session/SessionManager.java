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
package org.wings.session;


/**
 * A global way to access the current session.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 */
public final class SessionManager {
    private static final ThreadLocal currentSession = new ThreadLocal();

    /**
     * Get the Session that is currently associated with this Thread.
     *
     * @return the Session
     */
    public static Session getSession() {
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

    public static void removeSession() {
        currentSession.set(null);
    }
}


