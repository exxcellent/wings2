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
package org.wings.resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.Resource;
import org.wings.SFrame;
import org.wings.io.Device;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Traverses the component hierarchy of a frame and lets the CGs compose
 * the document.
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class DynamicCodeResource
        extends DynamicResource {
    private final transient static Log log = LogFactory.getLog(DynamicCodeResource.class);

    private static final ArrayList DEFAULT_CODE_HEADER = new ArrayList();

    static {
        DEFAULT_CODE_HEADER.add(new Resource.HeaderEntry("Expires", new Date(1000)));
        DEFAULT_CODE_HEADER.add(new Resource.HeaderEntry("Cache-Control", "no-store, no-cache, must-revalidate"));
        DEFAULT_CODE_HEADER.add(new Resource.HeaderEntry("Cache-Control", "post-check=0, pre-check=0"));
        DEFAULT_CODE_HEADER.add(new Resource.HeaderEntry("Pragma", "no-cache"));
    }


    /**
     * Create a code resource for the specified frame.
     */
    public DynamicCodeResource(SFrame f) {
        super(f, null, provideMimeType(f));
    }

    private static String provideMimeType(SFrame frame) {
        return "text/html; charset=" + frame.getSession().getCharacterEncoding();
    }

    /**
     * Write the code of the whole frame to the Device.
     */
    public void write(Device out)
            throws IOException {
        try {
            getFrame().write(out);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            log.fatal("resource: " + getId(), e);
            throw new IOException(e.getMessage()); // UndeclaredThrowable
        }
    }

    public Collection getHeaders() {
        return DEFAULT_CODE_HEADER;
    }
}


