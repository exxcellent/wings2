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

import java.lang.ref.WeakReference;


/**
 * A SRequestListener implementation, which uses WeakReference. This is
 * important for volatile objects, which needs to be registered as request
 * listener at the wings session, but which are not removed anymore from the
 * session. To make them
 * garbage collectable, they should use this proxy for registering as a request
 * listener.
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 */
public final class WeakRequestListenerProxy extends WeakReference implements SRequestListener {


    public WeakRequestListenerProxy(SRequestListener requestListener) {
        super(requestListener);
    }

    /**
     * Invokes the processRequest method of the proxied requestListener. If the
     * listener is garbage collected, it removes itself from the session as
     * request listener.
     *
     * @param e a <code>SRequestEvent</code> value
     */
    public void processRequest(SRequestEvent e) {
        SRequestListener requestListener = (SRequestListener) get();

        if (requestListener == null) {
            org.wings.session.SessionManager.getSession().removeRequestListener(this);
        } else {
            requestListener.processRequest(e);
        } // end of if ()
    }

    public int hashCode() {
        Object requestListener = get();

        if (requestListener == null) {
            return 0;
        } else {
            return requestListener.hashCode();
        } // end of if ()
    }

    public boolean equals(WeakRequestListenerProxy p) {
        Object requestListener = get();

        if (requestListener == null) {
            return p.get() == null;
        } else {
            return requestListener.equals(p.get());
        } // end of if ()
    }

    public boolean equals(Object o) {
        if (o instanceof WeakRequestListenerProxy) {
            return equals((WeakRequestListenerProxy) o);
        } else {
            return false;
        } // end of if ()
    }

}

