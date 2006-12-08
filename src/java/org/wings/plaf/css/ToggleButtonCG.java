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

import org.wings.SIcon;
import org.wings.SToggleButton;
import org.wings.plaf.Update;


public final class ToggleButtonCG extends ButtonCG implements
        org.wings.plaf.ToggleButtonCG {

    private static final long serialVersionUID = 1L;

    public Update updateText(SToggleButton toggleButton, String text) {
        return new TextUpdate(toggleButton, text);
    }

    public Update updateIcon(SToggleButton toggleButton, SIcon icon) {
        return new IconUpdate(toggleButton, icon);
    }

}