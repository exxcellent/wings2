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
import org.wings.StaticResource;
import org.wings.resource.ClassPathResource;
import org.wings.resource.NamedResource;
import org.wings.resource.HttpHeader;
import org.wings.io.Device;

import java.io.IOException;
import java.util.Collection;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 */
public class StaticResourceExternalizer implements Externalizer<StaticResource> {

    private static final Class[] SUPPORTED_CLASSES = {StaticResource.class};

    public static final StaticResourceExternalizer SHARED_INSTANCE = new StaticResourceExternalizer();

    public String getId(StaticResource obj) {
        if (obj instanceof NamedResource)
            return ((NamedResource)obj).getResourceName();
        else
            return null;
    }

    public String getExtension(StaticResource obj) {
        if (obj instanceof NamedResource)
            return null;
        else
            return obj.getExtension();
    }

    public String getMimeType(StaticResource obj) {
        if (obj != null)
            return obj.getMimeType();
        else
            return "unknown";
    }

    public int getLength(StaticResource obj) {
        if (obj != null)
            return obj.getLength();
        return -1;
    }

    public boolean isFinal(StaticResource obj) {
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

    public Collection<HttpHeader> getHeaders(StaticResource obj) {
        if (obj != null)
            return obj.getHeaders();
        else
            return null;
    }
}


