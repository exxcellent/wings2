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
package org.wings.session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.LowLevelEventListener;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SFrame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Registers session component instants which want to receive low level events.
 * The dispatcher holds a list of all known low level event dispatchers and is responsible
 * to dispatch the according part of an original HTTP request to the
 * {@link LowLevelEventListener#processLowLevelEvent(String, String[])} method of the registered
 * {@link LowLevelEventListener}s.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public final class LowLevelEventDispatcher
        implements java.io.Serializable {
    private final transient static Log log = LogFactory.getLog(LowLevelEventDispatcher.class);

    /**
     * The name prefix is stored in the
     * HashMap as key. The value is a Set (ArrayList) of {@link LowLevelEventListener}s
     */
    private final HashMap listeners = new HashMap();

    protected boolean namedEvents = true;

    public LowLevelEventDispatcher() {
    }

    public final void addLowLevelEventListener(LowLevelEventListener gl,
                                               String eventId) {
        List l = (List) listeners.get(eventId);
        if (l == null) {
            l = new ArrayList(2);
            l.add(gl);
            listeners.put(eventId, l);
        } else if (!l.contains(gl)) {
            l.add(gl);
        }
    }

    public final void removeLowLevelEventListener(LowLevelEventListener gl,
                                                  String eventId) {
        List l = (List) listeners.get(eventId);
        if (l != null) {
            l.remove(gl);
            if (l.isEmpty()) {
                listeners.remove(eventId);
            }
        }
    }

    /**
     * Returns list of registered low level event listener for the given event id.
     *
     * @param eventId The id (HTTP request parameter name) under which the listeners are registered.
     * @return A list of registered low level event listener for the given event id.
     */
    public final List getLowLevelEventListener(String eventId) {
        final List list = (List) listeners.get(eventId);
        return list != null ? Collections.unmodifiableList(list) : Collections.EMPTY_LIST;
    }

    /**
     * Register low level event listeners additionally by their component name as event id.
     * Used for purposes where you use fixed ids vs. dnymaically applied ids.
     *
     * @param registerListenerAlsoUnderName if <code>true</code> then components will also receieve
     *                                      HTTP values under their {@link org.wings.SComponent#getName()}
     *                                      in addition to {@link org.wings.LowLevelEventListener#getLowLevelEventId()}
     */
    public final void setNamedEvents(boolean registerListenerAlsoUnderName) {
        namedEvents = registerListenerAlsoUnderName;
    }

    /**
     * Registers a low level event listeners (for HTTP request processing).
     * <p/>
     * The NamePrefix of the listeners id is used as HTTP requestr parameter name. .
     *
     * @param gl listeners
     */
    public void register(LowLevelEventListener gl) {
        if (gl != null) {
            final String key = gl.getLowLevelEventId();
            final String name = gl.getName();

            log.debug("dispatcher: register id    '" + key + "' type: " + gl.getClass());
            addLowLevelEventListener(gl, key);

            if (namedEvents && (name != null) && !name.equals(key) && (name.length() > 0) ) {
                log.debug("dispatcher: register named '" + name + "'");
                addLowLevelEventListener(gl, name);
            }
        }
    }

    public void unregister(LowLevelEventListener gl) {
        if (gl == null) {
            return;
        }

        String key = gl.getLowLevelEventId();

        log.debug("unregister '" + key + "'");
        removeLowLevelEventListener(gl, key);

        key = gl.getName();
        if (key != null && key.trim().length() > 0) {
            log.debug("unregister named '" + key + "'");
            removeLowLevelEventListener(gl, key);
        }

    }

    /**
     * dispatch the events, encoded as [name/(multiple)values]
     * in the HTTP request.
     *
     * @param name
     * @param values
     * @return if the event has been dispatched
     */
    public boolean dispatch(String name, String[] values) {
        boolean result = false;
        int dividerIndex = name.indexOf(SConstants.UID_DIVIDER);
        String epoch = null;

        // no Alias
        if (dividerIndex > 0) {
            epoch = name.substring(0, dividerIndex);
            name = name.substring(dividerIndex + 1);
        }

        // make ImageButtons work in Forms .. browsers return
        // the click position as .x and .y suffix of the name
        if (name.endsWith(".x") || name.endsWith(".X")) {
            name = name.substring(0, name.length() - 2);
        } else if (name.endsWith(".y") || name.endsWith(".Y")) {
            // .. but don't process the same event twice.
            log.debug("discard '.y' part of image event");
            return false;
        }

        // does name contain underscores? Then use the part before the underscore for
        // identification of the low level event listener
        String id;
        dividerIndex = name.indexOf('_');
        if (dividerIndex > -1) {
            id = name.substring(0, dividerIndex);
        } else {
            id = name;
        }


        final List l = (List) listeners.get(id);
        if (l != null && l.size() > 0) {
            if (log.isDebugEnabled()) {
                log.debug("process event '" + epoch + SConstants.UID_DIVIDER + name + "'");
            }
            for (int i = 0; i < l.size(); ++i) {
                LowLevelEventListener gl = (LowLevelEventListener) l.get(i);
                if (gl.isEnabled()) {
                    if (checkEpoch(epoch, name, gl)) {
                        if (log.isDebugEnabled()) {
                            log.debug("process event '" + name + "' by " + gl.getClass() + "(" + gl.getLowLevelEventId() + ")");
                        }
                        gl.processLowLevelEvent(name, values);
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    protected boolean checkEpoch(String epoch, String name,
                                 LowLevelEventListener gl) {
        if (epoch != null) {
            SFrame frame = ((SComponent) gl).getParentFrame();
            if (frame == null) {
                if (log.isDebugEnabled()) {
                    log.debug("request for dangling component '" + epoch + SConstants.UID_DIVIDER + name);
                }
                unregister(gl);
                return false;
            }
            if (!epoch.equals(frame.getEventEpoch())) {
                if (log.isDebugEnabled()) {
                    log.debug("### got outdated event '" + epoch + SConstants.UID_DIVIDER + name
                            + "' from frame '" + frame.getName() + "'; expected epoch: " + frame.getEventEpoch());
                }
                frame.fireInvalidLowLevelEventListener(gl);
                return false;
            }
        }
        return true;
    }

    void clear() {
        listeners.clear();
    }
}


