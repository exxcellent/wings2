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
package org.wings.externalizer;

import org.wings.Resource;
import org.wings.io.Device;

import java.io.IOException;
import java.util.Collection;

/**
 * @author armin
 *         created at 24.02.2004 10:49:32
 */
public class ResourceExternalizer implements Externalizer {

    private static final Class[] SUPPORTED_CLASSES = {Resource.class};

    public static final ResourceExternalizer SHARED_INSTANCE = new ResourceExternalizer();

    public String getExtension(Object obj) {
        return ((Resource) obj).getExtension();
    }

    public String getMimeType(Object obj) {
        return ((Resource) obj).getMimeType();
    }

    public int getLength(Object obj) {
        return ((Resource) obj).getLength();
    }

    public boolean isFinal(Object obj) {
        return false;
    }

    public void write(Object obj, Device out)
            throws IOException {
        ((Resource) obj).write(out);
    }

    public Class[] getSupportedClasses() {
        return SUPPORTED_CLASSES;
    }

    public String[] getSupportedMimeTypes() {
        return null;
    }

    public Collection getHeaders(Object obj) {
        return ((Resource) obj).getHeaders();
    }

}
