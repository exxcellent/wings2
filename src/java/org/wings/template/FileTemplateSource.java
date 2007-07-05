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
package org.wings.template;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * A <CODE>FileDataSource</CODE> implements a TemplateSource
 * for a file.
 *
 * @author <A href="mailto:H.Zeller@acm.org">Henner Zeller</A>
 */
public class FileTemplateSource implements TemplateSource, Serializable {
    private File file;
    protected String canonicalName = null;

    public FileTemplateSource(File f) {
        this.file = f;
        if (file != null) {
            try {
                canonicalName = "file:" + file.getCanonicalPath();
            } catch (IOException e) {
                // should never happen for files ..
            }
        }
    }

    /**
     * Returns a canonical name of this DataSource.
     */
    public String getCanonicalName() {
        return canonicalName;
    }

    /**
     * Returns the time the content of this File
     * was last modified.
     * <p/>
     * The return value is used to decide whether to reparse a
     * Source or not. Reparsing is done if the value returned
     * here differs from the value returned at the last processing
     * time.
     *
     * @return long a modification time
     */
    public long lastModified() {
        return file.lastModified();
    }

    /**
     * Gets an InputStream of the File.
     */
    public InputStream getInputStream()
            throws IOException {
        return new FileInputStream(file);
    }
}




