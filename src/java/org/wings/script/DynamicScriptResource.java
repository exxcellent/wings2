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

package org.wings.script;

import org.wings.resource.DynamicResource;
import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.SFrame;
import org.wings.resource.DynamicResource;
import org.wings.io.Device;
import org.wings.util.ComponentVisitor;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Traverses the component hierarchy of a frame and gathers the scripts of
 * the ScriptListeners, that are attached to the components.
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class DynamicScriptResource extends DynamicResource {
    public DynamicScriptResource(SFrame frame) {
        super(frame, "js", "application/x-javascript");
    }

    public void write(Device out)
            throws IOException {
        try {
            ScriptWriter visitor = new ScriptWriter(out);
            getFrame().invite(visitor);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e.getMessage()); // UndeclaredThrowable
        }
    }

    protected static class ScriptWriter
            implements ComponentVisitor {
        Device out;
        Set set = new HashSet();

        public ScriptWriter(Device out) {
            this.out = out;
        }

        private void writeListenersFrom(SComponent component)
                throws IOException {
            ScriptListener[] listeners = component.getScriptListeners();
            if (listeners.length == 0)
                return;

            for (int i = 0; i < listeners.length; i++) {
                ScriptListener listener = listeners[i];
                if (listener.getScript() != null) {
                    if (set.contains(listener.getScript()))
                        continue;
                    set.add(listener.getScript());
                    out.print("\n");
                    out.print("// ");
                    out.print(component.getComponentId());
                    out.print(".");
                    out.print(listener.getEvent());
                    out.print("\n");
                    out.print(listener.getScript());
                }
            }
        }

        public void visit(SComponent component) throws IOException {
            writeListenersFrom(component);
        }

        public void visit(SContainer container) throws Exception {
            writeListenersFrom(container);
            container.inviteEachComponent(this);
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
