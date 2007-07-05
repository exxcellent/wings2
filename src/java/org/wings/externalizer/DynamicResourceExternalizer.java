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
package org.wings.externalizer;

import org.wings.io.Device;
import org.wings.resource.DynamicResource;
import org.wings.resource.HttpHeader;

import java.io.IOException;
import java.util.Collection;

/**
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 */
public class DynamicResourceExternalizer implements Externalizer<DynamicResource> {

    private static final Class[] SUPPORTED_CLASSES = {DynamicResource.class};

    public static final DynamicResourceExternalizer SHARED_INSTANCE = new DynamicResourceExternalizer();

    public String getId(DynamicResource obj) {
        return null;
    }

    public String getExtension(DynamicResource obj) {
        if (obj != null)
            return obj.getExtension();
        else
            return "";
    }

    public String getMimeType(DynamicResource obj) {
        if (obj != null) {
            return obj.getMimeType();
        } else
            return "unknown";
    }

    public int getLength(DynamicResource obj) {
        return -1;
    }

    public boolean isFinal(DynamicResource obj) {
        return false;
    }

    public String getEventEpoch(DynamicResource obj) {
        return obj.getFrame().getEventEpoch();
    }

    public void write(Object obj, Device out)
            throws IOException {
        ((DynamicResource)obj).write(out);
    }

    public Class[] getSupportedClasses() {
        return SUPPORTED_CLASSES;
    }

    public String[] getSupportedMimeTypes() {
        return null;
    }

    public Collection<HttpHeader> getHeaders(DynamicResource obj) {
        if (obj != null)
            return obj.getHeaders();
        else
            return null;
    }
}


