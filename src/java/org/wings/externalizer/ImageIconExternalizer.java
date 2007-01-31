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

import java.awt.*;

import org.wings.io.Device;

import javax.swing.*;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:mreinsch@to.com">Michael Reinsch</a>
 */
public class ImageIconExternalizer extends ImageExternalizer {
    private static final Class[] SUPPORTED_CLASSES = {ImageIcon.class};

    public static final ImageIconExternalizer SHARED_GIF_INSTANCE = new ImageIconExternalizer(FORMAT_GIF);
    public static final ImageIconExternalizer SHARED_PNG_INSTANCE = new ImageIconExternalizer(FORMAT_PNG);
    public static final ImageIconExternalizer SHARED_JPG_INSTANCE = new ImageIconExternalizer(FORMAT_JPG);


    public ImageIconExternalizer() {
        super();
    }

    public ImageIconExternalizer(String format) {
        super(format);
    }

    public Class[] getSupportedClasses() {
        return SUPPORTED_CLASSES;
    }

    @Override
    public void write(Object obj, Device out)
            throws java.io.IOException {
        super.write(((ImageIcon) obj).getImage(), out);
    }
}


