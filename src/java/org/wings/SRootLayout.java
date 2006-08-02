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
package org.wings;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.session.SessionManager;
import org.wings.session.BrowserType;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Default layout for all {@link SRootContainer} derivates.
 * <p/>
 * There might be situations when you want to use a custom SRootLayout. Why?
 * Because by setting an SFrame's layout you can change the look of a whole
 * application. Since only the object called "content" will be replaced with
 * the content element, you can statically define XHTML elements such as a
 * border, a title or a footer line by defining your own template with a STemplateLayout.
 */
public class SRootLayout extends STemplateLayout {
    private final static transient Log log = LogFactory.getLog(SRootLayout.class);

    // The layout states.
    private final static int NO_LAYOUT = -1;
    private final static int FRAME = 0;
    private final static int DIALOG = 1;

    // No layout was loaded on initial time.
    private int currentTemplate = NO_LAYOUT;

    // The components managed by this layout.
    private int componentCount = 0;

    /**
     * Use the default template.
     */
    public SRootLayout() {
        // Template will be loaded in changeLayout() if
        // required.
    }

    /**
     * Read the template from a file.
     *
     * @throws java.io.IOException
     */
    public SRootLayout(String tmplFileName) throws IOException {
        setTemplate(new File(tmplFileName));
    }

    /**
     * Read the template from a file.
     *
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

    public void addComponent(SComponent c, Object constraint, int index) {
        componentCount++;
        changeLayout();
    }

    public void removeComponent(SComponent c) {
        componentCount--;
        changeLayout();
    }

    public SComponent getComponent(String name) {
        if ("frame".equals(name)) {
            return container.getComponent(0);
        }
        else if ("dialog".equals(name)) {
            int topmost = container.getComponentCount() - 1;
            return container.getComponent(topmost);
        }

        return null;
    }

    // this has been overridden as noop in STemplateLayout
    // give it back the original behaviour
    public void setContainer(SContainer container) {
        this.container = container;
    }

    /**
     * Changes the layout to display the frame on top or
     * display modal dialogs if pushed on frame.
     */
    private void changeLayout() {

        if ((components.size() + componentCount) <= 1) {
            if (SRootLayout.FRAME != currentTemplate) {
                try {
                    setTemplate(Thread.currentThread().getContextClassLoader().getResource("org/wings/template/default.thtml"));
                }
                catch (IOException e) {
                    log.error("Unable to get template/default.thtml", e);
                }
            }
        }
        else {
            if (SRootLayout.DIALOG != currentTemplate) {
                if (BrowserType.IE.equals(SessionManager.getSession().getUserAgent().getBrowserType())) {
                    try {
                        setTemplate(Thread.currentThread().getContextClassLoader().getResource("org/wings/template/default_dialog_ie.thtml"));
                    }
                    catch (IOException e) {
                        log.error("Unable to get template/default_dialog_ie.thtml", e);
                    }
                }
                else {
                    try {
                        setTemplate(Thread.currentThread().getContextClassLoader().getResource("org/wings/template/default_dialog.thtml"));
                    }
                    catch (IOException e) {
                        log.error("Unable to get template/default_dialog.thtml", e);
                    }
                }
            }
        }
    }
}
