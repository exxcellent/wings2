/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.resource;

import org.wings.StaticResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class FileResource extends StaticResource {

    private final File file;

    public FileResource(String name)
        throws IOException {
        this(new File(name));
    }

    public FileResource(File file) {
        this(file, null, "unknown");
    }

    public FileResource(File file, String ext, String mt) {
        super(ext, mt);
        this.file = file;
        if (extension == null) {
            int dotIndex = file.getName().lastIndexOf('.');
            if (dotIndex > -1) {
                extension = file.getName().substring(dotIndex + 1);
            }
        }
        try {
            size = (int) file.length();
        } catch (SecurityException ignore) {
        }
    }

    public String toString() {
        return getId() + (file != null ? " " + file.getName() : "");
    }

    public final File getFile() {
        return file;
    }

    protected final InputStream getResourceStream() throws IOException {
        return new FileInputStream(file);
    }
}
