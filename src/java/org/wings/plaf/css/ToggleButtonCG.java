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
package org.wings.plaf.css;

import org.wings.SAbstractButton;
import org.wings.util.SStringBuilder;
import org.wings.border.SBorder;
import org.wings.border.SDefaultBorder;
import org.wings.border.SEmptyBorder;

public final class ToggleButtonCG extends ButtonCG implements
        org.wings.plaf.ToggleButtonCG {

    private static final long serialVersionUID = 1L;

    /*
     * Check doc of superclass!
     */
    protected void updateAssignedCssClass(SAbstractButton button) {
        final SBorder border = button.getBorder();
        final String origStyle = button.getStyle();
        final boolean hasStandardBorder = (border == SDefaultBorder.DEFAULT || border instanceof SDefaultBorder);
        // is this a wingS border-styled button? If yes, then we need to do some css logic
        if (origStyle != null && origStyle.indexOf("SToggleButton") >= 0 && hasStandardBorder) {
            // create a cleaned copy without any SButton_xxx stuff
            SStringBuilder className = new SStringBuilder(origStyle.replaceAll("SToggleButton[a-z_A-Z]*",""));
            className.append(" SToggleButton");
            if (button.getShowAsFormComponent())
                className.append("_form");
            if (!button.isEnabled())
                className.append("_disabled");
            if (button.isSelected())
                className.append("_selected");
            button.setStyle(className.toString());       // bad! this trigger reloads, etc.
        }
    }
}
