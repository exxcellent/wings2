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

import org.wings.Renderable;
import org.wings.io.Device;
import org.wings.style.StyleSheet;

import java.util.Collection;

/**
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class StyleSheetExternalizer implements Externalizer {

    public static final StyleSheetExternalizer SHARED_INSTANCE = new StyleSheetExternalizer();

    private static final Class[] SUPPORTED_CLASSES = {StyleSheet.class};
    private static final String[] SUPPORTED_MIME_TYPES = {"text/css"};

    public String getExtension(Object obj) {
        return "css";
    }

    public String getMimeType(Object obj) {
        return "text/css";
    }

    public boolean isFinal(Object obj) {
        return ((StyleSheet) obj).isFinal();
    }

    public int getLength(Object obj) {
        return -1;
    }

    public void write(Object obj, Device out)
            throws java.io.IOException {
        ((Renderable) obj).write(out);
    }

    public Class[] getSupportedClasses() {
        return SUPPORTED_CLASSES;
    }

    public String[] getSupportedMimeTypes() {
        return SUPPORTED_MIME_TYPES;
    }

    public Collection getHeaders(Object obj) {
        return null;
    }
}


