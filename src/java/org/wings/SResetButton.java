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

package org.wings;

import org.wings.plaf.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SResetButton
    extends SAbstractButton
{
    /**
     * TODO: documentation
     *
     * @param text
     */
    public SResetButton(String text) {
        super(text);
        
        setType(RESET_BUTTON);
    }

    /**
     * TODO: documentation
     *
     */
    public SResetButton() {
        this("RESET");
    }

    /**
     * TODO: documentation
     *
     * @param t
     */
    public void setType(String t) {
        if ( !RESET_BUTTON.equals(t) )
            throw new IllegalArgumentException("type change not supported, type is fix: resetbutton");
        
        super.setType(t);
    }

    public void setCG(ResetButtonCG cg) {
        super.setCG(cg);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
