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

import org.wings.SComponent;
import org.wings.STemplateLayout;
import org.wings.io.Device;
import org.wings.io.DeviceOutputStream;
import org.wings.template.parser.ParseContext;

import java.io.OutputStream;

/**
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 */
public final class TemplateParseContext implements ParseContext {
    private final OutputStream myOut;
    private final Device sink;
    private final STemplateLayout layout;

    public TemplateParseContext(final Device sink, STemplateLayout layout) {
        this.sink = sink;
        this.layout = layout;
        myOut = new DeviceOutputStream(sink);
    }

    public OutputStream getOutputStream() {
        return myOut;
    }

    public void startTag(int number) {
    }

    public void doneTag(int number) {
    }

    /*
     * important for the template: the components write to this sink
     */
    public Device getDevice() {
        return sink;
    }

    public SComponent getComponent(String name) {
        return layout.getComponent(name);
    }
}


