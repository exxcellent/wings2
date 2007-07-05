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

import org.wings.Renderable;
import org.wings.io.Device;
import org.wings.resource.StringResource;
import org.wings.resource.HttpHeader;

import java.io.IOException;
import java.util.Collection;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 */
public class StringResourceExternalizer implements Externalizer<StringResource> {

    private static final Class[] SUPPORTED_CLASSES = {StringResource.class};

    public static final StringResourceExternalizer SHARED_INSTANCE = new StringResourceExternalizer();

    public String getId(StringResource obj) {
        return null;
    }

    public String getExtension(StringResource obj) {
        if (obj != null)
            return obj.getExtension();
        else
            return "";
    }

    public String getMimeType(StringResource obj) {
        if (obj != null)
            return obj.getMimeType();
        else
            return "unknown";
    }

    public int getLength(StringResource obj) {
        if (obj != null)
            return obj.getLength();
        return -1;
    }

    public boolean isFinal(StringResource obj) {
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

    public Collection<HttpHeader> getHeaders(StringResource obj) {
        if (obj != null)
            return obj.getHeaders();
        else
            return null;
    }
}


