/*
 */

/*
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

import org.wings.io.Device;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;

public class TextExternalizer implements Externalizer {

    private static final Class[] SUPPORTED_CLASSES = {String.class};

    protected String extension;
    protected String mimeType;
    protected final String[] supportedMimeTypes = new String[1];


    public TextExternalizer(String mimeType, String extension) {
        this.mimeType = mimeType;
        this.extension = extension;

        supportedMimeTypes[0] = mimeType;
    }

    public TextExternalizer(String mimeType) {
        this(mimeType, "txt");
    }

    public TextExternalizer() {
        this("text/plain", "txt");
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getId(Object obj) {
        return null;
    }

    public String getExtension(Object obj) {
        return extension;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType(Object obj) {
        return mimeType;
    }

    public boolean isFinal(Object obj) {
        return true;
    }

    public int getLength(Object obj) {
        return -1;
    }

    public void write(Object obj, Device out)
            throws java.io.IOException {
        Reader reader = new StringReader((String) obj);
        char[] buffer = new char[2048];
        int num;
        while ((num = reader.read(buffer)) > 0) {
            out.print(buffer, 0, num);
        }
        reader.close();
    }

    public Class[] getSupportedClasses() {
        return SUPPORTED_CLASSES;
    }

    public String[] getSupportedMimeTypes() {
        return supportedMimeTypes;
    }

    public Collection getHeaders(Object obj) {
        return null;
    }
}
