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
package org.wings;


/**
 * A {@link SFlowLayout} with vertical orientation.
 * <p/>
 * The container to which this layout is applied will preferrably keep it's natural dimension.
 * Apply {@link SDimension#FULLWIDTH} as prefererred size on the container
 * to take up the full width.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SFlowDownLayout extends SFlowLayout {
    /**
     * creates a new flow down layouts
     */
    public SFlowDownLayout() {
        super();
    }

}


