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

import org.wings.SURLIcon;
import org.wings.SComponent;
import org.wings.SLabel;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SLabelPropertyManager
    extends SComponentPropertyManager
{
    static final Class[] classes = {SLabel.class};

    public SLabelPropertyManager() {
    }

    public void setProperty(SComponent comp, String name, String value) {
        SLabel c = (SLabel)comp;
        if ( name.equals("ICON") )
            c.setIcon(new SURLIcon(value));
        else if ( name.equals("TEXT") )
            c.setText(value);
        else
            super.setProperty(comp, name, value);
    }

    public Class[] getSupportedClasses() {
        return classes;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
