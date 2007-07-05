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

import java.awt.*;
import java.util.Collection;

import org.wings.io.Device;
import org.wings.resource.HttpHeader;

import javax.swing.*;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 */
public class ImageIconExternalizer implements Externalizer<ImageIcon> {
    ImageExternalizer delegate;

    private static final Class[] SUPPORTED_CLASSES = { ImageIcon.class };

    public static final ImageIconExternalizer SHARED_GIF_INSTANCE = new ImageIconExternalizer(ImageExternalizer.FORMAT_GIF);
    public static final ImageIconExternalizer SHARED_PNG_INSTANCE = new ImageIconExternalizer(ImageExternalizer.FORMAT_PNG);
    public static final ImageIconExternalizer SHARED_JPG_INSTANCE = new ImageIconExternalizer(ImageExternalizer.FORMAT_JPG);


    public ImageIconExternalizer() {
        delegate = new ImageExternalizer();
    }

    public ImageIconExternalizer(String format) {
        delegate = new ImageExternalizer(format);
    }

    public Class[] getSupportedClasses() {
        return SUPPORTED_CLASSES;
    }

    public String[] getSupportedMimeTypes() {
        return delegate.getSupportedMimeTypes();
    }

    public Collection<HttpHeader> getHeaders(ImageIcon obj) {
        return delegate.getHeaders(obj.getImage());
    }

    public String getId(ImageIcon obj) {
        return delegate.getId(obj.getImage());
    }

    public String getExtension(ImageIcon obj) {
        return delegate.getExtension(obj.getImage());
    }

    public String getMimeType(ImageIcon obj) {
        return delegate.getMimeType(obj.getImage());
    }

    public int getLength(ImageIcon obj) {
        return delegate.getLength(obj.getImage());
    }

    public boolean isFinal(ImageIcon obj) {
        return false;
    }

    public void write(Object obj, Device out)
            throws java.io.IOException {
        delegate.write(((ImageIcon) obj).getImage(), out);
    }
}


