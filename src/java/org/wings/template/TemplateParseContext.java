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

package org.wings.template;

import java.io.OutputStream;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import org.wings.io.Device;
import org.wings.io.DeviceOutputStream;

import org.wings.SComponent;
import org.wings.STemplateLayout;

import org.wings.template.parser.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 * @version $Revision$
 */
public final class TemplateParseContext implements ParseContext
{
    private final OutputStream myOut;
    private final Device sink;
    private final STemplateLayout layout;

    public TemplateParseContext (final Device sink, STemplateLayout layout) {
        this.sink = sink;
        this.layout = layout;
        myOut = new DeviceOutputStream (sink);
    }

    public OutputStream getOutputStream () {
        return myOut;
    }

    public void startTag (int number) {
    }

    public void doneTag (int number) {
    }

    /*
     * important for the template: the components write to this sink
     */
    public Device getDevice () {
        return sink;
    }

    public SComponent getComponent (String name) {
        return layout.getComponent(name);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
