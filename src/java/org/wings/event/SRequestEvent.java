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

import org.wings.externalizer.ExternalizedResource;

import java.awt.*;

/**
 * A request event meaning aspecific phase that has been reached during the request processing.
 * Possible states are defined as constants in this class (i.e. {@link #DELIVER_START}).
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class SRequestEvent extends AWTEvent {

    /**
     * Delivery of the HTML response started.
     */
    public static final int DELIVER_START = 50000;


    /**
     * Delivery of the HTML response finished.
     */
    public static final int DELIVER_DONE = DELIVER_START + 1;

    /**
     * Dispathcing of the low level events contained in the originating servlet request starts.
     * This will trigger i.e. button events later on.
     */

    public static final int DISPATCH_START = DELIVER_START + 2;

    /**
     * All low level events have been dispatches and hence the immediate event should
     * have been fired. Intermediate events will be fired soon.
     */
    public static final int DISPATCH_DONE = DELIVER_START + 3;


    /**
     * The initial request paramters have been processed.
     */
    public static final int PROCESS_REQUEST = DELIVER_START + 4;

    /**
     * The http request started.
     */
    public static final int REQUEST_START = DELIVER_START + 5;


    /**
     * The http request ist finished.
     */
    public static final int REQUEST_END = DELIVER_START + 6;

    /**
     * if type is {@link #DELIVER_START} or {@link #DELIVER_DONE} this field is
     * filled with info about the resource actually delivered, otherwise it is
     * null.
     */
    protected transient ExternalizedResource requestedResource;

    /**
     * Constructs a ComponentEvent object.
     *
     * @param aSource the Component object that originated the event
     * @param type    an integer indicating the type of event
     */
    public SRequestEvent(Object aSource, int type, ExternalizedResource requestedResource) {
        super(aSource, type);

        this.requestedResource = requestedResource;
    }


    public int getType() {
        return getID();
    }


    public ExternalizedResource getRequestedResource() {
        return requestedResource;
    }

    /**
     * Returns a string representing the state of this event. This
     * method is intended to be used only for debugging purposes, and the
     * content and format of the returned string may vary between
     * implementations. The returned string may be empty but may
     * not be <tt>null</tt>.
     */
    public String paramString() {
        if (getSource() == null)
            return "no source";

        String typeStr;

        switch (getID()) {
            case DISPATCH_START:
                typeStr = "DISPATCH_START";
                break;
            case DISPATCH_DONE:
                typeStr = "DISPATCH_DONE";
                break;
            case DELIVER_START:
                typeStr = "DELIVER_START";
                break;
            case DELIVER_DONE:
                typeStr = "DELIVER_DONE";
                break;
            default:
                typeStr = "unknown type";
        }
        return typeStr;
    }

    public String toString() {
        return "SRequestEvent[source=" + source + "; " + paramString() + "]";
    }
}
