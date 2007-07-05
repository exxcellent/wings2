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
import org.wings.resource.HttpHeader;
import org.wings.io.Device;
import org.wings.style.StyleSheet;

import java.util.Collection;

/**
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 */
public class StyleSheetExternalizer implements Externalizer<StyleSheet> {

    public static final StyleSheetExternalizer SHARED_INSTANCE = new StyleSheetExternalizer();

    private static final Class[] SUPPORTED_CLASSES = {StyleSheet.class};
    private static final String[] SUPPORTED_MIME_TYPES = {"text/css"};

    public String getId(StyleSheet obj) {
        return null;
    }

    public String getExtension(StyleSheet obj) {
        return "css";
    }

    public String getMimeType(StyleSheet obj) {
        return "text/css";
    }

    public boolean isFinal(StyleSheet obj) {
        return ((StyleSheet) obj).isFinal();
    }

    public int getLength(StyleSheet obj) {
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

    public Collection<HttpHeader> getHeaders(StyleSheet obj) {
        return null;
    }
}


