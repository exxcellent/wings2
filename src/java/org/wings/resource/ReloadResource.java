/*
 * $Id$
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
package org.wings.resource;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.Resource;
import org.wings.SFrame;
import org.wings.io.Device;

/**
 * Traverses the component hierarchy of a frame and lets the CGs compose
 * the document.
 *
 * @author Holger Engels
 * @version $Revision$
 */
public class ReloadResource extends DynamicResource {
    private final static Log log = LogFactory.getLog(ReloadResource.class);

    private static final ArrayList<HttpHeader> DEFAULT_CODE_HEADER = new ArrayList<HttpHeader>();
    private final PropertyChangeListener changeListener;

    static {
        DEFAULT_CODE_HEADER.add(new Resource.HeaderEntry("Expires", new Date(1000)));
        DEFAULT_CODE_HEADER.add(new Resource.HeaderEntry("Cache-Control", "no-store, no-cache, must-revalidate"));
        DEFAULT_CODE_HEADER.add(new Resource.HeaderEntry("Cache-Control", "post-check=0, pre-check=0"));
        DEFAULT_CODE_HEADER.add(new Resource.HeaderEntry("Pragma", "no-cache"));
    }


    /**
     * Create a code resource for the specified frame.
     * <p>The MIME-type for this frame will be <code>text/html; charset=<i>current encoding</i></code>
     */
    public ReloadResource(final SFrame f) {
        super(f, "html", provideMimeType(f));
        // update session encoding if manually updated in the session.
        changeListener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                mimeType = provideMimeType(f);
            }
        };
        f.getSession().addPropertyChangeListener(changeListener);
    }

    /**
     * The MIME-type for this {@link Resource}.
     */
    private static String provideMimeType(SFrame frame) {
        return "text/html; charset=" + frame.getSession().getCharacterEncoding();
    }

    /**
     * Renders and writes the code of the {@link SFrame} attached to this <code>ReloadResource</code>.
     */
    public void write(Device out) throws IOException {
        try {
        	getFrame().write(out);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            log.fatal("resource: " + getId(), e);
            throw new IOException(e.getMessage()); // UndeclaredThrowable
        }
    }

    /**
     * The HTTP header parameteres attached to this dynamic code ressource.
     * This <b>static</b> list will by default contain entries to disable caching
     * on the server side. Call <code>getHeaders().clear()</code> to avoid this
     * i.e. if you want to enable back buttons.
     *
     * @return A <code>Collection</code> of {@link org.wings.Resource.HeaderEntry} objects.
     */
    public Collection<HttpHeader> getHeaders() {
        if (getFrame().isNoCaching())
            return DEFAULT_CODE_HEADER;
        else
            return Collections.EMPTY_SET;
    }
}


