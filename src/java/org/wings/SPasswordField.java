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
package org.wings;

import org.wings.plaf.PasswordFieldCG;

/**
 * A text input field widget which hides it's input. 
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 */
public class SPasswordField
        extends STextField {
    /**
     * Creates a new password field. This is basicly a textfield, but
     * the input from the user is not displayed; instead stars (*) are shown.
     *
     * @see org.wings.STextField
     */
    public SPasswordField() {
    }

    public void setCG(PasswordFieldCG cg) {
        super.setCG(cg);
    }
}


