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
package org.wings.plaf.css;

import org.wings.script.JavaScriptListener;
import org.wings.SComponent;

/**
 * Listenerer to
 *
 * @author hengels
 * @version $Revision$
 */
class FocusScriptListener extends JavaScriptListener {

    private final SComponent focusComponent;

    public FocusScriptListener(SComponent setFocusOnLoadOntoComponent) {
        super("onload", "requestFocus('" + setFocusOnLoadOntoComponent.getName() + "')");
        this.focusComponent = setFocusOnLoadOntoComponent;
    }

    public SComponent getFocusComponent() {
        return focusComponent;
    }
}
