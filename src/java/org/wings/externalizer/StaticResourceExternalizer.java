/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.externalizer;

import org.wings.Renderable;
import org.wings.StaticResource;
import org.wings.io.Device;

import java.io.IOException;
import java.util.Collection;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 * @version $Revision$
 */
public class StaticResourceExternalizer implements Externalizer {

    private static final Class[] SUPPORTED_CLASSES = {StaticResource.class};

    public static final StaticResourceExternalizer SHARED_INSTANCE = new StaticResourceExternalizer();

    public String getExtension(Object obj) {
        if (obj != null)
            return ((StaticResource) obj).getExtension();
        else
            return "";
    }

    public String getMimeType(Object obj) {
        if (obj != null)
            return ((StaticResource) obj).getMimeType();
        else
            return "unknown";
    }

    public int getLength(Object obj) {
        if (obj != null)
            return ((StaticResource) obj).getLength();
        return -1;
    }

    public boolean isFinal(Object obj) {
        return true;
    }

    public void write(Object obj, Device out)
            throws IOException {
        ((Renderable) obj).write(out);
    }

    public Class[] getSupportedClasses() {
        return SUPPORTED_CLASSES;
    }

    public String[] getSupportedMimeTypes() {
        return null;
    }

    public Collection getHeaders(Object obj) {
        if (obj != null)
            return ((StaticResource) obj).getHeaders();
        else
            return null;
    }
}


