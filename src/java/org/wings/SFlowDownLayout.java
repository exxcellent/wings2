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



/**
 * This is a flow down layout. This is a flow layout
 * with vertical orientation and left alignment.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SFlowDownLayout extends SFlowLayout
{
    /**
     * creates a new flow down layouts
     */
    public SFlowDownLayout() {
        super();
        setOrientation(SConstants.VERTICAL);
        setAlignment(SConstants.LEFT_ALIGN);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
