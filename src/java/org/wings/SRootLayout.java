/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SRootLayout
        extends STemplateLayout {
    /**
     * Use the default template.
     */
    public SRootLayout() {
        try {
            setTemplate(getClass().getResource("template/default.thtml"));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    /**
     * Read the template from a file.
     *
     * @param tmplFileName
     * @throws java.io.IOException
     */
    public SRootLayout(String tmplFileName) throws IOException {
        setTemplate(new File(tmplFileName));
    }

    /**
     * Read the template from a file.
     *
     * @param tmplFile
     * @throws java.io.IOException
     */
    public SRootLayout(File tmplFile) throws IOException {
        setTemplate(tmplFile);
    }

    /**
     * Read the template from an URL.
     * The content is cached.
     */
    public SRootLayout(URL url) throws java.io.IOException {
        setTemplate(url);
    }

    public void addComponent(SComponent c, Object constraint, int index) {}

    public void removeComponent(SComponent comp) {}

    public SComponent getComponent(String name) {
        if (!"content".equals(name))
            return null;

        int topmost = container.getComponentCount() - 1;
        return container.getComponent(topmost);
    }

    // this has been overridden as noop in STemplateLayout
    // give it back the original behaviour
    public void setContainer(SContainer container) {
        this.container = container;
    }
}
